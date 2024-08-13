package data;

import data_structs.tree.BinarySearchTree;

public class Location implements Comparable<Location> {

	private String name;
	private BinarySearchTree<MartyrDate> dates;
	private LocationStat stat;

	
	public Location(String name) {
		setName(name);
		dates = new BinarySearchTree<>();
	}

	public Location(String name, BinarySearchTree<MartyrDate> dates) {
		setName(name);
		setMartyrDates(dates);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BinarySearchTree<MartyrDate> getMaryrDates() {
		return dates;
	}

	public void setMartyrDates(BinarySearchTree<MartyrDate> dates) {
		this.dates = dates;
	}

	@Override
	public int compareTo(Location o) {
		return name.toLowerCase().compareTo(o.name.toLowerCase());
	}
	
	@Override
	public String toString() {
		return name;
	}

	public LocationStat getStat() {
		return stat;
	}

	public void setStat(LocationStat stat) {
		this.stat = stat;
	}

}
