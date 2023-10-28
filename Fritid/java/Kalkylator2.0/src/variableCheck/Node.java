package variableCheck;

import java.util.ArrayList;

/**
 * Denna klass används vid sökning om det finns oöndliga loopar hos variablerna.
 * <p>
 * En 'Node' är en variabel. 
 * <p>
 * I 'links' finns de andra noderna som denna nod behöver värdet på för att kunns räknas ut.
 * <p>
 * De andra variablerna är till för algoritmen; Strongly Connected Components.
 * 
 * @author ErRi0401
 *
 */
public class Node {

	private ArrayList<Node> links;
	private String name;
	private boolean onStack = false;
	private int index, lowlink;
	
	public Node(String name){
		this.name = name;
		links = new ArrayList<Node>();
		index = -1;
	}
	
	public String getName(){return name;}
	/**
	 * Ger tillgång till 'links'
	 * 
	 * @return ArrayList:en links
	 */
	public ArrayList<Node> accessLinks(){return links;}
	public boolean isOnStack(){return onStack;}
	public void setOnStack(boolean onStack){this.onStack = onStack;}
	public int getIndex(){return index;}
	public void setIndex(int index){this.index = index;}
	public int getLowlink(){return lowlink;}
	public void setLowlink(int lowlink){this.lowlink = lowlink;}
	/*
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		//result = prime * result + ((linksTo == null) ? 0 : linksTo.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		/*if (linksTo == null) {
			if (other.linksTo != null)
				return false;
		} else if (!linksTo.equals(other.linksTo))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	*/

	@Override
	public String toString() {
		return "Node: "+name;
	}
	
}
