package funktioner;

import java.util.ArrayList;

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
	
	/**
	 * den minsta gemensamma multipeln av två tal a och b
	 * @param a
	 * @param b
	 * @return
	 */
	public static int LCM(int a, int b){
		return (a*b)/GCD(a, b);
	}
	
	/**
	 * använder sig utav kort division för att skriva ut decimalexpansionen för 1/i. 
	 * @param i
	 * @return
	 */
	public static String divide(int i){
		String s = "0.";
		int[] rem = new int[i];
		int t = 1; //täljare
		int counter = 0;
		while(rem[t] == 0){
			counter++;
			rem[t] = counter;
			t *= 10;
			s += t/i;
			t %= i;
		}
		
		if(t == 0){
			s = s.substring(0, s.length()-1);
			return s;
		}else {
			int l = counter - rem[t];
			s = s.substring(0, s.length()-l-1) + "(" + s.substring(s.length()-l-1) + ")";
			return s;
		}
	}
	
	public static int choose(int n, int k){
		if(k == 0 || k == n){
			return 1;
		}else{
			return (int)Math.round(choose(n, k-1)*((n-k+1)/(double)k));
		}
	}
	
	/**
	 * använder doubles för att få scientific notation så att man kan ha större tal
	 * @param n
	 * @param k
	 * @return
	 */
	public static double chooseDouble(double n, double k){
		if(k == 0 || k == n){
			return 1;
		}else{
			return chooseDouble(n, k-1)*((n-k+1)/k);
		}
	}
	
	public static int permutation(int n, int k){
		if(k == 0){
			return 1;
		}else{
			return permutation(n, k-1)*(n-k+1);
		}
	}
	
	/**
	 * Kollar ifall ett tal endast innehåller udda siffror
	 * @param i
	 * @return
	 */
	public static boolean containsOnlyOdd(int i){
		while(i > 0){
			if((i % 10) % 2 == 0){
				return false;
			}
			i /= 10;
		}
		return true;
	}
	
	/**
	 * roterar ett nummer n åt vänster
	 * <br>
	 * 123 = 231
	 * @param n
	 * @return
	 */
	public static int rotateNumberLeft(int n, int length){
		if(n < 10 || length < 2) return n;
		int left = n;
		int remove = 1;
		for(int i = 0; i < length-1; i++){
			left /= 10;
			remove *= 10;
		}
		n -= remove*left;
		n *= 10;
		return n + left;
	}
	
	/**
	 * flippar ett tal. 123 -> 321 : 120 -> 21
	 * @param n
	 * @return
	 */
	public static long flipNumber(long n){
		long flip = 0;
		while(n > 0){
			flip *= 10;
			flip += n % 10;
			n /= 10;
		}
		return flip;
	}
	
	public static int flipNumber(int n){
		return (int)flipNumber((long)n);
	}
	
	public static boolean isPalindrom(long n){
		long flip = flipNumber(n);
		return flip == n;
	}

	/**
	 * conditions:
	 * <br>
	 * m > n > 0
	 * <br>
	 * m - n är ojämnt. (den ena måste vara ojämn och den andra jämn)
	 * <br>
	 * GCD(m, n) = 1
	 * 
	 * @param maxc maxlängd på hypotenusan för en tripplet
	 * @return en arraylist med arrayer på formen {a, b, c} 
	 */
	public static ArrayList<int[]> generatePrimitivePythagoreanTriples(int maxm){
		ArrayList<int[]> svar = new ArrayList<int[]>();
		for(int m = 2; m <= maxm; m++){
			for(int n = 1; n < m; n++){
				if((m - n) % 2 == 0) continue;
				if(GCD(m, n) != 1) continue;
				svar.add(new int[]{m*m - n*n, 2*m*n, m*m + n*n});
			}
		}
		return svar;
	}
	
	public static boolean isPentaNumber(int p){
		int n = (int)Math.round(1/6.0 + Math.sqrt(1/36.0+2*p/3.0));
		return p == n*(3*n-1)/2;
	}
	
	public static boolean isHexaNumber(int h){
		int n = (int)Math.round(1/4.0 + Math.sqrt(1/16.0+h/2.0));
		return h == n*(2*n-1);
	}
	
	public static boolean isTriangularNumber(int n){
		int test = (int)Math.round(-0.5+Math.sqrt(0.25+2*n));
		return n == test*(test+1)/2;
	}
	
	/**
	 * kollar ifall a och b är permutationer av varandra
	 * <br>
	 * 123 och 321 är permutationer av varandra
	 * <br>
	 * 0221 och 2012 är inte det för att den ena börjar på en nolla.
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean isPermutation(int a, int b){
		if((int)Math.log10(a)+1 != (int)Math.log10(b)+1) return false;
		
		int[] grejer = new int[10];
		while(a > 0){
			grejer[a % 10]++;
			a /= 10;
			grejer[b % 10]--;
			b /= 10;
		}
		
		for(int i = 0; i < grejer.length; i++){
			if(grejer[i] != 0) return false;
		}
		
		return true;
	}
	
}
