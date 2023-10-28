package main;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Robot;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URISyntaxException;

import javax.swing.ImageIcon;

public class Slave {
	
	public static void main(String[] arg){
		new Slave().run();
	}
	
	private String ip = "192.168.1.71";
	private int port = 5687;
	private Socket connection; 
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	//private String side = "right";
	private int areaSize = 5;
	
	Robot rob;
	int sWidth = 1366;
	int sHeight = 768;
	int oldX = sWidth - areaSize-1;
	int oldY = sHeight/2;
	
	TrayIcon trayIcon = new TrayIcon(new ImageIcon(getClass().getResource("/main/tray.png")).getImage());
	
	public void run(){
		readSettings();
		setupUI();
		
		while(true){
			trayIcon.displayMessage(null, "Attempting to connect", MessageType.INFO);
			try{
				connection = new Socket(InetAddress.getByName(ip), port);
				trayIcon.displayMessage(null, "Connected to "+connection.getInetAddress().getHostName(), MessageType.INFO);
				out = new ObjectOutputStream(connection.getOutputStream());
				in = new ObjectInputStream(connection.getInputStream());
				out.flush();
				mechanism();
				
			}catch(EOFException eofe){
				trayIcon.displayMessage(null, "Connection lost!", MessageType.INFO);
			}catch(IOException ioe){
				trayIcon.displayMessage(null, "Connection lost!", MessageType.INFO);
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				closeStuff();
			}
			
		}
		
	}
	
	private void mechanism() throws IOException{
		try {
			rob = new Robot();
		} catch (AWTException e1) {
			e1.printStackTrace();
		}
		
		while(true){
			int[] input;
			try{
				input = (int[])in.readObject();
				simulateStuff(input);
				if(oldX >= sWidth-areaSize){
					out.writeObject("exit");
					out.flush();
					oldX = sWidth-areaSize-1;
					rob.mouseMove(oldX, oldY);
				}
			}catch(ClassNotFoundException e){
				e.printStackTrace();
			}
		}
	}
	
	private void simulateStuff(int[] i){
		if(i[0] == 0){
			//ping! gör ingenting
		}else if(i[0] == 1){
			//mouse movement
			oldX += i[1];
			oldY += i[2];
			rob.mouseMove(oldX, oldY);
		}else if(i[0] == 2){
			//mouse press
			rob.mousePress(InputEvent.getMaskForButton(i[1]));
		}else if(i[0] == 3){
			//mouse release
			rob.mouseRelease(InputEvent.getMaskForButton(i[1]));
		}else if(i[0] == 4){
			//mouse scroll
			rob.mouseWheel(i[1]);
		}
		else if(i[0] == 5){
			//key press
			rob.keyPress(i[1]);
		}else if(i[0] == 6){
			//key release
			rob.keyRelease(i[1]);
		}
	}
		
	private void closeStuff(){
		try {
			if(out != null)
				out.close();
			if(in != null)
				in.close();
			if(in != null)
				connection.close();
		} catch (Exception e) {
			//e.printStackTrace();
		}
		
	}
	
	private void readSettings(){
		try {
			File f = new File(getClass().getResource("/main/settings.txt").toURI());
			SettingsReader sr = new SettingsReader(f);
			
			ip = sr.get("ip");
			port = Integer.parseInt(sr.get("port"));
			//side = sr.get("side");
			areaSize = Integer.parseInt(sr.get("sideZoneWidth"));
			sWidth = Integer.parseInt(sr.get("screenWidth"));
			sHeight = Integer.parseInt(sr.get("screenHeight"));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
	}
	
	private void setupUI(){
		if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
        final PopupMenu popup = new PopupMenu();
        final SystemTray tray = SystemTray.getSystemTray();
       
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				closeStuff();
				System.exit(0);
			}
        	
        });
     
        popup.add(exitItem);
       
        trayIcon.setPopupMenu(popup);
        trayIcon.setToolTip("Eriks screen extender - Slave");
       
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
	}
	
}
