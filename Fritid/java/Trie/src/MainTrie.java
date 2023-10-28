
public class MainTrie {

	public static void main(String[] args) {
		Trie t = new Trie();
		t.add("2");
		t.add("3");
		t.add("a");
		t.add("abba");
		t.add("ce");
		t.add("ansikte");
		t.add("östergötaland");
		t.add("aurora");
		t.add("1");
		t.add("ab");
		t.add("auro");
		t.add("öster");
		t.printTree();
		System.out.println(t.sort());
	}

}
