package gemfor;


public class Main {

	public static void main(String[] args) {
		int[] a = {3, 3, 3};
		int[] b = {2, 3, 3, 2};
		System.out.println(compare(a, b));
	}
	
	public static boolean compare(int[] a, int[] b){//kollar om allting i a finns i b;
		boolean[] ta = new boolean[a.length];
		boolean[] tb = new boolean[b.length];
		return recurs(a, b, ta, tb);
	}
	
	private static boolean recurs(int[] a, int[] b, boolean[] testedA, boolean[] testedB){
		int toTest = -1;
		for(int i = 0; i < testedA.length; i++){
			if(testedA[i] == false){
				toTest = i;
				testedA[i] = true;
				break;
			}
		}
		if(toTest != -1){
			for(int c = 0; c < b.length; c++){
				if(a[toTest] == b[c] && !testedB[c]){
					testedB[c] = true;
					if(recurs(a, b, testedA, testedB)){
						return true;
					}
					break;
				}
			}
		}else{
			return true;
		}
		return false;
	}

}
