/**
 * This interface describes a listener for any changes made to the name of the currently logged user.
 * @author Camila Maione
 */
package GUI.listeners;

public interface UserNameChangeListener {
	
	/**
	 * Actions performed whenever a change to the currently logged user's name is detected. 
	 */
	void onUserNameChanged();
}
