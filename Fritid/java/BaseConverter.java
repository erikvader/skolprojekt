import java.math.BigInteger;




public class BaseConverter {

	private final static char[] values = { //index blir till dess value
		'0', '1', '2', '3', '4', '5', '6' , '7', '8', '9', 'A', 'B',
		'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
		'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
	};
	
	private static int valuesToIndex(char c){
		for(int i = 0; i < values.length; i++){
			if(values[i] == c){
				return i;
			}
		}
		
		return -1;
	}
	
	/*
	private static HashMap<Character, Integer> values2;

	public BaseConverter(){
		for(int i = 0; i < values.length; i++){
			values2.put(values[i], i);
		}


	}
	
	private static void init(){
		
	}
	*/

	private static String toBase(int base, String number){

	    String result = "";
	    int startValue = 0;
	    int toAdd = 0;
	    BigInteger takeAway = new BigInteger("0");
	    BigInteger b = new BigInteger(Integer.toString(base));
	    BigInteger n = new BigInteger(number);

	    while(b.pow(startValue).compareTo(n) <= 0){
	        startValue++;
	    }
	    if(startValue > 0){startValue--; }
	    while(startValue >= 0){
	    	BigInteger bM1 = b.subtract(new BigInteger("1")); //b - 1
	        for(int i = 0; new BigInteger(Integer.toString(i)).compareTo(bM1) < 0; i++){ //i < (b-1)
	            takeAway = b.pow(startValue);
	            n = n.subtract(takeAway);

	            if(n.compareTo(new BigInteger("0")) >= 0){
	                toAdd++;
	            }else{
	                n = n.add(takeAway);
	                break;
	            }
	        }

	        result += values[toAdd];
	        startValue--;
	        toAdd = 0;
	    }

	    return result;
	}

	private static String toNumber(int base, String number){

	    BigInteger result = new BigInteger("0");
	    BigInteger toAdd = new BigInteger("0");
	    int startValue = number.length()-1;
	    int readValue = 0;
	    char curChar = ' ';
	    int curInt = 0;
	    BigInteger b = new BigInteger(Integer.toString(base));

	    while(startValue >= 0){

	        curChar = number.charAt(readValue);
	        curInt = valuesToIndex(curChar);
	        //result += Math.pow(base, startValue)*curInt;
	        toAdd = b.pow(startValue);
	        toAdd = toAdd.multiply(new BigInteger(Integer.toString(curInt)));
	        
	        result = result.add(toAdd);
	        
	        readValue++;
	        startValue--;

	    }

	    return result.toString();
	}

	public static String changeBase(String number, int fromBase, int toBase){
	    String conversionInt = "";
	    String returnString = "";
	    conversionInt = toNumber(fromBase, number);
	    returnString = toBase(toBase, conversionInt);
	    return returnString;
	}

	/*template <class N>
	N Base<N>::power(N base, N exponent){
	    N result = 1;
	    for(N i = 0; i < exponent; i++){
	        result *= base;
	    }
	    return result;
	}*/



}
