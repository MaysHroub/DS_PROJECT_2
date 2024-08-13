package layouts;

import java.util.Optional;
import data.District;
import data.DistrictStat;
import data_holder.DataHolder;
import data_structs.tree.TNode;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class ModifyDistrictLayout extends TabLayout {
	
	private TextField insertTF, newNameTF;
	private Label statusL;
	private Button insertBtn, deleteBtn, updateBtn;
	private ComboBox<District> districtsCB;
	
	private Alert alert;
	
	
	public ModifyDistrictLayout(DataHolder dataHolder) {
		super("Modify District", dataHolder);
	}

	@Override
	protected Pane createLayout() {
		alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation required");
		alert.setHeaderText("Are you sure you want to proceed?");
		alert.setContentText("This action cannot be undone :)");
		
		Label enterNameL = new Label("Enter District Name: "),
				enterNewL = new Label("Enter new name for selected district: "),
				selectL = new Label("Select a district: ");
		
		statusL = new Label("status");
		
		insertTF = new TextField();
		newNameTF = new TextField();
		
		insertBtn = new Button("Insert");
		deleteBtn = new Button("Delete selected district");
		updateBtn = new Button("Update");
		
		insertBtn.setOnAction(e -> insertDistrict());
		deleteBtn.setOnAction(e -> deleteDistrict());
		updateBtn.setOnAction(e -> updateDistrict());
		
		districtsCB = new ComboBox<>();
		fillDistrictCB();
		
		GridPane layout = new GridPane(15, 15);
		layout.setPadding(new Insets(20));
		layout.setAlignment(Pos.CENTER);
		
		layout.add(enterNameL, 0, 0);
		layout.add(insertTF, 1, 0);
		layout.add(insertBtn, 2, 0);
		
		layout.add(selectL, 0, 2);
		layout.add(districtsCB, 1, 2);
		
		layout.add(deleteBtn, 0, 3);
		
		layout.add(enterNewL, 0, 4);
		layout.add(newNameTF, 1, 4);
		layout.add(updateBtn, 2, 4);
		
		layout.add(statusL, 1, 5);
		
		return layout;
	}

	private void fillDistrictCB() { 
		fillDistrictCB(getDataHolder().getDistricts().getRoot());
	}
	
	private void fillDistrictCB(TNode<District> curr) { 
		if (curr == null) return;
		fillDistrictCB(curr.getLeft());
		districtsCB.getItems().add(curr.getData());
		fillDistrictCB(curr.getRight());
	}

	private void updateDistrict() { 
		if (newNameTF.getText() == null || newNameTF.getText().equals("")) {
			statusL.setText("Text field is empty");
			return;
		}
		if (districtsCB.getValue() == null) {
			statusL.setText("No district is selected");
			return;
		}
		TNode<District> newNameNode = getDataHolder().getDistricts().find(new District(newNameTF.getText(), null)); 
		if (newNameNode != null) {
			statusL.setText("District name is already taken");
			return;
		}
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			TNode<District> deletedNode = getDataHolder().getDistricts().delete(districtsCB.getValue()); 
			districtsCB.getItems().remove(deletedNode.getData()); 
			getDataHolder().deleteFromDistNavStack(deletedNode.getData());
			deletedNode.getData().setName(newNameTF.getText());
			getDataHolder().getDistricts().insert(deletedNode.getData()); 
			districtsCB.getItems().add(deletedNode.getData());
			statusL.setText("District name is updated");
			getDataHolder().addToDistNavStack(deletedNode.getData());
		}
	}

	private void deleteDistrict() { 
		District selectedDistrict = districtsCB.getValue();
		if (selectedDistrict == null) {
			statusL.setText("No district is selected");
			return;
		}
		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			getDataHolder().getDistricts().delete(selectedDistrict); 
			districtsCB.getItems().remove(selectedDistrict); 
			statusL.setText("District  " + selectedDistrict + "  is deleted");
			getDataHolder().deleteFromDistNavStack(selectedDistrict);
		}
	}

	private void insertDistrict() { 
		if (insertTF.getText() == null || insertTF.getText().equals("")) {
			statusL.setText("Text field is empty");
			return;
		}
		if (getDataHolder().getDistricts().find(new District(insertTF.getText())) != null) {
			statusL.setText("District name already exists");
			return;
		}
		District district = new District(insertTF.getText());
		DistrictStat stat = new DistrictStat(district);
		district.setStat(stat);
		getDataHolder().getDistricts().insert(district); 
		districtsCB.getItems().add(district); 
		statusL.setText("District  " + insertTF.getText() + "  is inserted");
		getDataHolder().addToDistNavStack(district);
	}

	@Override
	public void updateContent() { /*do nothing*/ }
	
}