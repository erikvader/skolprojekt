package main;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		new Main().run();
	}
	
	private List<Vertex> nodes;
	private List<Edge> edges;

	public void run() {
		nodes = new ArrayList<Vertex>();
		edges = new ArrayList<Edge>();
		for (int i = 0; i < 11; i++) {
			Vertex location = new Vertex("Node_" + i, "Node_" + i);
			nodes.add(location);
		}

		addLane("Edge_0", 0, 1, 85);
		addLane("Edge_1", 0, 2, 217);
		addLane("Edge_2", 0, 4, 173);
		addLane("Edge_3", 2, 6, 186);
		addLane("Edge_4", 2, 7, 103);
		addLane("Edge_5", 3, 7, 183);
		addLane("Edge_6", 5, 8, 250);
		addLane("Edge_7", 8, 9, 84);
		addLane("Edge_8", 7, 9, 167);
		addLane("Edge_9", 4, 9, 502);
		addLane("Edge_10", 9, 10, 40);
		addLane("Edge_11", 1, 10, 600);
		//addLane("Edge_12", 2, 0, 217);
		
		
		//testExecuteDijkstra();
		testExecuteFloyd();
		
		
	}

	public void testExecuteFloyd() {
		Graph graph = new Graph(nodes, edges);
		FloydWarshall fw = new FloydWarshall(graph);
		
		System.out.println(fw.path(nodes.get(0), nodes.get(10)));
		System.out.println(fw.dist(nodes.get(0), nodes.get(10)));
		
		
	}

	public void testExecuteDijkstra() {
		// Lets check from location Loc_1 to Loc_10
		Graph graph = new Graph(nodes, edges);
		Dijkstra dijkstra = new Dijkstra(graph);
		dijkstra.execute(nodes.get(0));
		LinkedList<Vertex> path = dijkstra.getPath(nodes.get(10));

		// assertNotNull(path);
		// assertTrue(path.size() > 0);

		for (Vertex vertex : path) {
			System.out.println(vertex);
		}
		
		System.out.println("Distance: "+dijkstra.getDistance(nodes.get(10)));

	}

	private void addLane(String laneId, int sourceLocNo, int destLocNo, int duration) {
		Edge lane = new Edge(laneId, nodes.get(sourceLocNo), nodes.get(destLocNo), duration);
		edges.add(lane);
	}

}
