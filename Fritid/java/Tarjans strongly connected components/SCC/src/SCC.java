import java.util.ArrayList;
import java.util.Stack;


public class SCC {

	Stack<Node> stack = new Stack<Node>();
	ArrayList<Node> nodes = new ArrayList<Node>();
	int index = 0;
	ArrayList<ArrayList<Node>> scc = new ArrayList<>();
	
	public static void main(String[] args){
		new SCC().run();
	}
	
	private void addLink(int a, int b){
		nodes.get(a).accessLinks().add(nodes.get(b));
	}
	
	public void run(){
		nodes.clear();
		index = 0;
		stack.clear();
		scc.clear();
		
		//lägg till alla noder
		nodes.add(new Node("a"));
		nodes.add(new Node("b"));
		nodes.add(new Node("c"));
		nodes.add(new Node("d"));
		
		//fixa bridges
		addLink(0, 1);
		addLink(1, 2);
		addLink(2, 0);
		addLink(3, 0);
		
		//titta om det finns loopar
		
		//leta efter strong connections
		for(int i = 0; i < nodes.size(); i++){
			Node n = nodes.get(i);
			if(n.getIndex() == -1){ //not visited
				strongConnect(n);
			}
		}
		
		for(ArrayList<Node> scc : this.scc){
			System.out.println("component");
			for(Node n : scc){
				System.out.println(n);
			}
		}
	}

	private void strongConnect(Node node){
		node.setIndex(index);
		node.setLowlink(index);
		node.setOnStack(true);
		stack.push(node);
		index++;
		
		//leta bland typ barn
		ArrayList<Node> links = node.accessLinks();
		Node child;
		for(int i = 0; i < links.size(); i++){
			child = links.get(i);
			if(child.getIndex() == -1){//not visited
				strongConnect(child);
				node.setLowlink(Math.min(node.getLowlink(), child.getLowlink()));
			}else if(child.isOnStack()){
				node.setLowlink(Math.min(node.getLowlink(), child.getIndex()));
			}
		}
		
		//om det är en root node, generera SSC
		if(node.getIndex() == node.getLowlink()){
			Node n = null;
			ArrayList<Node> newSCC = new ArrayList<Node>();
			do{
				n = stack.pop();
				n.setOnStack(false);
				newSCC.add(n);
			}while(n != node);
			scc.add(newSCC);
		}
		
	}
	
}
