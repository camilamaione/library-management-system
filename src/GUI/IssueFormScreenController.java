/**
 * This class implements the controller for the view described by IssueFormScreenView.fxml file.
 * @author Camila Maione
 */
package GUI;

import java.util.Optional;

import org.kordamp.ikonli.javafx.FontIcon;

import GUI.listeners.Notifier;
import GUI.utils.Alerts;
import GUI.utils.Constraints;
import GUI.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Book;
import model.entities.Student;
import model.exceptions.LibraryException;
import model.services.LibraryService;

public class IssueFormScreenController {
	
	@FXML private TextField txtFieldID; // Text field for ID
	@FXML private TextField txtFieldName; // Text field for student's name
	@FXML private TextField txtFieldEmail; //  Text field for student's email
	@FXML private TextField txtFieldContact; // Text field for student's contact
	@FXML private Label labelErrorID; // Error label for bad inputs in txtFieldID
	@FXML private Label labelErrorEmail; // Error label for bad inputs in txtFieldEmail
	@FXML private FontIcon iconSearch; // Search icon
	@FXML private Button btIssue; // Issue button
	@FXML private Button btReset; // Reset button
	@FXML private Button btCancel; // Cancel button
	
	private LibraryService libraryService; // Service containing model operations
	private Student issuer; // Student that will receive the book
	private Book bookToIssue; // Book to be be issued
	
	/**
	 * Initializes the controller.
	 * @param libraryService LibraryService object containing all necessary model 
	 * operations.
	 * @param bookToIssue Book object representing the book that will be issued.
	 */
	public void init(LibraryService libraryService, Book bookToIssue) {
		this.libraryService = libraryService;
		this.bookToIssue = bookToIssue;		
		// Set fields to accept only numeric digits
		Constraints.setTextFieldInteger(txtFieldID);
		Constraints.setTextFieldInteger(txtFieldContact);
	}
	
	/**
	 * Actions performed when the user clicks on the search icon.
	 */
	@FXML public void onIconSearchClicked() {		
		if (txtFieldID.getText() == null || txtFieldID.getText().equals(""))
			labelErrorID.setText("(Insert the student's ID)");
		else {			
			Integer studentId = Integer.parseInt(txtFieldID.getText());
			Student student = libraryService.findStudent(studentId);
			/* If a student with the given ID is found, update the text fields to 
			 * display its data */
			if (student.getId() != null) {
				txtFieldName.setText(student.getName());
				txtFieldName.setDisable(false);
				txtFieldEmail.setText(student.getEmail());
				txtFieldEmail.setDisable(false);
				txtFieldContact.setText(String.valueOf(student.getContact()));
				txtFieldContact.setDisable(false);
				btIssue.setDisable(false);
			/* Otherwise and upon user confirmation, enable the text fields for user 
			 * to input a new student data */
			} else {
				Optional<ButtonType> option = Alerts.showConfirmation("Issue Book", "No student found with the provided ID. Do you want to register a new student?");
				if (option.get() == ButtonType.OK) {
					txtFieldID.setText("");
					txtFieldID.setDisable(true);
					txtFieldName.setText("");
					txtFieldName.setDisable(false);
					txtFieldName.setEditable(true);
					txtFieldEmail.setText("");
					txtFieldEmail.setDisable(false);
					txtFieldEmail.setEditable(true);
					txtFieldContact.setText("");
					txtFieldContact.setDisable(false);
					txtFieldContact.setEditable(true);					
				} else {
					resetTextFields();
				}
			}
		}		
	}
	
	/** 
	 * Actions performed whenever the user types a character in any of the visible
	 * text fields. Basically, it's a listener method that ensures that the Issue 
	 * button is enabled only when user fills all text fields. 
	 */	
	@FXML public void keyboardListener() {
		if ((txtFieldName.getText() != null && !txtFieldName.getText().equals(""))
				&& (txtFieldEmail.getText() != null && !txtFieldEmail.getText().equals(""))
				&& (txtFieldContact.getText() != null && !txtFieldContact.getText().equals("")))
			btIssue.setDisable(false);
		else
			btIssue.setDisable(true);
			
	}
	
	/**
	 * Actions performed whenever the user fires the Issue button.
	 * @param event ActionEvent originated from the button.
	 */
	@FXML public void onBtIssueAction(ActionEvent event) {
		// Check for error in the given email and display it, if found any
		String inputEmail = txtFieldEmail.getText();
		if (!inputEmail.contains("@") || inputEmail.charAt(0) == '@' 
				|| inputEmail.charAt(inputEmail.length() - 1) == '@')
			txtFieldEmail.setText("(Invalid e-mail format)");
		// Otherwise and upon user confirmation, proceed to issue the book
		else {
			Optional<ButtonType> option = Alerts.showConfirmation("Issue Book", "Issue \"" + bookToIssue.getTitle() + "\" to " + txtFieldName.getText() + "?");
			if (option.get() == ButtonType.OK) {
				// Get data for the new Student object from the fields
				issuer = new Student();
				issuer.setName(txtFieldName.getText());
				issuer.setEmail(txtFieldEmail.getText());
				issuer.setContact(Long.parseLong(txtFieldContact.getText()));
				try {
					if (txtFieldID.getText() == null || txtFieldID.getText().equals("")) {
						libraryService.insertStudent(issuer);
						issuer = libraryService.findStudent(issuer.getEmail());
					} else
						issuer.setId(Integer.parseInt(txtFieldID.getText()));
					// Register the new issue and inform user
					libraryService.issueBook(bookToIssue, issuer);
					Alerts.showAlert("Issue Book", null,
							"Book \"" + bookToIssue.getTitle() + "\" issued to: \n" + issuer, AlertType.INFORMATION);					
					Notifier.notifyIssuesChangeListeners(); // Notify listeners
					Utils.currentStage(event).close();
				} catch (LibraryException e) {
					Alerts.showAlert("Error", null, e.getMessage(), AlertType.ERROR);
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Actions performed whenever the user fires the Reset button.
	 */
	@FXML public void onBtResetAction() {
		resetTextFields();
	}
	
	/**
	 * Resets all text fields back to blank values and disabled status.
	 */
	private void resetTextFields() {
		txtFieldName.setText("");
		txtFieldEmail.setText("");
		txtFieldContact.setText("");
		txtFieldName.setDisable(true);
		txtFieldEmail.setDisable(true);
		txtFieldContact.setDisable(true);
		txtFieldID.setText("");
		txtFieldID.setDisable(false);
	}

	/**
	 * Actions performed when the user fires the Cancel button.
	 * @param event ActionEvent originated from the button.
	 */
	@FXML public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

}
