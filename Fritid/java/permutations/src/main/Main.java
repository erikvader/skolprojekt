package main;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;


public class Main {

	public static void main(String[] args) {
		new Main().run();
	}
	
	/*
	 * lite benchmark
	 * word: rimskogarna
	 * 48474.854067
	 * 45124.52253
	 * total: 27622907
	 * 
	 * slutsats: den första är aningen långsammare, men den är mer flexibel (kan ändra längden på orden som man vill generera)
	 */
	
	String word;
	ArrayList<String> perms;
	boolean[] inUse;
	
	public void run(){
		perms = new ArrayList<String>();
		
		Scanner scan = new Scanner(System.in);
		System.out.print("word: ");
		word = scan.nextLine();
		scan.close();
		
		inUse = new boolean[word.length()];
		Arrays.fill(inUse, false);
		
		long t = System.nanoTime();
		for(int i = 1; i <= word.length(); i++){
			recursOld("", i);
		}
		long after = System.nanoTime()-t;
		System.out.println(after/1000000.0);
		
		perms.clear();
		t = System.nanoTime();
		recurs("", word.length());
		after = System.nanoTime()-t;
		System.out.println(after/1000000.0);
		
		try{
			File f = new File("hej.txt");
			PrintWriter pw = new PrintWriter(f);
			for(String s : perms) pw.println(s);
			pw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		System.out.println("total: "+perms.size());
		
	}
	
	public void recurs(String prev, int length){
		if(prev.length() == length){
			//perms.add(prev);
			return;
		}
		
		HashSet<Character> visited = new HashSet<>();
		
		for(int i = 0; i < word.length(); i++){
			char c = word.charAt(i);
			if(!inUse[i]){
				if(visited.add(c)){
					inUse[i] = true;
					perms.add(prev+c);
					recurs(prev+c, length);
					inUse[i] = false;
				}
			}
		}
		
	}
	
	public void recursOld(String prev, int length){
		if(prev.length() == length){
			perms.add(prev);
			return;
		}
		
		HashSet<Character> visited = new HashSet<>();
		
		for(int i = 0; i < word.length(); i++){
			char c = word.charAt(i);
			if(!inUse[i]){
				if(visited.add(c)){
					inUse[i] = true;
					recursOld(prev+c, length);
					inUse[i] = false;
				}
			}
		}
		
	}

}
