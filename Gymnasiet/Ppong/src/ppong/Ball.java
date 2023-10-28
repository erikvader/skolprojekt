package ppong;

import java.awt.Color;
import java.awt.Graphics;

public class Ball extends GameObject{

	//if utanf�r och if �kte utanf�r p� v�nster sida
	private boolean outside = false;
	private boolean leftSide = false;
	
	//�r �ver elr under en pinne
	private boolean overPinneLeft = false;
	private boolean overPinneRight = false;
	private boolean underPinneLeft = false;
	private boolean underPinneRight = false;
	
	//g�r s� att man inte skiftar hastigheterna flera g�nger n�r man krockar med toppen eller med botten av pinnen. 
	private boolean ignoreIntersectLeft = false;
	private boolean ignoreIntersectRight = false;
	
	public Ball(String name, double x, double y, double width, double height, Color color) {
		super(name, x, y, width, height, color);
	}
	
	/**
	 * ifall bollen �r utanf�r planen s� att n�gon ska f� en po�ng.
	 * @return
	 */
	public boolean isOutside(){return outside;}
	
	/**
	 * ifall bollen �kte ut p� v�nstersida eller h�gersida.
	 * @return
	 */
	public boolean leftSide(){return leftSide;}
	
	/**
	 * flyttar p� bollen, kollar ifall den har �kt ut samt ser till s� att den studsar p� �ver- och underkanterna. 
	 */
	@Override
	public void tick(){
		super.tick();
		
		//outisde
		if(x+width < 0){
			outside = true;
			leftSide = true;
			return;
		}else if(x > PongGame.WIDTH){
			outside = true;
			leftSide = false;
			return;
		}
		
		//collision bounds
		if(y < 0){ 
			bounceY(0, 0);
			vy *= -1;
		}else if(y+height > PongGame.HEIGHT){
			bounceY(1, PongGame.HEIGHT);
			vy *= -1;
		}
	
	}
	
	/**
	 * �terst�ller bollen.
	 */
	public void reset(){
		outside = false;
		leftSide = false;
		
		overPinneLeft = false;
		overPinneRight = false;
		underPinneLeft = false;
		underPinneRight = false;
		
		ignoreIntersectLeft = false;
		ignoreIntersectRight = false;
	}
	
	/**
	 * fixar hastigheterna n�r den nuddar en pinne.
	 */
	private void ballChangeSpeedsPinne(){
		vx *= -1;
		//vx *= 1.1;
		//vy *= 1.1;
		double d = getDir();
		d += (Math.random()*6-3);
		shootDir(d, speed + 0.08);
		
	}
	
	/**
	 * skjuter bollen �t en viss vinkel med en viss hastighet. 
	 * @param grader
	 * @param speed
	 */
	public void shootDir(double grader, double speed){
		vx = Math.cos(Math.toRadians(grader))*speed;
		vy = -Math.sin(Math.toRadians(grader))*speed;
		this.speed = speed;
	}
	
	/**
	 * fixar bollens y-positioner n�r den krockar i n�gonting ovanf�r eller nedanf�r sig. 
	 * 
	 * @param dir
	 * @param py
	 */
	private void bounceY(int dir, double py){ //0 = up, 1 = down; up �r studsa p� �verkanten
		double extra;
		if(dir == 0){
			extra = py-y;
			y = py+extra;
		}else if(dir == 1){
			extra = (y+height)-py;
			y = py-height-extra;
		}
		
		//vy *= -1;
		
	}
	
	/**
	 * fixar bollens x-positioner n�r den krockar i n�gonting bredvid sig. 
	 * 
	 * @param dir
	 * @param px
	 */
	private void bounceX(int dir, double px){ //0 = left, 1 = right; left �r studsa p� v�nsterkanten
		double extra;
		if(dir == 0){
			extra = px-x;
			x = px+extra;
		}else if(dir == 1){
			extra = (x+width)-px;
			x = px-width-extra;
		}
		
		//vx *= -1;
		
	}
	
	/**
	 * fixar hastigheterna n�r bollen krockar �ver eller under pinnen
	 * 
	 * @param left
	 */
	private void bounceTopOrBottomChangeSpeeds(boolean left){
		//vx *= -1;
		
		boolean under, over, ballUp;
		if(left){
			if(ignoreIntersectLeft) return;
			ignoreIntersectLeft = true;
			under = underPinneLeft;
			over = overPinneLeft;
		}else{
			if(ignoreIntersectRight) return;
			ignoreIntersectRight = true;
			under = underPinneRight;
			over = overPinneRight;
		}
		ballUp = vy < 0;
		
		if(over && ballUp){
			//�ndra inte vy
		}else if(under && !ballUp){
			//�ndra inte
		}else{
			vy *= -1;
		}
		
	}
	
	/**
	 * kollar ifall bollen krockar med en pinne och hanterar det ifall den g�r det.
	 * 
	 * @param p
	 */
	public void bouncePinne(Pinne p){
		if(intersects(p)){
			if(p.isLeftSide()){
				if(overPinneLeft){
					bounceTopOrBottomChangeSpeeds(true);
					bounceY(1, p.y);
				}else if(underPinneLeft){
					bounceTopOrBottomChangeSpeeds(true);
					bounceY(0, p.y+p.height);
				}else{
					bounceX(0, p.x+p.width);
					ballChangeSpeedsPinne();
				}
			}else{
				if(overPinneRight){
					bounceTopOrBottomChangeSpeeds(false);
					bounceY(1, p.y);
				}else if(underPinneRight){
					bounceTopOrBottomChangeSpeeds(false);
					bounceY(0, p.y+p.height);
				}else{
					bounceX(1, p.x);
					ballChangeSpeedsPinne();
				}
			}
		}else{
			if(p.isLeftSide()){
				if(x <= p.x+p.width){
					overPinneLeft = (y+height < p.y);
					underPinneLeft = (y > p.y+p.height);
				}else{
					ignoreIntersectLeft = false;
				}
			}else{
				if(x+width >= p.x){
					overPinneRight = (y+height < p.y);
					underPinneRight = (y > p.y+p.height);
				}else{
					ignoreIntersectRight = false;
				}
			}
		}
	}
	
	@Override
	public void draw(Graphics g) {
		super.draw(g);
		
	}
	

}
