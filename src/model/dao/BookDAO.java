/**
 * This interface describes a data access object for retrieving and manipulating data from the database
 * regarding objects of Book type.
 * @author Camila Maione
 */
package model.dao;

import java.util.List;

import model.entities.Book;

public interface BookDAO {
	
	/**
	 * Insert the data of a new book in the database.
	 * @param book The new book, whose data must be persisted.
	 */
	void insertBook(Book book);
	
	/**
	 * Updates the data of an existing book in the database.
	 * @param book The book whose data must be persisted.
	 */
	void updateBook(Book book);
	
	/**
	 * Deletes the data of a book from the database.
	 * @param bookId The ID of the book to be removed.
	 */
	void deleteBook(Integer bookId);
	
	/**
	 * Retrieves the data of a book from the database according to a given ID 
	 * and converts them into an object.
	 * @param bookId The ID of the book to be retrieved.
	 * @return The book retrieved according to the given ID.
	 */
	Book findById(Integer bookId);
	
	/**
	 * Retrieves the data of all books stored in the database and converts them 
	 * into objects.
	 * @return A list of all books stored in the database.
	 */
	List<Book> findAll();

}
