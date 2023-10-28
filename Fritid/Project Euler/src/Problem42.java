import java.util.ArrayList;
import java.util.Scanner;

public class Problem42 {

	public static void main(String[] args) {
		ArrayList<String> names = new ArrayList<String>(2000);
		Scanner scan = new Scanner(System.in);
		String s = scan.nextLine();
		scan.close();
		
		String cur = "";
		for(int i = 0; i < s.length(); i++){
			char c = s.charAt(i);
			if(c != '"'){
				if(c == ','){
					names.add(cur);
					cur = "";
				}else{
					cur += c;
				}
			}
		}
		names.add(cur);
		
		//leta
		int svar = 0;
		for(int i = 0; i < names.size(); i++){
			String n = names.get(i);
			int summa = 0;
			for(int j = 0; j < n.length(); j++){
				summa += (int)(n.charAt(j)-64);
			}
			
			if(isTriangularNumber(summa)){
				svar++;
			}
		}
		
		System.out.println("svaret till denna underliga fråga är: "+svar);
	}
	
	public static boolean isTriangularNumber(int n){
		int test = (int)Math.round(-0.5+Math.sqrt(0.25+2*n));
		return n == test*(test+1)/2;
	}

}
