import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Problem22 {
	
	public static void main(String[] args) {
		ArrayList<String> names = new ArrayList<String>(5000);
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
		
		Collections.sort(names);
		
		long score = 0;
		for(int i = 0; i < names.size(); i++){
			String name = names.get(i);
			long sc = 0;
			for(int j = 0; j < name.length(); j++){
				sc += (int)name.charAt(j) - 64;
			}
			score += sc*(i+1);
		}
		
		System.out.println(score);
	}

}
