/**
 * This class implements a library's login service, which offers methods 
 * to log in and out from the system.
 * @author Camila Maione 
 */

package model.services;

import javax.security.auth.login.FailedLoginException;

import model.dao.DAOFactory;
import model.dao.LibrarianDAO;
import model.entities.Librarian;

public class LoginService {

	private LibrarianDAO librarianDAO = DAOFactory.createLibrarianDAO();
	private Librarian userLogged;
	
	public LoginService() { }
	
	/**
	 * Retrieves the librarian that is currently logged in the system.
	 * @return An object of type Librarian containing the data of the
	 * librarian currently logged in the system.
	 */
	public Librarian getUserLogged() {
		return userLogged;
	}

	/**
	 * Sets the librarian that is currently logged in the system.
	 * @param userLogged The librarian that is currenytly logged
	 * in the system.
	 */
	public void setUserLogged(Librarian userLogged) {
		this.userLogged = userLogged;
	}

	/**
	 * Tries to execute a login for the librarian associated to the
	 * given email and password.
	 * @param librarianEmail The e-mail of the librarian that is trying
	 * to login.
	 * @param password The password of the librarian that is trying to
	 * login.
	 * @throws FailedLoginException If user provided the wrong combination
	 * of email/password or null values for email and/or password.
	 */
	public void login(String librarianEmail, String password) throws FailedLoginException {
		if (librarianEmail == null || librarianEmail.equals("") || password == null || password.equals(""))
			throw new FailedLoginException("Email and password fields can't be empty.");
		Librarian librarian = librarianDAO.findByEmail(librarianEmail);
		if (librarian.getId() == null)
			throw new FailedLoginException("Wrong username/email.");
		if (!librarian.getPassword().equals(password))
			throw new FailedLoginException("Wrong password for user " + librarianEmail + ".");
		userLogged = librarian;
	}

}
