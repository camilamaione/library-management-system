/**
 * This class implements a notifier handler for all listeners and notifiers in the application.
 * @author Camila Maione
 */
package GUI.listeners;

import java.util.ArrayList;
import java.util.List;

public class Notifier {
	
	private static List<IssuesChangeListener> iclisteners = new ArrayList<>(); 
	private static List<BookDataChangeListener> bdclisteners = new ArrayList<>();
	private static List<LibrarianDataChangeListener> ldclisteners = new ArrayList<>();
	private static List<UserNameChangeListener> unclisteners = new ArrayList<>();
	private static List<UserLoginListener> ullisteners = new ArrayList<>();
	
	/**
	 * Notifies listeners that a change was made in an issue's data.
	 */
	public static void notifyIssuesChangeListeners() {
		for (IssuesChangeListener listener : iclisteners)
			listener.onIssuesChanged();
	}	
	
	/**
	 * Notifies listeners that a change was made in a book's data.
	 */
	public static void notifyBookDataChangeListeners() {
		for (BookDataChangeListener listener : bdclisteners)
			listener.onBookDataChanged();
	}	
	
	/**
	 * Notifies listeners that a change was made in a librarian's data.
	 */	
	public static void notifyLibrarianDataChangeListeners() {
		for (LibrarianDataChangeListener listener : ldclisteners)
			listener.onLibrarianDataChanged();
	}
	
	/**
	 * Notifies listeners that a change was made in the name of the currently
	 * logged user.
	 */
	public static void notifyUserNameChangeListeners() {
		for (UserNameChangeListener listener : unclisteners)
			listener.onUserNameChanged();
	}
	
	/**
	 * Notifies listeners that a user has logged in.
	 */
	public static void notifyUserLoginListeners() {
		for (UserLoginListener listener : ullisteners)
			listener.onUserLogged();
	}
	
	/**
	 * Subscribes a new listener for changes made in any issue registered in the system.
	 * @param listener The listener to be subscribed.
	 */
	public static void subscribeIssuesChangeListener(IssuesChangeListener listener) {
		iclisteners.add(listener);
	}
	
	/**
	 * Subscribes a new listener for changes made in any book registered in the system.
	 * @param listener The listener to be subscribed.
	 */
	public static void subscribeBookDataChangeListener(BookDataChangeListener listener) {
		bdclisteners.add(listener);
	}
	
	/**
	 * Subscribes a new listener for changes made in any librarian registered in the system.
	 * @param listener The listener to be subscribed.
	 */
	public static void subscribeLibrarianDataChangeListener(LibrarianDataChangeListener listener) {
		ldclisteners.add(listener);
	}
	
	/**
	 * Subscribes a new listener for changes made in the name of the currently logged user.
	 * @param listener The listener to be subscribed.
	 */
	public static void subscribeUserNameChangeListener(UserNameChangeListener listener) {
		unclisteners.add(listener);
	}
	
	/**
	 * Subscribes a new listener for logins.
	 * @param listener The listener to be subscribed.
	 */
	public static void subscribeUserLoginListener(UserLoginListener listener) {
		ullisteners.add(listener);
	}
}
