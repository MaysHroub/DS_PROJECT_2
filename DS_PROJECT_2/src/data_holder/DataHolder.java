package data_holder;

import data.District;
import data.Location;
import data.MartyrDate;
import data_structs.queue.LinkedListQueue;
import data_structs.stack.QueuedStack;
import data_structs.tree.BinarySearchTree;
import data_structs.tree.TNode;

public class DataHolder {

	private BinarySearchTree<District> districts;
	private District currentDistrict;
	private Location currentLocation;
	private MartyrDate currentMartyrDate;

	private QueuedStack<District> nextDistrict, prevDistrict;
	private QueuedStack<Location> nextLocation, prevLocation;
	private QueuedStack<MartyrDate> nextDate, prevDate;

	public DataHolder(BinarySearchTree<District> districts) {
		nextDistrict = new QueuedStack<>(); prevDistrict = new QueuedStack<>();
		nextLocation = new QueuedStack<>(); prevLocation = new QueuedStack<>();
		nextDate = new QueuedStack<>(); prevDate = new QueuedStack<>();
		this.districts = districts;
		updateDistrictStats(districts.getRoot());
		fillDistrictNavStacks();
	}

	// updating data
	private void updateDistrictStats(TNode<District> curr) {
		if (curr == null)
			return;
		updateLocs(curr.getData().getLocations().getRoot());
		curr.getData().getStat().updateStats();
		updateDistrictStats(curr.getLeft());
		updateDistrictStats(curr.getRight());
	}

	private void updateLocs(TNode<Location> curr) {
		if (curr == null) return;
		updateMartyrDates(curr.getData().getMaryrDates().getRoot());
		curr.getData().getStat().updateStats();
		updateLocs(curr.getLeft());
		updateLocs(curr.getRight());
	}

	private void updateMartyrDates(TNode<MartyrDate> curr) {
		if (curr == null)
			return;
		curr.getData().getStat().updateStats();
		updateMartyrDates(curr.getLeft());
		updateMartyrDates(curr.getRight());
	}

	public void fillDistrictNavStacks() {
		nextDistrict.clear(); 
		prevDistrict.clear();
		fillDistrictNavStacks(districts.getRoot());
		moveNextDistrict();
	}
	
	private void fillDistrictNavStacks(TNode<District> curr) {
		if (curr == null) return;
		fillDistrictNavStacks(curr.getRight());
		nextDistrict.push(curr.getData());
		fillDistrictNavStacks(curr.getLeft());
	}
	
	public void fillDateNavStacks() {
		nextDate.clear();
		prevDate.clear();
		System.out.println("root  " + currentLocation.getMaryrDates().getRoot());
		fillDateNavStacks(currentLocation.getMaryrDates().getRoot());
		currentMartyrDate = null;
		moveNextDate();
	}

	private void fillDateNavStacks(TNode<MartyrDate> curr) {
		if (curr == null) return;
		fillDateNavStacks(curr.getRight());
		nextDate.push(curr.getData());
		fillDateNavStacks(curr.getLeft());
	}
	
	public void fillLocationNavStacks() {
		fillLocationNavStacks(currentDistrict.getLocations().getRoot());
		relocateLocationPointer();
	}

	private void fillLocationNavStacks(TNode<Location> curr) {
		nextLocation.clear();
		prevLocation.clear();
		LinkedListQueue<TNode<Location>> queue = new LinkedListQueue<>();
		if (curr == null)
			return;
		queue.enqueue(curr);
		while (!queue.isEmpty()) {
			curr = queue.dequeue();
			prevLocation.push(curr.getData());
			if (curr.hasLeft())
				queue.enqueue(curr.getLeft());
			if (curr.hasRight())
				queue.enqueue(curr.getRight());
		}
		while (!prevLocation.isEmpty())
			nextLocation.push(prevLocation.pop());
	}

	public District moveNextDistrict() {
		if (nextDistrict.isEmpty()) return null;
		prevDistrict.push(nextDistrict.pop());
		currentDistrict = prevDistrict.peek();
		return currentDistrict;
	}

	public District movePrevDistrict() {
		if (prevDistrict.isEmpty()) return null;
		District temp = prevDistrict.pop();
		if (prevDistrict.isEmpty()) prevDistrict.push(temp);
		else nextDistrict.push(temp);
		currentDistrict = prevDistrict.peek();
		return currentDistrict;
	}
	
	public MartyrDate moveNextDate() {
		if (nextDate.isEmpty()) return null;
		prevDate.push(nextDate.pop());
		currentMartyrDate = prevDate.peek();
		return currentMartyrDate;
	}

	public MartyrDate movePrevDate() {
		if (prevDate.isEmpty()) return null;
		MartyrDate temp = prevDate.pop();
		if (prevDate.isEmpty()) prevDate.push(temp);
		else nextDate.push(temp);
		currentMartyrDate = prevDate.peek();
		return currentMartyrDate;
	}
	
	public Location moveNextLocation() {
		if (nextLocation.isEmpty()) return null;
		Location temp = nextLocation.pop();
		if (currentLocation != null) prevLocation.push(currentLocation);
		currentLocation = temp;
		return currentLocation;
	}

	public Location movePrevLocation() {
		if (prevLocation.isEmpty())
			return null;
		Location temp = prevLocation.pop();
		nextLocation.push(currentLocation);
		currentLocation = temp;
		return currentLocation;
	}

	private void relocateLocationPointer() {
		currentLocation = null;
		moveNextLocation();
	}
	
	public void addToDistNavStack(District district) {
		if (currentDistrict.compareTo(district) >= 0)
			addToPrevDist(district);
		else 
			addToNextDist(district);
	}
	
	public void deleteFromDistNavStack(District district) {
		if (currentDistrict.compareTo(district) >= 0)
			removeFromPrevDist(district);
		else 
			removeFromNextDist(district);
	}
	
	private void removeFromNextDist(District district) {
		QueuedStack<District> temp = new QueuedStack<>();
		while (!nextDistrict.isEmpty() && nextDistrict.peek().compareTo(district) != 0) 
			temp.push(nextDistrict.pop());
		if (!nextDistrict.isEmpty() && nextDistrict.peek().compareTo(district) == 0) nextDistrict.pop();
		while (!temp.isEmpty()) nextDistrict.push(temp.pop());
	}
	
	private void addToNextDist(District district) {
		QueuedStack<District> temp = new QueuedStack<>();
		while (!nextDistrict.isEmpty() && nextDistrict.peek().compareTo(district) < 0) 
			temp.push(nextDistrict.pop());
		nextDistrict.push(district);
		while (!temp.isEmpty()) nextDistrict.push(temp.pop());
	}
	
	private void removeFromPrevDist(District district) {
		QueuedStack<District> temp = new QueuedStack<>();
		while (!prevDistrict.isEmpty() && prevDistrict.peek().compareTo(district) != 0) 
			temp.push(prevDistrict.pop());
		if (!prevDistrict.isEmpty() && prevDistrict.peek().compareTo(district) == 0) prevDistrict.pop();
		while (!temp.isEmpty()) prevDistrict.push(temp.pop());
	}
	
	private void addToPrevDist(District district) {
		QueuedStack<District> temp = new QueuedStack<>();
		while (!prevDistrict.isEmpty() && prevDistrict.peek().compareTo(district) >= 0) 
			temp.push(prevDistrict.pop());
		prevDistrict.push(district);
		while (!temp.isEmpty()) prevDistrict.push(temp.pop());
	}
	
	public void addToDateNavStack(MartyrDate date) {
		if (currentMartyrDate.compareTo(date) >= 0)
			addToPrevDate(date);
		else 
			addToNextDate(date);
	}
	
	public void deleteFromDateNavStack(MartyrDate date) {
		if (currentMartyrDate.compareTo(date) >= 0)
			removeFromPrevDate(date);
		else 
			removeFromNextDate(date);
	}
	
	private void removeFromNextDate(MartyrDate date) {
		QueuedStack<MartyrDate> temp = new QueuedStack<>();
		while (!nextDate.isEmpty() && nextDate.peek().compareTo(date) != 0) 
			temp.push(nextDate.pop());
		if (!nextDate.isEmpty() && nextDate.peek().compareTo(date) == 0) nextDate.pop();
		while (!temp.isEmpty()) nextDate.push(temp.pop());
	}
	
	private void addToNextDate(MartyrDate date) {
		QueuedStack<MartyrDate> temp = new QueuedStack<>();
		while (!nextDate.isEmpty() && nextDate.peek().compareTo(date) < 0) 
			temp.push(nextDate.pop());
		nextDate.push(date);
		while (!temp.isEmpty()) nextDate.push(temp.pop());
	}
	
	private void removeFromPrevDate(MartyrDate date) {
		QueuedStack<MartyrDate> temp = new QueuedStack<>();
		while (!prevDate.isEmpty() && prevDate.peek().compareTo(date) != 0) 
			temp.push(prevDate.pop());
		if (!prevDate.isEmpty() && prevDate.peek().compareTo(date) == 0) prevDate.pop();
		while (!temp.isEmpty()) prevDate.push(temp.pop());
	}
	
	private void addToPrevDate(MartyrDate date) {
		QueuedStack<MartyrDate> temp = new QueuedStack<>();
		while (!prevDate.isEmpty() && prevDate.peek().compareTo(date) >= 0) 
			temp.push(prevDate.pop());
		prevDate.push(date);
		while (!temp.isEmpty()) prevDate.push(temp.pop());
	}
	
	public BinarySearchTree<District> getDistricts() {
		return districts;
	}

	public District getCurrentDistrict() {
		return currentDistrict;
	}

	public Location getCurrentLocation() {
		return currentLocation;
	}

	public MartyrDate getCurrentMartyrDate() {
		return currentMartyrDate;
	}

	
}
