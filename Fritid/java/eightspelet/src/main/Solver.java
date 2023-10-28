package main;


import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;



public class Solver {

	public static void main(String[] args) {
		new Solver().run();
	}
	
	
	
	byte[] board = new byte[9];
	int minsta = -1;
	State solved;
	Queue<State> queue = new LinkedList<State>();
	HashSet<State> visited = new HashSet<State>();
	
	public void run(){
		/*State s1 = new State(new byte[]{1, 2}, 0, 1);
		State s2 = new State(new byte[]{1, 2}, 0, 1);
		System.out.println(s1.equals(s2));*/
		Scanner scan = new Scanner(System.in);
		int zero = 0;
		for(int i = 0; i < 9; i++){
			board[i] = scan.nextByte();
			if(board[i] == 0){
				zero = i;
			}
		}
		scan.close();
		State s = new State(board, 0, zero);
		queue.add(s);
		visited.add(s);
		
		solvera();
		
		System.out.println(minsta);
		skriv(solved);
	}
	
	public void skriv(State s){
		System.out.println(s.toString());
		System.out.println("######");
		if(s.parent != null)
			skriv(s.parent);
	}
	
	public void solvera(){
		while(queue.size() > 0){
			State s = queue.poll();
			if(isSolved(s.board)){
				minsta = s.moves;
				solved = s;
				break;
			}
			int zeroIndex = s.zero;
			State temp;
			if(zeroIndex != 0 && zeroIndex != 3 && zeroIndex != 6){
				temp = flytta(s, zeroIndex-1, zeroIndex);
				temp.parent = s;
				if(visited.add(temp)) queue.add(temp);
			}
			if(zeroIndex != 2 && zeroIndex != 5 && zeroIndex != 8){
				temp = flytta(s, zeroIndex+1, zeroIndex);
				temp.parent = s;
				if(visited.add(temp)) queue.add(temp);
			}
			if(zeroIndex > 2){
				temp = flytta(s, zeroIndex-3, zeroIndex);
				temp.parent = s;
				if(visited.add(temp)) queue.add(temp);
			}
			if(zeroIndex < 6){
				temp = flytta(s, zeroIndex+3, zeroIndex);
				temp.parent = s;
				if(visited.add(temp)) queue.add(temp);
			}
			
		}
	}
	
	public boolean isSolved(byte[] b){
		for(int i = 0; i < b.length-1; i++){
			if(i+1 != b[i]){
				return false;
			}
		}
		
		return true;
	}
	
	public State flytta(State b, int m, int z){
		byte[] temp = b.board.clone();
		temp[z] = temp[m];
		temp[m] = 0;
		
		return new State(temp, b.moves+1, m);
	}
	
	public class State{
		public byte[] board;
		State parent;
		public int moves;
		public int zero;
		
		public State(byte[] b, int m, int z){
			board = b; moves = m; zero = z;
		}
		@Override
		public int hashCode(){
			/*
			int result = 0;
			for(int i = 0; i < board.length; i++){
				result = 37 * result + (int)board[i];
			}
			return result;
			*/
			return Arrays.hashCode(board);
		}
		@Override
		public boolean equals(Object o){
			byte[] tmp = ((State)o).board;
			for(int i = 0; i < tmp.length; i++){
				if(tmp[i] != board[i]) return false;
			}
			return true;
		}
		@Override
		public String toString(){
			String temp = "";
			for(int i = 0; i < board.length; i++){
				temp += Byte.toString(board[i])+" "+(i==2 || i==5?"\n":"");
			}
			return temp;
		}
	}

}
