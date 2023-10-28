import java.util.Scanner;

public class Namnsdag {

	public static void main(String[] args) {
		new Namnsdag().run();
	}
	
	String namn;
	int svar = 0;
	
	public void run(){
		Scanner scan = new Scanner(System.in);
		namn = scan.nextLine();
		int times = Integer.parseInt(scan.nextLine());
		
		String ord;
		for(int i = 0; i < times; i++){
			ord = scan.nextLine();
			if(svar != 0) continue;
			if(ord.length() == namn.length()){//att testa
				if(canChangeOne(ord, namn)){
					svar = i+1;
				}
			}
		}
		
		scan.close();
		
		System.out.println(svar);
	}
	
	private boolean canChangeOne(String a, String b){ //or same
		int different = 0;
		for(int i = 0; i < a.length(); i++){
			if(a.charAt(i) != b.charAt(i)){
				different++;
			}
		}
		if(different <= 1){
			return true;
		}else{
			return false;
		}
	}

}
