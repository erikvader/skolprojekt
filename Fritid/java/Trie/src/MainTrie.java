
public class MainTrie {

	public static void main(String[] args) {
		Trie t = new Trie();
		t.add("2");
		t.add("3");
		t.add("a");
		t.add("abba");
		t.add("ce");
		t.add("ansikte");
		t.add("�sterg�taland");
		t.add("aurora");
		t.add("1");
		t.add("ab");
		t.add("auro");
		t.add("�ster");
		t.printTree();
		System.out.println(t.sort());
	}

}
