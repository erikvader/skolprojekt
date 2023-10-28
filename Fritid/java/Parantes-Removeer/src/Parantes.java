import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class Parantes {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		
		System.out.println("Skriv in ett uttryck plz:");
		String in = scan.nextLine();
		String fixad = removeParanteser(in);
		System.out.println("Nu är den fixad! Titta!");
		System.out.println(fixad);
		
		scan.close();
	}
	
	public static String removeParanteser(final String s){
		ArrayList<Par> par = new ArrayList<Par>();
		
		//hitta alla par
		Stack<Integer> paran = new Stack<Integer>();
		char c;
		for(int i = 0; i < s.length(); i++){
			c = s.charAt(i);
			if(c == '('){
				paran.push(i);
			}else if(c == ')'){
				int start = paran.pop();
				par.add(new Par(start, i));
			}
		}
		
		/*
		for(Par p : par)
			System.out.println(p);
		*/
		
		//markera de som ska tas bort
		char[] mask = s.toCharArray();
		//ta bort om det finns en runt om hela uttrycket (specialfall)
		Par last = par.get(par.size()-1);
		if(last.left == 0 && last.right == mask.length-1){
			mask[0] = '$';
			mask[mask.length-1] = '$';
		}
		
		//hitta resten
		Par p1;
		Par p2;
		for(int i = par.size()-1; i >= 1; i--){
			p1 = par.get(i);
			p2 = par.get(i-1);
			
			if(p1.left == p2.left-1 && p1.right == p2.right+1){
				mask[p2.left] = '$';
				mask[p2.right] = '$';
			}
		}
		
		//bygger ny med alla markeringar borta
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < mask.length; i++){
			if(mask[i] != '$'){
				sb.append(mask[i]);
			}
		}
		
		return sb.toString();
	}
	
	private static class Par{
		public int left, right;
		public Par(int left, int right){
			this.left = left;
			this.right = right;
		}
		@Override
		public String toString() {
			return "Par("+left + ", " + right+")";
		}
	}

}
