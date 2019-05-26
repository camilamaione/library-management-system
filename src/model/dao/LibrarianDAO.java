/**
 * This interface describes a data access object for retrieving and manipulating data from the database
 * regarding objects of Librarian type.
 * @author Camila Maione
 */
package model.dao;

import java.util.List;

import model.entities.Librarian;

public interface LibrarianDAO {
	
	/**
	 * Insert the data of a new librarian in the database.
	 * @param librarian The new librarian, whose data must be persisted.
	 */
	void insertLibrarian(Librarian librarian);
	
	/**
	 * Updates the data of an existing librarian in the database.
	 * @param librarian The librarian whose data must be persisted.
	 */
	void updateLibrarian(Librarian librarian);
	
	/**
	 * Delete the data of a librarian from the database.
	 * @param librarianId The ID of the librarian whose data is to be removed.
	 */
	void deleteLibrarian(Integer librarianId);
	
	/**
	 * Retrieves the data of a librarian whose ID is given and converts them into
	 * an object.
	 * @param librarianId The ID of the librarian whose data is to be retrieved.
	 * @return The librarian retrieved according to the given ID.
	 */
	Librarian findById(Integer librarianId);
	
	/**
	 * Retrieves the data of a librarian whose email is given and converts them into
	 * an object.
	 * @param librarianEmail The email of the librarian whose data is to be retrieved.
	 * @return The librarian retrieved according to the given email.
	 */
	Librarian findByEmail(String librarianEmail);
	
	/**
	 * Retrieves the data of all librarians stored in the database and converts them into
	 * objects.
	 * @return A list of all librarians stored in the database.
	 */
	List<Librarian> findAll();
}
