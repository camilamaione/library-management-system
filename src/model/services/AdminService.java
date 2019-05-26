/**
 * This class implements a library's administrator service, which 
 * offers methods for managing, adding, deleting or editing librarians.
 * @author Camila Maione 
 */
package model.services;

import java.util.List;

import model.dao.DAOFactory;
import model.dao.LibrarianDAO;
import model.entities.Librarian;

public class AdminService {
	
	private final LibrarianDAO librarianDAO = DAOFactory.createLibrarianDAO();
	
	public AdminService() { }
	
	/**
	 * Registers a new librarian in the system.
	 * @param librarian The object containing the data of the librarian
	 * to be registered.
	 */
	public void registerLibrarian(Librarian librarian) {
		librarian.setPassword("12345");
		librarianDAO.insertLibrarian(librarian);
	}
	
	/**
	 * Updates the data of an existing librarian.
	 * @param librarian The object containing the data of the librarian.
	 */
	public void updateLibrarian(Librarian librarian) {
		librarianDAO.updateLibrarian(librarian);
	}
	
	/**
	 * Deletes a librarian from the system.
	 * @param librarianId The ID value of the librarian to be deleted.
	 */
	public void removeLibrarian(Integer librarianId) {
		librarianDAO.deleteLibrarian(librarianId);
	}
	
	/**
	 * Searches for a librarian whose ID value matches the given ID 
	 * value and returns the object containing its data if found.
	 * @param librarianId The ID value of the librarian to be searched
	 * for.
	 * @return The object containing the data of the found librarian,
	 * or an empty object (null attributes) if none matching librarian
	 * found.
	 */
	public Librarian findLibrarian(Integer librarianId) {
		return librarianDAO.findById(librarianId);
	}
	
	/**
	 * Retrieves all librarians currently registered in the system.
	 * @return A list of all Librarian type objects currently
	 * stored in the system.
	 */
	public List<Librarian> findAllLibrarians() {
		return librarianDAO.findAll();
	}

}
