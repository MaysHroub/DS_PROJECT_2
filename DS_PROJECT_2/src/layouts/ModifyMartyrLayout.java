package layouts;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import data.Martyr;
import data.MartyrDate;
import data.MartyrStat;
import data_holder.DataHolder;
import data_structs.linkedlist.Node;
import data_structs.tree.TNode;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class ModifyMartyrLayout extends TabLayout {

	private Label statusL;
	private Button insertBtn, deleteBtn, updateBtn, searchBtn;
	private TextField nameTF, updateTF, searchTF;
	private ComboBox<Martyr> martyrCB;
	private ComboBox<Integer> ageCB;
	private ComboBox<Character> genderCB;
	private TableView<Martyr> martyrTable;
	private Alert alert;
	private DatePicker datePicker;


	public ModifyMartyrLayout(DataHolder dataHolder) {
		super("Modify Martyrs", dataHolder);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Pane createLayout() {
		alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation required");
		alert.setHeaderText("Are you sure you want to proceed?");
		alert.setContentText("This action cannot be undone :)");
		
		martyrTable = new TableView<>();
		TableColumn<Martyr, String> nameColumn = new TableColumn<>("Name");
		TableColumn<Martyr, Integer> ageColumn = new TableColumn<>("Age");
		TableColumn<Martyr, String> genderColumn = new TableColumn<>("Gender");
		nameColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getName()));
		ageColumn.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getAge()).asObject());
		genderColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getGender() + ""));
		martyrTable.getColumns().addAll(nameColumn, ageColumn, genderColumn);
		martyrTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
		
		Label enterName = new Label("Enter the martyr's name: "),
				selectAge = new Label("Select the martyr's age: "),
				selectGender = new Label("Select the martyr's gender: "),
				selectDate = new Label("Select the martyr's event date: "),
				selectL = new Label("Select a martyr: "),
				enterNewNameL = new Label("Enter a new name for the selected martyr: "),
				searchByNameL = new Label("Enter the martyr's name (or part of it) : ");
		statusL = new Label("status");

		insertBtn = new Button("Insert");
		deleteBtn = new Button("Delete selected martyr");
		updateBtn = new Button("Update Name");
		searchBtn = new Button("Search");
		
		insertBtn.setOnAction(e -> insertMartyr());
		deleteBtn.setOnAction(e -> deleteMartyr());
		updateBtn.setOnAction(e -> updateMartyrName());
		searchBtn.setOnAction(e -> searchMartyr());

		nameTF = new TextField();
		updateTF = new TextField();
		searchTF = new TextField();

		martyrCB = new ComboBox<>();
		// fillMartyrsCB();
		ageCB = new ComboBox<>();
		fillAgeCB();
		genderCB = new ComboBox<>();
		genderCB.getItems().addAll('F', 'M');
		
		datePicker = new DatePicker();

		GridPane gp = new GridPane(15, 15);
		gp.setAlignment(Pos.CENTER);

		gp.add(enterName, 0, 0);
		gp.add(nameTF, 1, 0);
		gp.add(insertBtn, 2, 0);
		gp.add(selectAge, 0, 1);
		gp.add(ageCB, 1, 1);
		gp.add(selectGender, 0, 2);
		gp.add(genderCB, 1, 2);
		gp.add(selectDate, 0, 3);
		gp.add(datePicker, 1, 3);
		gp.add(selectL, 0, 4);
		gp.add(martyrCB, 1, 4);
		gp.add(deleteBtn, 0, 5);
		gp.add(enterNewNameL, 0, 6);
		gp.add(updateTF, 1, 6);
		gp.add(updateBtn, 2, 6);
		gp.add(searchByNameL, 0, 7);
		gp.add(searchTF, 1, 7);
		gp.add(searchBtn, 2, 7);
		gp.add(statusL, 1, 8);
		
		HBox layout = new HBox(20, gp, martyrTable);
		layout.setPadding(new Insets(10));
		layout.setAlignment(Pos.CENTER);
		return layout;
	}

	// related to the current martyr-date object
	private void searchMartyr() {  
		// if there's no data at all -> do nothing
		if (getDataHolder().getCurrentDistrict() == null ||
				getDataHolder().getCurrentLocation() == null ||
				getDataHolder().getCurrentMartyrDate() == null) 
			return;
		
		if (searchTF.getText() == null || searchTF.getText().equals("")) {
			statusL.setText("Text field is empty");
			return;
		}
		
		Node<Martyr> curr = getDataHolder()
				.getCurrentMartyrDate()
				.getMartyrs()
				.getHead();
		
		martyrTable.setItems(null);
		ObservableList<Martyr> allMatches = FXCollections.observableArrayList();
		while (curr != null) {  // O(M)
			if (curr.getData().getName().toLowerCase().contains(searchTF.getText().toLowerCase())) 
				allMatches.add(curr.getData());
			
			curr = curr.getNext();
		}
		martyrTable.setItems(allMatches);
		martyrTable.refresh();
	}

	// related to the current martyr-date object
	private void updateMartyrName() { 
		if (updateTF.getText() == null || updateTF.getText().equals("")) {
			statusL.setText("Text field is empty");
			return;
		}
		if (martyrCB.getValue() == null) {
			statusL.setText("No martyr is selected");
			return;
		}
		// delete then insert because it's a linked-list not a DLL
		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			
			Martyr updatedMartyr = martyrCB.getValue();
			
			getDataHolder()
			.getCurrentMartyrDate()
			.getMartyrs()
			.find(updatedMartyr)
			.getData()
			.setName(updateTF.getText());
			
			martyrCB.getItems().remove(updatedMartyr); 
			updatedMartyr.setName(updateTF.getText());
			martyrCB.getItems().add(updatedMartyr);  
			statusL.setText("Martyr name is updated");
		}
	}

	// related to the current martyr-date object
	private void deleteMartyr() { 
		Martyr selectedMartyr = martyrCB.getValue();
		if (selectedMartyr == null) {
			statusL.setText("No martyr is selected");
			return;
		}
		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			getDataHolder().getCurrentMartyrDate().getMartyrs().deleteByEquals(selectedMartyr);
			martyrCB.getItems().remove(selectedMartyr);
			martyrTable.getItems().remove(selectedMartyr);
			statusL.setText("Martyr  " + selectedMartyr + "  is deleted");
			if (getDataHolder().getCurrentMartyrDate().getMartyrs().length() == 0) {
				getDataHolder().getCurrentLocation().getMaryrDates().delete(getDataHolder().getCurrentMartyrDate());
				getDataHolder().deleteFromDateNavStack(getDataHolder().getCurrentMartyrDate());
			}
			updateStats();  
		}
	}

	// inserting a martyr is related more to the current location
	// since we're specifying it's date of death
	private void insertMartyr() { 
		// if there's no data at all -> do nothing
		if (getDataHolder().getDistricts().isEmpty() ||
				getDataHolder().getCurrentDistrict().getLocations().isEmpty()) 
			return;
		
		if (isAnyEmpty(nameTF, ageCB, genderCB, datePicker)) {
			statusL.setText("Martyr is not inserted. No enough information.");
			return;
		}
		String name = nameTF.getText();
		int age = ageCB.getValue();
		char gender = genderCB.getValue();
		Martyr martyr = new Martyr(name, age, gender);
		
		LocalDate selectedDate = datePicker.getValue();
		@SuppressWarnings("deprecation")
		Date date = new Date(selectedDate.getYear() - 1900, selectedDate.getMonthValue() - 1, selectedDate.getDayOfMonth());
		
		MartyrDate martyrDate = new MartyrDate(date);
		TNode<MartyrDate> node = getDataHolder().getCurrentLocation().getMaryrDates().find(martyrDate);
		
		if (node == null) {  // martyr-date does not exist
			martyrDate.setStat(new MartyrStat(martyrDate));
			getDataHolder().getCurrentLocation().getMaryrDates().insert(martyrDate);
			getDataHolder().addToDateNavStack(martyrDate);
		}
		else 
			martyrDate = node.getData();
		
		martyrDate.getMartyrs().insertSorted(martyr);
		
		if (getDataHolder().getCurrentMartyrDate() == martyrDate)
			martyrCB.getItems().add(martyr);
		
		statusL.setText("Martyr  " + name + "  is inserted");
		nameTF.setText("");
		ageCB.getSelectionModel().clearSelection();
		genderCB.getSelectionModel().clearSelection();

		martyrDate.getStat().updateStats();  // update the stats in case the martyr-date object is not the current one
		
		updateStats();
	}
	
	private void updateStats() { 
		if (getDataHolder().getCurrentMartyrDate() != null)
			getDataHolder().getCurrentMartyrDate().getStat().updateStats();
		getDataHolder().getCurrentLocation().getStat().updateStats();
		getDataHolder().getCurrentDistrict().getStat().updateStats();
	}

	@Override
	public void updateContent() {  
		if (getDataHolder().getCurrentDistrict() == null ||
				getDataHolder().getCurrentLocation() == null ||
				getDataHolder().getCurrentMartyrDate() == null) {
			martyrCB.getItems().clear();
			return;
		}
		// update the combo box
		martyrCB.getItems().clear();
		fillMartyrsCB();  // O(M)
		statusL.setText("");
	}
	
	private void fillMartyrsCB() { 
//		fillMartyrsCB(getDataHolder().getCurrentLocation().getData().getMaryrDates().getRoot());
		Node<Martyr> curr = getDataHolder().getCurrentMartyrDate().getMartyrs().getHead();
		while (curr != null) {
			martyrCB.getItems().add(curr.getData());
			curr = curr.getNext();
		}
	}

//	private void fillMartyrsCB(TNode<MartyrDate> curr) { 
//		if (curr == null) return;
//		fillMartyrsCB(curr.getLeft());
//		Node<Martyr> currMartyr = curr.getData().getMartyrs().getHead();
//		while (currMartyr != null) {
//			martyrCB.getItems().add(currMartyr.getData());
//			currMartyr = currMartyr.getNext();
//		}
//		fillMartyrsCB(curr.getRight());
//	}

	private void fillAgeCB() {
		for (int i = 0; i <= 150; i++) 
			ageCB.getItems().add(i); 
	} 
	
	private boolean isAnyEmpty(javafx.scene.Node... nodes) {
		for (javafx.scene.Node node : nodes) 
			if (node instanceof TextField) 
				if (((TextField) node).getText() == null || ((TextField) node).getText().equals(""))
					return true;
			
			else if (node instanceof ComboBox) 
				if (((ComboBox<?>) node).getValue() == null) 
					return true;
		
			else if (node instanceof DatePicker) 
				if (((DatePicker) node).getValue() == null)
					return true;
		return false;
	}

}
