package data_structs.linkedlist;


public class LinkedList< T extends Comparable<T> > {
	
	private Node<T> head, tail;
	
	public Node<T> getHead() {
		return head;
	}
	
	public T getFirst() {
		return (head != null) ? head.getData() : null;
	}
	
	public T getLast() {
		return (tail != null) ? tail.getData() : null;
	}
	
	public void insertLast(T data) {
		Node<T> newNode = new Node<>(data);
		if (tail == null)   // empty
			head = tail = newNode;
		else {
			tail.setNext(newNode);
			tail = newNode;
		}
	}
	
	public Node<T> deleteLast() {
		Node<T> deleted = tail, curr = head;
		for (; curr != tail && curr.getNext() != tail; curr = curr.getNext());
		if (curr == tail)  // empty or 1 node
			head = tail = null;
		else {
			curr.setNext(null);
			tail = curr;
		}
		return deleted;
	}
	
	public Node<T> deleteFirst() {
		Node<T> deleted = head;
		if (head == tail) // empty or 1 node
			head = tail = null;
		else
			head = head.getNext();
		return deleted;
	}
	
	public void insertSorted(T data) {
		Node<T> newNode = new Node<>(data);
		
		if (head == null) { // case 0
			head = tail = newNode;
			return;
		}
		Node<T> prev = null, curr = head;
		for (; curr != null && curr.getData().compareTo(data) < 0; 
				prev = curr, curr = curr.getNext());
		
		if (prev == null) { // case 1: insert first
			newNode.setNext(head);
			head = newNode;
		}
		else if (curr == null) { // case 2: insert at the end
			prev.setNext(newNode);
			tail = newNode;
		}
		else { // case 3: insert between two nodes
			// curr != null && prev != null
			newNode.setNext(curr);
			prev.setNext(newNode);
		}
	}
	
	public Node<T> find(T data) {
		Node<T> curr = head;
		for (; curr != null && curr.getData().compareTo(data) < 0;
				curr = curr.getNext());
		if (curr != null && curr.getData().compareTo(data) == 0) 
			return curr;
		return null;
	}
	
	public Node<T> delete(T data) {
		if (head != null) {
			Node<T> prev = null, curr = head;
			for (; curr != null && curr.getData().compareTo(data) < 0; 
					prev = curr, curr = curr.getNext());
			if (curr != null && curr.getData().compareTo(data) == 0) {
				if (prev == null) // delete the first element
					head = curr.getNext();
				else if (curr.getNext() == null) { // delete the last element
					tail = prev;
					prev.setNext(null);
				}
				else // delete between two elements
					prev.setNext(curr.getNext());
				return curr;
			}
		}
		return null;
	}
	

	public void traverse() {
		Node<T> curr = head;
		System.out.print("Head --> ");
		while (curr != null) {
			System.out.print(curr + " --> ");
			curr = curr.getNext();
		}
		System.out.println("Null");
	}
	
	@Override
	public String toString() {
		Node<T> curr = head;
		String linkedlist = "Head --> ";
		while (curr != null) {
			linkedlist += curr + " --> ";
			curr = curr.getNext();
		}
		return linkedlist + "Null";
	}
	
	public int length() {
		Node<T> curr = head;
		int count = 0;
		while (curr != null) {
			count++;
			curr = curr.getNext();
		}
		return count;	
	}
	
	public boolean isEmpty() {
		return head == null && tail == null;
	}
	
	public void clear() {
		head = tail = null;
	}

	public Node<T> deleteByEquals(T data) {
		Node<T> prev = null, curr = head;
		for (; curr != null && !curr.getData().equals(data); 
				prev = curr, curr = curr.getNext());
		if (curr != null) {
			if (prev == null) // delete the first element
				head = curr.getNext();
			else if (curr.getNext() == null) {  // delete the last element
				prev.setNext(null);
				tail = prev;
			}
			else // delete between two elements
				prev.setNext(curr.getNext());
			return curr;
		}
		return null;
	}
	
}


