
public class Problem26 {

	public static void main(String[] args) {
		int bestCycle = 0;
		int bestTal = 0;
		for(int i = 2; i < 1000; i++){
			int r = findRecurring(i);
			System.out.println(i+" "+r);
			if(r > bestCycle){
				bestCycle = r;
				bestTal = i;
			}
		}
		System.out.println(bestTal);
	}
	
	//g�r kort division
	public static int findRecurring(int i){
		int[] rem = new int[i]; //h�ller koll p� vilken position man hittade en rest p�. index = resten
		int t = 1; //t�ljare
		int counter = 0;
		while(rem[t] == 0){
			rem[t] = counter;
			counter++;
			t *= 10;
			t %= i;
		}
		if(t == 0) return 0;
		else return counter - rem[t];
	}
	
	/**
	 * anv�nder sig utav kort division f�r att skriva ut decimalexpansionen f�r 1/i. 
	 * @param i
	 * @return
	 */
	public static String divide(int i){
		String s = "0.";
		int[] rem = new int[i];
		int t = 1; //t�ljare
		int counter = 0;
		while(rem[t] == 0){
			counter++;
			rem[t] = counter;
			t *= 10;
			s += t/i;
			t %= i;
		}
		
		if(t == 0){
			s = s.substring(0, s.length()-1);
			return s;
		}else {
			int l = counter - rem[t];
			s = s.substring(0, s.length()-l-1) + "(" + s.substring(s.length()-l-1) + ")";
			return s;
		}
	}

}
