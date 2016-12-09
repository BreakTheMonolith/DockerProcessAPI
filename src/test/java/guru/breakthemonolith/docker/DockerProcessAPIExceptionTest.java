package guru.breakthemonolith.docker;

import org.junit.Assert;
import org.junit.Test;

public class DockerProcessAPIExceptionTest {

	private static final String TEST_MESSAGE = "crap";
	private static final Exception TEST_EXCEPTION = new RuntimeException("FUBAR");

	@Test
	public void testDockerProcessAPIExceptionString() throws Exception {
		Assert.assertTrue(new DockerProcessAPIException(TEST_MESSAGE).getMessage().contains(TEST_MESSAGE));
	}

	@Test
	public void testDockerProcessAPIExceptionThrowable() throws Exception {
		Assert.assertTrue(new DockerProcessAPIException(TEST_EXCEPTION).getCause().equals(TEST_EXCEPTION));
	}

	@Test
	public void testDockerProcessAPIExceptionStringThrowable() throws Exception {
		DockerProcessAPIException e = new DockerProcessAPIException(TEST_MESSAGE, TEST_EXCEPTION);
		Assert.assertTrue(e.getMessage().contains(TEST_MESSAGE));
		Assert.assertTrue(e.getCause().equals(TEST_EXCEPTION));
	}

}
