package main;

import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Main implements ActionListener{

	public static void main(String[] args) {
		new Main().run();
		
	}
	
	final PopupMenu popup = new PopupMenu();
    final SystemTray tray = SystemTray.getSystemTray();
    final TrayIcon trayIcon = new TrayIcon(new ImageIcon(getClass().getResource("icon1.png")).getImage().getScaledInstance((int)tray.getTrayIconSize().getWidth(), -1, Image.SCALE_SMOOTH));
   
    MenuItem aboutItem = new MenuItem("About");
    MenuItem exitItem = new MenuItem("Exit");
    
    int updateRate = 5000;
    boolean running = true;
    String alertTime = "13:37";
    
	
	public void init(){
		if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
        
        exitItem.addActionListener(this);
        aboutItem.addActionListener(this);
       
        popup.add(aboutItem);
        popup.add(exitItem);
       
        trayIcon.setPopupMenu(popup);
       
        try {
            tray.add(trayIcon);
        } catch (Exception e) {
            System.out.println("TrayIcon could not be added.");
        }
        
        trayIcon.setToolTip("Eriks 13:37 Alerter");
        
	}
	
	public void loop(){
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		boolean alertedOnce = false;
		
		while(running){
			Calendar cal = Calendar.getInstance();
			//System.out.println(cal.getTime());
			String time = sdf.format(cal.getTime());
			//String time = "19:20";
			if(!time.equals(alertTime)){
				alertedOnce = false;
			}else if(time.equals(alertTime) && !alertedOnce){
				showAlertWindow();
				trayIcon.displayMessage( "ERMERGERD!", "Klockan är "+alertTime+"!", TrayIcon.MessageType.WARNING);
				alertedOnce = true;
			}
			
			try{
				Thread.sleep(updateRate);
			}catch(Exception e){}
		}
	}
	
	public void showAlertWindow(){
		AlertWindow aw = new AlertWindow();
		aw.setMessage("Klockan är "+alertTime+"!");
		aw.start();
		//JOptionPane.showMessageDialog(null, "Klockan är "+alertTime+"!");
	}
	
	public void run(){
        init();
        loop();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		MenuItem source = (MenuItem) arg0.getSource();
		
		if(source == exitItem){
			System.exit(0);
		}else if(source == aboutItem){
			JOptionPane.showMessageDialog(null, "Erik Rimskog\n\nDet här är ett program som notifierar användaren när klockan blir 13:37\n\nAutostart-mappen:\nC:/ProgramData/Microsoft/Windows/Start Menu/Programs/Startup");
		}
		
	}

}
