package game.handlers;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

// this class contains a boolean array of current and previous key states
// for the 10 keys that are used for this game.
// a key k is down when keyState[k] is true.

public class Keys {
	
	public static final int NUM_KEYS = 10;
	
	public static boolean keyState[] = new boolean[NUM_KEYS];
	public static boolean prevKeyState[] = new boolean[NUM_KEYS];
	
	public static int UP = 0;
	public static int LEFT = 1;
	public static int DOWN = 2;
	public static int RIGHT = 3;
	public static int BUTTON1 = 4; //jump
	public static int BUTTON3 = 5; //attack
	public static int ENTER = 6;
	public static int ESCAPE = 7;
	public static int DELETE = 8;
	public static int RESET = 9;
	
	
	private static File keysFile;
	
	//binds[UP] -> VK_UP
	private static int[] binds = {KeyEvent.VK_UP, KeyEvent.VK_LEFT, KeyEvent.VK_DOWN, 
			KeyEvent.VK_RIGHT, KeyEvent.VK_SPACE, KeyEvent.VK_X,
			KeyEvent.VK_ENTER, KeyEvent.VK_ESCAPE, KeyEvent.VK_DELETE, KeyEvent.VK_R};
	
	//ifall en knapp TRYCKS och det inte är någon som finns i binds så sparas den här. för att rebinda keys i den menyn
	private static int otherButton;
	
	public static void init(){
		try {
			File f = new File(Saves.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
			f = f.getParentFile();
			keysFile = new File(f, "/keybindings.txt");
			if(!keysFile.exists()){
				keysFile.createNewFile();
				writeFile();
			}else{
				loadFile();
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void keySet(int i, boolean b) {
		/*
		if(i == KeyEvent.VK_UP) keyState[UP] = b;
		else if(i == KeyEvent.VK_LEFT) keyState[LEFT] = b;
		else if(i == KeyEvent.VK_DOWN) keyState[DOWN] = b;
		else if(i == KeyEvent.VK_RIGHT) keyState[RIGHT] = b;
		else if(i == KeyEvent.VK_SPACE) keyState[BUTTON1] = b;
		else if(i == KeyEvent.VK_SHIFT) keyState[BUTTON2] = b;
		else if(i == KeyEvent.VK_2) keyState[BUTTON3] = b;
		else if(i == KeyEvent.VK_3) keyState[BUTTON4] = b;
		else if(i == KeyEvent.VK_ENTER) keyState[ENTER] = b;
		else if(i == KeyEvent.VK_ESCAPE) keyState[ESCAPE] = b;
		else if(i == KeyEvent.VK_DELETE) keyState[DELETE] = b;
		*/
		boolean e = false;
		for(int a = 0; a < NUM_KEYS; a++){
			if(i == binds[a]){
				keyState[a] = b;
				e = true;
				break;
			}
		}
		
		if(!e && b){
			otherButton = i;
		}else if(e){
			otherButton = -1;
		}
	}
	
	public static void update() {
		for(int i = 0; i < NUM_KEYS; i++) {
			prevKeyState[i] = keyState[i];
		}
	}
	
	public static boolean isPressed(int i) {
		return keyState[i] && !prevKeyState[i];
	}
	
	public static boolean anyKeyPress() {
		for(int i = 0; i < NUM_KEYS; i++) {
			if(keyState[i]) return true;
		}
		return false;
	}
	
	public static boolean anyKeyPressed() {
		for(int i = 0; i < NUM_KEYS; i++) {
			if(isPressed(i)) return true;
		}
		return false;
	}
	
	public static int[] getBinds(){
		return binds.clone();
	}
	
	/**
	 * returnar den senaste okända knappen (som inte finns i binds) som tryckets (alltså inte released)
	 * @return
	 */
	public static int getOtherButton(){
		return otherButton;
	}
	
	public static String getBindName(int key){
		return KeyEvent.getKeyText(binds[key]);
	}
	
	public static void changeBind(int key, int keycode){
		binds[key] = keycode;
	}
	
	public static void writeFile(){
		HashMap<String, String> data = new HashMap<String, String>();
		//index -> keyid
		for(int i = 0; i < NUM_KEYS; i++){
			data.put(Integer.toString(i), Integer.toString(binds[i]));
		}
		
		try {
			FileHandler.writeFile(keysFile, data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void loadFile(){
		HashMap<String, String> data = new HashMap<String, String>();
		try {
			FileHandler.loadFile(keysFile, data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i < NUM_KEYS; i++){
			binds[i] = Integer.parseInt(data.get(Integer.toString(i)));
		}
	}
	
}
