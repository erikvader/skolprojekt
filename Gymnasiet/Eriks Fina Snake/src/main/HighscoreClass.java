package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.StringTokenizer;

public class HighscoreClass{
	
	public static void addScore(String name, int score, int timerA, int timerC, int timerPercent, boolean walls, int width, int height){
		String fileName = timerA + " "+timerC+" "+timerPercent+" "+walls+" "+width+" "+height;
		addScore(name, score, fileName);
	}
	
	public static void addScore(String name, int score, String fileName){
		
		try{
			File f = new File("snake/highscores", fileName);
			if(!f.exists())
				f.createNewFile();
			
			ArrayList<String> sorted = getHighscore(fileName);
			sorted.add(name+" "+Integer.toString(score));
			Collections.sort(sorted, c);
			PrintStream ps = new PrintStream(f);
			for(int i = 0; i < sorted.size(); i++){
				if(i < 10)
					ps.println(sorted.get(i));
				else
					break;
			}
			ps.close();
		}catch (Exception e){
			e.printStackTrace();
		}
		
	}
	
	static Comparator<String> c = new Comparator<String>(){
		@Override
		public int compare(String o1, String o2) {
			StringTokenizer st = new StringTokenizer(o1);
			st.nextToken();
			int val1 = Integer.parseInt(st.nextToken());
			st = new StringTokenizer(o2);
			st.nextToken();
			int val2 = Integer.parseInt(st.nextToken());
			
			if(val1 == val2)
				return 0;
			else if(val1 < val2)
				return 1;
			else
				return -1;
		}};
	
		
	public static File[] getHighscoreFiles(){
		File f = null;
		try{
			f = new File("snake/highscores");
		}catch (Exception e){
			e.printStackTrace();
		}
		File[] temp = f.listFiles();
		if(temp != null)
			return temp;
		else
			return new File[0];
	}
	
	public static ArrayList<String> getHighscore(String fileName){
		try{
			File f = new File("snake/highscores", fileName);
	
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
			ArrayList<String> sorted = new ArrayList<>();
			String input = "";
			while((input = br.readLine()) != null){
				sorted.add(input);
			}
			Collections.sort(sorted, c);
			br.close();
			return sorted;
		}catch(Exception e){
			//e.printStackTrace();
			System.out.println("hittade inte "+fileName+" när vi skulle kolla vad som fanns i den.");
		}
		return new ArrayList<String>();
	}
	
	public static ArrayList<ArrayList<String> > getAllHighscore(){
		ArrayList<ArrayList<String> > all = new ArrayList<ArrayList<String> >();
		File[] files = getHighscoreFiles();
		for(int i = 0; i < files.length; i++){
			all.add(getHighscore(files[i].getName()));
		}
		return all;
	}
	
	public static boolean isInTop10(int score, String fileName){
		ArrayList<String> high = getHighscore(fileName);
		
		if(!high.isEmpty()){
			if(high.size() < 10){
				return true;
			}else{
				StringTokenizer st = new StringTokenizer(high.get(high.size()-1));
				st.nextToken();
				int lastScore = Integer.parseInt(st.nextToken()); 
				if(score >= lastScore){
					return true;
				}
			}
		}else{
			return true;
		}
		
		return false;
		
	}
	
	public static boolean isInTop10(int score, int timerA, int timerC, int timerPercent, boolean walls, int width, int height){
		String fileName = timerA + " "+timerC+" "+timerPercent+" "+walls+" "+width+" "+height;
		return isInTop10(score, fileName);
	}
	
}
