import java.util.Scanner;
import java.util.TreeMap;

//garanterat inte en s�n bra l�sning. Har inte lagt upp denna p� PO �n, s� kan inte kolla ifall den �r bra eller int.
//kan spara en kvadrat f�r varje kolumn. ifall en kvadrat hittar ett tr�d s� kan den r�tta till alla andra kvadrater som ocks� p�verkas.
//sen kan man forts�tta att anv�nda TreeMap-metoden f�r att f�rhoppningsvist f�rb�ttra prestandra lite. (kan nog komma p� smarta s�tt att anv�nda intilliggande kvadrater p� f�r att g�ra sakerna snabbare)

public class Skidor {

	public static void main(String[] args) {
		new Skidor().run();
	}
	
	int r, c, l;
	int[][] board;
	
	public void run(){
		Scanner scan = new Scanner(System.in);
		r = scan.nextInt();
		c = scan.nextInt();
		l = scan.nextInt();
		//scan.nextLine();
		board = new int[r][c];
		for(int i = 0; i < r; i++){
			for(int j = 0; j < c; j++){
				board[i][j] = scan.nextInt();
			}
		}
		scan.close();
		
		solvera();
	}
	
	public void solvera(){
		//sorterad grej som tar en H och mappar den till hur manga ganger den forekommer
		TreeMap<Integer, Integer> ruta = new TreeMap<Integer, Integer>();
		
		int bestr = -1, bestc = -1, besth = Integer.MAX_VALUE;
		boolean readWhole = true;
		int evilTree = -1;
		
		for(int rl = 0; rl <= r-l; rl++){
			readWhole = true;
			for(int cl = 0; cl <= c-l; cl++){
				
				if(readWhole){
					readWhole = false;
					evilTree = setRuta(rl, cl, ruta);
				}else{
					evilTree = readRutaLite(rl, cl, ruta);
				}

				if(evilTree != -1){
					cl = evilTree+1;
					readWhole = true;
					evilTree = -1;
				}else{
					int h = getH(ruta);
					if(h < besth){
						bestr = rl;
						bestc = cl;
						besth = h;
					}
				}
			}
		}
		
		System.out.println(Integer.toString(bestr) + " " + Integer.toString(bestc));
				
	}
	
	public int readRutaLite(int r, int c, TreeMap<Integer, Integer> ruta){
		for(int i = r; i < r+l; i++){
			if(board[i][c+l-1] == -1){
				return c+l-1;
			}
			addTree(ruta, board[i][c+l-1]);
			removeTree(ruta, board[i][c-1]);
		}
		return -1;
	}
	
	public int setRuta(int r, int c, TreeMap<Integer, Integer> ruta){
		ruta.clear();
		int rightmostTree = -1;
		for(int rl = r; rl < r+l; rl++){
			for(int cl = c; cl < c+l; cl++){
				if(rightmostTree == -1){
					addTree(ruta, board[rl][cl]);
				}
				
				if(board[rl][cl] == -1){
					if(rightmostTree < cl){
						rightmostTree = cl;
					}
				}
			}
		}
		
		return rightmostTree;
	}
	
	public int getH(TreeMap<Integer, Integer> ruta){
		return ruta.lastKey() - ruta.firstKey();
	}
	
	public void addTree(TreeMap<Integer, Integer> ruta, Integer grej){
		Integer i = ruta.get(grej);
		if(i != null){
			ruta.put(grej, i+1);
		}else{
			ruta.put(grej, 1);
		}
	}
	
	public void removeTree(TreeMap<Integer, Integer> ruta, Integer grej){
		Integer i = ruta.get(grej);
		if(i != null){
			if(i > 1){
				ruta.put(grej, i-1);
			}else{
				ruta.remove(grej);
			}
		}
	}

}
