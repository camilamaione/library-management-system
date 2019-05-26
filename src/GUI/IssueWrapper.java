/**
 * This class implements a wrapper that encapsulates data of a Issue, a Book and a Student
 * objects in a single object to be used by a TableView.
 * @author Camila Maione
 */
package GUI;

import model.entities.Book;
import model.entities.Issue;
import model.entities.Student;

public class IssueWrapper {

	private Issue issue; // Issue object
	private Book book; // Book object corresponding to the book whose ID is stored in issue.
	private Student student; // Student object corresponding to the student whose ID is stored in issue.
	
	public IssueWrapper(Issue issue, Book book, Student student) {
		this.issue = issue;
		this.book = book;
		this.student = student;
	}

	/**
	 * Returns the issue.
	 * @return an object of type Issue corresponding to the issue.
	 */
	public Issue getIssue() {
		return issue;
	}

	/**
	 * Sets a new reference for the issue.
	 * @param issue The new Issue object.
	 */
	public void setIssue(Issue issue) {
		this.issue = issue;
	}

	/**
	 * Gets the book referenced in the issue.
	 * @return the book object referenced in the issue.
	 */
	public Book getBook() {
		return book;
	}

	/**
	 * Sets a new reference for the book referenced in the issue.
	 * @param book The new Book object.
	 */
	public void setBook(Book book) {
		this.book = book;
	}

	/**
	 * Gets the student which received the issue.
	 * @return the student which received the issue.
	 */
	public Student getStudent() {
		return student;
	}

	/**
	 * Sets a new reference for the student which received the issue.
	 * @param student The new Student object.
	 */
	public void setStudent(Student student) {
		this.student = student;
	}

	
}
