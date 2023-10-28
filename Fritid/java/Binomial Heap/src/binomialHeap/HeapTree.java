package binomialHeap;

import java.util.LinkedList;

class HeapTree<T extends Comparable<T>> {

	T key; //data stored
	LinkedList<HeapTree<T>> children; //children i increasing rank. 
	
	HeapTree(T key){
		this.key = key;
		children = new LinkedList<HeapTree<T>>();
	}
	
	/**
	 * The rank of this HeapTree.
	 * @return
	 */
	int getRank(){return children.size();}
	
	//lägger till other av samma rank
	void add(HeapTree<T> other){
		children.addLast(other);
	}

	int getSize(){
		return (int)Math.pow(2, getRank());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		/*
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("unchecked")
		HeapTree<T> other = (HeapTree<T>) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
		*/
		return false;
	}
	
	
	
	@Override
	public String toString() {
		String cs = "";
		for(HeapTree<T> ht : children) cs += ht.toString()+", ";
		if(cs.length() > 0) cs = cs.substring(0, cs.length()-2);
		return "(HeapTree: rank="+getRank()+", key="+key+", ["+cs+"])";
	}
}
