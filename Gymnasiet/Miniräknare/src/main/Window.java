package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.border.Border;

public class Window extends JFrame{
	
	private static final long serialVersionUID = 1L;
	
	Kalkylera _k = new Kalkylera();
	JTextField _inputField = new JTextField();
	JList<String> _historik = new JList<>();
	DefaultListModel<String> _historikModel = new DefaultListModel<>();
	JScrollPane _historikScrollPane;
	JPanel _alignmentPanel;
	ArrayList<String> _historikAnswers = new ArrayList<>();
	ArrayList<String> _historikAnswersLong = new ArrayList<>();
	
	HashMap<JButton, String> _buttons = new HashMap<>();
	JButton _equals;
	JButton _clear;
	JButton _back;
	JButton _longOrShortAnswers;
	JButton _graf;
	
	boolean _longAnswers = false;
	//boolean _curEditing = true;
	NumberFormat _nf = new DecimalFormat("#.#######################################################################################################################################################################################################################################################################################");
	
	Graph _graph;
	
	public Window(){
		
		initWindow();
		
	}
	
	public void openWindow(){
		super.setVisible(true);
		_inputField.requestFocus();
	}
	
	private void initWindow(){
		super.setSize(505, 455);
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.setResizable(false);
		super.getContentPane().setLayout(null);
		super.setTitle("Eriks Excellenta Miniräknare");
		//super.addKeyListener(mainKeyListener);
		super.setIconImage(new ImageIcon(getClass().getResource("/main/img/calculator.png")).getImage());
		
		ToolTipManager.sharedInstance().setInitialDelay(100);
		
		Color backgroundColor = Color.WHITE;
		_alignmentPanel = new JPanel(new BorderLayout());
		_alignmentPanel.setBackground(backgroundColor);
		_alignmentPanel.add(_historik, BorderLayout.SOUTH);
		_historikScrollPane = new JScrollPane(_alignmentPanel);
		_historikScrollPane.setBackground(backgroundColor);
		_historikScrollPane.setBounds(0, 0, 499, 100);
		super.getContentPane().add(_historikScrollPane);
		_historik.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		DefaultListCellRenderer renderer = (DefaultListCellRenderer) _historik.getCellRenderer();
		renderer.setHorizontalAlignment(JLabel.RIGHT);
		_historik.addMouseListener(historikListener);
		
		_historik.addMouseMotionListener(new MouseMotionAdapter() {
	        @SuppressWarnings("rawtypes")
			@Override
	        public void mouseMoved(MouseEvent e) {
	            JList l = /*(JList)e.getSource();*/ _historik;
	            //ListModel m = l.getModel();
	            int index = l.locationToIndex(e.getPoint());
	            if( index>-1 ) {
	            	if(_longAnswers)
	            		l.setToolTipText(_historikAnswersLong.get(index));
	            	else
	            		l.setToolTipText(_historikAnswers.get(index));
	            }
	        }
	    });
		
		_inputField.setBounds(0, 100, 499, 50);
		_inputField.setHorizontalAlignment(JTextField.RIGHT);
		_inputField.setFont(new Font("Arial", Font.PLAIN, 18));
		_inputField.addFocusListener(alwaysFocusOnInput);
		_inputField.addKeyListener(mainKeyListener);
		super.getContentPane().add(_inputField);
		
		fixKnappar();
		
	}
	
	private void fixKnappar(){
		//knappar större än en cell
		Font braFont = new Font("Arial", Font.PLAIN, 20);
		
		_equals = new JButton("=");
		_equals.setBounds(55*8+5, 150+55*2, 50, 150+10);
		_equals.addActionListener(functionButtonsListener);
		_equals.setFont(braFont);
		_buttons.put(_equals, "");
		super.getContentPane().add(_equals);
		
		JButton temp = new JButton("0");
		temp.setBounds(55*4+5, 150+55*4, 100+5, 50);
		temp.addActionListener(buttonListener);
		temp.setFont(braFont);
		_buttons.put(temp, "0");
		super.getContentPane().add(temp);
		
		_back = new JButton("←");
		_back.setBounds(55*4+5, 150+55*0, 100+5, 50);
		_back.addActionListener(functionButtonsListener);
		_back.setFont(braFont);
		_back.setMargin(new Insets(0, 0, 0, 0));
		_buttons.put(_back, "");
		super.getContentPane().add(_back);
		
		_clear = new JButton("C");
		_clear.setBounds(55*6+5, 150+55*0, 50, 50);
		_clear.addActionListener(functionButtonsListener);
		_clear.setFont(braFont);
		_buttons.put(_clear, "");
		super.getContentPane().add(_clear);

		_longOrShortAnswers = new JButton("S");
		_longOrShortAnswers.setBounds(55*8+5, 150+55*0, 50, 50);
		_longOrShortAnswers.addActionListener(functionButtonsListener);
		_longOrShortAnswers.setToolTipText("Short");
		_longOrShortAnswers.setFont(braFont);
		_buttons.put(_longOrShortAnswers, "");
		super.getContentPane().add(_longOrShortAnswers);
		
		_graf = new JButton(new ImageIcon(getClass().getResource("/main/img/graph1.png")));
		_graf.setBounds(55*7+5, 150+55*0, 50, 50);
		_graf.addActionListener(functionButtonsListener);
		_graf.setFont(braFont);
		_buttons.put(_graf, "");
		super.getContentPane().add(_graf);
		
		addKnappar();
		
	}
	
	private void addKnappar(){
		String[][] knapptext = {
				{"logb", "log ", "(", ")", "", "", ""},
				{"SIN ", "COS ", "TAN ", "xⁿ", "1", "2", "3", "÷", "E"},
				{"ASIN", "ACOS", "ATAN", "(-)", "4", "5", "6", "x"},
				{"π", "e", "!", "√", "7", "8", "9", "-"}, 
				{"c", "g", "φ", "ⁿ√", "", "", ",", "+"}
				
				};
		
		String[][] knappvalue = {
				{"()LOG(", "LOG(", "(", ")", "", "", ""},
				{"SIN(", "COS(", "TAN(", "^", "1", "2", "3", "/", "E"},
				{"ASIN(", "ACOS(", "ATAN(", "−", "4", "5", "6", "*"},
				{"π", "e", ")!", "√(", "7", "8", "9", "-"},
				{"c", "g", "φ", "()√(", "", "", ".", "+"}
				
				};
		
		/*Color[][] knappfarg = {
				{}
				
				};
		*/
		JButton temp;
		Font bigFont = new Font("Arial", Font.PLAIN, 20);
		Font smallFont = new Font("Arial", Font.BOLD, 12);
		
		for(int row = 0; row < knapptext.length; row++){
			for(int data = 0; data < knapptext[row].length; data++){
				if(!knapptext[row][data].equals("")){
					temp = new JButton(knapptext[row][data]);
					temp.setMargin(new Insets(0, 0, 0, 0));
					_buttons.put(temp, knappvalue[row][data]);
					temp.setBounds(5+55*data, 150+55*row, 50, 50);
					temp.addActionListener(buttonListener);
					if(knapptext[row][data].length() > 3){
						temp.setFont(smallFont);
					}else{
						temp.setFont(bigFont);
					}
					super.getContentPane().add(temp);
				}
			}
		}
	}
	
	FocusListener alwaysFocusOnInput = new FocusListener(){

		@Override
		public void focusGained(FocusEvent arg0) {
			
		}

		@Override
		public void focusLost(FocusEvent arg0) {
			new Thread(new Runnable(){
				@Override
				public void run() {
					try{
						Thread.sleep(100);
						_inputField.requestFocusInWindow();
					}catch (InterruptedException e){
						e.printStackTrace();
					}
				}
				
			}).start();
		}
		
	};
	
	ActionListener buttonListener = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent ae) {
			JButton source = (JButton) ae.getSource();
			String textToAdd = _buttons.get(source);
			int carPos = _inputField.getCaretPosition();
			//System.out.println(carPos);
			String oldText = _inputField.getText();
			String newText = oldText.substring(0, carPos)+textToAdd+oldText.substring(carPos, oldText.length());
			_inputField.setText(newText);
			_inputField.setCaretPosition((carPos+textToAdd.length()));
		}
		
	};
	
	ActionListener functionButtonsListener = new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			JButton source = (JButton) e.getSource();
			
			if(source == _equals){
				String original = _inputField.getText();
				if(!original.equals("")){
					try{
						double answer =_k.kalkylera(original);
						_historikModel.addElement(original);
						_historik.setModel(_historikModel);
						
						String longAnswer = _nf.format(answer);
						String shortAnswer = Double.toString(answer);
						_historikAnswersLong.add(longAnswer);
						_historikAnswers.add(shortAnswer);
						
						if(_longAnswers){
							_inputField.setText(longAnswer);
						}else{
							_inputField.setText(shortAnswer);
						}
						
						SwingUtilities.invokeLater(new Runnable(){
							@Override
							public void run() {
								int maxVal = _historikScrollPane.getVerticalScrollBar().getMaximum();
								_historikScrollPane.getVerticalScrollBar().setValue(maxVal);
							}
						});
					}catch(Exception ex){
						//ex.printStackTrace();
						new Thread(new Runnable(){
							public void run(){
								Border original = _inputField.getBorder();
								_inputField.setBorder(BorderFactory.createLineBorder(Color.RED));
								try{Thread.sleep(2500);}catch(Exception e){}
								_inputField.setBorder(original);
							}
						}).start();
						
					}
				}
			}else if(source == _clear){
				_inputField.setText("");
				_historik.clearSelection();
			}else if(source == _back){
				int carPos = _inputField.getCaretPosition();
				if(carPos != 0){
					String oldText = _inputField.getText();
					String newText = oldText.substring(0, carPos-1)+oldText.substring(carPos, oldText.length());
					_inputField.setText(newText);
					_inputField.setCaretPosition((carPos-1));
				}
			}else if(source == _longOrShortAnswers){
				if(_longAnswers){
					_longAnswers = false;
					_longOrShortAnswers.setText("S");
					_longOrShortAnswers.setToolTipText("Short");
				}else{
					_longAnswers = true;
					_longOrShortAnswers.setText("L");
					_longOrShortAnswers.setToolTipText("Long");
				}
				
			}else if(source == _graf){
				_graph = new Graph();
				_graph.showWindow();
			} 
		}
		
	};
	
	KeyListener mainKeyListener = new KeyListener(){

		@Override
		public void keyPressed(KeyEvent ke) {
			if(ke.getKeyCode() == 10){
				_equals.doClick();
			}
			//System.out.println(ke.getKeyCode());
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			
		}
		
		
	};
	
	MouseListener historikListener = new MouseListener(){
		@Override
		public void mouseClicked(MouseEvent me) {
			int curInd = _historik.getSelectedIndex();
			//System.out.println(me.getButton());
			if(me.getButton() == 1){
				if(curInd >= 0){
					if(_longAnswers){
						_inputField.setText(_historikAnswersLong.get(curInd));
					}else{
						_inputField.setText(_historikAnswers.get(curInd));
					}
				}
			}else if(me.getButton() == 3){
				if(curInd >= 0)
					deleteHistorikData(curInd);
			}
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
	
	public void deleteHistorikData(int index){
		_historikAnswers.remove(index);
		_historikAnswersLong.remove(index);
		_historikModel.remove(index);
		_historik.setModel(_historikModel);
	}
	
	
}
