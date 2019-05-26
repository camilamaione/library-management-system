/**
 * This class implements a handler for creating preformatted alerts statically. 
 * @author Camila Maione
 */
package GUI.utils;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class Alerts {	
	
	/**
	 * Creates an alert for showing general information.
	 * @param title Title of the window.
	 * @param header Header of the alert.
	 * @param content Content message displayed by the alert.
	 * @param type Type of alert.
	 */
	public static void showAlert(String title, String header, String content, AlertType type) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		((Stage)alert.getDialogPane().getScene().getWindow()).setAlwaysOnTop(true);
		((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(Utils.getWindowIcon());
		alert.show();
	}
	
	/**
	 * Creates an alert that obtains a confirmation from the user.
	 * @param title Title of the window.
	 * @param content Content message displayed by the alert, specifying what the user must confirm.
	 * @return a ButtonType corresponding to the user's choice.
	 */
	public static Optional<ButtonType> showConfirmation(String title, String content) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setContentText(content);
		alert.setHeaderText(null);
		((Stage)alert.getDialogPane().getScene().getWindow()).setAlwaysOnTop(true);
		((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(Utils.getWindowIcon());
		return alert.showAndWait();
	}
}
