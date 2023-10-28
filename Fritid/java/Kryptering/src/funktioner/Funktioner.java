package funktioner;

public class Funktioner {
	
	/**
	 * Förenklar  och räknar ut a^b (mod c)
	 * <p>
	 * det kan hända att a^b blir alldeless för stort eller inte tillräckligt precist, då kan man få fel svar.
	 * Den här funktionen ska klara av att lösa dessa fall som "dummare" metoder inte kan.
	 * 
	 * @param bas basen a
	 * @param exponent exponenten b
	 * @param modulo modulo c
	 * @return det korrekta svaret.
	 */
	public static int bigExponentModulos(int bas, int exponent, int modulo){
		
		long left = bas;
		long right = 1;
		
		left %= modulo;
		
		while(exponent > 1){
			
			if(exponent % 2 == 1){
				right *= left;
				right %= modulo;
			}
			
			exponent /= 2;
			
			left = left*left;
			left %= modulo;
			
		}
		
		long both = left * right;
		both %= modulo;
		
		return (int)both;
		
	}
	
	/**
	 * Räknar ut Greatest Common Divider, alltså det största talet som man dividera a och b jämnt ut med.
	 * <p>
	 * Denna funktion använder sig av Euklides Algoritm.
	 * 
	 * @param a det ena talet
	 * @param b det andra talet
	 * @return den största gemensamma delare
	 */
	public static int GCD(int a, int b){
		
		int left = Math.max(a, b);
		int right = Math.min(a, b);
		
		int res = 0;
		
		do{
			res = left % right;
			left = right;
			right = res;
		}while(res != 0);
		
		return left;
		
	}
	
	/**
	 * Hittar inversen till ett tal i ringen Zn. om talet inte har en invers
	 * så returnas 0. (0 kan inte vara en invers (?))
	 * 
	 * @param tal talet du vill hitta en invers till
	 * @param n vilken Zn-ring 
	 * @return inversen ifall den finns, annars 0
	 */
	public static int findInvers(int tal, int n){
		int[] res = euklidesExtended(tal, n);
		
		if(res[0] != 1)
			return 0;
		
		return res[1];
	}
	
	/**
	 * Kör algoritmen "Euklidean Extended". Den hittar GCD samt x och y i uttrycket:
	 * <p>
	 * GCD(a, b) = x*a + y*b
	 * <p>
	 * 
	 * @param a det ena talet
	 * @param b	det andra talet
	 * @return en array där 0 = GCD, 1 = x, 2 = y
	 */
	public static int[] euklidesExtended(int a, int b){ //0 = GCD, 1 = x(a), 2 = y(b)
		
		boolean flipped = false;
		int[][] table = new int[4][3]; //q, r, s, t
		int curRow = 1;
		int prevRow = 0;
		int prevPrevRow = 2;
		
		//largest first
		if(b > a){
			flipped = true;
			int temp = a;
			a = b;
			b = temp;
		}
		
		//init table
		table[1][0] = a;
		table[2][0] = 1;
		table[3][0] = 0;
		table[1][1] = b;
		table[2][1] = 0;
		table[3][1] = 1;
		
		while(table[1][curRow] != 0){
			curRow = (curRow+1)%3;
			prevRow = (prevRow+1)%3;
			prevPrevRow = (prevPrevRow+1)%3;
			
			table[0][curRow] = table[1][prevPrevRow]/table[1][prevRow];
			table[1][curRow] = table[1][prevPrevRow] - table[0][curRow] * table[1][prevRow];
			table[2][curRow] = table[2][prevPrevRow] - table[0][curRow] * table[2][prevRow];
			table[3][curRow] = table[3][prevPrevRow] - table[0][curRow] * table[3][prevRow];
		}
		
		int[] ret = new int[3];
		ret[0] = table[1][prevRow];
		
		if(flipped){
			ret[1] = table[3][prevRow];
			ret[2] = table[2][prevRow];
		}else{
			ret[1] = table[2][prevRow];
			ret[2] = table[3][prevRow];
		}
		
		return ret;
		
	}
	
	
	
	
	
	
	
	
	
}
