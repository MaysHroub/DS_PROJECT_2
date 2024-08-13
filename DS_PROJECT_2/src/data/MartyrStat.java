package data;

import data_structs.linkedlist.Node;

public class MartyrStat implements Stats {

	private MartyrDate martyrDate;
	private double avgAges;
	private Martyr youngest, oldest;

	public MartyrStat(MartyrDate martyrDate) {
		this.martyrDate = martyrDate;
	}

	private void calcYoungest() {
		youngest = martyrDate.getMartyrs().getFirst();
	}

	private void calcOldest() {
		oldest = martyrDate.getMartyrs().getLast();
	}

	private void calcAvgAges() {
		Node<Martyr> curr = martyrDate.getMartyrs().getHead();
		int sum = 0, count = martyrDate.getMartyrs().length();
		while (curr != null) {
			sum += curr.getData().getAge();
			curr = curr.getNext();
		}
		avgAges = (count == 0) ? 0 : (double) sum / count;
	}

	@Override
	public void updateStats() {
		calcYoungest();
		calcOldest();
		calcAvgAges();
	}

	public double getAvgAges() {
		return avgAges;
	}

	public Martyr getYoungest() {
		return youngest;
	}

	public Martyr getOldest() {
		return oldest;
	}
	
}
