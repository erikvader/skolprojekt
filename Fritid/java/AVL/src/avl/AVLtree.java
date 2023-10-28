package avl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * 
 * @author Erik Rimskog
 *
 * @param <T> 
 */
public class AVLtree <T extends Comparable<T>> {
	
	private int height, size;
	private LinkedList<T> data;
	private AVLtree<T> parent, left, right;
	
	/**
	 * Creates an empty always sorted list using an AVL-tree (balanced search tree). The elements
	 * in this list are sorted by the default compareTo-function in T (increasing order for Integer).
	 * T's compareTo must be equivalent with its equals-function. (if compareTo == 0 then equals must be true).
	 * The sorting of the elements is hopefully stable (the order in which equal objects are added is preserved). 
	 */
	public AVLtree(){
		this(null);
	}
	
	/**
	 * new empty node with a parent
	 * @param parent
	 */
	private AVLtree(AVLtree<T> parent){
		height = 0;
		size = 0;
		this.parent = parent;
	}
	
	/**
	 * transforms an empty node into a 'real' node with the specified data.
	 * @param data
	 */
	private void createNode(T data){
		this.data = new LinkedList<T>();
		this.data.add(data);
		height = 1;
		size = 1;
		left = new AVLtree<T>(this);
		right = new AVLtree<T>(this);
	}
	
	/**
	 * transforms a 'real' node into an empty one.
	 */
	private void destroyNode(){
		left = right = null;
		size = 0;
		height = 0;
		this.data = null;
	}
	
	/**
	 * Clears this list.
	 */
	public void clear(){
		destroyNode();
	}
	
	//private int getHeight(){return height;}
	
	/**
	 * returns the current number of elements
	 * @return
	 */
	public int size(){return size;}
	
	/**
	 * true if this list is empty.
	 * @return
	 */
	public boolean isEmpty(){return height == 0;}

	/**
	 * returns the element at index i. 0 <= i < size. O(lg(size))
	 * @param i
	 * @return
	 */
	public T get(int i){
		if(isEmpty()) throw new RuntimeException("Index out of bounds");
		int l = left.size;
		int r = l+data.size();
		if(i >= l && i < r){
			return data.get(i-l);
		}else if(i < l){
			return left.get(i);
		}else{ //i >= r
			return right.get(i-r);
		}
	}
	
	/**
	 * returns the smallest element (equivalent with get(0)) O(lg(size))
	 * @return
	 */
	public T getMinimum(){
		if(isEmpty()) throw new RuntimeException("AVLtree is empty!");
		return get(0);
	}
	
	/**
	 * returns the largest element (equivalent with get(size-1)) O(lg(size))
	 * @return
	 */
	public T getMaximum(){
		if(isEmpty()) throw new RuntimeException("AVLtree is empty!");
		return get(size-1);
	}
	
	/**
	 * returns true if t exists in this list. O(lg(size))
	 * @param t
	 * @return
	 */
	public boolean contains(T t){
		return find(t) != null;
	}

	/**
	 * removes the first occurrence of ele if it exists, otherwise nothing will happen. (uses the equals-function) O(lg(size))
	 * @param ele
	 */
	public void remove(T ele){
		AVLtree<T> toRemove = find(ele);
		if(toRemove == null) return;
		
		Iterator<T> ite = toRemove.data.iterator();
		while(ite.hasNext()){
			if(ite.next().equals(ele)){
				ite.remove();
				break;
			}
		}
		
		if(toRemove.data.isEmpty()){
			toRemove.removeNode();
		}else{
			toRemove.updateAttrUp();
		}
		
	}
	
	/**
	 * removes the element at index i. 0 <= i < size. O(lg(size))
	 * @param i
	 */
	public void remove(int i){
		//precis samma som i get()
		if(isEmpty()) throw new RuntimeException("Index out of bounds");
		int l = left.size;
		int r = l+data.size();
		if(i >= l && i < r){
			data.remove(i-l);
			if(data.isEmpty()) removeNode();
			else updateAttrUp();
		}else if(i < l){
			left.remove(i);
		}else{ //i >= r
			right.remove(i-r);
		}
	}
	
	/**
	 * physically removes this node
	 */
	private void removeNode(){
		if(isLeaf()){
			destroyNode();
			if(parent != null){
				parent.updateAttrUp();
				parent.balanceUp();
			}
			return;
		}else if(!left.isEmpty() && !right.isEmpty()){
			//hitta in order predecessor
			AVLtree<T> max = left.maxNode();
			this.data = max.data;
			max.removeNode();
			return;
		}else if(!left.isEmpty() && right.isEmpty()){
			this.data = left.data;
			left = left.left;
			right = left.parent.right;
			left.parent = this;
			right.parent = this;
		}else{
			this.data = right.data;
			right = right.right;
			left = right.parent.left;
			right.parent = this;
			left.parent = this;
		}
		
		updateAttrUp();
		balanceUp();
	}
	
	/**
	 * helper for removeNode. 
	 * @return
	 */
	private AVLtree<T> maxNode(){
		if(right.isEmpty()) return this;
		else return right.maxNode();
	}
	
	/**
	 * returns the internal node which contains the elment t.
	 * @param t
	 * @return
	 */
	private AVLtree<T> find(T t){
		if(isEmpty()) return null;
		
		int comp = this.data.getFirst().compareTo(t);
		
		if(comp == 0){
			for(T d : data){if(t.equals(d)) return this;}
			return null;
		}
		
		return comp < 0 ? right.find(t) : left.find(t);
	}

	/**
	 * fills buffer with all elements in this list in sorted order (by calling addAll on buffer).
	 * @param buffer
	 */
	public void getSorted(List<T> buffer){
		if(isEmpty()) return;
		left.getSorted(buffer);
		buffer.addAll(data);
		right.getSorted(buffer);
	}
	
	/**
	 * returns an ArrayList<T> with all elements in sorted order. 
	 * @return
	 */
	public ArrayList<T> getSorted(){
		ArrayList<T> l = new ArrayList<T>(size);
		getSorted(l);
		return l;
	}
	
	/**
	 * adds the element n to this list in its sorted location. O(lg(size))
	 * @param n
	 */
	public void add(T n){
		addAux(n, false);
	}
	
	/**
	 * adds n to the list if it already doesn't exist. returns true if successful. O(lg(size))
	 * @param n
	 * @return
	 */
	public boolean addUnique(T n){
		return addAux(n, true);
	}
	
	/**
	 * helper for the adds
	 * @param n
	 * @param unique
	 * @return
	 */
	private boolean addAux(T n, boolean unique){
		if(isEmpty()){
			createNode(n);
			return true;
		}
		
		boolean b;
		int comp = this.data.getFirst().compareTo(n);
		if(comp == 0){
			if(unique) {
				for(T d : data){ 
					if(d.equals(n)) return false;
				}
			}
			data.add(n);
			b = true;
		}else if(comp < 0){
			b = right.addAux(n, unique);
		}else{
			b = left.addAux(n, unique);
		}
		
		updateAttr();
		balance();
		return b;
	}
	
	/**
	 * updates size and height for this node and all of its parents
	 */
	private void updateAttrUp(){
		updateAttr();
		if(parent != null) parent.updateAttrUp();
	}
	
	/**
	 * updates size and height
	 */
	private void updateAttr(){
		height = 1 + Math.max(left.height,  right.height);
		size = data.size() + left.size + right.size;
	}
	
	/**
	 * the balance factor for this node. -1 = left heavy, 0 = balanced, 1 = right heavy
	 * @return
	 */
	private int balanceFactor(){
		return -left.height + right.height;
	}
	
	/**
	 * does the correct tree rotations to balance this node.
	 */
	private void balance(){
		int bf = balanceFactor();
		if(bf > 1){
			if(right.balanceFactor() < 0){
				right.rotateRight();
			}
			rotateLeft();
		}else if(bf < -1){
			if(left.balanceFactor() > 0){
				left.rotateLeft();
			}
			rotateRight();
		}
	}
	
	/**
	 * balances this node and all of its parents.
	 */
	private void balanceUp(){
		balance();
		if(parent != null) parent.balanceUp();
	}
	
	//node left must not be empty
	private void rotateRight(){
		AVLtree<T> a, b, c, d, e;
		a = this;
		b = a.left;
		c = b.left;
		d = b.right;
		e = a.right;
		
		//'byt plats' på a och b
		LinkedList<T> temp = a.data;
		a.data = b.data;
		b.data = temp;
		b = this;
		a = b.left;
		
		//fixa children
		b.right = a;
		a.parent = b;
		a.right = e;
		e.parent = a;
		a.left = d;
		d.parent = a;
		b.left = c;
		c.parent = b;
		
		//fixa heights
		a.updateAttrUp();
		
	}
	
	//node right must not be empty
	private void rotateLeft(){
		AVLtree<T> a, b, c, d, e;
		b = this;
		a = b.right;
		c = b.left;
		d = a.left;
		e = a.right;
		
		//'byt plats' på a och b
		LinkedList<T> temp = a.data;
		a.data = b.data;
		b.data = temp;
		a = this;
		b = a.right;
		
		//fixa children
		a.left = b;
		b.parent = a;
		a.right = e;
		e.parent = a;
		b.right = d;
		d.parent = b;
		b.left = c;
		c.parent = b;
		
		//fixa heights
		b.updateAttrUp();
		
	}
	
	private boolean isLeaf(){return left.isEmpty() && right.isEmpty();}
	
	@Override
	public String toString() {
		/*
		if(isEmpty()) return "Empty";
		return "(Node height="+height+", data="+data.getFirst()+", left="+left+", right="+right+")";
		*/
		return getSorted().toString();
	}
	
	/**
	 * prints the internal tree to System.out
	 */
	public void print(){
		print("", true);
	}
	
	private void print(String prefix, boolean isTail) {
		String name = "";
		if(!isEmpty()){
			name = "(h: "+height;
			name += ", s: "+size;
			name += ", [";
			for(T n : data) name += n + ", ";
			name = name.substring(0, name.length()-2);
			name += "])";
		}
		
	    System.out.println(prefix + (isTail ? "└──" : "├──") + name);
	    
	    if(!isEmpty() && !isLeaf()){
	    	right.print(prefix + (isTail ?"   " : "│  "), false);
	        left.print(prefix + (isTail ? "   " : "│  "), true);
	    }
	}

	/**
	 * ignore this function please :D
	 * @return
	 */
	public boolean validateAVL(){
		if(isEmpty()) return true;
		int bf = balanceFactor();
		int s = left.size+right.size+data.size();
		return Math.abs(bf) <= 1 && s == size && left.validateAVL() && right.validateAVL();
	}

	public VisualTree<T> getVisualTree(){
		if(isEmpty()) return null;
		return new VisualTree<T>(
				left.getVisualTree(),
				right.getVisualTree(),
				new ArrayList<T>(data)
				);
	}
	
}
