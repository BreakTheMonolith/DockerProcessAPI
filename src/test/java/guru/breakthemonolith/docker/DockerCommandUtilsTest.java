package guru.breakthemonolith.docker;

import org.apache.commons.lang3.reflect.FieldUtils;
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
import org.slf4j.LoggerFactory;

@RunWith(MockitoJUnitRunner.class)
public class DockerCommandUtilsTest {

	@Mock
	private Logger loggerMock;

	private ArgumentCaptor<String> logArgStr;

	@Before
	public void setUp() throws Exception {
		FieldUtils.writeStaticField(DockerCommandUtils.class, "logger", loggerMock, true);
		FieldUtils.writeStaticField(CommandUtils.class, "logger", loggerMock, true);
		logArgStr = ArgumentCaptor.forClass(String.class);
	}

	@After
	public void tearDown() throws Exception {
		FieldUtils.writeStaticField(DockerCommandUtils.class, "logger",
				LoggerFactory.getLogger(DockerCommandUtils.LOGGER_LABEL), true);
	}

	@Test(expected = DockerProcessAPIException.class)
	public void testDockerPull() throws Exception {
		DockerCommandUtils.dockerPull("non-existent");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDockerPullBlankImage() throws Exception {
		DockerCommandUtils.dockerPull("");
	}

	@Test(expected = DockerProcessAPIException.class)
	public void testDockerKillContainer() throws Exception {
		DockerCommandUtils.dockerKillContainer("foo");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDockerKillContainerBlankImage() throws Exception {
		DockerCommandUtils.dockerKillContainer("");
	}

	@Test
	public void testDockerContainerListing() throws Exception {
		DockerCommandUtils.dockerContainerListing();
		Mockito.verify(loggerMock).info(logArgStr.capture());
		Assert.assertTrue(logArgStr.getValue().contains("COMMAND"));
	}

	@Test
	public void testDockerRunInstream() throws Exception {
		ArgumentCaptor<String> commandStr = ArgumentCaptor.forClass(String.class);

		DockerRunSpecification spec = new DockerRunSpecification("hello-world");
		spec.setDetachInd(false);
		spec.getPortMap().put("8080", "8080");
		spec.getEnvironmentMap().put("TESTENV", "FOO");
		spec.getVolumeMap().put("C:\\", "/data");

		DockerCommandUtils.dockerRun(spec);
		Mockito.verify(loggerMock).info(Matchers.anyString(), commandStr.capture());
		
		System.out.println(commandStr.getValue());
		Assert.assertTrue(commandStr.getValue().contains("8080"));
		Assert.assertTrue(commandStr.getValue().contains("-p"));
		Assert.assertTrue(commandStr.getValue().contains("TESTENV"));
		Assert.assertTrue(commandStr.getValue().contains("-e"));
		Assert.assertTrue(commandStr.getValue().contains("data"));
		Assert.assertTrue(commandStr.getValue().contains("-v"));
		Assert.assertTrue(commandStr.getValue().contains("docker"));
		Assert.assertTrue(commandStr.getValue().contains("run"));
		Assert.assertTrue(commandStr.getValue().contains("hello-world"));
		
	}

	@Test
	public void testDockerRunDispatched() throws Exception {
		DockerRunSpecification spec = new DockerRunSpecification("rabbitmq");
		String containerName = DockerCommandUtils.dockerRun(spec);
		Assert.assertNotNull(containerName);

		DockerCommandUtils.dockerLogContainer(containerName);

		Thread.sleep(5000);
		DockerCommandUtils.dockerKillContainer(containerName);
		try {
			DockerCommandUtils.dockerKillContainer(containerName);
			Assert.fail();
		} catch (Exception e) {
			// NoOp
		}
	}

	@Test
	public void testDockerLogs() throws Exception {
		DockerRunSpecification spec = new DockerRunSpecification("rabbitmq");
		String containerName = DockerCommandUtils.dockerRun(spec);
		Assert.assertNotNull(containerName);

		Mockito.reset(loggerMock);
		DockerCommandUtils.dockerLogContainer(containerName);
		Mockito.verify(loggerMock).info(logArgStr.capture());
		Assert.assertTrue(logArgStr.getValue().contains("=INFO REPORT===="));

		Thread.sleep(5000);
		DockerCommandUtils.dockerKillContainer(containerName);
		try {
			DockerCommandUtils.dockerKillContainer(containerName);
			Assert.fail();
		} catch (Exception e) {
			// NoOp
		}
	}

}
