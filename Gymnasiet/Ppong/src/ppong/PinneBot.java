package ppong;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class PinneBot extends Pinne{

	//alla bollar
	private ArrayList<Ball> balls;
	//bollar som �ker mot oss
	private boolean[] comingTowards;
	//r�relsem�nstret ska uppdateras
	private boolean updateBot = false;
	private int counter = 0;
	//vilket r�relsem�nster vi ska utf�ra
	private Queue<Target> orders;
	
	//destination
	private double ty;
	private boolean gotoDestination = false;
	
	//s�kning//
	private ArrayList<Target> bestTarget; //b�sta r�relsem�nstret vi har hittat
	//safety marginals
	private int smTime = 1; 
	private double smDist = 5; //boll height = 10
	
	private double ballHeight;
	
	/**
	 * Skapar en pinne som �r en bot.
	 * 
	 * @param name
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param color
	 * @param leftSide
	 * @param balls
	 */
	public PinneBot(String name, double x, double y, double width, double height, Color color, boolean leftSide, ArrayList<Ball> balls){
		super(name, x, y, width, height, color, leftSide);
		orders = new LinkedList<Target>();
		bestTarget = new ArrayList<Target>();
		
		this.balls = balls;
		comingTowards = new boolean[balls.size()];
		ballHeight = balls.get(0).height;
		
		resetOrders();
		sealOrders();
		
		speed = 3;
	}
	
	/**
	 * uppdaterar boten.
	 * <br>
	 * kollar ifall det �r en NY boll som �ker emot oss, ifall det �r det s� ska vi uppdatera r�relsem�nstret. 
	 */
	@Override
	public void tick() {
		//borde uppdatera bollar?
		updateBot = false;
		for(int i = 0; i < balls.size(); i++){
			Ball b = balls.get(i);
			if((leftSide && b.vx < 0 && !comingTowards[i] && b.x > 0) || 
				(!leftSide && b.vx > 0 && !comingTowards[i] && b.x+b.width < PongGame.WIDTH)
				){
				comingTowards[i] = true;
				updateBot = true;
				counter = 0;
			}else if((leftSide && b.vx > 0 && comingTowards[i]) || 
					(!leftSide && b.vx < 0 && comingTowards[i]) || 
					(b.x <= 0) || (b.x+b.width >= PongGame.WIDTH)){
				comingTowards[i] = false;
			}
		}
		
		//updatera bot? hitta ny v�g
		if(updateBot){
			generateOrders();
		}
		
		//utf�r v�g
		counter++;
		if(counter == orders.peek().delay){
			counter = 0;
			orders.poll();
			gotoDestination(orders.peek().target);
		}
		
		//flytta sig
		if(gotoDestination){
			if(y > ty){
				if(ty-y > -speed){
					vy = ty-y;
				}else{
					vy = -speed;
				}
			}else if(y < ty){
				if(ty-y < speed){
					vy = ty-y;
				}else{
					vy = speed;
				}
			}else if(y == ty){
				gotoDestination = false;
				vy = 0;
			}
		}
		y += vy;
		checkBounds();
		
		//super.tick();
	}
	
	/**
	 * genererar och r�knar ut ett nytt optimalt r�relsem�nster. (kan bli b�ttre ifall man inte anv�nde brute force)
	 */
	private void generateOrders(){
		resetOrders();
		
		//generera target poses
		ArrayList<Target> targets = new ArrayList<Target>();
		for(int i = 0; i < balls.size(); i++){
			Ball b = balls.get(i);
			if(comingTowards[i]){
				targets.add(calcTarget(b));
			}
		}
		
		//testa alla kombinationer
		boolean[] visited = new boolean[targets.size()];
		ArrayList<Target> bestTarget = new ArrayList<Target>();
		this.bestTarget.clear();
		recurs(y, visited, bestTarget, targets, 0);
		
		for(int i = 0; i < this.bestTarget.size(); i++){
			orders.add(this.bestTarget.get(i));
		}
		
		sealOrders();
	}
	
	/**
	 * rekursiv funktion f�r att brute force:a
	 * @param cury
	 * @param visited
	 * @param bestTarget
	 * @param targets
	 * @param timeLapsed
	 */
	private void recurs(double cury, boolean[] visited, ArrayList<Target> bestTarget, ArrayList<Target> targets, int timeLapsed){
		for(int i = 0; i < targets.size(); i++){
			if(visited[i] == false){
				visited[i] = true;
				Target tar = targets.get(i);
				double s = 0; //str�ckan
				if(tar.target+ballHeight < cury+smDist){
					s = (tar.target+ballHeight)-(cury+smDist);
				}else if(tar.target > cury+height-smDist){
					 s = tar.target-(cury+height-smDist);
				}
				double t = Math.ceil(Math.abs(s)/speed); //tid det tar att �ka dit
				//hinner vi?
				if(tar.delay-timeLapsed+smTime > t){
					bestTarget.add(new Target(tar.delay-timeLapsed+smTime, cury+s));
					if(bestTarget.size() > this.bestTarget.size()){
						this.bestTarget = new ArrayList<Target>(bestTarget); //nya b�sta
					}
					recurs(cury+s, visited, bestTarget, targets, (tar.delay+smTime));
					bestTarget.remove(bestTarget.size()-1);
				}
				visited[i] = false;
			}
		}
	}
	
	/**
	 * r�knar ut vilken y-position som bollen kommer att krocka med pinnen p�.
	 * <br>
	 * Den g�r massor av r�ta linjer och forts�tter till n�gon linje tr�ffar n�gon sida. 
	 * 
	 * @param b
	 * @return
	 */
	private Target calcTarget(Ball b){
		double goal = leftSide ? x+width : x-b.width;
		double lower = PongGame.HEIGHT-b.height;
		
		double k, m, bx, by, bdx, bdy;
		
		bx = b.x;
		by = b.y;
		bdx = b.vx;
		bdy = b.vy;
		
		while(true){
			k = bdy/bdx;
			m = by - k*bx;
			
			//n�r goal
			double goalY = k*goal+m;
			if(goalY >= 0 && goalY <= lower){
				int time = (int)Math.ceil(Math.abs(goal-b.x)/Math.abs(b.vx));
				return new Target(time, goalY);
			}else{
				bdy *= -1;
				by = goalY < 0 ? 0 : lower;
				bx = (by-m)/k;
			}
		}
		
	}
	
	/**
	 * �terst�ller r�relsem�nstret och l�gger till en startgrej.
	 */
	private void resetOrders(){
		orders.clear();
		orders.add(new Target(1, 0)); //startgrej
	}
	
	/**
	 * avslutar r�relsem�nstret genom att l�gga till en slutgrej som g�r att den kommer att �ka till mitten och stanna d�r.
	 */
	private void sealOrders(){
		orders.add(new Target(-1, PongGame.HEIGHT/2 - height/2)); //slutgrej.
	}
	
	/**
	 * s�ger �t pinnen att g� mot en destination. 
	 * 
	 * @param y
	 */
	private void gotoDestination(double y){
		//System.out.println("Goin to: "+y);
		ty = y;
		gotoDestination = true;
	}
	
	//g�r s� att spelaren inte kan styra pinnen. 
	@Override
	public void setUp(boolean b) {
		up = false;
	}
	
	@Override
	public void setDown(boolean b) {
		down = false;
	}
	
	@Override
	public void draw(Graphics g) {
		super.draw(g);
		
		//draw target
		/*Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.RED);
		Shape l = new Line2D.Double(x*PongGame.SCALE, blah*PongGame.SCALE, (x+width*2)*PongGame.SCALE, blah*PongGame.SCALE);
        g2d.draw(l);
        l = new Line2D.Double((x+width)*PongGame.SCALE, (blah-20)*PongGame.SCALE, (x+width)*PongGame.SCALE, (blah+20)*PongGame.SCALE);
        g2d.draw(l);*/
		//g2d.dispose();
	}
	
	/**
	 * 
	 * en klass som s�ger vart en boll kommer att tr�ffa en sida och hur l�ng tid det tar f�r den att komma dit. 
	 *
	 */
	private class Target{
		public int delay;
		public double target;
		public Target(int d, double t){
			delay = d;
			target = t;
		}		
	}
}
