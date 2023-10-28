import java.util.Scanner;

public class Felskrivning {

	public static void main(String[] args) {
		new Felskrivning().run();
	}
	
	long[] blandlista; //vilken position som kan ersattas med vilken siffra
	String[] replacements;
	boolean[][][] replaUsed;
	String ord;
	
	int svar = 0;
	
	public void run(){
		Scanner scan = new Scanner(System.in);
		ord = scan.nextLine();
		int n = scan.nextInt();
		replacements = new String[n];
		replaUsed = new boolean[n][26][26]; //storsta replecement
		scan.nextLine();
		for(int i = 0; i < n; i++){
			replacements[i] = scan.nextLine();
		}
		
		scan.close();
		
		//fylla blandlista
		blandlista = new long[ord.length()];
		for(int i = 0; i < ord.length(); i++){
			blandlista[i] = -1;
			for(int j = 0; j < replacements.length; j++){
				int temp = replacements[j].indexOf(ord.charAt(i));
				if(temp != -1){
					blandlista[i] = j;
					blandlista[i] = blandlista[i] << 32;
					blandlista[i] += temp;
					break;
				}
			}
		}
		
		recurs(0);
		
		svar -= 1;
		
		System.out.println(svar);
	}
	
	public void recurs(int i){
		if(i == ord.length()){
			svar++;
			return;
		}
		
		int b = (int)(blandlista[i] >> 32);
		if(b != -1){
			int c = (int)blandlista[i];
			String r = replacements[b];
			for(int j = 0; j < r.length(); j++){
				if(!replaUsed[b][c][j] || c == j){
					replaUsed[b][c][j] = true;
					recurs(i+1);
					replaUsed[b][c][j] = false;
				}
			}
		}else{
			recurs(i+1);
		}
		
		
	}

}
