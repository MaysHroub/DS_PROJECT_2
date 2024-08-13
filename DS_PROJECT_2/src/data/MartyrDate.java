package data;

import java.util.Date;

import data_structs.linkedlist.LinkedList;

public class MartyrDate implements Comparable<MartyrDate> {
	
	private Date date;
	private LinkedList<Martyr> martyrs;
	private MartyrStat stat;
	

	public MartyrDate(Date date, LinkedList<Martyr> martyrs) {
		setDate(date);
		setMartyrs(martyrs);
	}

	public MartyrDate(Date date) {
		setDate(date);
		martyrs = new LinkedList<>();
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public LinkedList<Martyr> getMartyrs() {
		return martyrs;
	}

	public void setMartyrs(LinkedList<Martyr> martyrs) {
		this.martyrs = martyrs;
	}

	public MartyrStat getStat() {
		return stat;
	}

	public void setStat(MartyrStat stat) {
		this.stat = stat;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public String toString() {
		return String.format("%d/%d/%4d", date.getMonth() + 1, date.getDate(), date.getYear() + 1900);
	}

	@Override
	public int compareTo(MartyrDate o) {
		return date.compareTo(o.date);
	}

}
