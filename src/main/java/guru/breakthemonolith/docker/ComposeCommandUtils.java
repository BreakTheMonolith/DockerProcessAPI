package guru.breakthemonolith.docker;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Utility class to execute Docker-compose related commands locally. This
 * assumes that commands 'docker ps' and 'docker-compose version' works on the
 * machine that this executes on.
 * 
 * @author D. Ashmore
 *
 */
public class ComposeCommandUtils {

	/**
	 * Brings a docker-compose environment up in detached mode ('docker-compose
	 * up -d').
	 * 
	 * @param dockerComposeConfig
	 */
	public static void composeUp(DockerComposeConfiguration dockerComposeConfig) {

		issueBasicCommand(new String[] { "up", "-d" }, dockerComposeConfig);
	}

	/**
	 * Shuts a docker-compose environment down ('docker-compose down').
	 * 
	 * @param dockerComposeConfig
	 */
	public static void composeDown(DockerComposeConfiguration dockerComposeConfig) {
		issueBasicCommand(new String[] { "down" }, dockerComposeConfig);
	}

	/**
	 * Outputs all available docker-compose environments to the log
	 * ('docker-compose ps').
	 * 
	 * @param dockerComposeConfig
	 */
	public static void composeListing(DockerComposeConfiguration dockerComposeConfig) {
		issueBasicCommand(new String[] { "ps" }, dockerComposeConfig);
	}

	/**
	 * Outputs all available docker-compose environments to the log
	 * ('docker-compose config').
	 * 
	 * @param dockerComposeConfig
	 */
	public static void composeConfig(DockerComposeConfiguration dockerComposeConfig) {
		issueBasicCommand(new String[] { "config" }, dockerComposeConfig);
	}

	private static String[] createCommandArray(DockerComposeConfiguration dockerComposeConfig) {
		Validate.notNull(dockerComposeConfig, "Null dockerComposeConfig not allowed.");
		Validate.notBlank(dockerComposeConfig.getConfigYamlFileName(),
				"Null or blank configYamlFileName not allowed.");
		List<String> commandList = new ArrayList<String>();

		commandList.add("docker-compose");
		commandList.add("-f");
		commandList.add(dockerComposeConfig.getConfigYamlFileName());

		if (StringUtils.isNotBlank(dockerComposeConfig.getProjectName())) {
			commandList.add("-p");
			commandList.add(dockerComposeConfig.getProjectName());
		}

		return commandList.toArray(new String[0]);
	}

	private static void issueBasicCommand(String[] commandArray, DockerComposeConfiguration dockerComposeConfig) {
		String[] composeCommandArray = createCommandArray(dockerComposeConfig);
		String commandStr = StringUtils.join(commandArray, ' ');
		composeCommandArray = (String[]) ArrayUtils.addAll(composeCommandArray, commandArray);

		CommandUtils.issueCommand(
				composeCommandArray,
				"Error with 'docker-compose " + commandStr + "'",
				new Pair[] { new ImmutablePair("dockerComposeConfig", dockerComposeConfig) });
	}

}
