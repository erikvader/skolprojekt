package graph;
public class Edge {

	private int flow, capacity;
	private Node target, source;
	private Edge other;
	
	public Edge(){
		
	}
	
	public Edge(int flow, int capacity, Node target, Node source){
		this.flow = flow;
		this.capacity = capacity;
		this.target = target;
		this.source = source;
	}
	
	public int getFlow(){return flow;}
	public int getCapacity(){return capacity;}
	public void setFlow(int f){flow = f;}
	public void addFlow(int f){flow += f;}
	//public void setCapacity(int c){capacity = c;}
	public Node getTarget(){return target;}
	public Node getSource(){return source;}
	public int getAvailableCapacity(){return capacity - flow;}
	
	public Edge getOther(){return other;}
	public void setOther(Edge e){other = e;}
}
