/**
 * This class implements a custom exception that may be thrown by the
 * persistence layer of the application whenever a database operation
 * error occurs.
 * @author Camila Maione 
 */
package model.database;

public class DatabaseException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public DatabaseException(String msg) {
		super(msg);
	}
}
