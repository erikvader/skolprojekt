package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

public class Graph extends JFrame{

	private static final long serialVersionUID = 1L;

	EJPanel mainPanel = new EJPanel();
	JPanel northernField = new JPanel();
	JTextField input = new JTextField(); 
	JLabel lab = new JLabel("f(x)=");
	JButton goBtn = new JButton("DRAW!");
	JSpinner precisionSpinner = new JSpinner();
	JCheckBox autoDraw = new JCheckBox();
	
	JButton goLeft = new JButton();
	JButton goRight = new JButton();
	JButton goUp = new JButton();
	JButton goDown = new JButton();
	JButton goIn = new JButton();
	JButton goOut = new JButton();
	JPanel buttonsPane = new JPanel(new FlowLayout());
	
	Kalkylera k = new Kalkylera();
	double _offsetLeft = 0;
	double _offsetUp = 0;
	double _speed = 30;
	double _zoom = 1;
	//double _precision = 0.1;
	ArrayList<EPoint> _points = new ArrayList<>();
	
	public Graph(){
		super.setTitle("Eriks Excellenta Grafritare");
		super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		super.setSize(500, 500);
		super.setResizable(true);
		super.setIconImage(new ImageIcon(getClass().getResource("/main/img/graph1.png")).getImage());
		
		super.getContentPane().add(mainPanel, BorderLayout.CENTER);
		
		northernField.setLayout(new BoxLayout(northernField, BoxLayout.X_AXIS));
		northernField.add(lab);
		northernField.add(input);
		northernField.add(precisionSpinner);
		northernField.add(autoDraw);
		northernField.add(goBtn);
		
		precisionSpinner.setPreferredSize(new Dimension(60, 0));
		SpinnerNumberModel m = new SpinnerNumberModel(0.1, 0.001, 10, 0.01);
		precisionSpinner.setModel(m);
		
		autoDraw.setSelected(true);
		autoDraw.setToolTipText("Auto drawer");
		
		super.getContentPane().add(northernField, BorderLayout.NORTH);
		
		goBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mainPanel.generateGraph(input.getText());
				mainPanel.repaint();
			}
		});
		
		Dimension btnDim = new Dimension(40, 40);
		
		buttonsPane.add(goUp);
		buttonsPane.add(goDown);
		buttonsPane.add(goLeft);
		buttonsPane.add(goRight);
		buttonsPane.add(goIn);
		buttonsPane.add(goOut);
		super.getContentPane().add(buttonsPane, BorderLayout.SOUTH);
		goUp.addActionListener(navListener);
		goDown.addActionListener(navListener);
		goRight.addActionListener(navListener);
		goLeft.addActionListener(navListener);
		goIn.addActionListener(navListener);
		goOut.addActionListener(navListener);
		
		goUp.setPreferredSize(btnDim);
		goDown.setPreferredSize(btnDim);
		goIn.setPreferredSize(btnDim);
		goOut.setPreferredSize(btnDim);
		goRight.setPreferredSize(btnDim);
		goLeft.setPreferredSize(btnDim);
		
		goUp.setIcon(new ImageIcon(getClass().getResource("/main/img/arrow_up.png")));
		goRight.setIcon(new ImageIcon(getClass().getResource("/main/img/arrow_right.png")));
		goLeft.setIcon(new ImageIcon(getClass().getResource("/main/img/arrow_left.png")));
		goDown.setIcon(new ImageIcon(getClass().getResource("/main/img/arrow_down.png")));
		goIn.setIcon(new ImageIcon(getClass().getResource("/main/img/zoom_in.png")));
		goOut.setIcon(new ImageIcon(getClass().getResource("/main/img/zoom_out.png")));
		
	}
	
	ActionListener navListener = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent ae) {
			JButton source = (JButton) ae.getSource();
			if(source == goUp){
				_offsetUp += (_speed/_zoom);
			}else if(source == goDown){
				_offsetUp -= (_speed/_zoom);
			}else if(source == goRight){
				_offsetLeft -= (_speed/_zoom);
			}else if(source == goLeft){
				_offsetLeft += (_speed/_zoom);
			}else if(source == goIn){
				if(_zoom < 1)
					_zoom += 0.1;
				else
					_zoom += 1;
			}else if(source == goOut){
				if(_zoom > 1)
					_zoom -= 1;
				else if(_zoom > 0.2)
					_zoom -= 0.1;
			}
			mainPanel.repaint();
		}
	};
	
	public void showWindow(){
		super.setVisible(true);
	}
	
	
	private class EJPanel extends JPanel{

		private static final long serialVersionUID = 1L;
		
		double defaultOffsetLeft;
		double defaultOffsetTop;
		
		public EJPanel(){
			
		}
		
		@Override
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			super.setBackground(Color.WHITE);
			
			/*Graphics2D g2d = (Graphics2D) g.create();
	        Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
	        g2d.setStroke(dashed);
	        g2d.drawLine(0, 0, super.getWidth(), super.getHeight());
	        g2d.dispose();*/
			
			defaultOffsetLeft = super.getWidth()/2;
			defaultOffsetTop = super.getHeight()/2;
			
			Graphics2D g2 = (Graphics2D)g;
			
			g2.draw(new Line2D.Double(0, defaultOffsetTop+_offsetUp*_zoom, super.getWidth(), defaultOffsetTop+_offsetUp*_zoom));
			g2.draw(new Line2D.Double(defaultOffsetLeft+_offsetLeft*_zoom, 0, defaultOffsetLeft+_offsetLeft*_zoom, super.getHeight()));
		
			if(!_points.isEmpty()){
				if(autoDraw.isSelected() && !input.getText().equals("")){
					generateGraph(input.getText());
				}
				drawGraph(g2);
			}
			
			g2.dispose();
			
		}
		
		private void drawGraph(Graphics2D g){
			
			g.setColor(Color.BLUE);
			EPoint lastPoint = new EPoint(true);
			for(int i = 0; i < _points.size(); i++){
				EPoint newPoint = _points.get(i);
				
				if(!lastPoint.getEmpty() && !newPoint.getEmpty()){
					g.draw(new Line2D.Double(defaultOffsetLeft+(lastPoint.getX()+_offsetLeft)*_zoom, defaultOffsetTop+(-1*lastPoint.getY()+_offsetUp)*_zoom, defaultOffsetLeft+(newPoint.getX()+_offsetLeft)*_zoom, defaultOffsetTop+(-1*newPoint.getY()+_offsetUp)*_zoom));
				}
				
				//lastPoint = newPoint
				lastPoint.setX(newPoint.getX());
				lastPoint.setY(newPoint.getY());
				lastPoint.setEmpty(newPoint.getEmpty());
			}
		}
		
		public void generateGraph(String func){
			_points.clear();
			
			double leftSide = -defaultOffsetLeft*(1/_zoom)+_offsetLeft*-1;
			double rightSide = defaultOffsetLeft*(1/_zoom)+_offsetLeft*-1;
			
			for(double x = leftSide; x < rightSide; x += (double)precisionSpinner.getValue()){
				String xvalue = k.translateMinusToNeg(Double.toString(x));
				String toCalculate = func.replace("x", xvalue);
				double y = 0; 
				try{
					y = k.kalkylera(toCalculate);
					_points.add(new EPoint(x, y));
				}catch(Exception ex){
					System.out.println("ett fel");
					_points.add(new EPoint(true));
				}
			}
		}
	}
	
	public class EPoint{
		
		private double x;
		private double y;
		private boolean empty = true;
		
		public EPoint(double x, double y){
			this.x = x;
			this.y = y;
			empty = false;
		}
		
		public EPoint(boolean empty){
			this.empty = empty;
		}
		
		public double getX(){
			return x;
		}
		
		public void setX(double x){
			this.x = x;
		}
		
		public double getY(){
			return y;
		}
		
		public void setY(double y){
			this.y= y;
		}
		
		public boolean getEmpty(){
			return empty;
		}
		
		public void setEmpty(boolean empty){
			this.empty = empty;
		}
		
		public String toString(){
			return "["+x+", "+y+", "+empty+"]";
		}
	}
	
}
