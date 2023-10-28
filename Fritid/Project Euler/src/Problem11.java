import java.util.Scanner;

public class Problem11 {

	public static void main(String[] args) {
		//läs in tha grid
		int[][] table = new int[20][20]; //y, x
		Scanner scan = new Scanner(System.in);
		for(int i = 0; i < table.length; i++){
			String[] grej = scan.nextLine().split(" ");
			for(int j = 0; j < grej.length; j++){
				table[i][j] = Integer.parseInt(grej[j]);
			}
		}
		scan.close();
		
		int[][] visited = new int[20][20];//y, x //0: up, 1: up-right, etc.
		
		//sök
		int best = 0;
		for(int i = 0; i < table.length; i++){
			for(int j = 0; j < table[i].length; j++){
				for(int dir = 0; dir < 8; dir++){
					if(getBit(visited[i][j], dir)) break;
					int cur = 1;
					int newx = j;
					int newy = i;
					for(int step = 0; step < 4; step++){
						if(newx >= 0 && newy >= 0 && newx < 20 && newy < 20){
							cur *= table[newy][newx];
						}else{
							break;
						}
						
						if(step == 3){ //är på sista, sätt den till visited
							int opposite = (dir+4) % 8;
							visited[newy][newx] = setBit(visited[newy][newx], opposite, true);
						}else{
							newy -= Math.round(Math.sin((Math.PI/2) - (dir*Math.PI/4)));
							newx += Math.round(Math.cos((Math.PI/2) - (dir*Math.PI/4)));
						}
						
					}
					
					if(cur > best) best = cur;
				}
			}
		}
		
		System.out.println(best);
	}
	
	public static boolean getBit(int tal, int i){
		int t = tal >> i;
		return (t & 0b1) == 1 ? true : false;
	}
	
	public static int setBit(int tal, int i, boolean b){
		int mask = 1 << i;
		if(b){
			return tal | mask;
		}else{
			mask = ~mask;
			return tal & mask;
		}
	}

}
