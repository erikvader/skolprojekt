
public class Problem63 {

	public static void main(String[] args) {
		int svar = 0;
		int pow = 0;
		int bas = 0;
		double tal = 0;
		int length;
		boolean running = true;
		while(running){
			pow++;
			bas = 0;
			do{
				bas++;
				tal = Math.pow(bas, pow);
				length = (int)Math.log10(tal)+1;
				if(length == pow){
					svar++;
					System.out.println(bas+" "+pow);
				}
			}while(length <= pow && pow < 309); //dålig lösning
			
			if(bas <= 2) running = false;
		}
		
		System.out.println("svaret: "+svar);
	}

}
