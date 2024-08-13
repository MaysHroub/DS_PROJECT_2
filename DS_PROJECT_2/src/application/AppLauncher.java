package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.Scanner;
import data.District;
import data.DistrictStat;
import data.Location;
import data.LocationStat;
import data.Martyr;
import data.MartyrDate;
import data.MartyrStat;
import data_holder.DataHolder;
import data_structs.tree.BinarySearchTree;
import data_structs.tree.TNode;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import layouts.DistrictNavigationLayout;
import layouts.LocationNavigationLayout;
import layouts.MartyrNavigationLayout;
import layouts.ModifyDistrictLayout;
import layouts.ModifyLocationLayout;
import layouts.ModifyMartyrLayout;
import layouts.SaveDataLayout;
import scenes.DistrictScene;
import scenes.LocationScene;
import scenes.MartyrDateScene;
import scenes.MartyrScene;
import scenes.SaveScene;
import scenes.SceneID;
import scenes.SceneManager;

public class AppLauncher extends Application {

	final int WIDTH = 1050, HEIGHT = 550;

	@Override
	public void start(Stage primaryStage) {
		Button loadBtn = new Button("Load data");

		loadBtn.setStyle("-fx-font-size: 13;" + "	-fx-font-weight: bold;" + "	-fx-font-family: 'Cambria';"
				+ "	-fx-background-color: #fca503;" + "	-fx-text-fill: white;");
		loadBtn.setOnAction(e -> {
			BinarySearchTree<District> districts = loadData(primaryStage);
			if (districts == null)
				return;
			initPorgram(districts, primaryStage);
		});

		primaryStage.setScene(new Scene(new StackPane(loadBtn), WIDTH, HEIGHT));
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	private void initPorgram(BinarySearchTree<District> districts, Stage primaryStage) {
		// create the data holder
		DataHolder dataHolder = new DataHolder(districts);

		// create the tab layouts
		ModifyDistrictLayout modifyDist = new ModifyDistrictLayout(dataHolder);
		DistrictNavigationLayout distNav = new DistrictNavigationLayout(dataHolder);

		ModifyLocationLayout modifyLoc = new ModifyLocationLayout(dataHolder);
		LocationNavigationLayout locNav = new LocationNavigationLayout(dataHolder);

		MartyrNavigationLayout martyrNav = new MartyrNavigationLayout(dataHolder);
		
		ModifyMartyrLayout modifyMartyr = new ModifyMartyrLayout(dataHolder);
		
		SaveDataLayout saveData = new SaveDataLayout(dataHolder);

		// create the scene manager
		SceneManager manager = new SceneManager(primaryStage);

		// create the scenes
		DistrictScene districtScene = new DistrictScene(manager, SceneID.DISTRICT, WIDTH, HEIGHT, modifyDist, distNav);
		LocationScene locationScene = new LocationScene(manager, SceneID.LOCATION, WIDTH, HEIGHT, locNav, modifyLoc);
		MartyrDateScene martyrDateScene = new MartyrDateScene(manager, SceneID.MARTYR_DATE, WIDTH, HEIGHT, martyrNav);
		MartyrScene martyrScene = new MartyrScene(manager, SceneID.MARTYR, WIDTH, HEIGHT, modifyMartyr);
		SaveScene saveScene = new SaveScene(manager, SceneID.SAVE, WIDTH, HEIGHT, saveData);
		
		// set scenes to the manager
		manager.setScenes(districtScene, locationScene, martyrDateScene, martyrScene, saveScene);

		manager.switchTo(SceneID.DISTRICT);
	}

	BinarySearchTree<District> loadData(Stage stage) {
		BinarySearchTree<District> districts = new BinarySearchTree<>();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select a file");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("csv files", "*.csv"));
		fileChooser.setInitialDirectory(new File("/C:/Users/ismae/Downloads"));
		File selectedFile = fileChooser.showOpenDialog(stage);
		if (selectedFile == null)
			return null;
		try (Scanner in = new Scanner(new FileInputStream(selectedFile))) {
			in.nextLine(); // 0.name, 1.event, 2.age, 3.location, 4.district, 5.gender
			while (in.hasNext()) {
				String[] tokens = in.nextLine().split(",");

				if (tokens[2].length() == 0)
					continue;

				District district = new District(tokens[4]);
				TNode<District> districtNode = districts.find(district);
				if (districtNode != null)
					district = districtNode.getData();
				else {
					districts.insert(district);
					DistrictStat stat = new DistrictStat(district);
					district.setStat(stat);
				}

				Location location = new Location(tokens[3]);
				TNode<Location> locationNode = district.getLocations().find(location);
				if (locationNode != null)
					location = locationNode.getData();
				else {
					district.getLocations().insert(location);
					LocationStat stat = new LocationStat(location);
					location.setStat(stat);
				}

				String[] dateInfo = tokens[1].split("/");
				@SuppressWarnings("deprecation")
				Date date = new Date(Integer.parseInt(dateInfo[2]) - 1900, Integer.parseInt(dateInfo[0]) - 1,
						Integer.parseInt(dateInfo[1]));
				
				MartyrDate martyrDate = new MartyrDate(date);
				TNode<MartyrDate> dateNode = location.getMaryrDates().find(martyrDate);
				if (dateNode != null)
					martyrDate = dateNode.getData();
				else {
					location.getMaryrDates().insert(martyrDate);
					MartyrStat stat = new MartyrStat(martyrDate);
					martyrDate.setStat(stat);
				}

				Martyr martyr = new Martyr(tokens[0], Integer.parseInt(tokens[2]), tokens[5].charAt(0));

				martyrDate.getMartyrs().insertSorted(martyr);
			}

		} catch (FileNotFoundException e) {
			System.out.println(e);
		}
		
		return districts;
	}

}
