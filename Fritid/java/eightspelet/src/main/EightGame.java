package main;

import java.util.*;

public class EightGame
{
	public static void main(String [] klein)
	{
		Scanner scan = new Scanner(System.in);
 
		//Placerar ut brickorna på brädet.
		long start = 0;
		for(int i = 0; i<9; i++)
		{
			System.out.print("Bricka ? ");
			start = (start<<4) + scan.nextInt();
		}
 
		//Letar rätt på var den tomma rutan finns.
		int pos = 0;
		for(long i = start; (i&15)!=0; i>>=4) ++pos;
 
		//Här spar vi alla besökta positioner.
		HashSet <State> vis = new HashSet <State> ();
		State s = new State(start,0,pos);
		vis.add(s);
 
		//Kö för bredden-först-sökning.
		LinkedList <State> qu = new LinkedList <State> ();
		qu.add(s);
 
		while(true) //BFS
		{
			s = qu.pop();
			if(s.board==0x123456780L) //Är vi klara?
			{
				System.out.println("\nAntal drag: " + s.mvs);
				break;
			}
 
			start = s.board; pos = s.pos; //Återanvänder variabler. :D
 
			State tmp;
			if(pos<6) //Flyttar tomma rutan upp.
			{
				tmp = new State(move(start,pos+3,pos), s.mvs+1, pos+3);
				if(vis.add(tmp)) qu.add(tmp);
			}
			if(pos>2) //Flyttar ned.
			{
				tmp = new State(move(start,pos-3,pos), s.mvs+1, pos-3);
				if(vis.add(tmp)) qu.add(tmp);
			}
			if(pos%3>0) //Flyttar höger.
			{
				tmp = new State(move(start,pos-1,pos), s.mvs+1, pos-1);
				if(vis.add(tmp)) qu.add(tmp);
			}
			if(pos%3<2) //Flyttar vänster.
			{
				tmp = new State(move(start,pos+1,pos), s.mvs+1, pos+1);
				if(vis.add(tmp)) qu.add(tmp);
			}
		}
	}
 
	//board = brädet
	//from = positionen för brickan vi vill flytta
	//to = positionen för den tomma rutan
	//Flyttar en bricka till den tomma rutan.
	private static long move(final long board, int from, int to)
	{
		from <<= 2; //from*4
		to <<= 2;
		final long mask = 0xFL<<from;
		return (board&mask)>>from<<to|board&~mask;
	}
 
	private static class State
	{
		//Spelbrädet
		final long board;
 
		//Antal drag och positionen för den tomma rutan.
		final int mvs, pos;
 
		State(long b, int m, int p)
		{
			board = b; mvs = m; pos = p;
		}
 
		public int hashCode()
		{
			return (int)board;
		}
 
		public boolean equals(Object o)
		{
			return ((State)o).board==board;
		}
	}
}