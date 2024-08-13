package scenes;

import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import layouts.TabLayout;

public class MartyrDateScene extends TabScene {

	public MartyrDateScene(SceneManager manager, SceneID id, int width, int height, TabLayout... layouts) {
		super(manager, id, width, height, layouts);
	}

	@Override
	protected Scene createScene(int width, int height) {
		TabPane tabPane = new TabPane();
		TabLayout[] layouts = getLayouts();

		for (int i = 0; i < layouts.length; i++) {
			Tab tab = new Tab(layouts[i].getName());
			tab.setContent(layouts[i].getLayout());
			tab.setClosable(false);
			tabPane.getTabs().add(tab);
		}
		tabPane.setSide(Side.BOTTOM);

		MenuItem distItem = new MenuItem("Districts screen");
		MenuItem locItem = new MenuItem("Locations screen");
		MenuItem martyrItem = new MenuItem("Martyr screen");
		
		distItem.setOnAction(e -> {
			getManager().switchTo(SceneID.DISTRICT);
		});
		locItem.setOnAction(e -> {
			getManager().switchTo(SceneID.LOCATION);
		});
		martyrItem.setOnAction(e -> {
			getManager().switchTo(SceneID.MARTYR);
		});
		Menu menu = new Menu("Go to");
		menu.getItems().addAll(distItem, locItem, martyrItem);
		MenuBar menuBar = new MenuBar(menu);
		
		BorderPane bp = new BorderPane();
		bp.setTop(menuBar);
		bp.setCenter(tabPane);
		
		return new Scene(bp, width, height);
	}


}
