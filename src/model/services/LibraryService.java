/**
 * This class implements a library service, which offers methods for managing
 * books and issues. Management includes adding, deleting and editing books and
 * issues, and also insertion of new students.
 * @author Camila Maione 
 */

package model.services;

import java.util.Date;
import java.util.List;

import model.dao.BookDAO;
import model.dao.DAOFactory;
import model.dao.IssueDAO;
import model.dao.LibrarianDAO;
import model.dao.StudentDAO;
import model.database.DatabaseException;
import model.entities.Book;
import model.entities.Issue;
import model.entities.Librarian;
import model.entities.Student;
import model.exceptions.LibraryException;

public class LibraryService {

	private final BookDAO bookDAO = DAOFactory.createBookDAO();
	private final IssueDAO issueDAO = DAOFactory.createIssueDAO();
	private final LibrarianDAO librarianDAO = DAOFactory.createLibrarianDAO();
	private final StudentDAO studentDAO = DAOFactory.createStudentDAO();
	
	public LibraryService() { }
	
	/**
	 * Updates the data of the librarian currently logged in the system.
	 * @param librarian The object corresponding to the librarian currently
	 * logged in the system.
	 * @throws LibraryException An error thrown if the given Library object is
	 * null or doensn't correspond to a valid librarian registered in the
	 * system.
	 */
	public void updateProfile(Librarian librarian) throws LibraryException {
		if (librarian.getId() == null || librarian == null)
			throw new LibraryException("Librarian not found.");
		librarianDAO.updateLibrarian(librarian);
	}
	
	/**
	 * Register a new book in the system.
	 * @param book The object of type Book containing the data of the book
	 * to be registered in the system.
	 */
	public void registerBook(Book book) {
		bookDAO.insertBook(book);
	}
	
	/**
	 * Updates the data of an existing book.
	 * @param book The object containing the data of the book to be
	 * updated.
	 */
	public void updateBook(Book book) {
		bookDAO.updateBook(book);
	}
	
	/**
	 * Deletes from the system the book whose ID matches the given ID
	 * value. 
	 * @param bookId The ID value of the book to be delted.
	 */
	public void removeBook(Integer bookId) {
		bookDAO.deleteBook(bookId);
	}
	
	/**
	 * Retrieves the existing book whose ID value matches the given ID
	 * value.
	 * @param bookId The ID value of the book to be retrieved.
	 * @return An object containing the data of the retrieved book.
	 */
	public Book findBook(Integer bookId) {
		return bookDAO.findById(bookId);
	}
	
	
	/**
	 * Retrieves all books currently registered in the system.
	 * @return A list containing all objects of type Book stored in
	 * the system.
	 */
	public List<Book> findAllBooks() {
		return bookDAO.findAll();
	}
	
	/**
	 * Inserts a new student in the system.
	 * @param student The object containing the data of the student to be
	 * added in the system.
	 * @throws LibraryException if the email stored in the given Student 
	 * object already exists in any Student object currently registered. 
	 * in the system.
	 */
	public void insertStudent(Student student) throws LibraryException {
		try {
			studentDAO.insertStudent(student);
		} catch (DatabaseException e) {
			throw new LibraryException("The e-mail provided is already in use.");
		}
	}
	
	/**
	 * Retrieves the student registered in the systems whose ID value 
	 * matches the given ID value.
	 * @param studentId The ID value of the student to be retrieved.
	 * @return An object of type Student containing either the data of 
	 * the retrieved student or null fields if no student is found for
	 * the given ID.
	 */
	public Student findStudent(Integer studentId) {
		return studentDAO.findById(studentId);
	}
	
	/**
	 * Retrieves the student registered in the system whose e-mail
	 * matches the given e-mail.
	 * @param studentId The e-mail of the student to be retrieved.
	 * @return An object of type Student containing either the data of 
	 * the retrieved student or null fields if no student is found for
	 * the given e-mail.
	 */
	public Student findStudent(String studentEmail) {
		return studentDAO.findByEmail(studentEmail);
	}
	
	/**
	 * Retrieves the issue registered in the system whose ID value
	 * matches the given ID value.
	 * @param issueId The ID value of the issue to be retrieved.
	 * @return An object of type Issue containing either the data of 
	 * the retrieved issue or null fields if no issue is found for
	 * the given ID.
	 */
	public Issue findIssue(Integer issueId) {
		return issueDAO.findById(issueId);
	}
	
	/**
	 * Retrieves all issues registered in the system for the book whose
	 * ID value matches the given ID value.
	 * @param bookId The ID value of the book whose issues currently 
	 * registered in the system are to be retrieved.
	 * @return A list of objects of type Issue that are registered to 
	 * the book associated to the given ID.
	 */
	public List<Issue> findIssuesByBookId(Integer bookId) {
		return issueDAO.findByBookId(bookId);
	}
	
	/**
	 * Retrieves all issues registered in the system for the student whose
	 * ID value matches the given ID value.
	 * @param studentId The ID value of the student whose issues currently 
	 * registered in the system are to be retrieved.
	 * @return A list of objects of type Issue that are registered to 
	 * the student associated to the given ID.
	 */
	public List<Issue> findIssuesByStudentId(Integer studentId) {
		return issueDAO.findByStudentId(studentId);
	}
	
	/**
	 * Retrieves all issues currently registered in the system.
	 * @return A list of all objects of type Issue currently registered in 
	 * the system.
	 */
	public List<Issue> findAllIssues() {
		return issueDAO.findAll();
	}
	
	
	/**
	 * Issues the given book to the given student.
	 * @param book The book to be issued.
	 * @param student The student to receive the issue.
	 * @throws LibraryException if there is already an issue of the given book
	 * registered for the given student.
	 */
	public void issueBook(Book book, Student student) throws LibraryException {
		// Check if given book is already issued to the given student
		List<Issue> issuesByStudent = issueDAO.findByStudentId(student.getId());
		if (!issuesByStudent.isEmpty()) {
			for (Issue issue : issuesByStudent) {
				if (issue.getBookId() == book.getId())
					throw new LibraryException("An exemplar of this book is already issued to the specified student.");
			}
		}
		
		// Check if there are free exemplars of the book to be issued
		if (book.getQuantity() == book.getIssued())
			throw new LibraryException("All exemplars already issued.");		

		// Register issue and update the book's data
		issueDAO.insertIssue(new Issue(book.getId(), student.getId(), new Date()));
		book.setIssued(book.getIssued() + 1);
		bookDAO.updateBook(book);
	}
	
	/**
	 * Returns the given issue.
	 * @param issue The issue containing the book to be returned to the library and
	 * the student that received the issue.
	 */
	public void returnBook(Issue issue) {
		Book issuedBook = bookDAO.findById(issue.getBookId());
		issuedBook.setIssued(issuedBook.getIssued() - 1);
		bookDAO.updateBook(issuedBook);
		issueDAO.deleteIssue(issue.getId());
	}
}
