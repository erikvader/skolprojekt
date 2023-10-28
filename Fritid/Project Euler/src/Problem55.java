import funktioner.Funktioner;

public class Problem55 {

	//TODO long overflowar, fixa
	public static void main(String[] args) {
		int lych = 0;
		for(int i = 1; i < 10000; i++){
			long n = i;
			boolean works = false;
			for(int j = 0; j < 50; j++){
				n += Funktioner.flipNumber(n);
				if(Funktioner.isPalindrom(n)){
					works = true;
					break;
				}
			}
			
			if(!works) lych++;
			
		}
		
		System.out.println(lych);
	}

}
