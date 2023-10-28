
public class Problem17 {

	public static void main(String[] args) {
		int total = 0;
		for(int i = 1; i <= 1000; i++){
			String s = Integer.toString(i);
			int cur = s.length();
			boolean addAnd = false;
			for(int j = 0; j < s.length(); j++){
				if(s.charAt(j) != '0'){
					if(cur == 4){
						total += getLength(Integer.parseInt(s.substring(j, j+1)));
						total += 8; //thousand
						addAnd = true;
					}else if(cur == 3){
						total += getLength(Integer.parseInt(s.substring(j, j+1)));
						total += 7; //hundred
						addAnd = true;
					}else if(cur == 2){
						if(addAnd) total += 3; //and
						if(s.charAt(j) == '1'){
							total += getLength(Integer.parseInt(s.substring(j, j+2)));
						}else{
							total += getLength(Integer.parseInt(s.substring(j, j+1)+"0"));
							char next = s.charAt(j+1);
							if(next != '0')
								total += getLength(Integer.parseInt(s.substring(j+1, j+2)));
						}
						j++;
						cur--;
					}else if(cur == 1){
						if(addAnd) total += 3; //and
						total += getLength(Integer.parseInt(s.substring(j, j+1)));
					}
				}
				cur--;
			}
		}
		System.out.println(total);
	}
	
	public static int getLength(int c){
		switch(c){
		case 1: //one
			return 3;
		case 2: //two
			return 3;
		case 3: //three
			return 5;
		case 4: //four
			return 4;
		case 5: //five
			return 4;
		case 6: //six
			return 3;
		case 7: //seven
			return 5;
		case 8: //eight
			return 5;
		case 9: //nine
			return 4;
		case 0: //zero
			return 4;
		case 10: //ten
			return 3;
		case 20: //twenty
			return 6;
		case 30: //thirty
			return 6;
		case 40: //forty
			return 5;
		case 50: //fifty
			return 5;
		case 60: //sixty
			return 5;
		case 70: //seventy
			return 7;
		case 80: //eighty
			return 6;
		case 90: //ninety
			return 6;
		case 11: //eleven
			return 6;
		case 12: //twelve
			return 6;
		case 13: //thirteen
			return 8;
		case 14: //fourteen
			return 8;
		case 15: //fifteen
			return 7;
		case 16: //sixteen
			return 7;
		case 17: //seventeen
			return 9;
		case 18: //eighteen
			return 8;
		case 19: //nineteen
			return 8;
		}
		return -1;
	}

}
