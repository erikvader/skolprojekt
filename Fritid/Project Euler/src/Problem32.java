import java.util.HashSet;

public class Problem32 {

	public static void main(String[] args) {
		new Problem32().run();
	}
	
	String word = "123456789";
	boolean[] inUse = new boolean[9];
	HashSet<Integer> pandigital = new HashSet<Integer>();
	
	public void run(){
		for(int i = 2; i <= 9; i++){
			recursOld("", i);
		}
		
		int summa = 0;
		for(int i : pandigital)
			summa += i;
		
		System.out.println(summa);
		
	}
	
	public void searchForPandigital(String s){
		for(int i = 1; i <= s.length()-1; i++){
			int produkt = Integer.parseInt(s.substring(0, i)) * Integer.parseInt(s.substring(i));
			
			//is pandigital
			String ps = Integer.toString(produkt)+s;
			if(ps.length() == 9){
				boolean[] visited = new boolean[9];
				int c = 0;
				for(int j = 0; j < ps.length(); j++){
					int k = Integer.parseInt(ps.substring(j, j+1));
					if(k == 0) break;
					if(visited[k-1]){
						break;
					}else{
						visited[k-1] = true;
						c++;
					}
				}
				
				if(c == 9){
					pandigital.add(produkt);
				}
				
			}
		}
	}
	
	public void recursOld(String prev, int length){
		if(prev.length() == length){
			searchForPandigital(prev);
			return;
		}
		
		for(int i = 0; i < word.length(); i++){
			char c = word.charAt(i);
			if(!inUse[i]){
				inUse[i] = true;
				recursOld(prev+c, length);
				inUse[i] = false;
			}
		}
		
	}

}
