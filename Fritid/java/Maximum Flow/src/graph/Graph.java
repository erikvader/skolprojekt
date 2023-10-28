package graph;
import java.util.ArrayList;

public class Graph {

	private ArrayList<Node> nodes;
	//private ArrayList<Edge> edges;
	
	public Graph(){
		nodes = new ArrayList<Node>();
		//edges = new ArrayList<Edge>();
	}
	
	public ArrayList<Node> getNodes(){return nodes;}
	//public ArrayList<Edge> getEdges(){return edges;}
	
	public void addNode(int antal){
		for(int i = 0; i < antal; i++){
			nodes.add(new Node(nodes.size()));
		}
	}
	
	public void addEdge(Node source, Node target, int capacity){
		//forward
		Edge f = new Edge(0, capacity, target, source);
		source.getEdges().add(f);
		
		//backward
		Edge b = new Edge(0, 0, source, target);
		target.getEdges().add(b);
		
		b.setOther(f);
		f.setOther(b);
	}
	
	public void addEdge(int source, int target, int capacity){
		addEdge(nodes.get(source), nodes.get(target), capacity);
	}
}
