package guru.breakthemonolith.docker;

import java.util.HashMap;
import java.util.Map;

import org.force66.vobase.ValueObjectBase;

/**
 * Specification for 'docker run' arguments.
 * 
 * <p>
 * Rules are:
 * </p>
 * <li>imageName is required</li>
 * <li>detachInd defaults to 'true'</li>
 * <li>detachedWaitTimeMillis is the time to wait after spawning a detached
 * container. Defaults to 3 seconds.</li>
 * 
 * @author D. Ashmore
 *
 */
public class DockerRunSpecification extends ValueObjectBase {
	public static final int DEFAULT_DETACHED_WAIT_TIME_MILLIS = 3000;

	private String imageName;
	private boolean detachInd = true;
	private int detachedWaitTimeMillis = DEFAULT_DETACHED_WAIT_TIME_MILLIS;
	private String command;
	private Map<String, String> environmentMap = new HashMap<String, String>();
	private Map<String, String> volumeMap = new HashMap<String, String>();
	private Map<String, String> portMap = new HashMap<String, String>();

	public DockerRunSpecification() {
	}

	public DockerRunSpecification(String imageName) {
		this.setImageName(imageName);
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public boolean isDetachInd() {
		return detachInd;
	}

	public void setDetachInd(boolean detachInd) {
		this.detachInd = detachInd;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public Map<String, String> getEnvironmentMap() {
		return environmentMap;
	}

	public Map<String, String> getVolumeMap() {
		return volumeMap;
	}

	public Map<String, String> getPortMap() {
		return portMap;
	}

	public int getDetachedWaitTimeMillis() {
		return detachedWaitTimeMillis;
	}

	public void setDetachedWaitTimeMillis(int detachedWaitTimeMillis) {
		this.detachedWaitTimeMillis = detachedWaitTimeMillis;
	}

}
