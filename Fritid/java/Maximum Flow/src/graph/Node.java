package graph;
import java.util.ArrayList;

public class Node {

	private int id;
	private ArrayList<Edge> edges;
	//private Edge parent;//för sökning av augmenting path
	
	public Node(int id){
		edges = new ArrayList<Edge>();
		this.id = id;
	}
	
	public int getId(){return id;}
	public ArrayList<Edge> getEdges(){return edges;}
	
	/*
	public Edge getParent(){return parent;}
	public void setParent(Edge p){parent = p;}
	 */
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
