import java.util.Scanner;

public class Mangtydlig_Morse {

	public static void main(String args[]){
		new Mangtydlig_Morse().run();
	}
	
	private String[] alfabetet = new String[26];
	private String ord;
	
	public void run(){
		Scanner scan = new Scanner(System.in);
		ord = scan.nextLine();
		for(int i = 0; i < 26; i++){
			alfabetet[i] = scan.nextLine();
		}
		scan.close();
		
		solvera();
	}
	
	private void solvera(){
		recurs("", 0);
	}
	
	private void recurs(String ord, int index){
		if(index >= this.ord.length()){
			System.out.println(ord);
		}else{
			for(int i = 0; i < alfabetet.length; i++){
				if(this.ord.startsWith(alfabetet[i], index)){
					String tempOrd = ord;
					tempOrd += (char)(i+65);
					recurs(tempOrd, index+alfabetet[i].length());
				}
			}
		}
	}
	
	
	
	
}
