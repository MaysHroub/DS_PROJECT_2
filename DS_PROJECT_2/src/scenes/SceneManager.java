package scenes;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
	
	private TabScene[] scenes;
	private Stage primaryStage;
	
	public SceneManager() {}
	
	public SceneManager(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	
	public void setScenes(TabScene... scenes) {
		this.scenes = scenes;
	}
	
	public void switchTo(SceneID id) {
		for (int i = 0; i < scenes.length; i++) 
			if (scenes[i].getId() == id) {
				primaryStage.setScene(scenes[i].getScene());
				scenes[i].updateLayouts();
				System.out.println("switching to " + id);
				break;
			}
	}
	
	public Scene getSceneByID(SceneID id) {
		for (int i = 0; i < scenes.length; i++) 
			if (scenes[i].getId() == id) 
				return scenes[i].getScene();
		return null;
	}

}
