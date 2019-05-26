/**
 * This class implements a custom exception that may be thrown by the
 * persistence layer of the application whenever a database operation
 * error occurs and that error affects data integrity. 
 * @author Camila Maione 
 */
package model.database;

public class DatabaseIntegrityException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public DatabaseIntegrityException(String msg) {
		super(msg);
	}
}
