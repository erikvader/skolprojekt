import isPrime.Prime;

public class Problem41 {

	public static void main(String[] args) {
		long t = System.nanoTime();
		new Problem41().run();
		System.out.println((System.nanoTime() - t)/1000000.0);
	}
	
	String word = "987654321";
	boolean[] inUse;
	String solution;
	
	public void run(){
		for(int i = 9; i >= 1; i--){
			inUse = new boolean[i];
			if(recurs("", i)){
				break;
			}
			word = word.substring(1);
		}
		System.out.println(solution);
	}
	
	public boolean recurs(String prev, int length){
		if(prev.length() == length){
			if(Prime.isPrimeDumb(Long.parseLong(prev))){
				solution = prev;
				return true;
			}
			return false;
		}
		
		for(int i = 0; i < word.length(); i++){
			char c = word.charAt(i);
			if(!inUse[i]){
				inUse[i] = true;
				if(recurs(prev+c, length)){
					return true;
				}
				inUse[i] = false;
			}
		}
		
		return false;
	}

}
