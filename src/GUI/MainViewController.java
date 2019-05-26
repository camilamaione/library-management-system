/**
 * This class implements the controller for the view described by MainView.fxml file.
 * @author Camila Maione
 */
package GUI;

import java.io.IOException;
import java.util.function.Consumer;

import org.kordamp.ikonli.javafx.FontIcon;

import GUI.listeners.Notifier;
import GUI.listeners.UserLoginListener;
import GUI.listeners.UserNameChangeListener;
import GUI.utils.Alerts;
import GUI.utils.Utils;
import application.LibraryManagementSystem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.services.LibraryService;
import model.services.LoginService;

public class MainViewController implements UserNameChangeListener, UserLoginListener {
	
	// User bar
	@FXML private Label labelUserLogged; // Label showing the currently logged user's name.
	@FXML private FontIcon fontIconLogout; // Logout icon
	@FXML private FontIcon fontIconProfileSettings; // Profile settings icon
	
	// Tool bar
	@FXML private ImageView imgIcon; // Image at the beginning of the option bar	
	@FXML private Label labelIssues; // Label Issues
	@FXML private Rectangle recIssues; // Rectangle beside labelIssues
	@FXML private Label labelBooks; // Label Books
	@FXML private Rectangle recBooks; // Rectangle beside labelBooks
	@FXML private Label labelLibrarians; // Label Librarians
	@FXML private Rectangle recLibrarians; // Rectangle beside labelLibrarians
	
	@FXML private HBox librariansHBox; // Parent of all nodes composing the tool bar 
	@FXML private AnchorPane paneToolBar; // Parent of all nodes to be shown in the "main" region of the view
		
	private Stage primaryStage; // Primary stage of the application
	private final LoginService loginService = new LoginService(); // Login service
	private final LibraryService libraryService = new LibraryService(); // Library service
	private Font defaultLabelFont = Font.font("System", FontWeight.NORMAL, 12); // Default font for unselected option in the toolbar
	private Font highlightedLabelFont = Font.font("System", FontWeight.BOLD, 12); // Font for selected option in the toolbar
		
	/**
	 * Initializes the controller.
	 */
	public void init() {
		this.primaryStage = LibraryManagementSystem.getPrimaryStage();
		imgIcon.setImage(Utils.getWindowIcon());
		imgIcon.setFitHeight(50);
		imgIcon.setFitWidth(50);
		// Make this controller listen to any changes made to the name of the currently logged librarian.
		Notifier.subscribeUserLoginListener(this);
		// Initializes visible nodes as if the user just logged out from the application.
		onIconLogoutClicked();
	}
	
	/**
	 * Changes the view displayed below the tool bar. 
	 * @param viewUrl The address of the FXML file describing the new view.
	 * @param initialAction Initial operations to be done by the controller of the
	 * loaded view.
	 */
	private <T> void changeView(String viewUrl, Consumer<T> initialAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(viewUrl));
			AnchorPane newPane = loader.load();			
			AnchorPane root = (AnchorPane)primaryStage.getScene().getRoot();
			VBox vbox = (VBox) root.getChildren().get(0);
			AnchorPane pane = (AnchorPane) vbox.getChildren().get(2);
			pane.getChildren().clear();
			pane.getChildren().addAll(newPane.getChildren());
			newPane.setVisible(true);			
			T controller = loader.getController();
			initialAction.accept(controller);
		} catch (IOException e) {
			Alerts.showAlert("Error", null, e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		}
	}

	/**
	 * Actions performed when the user clicks on the label Issues in the tool bar.
	 */
	@FXML public void onIssuesClicked() {
		// Highlight Issues label and change the view. 
		highlightHBox((HBox)labelIssues.getParent());
		changeView("/GUI/IssuesView.fxml", (IssuesViewController controller) -> controller.init(libraryService));		
	}
	
	/**
	 * Actions performed when the user clicks on the label Books in the tool bar.
	 */
	@FXML public void onBooksClicked() {
		// Highlight Books label and change the view.
		highlightHBox((HBox)labelBooks.getParent());
		changeView("/GUI/BooksView.fxml", (BooksViewController controller) -> controller.init(libraryService));
	}
	
	/**
	 * Actions performed when the user clicks on the label Librarians in the tool bar.
	 */
	@FXML public void onLibrariansClicked() {		
		// Highlight Librarians label and change the view.
		highlightHBox((HBox)labelLibrarians.getParent());
		changeView("/GUI/LibrariansView.fxml", (LibrariansViewController controller) -> controller.init(loginService));
	}
	
	/**
	 * Highlights the label whose view is currently loaded. 
	 */
	private void highlightHBox(HBox hbox) {
		resetToolBar();
		Rectangle rec = (Rectangle) hbox.getChildren().get(0);
		Label label = (Label) hbox.getChildren().get(1);
		rec.setVisible(true);
		label.setFont(highlightedLabelFont);
	}
	
	/**
	 * Remove highlights from all labels in the tool bar. 
	 */
	private void resetToolBar() {		
		labelIssues.setFont(defaultLabelFont);
		labelBooks.setFont(defaultLabelFont);
		labelLibrarians.setFont(defaultLabelFont);
		recIssues.setVisible(false);
		recBooks.setVisible(false);
		recLibrarians.setVisible(false);		
	}
	
	/**
	 * Actions performed when the user clicks on the Logout icon. 
	 */
	@FXML public void onIconLogoutClicked() {
		// Set currently logged user to null
		loginService.setUserLogged(null);	
		// Hide nodes
		paneToolBar.setVisible(false);
		labelUserLogged.setText("");	
		hideLibrariansAccess();
		// Change view back to login screen
		changeView("LoginScreen.fxml", (LoginScreenController controller) -> controller.init(loginService));
	}
	
	/**
	 * Actions performed when the user clicks on the Profile Settings icon.  
	 */
	@FXML public void onIconProfileSettingsClicked() {
		try {
			// Try to load the profile settings view
			FXMLLoader loader = new FXMLLoader(MainViewController.class.getResource("LibrarianFormScreen.fxml"));
			AnchorPane root = loader.load();			
			Scene scene = new Scene(root);			
			Stage stage = Utils.createSecondaryStage("Profile Settings");
			stage.setScene(scene);
			
			// Send library and login services to the ProfileSettingsView controller
			LibrarianFormScreenController controller = loader.getController();
			controller.init(loginService.getUserLogged());
			
			/* Make this controller listen to any changes possibly made by ProfileSettingsView 
			 * to the currently logged user's name */
			Notifier.subscribeUserNameChangeListener(this);		
			stage.showAndWait();
		} catch(Exception e) {
			Alerts.showAlert("Error", "Error logging out", e.getMessage(), AlertType.ERROR);
		}
	}
		
	@Override
	public void onUserNameChanged() {		
		labelUserLogged.setText("[ Welcome, " + loginService.getUserLogged().getName() + " ]");
	}
	
	/**
	 * Hides the Librarians option from the tool bar, typically used when the currently
	 * logged user is not administrator and therefore cannot manage librarians.	 
	 */
	private void hideLibrariansAccess() {
		librariansHBox.setVisible(false);
	}
	
	/**
	 * Turns the Librarians option visible in the tool bar, typically used when the currently
	 * logged user is administrator.
	 */
	private void showLibrariansAccess() {
		librariansHBox.setVisible(true);
	}

	@Override
	public void onUserLogged() {		
		paneToolBar.setVisible(true);
		labelUserLogged.setText("[ Welcome, " + loginService.getUserLogged().getName() + " ]");	
		if (!loginService.getUserLogged().isAdmin())
			hideLibrariansAccess();
		else
			showLibrariansAccess();
		onIssuesClicked(); // Loads the Issues view first
	}
}
