package main.menu;

import java.awt.CardLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import main.Game;
import net.lingala.zip4j.core.ZipFile;

public class Menyu implements KeyListener{

	public static void main(String[] args) {
		//new Game().run();
		checkSnakeFolder();
		new Menyu().run();
	}
	
	JFrame f;
	MenuScreen curScreen;
	HashMap<String, MenuScreen> screens = new HashMap<String, MenuScreen>();
	CardLayout cl = new CardLayout();
		
	public void run(){
		//curScreen = screens.get(0);
		
		
		f = new JFrame();
		f.addKeyListener(this);
		
		f.setLayout(cl);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(500, 500);
		f.setResizable(false);
		try{
			f.setIconImage(ImageIO.read(getClass().getResourceAsStream("/main/snake-icon.png")));
		}catch (IOException e){
			e.printStackTrace();
		}
		//f.add(curScreen, BorderLayout.CENTER);
		initScreens();
		
		f.setVisible(true);
		f.setLocationRelativeTo(null);
		
	}
	
	private void initScreens(){
		//startskärm
		MenuScreen temp = new MenuScreen("Eriks fina snake!");
		temp.addOption("Start", new MenyuAction(){
			@Override
			public void action() {
				f.dispose();
				new Game().run();
			}
		});
		temp.addOption("Highscore", new MenyuAction(){
			@Override
			public void action() {
				changeScreen("high");
			}
		});
		temp.addOption("Settings", new MenyuAction(){
			@Override
			public void action() {
				changeScreen("opts");
			}
		});
		temp.addOption("Skins", new MenyuAction(){
			@Override
			public void action() {
				changeScreen("skins");
			}
		});
		temp.addOption("Keybindings", new MenyuAction(){
			@Override
			public void action() {
				changeScreen("keys");
			}
		});
		temp.addOption("Exit", new MenyuAction(){
			@Override
			public void action() {
				System.exit(0);
			}
		});
		addThing(temp, "home");
		
		//highschore
		temp = new HighscoreScreen("Highscore");
		temp.addOption("Back", new MenyuAction(){
			@Override
			public void action() {
				changeScreen("home");
			}	
		});
		addThing(temp, "high");
		
		//settings
		temp = new SettingsScreen("Settings");
		temp.addOption("Back", new MenyuAction(){
			@Override
			public void action() {
				changeScreen("home");
			}
		});
		addThing(temp, "opts");
		
		//keybindings
		temp = new KeybindingScreen("Keybindings");
		temp.addOption("Back", new MenyuAction(){
			@Override
			public void action() {
				changeScreen("home");
			}
		});
		addThing(temp, "keys");
		
		//skins
		temp = new SkinsScreen("Skins");
		temp.addOption("Back", new MenyuAction(){
			@Override
			public void action() {
				changeScreen("home");
			}
		});
		addThing(temp, "skins");
		
		changeScreen("home");
		
	}
	
	private void addThing(MenuScreen ms, String name){
		f.add(ms, name);
		screens.put(name, ms);
	}
	
	public void changeScreen(String name){
		cl.show(f.getContentPane(), name);
		curScreen = screens.get(name);
		curScreen.changedTo();
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		curScreen.processPress(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
	private static void checkSnakeFolder(){
		try{
			File f = new File("snake");
			if(!f.exists()){
				exportResource("/snake.zip", "", "/snake.zip");
				ZipFile zf = new ZipFile(new File("snake.zip"));
				zf.extractAll("snake");
				new File("snake.zip").delete();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static String exportResource(String resourceName, String folder, String target) throws Exception {
        InputStream stream = null;
        OutputStream resStreamOut = null;
        String jarFolder;
        try {
            stream = Menyu.class.getResourceAsStream(resourceName);//note that each / is a directory down in the "jar tree" been the jar the root of the tree
            if(stream == null) {
                throw new Exception("Cannot get resource \"" + resourceName + "\" from Jar file.");
            }

            int readBytes;
            byte[] buffer = new byte[4096];
            jarFolder = new File(Menyu.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath().replace('\\', '/');
            //System.out.println(jarFolder + folder + resourceName);
            resStreamOut = new FileOutputStream(jarFolder + folder + target);
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            stream.close();
            resStreamOut.close();
        }

        return jarFolder + resourceName;
    }

	
}
