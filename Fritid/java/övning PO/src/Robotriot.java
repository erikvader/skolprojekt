import java.awt.Point;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Robotriot {

	public static void main(String args[]){
		new Robotriot().run();
	}
	
	char[][] map; //y, x
	int lines, chars;
	//ArrayList<Point> robots;
	
	public void run(){
		Scanner scan = new Scanner(System.in);
		lines = scan.nextInt();
		chars = scan.nextInt();
		map = new char[lines][chars];
		//robots = new ArrayList<Point>();
		scan.nextLine();
		for(int i = 0; i < lines; i++){
			String line = scan.nextLine();
			for(int j = 0; j < chars; j++){
				map[i][j] = line.charAt(j);
				/*if(map[i][j] == 'X'){
					robots.add(new Point(j, i));
				}*/
			}
		}
		scan.close();
		
		solvera();
	}
	
	public void solvera(){
		if(search(new Point(0, 0))){
			System.out.println("Death to humans");
		}else{
			System.out.println("We are safe");
		}
	}
	
	public boolean search(Point robot){
		Queue<Point> coords = new LinkedList<Point>();
		boolean[][] visited = new boolean[chars][lines];
		
		coords.add(robot);
		visited[robot.x][robot.y] = true;
		
		while(!coords.isEmpty()){
			Point cur = coords.poll();
			if(map[cur.y][cur.x] == 'X'){
				return true;
			}
			
			int tx, ty;
			for(int x = -1; x <= 1; x++){
				for(int y = -1; y <= 1; y++){
					if((x == 0 || y == 0) && !(x == 0 && y == 0)){
						tx = cur.x+x;
						ty = cur.y+y;
						if(tx >= 0 && tx < chars && ty >= 0 && ty < lines){
							if(map[ty][tx] != '#' && !visited[tx][ty]){
								visited[tx][ty] = true;
								coords.add(new Point(tx, ty));
							}
						}
					}
				}
			}
		}
		
		return false;
	}
	
}
