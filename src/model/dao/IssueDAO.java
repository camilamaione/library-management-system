/**
 * This interface describes a data access object for retrieving and manipulating data from the database
 * regarding objects of Issue type.
 * @author Camila Maione
 */
package model.dao;

import java.util.List;

import model.entities.Issue;

public interface IssueDAO {
	
	/**
	 * Insert the data of a new issue in the database.
	 * @param issue The new issue, whose data must be persisted.
	 */
	void insertIssue(Issue issue);
	
	/**
	 * Updates the data of an existing issue in the database.
	 * @param issue The issue whose data must be persisted.
	 */
	void updateIssue(Issue issue);
	
	/**
	 * Deletes the data of an issue from the database.
	 * @param issueId The ID of the issue to be removed.
	 */
	void deleteIssue(Integer issueId);
	
	/**
	 * Retrieves the data of an issue from the database according to a given ID
	 * and converts them into an object.
	 * @param issueId The ID of the issue to be retrieved.
	 * @return The issue retrieved according to the given ID.
	 */
	Issue findById(Integer issueId);
	
	/**
	 * Retrieves the data of all issues currently active for a given student
	 * and converts them into objects.
	 * @param studentId The ID of the student.
	 * @return A list of all issues currently active for the given student.
	 */
	List<Issue> findByStudentId(Integer studentId);
	
	/**
	 * Retrieves the data of all issues currently active for a given book
	 * and converts them into objects.
	 * @param bookId The ID of the book.
	 * @return A list of all issues currently active for the given book.
	 */
	List<Issue> findByBookId(Integer bookId);
	
	/**
	 * Retrieves the data of all issues stored in the database
	 * and converts them into objects.
	 * @return A list of all issues stored in the database.
	 */
	List<Issue> findAll();
}
