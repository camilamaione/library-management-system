/**
 * This interface describes a listener for changes made to the data of any book registered in the system.
 * @author Camila Maione
 */
package GUI.listeners;

public interface BookDataChangeListener {
	
	/**
	 * Actions performed whenever a change in the data of a book stored in the system 
	 * is detected.
	 */
	void onBookDataChanged();
}
