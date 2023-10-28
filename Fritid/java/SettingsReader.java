package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//någonting jag gjorde för längesedan
public class SettingsReader {
	private HashMap<String, String> settings = new HashMap<String, String>();
	private ArrayList<String> comments = new ArrayList<>();
	//port, ip, hz, side, areaSize, baloons, 
	/*
	    //settings for Master
	    
		port: 5687
		side: left
		sideZoneWidth: 5
		screenWidth: 1920
		screenHeight: 1080
	 */
	private File daFile;
	private InputStream daStream;
	
	public SettingsReader(File f){
		daFile = f;
		load();
	}
	
	public SettingsReader(InputStream f){
		daStream = f;
		loadFromStream();
	}
	
	public void load(){
		BufferedReader reader;
		String in;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(daFile)));
			while((in = reader.readLine()) != null){
				if(!in.startsWith("//")){
					int spaceInd = in.indexOf(' ');
					String key = in.substring(0, spaceInd-1);
					String value = in.substring(spaceInd+1);
					settings.put(key, value);
				}else{
					comments.add(in);
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void loadFromStream(){
		BufferedReader reader;
		String in;
		try {
			reader = new BufferedReader(new InputStreamReader(daStream));
			while((in = reader.readLine()) != null){
				if(!in.startsWith("//")){
					int spaceInd = in.indexOf(' ');
					String key = in.substring(0, spaceInd-1);
					String value = in.substring(spaceInd+1);
					settings.put(key, value);
				}else{
					comments.add(in);
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void save(){
		try {
			PrintStream ps = new PrintStream(daFile);
			for(int i = 0; i < comments.size(); i++){
				ps.println(comments.get(i));
			}
			//ps.println();
			List<String> keys = new ArrayList<>(settings.keySet());
			for(int i = 0; i < keys.size(); i++){
				ps.println(keys.get(i)+": "+settings.get(keys.get(i)));
			}
			ps.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public void changeFile(File f){
		daFile = f;
	}
	
	public String get(String key){
		return settings.get(key);
	}
	
	public void set(String key, String value){//add
		if(settings.containsKey(key)){
			remove(key);
		}
		settings.put(key, value);
	}
	
	public void remove(String key){
		settings.remove(key);
	}
	
}
