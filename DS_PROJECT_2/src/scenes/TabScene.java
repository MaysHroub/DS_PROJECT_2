package scenes;

import javafx.scene.Scene;
import layouts.TabLayout;

public abstract class TabScene {
	
	private Scene scene;
	private SceneID id;
	private SceneManager manager;
	private TabLayout[] layouts;
	
	public TabScene(SceneManager manager, SceneID id, int width, int height, TabLayout... layouts) {
		this.id = id;
		this.manager = manager;
		this.layouts = layouts;
		scene = createScene(width, height);
		scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
	}
	
	public Scene getScene() {
		return scene;
	}

	public SceneID getId() {
		return id;
	}
	
	protected abstract Scene createScene(int width, int height);
	
	public void updateLayouts() {
		for (int i = 0; i < layouts.length; i++) 
			layouts[i].updateContent();
	}

	public TabLayout[] getLayouts() {
		return layouts;
	}

	public SceneManager getManager() {
		return manager;
	}
	
}










