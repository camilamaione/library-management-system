/**
 * This class implements a factory of data access objects used in the application.
 * @author Camila Maione
 */
package model.dao;

import model.dao.impl.BookDAOJDBC;
import model.dao.impl.IssueDAOJDBC;
import model.dao.impl.LibrarianDAOJDBC;
import model.dao.impl.StudentDAOJDBC;
import model.database.Database;

public class DAOFactory {
	
	/**
	 * Creates a data access object for object of type Librarian.
	 * @return A data access object for librarians.
	 */
	public static LibrarianDAO createLibrarianDAO() {
		return new LibrarianDAOJDBC(Database.getConnection());
	}
	
	/**
	 * Creates a data access object for object of type Student.
	 * @return A data access object for students.
	 */
	public static StudentDAO createStudentDAO() {
		return new StudentDAOJDBC(Database.getConnection());
	}
	
	/**
	 * Creates a data access object for object of type Book.
	 * @return A data access object for books.
	 */
	public static BookDAO createBookDAO() {
		return new BookDAOJDBC(Database.getConnection());
	}
	
	/**
	 * Creates a data access object for object of type Issue.
	 * @return A data access object for issues.
	 */
	public static IssueDAO createIssueDAO() {
		return new IssueDAOJDBC(Database.getConnection());
	}
}
