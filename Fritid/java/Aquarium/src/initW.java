import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class initW implements ActionListener, ItemListener, ChangeListener {
	
	public int fishes = 5;
	public int weeds = 5;
	public boolean go = false;
	public int selIn;
	
	spriteCollection col;
	screen s;
	JFrame window = new JFrame();
	
	ImageIcon curImage;
	ImageIcon[] alt = {
			new ImageIcon(setIconImage(new ImageIcon (getClass().getResource("textures/backgrounds/fishtank1.jpg")).getImage())),
			new ImageIcon(setIconImage(new ImageIcon (getClass().getResource("textures/backgrounds/fishtank2.jpg")).getImage())),
			new ImageIcon(setIconImage(new ImageIcon (getClass().getResource("textures/backgrounds/fishtank3.jpg")).getImage())),
			new ImageIcon(setIconImage(new ImageIcon (getClass().getResource("textures/backgrounds/fishtank4.jpg")).getImage())),
			new ImageIcon(setIconImage(new ImageIcon (getClass().getResource("textures/backgrounds/fishtank5.jpg")).getImage())),
			new ImageIcon(getClass().getResource("textures/backgrounds/fishtank6i.jpg")),
			new ImageIcon(getClass().getResource("textures/backgrounds/fishtank7i.jpg"))
						};
	JComboBox<ImageIcon> bgChooser = new JComboBox<ImageIcon>(alt);
	JButton start = new JButton("Start");
	JTextField weedField = new JTextField("5 seaweed(s)", 20);
	JTextField fishField = new JTextField("5 fish(es)", 20);
	JSlider weedSlider = new JSlider(JSlider.HORIZONTAL, 0, 20, 5);
	JSlider fishSlider = new JSlider(JSlider.HORIZONTAL, 0, 20, 5);
	
	//tutScreen tut = new tutScreen();
	welcomeScreen ws = new welcomeScreen();
		
	JPanel topLeft = new JPanel();
	JPanel topRight = new JPanel();
	JPanel top = new JPanel();
	JPanel middle = new JPanel();
	JPanel bot = new JPanel();
	JPanel whole = new JPanel();
	JPanel tut = new JPanel();
	
	JTabbedPane tabs = new JTabbedPane();
	
	Random rand = new Random();
	
	JTextField[] fieldTable;
	String[] textTable = {
			"KeyBindings", "LMB: Change fish direction", "RMB: Stop fish",
			"Scrollwheel: Change speed on fish", "Ctrl + Scrollwheel: Change X speed",
			"Shift + Scrollwheel: Change Y speed", "S: Stop fish", "D: Stop all fishes",
			"Arrow up: Increase Y speed", "Arrow down: Decrease Y speed", "Arrow right: Increase X speed",
			"Arrow left: Decrease X speed", "ESC: Exit program"
			};
	
	int[][] startPoses;

	public initW(screen ss, spriteCollection sss) {
		s = ss;
		col = sss;
		
		startPoses = setStartPoses();
		window.setTitle("Aquarium");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(400, 325);
		window.setResizable(false);
		window.setVisible(true);
		window.add(tabs, BorderLayout.CENTER);
		
		topLeft.setLayout(new BoxLayout(topLeft, BoxLayout.Y_AXIS));
		topLeft.add(fishField);
		topLeft.add(fishSlider);
		fishField.setEditable(false);
		fishField.setHorizontalAlignment(JTextField.CENTER);
		fishSlider.setMajorTickSpacing(5);
		fishSlider.setMinorTickSpacing(1);
		fishSlider.setPaintLabels(true);
		fishSlider.setPaintTicks(true);
		fishSlider.addChangeListener(this);
		
		topRight.setLayout(new BoxLayout(topRight, BoxLayout.Y_AXIS));
		topRight.add(weedField);
		topRight.add(weedSlider);
		topRight.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		weedField.setEditable(false);
		weedField.setHorizontalAlignment(JTextField.CENTER);
		weedSlider.setMajorTickSpacing(5);
		weedSlider.setMinorTickSpacing(1);
		weedSlider.setPaintLabels(true);
		weedSlider.setPaintTicks(true);
		weedSlider.addChangeListener(this);
		
		top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));
		top.add(topLeft);
		top.add(topRight);
		top.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		middle.setLayout(new FlowLayout());
		middle.add(bgChooser);
		bgChooser.setMaximumRowCount(5);
		bgChooser.addItemListener(this);
		
		bot.setLayout(new FlowLayout());
		bot.add(start);
		start.setPreferredSize(new Dimension(100, 50));
		start.addActionListener(this);
		
		whole.setLayout(new BoxLayout(whole, BoxLayout.Y_AXIS));
		whole.add(top);
		whole.add(middle);
		whole.add(bot);
		
		tut.setLayout(new BoxLayout(tut, BoxLayout.Y_AXIS));
		addTutorialLines(tut);
		
		tabs.setTabPlacement(JTabbedPane.BOTTOM);
		tabs.addTab("Home", null, ws);
		tabs.addTab("Settings", null, whole);
		tabs.addTab("Keybindings", tut);
		
	}
	
	public void addTutorialLines(JPanel t){
		fieldTable = new JTextField[textTable.length];
		for(int i = 0; i<textTable.length; i++){
			fieldTable[i] = new JTextField(textTable[i]);
			fieldTable[i].setEditable(false);
			fieldTable[i].setBorder(null);
			fieldTable[i].setBackground(Color.WHITE);
			if(i==0){
				fieldTable[0].setHorizontalAlignment(JTextField.CENTER);
				fieldTable[0].setFont(new Font("Serif", Font.BOLD, 14));
			}
			t.add(fieldTable[i]);
		}
	}
	
	public int[][] setStartPoses(){
		
		int[][] returnTable = new int[40][3];
		//int[] xposes = {0, 50, 150, 100, 10, };
		//int[] yposes = {0, 50, 100, 150, 900,};
		int ypos = 10;
		int xpos = 10;
		
		for(int i = 0; i<40; i++){
			if(xpos > 1000){
				xpos = 10;
				ypos += 200;
			}
			returnTable[i][0] = xpos;
			xpos += 100;
			//System.out.println("X "+returnTable[i][0]);
			returnTable[i][1] = ypos;
			//System.out.println("Y "+returnTable[i][1]);
			
			returnTable[i][2] = 0;
		}
		
		return returnTable;
	}
	
	public int[] getRandomPos(){
		
		int[] returnTable = new int[2];
		int randIndex;
		do{
			 randIndex = rand.nextInt(40);
		}while(startPoses[randIndex][2] == 1);
		
		returnTable[0] = startPoses[randIndex][0];
		returnTable[1] = startPoses[randIndex][1];
		startPoses[randIndex][2] = 1;
		
		return returnTable;
		
	}

	/*public static void main(String[] args) {
		initW b = new initW();
	}*/
	
	public boolean getGo(){
		return go;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == start) {
			setStuff();
			window.setVisible(false);
			go = true;
		}
	}

	public void itemStateChanged(ItemEvent e) {
		curImage = new ImageIcon(getClass().getResource("textures/backgrounds/fishtank"+Integer.toString(bgChooser.getSelectedIndex()+1)+".jpg"));
		s.setBG(curImage);
		selIn = bgChooser.getSelectedIndex()+1;
	}
	
	public Image setIconImage(Image i){
		return i.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
	}

	public void stateChanged(ChangeEvent e) {
		if(e.getSource() == fishSlider){
			fishes = fishSlider.getValue();
			fishField.setText(Integer.toString(fishes)+" fish(es)");
		}else if(e.getSource() == weedSlider){
			weeds = weedSlider.getValue();
			weedField.setText(Integer.toString(weeds)+" seaweed(s)");
		}
	}
	
	public void setStuff(){
		setFishes();
		setWeeds();
	}
	
	public void setFishes(){
		sprite tempFish;
		int[] pos;
		
		for(int i = 0; i < fishes; i++){
			int randFish = rand.nextInt(6)+1; //hur många olika fiskar det finns
			float randvx = setRandFloat();
			float randvy = setRandFloat();
			pos = getRandomPos();
			tempFish = new sprite("fish"+Integer.toString(i), randvx, randvy, pos[0], pos[1], false);
			tempFish.addImage(new ImageIcon(getClass().getResource("textures/fishes/fish"+Integer.toString(randFish)+".png")));
			tempFish.addImage(new ImageIcon(getClass().getResource("textures/fishes/fish"+Integer.toString(randFish)+"r.png")));
			tempFish.switchImage(1);
			tempFish.setHitboxFromImageDimension();
			col.add(tempFish);
		}
	}
	
	public float setRandFloat(){
		float returnFloat;
		
		returnFloat = rand.nextInt(2)+1;
		int digit = rand.nextInt(4);
		if(digit == 0){
			returnFloat += 0.25;
		}else if(digit == 1){
			returnFloat += 0.5;
		}else if(digit == 2){
			returnFloat += 0.75;
		}
		
		return returnFloat;
	}
	
	public void setWeeds(){
		sprite tempWeed;
		int[] pos;
		
		for(int i = 0; i < weeds; i++){
			int randFish = rand.nextInt(3)+1; //hur många olika sjögräs det finns
			float randvx = setRandFloat();
			float randvy = setRandFloat();
			pos = getRandomPos();
			tempWeed = new sprite("weed"+Integer.toString(i), randvx, randvy, pos[0], pos[1], true);
			tempWeed.setCurImage(new ImageIcon(getClass().getResource("textures/seaweeds/seaweed"+Integer.toString(randFish)+".png")));
			tempWeed.setHitboxFromImageDimension();
			col.add(tempWeed);
		}
	}

}
