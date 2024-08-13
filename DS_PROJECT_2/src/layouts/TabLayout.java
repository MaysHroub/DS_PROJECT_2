package layouts;

import data_holder.DataHolder;
import javafx.scene.layout.Pane;

public abstract class TabLayout {
	
	private Pane layout;
	private String name;
	private DataHolder dataHolder;
	
	protected TabLayout(String name, DataHolder dataHolder) {
		this.name = name;
		this.dataHolder = dataHolder;
		layout = createLayout();
	}

	protected abstract Pane createLayout();
	
	public abstract void updateContent();

	public String getName() {
		return name;
	}

	public Pane getLayout() {
		return layout;
	}

	public DataHolder getDataHolder() {
		return dataHolder;
	}
	
}
