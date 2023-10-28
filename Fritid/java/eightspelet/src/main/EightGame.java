package main;

import java.util.*;

public class EightGame
{
	public static void main(String [] klein)
	{
		Scanner scan = new Scanner(System.in);
 
		//Placerar ut brickorna p� br�det.
		long start = 0;
		for(int i = 0; i<9; i++)
		{
			System.out.print("Bricka ? ");
			start = (start<<4) + scan.nextInt();
		}
 
		//Letar r�tt p� var den tomma rutan finns.
		int pos = 0;
		for(long i = start; (i&15)!=0; i>>=4) ++pos;
 
		//H�r spar vi alla bes�kta positioner.
		HashSet <State> vis = new HashSet <State> ();
		State s = new State(start,0,pos);
		vis.add(s);
 
		//K� f�r bredden-f�rst-s�kning.
		LinkedList <State> qu = new LinkedList <State> ();
		qu.add(s);
 
		while(true) //BFS
		{
			s = qu.pop();
			if(s.board==0x123456780L) //�r vi klara?
			{
				System.out.println("\nAntal drag: " + s.mvs);
				break;
			}
 
			start = s.board; pos = s.pos; //�teranv�nder variabler. :D
 
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
			if(pos%3>0) //Flyttar h�ger.
			{
				tmp = new State(move(start,pos-1,pos), s.mvs+1, pos-1);
				if(vis.add(tmp)) qu.add(tmp);
			}
			if(pos%3<2) //Flyttar v�nster.
			{
				tmp = new State(move(start,pos+1,pos), s.mvs+1, pos+1);
				if(vis.add(tmp)) qu.add(tmp);
			}
		}
	}
 
	//board = br�det
	//from = positionen f�r brickan vi vill flytta
	//to = positionen f�r den tomma rutan
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
		//Spelbr�det
		final long board;
 
		//Antal drag och positionen f�r den tomma rutan.
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