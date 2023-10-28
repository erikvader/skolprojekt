package maxflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import graph.Edge;
import graph.Node;

public class Maxflow {

	//private ArrayList<Node> nodes;
	//private ArrayList<Edge> edges;
	private Node start, goal;
	
	public Maxflow(/*Graph g, */Node start, Node goal){
		/*
		nodes = new ArrayList<Node>(g.getNodes());
		edges = new ArrayList<Edge>(g.getEdges());
		*/
		//nodes = g.getNodes();
		//edges = g.getEdges();
		
		this.start = start;
		this.goal = goal;
	}
	
	public void go(){
		ArrayList<Edge> path;
		while((path = getPath()).size() != 0){
			//hitta lowest
			int lowest = path.get(0).getAvailableCapacity();
			for(int i = 1; i < path.size(); i++){
				int ava = path.get(i).getAvailableCapacity();
				if(ava < lowest){
					lowest = ava;
				}
			}
			
			//fixa flow
			Edge e;
			for(int i = 0; i < path.size(); i++){
				e = path.get(i);
				e.addFlow(lowest);
				//findOpposite(e).addFlow(-lowest);
				e.getOther().addFlow(-lowest);
			}
		}
	}
	
	/*
	private Edge findOpposite(Edge e){
		Node t = e.getTarget();
		for(Edge ee : t.getEdges()){
			if(ee.getTarget().equals(e.getSource())){
				return ee;
			}
		}
		return null;
	}
	*/
	
	private ArrayList<Edge> getPath(){
		ArrayList<Edge> path = new ArrayList<Edge>();
		
		Queue<Node> queue = new LinkedList<Node>();
		HashSet<Node> visited = new HashSet<Node>();
		HashMap<Node, Edge> parents = new HashMap<Node, Edge>();
		queue.add(start);
		
		Node cur;
		Node target;
		while(queue.size() > 0){
			cur = queue.poll();
			visited.add(cur);
			for(Edge e : cur.getEdges()){
				if(e.getAvailableCapacity() > 0){
					target = e.getTarget();
					if(!visited.contains(target)){
						if(!queue.contains(target)){
							queue.add(target);
							parents.put(target, e);
							if(target.equals(goal)) break;
						}
					}
				}
			}
		}
		
		//fixa path
		Node p = goal;
		Edge h = parents.get(p);
		while(h != null){
			path.add(h);
			p = h.getSource();
			h = parents.get(p);
		}
		
		return path;
	}
}
