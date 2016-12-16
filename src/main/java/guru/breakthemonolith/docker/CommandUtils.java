package guru.breakthemonolith.docker;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ContextedRuntimeException;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Internal class for making process management easy.
 * 
 * @author D. Ashmore
 *
 */
class CommandUtils {

	private static Logger logger = LoggerFactory.getLogger(DockerCommandUtils.LOGGER_LABEL);

	protected static void issueCommand(String[] commandArray, String errorMessage) {
		issueCommand(commandArray, errorMessage, null);
	}

	protected static void issueCommand(String[] commandArray, String errorMessage,
			Pair<String, Object>[] errorContextValues) {
		String commandStr = StringUtils.join(commandArray, ' ');
	
		logger.info("Docker command: {}", commandStr);
		try {
			Process docker = createProcess(commandArray);
			waitForThrowingException(docker, commandStr);
		} catch (Exception e) {
			ContextedRuntimeException cEx = new DockerProcessAPIException(errorMessage, e)
					.addContextValue("commandStr", commandStr);
			if (errorContextValues != null) {
				for (Pair<String, Object> pair : errorContextValues) {
					cEx.addContextValue(pair.getKey(), pair.getValue());
				}
			}
			throw cEx;
		}
	}

	protected static void waitForThrowingException(Process process, String commandStr)
			throws InterruptedException, IOException {
		int rc = process.waitFor();
		String stdOut = IOUtils.toString(process.getInputStream(), Charset.defaultCharset());
		String stdErr = IOUtils.toString(process.getErrorStream(), Charset.defaultCharset());
	
		logger.info(stdOut);
		if (StringUtils.isNotBlank(stdErr)) {
			logger.error(stdErr);
		}
		if (rc != 0) {
			throw new DockerProcessAPIException("Command Error reported")
					.addContextValue("commandStr", commandStr)
					.addContextValue("standardError", stdErr)
					.addContextValue("standardOut", stdOut);
		}
	}

	protected static Process createProcess(String[] commandArray) throws IOException {
		return new ProcessBuilder(commandArray).start();
	}

}
