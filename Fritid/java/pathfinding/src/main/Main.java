package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class Main {

	public static void main(String[] args) {
		new Main().run();
	}
	
	JFrame f;
	panel p;
	JPanel centerPanel;
	JButton btnRandom;
	JButton btnClear;
	JButton btnReset;
	JButton btnGo;
	JButton btnNext;
	JTextField labelTimer;
	JTextField labelWallpercent;
	JRadioButton radioBFS;
	JRadioButton radioDijkstra;
	JRadioButton radioAStar;
	JCheckBox chkbDiagonal;
	JButton btnLabyrinth;
	JTextField labelWidth;
	JTextField labelHeight;
	JButton btnSetSize;
	
	int width = 25;
	int height = 25;
	int size = 20;
	int spacing = 1;
	
	boolean running = false;
	boolean diagonal = true;
	
	Point targetPos;
	Point startPos;
	ArrayList<Point> path = new ArrayList<>();
	int[][] printState = new int[width][height]; //0=ingenting, 1=wall, 2=open, 3=closed
	
	Thread pathFindingThread;
	
	ErikInterface heuristicInterface;
	
	public void run(){
		f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//init innehåll
		p = new panel();
		Dimension pSize = new Dimension(width*(spacing+size), (spacing+size)*height);
		p.setPreferredSize(pSize);
		p.setMaximumSize(pSize);
		p.setMinimumSize(pSize);
		p.addMouseListener(mouseListener);
		p.addMouseMotionListener(mml);
		
		centerPanel = new JPanel(new GridBagLayout());
		centerPanel.add(p);
		f.add(centerPanel, BorderLayout.CENTER);
		
		JPanel sidoPanel = new JPanel();
		Dimension sSize = new Dimension(200, 0);
		sidoPanel.setPreferredSize(sSize);
		sidoPanel.setMaximumSize(sSize);
		sidoPanel.setMinimumSize(sSize);
		sidoPanel.setBackground(Color.BLUE);
		sidoPanel.setLayout(new BoxLayout(sidoPanel, BoxLayout.Y_AXIS));
		
		btnRandom = new JButton("Random");
		btnRandom.addActionListener(btnListener);
		btnRandom.setAlignmentX(Component.CENTER_ALIGNMENT);
		sidoPanel.add(btnRandom);
		labelWallpercent = new JTextField("10");
		labelWallpercent.setEditable(true);
		labelWallpercent.setMaximumSize(new Dimension(50, 25));
		sidoPanel.add(labelWallpercent);
		btnClear = new JButton("Clear");
		btnClear.addActionListener(btnListener);
		btnClear.setAlignmentX(Component.CENTER_ALIGNMENT);
		sidoPanel.add(btnClear);
		btnReset = new JButton("Reset");
		btnReset.addActionListener(btnListener);
		btnReset.setAlignmentX(Component.CENTER_ALIGNMENT);
		sidoPanel.add(btnReset);
		btnGo = new JButton("Go");
		btnGo.addActionListener(btnListener);
		btnGo.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnGo.setBackground(Color.GREEN);
		sidoPanel.add(btnGo);
		btnNext = new JButton("Next");
		btnNext.addActionListener(btnListener);
		btnNext.setAlignmentX(Component.CENTER_ALIGNMENT);
		sidoPanel.add(btnNext);
		labelTimer = new JTextField("100");
		labelTimer.setEditable(true);
		labelTimer.setMaximumSize(new Dimension(50, 25));
		sidoPanel.add(labelTimer);
		chkbDiagonal = new JCheckBox("Allow diagonal");
		chkbDiagonal.setSelected(true);
		sidoPanel.add(chkbDiagonal);
		chkbDiagonal.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent arg0) {
				diagonal = chkbDiagonal.isSelected();
			}
		});
		
		radioBFS = new JRadioButton("BFS");
		radioDijkstra = new JRadioButton("Dijkstra");
		radioAStar = new JRadioButton("A*");
		ButtonGroup bg = new ButtonGroup();
		bg.add(radioBFS);
		bg.add(radioDijkstra);
		bg.add(radioAStar);
		sidoPanel.add(radioAStar);
		sidoPanel.add(radioBFS);
		sidoPanel.add(radioDijkstra);
		radioAStar.setSelected(true);
		
		btnLabyrinth = new JButton("Labyrinth");
		btnLabyrinth.addActionListener(btnListener);
		btnLabyrinth.setAlignmentX(Component.CENTER_ALIGNMENT);
		sidoPanel.add(btnLabyrinth);
		
		labelWidth = new JTextField("25");
		labelWidth.setEditable(true);
		labelWidth.setMaximumSize(new Dimension(50, 25));
		sidoPanel.add(labelWidth);
		labelHeight = new JTextField("25");
		labelHeight.setEditable(true);
		labelHeight.setMaximumSize(new Dimension(50, 25));
		sidoPanel.add(labelHeight);
		btnSetSize = new JButton("Set Size");
		btnSetSize.addActionListener(btnListener);
		btnSetSize.setAlignmentX(Component.CENTER_ALIGNMENT);
		sidoPanel.add(btnSetSize);
		
		f.add(sidoPanel, BorderLayout.EAST);
		
		f.addComponentListener(resizeListener);
		f.pack();
		f.setLocationRelativeTo(null);
		f.setVisible(true);
		
		//heuristicInterface = manhattanHeuristic;
		heuristicInterface = denAndraHeuristic;
		putRandom();
		
	}
	
	class panel extends JPanel{
		
		private static final long serialVersionUID = 1L;

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			super.setBackground(Color.BLACK);
		
			//draw checker
			g.setColor(Color.WHITE);
			for(int x = 0; x < width; x++){
				for(int y = 0; y < height; y++){
					g.fillRect(x*(size+spacing), y*(size+spacing), size, size);
				}
			}
			
			for(int x = 0; x < width; x++){
				for(int y = 0; y < height; y++){
					Color c = Color.BLACK;
					switch(printState[x][y]){
					case 0:
						c = Color.WHITE;
						break;
					case 1:
						c = new Color(183, 122, 16);
						break;
					case 2:
						c = Color.GREEN;
						break;
					case 3:
						c = Color.RED;
						break;
					}
					g.setColor(c);
					g.fillRect(x*(size+spacing), y*(size+spacing), size, size);
				}
			}
						
			g.setColor(Color.ORANGE);
			for(Point n : path){
				g.fillRect(n.x*(size+spacing), n.y*(size+spacing), size, size);
			}
			
			g.setColor(Color.BLUE);
			g.fillRect(startPos.x*(size+spacing), startPos.y*(size+spacing), size, size);
			
			g.setColor(new Color(155, 0, 194));
			g.fillRect(targetPos.x*(size+spacing), targetPos.y*(size+spacing), size, size);
		}
	}
	
	public void clear(){
		reset();
		printState = new int[width][height];
		startPos = new Point(-1, -1);
		targetPos = new Point(-1, -1);
	}
	
	public void reset(){
		for(int x = 0; x < printState.length; x++){
			for(int y = 0; y < printState[0].length; y++){
				if(printState[x][y] != 1 && printState[x][y] != 0){
					printState[x][y] = 0;
				}
			}
		}
		path.clear();
	}
	
	public void generateLabyrinth(){
		if(width%2 == 0 || height%2 == 0) return;
		
		clear();
		//ytterväggar
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				if(x == 0 || y == 0 || x == width-1 || y == height-1)
					printState[x][y] = 1;
			}
		}
		//ena hållet
		for(int y = 1; y < height-1; y++){
			for(int x = 2; x <= width-3; x+=2){
				printState[x][y] = 1;
			}
		}
		//andra hållet
		for(int x = 1; x < width-1; x++){
			for(int y = 2; y <= height-3; y+=2){
				printState[x][y] = 1;
			}
		}
		
		labVisited = new boolean[width][height];
		int startX, startY;
		do{
			startX = getRandom(1, width-1);
			startY = getRandom(1, height-1);
		}while(startX%2==0 || startY%2==0);
		makeLabPath(startX, startY);
		
		//ut- och ingång
		findEntrance(startPos);
		printState[startPos.x][startPos.y] = 0;
		
		double maxDis;
		double dist;
		do{
			int wall = findEntrance(targetPos);
			if(wall == 0){
				maxDis = height-1;
			}else{
				maxDis = width-1;
			}
			dist = Math.sqrt(Math.pow(targetPos.x-startPos.x, 2)+Math.pow(targetPos.y-startPos.y, 2));
	
		}while(startPos.equals(targetPos) || dist < maxDis);
		
		printState[targetPos.x][targetPos.y] = 0;
	}
	
	private int findEntrance(Point p){ //returna 0 om övre eller undre väggen, 1 för höger och vänster
		int whichWall = getRandom(0, 4);//0=upp, 1=höger etc.
		int pos = 0;
		if(whichWall == 0 || whichWall == 2){
			do{
				pos = getRandom(0, width);
			}while(pos%2==0);
			p.x = pos;
			if(whichWall == 0) 
				p.y = 0;
			else
				p.y = height-1;
		}else if(whichWall == 1 || whichWall == 3){
			do{
				pos = getRandom(0, height);
			}while(pos%2==0);
			p.y = pos;
			if(whichWall == 1) 
				p.x = width-1;
			else
				p.x = 0;
		}
		
		return whichWall%2 == 0? 0:1;
	}
	
	boolean[][] labVisited;
	public void makeLabPath(int x, int y){
		boolean[] dirVisited = new boolean[4];
		
		int randDir;
		for(int i = 0; i < 4; i++){
			do{
				randDir = getRandom(0, 4);
			}while(dirVisited[randDir] == true);
			dirVisited[randDir] = true;
			
			int newX = x;
			int newY = y;
			
			switch(randDir){
			case 0:
				newY -= 2;
				break;
			case 1:
				newX += 2;
				break;
			case 2:
				newY += 2;
				break;
			case 3:
				newX -= 2;
				break;
			}
			
			if(newX > 0 && newX < width-1 && newY > 0 && newY < height-1){
				if(labVisited[newX][newY] == false){
					printState[(newX+x)/2][(newY+y)/2] = 0;//ta bort väggen
					labVisited[newX][newY] = true;
					makeLabPath(newX, newY);
				}
			}
			
		}
	}
	
	public void putRandom(){
		startPos = new Point(getRandom(0, width), getRandom(0, height));
		do{
			targetPos = new Point(getRandom(0, width), getRandom(0, height)); 
		}while(startPos.x == targetPos.x && targetPos.y == startPos.y);
		
		double perc = Double.parseDouble(labelWallpercent.getText())/100;
		for(int i = 0; i < (width*height)*perc; i++){
			Point temp = null;
			do{
				temp = new Point(getRandom(0, width), getRandom(0, height)); 
			}while(alreadyOccupied(temp.x, temp.y));
			printState[temp.x][temp.y] = 1; 
		}
		
	}
	
	public int getRandom(int from, int to){
		return (int)(Math.random()*(to-from)+from);
	}
	
	public boolean alreadyOccupied(int x, int y){
		if(printState[x][y] == 1) return true;
		
		if(startPos.x == x && startPos.y == y) return true;
		
		if(targetPos.x == x && targetPos.y == y) return true;
		
		return false;
	}
	
	ComponentListener resizeListener = new ComponentListener(){

		@Override
		public void componentHidden(ComponentEvent arg0) {
			
		}

		@Override
		public void componentMoved(ComponentEvent arg0) {
			
		}

		@Override
		public void componentResized(ComponentEvent arg0) {
			fitSize();
			
		}

		@Override
		public void componentShown(ComponentEvent arg0) {
			
		}
		
	};
	
	Point mmlOld = new Point(-1, -1);
	MouseMotionListener mml = new MouseMotionListener(){

		@Override
		public void mouseDragged(MouseEvent me) {
			int x = me.getX()/(size+spacing);
			int y = me.getY()/(size+spacing);
			Point temp = new Point(x, y);
			if(!mmlOld.equals(temp)){
				mouseListener.mouseClicked(me);
				mmlOld = temp;
			}
	
		}
		@Override
		public void mouseMoved(MouseEvent arg0) {
			
		}
		
	};
	
	MouseListener mouseListener = new MouseListener(){

		@Override
		public void mouseClicked(MouseEvent me) {
			int x = me.getX()/(size+spacing);
			int y = me.getY()/(size+spacing);
			
			if(me.isShiftDown()){
				startPos.x = x;
				startPos.y = y;
			}else if(me.isAltDown()){
				targetPos.x = x;
				targetPos.y = y;
			}else{
				Point temp = new Point(x, y);
				if(printState[temp.x][temp.y] == 0){
					printState[temp.x][temp.y] = 1;
				}else{
					printState[temp.x][temp.y] = 0;
				}
				
			}
			p.repaint();
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			
		}
		
	};
	
	ActionListener btnListener = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent ae) {
			JButton src = (JButton)ae.getSource();
			if(src == btnRandom){
				clear();
				putRandom();
			}else if(src == btnClear){
				clear();
			}else if(src == btnReset){
				reset();
			}else if(src == btnGo){
				if(running)
					stop();
				else
					start();
			}else if(src == btnLabyrinth){
				generateLabyrinth();
			}else if(src == btnSetSize){
				height = Integer.parseInt(labelHeight.getText());
				width = Integer.parseInt(labelWidth.getText());
				clear();
				fitSize();
			}
			p.repaint();
			
		}
		
	};
	
	public void fitSize(){
		int deltaWidth = centerPanel.getWidth()-p.getWidth();
		int deltaHeight = centerPanel.getHeight()-p.getHeight();
		
		if(deltaWidth < deltaHeight){
			size = centerPanel.getWidth()/(width)-spacing;
		}else{
			size = centerPanel.getHeight()/(height)-spacing;
		}
		Dimension temp = new Dimension(width*(spacing+size), (spacing+size)*height);
		//System.out.println(deltaWidth+" "+deltaHeight+" "+size+" "+temp);
		p.setPreferredSize(temp);
		p.setMinimumSize(temp);
		p.setMaximumSize(temp);
		p.revalidate();
		p.repaint();
	}
	
	/*
	public void fixWindowSize(){
		
	}
	*/
	
	public void start(){
		reset();
		running = true;
		btnGo.setBackground(Color.RED);
		
		pathFindingThread = new Thread(new Runnable(){

			@Override
			public void run() {
				if(radioAStar.isSelected()){
					aStar();
				}else if(radioBFS.isSelected()){
					bfs();
				}else if(radioDijkstra.isSelected()){
					dijkstra();
				}
				
				stop();
			}
			
		});
		
		pathFindingThread.setDaemon(true);
		pathFindingThread.start();
		
	}
	
	public void stop(){
		running = false;
		btnGo.setBackground(Color.GREEN);
		
	}
	
	//SÖKALGORITMERNA!
	
	public void controls(){
		try{
			p.repaint();
			long toSleep = Long.parseLong(labelTimer.getText());
			Thread.sleep(toSleep);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void bfs(){
		reset();
		Node[][] nodes = setupMap();
		Queue<Node> queue = new LinkedList<>();
		boolean[][] visited = new boolean[width][height];
		
		queue.add(nodes[startPos.x][startPos.y]);
		
		while(running && queue.size() > 0){
			Node current = queue.poll();
			visited[current.x][current.y] = true;
			
			printState[current.x][current.y] = 3;
			
			if(current.equals(targetPos)){
				getPath(current);
				return;
			}
			
			for(int x = -1; x <= 1; x++){
				for(int y = -1; y <= 1; y++){
					if(x == 0 && y == 0) continue;
					if(!diagonal)if(Math.abs(x) == 1 && Math.abs(y) == 1) continue;
					
					int newX = current.x+x;
					int newY = current.y+y;
					
					if(newX >= 0 && newX < width && newY >= 0 && newY < height){
						Node neighbour = nodes[newX][newY];
						if(!neighbour.traversable || visited[neighbour.x][neighbour.y] == true){
							continue;
						}
						
						if(!queue.contains(neighbour)){
							neighbour.parent = current;
							queue.add(neighbour);
						}
						
					}
				}
			}
			
			controls();
		}
	}
	
	public void dijkstra(){
		reset();
		Node[][] nodes = setupMap();
		Queue<Node> queue = new LinkedList<>();
		boolean[][] visited = new boolean[width][height];
		
		queue.add(nodes[startPos.x][startPos.y]);
		
		while(running && queue.size() > 0){
			Node current = queue.poll();
			visited[current.x][current.y] = true;
			
			printState[current.x][current.y] = 3;
			
			if(current.equals(targetPos)){
				getPath(current);
				return;
			}
			
			for(int x = -1; x <= 1; x++){
				for(int y = -1; y <= 1; y++){
					if(x == 0 && y == 0) continue;
					if(!diagonal)if(Math.abs(x) == 1 && Math.abs(y) == 1) continue;
					
					int newX = current.x+x;
					int newY = current.y+y;
					
					if(newX >= 0 && newX < width && newY >= 0 && newY < height){
						Node neighbour = nodes[newX][newY];
						if(!neighbour.traversable || visited[neighbour.x][neighbour.y] == true){
							continue;
						}
						
						int newG = current.g + getNewG(current, neighbour);
						
						if(!queue.contains(neighbour) || newG < neighbour.g){
							neighbour.parent = current;
							neighbour.g = newG;
							queue.add(neighbour);
							if(!queue.contains(neighbour))
								queue.add(neighbour);
						}
						
					}
				}
			}
			
			controls();
		}
	}
	
	public void aStar(){
		reset();
		Node[][] nodes = setupMap();
		
		ArrayList<Node> openList = new ArrayList<>();
		ArrayList<Node> closedList = new ArrayList<>();
		
		openList.add(nodes[startPos.x][startPos.y]);
		
		printState[startPos.x][startPos.y] = 2;
		
		while(running && openList.size() > 0){
			Collections.sort(openList, lowestFComparator);
			Node current = openList.get(openList.size()-1);
			openList.remove(openList.size()-1);
			closedList.add(current);
			
			printState[current.x][current.y] = 3;
			
			if(current.equals(targetPos)){
				getPath(current);
				return;
			}
			
			for(int x = -1; x <= 1; x++){
				for(int y = -1; y <= 1; y++){
					if(x == 0 && y == 0) continue;
					if(!diagonal)if(Math.abs(x) == 1 && Math.abs(y) == 1) continue;
					
					int newX = current.x+x;
					int newY = current.y+y;
					
					if(newX >= 0 && newX < width && newY >= 0 && newY < height){
						Node neighbour = nodes[newX][newY];
						if(!neighbour.traversable || closedList.contains(neighbour)){
							continue;
						}
						
						int newG = current.g + getNewG(current, neighbour);
						
						if(!openList.contains(neighbour) || newG < neighbour.g){
							neighbour.parent = current;
							neighbour.g = newG;
							neighbour.h = heuristicInterface.getHeuristic(neighbour);
							if(!openList.contains(neighbour)){
								openList.add(neighbour);
								
								printState[newX][newY] = 2;
							}
						}
					}
					
				}
			}
			
			controls();
		}
		
	}
	
	public int getNewG(Node n1, Node n2){
		if(Math.abs(n1.x-n2.x) == 1 && Math.abs(n1.y-n2.y) == 1){
			return 14;
		}
		return 10;
	}
	
	ErikInterface manhattanHeuristic = new ErikInterface(){

		@Override
		public int getHeuristic(Node neighbour) {
			int dx = Math.abs(neighbour.x-targetPos.x);
			int dy = Math.abs(neighbour.y-targetPos.y);
			
			return (dx+dy)*10;
		}
		
	};
	
	ErikInterface denAndraHeuristic = new ErikInterface(){

		@Override
		public int getHeuristic(Node neighbour) {
			int dx = Math.abs(neighbour.x-targetPos.x);
			int dy = Math.abs(neighbour.y-targetPos.y);
			
			return (int)(Math.sqrt(Math.pow(dx, 2)+Math.pow(dy, 2))*10);
		}
		
	};
	
	public Node[][] setupMap(){
		Node[][] nodes = new Node[width][height];
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				nodes[i][j] = new Node(null, i, j, true);
			}
		}
		
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				if(printState[x][y] == 1)
					nodes[x][y].traversable = false;
			}
		}
		
		return nodes;
	}
	
	public void getPath(Node n){
		//animera
		//reset();
		
		Node parent = n.parent;
		do{
			path.add(parent.getPoint());
			parent = parent.parent;
			controls(); //animera
		}while(parent != null);
		
	}
	
	class Node{
		public int x, y, g = 0, h = 0; //x ,y används väl för att kunna separera noderna
		public Node parent;
		public boolean traversable = true;
		
		public Node(Node p, int x, int y, boolean traversable){
			parent = p;
			this.x = x;
			this.y = y;
			this.traversable = traversable;
		}
		
		public int getF(){
			return g + h;
		}
		
		public Point getPoint(){
			return new Point(x, y);
		}
		
		public Node(Node p, int x, int y, int g, int h){
		
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + x;
			result = prime * result + y;
			return result;
		}
		
		public boolean equals(Point p){
			return x==p.x && y==p.y;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Node other = (Node) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (x != other.x)
				return false;
			if (y != other.y)
				return false;
			return true;
		}

		private Main getOuterType() {
			return Main.this;
		}

		@Override
		public String toString() {
			return "Node [x=" + x + ", y=" + y + ", g=" + g + ", h=" + h + ", parent=[" + parent.x + ", "+parent.y + "], traversable=" + traversable + "]";
		}
		
		
		
	}
	
	Comparator<Node> lowestFComparator = new Comparator<Node>(){

		@Override
		public int compare(Node n1, Node n2) { //minsta hamnar längst bak
			if(n1.getF() < n2.getF()){
				return 1;
			}else if(n1.getF() == n2.getF()){
				return 0;
			}else{
				return -1;
			}
		}
		
	};

}
