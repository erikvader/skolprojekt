import java.util.ArrayList;

import graph.Edge;
import graph.Graph;
import graph.Node;
import maxflow.Maxflow;

public class Main {

	//använder sig av Edmonds-Karp
	public static void main(String args[]){
		
		Graph g = new Graph();
		g.addNode(7);
		g.addEdge(0, 1, 3);
		g.addEdge(0, 3, 3);
		g.addEdge(1, 2, 4);
		g.addEdge(2, 0, 3);
		g.addEdge(2, 3, 1);
		g.addEdge(2, 4, 2);
		g.addEdge(3, 4, 2);
		g.addEdge(3, 5, 6);
		g.addEdge(4, 1, 1);
		g.addEdge(4, 6, 1);
		g.addEdge(5, 6, 9);
		
		Maxflow mf = new Maxflow(g.getNodes().get(0), g.getNodes().get(6));
		mf.go();
		
		//skriva ut
		ArrayList<Node> nodes = g.getNodes();
		for(int i = 0; i < nodes.size(); i++){
			System.out.println("node: "+nodes.get(i).getId());
			
			ArrayList<Edge> edges = nodes.get(i).getEdges();
			for(int j = 0; j < edges.size(); j++){
				Edge e = edges.get(j);
				System.out.println("    "+e.getFlow()+"/"+e.getCapacity()+" : "+e.getTarget().getId());
			}
		}
		
	}
}
