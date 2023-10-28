
public class Main {

	private static int[] array = {5, 7, 1, 2, 8, 9, 23, 89, 89, 2, 4};
	
	public static void main(String[] args) {
		
		SegmentTree st = new SegmentTree(SegmentTree.GCD);
		st.generateTree(array);
		st.print();
		int m = st.getInterval(3, 9);
		System.out.println(m);
	}

}
