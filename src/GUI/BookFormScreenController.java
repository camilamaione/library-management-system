/**
 * This class describes the controller for BookFormScreen.fxml view.
 * @author Camila Maione
 */
package GUI;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.entities.Book;
import model.services.LibraryService;

public class BookFormScreenController {

	@FXML private Button btSave; // Save button
	@FXML private Button btReset; // Reset button
	@FXML private TextField txtFieldTitle; // Where user will input title
	@FXML private TextField txtFieldAuthor; // Where user will input authors
	@FXML private TextField txtFieldPublisher; // Where user will input publisher
	@FXML private TextField txtFieldPages; // Where user will input number of pages
	@FXML private TextField txtFieldYear; // Where user will input year
	@FXML private TextField txtFieldEdition; // Where user will input edition
	@FXML private TextArea txtAreaDescription; // Where user will input description
	@FXML private TextField txtFieldQuantity; // Where user will input quantity
	@FXML private TextField txtFieldURL; // Where user will input image url
	@FXML private ImageView imageViewPreview; // If valid URL is given, load image here	
	
	@FXML private Label labelBookTitle; // If editing a book, this label shows the title of the book being edited
	@FXML private Label labelMessage; // Show if user is either adding a new book or editing an existing one
	@FXML private Label labelTitleError; // Label for showing errors with the input title
	@FXML private Label labelAuthorError; // Label for showing errors with the input author
	@FXML private Label labelYearError; // Label for showing errors with the input year
	@FXML private Label labelQuantityError; // Label for showing errors with the input quantity
	@FXML private Label labelURLError; // Label for showing errors with the input quantity

	private LibraryService libraryService;
	private Book book;

	/**
	 * Initializes the controller.
	 * @param libraryService LibraryService object containing all model operations for Book.
	 * @param book Book object whose data will be edited by the screen. If null,
	 * then the screen will add a new Book object in the system.
	 */
	public void init(LibraryService libraryService, Book book) {
		// Setup global variables 
		this.book = book;
		this.libraryService = libraryService; // Service for library operations
		
		// If an non-null object Book is passed, then the window was opened for editing this object data
		if (book != null) {
			labelMessage.setText("Editing: ");
			labelBookTitle.setText(book.getTitle());
		} else {
			// Otherwise, the window was opened for adding a new book on the database
			labelMessage.setText("Please insert information for the new book in the fields below.");
		}
		
		// Set which values will be shown in the text fields
		initTextFieldTexts();
		
		// Set constraints for the text fields
		Constraints.setTextFieldIntegerNotNull(txtFieldQuantity);
		Constraints.setTextFieldInteger(txtFieldEdition);
		Constraints.setTextFieldInteger(txtFieldPages);
		Constraints.setTextFieldInteger(txtFieldYear);
	}

	/**
	 * Set the initial values for the text fields to be input by the user, which are either
	 * the data stored in the global Book object (in case of editing) or blank values (in case of
	 * adding).
	 */
	private void initTextFieldTexts() {
		/* If the global object Book is not null, then it's an editing window and 
		 * the data stored in the global object must appear in the corresponding 
		 * text fields */
		if (book != null) {
			txtFieldTitle.setText(book.getTitle());
			txtFieldAuthor.setText(book.getAuthor());
			txtFieldPublisher.setText(book.getPublisher());			
			if (book.getEdition() != null)		
				txtFieldEdition.setText(String.valueOf(book.getEdition()));			
			if (book.getNumPages() != null)
				txtFieldPages.setText(String.valueOf(book.getNumPages()));			
			if (book.getYear() != null)
				txtFieldYear.setText(String.valueOf(book.getYear()));
			txtAreaDescription.setText(book.getDescription());
			txtFieldQuantity.setText(String.valueOf(book.getQuantity()));	
			txtFieldURL.setText(book.getImageUrl());
			if (book.getImageUrl() != null && !book.getImageUrl().equals("")) {
				try {
					imageViewPreview.setImage(new Image(book.getImageUrl()));
					imageViewPreview.setFitHeight(100);
					imageViewPreview.setFitWidth(150);
				} catch (IllegalArgumentException e) {					
				}
			}
		/* Otherwise, it's an adding window and the text fields must be empty initially */
		} else {
			txtFieldTitle.setText("");
			txtFieldAuthor.setText("");
			txtFieldPublisher.setText("");
			txtFieldEdition.setText("");
			txtFieldPages.setText("");
			txtFieldYear.setText("");
			txtAreaDescription.setText("");
			txtFieldQuantity.setText("1");
			txtFieldURL.setText("");
		}
	}

	/**
	 * Actions performed when the user fires the Reset button.
	 * @param event ActionEvent generated by the button.
	 */
	@FXML public void onBtResetAction(ActionEvent event) {
		resetErrorLabels(); // Clear all error labels
		initTextFieldTexts(); // Reset the text fields 
	}

	/**
	 * Actions performed when the user fires the Save button.
	 * @param event ActionEvent generated by the button.
	 */
	@FXML public void onBtSaveAction(ActionEvent event) {		
		// Clear all error labels
		resetErrorLabels();
		// Search for possible errors in input values
		Map<String, String> errors = checkInputValuesErrors();
		// If there are errors, error labels are updated
		if (!errors.isEmpty()) {
			showErrorLabels(errors);			
		// Otherwise, save changes to global object or add new book
		} else {			
			Optional<ButtonType> option;
			if (book == null) {
				book = new Book();				
				option = Alerts.showConfirmation("Add Book", "Are you sure to add this book?");				
			} else		
				option = Alerts.showConfirmation("Edit Book", "Are you sure to edit this book's information?");			
			// Proceed upon confirmation from user
			if (option.get() == ButtonType.OK) {
				// Update attributes according to input values
				book.setTitle(txtFieldTitle.getText());
				book.setAuthor(txtFieldAuthor.getText());
				book.setPublisher(txtFieldPublisher.getText());				
				if ((txtFieldEdition.getText() != null) && !txtFieldEdition.getText().equals(""))
					book.setEdition(Integer.parseInt(txtFieldEdition.getText()));
				else
					book.setEdition(null);				
				if ((txtFieldPages.getText() != null) && !txtFieldPages.getText().equals(""))
					book.setNumPages(Integer.parseInt(txtFieldPages.getText()));
				else 
					book.setNumPages(null);				
				if ((txtFieldYear.getText() != null) && !txtFieldYear.getText().equals(""))
					book.setYear(Integer.parseInt(txtFieldYear.getText()));
				else
					book.setNumPages(null);				
				book.setDescription(txtAreaDescription.getText());
				book.setQuantity(Integer.parseInt(txtFieldQuantity.getText()));
				book.setImageUrl(txtFieldURL.getText());

				// Call libraryService to persist the changes in the book or add a new book
				if (book.getId() == null) {
					libraryService.registerBook(book);
					Alerts.showAlert("Add Book", null, "Book added.", AlertType.INFORMATION);
				} else {
					libraryService.updateBook(book);
					Alerts.showAlert("Edit Book", null, "Book's information updated.", AlertType.INFORMATION);
				}			
				// Notify listeners about the change in book's data
				Notifier.notifyBookDataChangeListeners();
				// Close window
				Utils.currentStage(event).close();
			}
		}
	}

	/**
	 * Updates and display the error labels in the view, in case there are any.
	 * @param errors A Map<String, String> containing pairs of error identifications 
	 * and messages to be displayed by the error label.
	 */
	private void showErrorLabels(Map<String, String> errors) {
		if (errors.containsKey("title"))
			labelTitleError.setText(errors.get("title"));
		if (errors.containsKey("author"))
			labelAuthorError.setText(errors.get("author"));	
		if (errors.containsKey("year"))
			labelYearError.setText(errors.get("year"));
		if (errors.containsKey("url"))
			labelURLError.setText(errors.get("url"));
		if (errors.containsKey("quantity"))
			labelQuantityError.setText(errors.get("quantity"));
	}

	/**
	 * Hide all error labels from the view.
	 */
	private void resetErrorLabels() {
		labelTitleError.setText("");
		labelAuthorError.setText("");
		labelQuantityError.setText("");
		labelYearError.setText("");
		labelURLError.setText("");		
	}

	/**
	 * Search for any errors in the input values and store them in a map to be 
	 * retrieved.
	 * @return A Map<String, String> containing pairs of error identifications 
	 * and the messages to be displayed by the error label.
	 */
	private Map<String, String> checkInputValuesErrors() {
		Map<String, String> errors = new HashMap<>();

		// txtFieldTitle cannot be null
		String inputTitle = txtFieldTitle.getText();
		if (inputTitle == null || inputTitle.equals(""))
			errors.put("title", "(Can't be empty)");

		// txtFieldAuthor cannot be null
		String inputAuthor = txtFieldAuthor.getText();		
		if (inputAuthor == null || inputAuthor.equals(""))
			errors.put("email", "(Can't be empty)");

		// txtFieldYear cannot be null, less than 1700 or higher than current year
		if (txtFieldYear.getText() != null && !txtFieldYear.getText().equals("")) {
			Integer year = Integer.parseInt(txtFieldYear.getText());
			if ((year < 1700)||(year > Calendar.getInstance().get(Calendar.YEAR)))
				errors.put("year", "(Invalid year)");
		}
		
		// txtFieldQuantity cannot be null nor less than the number of currently issued exemplars
		if (book != null && book.getIssued() > 0) {
			int inputQuantity = Integer.parseInt(txtFieldQuantity.getText());
			if (inputQuantity < book.getIssued())
				errors.put("quantity", "(Can't be less than " + book.getIssued() + ")");
		}
		
		// txtFieldURL cannot be invalid (doesn't point to an available image)		
		String url = txtFieldURL.getText();
		if ((url != null)&&(!url.equals(""))) {
			try {
				// Also, if URL is valid, loads the image on the view
				imageViewPreview.setImage(new Image(url));
				imageViewPreview.setFitHeight(100);
				imageViewPreview.setFitWidth(150);
				Stage stage = (Stage) imageViewPreview.getScene().getWindow();
				stage.setHeight(stage.getHeight() + imageViewPreview.getFitHeight());
			} catch (IllegalArgumentException e) {
				errors.put("url", "(Image not found)");
			}
		} else 
			imageViewPreview.setImage(null);
		return errors;
	}
}
