package avl;

import java.util.ArrayList;

public class VisualTree <T extends Comparable<T>> {

	private VisualTree<T> left, right;
	private ArrayList<T> label;
	
	public VisualTree(VisualTree<T> left, VisualTree<T> right, ArrayList<T> label) {
		this.left = left;
		this.right = right;
		this.label = label;
	}

	public VisualTree<T> getLeft() {
		return left;
	}

	public VisualTree<T> getRight() {
		return right;
	}

	public ArrayList<T> getLabel() {
		return label;
	}
	
	public T getFirstLabel(){
		return label.get(0);
	}
	
}
