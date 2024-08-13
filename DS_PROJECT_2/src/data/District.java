package data;

import data_structs.tree.BinarySearchTree;

public class District implements Comparable<District> {
	
	private String name;
	private BinarySearchTree<Location> locations;
	private DistrictStat stat;

	
	public District(String name) {
		setName(name);
		locations = new BinarySearchTree<>();
	}
	
	public District(String name, BinarySearchTree<Location> locations) {
		setName(name);
		setLocations(locations);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BinarySearchTree<Location> getLocations() {
		return locations;
	}

	public void setLocations(BinarySearchTree<Location> locations) {
		this.locations = locations;
	}

	@Override
	public int compareTo(District o) {
		return name.toLowerCase().compareTo(o.name.toLowerCase());
	}
	
	@Override
	public String toString() {
		return name;
	}

	public DistrictStat getStat() {
		return stat;
	}

	public void setStat(DistrictStat stat) {
		this.stat = stat;
	}
	
}
