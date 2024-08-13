package layouts;

import data.District;
import data.Location;
import data_holder.DataHolder;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class LocationNavigationLayout extends TabLayout {

	private Label locationNameL, earliestDateL, latestDateL, maxDateL;
	private Button nextBtn, prevBtn;

	
	public LocationNavigationLayout(DataHolder dataHolder) {
		super("Location navigation", dataHolder);
	}

	@Override
	protected Pane createLayout() {
		locationNameL = new Label("Location Name");
		locationNameL.getStyleClass().add("title");
		earliestDateL = new Label();
		latestDateL = new Label();
		maxDateL = new Label();

		Label earliestL = new Label("Earliest date: "), latestL = new Label("Latest date: "),
				maxL = new Label("Date with maximum number of martyrs: ");

		
		nextBtn = new Button("NEXT >>");
		prevBtn = new Button("<< PREV");
		
		nextBtn.setOnAction(e -> {
			if (getDataHolder().getDistricts().isEmpty()) 
				return;
			getDataHolder().moveNextLocation();
			fillLayoutWithData();
		});
		prevBtn.setOnAction(e -> {
			if (getDataHolder().getDistricts().isEmpty()) 
				return;
			getDataHolder().movePrevLocation();
			fillLayoutWithData();
		});

		GridPane midLayout = new GridPane(15, 15);
		midLayout.setAlignment(Pos.CENTER);
		midLayout.add(earliestL, 0, 0);
		midLayout.add(latestL, 0, 1);
		midLayout.add(maxL, 0, 2);
		midLayout.add(earliestDateL, 1, 0);
		midLayout.add(latestDateL, 1, 1);
		midLayout.add(maxDateL, 1, 2);
		
		HBox navButtons = new HBox(15, prevBtn, nextBtn);
		navButtons.setAlignment(Pos.CENTER);

		BorderPane layout = new BorderPane();
		layout.setCenter(midLayout);
		layout.setTop(locationNameL);
		layout.setBottom(navButtons);

		layout.setPadding(new Insets(15));
		BorderPane.setAlignment(navButtons, Pos.CENTER);
		BorderPane.setAlignment(locationNameL, Pos.CENTER);
		
		return layout;
	}
	
	private void fillLayoutWithData() {  
		if (getDataHolder().getDistricts().isEmpty()) { // no districts at all
			locationNameL.setText(" There's no data ");
			earliestDateL.setText("");
			latestDateL.setText("");
			maxDateL.setText("");
			return;  
		}
		District district = getDataHolder().getCurrentDistrict();
		if (district.getLocations().isEmpty()) {  // no locations for this district
			locationNameL.setText(" No locations for this district ");
			earliestDateL.setText("");
			latestDateL.setText("");
			maxDateL.setText("");
			return;
		}
		Location currLoc = getDataHolder().getCurrentLocation();
		locationNameL.setText(currLoc.getName());
		earliestDateL.setText((currLoc.getStat().getEarlistDate() == null) ? "none" : currLoc.getStat().getEarlistDate().toString());
		latestDateL.setText((currLoc.getStat().getLatestDate() == null) ? "none" : currLoc.getStat().getLatestDate().toString());
		maxDateL.setText((currLoc.getStat().getMaxDate() == null) ? "none" : currLoc.getStat().getMaxDate().toString());
	}

	@Override
	public void updateContent() { 
		getDataHolder().fillLocationNavStacks();
		fillLayoutWithData();
	}


}
