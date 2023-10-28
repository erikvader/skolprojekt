import funktioner.Funktioner;

public class Problem52 {
	
	public static void main(String[] args){
		boolean running = true;
		int tal = 1;
		
		while(running){
			tal++;
			running = false;
			for(int i = 2; i <= 6; i++){
				if(!Funktioner.isPermutation(tal, i*tal)){
					running = true;
				}
			}
		}
		
		System.out.println(tal);
	}
}
