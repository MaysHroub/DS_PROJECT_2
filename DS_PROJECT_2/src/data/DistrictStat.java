package data;

import data_structs.tree.BinarySearchTree;
import data_structs.tree.TNode;

public class DistrictStat implements Stats {

	private District district;
	private int totalMartyrs;
	
	public DistrictStat(District district) {
		this.district = district;
	}
	
	public int getTotalMartyrs() {
		return totalMartyrs;
	}
	
	private void calcTotalMartyrs() {
		BinarySearchTree<Location> locations = district.getLocations();
		totalMartyrs = totalInLocations(locations.getRoot());
	}
	
	private int totalInLocations(TNode<Location> curr) {
		if (curr == null) return 0;
		return totalInDates(curr.getData().getMaryrDates().getRoot()) +
				totalInLocations(curr.getLeft()) +
				totalInLocations(curr.getRight());
	}

	private int totalInDates(TNode<MartyrDate> curr) {
		if (curr == null) return 0;
		return curr.getData().getMartyrs().length() + 
				totalInDates(curr.getLeft()) + 
				totalInDates(curr.getRight());
	}

	@Override
	public void updateStats() {
		calcTotalMartyrs();
	}

	
}









