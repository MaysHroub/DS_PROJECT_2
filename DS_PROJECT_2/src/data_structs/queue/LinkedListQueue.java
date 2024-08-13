package data_structs.queue;

import data_structs.linkedlist.LinkedList;
import data_structs.linkedlist.Node;

public class LinkedListQueue<T extends Comparable<T>> implements Queueable<T> {
	
	private LinkedList<T> linkedList;
	
	
	public LinkedListQueue() {
		linkedList = new LinkedList<>();
	}
	
	@Override
	public void enqueue(T data) {
		linkedList.insertLast(data);
	}
	
	@Override
	public T dequeue() {
		Node<T> deleted = linkedList.deleteFirst(); 
		return (deleted == null) ? null : deleted.getData(); 
	}
	
	@Override
	public T getFront() {
		return linkedList.getFirst();
	}
	
	@Override
	public boolean isEmpty() {
		return linkedList.isEmpty();
	}
	
	@Override
	public void clear() {
		linkedList.clear();
	}
}
