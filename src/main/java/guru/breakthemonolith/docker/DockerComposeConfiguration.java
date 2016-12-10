package guru.breakthemonolith.docker;

import org.force66.vobase.ValueObjectBase;

/**
 * Describes a docker compose environment.
 * 
 * @author D. Ashmore
 *
 */
public class DockerComposeConfiguration extends ValueObjectBase {

	private String configYamlFileName;
	private String projectName;

	public DockerComposeConfiguration() {
	}

	public DockerComposeConfiguration(String configurationYamlFileName) {
		this.setConfigYamlFileName(configurationYamlFileName);
	}

	public String getConfigYamlFileName() {
		return configYamlFileName;
	}

	public void setConfigYamlFileName(String configurationYamlFileName) {
		this.configYamlFileName = configurationYamlFileName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

}
