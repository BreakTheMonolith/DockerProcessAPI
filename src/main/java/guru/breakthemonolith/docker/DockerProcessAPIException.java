package guru.breakthemonolith.docker;

import org.apache.commons.lang3.exception.ContextedRuntimeException;

/**
 * Standard DockerProcessAPI Exception.
 * 
 * @author D. Ashmore
 *
 */
public class DockerProcessAPIException extends ContextedRuntimeException {

	private static final long serialVersionUID = -5914291311025517926L;

	public DockerProcessAPIException(String message) {
		super(message);
	}

	public DockerProcessAPIException(Throwable cause) {
		super(cause);
	}

	public DockerProcessAPIException(String message, Throwable cause) {
		super(message, cause);
	}

}
