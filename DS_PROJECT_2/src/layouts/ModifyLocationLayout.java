package layouts;

import java.util.Optional;
import data.Location;
import data.LocationStat;
import data_holder.DataHolder;
import data_structs.tree.TNode;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class ModifyLocationLayout extends TabLayout {

	private Label statusL;
	private Button insertBtn, deleteBtn, updateBtn;
	private TextField insertTF, updateTF;
	private ComboBox<Location> locationsCB;
	private Alert alert;
	
	public ModifyLocationLayout(DataHolder dataHolder) {
		super("Modify Locations", dataHolder);
	}

	
	@Override
	protected Pane createLayout() {
		alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation required");
		alert.setHeaderText("Are you sure you want to proceed?");
		alert.setContentText("This action cannot be undone :)");
		
		Label enterName = new Label("Enter the location name: "),
				selectL = new Label("Select a location: "),
				enterNewNameL = new Label("Enter the new location name: ");
		statusL = new Label("status");
		
		insertBtn = new Button("Insert");
		deleteBtn = new Button("Delete selected location");
		updateBtn = new Button("Update");
		
		insertBtn.setOnAction(e -> insertLocation());
		deleteBtn.setOnAction(e -> deleteLocation());
		updateBtn.setOnAction(e -> updateLocation());
		
		insertTF = new TextField();
		updateTF = new TextField();
		
		locationsCB = new ComboBox<>();
		fillLocationsCB();
		
		GridPane layout = new GridPane(15, 15);
		layout.setPadding(new Insets(15));
		layout.setAlignment(Pos.CENTER);
		
		layout.add(enterName, 0, 0);
		layout.add(insertTF, 1, 0);
		layout.add(insertBtn, 2, 0);
		layout.add(selectL, 0, 2);
		layout.add(locationsCB, 1, 2);
		layout.add(deleteBtn, 0, 3);
		layout.add(enterNewNameL, 0, 4);
		layout.add(updateTF, 1, 4);
		layout.add(updateBtn, 2, 4);
		layout.add(statusL, 1, 5);
		
		return layout;
	}


	private void updateLocation() {
		if (getDataHolder().getDistricts().isEmpty()) return;
		
		if (updateTF.getText() == null || updateTF.getText().equals("")) {
			statusL.setText("Text field is empty");
			return;
		}
		if (locationsCB.getValue() == null) {
			statusL.setText("No location is selected");
			return;
		}
		if (getDataHolder()
				.getCurrentDistrict()
				.getLocations()
				.find(new Location(updateTF.getText())) != null) {
			statusL.setText("Location name is already taken");
			return;
		}
		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			TNode<Location> deletedNode = getDataHolder().getCurrentDistrict().getLocations().delete(locationsCB.getValue()); 
			locationsCB.getItems().remove(deletedNode.getData()); 
			deletedNode.getData().setName(updateTF.getText());
			getDataHolder().getCurrentDistrict().getLocations().insert(deletedNode.getData());
			locationsCB.getItems().add(deletedNode.getData()); 
			statusL.setText("Location name is updated");
			getDataHolder().fillLocationNavStacks();
		}
	}


	private void deleteLocation() { 
		if (getDataHolder().getDistricts().isEmpty()) return;
		
		Location selectedLocation = locationsCB.getValue();
		if (selectedLocation == null) {
			statusL.setText("No location is selected");
			return;
		}
		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			getDataHolder().getCurrentDistrict().getLocations().delete(selectedLocation); 
			locationsCB.getItems().remove(selectedLocation); 
			getDataHolder().getCurrentDistrict().getStat().updateStats(); 
			statusL.setText("Location  " + selectedLocation + "  is deleted");
			getDataHolder().fillLocationNavStacks();
		}
	}

	private void insertLocation() {  
		if (getDataHolder().getDistricts().isEmpty()) return;
		
		if (getDataHolder().getDistricts().isEmpty()) 
			return;
		if (insertTF.getText() == null || insertTF.getText().equals("")) {
			statusL.setText("Text field is empty");
			return;
		}
		Location location = new Location(insertTF.getText());
		LocationStat stat = new LocationStat(location);
		location.setStat(stat);
		if (getDataHolder().getCurrentDistrict().getLocations().find(location) != null) { 
			statusL.setText("Location name already exists");
			return;
		}
		getDataHolder().getCurrentDistrict().getLocations().insert(location); 
		locationsCB.getItems().add(location); 
		statusL.setText("Location  " + insertTF.getText() + "  is inserted");
		getDataHolder().fillLocationNavStacks();
	}

	@Override
	public void updateContent() { 
		// update the combo box
		locationsCB.getItems().clear();
		if (getDataHolder().getDistricts().isEmpty()) return;
		fillLocationsCB(); 
		statusL.setText("");
	}

	private void fillLocationsCB() { 
		fillLocationsCB(getDataHolder().getCurrentDistrict().getLocations().getRoot());
	}
	
	private void fillLocationsCB(TNode<Location> curr) { 
		if (curr == null) return;
		fillLocationsCB(curr.getLeft());
		locationsCB.getItems().add(curr.getData());
		fillLocationsCB(curr.getRight());
	}

}















