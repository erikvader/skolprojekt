import java.util.ArrayList;

public class Problem46 {

	ArrayList<Integer> primes, uddaComp;
	
	public static void main(String[] args) {
		new Problem46().run();
	}
	
	public void run(){
		ESieve(10000);
		for(int i = 0; i < uddaComp.size(); i++){
			int tal = uddaComp.get(i);
			if(kanInteSkrivas(tal)){
				System.out.println(tal);
				break;
			}
		}
	}
	
	public boolean kanInteSkrivas(int tal){
		int i = 0;
		int prime = 0;
		do{
			prime = primes.get(i);
			i++;
			int sq = tal - prime;
			if(sq % 2 == 0){
				sq /= 2;
				int sqq = (int)Math.round(Math.sqrt(sq));
				if(sqq*sqq == sq) return false;
			}
		}while(prime < tal);
		
		return true;
	}
	
	public void ESieve(int limit){
		boolean[] marked = new boolean[limit];
		primes = new ArrayList<Integer>();
		uddaComp = new ArrayList<Integer>();
		for(int i = 2; i < limit; i++){
			if(marked[i]){
				if(i % 2 != 0){
					uddaComp.add(i);
				}
			}else{
				if(i*i <= limit){ //av någon anledning
					for(int j = 2*i; j < limit; j+=i){
						marked[j] = true;
					}
				}
				primes.add(i);
			}
		}
	}

}
