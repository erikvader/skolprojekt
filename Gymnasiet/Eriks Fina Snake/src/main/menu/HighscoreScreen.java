package main.menu;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import main.HighscoreClass;

public class HighscoreScreen extends MenuScreen{

	private static final long serialVersionUID = 1L;
	
	private File[] hiscFiles;
	private int curFile = 0;
	private ArrayList<ArrayList<String>> everything; 
	private HashMap<File, String> highscoreNames = new HashMap<>();
	
	public HighscoreScreen(String title){
		super(title);
		
		hiscFiles = HighscoreClass.getHighscoreFiles();
		
		addOption("Next",new MenyuAction(){
			@Override
			public void action() {
				if(curFile < everything.size()-1){
					curFile++;
				}else{
					curFile = 0;
				}
				//repaint();
				//HighscoreClass.addScore("eri", 45, 45, 45, 0.67, false, 100, 100);
			}
		});
		addOption("Prev", new MenyuAction(){
			@Override
			public void action() {
				if(curFile > 0){
					curFile--;
				}else{
					if(everything.isEmpty())
						curFile = 0;
					else
						curFile = everything.size()-1;
				}
				//repaint();
			}
		});
		
		everything = HighscoreClass.getAllHighscore();
		fix();
		
	}
	
	
	private void fix(){//ska lägga alla presets först.
		int nextIndexToAdd = 0;
		for(int i = 0; i < everything.size(); i++){
			for(int j = 0; j < SettingsScreen.preset.length; j++){
				String generatedName = generateName(SettingsScreen.preset[j]);
				if(hiscFiles[i].getName().equals(generatedName)){
					changePlace(nextIndexToAdd++, i);
					highscoreNames.put(hiscFiles[nextIndexToAdd-1], SettingsScreen.presetNames[j]);
					break;
				}
			}
				
		}
	}
	
	private String generateName(String[] s){
		String finalGrej = "";
		for(String ss : s){
			finalGrej += ss+" ";
		}
		return finalGrej.substring(0, finalGrej.length()-1);
	}
	
	private void changePlace(int i, int j){
		File temp = hiscFiles[i];
		hiscFiles[i] = hiscFiles[j];
		hiscFiles[j] = temp;
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		//System.out.println(curFile);
		
		if(!everything.isEmpty()){
			String n = hiscFiles[curFile].getName();
			if(highscoreNames.containsKey(hiscFiles[curFile])){
				n = highscoreNames.get(hiscFiles[curFile]);
			}
			g.setFont(new Font("Arial", Font.BOLD, 35));
			FontMetrics fm = g.getFontMetrics();
			g.drawString(n, super.getWidth()/2-fm.stringWidth(n)/2, 215);
			
			ArrayList<String> selected = everything.get(curFile);
			g.setFont(new Font("Arial", Font.PLAIN, 20));
			fm = g.getFontMetrics();
			for(int i = 0; i < selected.size(); i++){
				String s = selected.get(i);
				//int tempIndex = s.indexOf(" ");
				s = (i+1)+". "+s;
				g.drawString(s, super.getWidth()/2-fm.stringWidth(s)/2, 235+(fm.getHeight()+1)*i);
			}
		}
		
	}

	

}
