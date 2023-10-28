package game.hud;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import game.handlers.Content;
import game.main.GamePanel;


public class Conversation {
	
	private ArrayList<String> messages;
	private ArrayList<Integer> profileIndex;
	private int currentSlide = 0;
	private boolean currentSlideDone = false;
	private boolean done = false;
	private int currentIndex = 0;
	private boolean enter = false;
	private boolean firstTime = true;
	private final int delay = 5;
	private int counter = 0;
	
	private int x, y;
	
	//profiles
	private ArrayList<Profile> profiles;
	
	private BufferedImage sprite;
	
	private Font font1;
	private Font font2;
	
	//sides
	private int textX, profileX, nameX;
	private int profileNameWidth = 40;
	
	public Conversation(){
		messages = new ArrayList<String>();
		profiles = new ArrayList<Profile>();
		profileIndex = new ArrayList<Integer>();
		
		sprite = Content.conversation1;
		
		font1 = new Font("Arial", Font.PLAIN, 12);
		font2 = new Font("Arial", Font.PLAIN, 10);
		
		x = 84;
		y = 230;
		
	}
	
	/*
	public void addProfile(Profile p){
		profiles.add(p);
	}
	*/
	
	private void setSide(int s){
		if(s == 0){
			textX = 52; //65
			profileX = 10; //23
			nameX = 3; //16
		}else if(s == 1){
			textX = 7; //20
			profileX = 252; //265
			nameX = 245; //260
		}
	}
	
	public void reset(){
		done = false;
		currentSlide = 0;
		counter = 0;
		currentIndex = 0;
		firstTime = true;
		currentSlideDone = false;
		enter = false;
	}
	
	public void setEmpty(){ //en tom conversation som det inte ska göras något med
		done = true;
		firstTime = false;
	}
	
	public boolean getDone(){
		return done;
	}
	
	public void setEnter(boolean b){enter = b;}
	
	private int getLength(String s){
		String[] msg = s.split("\n");
		int total = 0;
		for(String a : msg){
			total += a.length();
		}
		return total;
	}
	
	public void addMessage(String s, Profile p){
		//profiler
		if(!profiles.contains(p)){
			profiles.add(p);
		}
		profileIndex.add(profiles.indexOf(p));
		
		//splittta text rätt
		FontMetrics fm = GamePanel.g.getFontMetrics(font1);
		String finalMsg = "";
		int index = s.indexOf("\n");
		int last = 0;
		while (index >= 0) {
			finalMsg += split(s.substring(last, index), fm)+"\n";
			last = index+1;
		    index = s.indexOf("\n", index + 1);
		}
		finalMsg += split(s.substring(last, s.length()), fm);
		
		messages.add(finalMsg);
		
		//kolla if för långt
		int newLineCount = 0;
		index = finalMsg.indexOf("\n");
		while (index >= 0) {
			newLineCount++;
		    index = finalMsg.indexOf("\n", index + 1);
		}
		if(newLineCount > 2)
			System.out.println("\""+s+"\" har för många radbyten, "+newLineCount+" stycken.");
	}
	
	//lägger till nya rader på rätt ställen
	private String split(String s, FontMetrics fm){
		s = s.trim();
		s += " ";
		String curRad = "";
		int start = 0;
		int end = -1;
		String finalMsg = "";
		
		while((end = s.indexOf(' ', start+1)) != -1){
			String ord = s.substring(start, end);
			if(fm.stringWidth(curRad + ord) > 240){
				finalMsg += curRad+"\n";
				curRad = "";
			}
			curRad += ord+" ";
			start = end+1;
		}
		
		finalMsg += curRad;
		if(finalMsg.endsWith(" "))
			finalMsg = finalMsg.substring(0, finalMsg.length()-1);
		
		return finalMsg;
	}
	
	public void draw(Graphics2D g){
		if(done) return;
		g.drawImage(sprite, x, y, null);
		
		//profile pic
		Profile p = profiles.get(profileIndex.get(currentSlide));
		g.drawImage(p.getPic(), profileX+x, 7+y, null); //176
		
		//profile name
		drawName(g, p);
		
		//da text
		drawText(g);
		
	}
	
	private void drawName(Graphics2D g, Profile p){
		g.setFont(font2);
		g.setColor(Color.YELLOW);
		
		FontMetrics fm = g.getFontMetrics(font2);
		int k = fm.stringWidth(p.getName());
		
		int temp = (nameX+x)+(profileNameWidth-k)/2;
		
		g.drawString(p.getName(), temp, 46+y); //215
	}
	
	private void drawText(Graphics2D g){
		g.setColor(Color.BLACK);
		g.setFont(font1);
		
		String[] msg = messages.get(currentSlide).split("\n");
		int temp = currentIndex;
		
		for(int i = 0; i < msg.length; i++){
			if(temp >= msg[i].length()){
				temp -= msg[i].length();
			}else{
				msg[i] = msg[i].substring(0, temp);
				temp = 0;
			}
		}
		
		if(msg.length > 0) g.drawString(msg[0], textX+x, 18+y);//187
		if(msg.length > 1) g.drawString(msg[1], textX+x, 33+y);//202
		if(msg.length > 2) g.drawString(msg[2], textX+x, 48+y);//217
	}
	
	public void update(){
		if(firstTime){ 
			setSide(profiles.get(profileIndex.get(currentSlide)).getSide());
			firstTime = false;
		}
		
		if(!done){
			counter++;
			if(counter >= delay){
				counter = 0;
				currentIndex++;
				if(currentIndex > getLength(messages.get(currentSlide))){
					currentIndex--;
					currentSlideDone = true;
				}else{
					currentSlideDone = false;
				}
			}
			
			if(enter){
				if(!currentSlideDone){
					currentSlideDone = true;
					currentIndex = getLength(messages.get(currentSlide));
				}else{
					currentSlide++;
					if(currentSlide >= messages.size()){
						done = true;
					}else{
						setSide(profiles.get(profileIndex.get(currentSlide)).getSide());
						currentIndex = 0;
						//currentSlideDone = false;
					}
				}
			}
		}
		
		
	}
	
}
