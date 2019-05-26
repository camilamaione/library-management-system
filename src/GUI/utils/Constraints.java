/**
 * This class implements a handler for applying certain constraints to the text fields 
 * displayed during the use of the application.
 */
package GUI.utils;

import javafx.scene.control.TextField;

public class Constraints {

	/**
	 * Makes a text field accept only numeric digits.
	 * @param txt The text field to be constrained.
	 */
	public static void setTextFieldInteger(TextField txt) {
		txt.textProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue != null && !newValue.matches("\\d*"))
				txt.setText(oldValue);
		});
	}
	
	/**
	 * Makes a text field accept only numeric digits and contain
	 * at least one digit.
	 * @param txt The text field to be constrained.
	 */
	public static void setTextFieldIntegerNotNull(TextField txt) {
		txt.textProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue != null && !newValue.matches("[1-9]\\d*"))
				txt.setText(oldValue);
		});
	}
	
	/**
	 * Makes a text field accept only letters.
	 * @param txt The text field to be constrained.
	 */
	public static void setTextFieldLetters(TextField txt) {
		txt.textProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue != null && !newValue.matches("[a-zA-Z ]*"))
				txt.setText(oldValue);
		});
	}

	/**
	 * Sets a maximum number of characters for a text field.
	 * @param txt The text field to be constrained.
	 * @param max The maximum number of characters.
	 */
	public static void setTextFieldMaxLength(TextField txt, int max) {
		txt.textProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue != null && newValue.length() > max)
				txt.setText(oldValue);
		});
	}

	/**
	 * Makes a text field accept only double values. 
	 * @param txt The text field to be constrained.
	 */
	public static void setTextFieldDouble(TextField txt) {
		txt.textProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue != null && !newValue.matches("\\d*([\\.]\\d*)?"))
				txt.setText(oldValue);
		});
	}


}
