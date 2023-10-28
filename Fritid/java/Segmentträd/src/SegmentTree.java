
import java.util.ArrayList;
import java.util.Comparator;

public class SegmentTree {
	
	//trädet
	private ArrayList<Integer> tree;
	
	//alla comparators tar två värden (från barnen) och returnar det önskade värdet (till föräldern).
	
	private static final Comparator<Integer> min = new Comparator<Integer>(){
		@Override
		public int compare(Integer o1, Integer o2) {
			if(o1 < o2){
				return o1;
			}else{
				return o2;
			}
		}
	};
	
	private static final Comparator<Integer> max = new Comparator<Integer>(){
		@Override
		public int compare(Integer o1, Integer o2) {
			if(o1 > o2){
				return o1;
			}else{
				return o2;
			}
		}
	};
	
	private static final Comparator<Integer> sum = new Comparator<Integer>(){
		@Override
		public int compare(Integer o1, Integer o2) {
			return o1 + o2;
		}
	};
	
	private static final Comparator<Integer> product = new Comparator<Integer>(){
		@Override
		public int compare(Integer o1, Integer o2) {
			return o1 * o2;
		}
	};
	
	private static final Comparator<Integer> gcd = new Comparator<Integer>(){
		@Override
		public int compare(Integer o1, Integer o2) {
			if(o1 == 0 && o2 == 0) return 0;
			if(o1 == 0) return o2;
			if(o2 == 0) return o1;
			return GCD(o1, o2);
		}
	};
	
	public static final int MIN = 0;
	public static final int MAX = 1;
	public static final int SUM = 2;
	public static final int PRODUCT = 3;
	public static final int GCD = 4;
	
	private Comparator<Integer> curMode = min;
	private int defaultValue;
	
	//för change
	private int totalOvan; //antalet noden ovanför den understa lagret
	private int totalRad; //antal noder på det understa lagret (med padding)
	private int len; //längden på arrayen

	/**
	 * 
	 * @param mode
	 */
	public SegmentTree(int mode){
		tree = new ArrayList<Integer>();
	
		switch(mode){
		case MIN:
			curMode = min;
			defaultValue = Integer.MAX_VALUE;
			break;
		case MAX:
			curMode = max;
			defaultValue = Integer.MIN_VALUE;
			break;
		case SUM:
			curMode = sum;
			defaultValue = 0;
			break;
		case PRODUCT:
			curMode = product;
			defaultValue = 1;
			break;
		case GCD:
			curMode = gcd;
			defaultValue = 0;
			break;
		}
	}
	
	public SegmentTree(){
		this(MIN);
	}
	
	/**
	 * left och right inclusive
	 * @param l
	 * @param r
	 * @return
	 */
	public int getInterval(int l, int r){
		return interval(l, r, 0, 0, totalRad-1);
	}
	
	private int interval(int l, int r, int node, int nl, int nr){
		//är hela nod-intervallet innanför intervallet vi vill ha?
		if(l <= nl && r >= nr){
			return tree.get(node);
		}
		int middle = (nl+nr)/2;
		
		int leftChild = getLeft(node);
		int lValue = defaultValue;
		if(leftChild != -1){
			lValue = interval(l, r, leftChild, nl, middle);
		}
		
		int rightChild = getRight(node);
		int rValue = defaultValue;
		if(rightChild != -1){
			rValue = interval(l, r, rightChild, middle+1, nr);
		}
		
		int com = curMode.compare(lValue, rValue);
		return com;
	}
	
	public void generateTree(int[] a){
		tree.clear();
		len = a.length;
		int rad = (int)Math.ceil(Math.log(len)/Math.log(2));
		totalOvan = (int)(Math.pow(2, rad)-1);
		totalRad = (int)(Math.pow(2, rad));
		
		for(int i = 0; i < totalOvan; i++){
			tree.add(defaultValue);
		}
		for(int i = 0; i < len; i++){
			tree.add(a[i]);
		}
		
		build(0);
	}
	
	//index i den originella arrayen
	public void change(int index, int value){
		tree.set(totalOvan+index, value);
		
		goUp(totalOvan+index);
	}
	
	private void goUp(int node){
		update(node);
		int parent = getParent(node);
		if(parent == node) return; //rooten nåddes
		goUp(parent);
	}
	
	private void update(int node){
		int l = getLeft(node);
		int r = getRight(node);
		
		if(l == -1 && r == -1){
			return;
		}
		
		int a = 0;
		if(l == -1){
			a = tree.get(r);
		}else if(r == -1){
			a = tree.get(l);
		}else{
			a = curMode.compare(tree.get(l), tree.get(r));
		}
		
		tree.set(node, a);
	}
	
	private void build(int node){
		int l = getLeft(node);
		int r = getRight(node);
		
		if(l == -1 && r == -1){
			return;
		}
		
		if(l != -1){
			build(l);
		}
		if(r != -1){
			build(r);
		}
		
		update(node);
	}
	
	private int getLeft(int node){
		int temp = 2*node+1;
		return temp >= tree.size() ? -1 : temp;
	}
	
	private int getRight(int node){
		int temp = 2*node+2;
		return temp >= tree.size() ? -1 : temp;
	}
	
	private int getParent(int node){
		int temp = (node-1)/2;
		return temp >= tree.size() ? -1 : temp;
	}
	
	public void print(){
		print("", true, 0);
	}
	
	private void print(String prefix, boolean isTail, int node){
		System.out.println(prefix + (isTail ? "V-- " : "|-- ") + tree.get(node));
        int r = getRight(node);
        if (r != -1) {
            print(prefix + (isTail ?"    " : "|   "), false, r);
        }
        int l = getLeft(node);
        if(l != -1) {
            print(prefix + (isTail ? "    " : "|   "), true, l);
        }
	}
	
	/* original som kan skriva ut träd med fler barn
	public void print() {
        print("", true);
    }

    private void print(String prefix, boolean isTail) {
        System.out.println(prefix + (isTail ? "└── " : "├── ") + name);
        for (int i = 0; i < children.size() - 1; i++) {
            children.get(i).print(prefix + (isTail ? "    " : "│   "), false);
        }
        if (children.size() > 0) {
            children.get(children.size() - 1).print(prefix + (isTail ?"    " : "│   "), true);
        }
    }
    */
	
	/**
	 * Räknar ut Greatest Common Divider, alltså det största talet som man dividera a och b jämnt ut med.
	 * <p>
	 * Denna funktion använder sig av Euklides Algoritm.
	 * 
	 * @param a det ena talet
	 * @param b det andra talet
	 * @return den största gemensamma delare
	 */
	private static int GCD(int a, int b){
		
		int left = Math.max(a, b);
		int right = Math.min(a, b);
		
		int res = 0;
		
		do{
			res = left % right;
			left = right;
			right = res;
		}while(res != 0);
		
		return left;
		
	}
}
