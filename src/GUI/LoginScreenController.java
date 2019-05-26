/**
 * This class implements the controller for the view described by LoginScreen.fxml file.
 * @author Camila Maione
 */
package GUI;

import javax.security.auth.login.FailedLoginException;

import GUI.listeners.Notifier;
import GUI.utils.Alerts;
import application.LibraryManagementSystem;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.services.LoginService;

public class LoginScreenController {
	
	@FXML private TextField txtFieldUsername; // Where user will input e-mail
	@FXML private TextField txtFieldPassword; // Where user will input password
	@FXML private Button btLogin; // Login button
	@FXML private Button btExit; // Exit button
	
	private LoginService loginService;
	
	/**
	 * Initialized the controller.
	 * @param loginService A login service.
	 */
	public void init(LoginService loginService) {
		this.loginService = loginService;
	}
	
	/**
	 * Actions performed when the user fires the Login button.
	 */
	@FXML public void onBtLoginAction() {		
		try {
			// Try to login using input e-mail and password
			loginService.login(txtFieldUsername.getText(), txtFieldPassword.getText());
			Notifier.notifyUserLoginListeners();
		} catch (FailedLoginException e) {
			// If login fails
			Alerts.showAlert("Error", "Login failed", e.getMessage(), AlertType.ERROR);
		} catch (Exception e) {
			// Other errors
			Alerts.showAlert("Error", null, e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		}
	}
	
	/**
	 * Actions performed when the user fires the Exit button.
	 * @param event
	 */
	@FXML public void onBtExitAction() {
		LibraryManagementSystem.getPrimaryStage().close(); // Close application
	}

}
