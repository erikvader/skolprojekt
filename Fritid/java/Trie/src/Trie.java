import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class Trie {

	private Node master;
	
	public Trie(){
		master = new Node('$', "", false);
	}
	
	/**
	 * lägger till ett nytt ord
	 * @param s
	 * @return
	 */
	public boolean add(String s){
		return addInternal(master, 0, s);
	}
	
	private boolean addInternal(Node n, int i, String s){
		if(i == s.length()){
			if(n.isWord){
				return false;
			}else{
				n.isWord = true;
				n.word = s;
				return true;
			}
		}
		
		char c = s.charAt(i);
		if(n.children.containsKey(c)){
			return addInternal(n.children.get(c), i+1, s);
		}else{
			n.children.put(c, new Node(c, "", false));
			return addInternal(n.children.get(c), i+1, s);
		}
	}
	
	/**
	 * kollar ifall ett ord finns eller inte
	 * @param s
	 * @return
	 */
	public boolean contains(String s){
		return containsInternal(master, 0, s);
	}
	
	private boolean containsInternal(Node n, int i, String s){
		if(i == s.length()){
			if(n.isWord){
				return true;
			}else{
				return false;
			}
		}
		
		char c = s.charAt(i);
		if(n.children.containsKey(c)){
			return containsInternal(n.children.get(c), i+1, s);
		}else{
			return false;
		}
	}
	
	/**
	 * Tar bort ett ord
	 * @param s
	 */
	public void remove(String s){
		removeInternal(master, 0, s);
	}
	
	private boolean removeInternal(Node n, int i, String s){
		if(i == s.length()){
			n.isWord = false;
			if(n.children.isEmpty()){
				return true;
			}else{
				return false;
			}
		}
		
		char c = s.charAt(i);
		if(n.children.containsKey(c)){
			boolean b = removeInternal(n.children.get(c), i+1, s);
			if(b){
				n.children.remove(c);
			}
			if(n.isWord || !b || !n.children.isEmpty()) return false;
			else return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Räknar antalet noder som används inklusive den extra master-noden
	 * @return
	 */
	public int countNodes(){
		return countNodesInternal(master);
	}
	
	private int countNodesInternal(Node n){
		int count = 1;
		for(Map.Entry<Character, Node> a : n.children.entrySet()){
			count += countNodesInternal(a.getValue());
		}
		return count;
	}
	
	/**
	 * Returnar alla ord i sorterad ordning
	 * @return
	 */
	public ArrayList<String> sort(){
		ArrayList<String> lista = new ArrayList<String>();
		sortInternal(master, lista);
		return lista;
	}
	
	private void sortInternal(Node n, ArrayList<String> lista){
		if(n.isWord){
			lista.add(n.word);
		}
		
		for(Map.Entry<Character, Node> a : n.children.entrySet()){
			sortInternal(a.getValue(), lista);
		}
	}
	
	/**
	 * Skrivet ut den interna trädet i standard output
	 */
	public void printTree(){
        print("", true, master);
    }

    private void print(String prefix, boolean isTail, Node n) {
        System.out.println(prefix + (isTail ? "|__ " : "|-- ") + n.character + (n.isWord ? "<" : ""));
        Iterator<Map.Entry<Character, Node>> entry = n.children.entrySet().iterator();
        Map.Entry<Character, Node> e = null;
        
        while(entry.hasNext()){
        	e = entry.next();
        	if(!entry.hasNext()) break;
        	print(prefix + (isTail ? "    " : "|   "), false, e.getValue());
        }
        if (e != null) {
            print(prefix + (isTail ?"    " : "|   "), true, e.getValue());
        }
    }
	
	private class Node{
		public TreeMap<Character, Node> children;
		public char character;
		public String word;
		public boolean isWord = false;
		
		public Node(char c, String word, boolean isWord){
			children = new TreeMap<Character, Node>();
			character = c;
			this.word = word;
			this.isWord = isWord;
		}
		
		 @Override
		public String toString(){
			return "("+character + "), \"" +word+"\" "+isWord;
		}
	}
}
