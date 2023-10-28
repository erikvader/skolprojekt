package main.bot;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import main.Apple;
import main.Ormdel;

public class ShortestBot extends SnakeBot{
	
	//TODO räknar inte med att svansen blir längre, är nog ett problem som inte förekommer så ofta
	//TODO längst upp och längst ner kan kan helt plötsligt vända 180 och åka in i sig själv
	
	int width, height;
	boolean solidWalls;
	ArrayList<Integer> moves = new ArrayList<>();
	int secondTime = 0;
	long maxDelay = 5000000000l;
	long timeDelayed = 0;
	
	public ShortestBot(int width, int height, boolean solidWalls){
		this.width = width;
		this.height = height;
		this.solidWalls = solidWalls;
		
	}
	
	@Override
	public int play(ArrayList<Ormdel> snake, Apple apple, int lastMove) {
		
		timeDelayed = 0;
		return go(snake, apple, lastMove, false);
		
	}
	
	private int go(ArrayList<Ormdel> snake, Apple apple, int lastMove, boolean second){
		if(moves.size() > 0){
			int toReturn = moves.get(moves.size()-1);
			moves.remove(moves.size()-1);
			return toReturn;
		}
		if(second == false){
			if(moves.size() == 0){
				findShortest(snake, apple);
			}
			
			return go(snake, apple, lastMove, true);
		}else{
			return lastMove;
		}
	}
	
	private void findShortest(ArrayList<Ormdel> snake, Apple apple){
		Queue<SnakeState> ko = new LinkedList<SnakeState>();
		SnakeState s = new SnakeState(snake, -1, null); 
		ko.add(s);
		
		int applePos = width*apple.getY() + apple.getX();
		boolean running = true;
		
		while(!ko.isEmpty() && running){
			SnakeState cur = ko.poll();
			long time1 = System.nanoTime();
			for(int i = 0; i < 4; i++){
				if(getOpposite(cur.lastMove) != i){ //gör inte 180 graders sväng.
					int[] tempOrm = cur.snake.clone();
					boolean success = moveOrm(tempOrm, i);
					if(success){
						SnakeState toAdd = new SnakeState(tempOrm, i, cur);
						if(applePos == tempOrm[0]){
							generateShortestPath(toAdd);
							return;
						}
						ko.add(toAdd);
					}
				}
			}
			timeDelayed += (int)(System.nanoTime() - time1);
			if(timeDelayed >= maxDelay) running = false;
		}
	}
	
	private void generateShortestPath(SnakeState s){
		SnakeState parent = s;
		do{
			moves.add((int) parent.lastMove);
			parent = parent.parent;
			
		}while(parent != null && parent.lastMove != -1);
	}
	
	private class SnakeState{
		public int[] snake;
		public byte lastMove;
		//public Apple apple;
		public SnakeState parent;
		
		public SnakeState(ArrayList<Ormdel> snake, /*Apple apple, */int lastMove, SnakeState parent){
			//apple = new Apple(apple);
			this.parent = parent;
			this.lastMove = (byte)lastMove;
			this.snake = new int[snake.size()];
			for(int i = 0; i < snake.size(); i++){
				Ormdel temp = snake.get(i);
				this.snake[i] = width*temp.getY() + temp.getX();
			}
		}
		
		public SnakeState(int[] orm, int lastMove, SnakeState parent){
			snake = orm;
			this.parent = parent;
			this.lastMove = (byte)lastMove;
		}
		
	}
	
	private int getOpposite(int i){
		switch(i){
		case 0:
			return 2;
		case 1:
			return 3;
		case 2:
			return 0;
		case 3:
			return 1;
		default:
			return -1;	
		}
	}
	
	private boolean moveOrm(int[] orm, int ormDir){
		//Ormdel tail = orm.get(orm.size()-1);
		//newTail.setLocation(tail.getX(), tail.getY());
		
		//flytta resten
		for(int i = orm.length-1; i > 0; i--){
			//orm.get(i).move(orm.get(i-1));
			orm[i] = orm[i-1];
		}
		//flytta huvud
		int huvud = orm[0];
		switch(ormDir){
		case 0:
			huvud -= width;
			break;
		case 1:
			huvud += 1;	
			break;
		case 2:
			huvud += width;
			break;
		case 3:
			huvud -= 1;
			break;
		}
		
		orm[0] = huvud;
		
		if(passedAWall(orm)){
			if(solidWalls){
				return false;
			}
		}
		
		//kolla kollision
		for(int i = orm.length-1; i > 0; i--){
			if(orm[0] == orm[i]){
				return false;
			}
		}
		
		return true;
		
	}
	
	private boolean passedAWall(int[] h){
		int old = h[0];
		return getCorrectPos(h) != old;
	}
	
	private int getCorrectPos(int[] h){
		int huvud = h[0];
		//specialfall för att huvudets pos blir negativt så det funkar inte på den andra if-satsen
		if(huvud == -1 && h[1] == 0){
			huvud = width-1;
		}else if(huvud%width == width-1 && h[1]%width == 0){//gått in i vänstra väggen
			huvud += width;
		}else if(huvud%width == 0 && h[1]%width == width-1){
			huvud -= width;
		}else if(huvud < 0){
			huvud = width*height + huvud;
		}else if(huvud > width*height -1){
			huvud = huvud - (width*height);
		}
		
		h[0] = huvud;
		return huvud;
	}
	
}














