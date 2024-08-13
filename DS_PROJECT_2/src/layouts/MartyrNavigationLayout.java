package layouts;

import java.util.Comparator;

import data.Martyr;
import data.MartyrDate;
import data_holder.DataHolder;
import data_structs.linkedlist.Node;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class MartyrNavigationLayout extends TabLayout {

	private Label dateL, avgAgesL, youngestMartyrL, oldestMartyrL;
	private Button nextBtn, prevBtn;
	private TableView<Martyr> martyrTable;
	
	public MartyrNavigationLayout(DataHolder dataHolder) {
		super("Martyr Navigation", dataHolder);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Pane createLayout() {
		martyrTable = new TableView<>();
		TableColumn<Martyr, String> nameColumn = new TableColumn<>("Name");
		TableColumn<Martyr, Integer> ageColumn = new TableColumn<>("Age");
		TableColumn<Martyr, String> genderColumn = new TableColumn<>("Gender");
		nameColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getName()));
		nameColumn.setPrefWidth(250);
		ageColumn.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getAge()).asObject());
		genderColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getGender() + ""));
		martyrTable.getColumns().addAll(nameColumn, ageColumn, genderColumn);
		
		martyrTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
		
		
		avgAgesL = new Label("");
		youngestMartyrL = new Label("");
		oldestMartyrL = new Label("");
		dateL = new Label();
		
		Label agesL = new Label("Average ages: "),
				youngestL = new Label("Youngest Martyr: "),
				oldestL = new Label("Oldest Martyr: ");
		
		nextBtn = new Button("NEXT >>");
		prevBtn = new Button("<< PREV");
		
		nextBtn.setOnAction(e -> {
			if (getDataHolder().getDistricts().isEmpty() ||
					getDataHolder().getCurrentDistrict().getLocations().isEmpty()) 
				return;
			getDataHolder().moveNextDate();
			fillLayoutWithData();
		});
		prevBtn.setOnAction(e -> {
			if (getDataHolder().getDistricts().isEmpty() ||
					getDataHolder().getCurrentDistrict().getLocations().isEmpty()) 
				return;
			getDataHolder().movePrevDate();
			fillLayoutWithData();
		});
		
		VBox vBox = new VBox(10, agesL, avgAgesL, new Label(" "), youngestL, youngestMartyrL, new Label(" "), oldestL, oldestMartyrL);
		vBox.setAlignment(Pos.CENTER);
		
		HBox hBox = new HBox(15, prevBtn, nextBtn);
		hBox.setAlignment(Pos.CENTER);
		
		BorderPane layout = new BorderPane();
		layout.setTop(dateL);
		layout.setLeft(vBox);
		layout.setRight(martyrTable);
		layout.setBottom(hBox);
		layout.setPadding(new Insets(20));
		
		BorderPane.setMargin(martyrTable, new Insets(20));
		
		martyrTable.prefWidthProperty().bind(layout.widthProperty().divide(2));
		
		return layout;
	}

	private void fillLayoutWithData() {
		// no data at all
		if (getDataHolder().getCurrentDistrict() == null ||
				getDataHolder().getCurrentLocation() == null ||
				getDataHolder().getCurrentMartyrDate() == null) {
			avgAgesL.setText("");
			youngestMartyrL.setText("");
			oldestMartyrL.setText("");
			dateL.setText("");
			martyrTable.setItems(FXCollections.observableArrayList()); // empty list
			return;
		}
		// there's data
		MartyrDate martyrDate = getDataHolder().getCurrentMartyrDate();
		dateL.setText(martyrDate.toString());
		youngestMartyrL.setText(martyrDate.getStat().getYoungest().toString());
		oldestMartyrL.setText(martyrDate.getStat().getOldest().toString());
		avgAgesL.setText(martyrDate.getStat().getAvgAges() + "");
		fillTable();
	}

	private void fillTable() {
		ObservableList<Martyr> martyrs = FXCollections.observableArrayList();
		Node<Martyr> curr = getDataHolder().getCurrentMartyrDate().getMartyrs().getHead();
		for (; curr != null; martyrs.add(curr.getData()), curr = curr.getNext());
		martyrs.sort(new Comparator<Martyr>() {
			@Override
			public int compare(Martyr o1, Martyr o2) {
				return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
			}
		});
		martyrTable.setItems(martyrs);
		martyrTable.refresh();
	}

	@Override
	public void updateContent() {
		getDataHolder().fillDateNavStacks();
		fillLayoutWithData();
	}
	

}
