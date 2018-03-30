package ca.magex.maven.exceptions;

import ca.magex.maven.model.Gav;

/**
 * A generic catch all for thrown exceptions to become runtime.
 * 
 * This should be used when exception could happen, but it should be handled at
 * a generic statement in the servlet/main class/maven mojo.
 * 
 * These will be all things that will result in a failed build and do not need
 * to be handled in a specific way.
 * 
 * @author magex
 *
 */
public class GavNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 928948923492843L;

	public GavNotFoundException(Gav gav) {
		super("Unable to find gav " + gav);
	}

	public GavNotFoundException(Gav gav, Exception e) {
		super("Unable to find gav " + gav, e);
	}

}
