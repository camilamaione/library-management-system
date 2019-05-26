/**
 * This interface describes a listener for changes made to the data of any issue registered in the system.
 * @author Camila Maione
 */
package GUI.listeners;

public interface IssuesChangeListener {
	
	/**
	 * Actions performed whenever a change in the data of a issue stored in the system 
	 * is detected.
	 */
	void onIssuesChanged();

}
