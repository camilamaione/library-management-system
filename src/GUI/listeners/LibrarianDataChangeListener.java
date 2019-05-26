/**
 * This interface describes a listener for changes made to the data of any librarian registered in the system.
 * @author Camila Maione
 */
package GUI.listeners;

public interface LibrarianDataChangeListener {
	
	/**
	 * Actions performed whenever a change in the data of a librarian stored in the system 
	 * is detected.
	 */
	void onLibrarianDataChanged();
}
