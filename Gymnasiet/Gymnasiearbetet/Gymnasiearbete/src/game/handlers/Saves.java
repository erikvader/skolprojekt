package game.handlers;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

public class Saves {
	
	public Saves(){
		
	}
	
	private static File savesFolder;
	private static File saveFile;
	
	public static final int SAVE1 = 0;
	public static final int SAVE2 = 1;
	public static final int SAVE3 = 2;
	
	private static HashMap<String, String> data;
	
	public static void init(){
		try {
			File f = new File(Saves.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
			f = f.getParentFile();
			savesFolder = new File(f, "/saves");
			if(!savesFolder.exists()){
				savesFolder.mkdir();
			}
			data = new HashMap<String, String>();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}	
		
	}
	
	private static File getSaveFile(int profile){
		return new File(savesFolder, "/save"+(profile+1)+".txt");
	}
	
	public static void load(int profile) throws IOException{
		saveFile = getSaveFile(profile);
		
		data.clear();
		
		if(!saveFile.exists()){
			saveFile.createNewFile();
			initEmpty();
			FileHandler.writeFile(saveFile, data);
		}
		
		FileHandler.loadFile(saveFile, data);
	}
	
	private static void initEmpty(){
		setStageProgress("1_1", true);
		setStageScore("1_1", 0);
		setHubSpawn(-1);
		setData("deaths", "0");
		setData("experienced_intro", "false");
	}
	
	public static boolean exists(int profile){
		return getSaveFile(profile).exists();
	}
	
	public static void removeSave(int profile){
		if(exists(profile)){
			getSaveFile(profile).delete();
		}
	}
	
	public static void setData(String key, String value){
		data.put(key, value);
	}
	
	public static String getData(String key){
		return data.get(key);
	}
	
	public static boolean isGodmode(){
		String d = getData("godmode");
		if(d != null && d.equals("true"))
			return true;
		return false;
	}
	
	public static boolean hasExperiencesIntro(){
		String d = getData("experienced_intro");
		if(d != null && d.equals("true"))
			return true;
		else
			return false;
	} 
	
	public static void addOneToDeath(){
		int old = Integer.parseInt(getData("deaths"));
		setData("deaths", Integer.toString(old+1));
	}
	
	public static int getDeaths(){
		String d = getData("deaths");
		if(d != null)
			return Integer.parseInt(d);
		else
			return -1;
	}
	
	public static void addOneToAlsenholt2(){
		String d = getData("alsenholt2_deaths");
		if(d == null){
			setData("alsenholt2_deaths", "1");
		}else{
			int old = Integer.parseInt(d);
			setData("alsenholt2_deaths", Integer.toString(old+1));
		}
	}
	
	public static int getAlsenholt2(){
		String d = getData("alsenholt2_deaths");
		if(d == null){
			return 0;
		}else{
			return Integer.parseInt(d);
		}
	}
	
	public static void setHubSpawn(int doorIndex){
		Saves.setData("hubDoorSpawnIndex", Integer.toString(doorIndex));
	}
	
	public static int getHubSpawn(){ 
		String d = getData("hubDoorSpawnIndex");
		if(d == null) return -1;
		else return Integer.parseInt(d);
	}
	
	public static void setStageProgress(String stageid, boolean b){
		setData("stage_unlock_"+stageid, Boolean.toString(b));
	}
	
	public static boolean getStageProgress(String stageid){ //if available or not
		String d = getData("stage_unlock_"+stageid);
		if(d == null) return false;
		else return Boolean.parseBoolean(d);
	}
	
	public static void setStageScoreIfHigher(String stageid, int s){
		int cur = getStageScore(stageid);
		if(s > cur){
			setStageScore(stageid, s);
		}
	}
	
	public static void setStageScore(String stageid, int s){
		setData("stage_score_"+stageid, Integer.toString(s));
	}
	
	public static int getStageScore(String stageid){
		String d = getData("stage_score_"+stageid);
		if(d == null) return -1;
		else return Integer.parseInt(d);
	}
	
	/**
	 * sparar
	 * 
	 * @throws IOException
	 */
	public static void writeFile() throws IOException{
		FileHandler.writeFile(saveFile, data);
	}
	
	
}
