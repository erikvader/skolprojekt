package mapFunction;

public class MapFunction {

	/*
	public static void main(String[] args) {
		//testar map
		Scanner scan = new Scanner(System.in);
		while(true){
			System.out.print(">> map(");
			String input = scan.nextLine();
			if(input.equals("q")) break;
			String[] ar = input.substring(0, input.length()-1).split(", ");
			double[] ints = new double[ar.length]; 
			for(int i = 0; i < ar.length; i++){
				ints[i] = Double.parseDouble(ar[i]);
			}
			System.out.println("= "+Double.toString(map(ints[0], ints[1], ints[2], ints[3], ints[4])));
		}
		scan.close();
	}
	*/
	
	/**
	 * Mappar ett värde v i ett intervall [x1, y1] till en korresponderande värde i intervallet [x2, y2]
	 * 
	 * @param v - värdet du vill mappa
	 * @param x1 - minsta värdet v kan ha
	 * @param y1 - största värdet v kan ha
	 * @param x2 - minsta/största värdet resultatet ska ha
	 * @param y2 - största/minsta värdet resultatet ska ha
	 * @return - ett värde mellan x2 och y2
	 */
	public static double map(double v, double x1, double y1, double x2, double y2){
		return x2+(v*(y2-x2))/(y1-x1);
	}

}
