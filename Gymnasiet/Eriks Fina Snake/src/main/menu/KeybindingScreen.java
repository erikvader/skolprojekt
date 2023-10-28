package main.menu;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.File;

import main.SettingsReader;

public class KeybindingScreen extends MenuScreen{
	
	private static final long serialVersionUID = 1L;
	
	private SettingsReader sr;
	private int rebinding = -1;
	private String[] settingNames = {"up", "down", "left", "right", "exit", "pause", "restart"};

	public KeybindingScreen(String title){
		super(title);
		
		try{
			sr = new SettingsReader(new File("snake/settings/settings.txt"));
		}catch (Exception e){
			e.printStackTrace();
		}
		
		for(int i = 0; i < settingNames.length; i++){
			final int temp = i;
			addOption(settingNames[i]+": "+sr.get(settingNames[i]), new MenyuAction(){
				@Override
				public void action() {
					startRebindKey(temp);
				}
			});
		}
		
		
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
		
		if(rebinding != -1 && code != KeyEvent.VK_ENTER){
			rebindKey(e);
		}
	}

	private void startRebindKey(int index){
		rebinding = index;
		marked = Color.PINK;
		super.repaint();
	}
	
	private void rebindKey(KeyEvent e){
		changeValue(rebinding, Integer.toString(e.getKeyCode()));
		marked = Color.RED;
		rebinding = -1;
	}
	

	private void changeValue(int index, String value){
		sr.set(settingNames[index], value);
		options.get(index).setName(settingNames[index]+": "+sr.get(settingNames[index]));
	}
	
	@Override
	public void changedTo(){
		super.changedTo();
		sr.load();
	}
	

}
