package binomialHeap;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * en Binomial Heap. Saker jämförs med compareTo(). De minsta grejerna hamnar först (returnar -1 i compareTo mot allt annat)
 * 
 * @author Erik Rimskog
 *
 */
public class BinomialHeap<T extends Comparable<T>> {
	
	//trädens rank i strictly increasing order
	private LinkedList<HeapTree<T>> trees;
	
	/**
	 * Creates an empty BinomialHeap. 
	 */
	public BinomialHeap(){
		trees = new LinkedList<HeapTree<T>>();
	}
	
	/**
	 * Whether this Heap is empty or not
	 * @return
	 */
	public boolean isEmpty(){return trees.isEmpty();}
	
	/**
	 * Calculates the size of this Heap in O(lg n)
	 * @return
	 */
	public int size(){
		int res = 0;
		Iterator<HeapTree<T>> ite = trees.iterator();
		while(ite.hasNext()){
			res += ite.next().getSize();
		}
		return res;
	}
	
	/**
	 * adds a in O(lg n)
	 * @param a
	 */
	public void add(T a){
		LinkedList<HeapTree<T>> ll = new LinkedList<HeapTree<T>>();
		ll.add(new HeapTree<T>(a));
		merge(ll);
	}
	
	//tar trees och en HeapTree s och lägger till den i trees. om det sista elementet i trees är samma rank som a, linkas dem. 
	private void addMerge(LinkedList<HeapTree<T>> trees, HeapTree<T> a){
		if(!trees.isEmpty() && a.getRank() == trees.getLast().getRank()){
			trees.add(link(trees.removeLast(), a));
		}else{
			trees.add(a);
		}
	}	
	
	//other will be changed
	//other in increasing rank order
	//mergar
	private void merge(LinkedList<HeapTree<T>> other){
		LinkedList<HeapTree<T>> newTrees = new LinkedList<HeapTree<T>>();
		LinkedList<HeapTree<T>> a = other;
		LinkedList<HeapTree<T>> b = this.trees;
		
		while(!a.isEmpty() && !b.isEmpty()){
			if(a.getFirst().getRank() == b.getFirst().getRank()){
				addMerge(newTrees, link(a.removeFirst(), b.removeFirst()));
			}else{
				if(a.getFirst().getRank() < b.getFirst().getRank()){
					addMerge(newTrees, a.removeFirst());
				}else{
					addMerge(newTrees, b.removeFirst());
				}
			}
		}
		
		newTrees.addAll(a);
		newTrees.addAll(b);
		
		this.trees = newTrees;
		
	}
	
	/**
	 * Merges this Heap with another Heap 'other'. Takes O(lg n) and destroys 'other' (changing it to make it useless)
	 * @param other
	 */
	public void merge(BinomialHeap<T> other){
		merge(other.trees);
	}
	
	/**
	 * Returns the element with lowest priority. O(lg n)
	 * @return
	 */
	public T peek(){
		if(trees.size() == 0) return null;
		Iterator<HeapTree<T>> ite = trees.iterator();
		T min = ite.next().key;
		T next;
		while(ite.hasNext()){
			next = ite.next().key;
			if(next.compareTo(min) < 0)
				min = next;
		}
		return min;
	}
	
	/**
	 * Pops this Heap, returns the element with lowest priority and removes it. O(lg n)
	 * @return
	 */
	public T pop(){
		if(trees.size() == 0) return null;
		
		//hitta min
		Iterator<HeapTree<T>> ite = trees.iterator();
		HeapTree<T> min = ite.next();
		HeapTree<T> next;
		while(ite.hasNext()){
			next = ite.next();
			if(next.key.compareTo(min.key) < 0)
				min = next;
		}
		
		trees.remove(min);
		
		merge(min.children);
		
		return min.key;
	}
	
	/**
	 * slår ihpå två HeapTree:s av SAMMA rank r till en ny HeapTree av rank r+1.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	private HeapTree<T> link(HeapTree<T> a, HeapTree<T> b){
		if(a.key.compareTo(b.key) < 0){
			a.add(b);
			return a;
		}else{
			b.add(a);
			return b;
		}
	}
}
