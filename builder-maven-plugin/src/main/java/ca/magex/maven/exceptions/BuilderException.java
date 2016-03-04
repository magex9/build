package ca.magex.maven.exceptions;

/**
 * A generic catch all for thrown exceptions to become runtime.
 * 
 * This should be used when exception could happen, but it should be handled at
 * a generic statement in the servlet/main class.
 * 
 * These will be all things that will result in a failed build and do not need
 * to be handled in a specific way.
 * 
 * @author magex
 *
 */
public class BuilderException extends RuntimeException {

	private static final long serialVersionUID = 928948923492843L;

	public BuilderException(String msg) {
		super(msg);
	}

	public BuilderException(String msg, Exception e) {
		super(msg, e);
	}

}
