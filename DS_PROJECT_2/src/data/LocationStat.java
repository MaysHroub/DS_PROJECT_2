package data;

import java.util.Date;

import data_structs.tree.TNode;

public class LocationStat implements Stats {

	private Location location;
	private Date earlistDate, latestDate, maxDate;
	
	public LocationStat(Location location) {
		this.location = location;
	}
	
	private void calcEarliest() {
		TNode<MartyrDate> curr = location.getMaryrDates().getRoot();
		for (; curr != null && curr.hasLeft(); curr = curr.getLeft());
		earlistDate = (curr == null) ? null : curr.getData().getDate();
	}
	
	private void calcLatest() {
		TNode<MartyrDate> curr = location.getMaryrDates().getRoot();
		for (; curr != null && curr.hasRight(); curr = curr.getRight());
		latestDate = (curr == null) ? null : curr.getData().getDate();
	}
	
	private void calcMaxDate() {
		MartyrDate martyrDate = maxMartyrDate(location.getMaryrDates().getRoot());
		maxDate = (martyrDate == null || martyrDate.getMartyrs().length() == 0) ? null : martyrDate.getDate();
//		maxDate = maxMartyrDate(location.getMaryrDates().getRoot()).getDate();
	}
	
	private MartyrDate maxMartyrDate(TNode<MartyrDate> curr) {
		if (curr == null) return null;
		MartyrDate currDate = curr.getData(),
				leftDate = maxMartyrDate(curr.getLeft()),
				rightDate = maxMartyrDate(curr.getRight());
		
		int currLength = currDate.getMartyrs().length();
		int leftLength = (leftDate == null) ? 0 : leftDate.getMartyrs().length();
		int rightLength = (rightDate == null) ? 0 : rightDate.getMartyrs().length();
		
//		if (leftDate == null && rightDate == null) return curr.getData();
//		else if (leftDate == null) 
//			return (currLength > rightDate.getMartyrs().length()) 
//					? currDate : rightDate;
//		else if (rightDate == null) 
//			return (currLength > leftDate.getMartyrs().length()) 
//					? currDate : leftDate;
//		else 
//			return (rightDate.compareTo(leftDate) > 0) 
//					? ((rightDate.compareTo(currDate) > 0) ? rightDate : currDate) 
//					: ((leftDate.compareTo(currDate) > 0) ? leftDate : currDate);
		return (rightLength > leftLength) 
				? ((rightLength > currLength) ? rightDate : currDate) 
				: ((leftLength > currLength) ? leftDate : currDate);
	}

	@Override
	public void updateStats() {
		calcEarliest();
		calcLatest();
		calcMaxDate();
	}

	public Date getEarlistDate() {
		return earlistDate;
	}

	public Date getLatestDate() {
		return latestDate;
	}

	public Date getMaxDate() {
		return maxDate;
	}

}



















