import java.util.ArrayList;
import java.util.Scanner;

public class Problem79 {

	//funkar inte
	public static void main(String[] args) {
		String ans = "";
		ArrayList<String> inputs = new ArrayList<String>();
		
		Scanner scan = new Scanner(System.in);
		for(int i = 0; i < 50; i++){
			inputs.add(scan.nextLine());
		}
		scan.close();
		
		String input;
		for(int i = 0; i < inputs.size(); i++){
			input = inputs.get(i);
			int k = 0;
			for(int j = 0; j < ans.length(); j++){
				if(ans.charAt(j) == input.charAt(k)){
					k++;
					if(k == input.length()) break;
				}
			}
			ans += input.substring(k);
		}
		
		System.out.println("svaret: "+ans+", length: "+ans.length());
	}

}
