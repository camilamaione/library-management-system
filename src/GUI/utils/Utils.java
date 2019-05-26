/**
 * This class implements a handler for overall use.
 * @author Camila Maione
 */
package GUI.utils;

import java.text.SimpleDateFormat;

import application.LibraryManagementSystem;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Utils {

	/**
	 * Creates a secondary, floating window, typically for forms.
	 * @param title The title of the window.
	 * @return The created window.
	 */
	public static Stage createSecondaryStage(String title) {
		Stage stage = new Stage();
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(LibraryManagementSystem.getPrimaryStage());
		stage.setTitle(title);
		stage.getIcons().add(getWindowIcon());
		return stage;
	}
	
	/**
	 * Returns the stage in which a given event occurred.
	 * @param event The event whose source stage must be identified.
	 * @return The source stage of the given event.
	 */
	public static Stage currentStage(Event event) {
		return (Stage)((Node)event.getSource()).getScene().getWindow();
	}
	
	/**
	 * Returns the default date format used in the application.
	 * @return the default date format used in the application.
	 */
	public static SimpleDateFormat getDateFormat() {
		return new SimpleDateFormat("dd/MM/yyyy");
	}
	
	/**
	 * Returns the default icon used by all windows composing the application.
	 * @return the default icon used by all windows composing the application.
	 */
	public static Image getWindowIcon() {
		return new Image(Utils.class.getResource("/GUI/icons/tea.png").toString());
	}
}
