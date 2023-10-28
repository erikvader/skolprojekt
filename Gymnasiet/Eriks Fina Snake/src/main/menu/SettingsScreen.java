package main.menu;

import java.awt.event.KeyEvent;
import java.io.File;

import main.SettingsReader;

public class SettingsScreen extends MenuScreen{

	private static final long serialVersionUID = 1L;
	
	private SettingsReader sr;
	private String[] settingNames = {"", "timerStart", "timerEnd", "timerStep", "solidWalls", "width", "height"};
	private int[] changeValue = {0, 1, 1, 1, 0, 1, 1};
	public static String[][] preset = {{"200", "250", "5", "true", "20", "20"}, {"250", "350", "10", "false", "20", "20"}, {"250", "1000", "30", "false", "20", "20"}, {"400", "800", "20", "false", "100", "100"}, {"250", "300", "5", "false", "10", "10"}};
	public static String[] presetNames = {"snigel", "normal", "c^2", "Gigantisch!", "small"};
	private int curPresetIndex = 1;
	

	public SettingsScreen(String s){
		super(s);
		try{
			sr = new SettingsReader(new File("snake/settings/settings.txt"));
		}catch (Exception e){
			e.printStackTrace();
		}
		
		addOption("Speed preset: normal", null);
		addOption("timerStart: "+sr.get("timerStart"), null);
		addOption("timerEnd: "+sr.get("timerEnd"), null);
		addOption("timerStep: "+sr.get("timerStep"), null);
		addOption("solidWalls: "+sr.get("solidWalls"), new MenyuAction(){
			@Override
			public void action() {
				boolean b = Boolean.parseBoolean(sr.get("solidWalls"));
				b = !b;
				changeValue(4, Boolean.toString(b));
			}
		});
		addOption("width: "+sr.get("width"), null);
		addOption("height: "+sr.get("height"), null);
		
		addOption("Save", new MenyuAction(){
			@Override
			public void action() {
				sr.save();
			}
		});
	}
	
	@Override
	public void processPress(KeyEvent e) {
		super.processPress(e);
		int code = e.getKeyCode();
		if(cur >= 0 && cur <= 6){
			if(code == KeyEvent.VK_LEFT){
				if(cur == 0){
					if(curPresetIndex > 0)
						curPresetIndex--;
					else
						curPresetIndex = presetNames.length-1;
					
					changePreset(curPresetIndex);
				}else if(cur != 4){
					changeValue(cur, Integer.toString(Integer.parseInt(sr.get(settingNames[cur]))-changeValue[cur]));
				}
			}else if(code == KeyEvent.VK_RIGHT){
				if(cur == 0){
					if(curPresetIndex < presetNames.length-1)
						curPresetIndex++;
					else
						curPresetIndex = 0;
					
					changePreset(curPresetIndex);
				}else if(cur != 4){
					changeValue(cur, Integer.toString(Integer.parseInt(sr.get(settingNames[cur]))+changeValue[cur]));
				}
			}
		}
		
	}
	
	private void changeValue(int index, String value){
		sr.set(settingNames[index], value);
		options.get(index).setName(settingNames[index]+": "+sr.get(settingNames[index]));
	}
	
	private void changePreset(int index){
		options.get(0).setName("Speed preset: "+presetNames[curPresetIndex]);
		changeValue(1, preset[index][0]);
		changeValue(2, preset[index][1]);
		changeValue(3, preset[index][2]);
		changeValue(4, preset[index][3]);
		changeValue(5, preset[index][4]);
		changeValue(6, preset[index][5]);
		
	}
	
	@Override
	public void changedTo(){
		super.changedTo();
		sr.load();
	}
}
