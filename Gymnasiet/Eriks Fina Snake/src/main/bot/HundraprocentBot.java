package main.bot;

import java.util.ArrayList;

import main.Apple;
import main.Ormdel;

public class HundraprocentBot extends SnakeBot{

	int height, width;
	
	public HundraprocentBot(int width, int height) throws RuntimeException{
		if(width%2==0 && height%2==0){
			this.height = height;
			this.width = width;
		}else{
			throw new RuntimeException("width och height måste vara jämna!");
		}
	}
	
	@Override
	public int play(ArrayList<Ormdel> snake, Apple apple, int lastestMove) {
		int x = snake.get(0).getX();
		int y = snake.get(0).getY();
		
		if(x == 0 && y > 0){
			return 0;
		}/*else if(x < width-1 && y == 0){
			return 1;
		}*/else if(y % 2 == 0){
			if(x < width-1){
				return 1;
			}else{
				return 2;
			}
		}else if(y % 2 != 0){
			if(x > 1 || y == height-1){
				return 3;
			}else{
				return 2;
			}
		}
		
		return 0;
	}
	
}
