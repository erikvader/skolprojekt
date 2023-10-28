package main;

import java.util.ArrayList;
import java.util.List;

public class Graph {

	private final List<Vertex> vertexes;
	private final List<Edge> edges;

	public Graph(List<Vertex> vertexes, List<Edge> edges) {
		this.vertexes = vertexes;
		this.edges = edges;
	}

	public List<Vertex> getVertexes() {
		return vertexes;
	}

	public List<Edge> getEdges() {
		return edges;
	}
	
	public List<Vertex> getNeighbours(Vertex node) {
		List<Vertex> neighbors = new ArrayList<Vertex>();
		for (Edge edge : edges) {
			if (edge.getSource().equals(node)) {
				neighbors.add(edge.getDestination());
			}
		}
		return neighbors;
	}
	
	public List<Edge> getNeighboursEdges(Vertex node) {
		List<Edge> neighbors = new ArrayList<Edge>();
		for (Edge edge : edges) {
			if (edge.getSource().equals(node)) {
				neighbors.add(edge);
			}
		}
		return neighbors;
	}

}
