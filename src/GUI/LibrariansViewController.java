/**
 * This class implements the controller for the view described in LibrariansView.fxml vile.
 * @author Camila Maione
 */
package GUI;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.kordamp.ikonli.javafx.FontIcon;

import GUI.listeners.LibrarianDataChangeListener;
import GUI.listeners.Notifier;
import GUI.listeners.UserNameChangeListener;
import GUI.utils.Alerts;
import GUI.utils.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.entities.Librarian;
import model.services.AdminService;
import model.services.LoginService;

public class LibrariansViewController implements UserNameChangeListener, LibrarianDataChangeListener {

	@FXML protected TableView<Librarian> tableView; // TableView located at the Librarians tab, showing all librarians
												// currently registered
	@FXML protected TableColumn<Librarian, Integer> tableColumnID; // ID column
	@FXML protected TableColumn<Librarian, String> tableColumnName; // Name column
	@FXML protected TableColumn<Librarian, String> tableColumnEmail; // Email column
	@FXML protected TableColumn<Librarian, String> tableColumnAddress; // Address column
	@FXML protected TableColumn<Librarian, String> tableColumnCity; // City column
	@FXML protected TableColumn<Librarian, Long> tableColumnContact; // Contact column
	@FXML protected TableColumn<Librarian, Librarian> tableColumnEdit; // Edit column
	@FXML protected TableColumn<Librarian, Librarian> tableColumnRemove; // Remove column
	@FXML protected FontIcon fontIconAddLibrarian; // Add librarian icon (plus)

	private AdminService adminService = new AdminService();
	private LoginService loginService; 

	/**
	 * Initializes the controller.
	 * @param loginService A LoginService object containing necessary operations for login.
	 */
	public void init(LoginService loginService) {
		this.loginService = loginService;
		Notifier.subscribeUserNameChangeListener(this);
		Notifier.subscribeLibrarianDataChangeListener(this);
		initTableView();
	}

	/**
	 *  Initializes the cells from the TableView and the return book icons.
	 */
	private void initTableView() {
		tableColumnID.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnID.setStyle( "-fx-alignment: CENTER;");
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
		tableColumnCity.setCellValueFactory(new PropertyValueFactory<>("city"));
		tableColumnContact.setCellValueFactory(new PropertyValueFactory<>("contact"));

		tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEdit.setCellFactory(param -> new TableCell<Librarian, Librarian>() {
			private final FontIcon editIcon = new FontIcon();

			@Override
			protected void updateItem(Librarian obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				editIcon.setIconLiteral("fa-pencil-square-o");
				editIcon.setIconSize(22);
				editIcon.setIconColor(Color.web("#333333"));
				setAlignment(Pos.CENTER);
				setCursor(Cursor.HAND);
				setGraphic(editIcon);
				setTooltip(new Tooltip("Edit"));
				editIcon.setOnMouseClicked(event -> onEditLibrarianIconClicked(obj));
			}
		});

		tableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemove.setCellFactory(param -> new TableCell<Librarian, Librarian>() {
			private final FontIcon removeIcon = new FontIcon();

			@Override
			protected void updateItem(Librarian obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				removeIcon.setIconLiteral("fa-trash-o");
				removeIcon.setIconSize(21);
				removeIcon.setIconColor(Color.web("#333333"));
				setAlignment(Pos.CENTER);
				setCursor(Cursor.HAND);
				setGraphic(removeIcon);
				setTooltip(new Tooltip("Delete"));
				removeIcon.setOnMouseClicked(event -> onRemoveIconClicked(obj));
			}
		});

		updateTableView();
	}

	/**
	 * Handler for actions performed when the user clicks on a remove icon in the TableView.
	 * @param obj The librarian to be removed.
	 */
	private void onRemoveIconClicked(Librarian obj) {
		if (obj.getId() == loginService.getUserLogged().getId())
			Alerts.showAlert("Error", null, "You cannot delete yourself.", AlertType.ERROR);
		else {
			Optional<ButtonType> option = Alerts.showConfirmation("Delete librarian",
					"Are you sure to delete this librarian from the system?");
			if (option.get() == ButtonType.OK) {
				adminService.removeLibrarian(obj.getId());
				updateTableView();
			}
		}
	}

	/**
	 * Handler method for actions performed when the user clicks on a edit icon in the TableView.
	 * @param event
	 * @param obj
	 */
	private void onEditLibrarianIconClicked(Librarian obj) {
		if (obj.equals(loginService.getUserLogged())) {
			Alerts.showAlert("Edit Librarian", null, "To edit your own information, please go to Profile Settings.", AlertType.WARNING);
		} else {
			createEditLibrarianFormScreen(obj);
		}
	}
	
	/**
	 * Actions performed when the user clicks on the add icon.
	 */
	@FXML public void onAddLibrarianIconClicked() {
		createAddLibrarianFormScreen();
	}
	
	/**
	 * Loads a secondary, floating window with a form for register a new librarian in the system.
	 */
	private void createAddLibrarianFormScreen() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("LibrarianFormScreen.fxml"));
			AnchorPane root = loader.load();
			LibrarianFormScreenController controller = loader.getController();
			Notifier.subscribeLibrarianDataChangeListener(this);
			controller.init(null);
			Stage stage = Utils.createSecondaryStage("Add Librarian");
			stage.setScene(new Scene(root));
			stage.showAndWait();
		} catch (IOException e) {
			Alerts.showAlert("Error", "Error opening window", e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads a secondary, floating window with a form for editing a librarian's data.
	 * @param obj The Librarian object whose data will be loaded in the window's text fields 
	 * and possibly edited by the user.
	 */
	private void createEditLibrarianFormScreen(Librarian obj) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("LibrarianFormScreen.fxml"));
			AnchorPane root = loader.load();
			LibrarianFormScreenController controller = loader.getController();
			Notifier.subscribeLibrarianDataChangeListener(this);
			controller.init(obj);
			Stage stage = Utils.createSecondaryStage("Edit Librarian");
			stage.setScene(new Scene(root));
			stage.showAndWait();
		} catch (IOException e) {
			Alerts.showAlert("Error", "Error opening window", e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves all librarians registered in the system and display their data in
	 * the TableView. 
	 */
	 public void updateTableView() {
		List<Librarian> allLibrarians = adminService.findAllLibrarians();
		tableView.setItems(FXCollections.observableArrayList(allLibrarians));
	}

	@Override
	public void onLibrarianDataChanged() {
		initTableView();
	}

	@Override
	public void onUserNameChanged() {
		initTableView();
	}
}
