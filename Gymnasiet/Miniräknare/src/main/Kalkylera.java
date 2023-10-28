package main;


/*
	****Functions and syntaxes****
	a+b, a-b, a*b, a/b, a+(b-c)
	−a //ett speciellt tecken för att visa negativa tal (U+2212) (inget vanligt bindesstreck)
	a^b
	√(a)
	(b)√(a) //b är vilken sort rot. 3 = kubikroten, 2 = kvadratroten
	LOG(a)
	(b)LOG(a) //b är basen
	SIN(a), COS(a), TAN(a), ASIN(a), ACOS(a), ATAN(a) //a i grader
	(a)!
	
	****konstanter****
	π //pi = 3.14 (10 decimaler)
	e //= 2.718 (10 decimaler)
	c //ljusets hastighet i vakum (m/s)
	φ //phi, det gyllene snittet = 1.618
	g //tyndgravitationen = 9.82
*/

/*
 	****char-tabell****
    − U+2212/8722
    ÷ U+00F7/247
    π U+03C0/960
    e U+0065/101
    φ U+03C6/966
    √ U+221A/8730
    !
    ⁿ U+207F/8319
    ← U+2190
    
  	
 */

//TO ADD: summa (zigma), absolutbelopp, variabler

public class Kalkylera{
	
	private final char[] symbols = {'+', '-', '/', '*'};
	
	public double kalkylera(String uttryck) throws Exception{
		uttryck = fixSubtraktion(uttryck);
		uttryck = fixKonstanter(uttryck);
		return Double.parseDouble(translateNegToMinus(calc(uttryck)));
	}

	private String calc(String uttryck){
		//uttryck = fixSubtraktion(uttryck);
		//uttryck = fixKonstanter(uttryck);
		String oldUttryck = "";
		
		do{
			oldUttryck = uttryck;
			uttryck = factorial(uttryck);
		}while(uttryck != oldUttryck);
		
		do{
			oldUttryck = uttryck;
			uttryck = rooten(uttryck);
		}while(uttryck != oldUttryck);
		
		do{
			oldUttryck = uttryck;
			uttryck = logaritmen(uttryck);
		}while(uttryck != oldUttryck);
		
		do{
			oldUttryck = uttryck;
			uttryck = trigonometri(uttryck);
		}while(uttryck != oldUttryck);
		
		do{
			oldUttryck = uttryck;
			uttryck = paranteser(uttryck);
		}while(uttryck != oldUttryck);
		
		do{
			oldUttryck = uttryck;
			uttryck = potenser(uttryck);
		}while(uttryck != oldUttryck);
		
		do{
			oldUttryck = uttryck;
			uttryck = multiplikation(uttryck);
		}while(uttryck != oldUttryck);
		
		do{
			oldUttryck = uttryck;
			uttryck = division(uttryck);
		}while(uttryck != oldUttryck);
		
		do{
			oldUttryck = uttryck;
			uttryck = addition(uttryck); //och subtraktion
		}while(uttryck != oldUttryck);
		
		return uttryck;
	}
	
	private String potenser(String uttryck) {
		
		String exponent = "";
		String base = "";
		String result = "";
		
		int upphojt = uttryck.indexOf('^');
		if(upphojt != -1){
			exponent = getRightNumber(upphojt, uttryck);
			base = getLeftNumber(upphojt, uttryck);
			
			result = Double.toString(Math.pow(Double.parseDouble(base), Double.parseDouble(exponent)));
			
			exponent = translateMinusToNeg(exponent);
			base = translateMinusToNeg(base);
			result = translateMinusToNeg(result);
			
			uttryck = uttryck.replace(base+"^"+exponent, result);
		}
		
		return uttryck;
	}

	private int getEndingParanthesis(int index, String uttryck){
		int firstParIndex = index;
		int lastParIndex = 0;
		int openPar = 0;
		
		//if(firstParIndex != -1){
		for(int i = firstParIndex+1; i < uttryck.length(); i++){
			if(uttryck.charAt(i) == ')'){
				if(openPar > 0){
					openPar--;
				}else{
					lastParIndex = i;
					break;
				}
			}else if(uttryck.charAt(i) == '('){
				openPar++;
			}
		}		
		//}
		
		return lastParIndex;
	}
	
	private int getBeginningParanthesis(int index, String uttryck){
		int firstParIndex = index;
		int lastParIndex = 0;
		int openPar = 0;
		
		//if(firstParIndex != -1){
		for(int i = firstParIndex-1; i >= 0; i--){
			if(uttryck.charAt(i) == '('){
				if(openPar > 0){
					openPar--;
				}else{
					lastParIndex = i;
					break;
				}
			}else if(uttryck.charAt(i) == ')'){
				openPar++;
			}
		}		
		//}
		
		return lastParIndex;
	}
	
	private String rooten(String uttryck){
		
		String talCalculated = "";
		String tal = "";
		String nterooten = "";
		String answer = "";
		int startParIndex = 0;
		int lastParIndex = 0;
		boolean defaultNteroot = false;
		String originalNteroot = "";
		
		int rootInd = uttryck.indexOf((char)8730);//√
		if(rootInd != -1){
			startParIndex = rootInd + 1;
			lastParIndex = getEndingParanthesis(startParIndex, uttryck);
			
			tal = uttryck.substring(startParIndex+1, lastParIndex);
			
			talCalculated = calc(tal);
			
			lastParIndex = rootInd - 1;
			if(lastParIndex >= 0 && uttryck.charAt(lastParIndex) == ')'){
				startParIndex = getBeginningParanthesis(lastParIndex, uttryck);
				originalNteroot = uttryck.substring(startParIndex+1, lastParIndex);
				nterooten = calc(originalNteroot);
			}else{
				defaultNteroot = true;
				nterooten = "2";
			}
			
			answer = Double.toString(Math.pow(Double.parseDouble(talCalculated), 1/Double.parseDouble(nterooten)));
			
			answer = translateMinusToNeg(answer);
			
			if(defaultNteroot){
				uttryck = uttryck.replace("√"+"("+tal+")", answer);
			}else{
				uttryck = uttryck.replace("("+originalNteroot+")"+"√"+"("+tal+")", answer);
			}
		}
		
		
		return uttryck;
	}
	
	private String factorial(String uttryck){
		String talCalculated = "";
		String tal = "";
		String answer = "";
		int startParIndex = 0;
		int lastParIndex = 0;
		
		int facInd = uttryck.indexOf('!');
		if(facInd != -1){
			startParIndex = facInd - 1;
			lastParIndex = getBeginningParanthesis(startParIndex, uttryck);
			
			tal = uttryck.substring(lastParIndex+1, startParIndex);
			
			talCalculated = calc(tal);
			
			answer = Double.toString(calcFactorial(Double.parseDouble(talCalculated)));
			
			uttryck = uttryck.replace("("+tal+")"+"!", answer);
			
		}
		
		
		return uttryck;	
		
	}
	
	private double calcFactorial(double siffra){
		if(siffra == 1){
			return siffra;
		}else{
			return siffra*calcFactorial(siffra-1);
		}
	}

	private String trigonometri(String uttryck) {
		String talCalculated = "";
		String tal = "";
		String answer = "";
		int startParIndex = 0;
		int lastParIndex = 0;
		
		String[] trigs = {"ASIN", "ACOS", "ATAN", "SIN", "COS", "TAN"};
		
		int index = 0;
		int curTrig = 0;
		
		for(int i = 0; i < trigs.length; i++){
			if((index = uttryck.indexOf(trigs[i])) != -1){
				curTrig = i;
				break;
			}
		}
		
		if(index != -1){
			startParIndex = curTrig > 2 ? index + 3 : index + 4;
			lastParIndex = getEndingParanthesis(startParIndex, uttryck);
			
			tal = uttryck.substring(startParIndex+1, lastParIndex);
			
			talCalculated = calc(tal);
			
			talCalculated = translateNegToMinus(talCalculated);
			
			Double inRadians = Math.toRadians(Double.parseDouble(talCalculated));
			
			switch(curTrig){
			case 0:
				answer = Double.toString(Math.toDegrees(Math.asin(Double.parseDouble(talCalculated))));
				break;
			case 1:
				answer = Double.toString(Math.toDegrees(Math.acos(Double.parseDouble(talCalculated))));
				break;
			case 2:
				answer = Double.toString(Math.toDegrees(Math.atan(Double.parseDouble(talCalculated))));
				break;
			case 3:
				answer = Double.toString(Math.sin(inRadians));
				break;
			case 4:
				answer = Double.toString(Math.cos(inRadians));
				break;
			case 5:
				answer = Double.toString(Math.tan(inRadians));
				break;
			}
			
			answer = translateMinusToNeg(answer);
			
			uttryck = uttryck.replace(trigs[curTrig]+"("+tal+")", answer);
		}
		
		return uttryck;
	}

	private String logaritmen(String uttryck) {
		String talCalculated = "";
		String tal = "";
		String bas = "";
		String answer = "";
		int startParIndex = 0;
		int lastParIndex = 0;
		boolean defaultBas = false;
		String originalBas = "";
		
		int logInd = uttryck.indexOf("LOG");
		if(logInd != -1){
			startParIndex = logInd + 3;
			lastParIndex = getEndingParanthesis(startParIndex, uttryck);
			
			tal = uttryck.substring(startParIndex+1, lastParIndex);
			
			talCalculated = calc(tal);
			
			lastParIndex = logInd - 1;
			if(lastParIndex >= 0 && uttryck.charAt(lastParIndex) == ')'){
				startParIndex = getBeginningParanthesis(lastParIndex, uttryck);
				originalBas = uttryck.substring(startParIndex+1, lastParIndex);
				bas = calc(originalBas);
			}else{
				defaultBas = true;
				bas = "10";
			}
			
			answer = Double.toString(Math.log(Double.parseDouble(talCalculated))/Math.log(Double.parseDouble(bas)));
			
			answer = translateMinusToNeg(answer);
			
			if(defaultBas){
				uttryck = uttryck.replace("LOG"+"("+tal+")", answer);
			}else{
				uttryck = uttryck.replace("("+originalBas+")"+"LOG"+"("+tal+")", answer);
			}
		}
		
		
		return uttryck;
	}

	private String paranteser(String uttryck){
		
		//boolean running = true;
		String result = "";
		int lastParIndex = 0;
		int firstParIndex = uttryck.indexOf('(');
		if(firstParIndex != -1){
			lastParIndex = getEndingParanthesis(firstParIndex, uttryck);
		
			result = calc(uttryck.substring(firstParIndex+1, lastParIndex));
		
			//result = translateMinusToNeg(result);
			
			String old = uttryck.substring(firstParIndex, lastParIndex+1);
			//old = old.replace("+", "\\+");
			
			uttryck = uttryck.replace(old, result);
		}
		
		return uttryck;
	}
	
	private String addition(String uttryck){
		String result = "";
		int index = 0;
		String leftSide = "";
		String rightSide = "";
		
		index = uttryck.indexOf('+');
		if(index != -1){
			leftSide = getLeftNumber(index, uttryck);
			rightSide = getRightNumber(index, uttryck);
			//paranteser på right and left
			
			result = Double.toString(Double.parseDouble(leftSide)+Double.parseDouble(rightSide));
			
			leftSide = translateMinusToNeg(leftSide);
			rightSide = translateMinusToNeg(rightSide);
			result = translateMinusToNeg(result);
			
			uttryck = uttryck.replaceFirst(String.format("%s\\+%s", leftSide, rightSide), result);
		}
		return uttryck;
	}
	
	private String multiplikation(String uttryck){
		String result = "";
		int index = 0;
		String leftSide = "";
		String rightSide = "";
		
		index = uttryck.indexOf('*');
		if(index != -1){
			leftSide = getLeftNumber(index, uttryck);
			rightSide = getRightNumber(index, uttryck);
			//paranteser på right and left
			
			result = Double.toString(Double.parseDouble(leftSide)*Double.parseDouble(rightSide));
			
			leftSide = translateMinusToNeg(leftSide);
			rightSide = translateMinusToNeg(rightSide);
			result = translateMinusToNeg(result);
			
			uttryck = uttryck.replaceFirst(String.format("%s\\*%s", leftSide, rightSide), result);
		}
		return uttryck;
	}
	
	private String division(String uttryck){
		String result = "";
		int index = 0;
		String leftSide = "";
		String rightSide = "";
		
		index = uttryck.indexOf('/');
		if(index != -1){
			leftSide = getLeftNumber(index, uttryck);
			rightSide = getRightNumber(index, uttryck);
			//paranteser på right and left
			
			if(rightSide.equals("0") || rightSide.equals("0.0") || rightSide.equals("0,0"))
				throw new java.lang.ArithmeticException("/ by zero");
			
			result = Double.toString(Double.parseDouble(leftSide)/Double.parseDouble(rightSide));
			
			leftSide = translateMinusToNeg(leftSide);
			rightSide = translateMinusToNeg(rightSide);
			result = translateMinusToNeg(result);
			
			uttryck = uttryck.replaceFirst(String.format("%s\\/%s", leftSide, rightSide), result);
		}
		return uttryck;
	}
	
	/*private String subtraktion(String uttryck){ //ganska lika kopia av "addition"
		String result = "";
		int index = 0;
		String leftSide = "";
		String rightSide = "";
		
		index = uttryck.indexOf('-');
		if(index != -1){
			leftSide = getLeftNumber(index, uttryck);
			rightSide = getRightNumber(index, uttryck);
			//paranteser på right and left
			
			result = Double.toString(Double.parseDouble(leftSide)-Double.parseDouble(rightSide));
			
			leftSide = translateMinusToNeg(leftSide);
			rightSide = translateMinusToNeg(rightSide);
			result = translateMinusToNeg(result);
			
			uttryck = uttryck.replaceFirst(String.format("%s\\-%s", leftSide, rightSide), result);
		}
		return uttryck;
	}*/
	
	private String getLeftNumber(int index, String uttryck){
		boolean running = true;
		int operatorIndex = index;
		do{
			if(index > 0)
				index--;
			for(char x : symbols){
				if(uttryck.charAt(index) == x){
					running = false;
					index++;
					break;
				}
			}	
		}while(running && index != 0);
		
		return translateNegToMinus(uttryck.substring(index, operatorIndex));
	}
	
	private String getRightNumber(int index, String uttryck){
		boolean running = true;
		int operatorIndex = index;
		do{
			index++;
			if(index == uttryck.length()-1){
				running = false;
				index++;
				break;
			}
				
			for(char x : symbols){
				if(uttryck.charAt(index) == x){
					running = false;
					break;
				}
			}	
		}while(running && index != uttryck.length()-1);
		
		return translateNegToMinus(uttryck.substring(++operatorIndex, index));
	}
	
	public String translateNegToMinus(String number){
		return number.replace((char)8722, (char)45);
	}
	
	public String translateMinusToNeg(String number){
		return number.replace((char)45, (char)8722);
	}
	
	private String fixSubtraktion(String uttryck){//gör om subtraktioner så att de funkar i addition
		uttryck = uttryck.replace("-−", "+");//minusNeg till plus
		uttryck = uttryck.replace("-", "+−");//minus till plusNeg
		uttryck = uttryck.replace("++", "+");//behövs egentligen inte
		
		return uttryck;
	}
	
	private String fixKonstanter(String uttryck) {
		char[] pi = {(char)960};
		char[] phi = {(char)966};
		
		uttryck = uttryck.replace(" ", "");
		uttryck = uttryck.replace(new String(pi), "3.1415926535");
		uttryck = uttryck.replace("e", "2.71828182846");
		uttryck = uttryck.replace("c", "299792458");
		uttryck = uttryck.replace(new String(phi), "1.6180339887"); // (1+√5)/2
		uttryck = uttryck.replace("g", "9.82");
		
		return uttryck;
	}
	
}
