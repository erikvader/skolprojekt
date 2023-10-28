import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class Solnedgang {

	public static void main(String[] args) {
		new Solnedgang().run();
	}
	
	ArrayList<ArrayList<Point>> houses;
	long numHouses;
	long time;
	long curTime = 0;
	Point start, end;
	long width = 0, height = 0;
	long svar = -1;
	
	ArrayList<Point> path = new ArrayList<Point>();
	
	public void run(){
		Scanner scan = new Scanner(System.in);
		
		numHouses = scan.nextLong();
		time = scan.nextLong();
		//scan.nextLine();
	
		
		houses = new ArrayList<ArrayList<Point>>(0);
		
		Point p;
		for(int i = 0; i < numHouses; i++){
			p = new Point(scan.nextInt(), scan.nextInt());
			if(i == 0){
				start = new Point(p.x, p.y+1);
			}
			if(i == numHouses-1){
				end = new Point(p.x, p.y+1);
			}
			//houses.add(p);
			addHouse(p);
			
			if(p.x+3 > width) width = p.x+3;
			if(p.y+3 > height) height = p.y+3;
		}
		
		scan.close();
		
		fixHouses();
		
		solvera();
		
		
	}
	
	public void addHouse(Point p){
		while(houses.size() <= p.x){
			houses.add(new ArrayList<Point>());
		}
		
		houses.get(p.x).add(p);
	}
	
	public void fixHouses(){
		for(int i = 0; i < houses.size(); i++){
			Collections.sort(houses.get(i), houseComp);
		}
	}
	
	public Point getClosestHouseUnder(Point p){
		if(p.x < 0 || p.x >= houses.size())
			return null;
		
		ArrayList<Point> hou = houses.get(p.x);
		
		if(hou.size() == 0) return null;
		/*
		Point h;
		for(int i = hou.size()-1; i >= 0; i--){
			h = hou.get(i);
			if(h.y < p.y){
				return h;
			}
		}
		
		return null;
		*/
		//binary search
		
		int min = 0;
		int max = hou.size()-1;
		Point h = null;
		
		while(min < max){
			int mitt = (int)Math.ceil((min+max)/2.0);
			h = hou.get(mitt);
			if(p.y > h.y)
				min = mitt;
			else if(p.y < h.y)
				max = mitt-1;
		}
		
		h = hou.get(min);
		if(h.y < p.y)
			return h;
		else
			return null;
	}
	
	public Comparator<Point> houseComp = new Comparator<Point>(){
		@Override
		public int compare(Point o1, Point o2) {
			return Integer.compare(o1.y, o2.y);
		}
		
	};
	
	public void solvera(){
		findShortestPath();
		
		//findLowestShadow();
		
		/*
		for(Point p : path){
			System.out.println(p);
		}
		*/
		
		if(svar >= time || svar == -1){
			System.out.println("NATT");
		}else{
			System.out.println(svar);
		}
	}
	
	public void findLowestShadow(){
		Point p;
		Point h;
		int delta = -1;
		int closest = -1;
		ArrayList<Point> col;
		for(int i = 0; i < path.size(); i++){
			p = path.get(i);
			col = houses.get(p.x);
			closest = -1;
			for(int j = 0; j < col.size(); j++){
				h = col.get(j);
				delta = p.y - (h.y+1);
				if(delta >= 0 && (delta < closest || closest == -1)){
					closest = delta;
				}
			}
			if(closest == -1){
				svar = time;
				return;
			}
			if(closest > svar){
				svar = closest;
			}
		}
	}
	
	public int getNewG(Node n1, Node n2){
		if(Math.abs(n1.x-n2.x) == 1 && Math.abs(n1.y-n2.y) == 1){
			return 14;
		}
		return 10;
	}
	
	public int getHeuristic(Node neighbour) {
		int dx = Math.abs(neighbour.x-end.x);
		int dy = Math.abs(neighbour.y-end.y);
		
		return (dx+dy)*10;
	}
	
	public void getMap(Node[][] nodes){
		//Point h;
		for(int i = 0; i < houses.size(); i++){
			for(Point p : houses.get(i)){
				nodes[p.x][p.y] = new Node(null, p.x, p.y, false, false);
				nodes[p.x][p.y+1] = new Node(null, p.x, p.y+1, true, true);
				
			}
		}
		
		nodes[start.x][start.y] = new Node(null, start.x, start.y, true, true);
		nodes[end.x][end.y] = new Node(null, end.x, end.y, true, true);
	}
	
	public void increaseTime(){
		curTime++;
		
	}
	
	public void findShortestPath(){
		Node[][] nodes = new Node[(int)width][(int)(height+time)];
		getMap(nodes);
		
		ArrayList<Node> openList = new ArrayList<>();
		ArrayList<Node> closedList = new ArrayList<>();
		
		openList.add(nodes[start.x][start.y]);
		
		while(openList.size() > 0){
			Collections.sort(openList, lowestFComparator);
			Node current;
			int grej = 0;
			do{
				grej++;
				current = openList.get(openList.size()-grej);
			}while(current.dayTime == false && grej < openList.size());
			
			if(current.dayTime == false){
				//increase time
				curTime++;
				if(curTime == time){
					svar = time;
					return;
				}
				Point h;
				for(Node n : openList){
					h = getClosestHouseUnder(n.getPoint());
					if(n.y - (h.y+1) <= curTime){
						n.dayTime = true;
					}
				}
				continue;
			}
			
			openList.remove(openList.size()-grej);
			closedList.add(current);
			
			if(current.equals(end)){
				//getPath(current);
				svar = curTime;
				return;
			}
			
			for(int x = -1; x <= 1; x++){
				for(int y = -1; y <= 1; y++){
					if(x == 0 && y == 0) continue;
					if(Math.abs(x) == 1 && Math.abs(y) == 1) continue;
					
					int newX = current.x+x;
					int newY = current.y+y;
					
					if(newX >= 0 && newX < width && newY >= 0 && newY < (height+curTime-1)){
						Node neighbour = nodes[newX][newY];
						if(neighbour == null){
							Point h;
							if((h = getClosestHouseUnder(new Point(newX, newY))) != null){
								nodes[newX][newY] = neighbour = new Node(null, newX, newY, true, (newY - (h.y+1) <= curTime ? true : false));
							}else{
								continue;
							}
						}
						if(!neighbour.traversable || closedList.contains(neighbour)){
							continue;
						}
						
						int newG = current.g + getNewG(current, neighbour);
						
						if(!openList.contains(neighbour) || newG < neighbour.g){
							neighbour.parent = current;
							neighbour.g = newG;
							neighbour.h = getHeuristic(neighbour);
							if(!openList.contains(neighbour)){
								openList.add(neighbour);
							}
						}
					}
					
				}
			}
		}
	}
	
	public void getPath(Node n){
		path.add(n.getPoint());
		
		Node parent = n.parent;
		do{
			path.add(parent.getPoint());
			parent = parent.parent;
		}while(parent != null);
		
	}
	
	class Node{
		public int x, y, g = 0, h = 0; //x ,y anvands val for att kunna separera noderna
		public Node parent;
		public boolean traversable = true;
		public boolean dayTime = false;
		
		public Node(Node p, int x, int y, boolean traversable, boolean dayTime){
			parent = p;
			this.x = x;
			this.y = y;
			this.traversable = traversable;
			this.dayTime = dayTime;
		}
		
		public int getF(){
			return g + h;
		}
		
		public Point getPoint(){
			return new Point(x, y);
		}
		
		public Node(Node p, int x, int y, int g, int h){
		
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + x;
			result = prime * result + y;
			return result;
		}
		
		public boolean equals(Point p){
			return x==p.x && y==p.y;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Node other = (Node) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (x != other.x)
				return false;
			if (y != other.y)
				return false;
			return true;
		}

		private Solnedgang getOuterType() {
			return Solnedgang.this;
		}

		@Override
		public String toString() {
			return "Node [x=" + x + ", y=" + y + ", g=" + g + ", h=" + h + ", parent=[" + parent.x + ", "+parent.y + "], traversable=" + traversable + "], dayTime="+dayTime+"]";
		}
		
		
		
	}
	
	Comparator<Node> lowestFComparator = new Comparator<Node>(){

		@Override
		public int compare(Node n1, Node n2) { //minsta hamnar langst bak
			
			if(n1.getF() < n2.getF()){
				return 1;
			}else if(n1.getF() == n2.getF()){
				return 0;
			}else{
				return -1;
			}
		}
		
	};

}
