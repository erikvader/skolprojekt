package main.menu;

import java.io.File;
import java.util.ArrayList;

import main.SettingsReader;

public class SkinsScreen extends MenuScreen{

	private static final long serialVersionUID = 1L;
	
	private ArrayList<String> names = new ArrayList<>();

	public SkinsScreen(String title){
		super(title);
		try{
			File root = new File("snake/skins");
			File[] children = root.listFiles();
			for(File f : children){
				if(f.isDirectory()){
					names.add(f.getName());
					addOption(f.getName(), skinsAction);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void setSkin(int index){
		try{
			SettingsReader sr = new SettingsReader(new File("snake/settings/settings.txt"));
			sr.set("skin", names.get(index));
			sr.save();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	MenyuAction skinsAction = new MenyuAction(){
		@Override
		public void action() {
			setSkin(cur);
			options.get(options.size()-1).click();
		}
	};

}
