package layouts;

import java.io.IOException;
import java.io.PrintWriter;
import data.District;
import data.Location;
import data.Martyr;
import data.MartyrDate;
import data_holder.DataHolder;
import data_structs.linkedlist.Node;
import data_structs.tree.TNode;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class SaveDataLayout extends TabLayout {
	
	private TextField fileTF;
	private Button saveBtn;
	private Label saveL;

	public SaveDataLayout(DataHolder dataHolder) {
		super("Save data", dataHolder);
	}

	@Override
	protected Pane createLayout() {
		fileTF = new TextField();
		saveBtn = new Button("Save");
		saveL = new Label();
		
		fileTF.setPromptText("Enter file name/path");
		
		saveBtn.setOnAction(e -> saveDate());
		
		VBox vBox = new VBox(20, fileTF, saveBtn, saveL);
		vBox.setAlignment(Pos.CENTER);
		vBox.setPadding(new Insets(50));
		
		return vBox;
	}

	private void saveDate() {
		if (fileTF.getText() == null || fileTF.getText().length() == 0) return;
		
		try (PrintWriter out = new PrintWriter(fileTF.getText())) {
			writeDists(getDataHolder().getDistricts().getRoot(), out);
			saveL.setText("All data is saved");
		} catch (IOException e) {
			System.out.println(e);
			saveL.setText("Couldn't save to file!");
		}
	}
	
	private void writeDists(TNode<District> curr, PrintWriter out) {
		if (curr == null) return;
		writeLocs(curr.getData().getLocations().getRoot(), curr.getData(), out);
		writeDists(curr.getLeft(), out);
		writeDists(curr.getRight(), out);
	}
	
	private void writeLocs(TNode<Location> curr, District dis, PrintWriter out) {
		if (curr == null) return;
		writeMartyrs(curr.getData().getMaryrDates().getRoot(), dis, curr.getData(), out);
		writeLocs(curr.getLeft(), dis, out);
		writeLocs(curr.getRight(), dis, out);
	}
	
	@SuppressWarnings("deprecation")
	private void writeMartyrs(TNode<MartyrDate> curr, District dis, Location loc, PrintWriter out) {
		if (curr == null) return;
		// 0.name, 1.event, 2.age, 3.location, 4.district, 5.gender
		Node<Martyr> currMartyr = curr.getData().getMartyrs().getHead();
		while (currMartyr != null) {
			out.println(String.format("%s,%s,%d,%s,%s,%c",
					currMartyr.getData().getName(),
					(curr.getData().getDate().getMonth() + 1) + "/" + 
					(curr.getData().getDate().getDate()) + "/" + 
					(curr.getData().getDate().getYear() + 1900),
					currMartyr.getData().getAge(),
					loc, dis,
					currMartyr.getData().getGender()));
			currMartyr = currMartyr.getNext();
		}
		writeMartyrs(curr.getLeft(), dis, loc, out);
		writeMartyrs(curr.getRight(), dis, loc, out);
	}

	@Override
	public void updateContent() { /* do nth */ }

}









