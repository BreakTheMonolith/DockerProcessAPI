package guru.breakthemonolith.docker;

import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Test;

public class CommandUtilsTest {

	@Test
	public void testCreateProcess() throws Exception {
		Process process = CommandUtils.createProcess(new String[] { "docker", "help" });
		Thread.sleep(2000);
		String stdOut = IOUtils.toString(process.getInputStream(), Charset.defaultCharset());
		Assert.assertTrue(stdOut.contains("Usage:"));
	}

	@Test(expected = DockerProcessAPIException.class)
	public void testIssueCommand2Args() throws Exception {
		CommandUtils.issueCommand(new String[] { "docker", "non-existent" }, "non-existent");
	}

	@Test
	public void testIssueCommand3Args() throws Exception {
		try {
			CommandUtils.issueCommand(new String[] { "docker", "non-existent" }, "non-existent",
				new Pair[] { new ImmutablePair("imageName", "foo") });
			Assert.fail();
		} catch (DockerProcessAPIException e) {
			Assert.assertTrue(e.getContextValues("imageName").size() == 1);
		}
	}

}
