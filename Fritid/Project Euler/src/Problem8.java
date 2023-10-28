import java.util.Scanner;

public class Problem8 {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		String tal = "";
		String input = "";
		do{
			tal += input;
			input = scan.nextLine();
		}while(!input.equals("sluta"));
		scan.close();
		
		int len = 13;
		
		long largest = 0;
		for(int i = 0; i <= tal.length()-len; i++){
			long sum = 1;
			for(int j = i; j < i+len; j++){
				sum *= Integer.parseInt(tal.substring(j, j+1));
			}
			if(sum > largest) largest = sum;
		}
		
		
		System.out.println(largest);
		
	}

}
