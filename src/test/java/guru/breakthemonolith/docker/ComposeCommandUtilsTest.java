package guru.breakthemonolith.docker;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

@RunWith(MockitoJUnitRunner.class)
public class ComposeCommandUtilsTest {

	private static final String TEST_YAML = "src/test/composeYml/test1.yaml";

	@Mock
	private Logger loggerMock;

	private ArgumentCaptor<String> commandStr;

	private DockerComposeConfiguration config;

	@Before
	public void setUp() throws Exception {
		FieldUtils.writeStaticField(CommandUtils.class, "logger", loggerMock, true);
		commandStr = ArgumentCaptor.forClass(String.class);

		config = new DockerComposeConfiguration(TEST_YAML);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testComposeListing() throws Exception {
		ComposeCommandUtils.composeListing(config);
		Mockito.verify(loggerMock).info(Matchers.anyString(), commandStr.capture());
		Assert.assertTrue(commandStr.getValue().contains("ps"));
	}

	@Test
	public void testComposeConfig() throws Exception {
		ComposeCommandUtils.composeConfig(config);
		Mockito.verify(loggerMock).info(Matchers.anyString(), commandStr.capture());
		Assert.assertTrue(commandStr.getValue().contains("config"));
	}

	@Test
	public void testComposeUpDown() throws Exception {
		ComposeCommandUtils.composeUp(config);
		Mockito.verify(loggerMock).info(Matchers.anyString(), commandStr.capture());
		Assert.assertTrue(commandStr.getValue().contains("up -d"));

		Mockito.reset(loggerMock);
		ComposeCommandUtils.composeDown(config);
		Mockito.verify(loggerMock).info(Matchers.anyString(), commandStr.capture());
		Assert.assertTrue(commandStr.getValue().contains("down"));
	}

	@Test
	public void testCreateCommandArrayNoProject() throws Exception {
		String[] commandarray = (String[]) MethodUtils.invokeMethod(new ComposeCommandUtils(), true,
				"createCommandArray", config);
		Assert.assertEquals(3, commandarray.length);
		Assert.assertEquals("docker-compose", commandarray[0]);
		Assert.assertEquals("-f", commandarray[1]);
		Assert.assertEquals(config.getConfigYamlFileName(), commandarray[2]);
	}

	@Test
	public void testCreateCommandArrayWithProject() throws Exception {
		config.setProjectName("testProject");
		String[] commandarray = (String[]) MethodUtils.invokeMethod(new ComposeCommandUtils(), true,
				"createCommandArray", config);

		Assert.assertEquals(5, commandarray.length);
		Assert.assertEquals("-p", commandarray[3]);
		Assert.assertEquals(config.getProjectName(), commandarray[4]);
	}

	@Test(expected = NullPointerException.class)
	public void testCreateCommandArrayNoConfig() throws Exception {
		try {
			MethodUtils.invokeMethod(new ComposeCommandUtils(), true,
					"createCommandArray", (DockerComposeConfiguration) null);
		} catch (Exception e) {
			throw (Exception) e.getCause();
		}
	}

	@Test(expected = NullPointerException.class)
	public void testCreateCommandArrayNullYaml() throws Exception {
		config.setConfigYamlFileName(null);
		try {
			MethodUtils.invokeMethod(new ComposeCommandUtils(), true,
				"createCommandArray", config);
		} catch (Exception e) {
			throw (Exception) e.getCause();
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateCommandArrayBlankYaml() throws Exception {
		config.setConfigYamlFileName("");
		try {
			MethodUtils.invokeMethod(new ComposeCommandUtils(), true,
				"createCommandArray", config);
		} catch (Exception e) {
			throw (Exception) e.getCause();
		}
	}

}
