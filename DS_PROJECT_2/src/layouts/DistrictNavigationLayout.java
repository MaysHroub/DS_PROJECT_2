package layouts;

import data.District;
import data_holder.DataHolder;
import data_structs.tree.TNode;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class DistrictNavigationLayout extends TabLayout {

	private Label districtNameL;
	private Button nextBtn, prevBtn;
	private TextField totalMartyrsTF;
	
	public DistrictNavigationLayout(DataHolder dataHolder) {
		super("District Navigation", dataHolder);
	}

	@Override
	protected Pane createLayout() {
		districtNameL = new Label("District Name");
		districtNameL.getStyleClass().add("title");
		
		totalMartyrsTF = new TextField("");
		totalMartyrsTF.setEditable(false);
		
		Label martyrsL = new Label("Total Martyrs: ");
		
		nextBtn = new Button("NEXT >>");
		prevBtn = new Button("<< PREV");
		
		nextBtn.setOnAction(e -> {
			getDataHolder().moveNextDistrict();
			fillLayoutWithData();
		});
		prevBtn.setOnAction(e -> {
			getDataHolder().movePrevDistrict();
			fillLayoutWithData();
		});
		
		HBox content = new HBox(15, martyrsL, totalMartyrsTF);
		content.setAlignment(Pos.CENTER);
		
		HBox navButtons = new HBox(15, prevBtn, nextBtn);
		navButtons.setAlignment(Pos.CENTER);

		BorderPane layout = new BorderPane();
		layout.setTop(districtNameL);
		layout.setCenter(content);
		layout.setBottom(navButtons);
		
		layout.setPadding(new Insets(15));
		BorderPane.setAlignment(districtNameL, Pos.CENTER);
		
		return layout;
	}
	
	private void fillLayoutWithData() { 
		if (getDataHolder().getDistricts().isEmpty()) {
			districtNameL.setText(" Empty ");
			totalMartyrsTF.setText("");
			return;  
		}
		District district = getDataHolder().getCurrentDistrict();
		districtNameL.setText(district.getName());
		totalMartyrsTF.setText(district.getStat().getTotalMartyrs()+"");
	}
	
	@Override
	public void updateContent() {
		fillLayoutWithData();
	}

}



















