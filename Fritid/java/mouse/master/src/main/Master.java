package main;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.MenuItem;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Robot;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;



public class Master implements MouseListener, MouseWheelListener, KeyListener, MouseMotionListener{

	public static void main(String[] args) {
		new Master().run();
	}
	
	//0=ping, 1=mouse, 2=mousePress, 3=mouseRelease, 4=mouseScroll, 5=keyPress, 6=keyRelease
	
	//private int hz = 60;
	//private Timer timer;
	private ServerSocket servSock;
	private Socket connection;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private int port = 5687;
	private String side = "left";
	private int areaSize = 5;
	Robot rob;
	boolean isOnOtherScreen = false;
	private int sHeight = 1080;
	private int sWidth = 1920;
	private JFrame window;
	
	private int mouseX = 0;
	private int mouseY = 0;
	//private int oldMouseX, oldMOuseY = 0;
	
	private Thread receiveThread;
	private boolean threadError = false;
	
	 final TrayIcon trayIcon = new TrayIcon(new ImageIcon(getClass().getResource("/main/tray.png")).getImage());
	
	public void run(){
		readSettings();
		setupUI();
		
		try {
			rob = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		
		try{
			servSock = new ServerSocket(port);
			while(true){
				try{
					//System.out.println("waiting for a connection");
					trayIcon.displayMessage(null, "Waiting for a connection", MessageType.INFO);
					connection = servSock.accept();
					//connection.setSoTimeout((1000/hz)/2);
					//System.out.println("Connected to "+connection.getInetAddress().getHostName());
					trayIcon.displayMessage(null, "connected to "+connection.getInetAddress().getHostName(), MessageType.INFO);
					out = new ObjectOutputStream(connection.getOutputStream());
				    out.flush();
				    in = new ObjectInputStream(connection.getInputStream());
				    startReader();
				    mechanism();
				}catch(EOFException eofe){
					trayIcon.displayMessage(null, "Connection ended!", MessageType.WARNING);
				}catch(IOException ioe){
					trayIcon.displayMessage(null, "Connection ended!", MessageType.WARNING);
				}catch(Exception ex){
					ex.printStackTrace();
				}finally{
					closeEverything();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	private void readSettings(){
		try {
			File f = new File(getClass().getResource("/main/settings.txt").toURI());
			SettingsReader sr = new SettingsReader(f);
			
			port = Integer.parseInt(sr.get("port"));
			side = sr.get("side");
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
				closeEverything();
				System.exit(0);
			}
        	
        });
     
        popup.add(exitItem);
       
        trayIcon.setPopupMenu(popup);
        trayIcon.setToolTip("Eriks screen extender - Master");
       
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
	}
	
	private void mechanism() throws IOException{
		while(true){
			if(!isOnOtherScreen){
				Point p = MouseInfo.getPointerInfo().getLocation();
				mouseX = p.x;
				mouseY = p.y;
				if(side.equals("left")){
					if(mouseX <= 0+areaSize && !isOnOtherScreen){
						startSendData();
					}
				}
				/* har redan en annan thread som läser in
				int[] ping = {0};
				synchronized(out){send(ping);}
				*/
			}
			if(threadError){
				throw new IOException("receiveThreaden kraschade");
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void startSendData(){
		window = new JFrame();
		window.setSize(250, 250);
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		window.setLocation((sWidth/2)-(250/2), sHeight/2-250/2);
		window.addMouseWheelListener(this);
		window.addMouseListener(this);
		window.addKeyListener(this);
		window.addMouseMotionListener(this);
		window.setVisible(true);
		
		class Frame extends JPanel{

			private static final long serialVersionUID = 1L;
			
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawLine(234/2, 0, 234/2, 212);
				g.drawLine(0, 212/2, 234, 212/2);
			}
			
		}
		
		Frame f = new Frame();
		
		window.add(f, BorderLayout.CENTER);
		
		isOnOtherScreen = true;
		rob.mouseMove(sWidth/2, sHeight/2);
		
	}
	
	public void startReader(){
		receiveThread = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					while(true){
						String answer = (String) in.readObject();
						if(answer.equals("exit")){ 
							resetWindow();
						}
					}
				} catch (Exception e) {
					//e.printStackTrace();
					threadError = true;
				}
			}
			
		});
		receiveThread.setDaemon(true);
		threadError = false;
		receiveThread.start();
		
	}
	
	private void resetWindow(){
		window.dispose();
		rob.mouseMove(areaSize+1, mouseY);
		isOnOtherScreen = false;
	}
	
	private void send(int[] a) throws IOException{
		out.writeObject(a);
		out.flush();
	}
	
	private void closeEverything(){
		try{
			if(out != null)
				out.close();
			if(in != null)
				in.close();
			if(connection != null)
				connection.close();
		}catch(Exception e){
			//e.printStackTrace();
		}
	}


	@Override
	public void keyPressed(KeyEvent ke) {
		if(isOnOtherScreen){
			int btn = ke.getKeyCode();
			int[] toSend = {5, btn};
			try {
				synchronized(out){//hoppas att jag använder 'sycnhronized' rätt
					send(toSend);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(ke.getKeyCode() == KeyEvent.VK_ESCAPE){
			resetWindow();
		}
	}

	@Override
	public void keyReleased(KeyEvent ke) {
		if(isOnOtherScreen){
			int btn = ke.getKeyCode();
			int[] toSend = {6, btn};
			try {
				synchronized(out){//hoppas att jag använder 'sycnhronized' rätt
					send(toSend);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent mwe) {
		if(isOnOtherScreen){
			int btn = mwe.getWheelRotation();
			int[] toSend = {4, btn};
			try {
				synchronized(out){//hoppas att jag använder 'sycnhronized' rätt
					send(toSend);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		
	}

	@Override
	public void mousePressed(MouseEvent me) {
		if(isOnOtherScreen){
			int btn = me.getButton();
			int[] toSend = {2, btn};
			try {
				synchronized(out){//hoppas att jag använder 'sycnhronized' rätt
					send(toSend);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		if(isOnOtherScreen){
			int btn = me.getButton();
			int[] toSend = {3, btn};
			try {
				synchronized(out){//hoppas att jag använder 'sycnhronized' rätt
					send(toSend);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent me) {
		mouseMoved(me);
	}

	@Override
	public void mouseMoved(MouseEvent me) {
		if(isOnOtherScreen){
			int dx = me.getX()-250/2;
			int dy = me.getY()-250/2;
			rob.mouseMove(sWidth/2, sHeight/2);
			int[] toSend = {1, dx, dy};
			try {
				synchronized(out){//hoppas att jag använder 'sycnhronized' rätt
					send(toSend);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}



}
