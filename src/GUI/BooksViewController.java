/**
 * This class implements the controller for the view described by BooksView.fxml file.
 * @author Camila Maione
 */

package GUI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.kordamp.ikonli.javafx.FontIcon;

import GUI.listeners.BookDataChangeListener;
import GUI.listeners.IssuesChangeListener;
import GUI.listeners.Notifier;
import GUI.utils.Alerts;
import GUI.utils.Utils;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.entities.Book;
import model.services.LibraryService;

public class BooksViewController implements BookDataChangeListener, IssuesChangeListener {

	// TableView components
	@FXML private TableView<Book> tableView; // TableView located at the Books tab, showing all books currently registered
	@FXML private TableColumn<Book, Integer> tableColumnID; // ID column
	@FXML private TableColumn<Book, String> tableColumnTitle; // Title column
	@FXML private TableColumn<Book, String> tableColumnAuthor; // Author column	
	
	// Components inside the side pane for information regarding the selected book	
	@FXML private Button btEdit; // "Edit" button
	@FXML private Button btIssue; // "Issue" button
	@FXML private Button btRemove; // "Delete" button	
	@FXML private Label labelTitle; // Title
	@FXML private Label labelAuthor; // Author
	@FXML private Label labelPublisher; // Publisher
	@FXML private Label labelEdition; // Edition
	@FXML private Label labelYear; // Year
	@FXML private Label labelNumPages; // Num. of pages
	@FXML private Text textDescription; // Author
	@FXML private Label labelQuantity; // Quantity
	@FXML private Label labelCurrentlyIssued; // Issued
	@FXML private ImageView bookImage; // Book image
	
	@FXML private HBox selectedBookInfo; // HBox containing selected book's info (side pane)
	@FXML private VBox clickOnBook; // Label that appears in the middle of the side pane when no book is selected
	
	// Components inside the "search bar"
	@FXML private FontIcon fontIconAddBook; // FontIcon that adds new books (plus icon)
	@FXML private ComboBox<String> comboBoxFindBy; // ComboBox with options of indexes to search for
	@FXML private TextField txtFieldFindBy; // TextField for user to input search term
	@FXML private FontIcon fontIconFindBy; // FontIcon that performs the search when clicked

	private LibraryService libraryService; // Service that provides model operations for Book

	/**
	 *  Initializes the controller.
	 * @param libraryService LibraryService object containing all model operations 
	 * for Book objects.
	 */
	public void init(LibraryService libraryService) {
		this.libraryService = libraryService;
		/* Make this controller listen to any changes made to Book or Issue objects
		 * stored in the system. */
		Notifier.subscribeBookDataChangeListener(this);
		Notifier.subscribeIssuesChangeListener(this);		
		initTableView();
		resetBookInfoPane();
		initFindBar();
	}

	/**
	 *  Initializes the cells in the TableView.
	 */
	private void initTableView() {
		tableColumnID.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnID.setStyle( "-fx-alignment: CENTER;");
		tableColumnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
		tableColumnAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
		updateTableView();
	}

	/**
	 *  Initializes the nodes in the search bar.
	 */
	private void initFindBar() {
		comboBoxFindBy.setItems(FXCollections.observableArrayList(List.of("Author", "Title")));
		comboBoxFindBy.getSelectionModel().select(0);
	}
	
	/**
	 *  Actions performed when the user clicks on a cell from tableView.
	 */
	@FXML public void onTableViewClick() {
		// Turn visible the side pane that shows the information for the selected book
		selectedBookInfo.setVisible(true);
		/* Get the Book object from the table selection model and present its information
		 * on the side pane */
		Book obj = tableView.getSelectionModel().getSelectedItem();		
		obj = libraryService.findBook(obj.getId());
		setBookInfoPane(obj);		
	}
	
	/**
	 * Presents a Book object's data on the side pane.
	 * @param obj Book object whose data will be displayed in the side pane.
	 */
	private void setBookInfoPane(Book obj) {
		selectedBookInfo.setVisible(true);
		clickOnBook.setVisible(false);
		labelTitle.setText(obj.getTitle());
		labelAuthor.setText(obj.getAuthor());
		labelPublisher.setText(obj.getPublisher());
		labelEdition.setText(String.valueOf(obj.getEdition()));
		labelYear.setText(String.valueOf(obj.getYear()));
		labelNumPages.setText(String.valueOf(obj.getNumPages()));
		textDescription.setText(obj.getDescription());
		labelQuantity.setText(String.valueOf(obj.getQuantity()));
		labelCurrentlyIssued.setText(String.valueOf(obj.getIssued()));
		if (obj.getImageUrl() != null && !obj.getImageUrl().equals(""))
			bookImage.setImage(new Image(obj.getImageUrl()));
		else
			bookImage.setImage(null);
		if (obj.getQuantity() == obj.getIssued())
			btIssue.setVisible(false);
		else
			btIssue.setVisible(true);
	}
	
	/**
	 * Reset the side pane back to idle state.
	 */
	private void resetBookInfoPane() {
		selectedBookInfo.setVisible(false);
		clickOnBook.setVisible(true);
	}

	/**
	 * Actions performed whenever the user types a key in the search text field.
	 */
	@FXML public void onFontIconFindByClicked() {
		/* If txtFieldFindBy is not empty, search the database for any book whose
		 * author or title contains the string written on the text field */		
		if (txtFieldFindBy.getText() != null && !txtFieldFindBy.getText().equals("")) {			
			String findBy = comboBoxFindBy.getSelectionModel().getSelectedItem();			
			if (findBy.equals("Author")) {
				String term = txtFieldFindBy.getText();
				List<Book> matchedBooks = new ArrayList<>();
				List<Book> allBooks = libraryService.findAllBooks();
				for (Book book : allBooks) {
					if (book.getAuthor().toUpperCase().contains(term.toUpperCase()))
						matchedBooks.add(book);
				}
				tableView.setItems(FXCollections.observableArrayList(matchedBooks));
			} else if (findBy.equals("Title")) {
				String term = txtFieldFindBy.getText();
				List<Book> matchedBooks = new ArrayList<>();
				List<Book> allBooks = libraryService.findAllBooks();
				for (Book book : allBooks) {
					if (book.getTitle().toUpperCase().contains(term.toUpperCase()))
						matchedBooks.add(book);
				}
				tableView.setItems(FXCollections.observableArrayList(matchedBooks));
			}
		// Otherwise, tableView must show all the book objects stored
		} else {			
			updateTableView();
		}
	}

	/**
	 * Actions performed whenever the user fires the Edit button inside the side pane.
	 * @param event ActionEvent originated from the button.
	 */
	@FXML public void onBtEditAction(ActionEvent event) {	
		Book obj = tableView.getSelectionModel().getSelectedItem();
		obj = libraryService.findBook(obj.getId());
		
		if (obj.getIssued() > 1)
			Alerts.showAlert("Edit Book - Warning", null, "This book has a total of " + obj.getIssued() + 
					" exemplars currently issued. The quantity informed can't be inferior to this number.", AlertType.WARNING);
		// Loads a secondary window with a form 
		createEditBookFormScreen(obj);
	}

	/**
	 * Actions performed whenever the user clicks on the Add icon.
	 * @param event MouseEvent originated from the icon.
	 */
	@FXML public void onAddIconClicked(MouseEvent event) {
		// Loads a secondary window with a form 
		createAddBookFormScreen();
	}

	/**
	 * Actions performed whenever the user fires the Issue button inside the side pane.
	 * @param event ActionEvent originated from the button.
	 */
	@FXML public void onBtIssueAction(ActionEvent event) {
		Book book = tableView.getSelectionModel().getSelectedItem();
		book = libraryService.findBook(book.getId());
		// Loads a secondary window with a form 
		createIssueFormScreen(book);
		// Change the labelCurrentlyIssued on the side pane to show the updated value
		labelCurrentlyIssued.setText(String.valueOf(book.getIssued()));
	}

	/**
	 * Loads a secondary, floating window with a form for register a new book in the system.
	 */
	private void createAddBookFormScreen() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("BookFormScreen.fxml"));
			AnchorPane root = loader.load();
			BookFormScreenController controller = loader.getController();			
			controller.init(libraryService, null);
			Stage stage = Utils.createSecondaryStage("Add Book");
			stage.setScene(new Scene(root));			
			stage.showAndWait();
		} catch (IOException e) {
			Alerts.showAlert("Error", "Error opening window", e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads a secondary, floating window with a form for editing a book's data.
	 * @param obj The Book object whose data will be loaded in the window's text fields 
	 * and possibly edited by the user.
	 */
	private void createEditBookFormScreen(Book obj) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("BookFormScreen.fxml"));
			AnchorPane root = loader.load();
			BookFormScreenController controller = loader.getController();			
			controller.init(libraryService, obj);
			Stage stage = Utils.createSecondaryStage("Edit Book");
			stage.setScene(new Scene(root));			
			stage.showAndWait();
		} catch (IOException e) {
			Alerts.showAlert("Error", "Error opening window", e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		}
	}	

	/**
	 * Actions performed when the user clicks on the Delete button inside the side pane.
	 * @param event ActionEvent originated from the button.
	 */
	@FXML public void onBtRemoveLabelAction(ActionEvent event) {
		// Get the book object selected in tableView
		Book obj = tableView.getSelectionModel().getSelectedItem();
		obj = libraryService.findBook(obj.getId());	
		// If book is issued, inform user 
		if (obj.getIssued() > 0) {
			Alerts.showAlert("Error removing book", null,
					"This book can't be removed because there are one or more exemplars currently issued.",
					AlertType.ERROR);
		// Otherwise and upon confirmation from the user, proceed to delete
		} else {
			Optional<ButtonType> option = Alerts.showConfirmation("Remove book",
					"Are you sure to delete this book from system?");
			if (option.get() == ButtonType.OK) {
				libraryService.removeBook(obj.getId());
				updateTableView();
			}
		}
	}
	
	/**
	 * Loads a secondary, floating window with a form for book issuing.
	 * @param obj The Book object to be issued and whose data will be loaded to the
	 * text fields.
	 */
	private void createIssueFormScreen(Book obj) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("IssueFormScreen.fxml"));
			AnchorPane root = loader.load();
			IssueFormScreenController controller = loader.getController();
			controller.init(libraryService, obj);
			Stage stage = Utils.createSecondaryStage("Issue Book");
			stage.setScene(new Scene(root));		
			stage.showAndWait();
		} catch (IOException e) {
			Alerts.showAlert("Error", "Error opening window", e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves all Book objects stored in the system and display their 
	 * data in tableView.
	 */
	public void updateTableView() {
		List<Book> allBooks = libraryService.findAllBooks();
		tableView.setItems(FXCollections.observableArrayList(allBooks));		
	}
	
	@Override
	public void onBookDataChanged() {
		// Updates the information displayed in the side pane
		Book book = tableView.getSelectionModel().getSelectedItem();
		if (book != null)
			book = libraryService.findBook(book.getId());
		setBookInfoPane(book);
		// Updates the tableView
		initTableView();
	}

	@Override
	public void onIssuesChanged() {
		Book book = tableView.getSelectionModel().getSelectedItem();
		if (book != null) {
			book = libraryService.findBook(book.getId());
			// If all exemplars from the selected book are issued, hide the Issue button from the side pane
			if (book.getQuantity() == book.getIssued())
				btIssue.setVisible(false);
			else
				btIssue.setVisible(true);
		}		
	}
}
