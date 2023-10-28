package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

//TODO set background (för att testa //kanske bättre att anpassa background efter map istället)

public class Editor {

	//GUI
	private JFrame window;
	private JMenuItem itemSave;
	private JMenuItem itemOpen;
	private JMenuItem itemImportTileset;
	private JMenuItem itemExportMap;
	private JMenuItem itemExportJava;
	private JMenuItem itemClearMap;
	private MainPanel mainPanel;
	private TilesetPanel tilesetPanel;
	private JScrollPane jsp;
	
	//controls
	private JSlider sliderScale;
	private JButton btnGrowX;
	private JButton btnGrowY;
	private JButton btnShrinkX;
	private JButton btnShrinkY;
	private JButton btnShiftRight;
	private JButton btnShiftDown;
	private JButton btnShiftLeft;
	private JButton btnShiftUp;
	private JToolBar toolbar1;
	private JToolBar toolbarControls;
	
	private JButton btnToolNone;
	private JButton btnToolMove;
	private JButton btnToolSelectNew;
	private JButton btnToolSelectAdd;
	private JButton btnToolSelectSubtract;
	private JButton btnToolSelectMove;
	
	//info
	private JLabel lblMouseX;
	private JLabel lblMouseY;
	private JLabel lblTileX;
	private JLabel lblTileY;
	private JLabel lblWidth;
	private JLabel lblHeight;
	private JLabel lblScale;
	private JLabel lblTool;
	
	//tools
	
	
	//tileset
	private BufferedImage tilesetImage;
	private BufferedImage[][] tilesetImages;
	private int tilesetWidth, tilesetHeight, tileSize = 30, tilesetAcross, tilesetUpDown;
	
	//misc
	private File runningDir;
	private final JFileChooser fc = new JFileChooser();
	
	//editor
	private double scale = 1;
	private int mapAreaWidth = 5;
	private int mapAreaHeight = 3;
	private ArrayList<ArrayList<Point>> map; //x, y //col, row
	private double editorX = 0, editorY = 0;
	private Thread repaintThread;
	private int mouseX, mouseY;
	
	//tools
	public static final int TOOL_NONE = 0;
	public static final int TOOL_PLACE = 1;
	public static final int TOOL_MOVE = 2;
	public static final int TOOL_SELECTION_NEW = 3;
	public static final int TOOL_SELECTION_ADD = 4;
	public static final int TOOL_SELECTION_SUBTRACT = 5;
	public static final int TOOL_SELECTION_MOVE = 6;
	private String[] toolNames = {"none", "place", "move", "Selection new", "Selection add", "selection subtract", "selection move"};
	
	private int currentTool = TOOL_NONE;
	private int lastTool = TOOL_NONE;
	
	//move tool
	private boolean isScrollDown = false;
	private Point startPoint = new Point(-1, -1);
	private double orgX, orgY;
	
	//place tool
	private Point curPoint = new Point(0, 0);
	
	//Selection Tool
	private Rectangle selectedArea = new Rectangle(0, 0, 0, 0);
	private Point selectStart = new Point(-1, -1);
	private boolean selectDragging = false;
	private Point[][] moveArray;
	private Point mouseOffset = new Point(0, 0);
	
	
	public Editor(){
		//random
		try{
			runningDir = new File(Editor.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
			fc.setCurrentDirectory(runningDir);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		//window
		window = new JFrame("editor");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLayout(new BorderLayout());
		
		//menubar
		JMenuBar menuBar = new JMenuBar();
		//JMenuItem item;
		
		//file
		JMenu menu = new JMenu("File");
		menuBar.add(menu);
		itemSave = new JMenuItem("save");
		itemSave.addActionListener(menuListener);
		menu.add(itemSave);
		itemOpen = new JMenuItem("open");
		itemOpen.addActionListener(menuListener);
		menu.add(itemOpen);
		itemImportTileset = new JMenuItem("import tileset");
		itemImportTileset.addActionListener(menuListener);
		menu.add(itemImportTileset);
		itemExportMap = new JMenuItem("export map");
		itemExportMap.addActionListener(menuListener);
		menu.add(itemExportMap);
		itemExportJava = new JMenuItem("Export java");
		itemExportJava.addActionListener(menuListener);
		menu.add(itemExportJava);
		
		window.setJMenuBar(menuBar);
		
		JMenu editMenu = new JMenu("Edit");
		menuBar.add(editMenu);
		itemClearMap = new JMenuItem("Clear");
		itemClearMap.addActionListener(menuListener);
		editMenu.add(itemClearMap);
		
		//toolbars
		toolbar1 = new JToolBar("tileset");
		
		//tileset chooser
		tilesetPanel = new TilesetPanel();
		//tilesetPanel.setPreferredSize(new Dimension(400, 400));
		jsp = new JScrollPane(tilesetPanel);
		//jsp.setBounds(650, 0, 605, 140);
		jsp.setPreferredSize(new Dimension(605, 104));
		//jsp.setMaximumSize(new Dimension(605, 104));
		
		toolbar1.add(jsp);
		
		window.add(toolbar1, BorderLayout.PAGE_END);
		
		//toolbar2
		toolbarControls = new JToolBar("controls");
		JPanel toolsPanel = new JPanel(new GridLayout(2, 4));
		btnToolNone = new JButton("None");
		btnToolNone.addActionListener(controlsListener);
		toolsPanel.add(btnToolNone);
		btnToolMove = new JButton("Move");
		btnToolMove.addActionListener(controlsListener);
		toolsPanel.add(btnToolMove);
		btnToolSelectNew = new JButton("Select new");
		btnToolSelectNew.addActionListener(controlsListener);
		toolsPanel.add(btnToolSelectNew);
		btnToolSelectAdd = new JButton("(Select add)");
		btnToolSelectAdd.addActionListener(controlsListener);
		toolsPanel.add(btnToolSelectAdd);
		btnToolSelectSubtract = new JButton("(Select subtract)");
		btnToolSelectSubtract.addActionListener(controlsListener);
		toolsPanel.add(btnToolSelectSubtract);
		btnToolSelectMove = new JButton("Select move");
		btnToolSelectMove.addActionListener(controlsListener);
		toolsPanel.add(btnToolSelectMove);
		
		
		
		toolbarControls.add(toolsPanel);
		
		JPanel btnPanel = new JPanel(new GridLayout(2, 4));
		btnGrowX = new JButton("GrowX");
		btnGrowX.addActionListener(controlsListener);
		btnPanel.add(btnGrowX);
		btnGrowY = new JButton("GrowY");
		btnGrowY.addActionListener(controlsListener);
		btnPanel.add(btnGrowY);
		btnShiftUp = new JButton("Shift Up");
		btnShiftUp.addActionListener(controlsListener);
		btnPanel.add(btnShiftUp);
		btnShiftLeft = new JButton("Shift Left");
		btnShiftLeft.addActionListener(controlsListener);
		btnPanel.add(btnShiftLeft);
		btnShrinkX = new JButton("ShrinkX");
		btnShrinkX.addActionListener(controlsListener);
		btnPanel.add(btnShrinkX);
		btnShrinkY = new JButton("ShrinkY");
		btnShrinkY.addActionListener(controlsListener);
		btnPanel.add(btnShrinkY);
		btnShiftDown = new JButton("Shift Down");
		btnShiftDown.addActionListener(controlsListener);
		btnPanel.add(btnShiftDown);
		btnShiftRight = new JButton("Shift Right");
		btnShiftRight.addActionListener(controlsListener);
		btnPanel.add(btnShiftRight);
		
		
		toolbarControls.add(btnPanel);
		
		sliderScale = new JSlider(1, 600, 100);
		sliderScale.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent arg0) {
				scale = sliderScale.getValue()/100.0;
			}
			
		});
		toolbarControls.add(sliderScale);
		
		JPanel infoPane = new JPanel(new GridLayout(3, 2));
		lblMouseX = new JLabel("X: 0");
		infoPane.add(lblMouseX);
		lblTileX = new JLabel("TileX: 0");
		infoPane.add(lblTileX);
		lblWidth = new JLabel("W: 0");
		infoPane.add(lblWidth);
		lblMouseY = new JLabel("Y: 0");
		infoPane.add(lblMouseY);
		lblTileY = new JLabel("TileY: 0");
		infoPane.add(lblTileY);
		lblHeight = new JLabel("H: 0");
		infoPane.add(lblHeight);
		lblScale = new JLabel("Scale: 0");
		infoPane.add(lblScale);
		lblTool = new JLabel("Tool: None");
		infoPane.add(lblTool);
		
		toolbarControls.add(infoPane);
		
		window.add(toolbarControls, BorderLayout.PAGE_START);
		
		//main panel
		mainPanel = new MainPanel();
		window.add(mainPanel, BorderLayout.CENTER);
		
		window.setSize(1280, 720);
		window.setLocationRelativeTo(null);
		
		//editor
		clearMap();
		
		repaintThread = new Thread(new Runnable(){
			@Override
			public void run() {
				while(true){
					updateAll();
					try{
						Thread.sleep(1000/20);
					}catch(Exception e){
						
					}
				}
			}
			
		});
		
		repaintThread.setDaemon(true);
		repaintThread.start();
		
	}
	
	public void clearMap(){
		map = new ArrayList<>();
		//init map
		for(int col = 0; col < mapAreaWidth; col++){
			map.add(new ArrayList<Point>());
			for(int row = 0; row < mapAreaHeight; row++){
				map.get(col).add(new Point(0, 0));
			}
		}
		
	}
	
	public void run(){
		window.setVisible(true);
	}
	
	//editor
	private void moveTool(int state, MouseEvent me){ //0 = pressed, 1 = released, 2 = dragged
		if(state == 0){
			isScrollDown = true;
			if(startPoint.x == -1){
				startPoint.setLocation(me.getPoint());
				orgX = editorX;
				orgY = editorY;
			}
		}else if(state == 1){
			isScrollDown = false;
			startPoint.x = -1;
		}else if(state == 2){
			if(isScrollDown){
				editorX = orgX - startPoint.getX()+me.getX();
				editorY = orgY - startPoint.getY()+me.getY();
			}
		}
	}
	
	private void placeTool(MouseEvent me){
		int x = me.getX();
		int y = me.getY();
		if(!mouseInsideArea(x, y)){
			return;
		}
		
		int xtile = mouseXtoTileX(x);
		int ytile = mouseYtoTileY(y);
		
		Point p = map.get(xtile).get(ytile);
		if(me.getButton() == 1 || me.getButton() == 0){
			p.x = curPoint.x;
			p.y = curPoint.y;
		}else if(me.getButton() == 3){
			p.x = 0;
			p.y = 0;
		}
	}
	
	public void selectionToolNew(MouseEvent me, int state){ //0 = pressed, 1 = released, 2 = dragged
		int x = me.getX();
		int y = me.getY();
		if(!mouseInsideArea(x, y)){
			return;
		}
		
		if(state == 0){
			selectStart.x = mouseXtoTileX(x);
			selectStart.y = mouseYtoTileY(y);
			selectedArea.setBounds(0, 0, 0, 0);
			selectDragging = true;
		}else if(state == 1){
			selectDragging = false;
		}else if(state == 2){
			if(selectDragging){
				Point p2 = new Point(mouseXtoTileX(x), mouseYtoTileY(y));
				int w = p2.x-selectStart.x;
				int h = p2.y-selectStart.y;
				
				if(w < 0) selectedArea.x = w; else selectedArea.x = 0;
				if(h < 0) selectedArea.y = h; else selectedArea.y = 0;
				
				selectedArea.x += selectStart.x;
				selectedArea.y += selectStart.y;
				selectedArea.width = Math.abs(w)+1;
				selectedArea.height = Math.abs(h)+1;
				
			}
		}
	}
	
	public void selectionToolMove(MouseEvent me, int state){ //0 = pressed, 1 = released, 2 = dragged
		int x = me.getX();
		int y = me.getY();
		/*if(!mouseInsideArea(x, y)){
			return;
		}
		*/
		if(state == 0){
			mouseOffset.setLocation(mouseXtoTileX(x)-selectedArea.x, mouseYtoTileY(y)-selectedArea.y);
			if(selectedArea.width != 0 && selectedArea.height != 0){
				moveArray = new Point[selectedArea.width][selectedArea.height];
				for(int i = selectedArea.x; i < selectedArea.x+selectedArea.width; i++){
					for(int j = selectedArea.y; j < selectedArea.y+selectedArea.height; j++){
						if(i < mapAreaWidth && i >= 0 && j >= 0 && j < mapAreaHeight){
							moveArray[i-selectedArea.x][j-selectedArea.y] = map.get(i).get(j);
							map.get(i).set(j, new Point(0, 0));
						}else{
							moveArray[i-selectedArea.x][j-selectedArea.y] = new Point(0, 0);
						}
					}
				}
			}
		}else if(state == 1){
			for(int i = selectedArea.x; i < selectedArea.x+selectedArea.width; i++){
				for(int j = selectedArea.y; j < selectedArea.y+selectedArea.height; j++){
					if(i < mapAreaWidth && i >= 0 && j >= 0 && j < mapAreaHeight){
						map.get(i).set(j, moveArray[i-selectedArea.x][j-selectedArea.y]);
					}
				}
			}
			moveArray = null;
		}else if(state == 2){
			selectedArea.setLocation(mouseXtoTileX(x)-mouseOffset.x, mouseYtoTileY(y)-mouseOffset.y);
		}
		
	}
	
	public int mouseXtoTileX(int x){
		return (int)((x-editorX)/(tileSize*scale));
	}
	
	public int mouseYtoTileY(int y){
		return (int)((y-editorY)/(tileSize*scale));
	}
	
	public boolean mouseInsideArea(int x, int y){
		if(x < editorX || x >= editorX + mapAreaWidth*tileSize*scale
			|| y < editorY || y >= editorY + mapAreaHeight*tileSize*scale){
			
			return false;
		}
		
		return true;
	}
	
	public class MainPanel extends JPanel implements MouseMotionListener, MouseListener{

		private static final long serialVersionUID = 1L;
		
		
		public MainPanel(){
			setBackground(Color.BLACK);
			addMouseListener(this);
			addMouseMotionListener(this);
			
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			//grid
			g.setColor(Color.GRAY);
			for(int x = 0; x < map.size(); x++){
				for(int y = 0; y < map.get(0).size(); y++){
					g.drawRect((int)(editorX+x*tileSize*scale), (int)(editorY+y*tileSize*scale), (int)(tileSize*scale), (int)(tileSize*scale));
				}
			}
			
			//tiles
			if(tilesetImage != null){
				for(int x = 0; x < map.size(); x++){
					for(int y = 0; y < map.get(0).size(); y++){
						Point p = map.get(x).get(y);
						g.drawImage(tilesetImages[p.x][p.y], (int)(editorX+x*tileSize*scale), (int)(editorY+y*tileSize*scale), (int)(tileSize*scale), (int)(tileSize*scale), null);
					}
				}
			}
			
			//red outline
			g.setColor(Color.RED);
			g.drawRect((int)(editorX-1), (int)(editorY-1), (int)(mapAreaWidth*tileSize*scale+2), (int)(mapAreaHeight*tileSize*scale+2));
			
			//moveArray
			if(moveArray != null){
				for(int x = 0; x < moveArray.length; x++){
					for(int y = 0; y < moveArray[0].length; y++){
						Point p = moveArray[x][y];
						g.drawImage(tilesetImages[p.x][p.y], (int)(editorX+(selectedArea.x+x)*tileSize*scale), (int)(editorY+(selectedArea.y+y)*tileSize*scale), (int)(tileSize*scale), (int)(tileSize*scale), null);
					}
				}
			}
			
			//selected area blue
			if(currentTool == TOOL_SELECTION_ADD || currentTool == TOOL_SELECTION_NEW || currentTool == TOOL_SELECTION_SUBTRACT || currentTool == TOOL_SELECTION_MOVE){
				g.setColor(new Color(0,0,1,0.5f));
				g.fillRect((int)(editorX+selectedArea.x*tileSize*scale), (int)(editorY+selectedArea.y*tileSize*scale), (int)(selectedArea.width*tileSize*scale), (int)(selectedArea.height*tileSize*scale));
			}
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {}
		@Override
		public void mouseEntered(MouseEvent arg0) {}
		@Override
		public void mouseExited(MouseEvent arg0) {}

		@Override
		public void mousePressed(MouseEvent me) {
			if(me.getButton() == 2){
				lastTool = currentTool;
				currentTool = TOOL_MOVE;
			}
			
			if(currentTool == TOOL_MOVE){
				moveTool(0, me);
			}else if(currentTool == TOOL_PLACE){
				placeTool(me);
			}else if(currentTool == TOOL_SELECTION_NEW){
				selectionToolNew(me, 0);
			}else if(currentTool == TOOL_SELECTION_MOVE){
				selectionToolMove(me, 0);
			}
			
		}

		@Override
		public void mouseReleased(MouseEvent me) {
			if(currentTool == TOOL_MOVE){
				moveTool(1, me);
			}else if(currentTool == TOOL_SELECTION_NEW){
				selectionToolNew(me, 1);
			}else if(currentTool == TOOL_SELECTION_MOVE){
				selectionToolMove(me, 1);
			}
			
			if(me.getButton() == 2){
				currentTool = lastTool;
			}
		}

		@Override
		public void mouseDragged(MouseEvent me) {
			if(currentTool == TOOL_MOVE){
				moveTool(2, me);
			}else if(currentTool == TOOL_PLACE){
				placeTool(me);
			}else if(currentTool == TOOL_SELECTION_NEW){
				selectionToolNew(me, 2);
			}else if(currentTool == TOOL_SELECTION_MOVE){
				selectionToolMove(me, 2);
			}
		}

		@Override
		public void mouseMoved(MouseEvent me) {
			int xtemp = me.getX();
			int ytemp = me.getY(); 
			
			mouseX = xtemp;
			mouseY = ytemp;
		}
	}
	
	/*
	public int toNumber(int x, int y){
		return y*tilesetWidth + x;
	}
	
	public int toX(int number){
		return number%tilesetWidth;
	}
	
	public int toY(int number){
		return number/tilesetWidth;
	}
	*/
	
	//tileset
	public class TilesetPanel extends JPanel implements MouseListener{

		private static final long serialVersionUID = 1L;
		
		public TilesetPanel(){
			addMouseListener(this);
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			if(tilesetImage == null) return;
			
			g.drawImage(tilesetImage, 0, 0, null);
			
			g.setColor(Color.RED);
			g.drawRect(curPoint.x*tileSize, curPoint.y*tileSize, tileSize, tileSize);
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {}
		@Override
		public void mouseEntered(MouseEvent arg0) {}
		@Override
		public void mouseExited(MouseEvent arg0) {}
		@Override
		public void mousePressed(MouseEvent me) {
			int col = me.getX()/tileSize;
			int row = me.getY()/tileSize;
			if(col < tilesetAcross && row < tilesetUpDown){
				curPoint.x = col;
				curPoint.y = row;
				
				currentTool = TOOL_PLACE;
			}
		}

		@Override
		public void mouseReleased(MouseEvent me) {
			
		}
	}
	
	//menu
	public void updateAll(){
		mainPanel.repaint();
		tilesetPanel.repaint();
		
		//info
		String scaleString = Double.toString(scale);
		while(scaleString.length() < 4) scaleString += "0";
		
		setLabel(lblScale, scaleString);
		setLabel(lblWidth, Integer.toString(mapAreaWidth));
		setLabel(lblHeight, Integer.toString(mapAreaHeight));
		setLabel(lblMouseX, Integer.toString(mouseX));
		setLabel(lblMouseY, Integer.toString(mouseY));
		setLabel(lblTileX, Integer.toString((int)((mouseX-editorX)/(scale*tileSize))));
		setLabel(lblTileY, Integer.toString((int)((mouseY-editorY)/(scale*tileSize))));
		setLabel(lblTool, toolNames[currentTool]);
	}
	
	public void setLabel(JLabel lbl, String val){
		String old = lbl.getText();
		String grej = old.substring(0, old.indexOf(":")+1)+" ";
		lbl.setText(grej+val);
	}
	
	//menu listener
	ActionListener menuListener = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent ae) {
			JMenuItem src = (JMenuItem)ae.getSource();
			handleMenu(src);
		}
		
	};
	
	public void handleMenu(JMenuItem jmi){
		if(jmi == itemImportTileset){
			importTileset();
		}else if(jmi == itemSave){
			saveMap();
		}else if(jmi == itemOpen){
			openMap();
		}else if(jmi == itemClearMap){
			clearMap();
		}
	}
	
	public void openMap(){
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int val = fc.showOpenDialog(window);
		
		if(val == JFileChooser.APPROVE_OPTION){
			File f = fc.getSelectedFile();
			loadMap(f);
		}
	}
	
	public void loadMap(File f){ //mera som för save? att denna endast läser in map-delen.
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			
			mapAreaWidth = Integer.parseInt(br.readLine());
			mapAreaHeight = Integer.parseInt(br.readLine());
			
			clearMap();
			
			int theY = 0;
			String input = "";
			while((input = br.readLine()) != null){
				StringTokenizer tk = new StringTokenizer(input);
				for(int i = 0; i < mapAreaWidth; i++){
					int x = Integer.parseInt(tk.nextToken());
					int y = Integer.parseInt(tk.nextToken());
					map.get(i).get(theY).setLocation(x, y);
				}
				theY++;
			}
			
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void saveMap(){
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int val = fc.showSaveDialog(window);
		
		if(val == JFileChooser.APPROVE_OPTION){
			File f = fc.getSelectedFile();
			try {
				PrintWriter pw = new PrintWriter(f);
				writeToSaveFile(pw);
				pw.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void writeToSaveFile(PrintWriter pw){
		pw.println(mapAreaWidth);
		pw.println(mapAreaHeight);
		
		for(int y = 0; y < mapAreaHeight; y++){
			for(int x = 0; x < mapAreaWidth; x++){
				Point p = map.get(x).get(y);
				pw.print(p.x+" "+p.y+" ");
			}
			pw.println();
		}
	}
	
	public void importTileset(){
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int val = fc.showOpenDialog(window);
		
		if(val == JFileChooser.APPROVE_OPTION){ //laddar in tileset
			File dir = fc.getSelectedFile();
			File tileset = new File(dir, "tileset.png");
			//System.out.println(tileset);
			try{
				tilesetImage = ImageIO.read(tileset);
				tilesetWidth = tilesetImage.getWidth();
				tilesetHeight = tilesetImage.getHeight();
				tilesetAcross = tilesetWidth/tileSize;
				tilesetUpDown = tilesetHeight/tileSize;
				
				tilesetImages = new BufferedImage[tilesetAcross][tilesetUpDown];
				for(int x = 0; x < tilesetAcross; x++){
					for(int y = 0; y < tilesetUpDown; y++){
						tilesetImages[x][y] = tilesetImage.getSubimage(x*tileSize, y*tileSize, tileSize, tileSize);
					}
				}
				
				tilesetPanel.setPreferredSize(new Dimension(tilesetWidth, tilesetHeight));
				tilesetPanel.revalidate();
				jsp.revalidate();
				toolbar1.revalidate();
				tilesetPanel.repaint();
				
			}catch(IOException e){
				System.out.println("Tileset image finns int");
				e.printStackTrace();
			}
		}
	}
	
	//controls listener
	ActionListener controlsListener = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent ae) {
			JButton src = (JButton)ae.getSource();
			handleControls(src);
		}
		
	};
	
	public void handleControls(JButton jmi){
		if(jmi == btnToolNone){
			currentTool = TOOL_NONE;
		}else if(jmi == btnToolMove){
			currentTool = TOOL_MOVE;
		}else if(jmi == btnGrowX){
			growX();
		}else if(jmi == btnGrowY){
			growY();
		}else if(jmi == btnShrinkX){
			shrinkX();
		}else if(jmi == btnShrinkY){
			shrinkY();
		}else if(jmi == btnShiftRight){
			shiftRight();
		}else if(jmi == btnShiftLeft){
			shiftLeft();
		}else if(jmi == btnShiftUp){
			shiftUp();
		}else if(jmi == btnShiftDown){
			shiftDown();
		}else if(jmi == btnToolSelectNew){
			currentTool = TOOL_SELECTION_NEW;
		}else if(jmi == btnToolSelectAdd){
			currentTool = TOOL_SELECTION_ADD;
		}else if(jmi == btnToolSelectSubtract){
			currentTool = TOOL_SELECTION_SUBTRACT;
		}else if(jmi == btnToolSelectMove){
			currentTool = TOOL_SELECTION_MOVE;
		}
	}
	
	public void shiftRight(){
		ArrayList<Point> empty = new ArrayList<Point>();
		for(int i = 0; i < mapAreaHeight; i++) empty.add(new Point(0, 0));
		map.add(0, empty);
		map.remove(map.size()-1);
	}
	
	public void shiftLeft(){
		ArrayList<Point> empty = new ArrayList<Point>();
		for(int i = 0; i < mapAreaHeight; i++) empty.add(new Point(0, 0));
		map.remove(0);
		map.add(empty);
	}
	
	public void shiftUp(){
		for(int x = 0; x < mapAreaWidth; x++){
			ArrayList<Point> temp = map.get(x);
			for(int y = 0; y < mapAreaHeight; y++){
				if(y == mapAreaHeight-1){
					temp.get(y).setLocation(0, 0);
				}else{
					temp.get(y).setLocation(temp.get(y+1));
				}
			}
		}
	}
	
	public void shiftDown(){
		for(int x = 0; x < mapAreaWidth; x++){
			ArrayList<Point> temp = map.get(x);
			for(int y = mapAreaHeight-1; y >= 0; y--){
				if(y == 0){
					temp.get(y).setLocation(0, 0);
				}else{
					temp.get(y).setLocation(temp.get(y-1));
				}
			}
		}
	}
	
	public void growX(){
		mapAreaWidth += 1;
		map.add(new ArrayList<Point>());
		for(int y = 0; y < mapAreaHeight; y++){
			map.get(mapAreaWidth-1).add(new Point(0, 0));
		}
	}
	
	public void shrinkX(){
		mapAreaWidth -= 1;
		map.remove(mapAreaWidth);
		
	}
	
	public void growY(){
		mapAreaHeight += 1;
		for(int x = 0; x < mapAreaWidth; x++){
			map.get(x).add(new Point(0, 0));
		}
	}
	
	public void shrinkY(){
		mapAreaHeight -= 1;
		for(int x = 0; x < mapAreaWidth; x++){
			map.get(x).remove(mapAreaHeight);
		}
	}

}
