/**
 * This class implements a custom exception that may be thrown by the
 * model layer of the application whenever the system tries to execute 
 * a bad operation or that is forbidden in a library's perspective.
 * @author Camila Maione 
 */
package model.exceptions;

public class LibraryException extends Exception {

	private static final long serialVersionUID = 1L;

	public LibraryException(String msg) {
		super(msg);
	}
}
