package main;

import java.util.List;
import java.util.Stack;

/**
 * The <tt>FloydWarshall</tt> class represents a data type for solving the
 * all-pairs shortest paths problem in edge-weighted digraphs with no negative
 * cycles. The edge weights can be positive, negative, or zero. This class finds
 * either a shortest path between every pair of vertices or a negative cycle.
 * <p>
 * This implementation uses the Floyd-Warshall algorithm. The constructor takes
 * time proportional to <em>V</em><sup>3</sup> in the worst case, where
 * <em>V</em> is the number of vertices. Afterwards, the <tt>dist()</tt>,
 * <tt>hasPath()</tt>, and <tt>hasNegativeCycle()</tt> methods take constant
 * time; the <tt>path()</tt> and <tt>negativeCycle()</tt> method takes time
 * proportional to the number of edges returned.
 * <p>
 * For additional documentation, see <a href="/algs4/44sp">Section 4.4</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 * 
 */
public class FloydWarshall {

	private boolean hasNegativeCycle; // is there a negative cycle?
	//private double[][] distTo; // distTo[v][w] = length of shortest v->w path
	//private Edge[][] edgeTo; // edgeTo[v][w] = last edge on shortest v->w path
	private List<Vertex> vertexes;
	private List<Edge> edges;
	private FloydArray<Integer> distTo; // distTo[v][w] = length of shortest v->w path
	private FloydArray<Edge> edgeTo; // edgeTo[v][w] = last edge on shortest v->w path
	
	/**
	 * Computes a shortest paths tree from each vertex to to every other vertex
	 * in the edge-weighted digraph <tt>G</tt>. If no such shortest path exists
	 * for some pair of vertices, it computes a negative cycle.
	 * 
	 * @param G
	 *            the edge-weighted digraph
	 */
	public FloydWarshall(Graph G) {
		int V = G.getVertexes().size();
		distTo = new FloydArray<Integer>(V, 1);
		edgeTo = new FloydArray<Edge>(V, 0);
		vertexes = G.getVertexes();
		edges = G.getEdges();

		// initialize distances to infinity
		for (int v = 0; v < V; v++) {
			for (int w = 0; w < V; w++) {
				distTo.set(vertexes.get(v), vertexes.get(w), Integer.MAX_VALUE);
			}
		}

		// initialize distances using edge-weighted digraph's
		for (int v = 0; v < G.getVertexes().size(); v++) {
			for (Edge e : G.getNeighboursEdges(vertexes.get(v))) {
				distTo.set(e.getSource(), e.getDestination(), e.getWeight());
				edgeTo.set(e.getSource(), e.getDestination(), e);
			}
			// in case of self-loops
			if (distTo.get(vertexes.get(v), vertexes.get(v)) >= 0) {
				distTo.set(vertexes.get(v), vertexes.get(v), 0);
				edgeTo.set(vertexes.get(v), vertexes.get(v), null);
			}
		}

		// Floyd-Warshall updates
		for (int i = 0; i < V; i++) {
			// compute shortest paths using only 0, 1, ..., i as intermediate
			// vertices
			for (int v = 0; v < V; v++) {
				if (edgeTo.get(vertexes.get(v), vertexes.get(i)) == null)
					continue; // optimization
				for (int w = 0; w < V; w++) {
					if (distTo.get(vertexes.get(v), vertexes.get(w)) > distTo.get(vertexes.get(v), vertexes.get(i)) + distTo.get(vertexes.get(i), vertexes.get(w))) {
						distTo.set(vertexes.get(v), vertexes.get(w), distTo.get(vertexes.get(v), vertexes.get(i)) + distTo.get(vertexes.get(i), vertexes.get(w)));
						edgeTo.set(vertexes.get(v), vertexes.get(w), edgeTo.get(vertexes.get(i), vertexes.get(w)));
					}
				}
				// check for negative cycle
				if (distTo.get(vertexes.get(v), vertexes.get(v)) < 0) {
					//System.out.println(distTo.get(vertexes.get(v), vertexes.get(v)));
					//hasNegativeCycle = true;
					return;
				}
			}
		}
	}

	/**
	 * Is there a negative cycle?
	 * 
	 * @return <tt>true</tt> if there is a negative cycle, and <tt>false</tt>
	 *         otherwise
	 */
	public boolean hasNegativeCycle() {
		return hasNegativeCycle;
	}

	/**
	 * Returns a negative cycle, or <tt>null</tt> if there is no such cycle.
	 * 
	 * @return a negative cycle as an iterable of edges, or <tt>null</tt> if
	 *         there is no such cycle
	 */
	/*public Iterable<Edge> negativeCycle() {
		for (int v = 0; v < distTo.length(); v++) {
			// negative cycle in v's predecessor graph
			if (distTo.get(vertexes.get(v), vertexes.get(v)) < 0) {
				int V = edgeTo.length();
				Graph spt = new Graph(V);
				for (int w = 0; w < V; w++)
					if (edgeTo[v][w] != null)
						spt.addEdge(edgeTo[v][w]);
				EdgeWeightedDirectedCycle finder = new EdgeWeightedDirectedCycle(spt);
				assert finder.hasCycle();
				return finder.cycle();
			}
		}
		return null;
	}*/                   //VAR JOBBIG!!!

	/**
	 * Is there a path from the vertex <tt>s</tt> to vertex <tt>t</tt>?
	 * 
	 * @param s
	 *            the source vertex
	 * @param t
	 *            the destination vertex
	 * @return <tt>true</tt> if there is a path from vertex <tt>s</tt> to vertex
	 *         <tt>t</tt>, and <tt>false</tt> otherwise
	 */
	private boolean hasPath(int s, int t) {
		return distTo.get(vertexes.get(s), vertexes.get(t)) < Integer.MAX_VALUE;
	}
	
	public boolean hasPath(Vertex s, Vertex t) {
		return distTo.get(s, t) < Integer.MAX_VALUE;
	}

	/**
	 * Returns the length of a shortest path from vertex <tt>s</tt> to vertex
	 * <tt>t</tt>.
	 * 
	 * @param s
	 *            the source vertex
	 * @param t
	 *            the destination vertex
	 * @return the length of a shortest path from vertex <tt>s</tt> to vertex
	 *         <tt>t</tt>; <tt>Double.POSITIVE_INFINITY</tt> if no such path
	 * @throws UnsupportedOperationException
	 *             if there is a negative cost cycle
	 */
	public int dist(Vertex s, Vertex t) {
		if (hasNegativeCycle())
			throw new UnsupportedOperationException("Negative cost cycle exists");
		return distTo.get(s, t);
	}

	/**
	 * Returns a shortest path from vertex <tt>s</tt> to vertex <tt>t</tt>.
	 * 
	 * @param s
	 *            the source vertex
	 * @param t
	 *            the destination vertex
	 * @return a shortest path from vertex <tt>s</tt> to vertex <tt>t</tt> as an
	 *         iterable of edges, and <tt>null</tt> if no such path
	 * @throws UnsupportedOperationException
	 *             if there is a negative cost cycle
	 */
	public List<Edge> path(Vertex s, Vertex t) {
		if (hasNegativeCycle())
			throw new UnsupportedOperationException("Negative cost cycle exists");
		if (!hasPath(s, t))
			return null;
		Stack<Edge> path = new Stack<Edge>();
		for (Edge e = edgeTo.get(s, t); e != null; e = edgeTo.get(s, e.getSource())) {
			path.push(e);
		}
		return path;
	}

	// check optimality conditions
	private boolean check(Graph G, int s) {

		// no negative cycle
		if (!hasNegativeCycle()) {
			for (int v = 0; v < G.getVertexes().size(); v++) {
				for (Edge e : G.getNeighboursEdges(vertexes.get(v))) {
					Vertex w = e.getDestination();
					for (int i = 0; i < G.getVertexes().size(); i++) {
						if (distTo.get(vertexes.get(i), w) > distTo.get(vertexes.get(i), vertexes.get(v)) + e.getWeight()) {
							System.err.println("edge " + e + " is eligible");
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * Unit tests the <tt>FloydWarshall</tt> data type.
	 */
	/*
	public static void main(String[] args) {

		// random graph with V vertices and E edges, parallel edges allowed
		int V = Integer.parseInt(args[0]);
		int E = Integer.parseInt(args[1]);
		AdjMatrixEdgeWeightedDigraph G = new AdjMatrixEdgeWeightedDigraph(V);
		for (int i = 0; i < E; i++) {
			int v = (int) (V * Math.random());
			int w = (int) (V * Math.random());
			double weight = Math.round(100 * (Math.random() - 0.15)) / 100.0;
			if (v == w)
				G.addEdge(new DirectedEdge(v, w, Math.abs(weight)));
			else
				G.addEdge(new DirectedEdge(v, w, weight));
		}

		StdOut.println(G);

		// run Floyd-Warshall algorithm
		FloydWarshall spt = new FloydWarshall(G);

		// print all-pairs shortest path distances
		StdOut.printf("  ");
		for (int v = 0; v < G.V(); v++) {
			StdOut.printf("%6d ", v);
		}
		StdOut.println();
		for (int v = 0; v < G.V(); v++) {
			StdOut.printf("%3d: ", v);
			for (int w = 0; w < G.V(); w++) {
				if (spt.hasPath(v, w))
					StdOut.printf("%6.2f ", spt.dist(v, w));
				else
					StdOut.printf("  Inf ");
			}
			StdOut.println();
		}

		// print negative cycle
		if (spt.hasNegativeCycle()) {
			StdOut.println("Negative cost cycle:");
			for (DirectedEdge e : spt.negativeCycle())
				StdOut.println(e);
			StdOut.println();
		}

		// print all-pairs shortest paths
		else {
			for (int v = 0; v < G.V(); v++) {
				for (int w = 0; w < G.V(); w++) {
					if (spt.hasPath(v, w)) {
						StdOut.printf("%d to %d (%5.2f)  ", v, w, spt.dist(v, w));
						for (DirectedEdge e : spt.path(v, w))
							StdOut.print(e + "  ");
						StdOut.println();
					} else {
						StdOut.printf("%d to %d no path\n", v, w);
					}
				}
			}
		}

	}
	*/

}
