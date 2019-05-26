/**
 * This class implements the controller for the view described in IssuesView.fxml file.
 * @author Camila Maione
 */
package GUI;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.kordamp.ikonli.javafx.FontIcon;

import GUI.listeners.BookDataChangeListener;
import GUI.listeners.IssuesChangeListener;
import GUI.listeners.Notifier;
import GUI.utils.Alerts;
import GUI.utils.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import model.entities.Book;
import model.entities.Issue;
import model.entities.Student;
import model.services.LibraryService;

public class IssuesViewController implements IssuesChangeListener, BookDataChangeListener {
	
	// Components of the table
	@FXML protected TableView<IssueWrapper> tableView; // TableView showing all issues currently registered
	@FXML protected TableColumn<IssueWrapper, Integer> tableColumnID; // ID column
	@FXML protected TableColumn<IssueWrapper, String> tableColumnBookTitle; // Book title column
	@FXML protected TableColumn<IssueWrapper, String> tableColumnName; // Student name column
	@FXML protected TableColumn<IssueWrapper, String> tableColumnEmail; // Student email column
	@FXML protected TableColumn<IssueWrapper, Long> tableColumnContact; // Student contact column
	@FXML protected TableColumn<IssueWrapper, Date> tableColumnIssueDate; // Issue date column
	@FXML protected TableColumn<IssueWrapper, IssueWrapper> tableColumnReturn; // Column for the return icons
	
	// Components of the search bar
	@FXML protected ComboBox<String> comboBoxFindBy;
	@FXML protected TextField txtFieldFindBy;
	@FXML protected FontIcon fontIconFindBy;
	
	private LibraryService libraryService; // LibraryService object containing model operations
	
	/**
	 * Initializes the controller.	
	 * @param libraryService A LibraryService object containing the necessary model operations.
	 */
	public void init(LibraryService libraryService) {
		this.libraryService = libraryService;
		// Make this controller listen to any changes made to stored issues and books
		Notifier.subscribeIssuesChangeListener(this);
		Notifier.subscribeBookDataChangeListener(this);
		initTableView();
		initSearchBar();
	}
	
	/**
	 * Initializes the cells from tableView and the return book icons.
	 */
	private void initTableView() {
		tableColumnID.setCellValueFactory(obj -> new ReadOnlyObjectWrapper<>(obj.getValue().getIssue().getId()));
		tableColumnID.setStyle( "-fx-alignment: CENTER;");
		tableColumnBookTitle.setCellValueFactory(obj -> new ReadOnlyObjectWrapper<>(obj.getValue().getBook().getTitle()));
		tableColumnName.setCellValueFactory(obj -> new ReadOnlyObjectWrapper<>(obj.getValue().getStudent().getName()));
		tableColumnEmail.setCellValueFactory(obj -> new ReadOnlyObjectWrapper<>(obj.getValue().getStudent().getEmail()));	
		tableColumnContact.setCellValueFactory(obj -> new ReadOnlyObjectWrapper<>(obj.getValue().getStudent().getContact()));
		tableColumnIssueDate.setCellValueFactory(obj -> new ReadOnlyObjectWrapper<>(obj.getValue().getIssue().getIssueDate()));		
		tableColumnIssueDate.setCellFactory(column -> {
		    TableCell<IssueWrapper, Date> cell = new TableCell<IssueWrapper, Date>() {
		        @Override
		        protected void updateItem(Date item, boolean empty) {
		            super.updateItem(item, empty);
		            if(empty)
		                setText(null);
		            else
		                setText(Utils.getDateFormat().format(item));
		        }
		    };
		    return cell;
		});		
		tableColumnReturn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnReturn.setCellFactory(param -> new TableCell<IssueWrapper, IssueWrapper>() {
			private final FontIcon returnIcon = new FontIcon();			
			@Override
			protected void updateItem(IssueWrapper obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				returnIcon.setIconLiteral("fa-mail-reply");
				returnIcon.setIconSize(20);
				returnIcon.setIconColor(Color.web("#333333"));
				setCursor(Cursor.HAND);				
				setGraphic(returnIcon);
				setTooltip(new Tooltip("Return book"));
				//button.setOnAction(event -> createDialogForm(obj, "/gui/DepartmentForm.fxml", Utils.currentStage(event)));
				returnIcon.setOnMouseClicked(event -> onReturnBookIconClicked(obj));
			}
		});
		updateTableView();
	}
	
	/**
	 * Initializes the nodes in the search bar.
	 */
	private void initSearchBar() {
		comboBoxFindBy.setItems(FXCollections.observableArrayList(List.of("Book Title", "Name")));
		comboBoxFindBy.getSelectionModel().select(0);		
	}

	/**
	 * Actions performed when the user clicks on the search icon.
	 */
	@FXML public void onFontIconFindByClicked() {		
		/* If user didn't provide an empty search string, search the storage for any issues whose
		 * book title or student name matches the given string. */
		if (txtFieldFindBy.getText() != null && !txtFieldFindBy.getText().equals("")) {			
			String findBy = comboBoxFindBy.getSelectionModel().getSelectedItem();
			String term = txtFieldFindBy.getText();
			List<IssueWrapper> issuesWrappers = getIssueWrapperList(libraryService.findAllIssues());
			List<IssueWrapper> matched = new ArrayList<>();
			if (findBy.equals("Book Title")) {
				for (IssueWrapper iw : issuesWrappers) {
					if (iw.getBook().getTitle().toUpperCase().contains(term.toUpperCase()))
						matched.add(iw);
				}
				tableView.setItems(FXCollections.observableArrayList(matched));
			} else if (findBy.equals("Name")) {
				for (IssueWrapper iw : issuesWrappers) {
					if (iw.getStudent().getName().toUpperCase().contains(term.toUpperCase()))
						matched.add(iw);
				}
				tableView.setItems(FXCollections.observableArrayList(matched));
			}
		// Otherwise, simply update tableView to show all Issue objects stored
		} else {			
			updateTableView();
		}
	}
 
	/**
	 * Actions performed when the user clicks on a return book icon in tableView.
	 * @param obj The IssueWrapper object referenced in the same row as the clicked
	 * icon.
	 */
	private void onReturnBookIconClicked(IssueWrapper obj) {
		Issue issue = obj.getIssue();
		Optional<ButtonType> option = Alerts.showConfirmation("Return Book", "Are you sure to return book?");
		if (option.get() == ButtonType.OK) {
			libraryService.returnBook(issue);
			updateTableView();
			Alerts.showAlert("Return Book", null, "Book \"" + obj.getBook().getTitle() + "\" returned from: \n" + 
					obj.getStudent().getName() + " (ID: " + obj.getStudent().getId() + ")", AlertType.INFORMATION);
			Notifier.notifyIssuesChangeListeners(); // Notify listeners about a new change made to a stored issue
		}
	}
	
	/**
	 * Retrieves all issues registered and updates the tableView to display their data.
	 */
	public void updateTableView() {
		List<Issue> allIssues = libraryService.findAllIssues();		
		tableView.setItems(FXCollections.observableArrayList(getIssueWrapperList(allIssues)));
	}
	
	/**
	 * Converts a list of Issue objects in a list of IssueWrapper objects.
	 * @param allIssues List of Issue objects to be converted.
	 * @return A list of IssueWrapper objects.
	 */
	public List<IssueWrapper> getIssueWrapperList(List<Issue> allIssues) {
		List<IssueWrapper> list = new ArrayList<>();		
		for (Issue issue : allIssues) {
			Book book = libraryService.findBook(issue.getBookId());
			Student student = libraryService.findStudent(issue.getStudentId());
			list.add(new IssueWrapper(issue, book, student));
		}			
		return list;
	}

	@Override
	public void onIssuesChanged() {
		initTableView();		
	}

	@Override
	public void onBookDataChanged() {
		initTableView();		
	}
}
