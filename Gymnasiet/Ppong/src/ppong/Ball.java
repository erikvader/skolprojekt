package ppong;

import java.awt.Color;
import java.awt.Graphics;

public class Ball extends GameObject{

	//if utanför och if åkte utanför på vänster sida
	private boolean outside = false;
	private boolean leftSide = false;
	
	//är över elr under en pinne
	private boolean overPinneLeft = false;
	private boolean overPinneRight = false;
	private boolean underPinneLeft = false;
	private boolean underPinneRight = false;
	
	//gör så att man inte skiftar hastigheterna flera gånger när man krockar med toppen eller med botten av pinnen. 
	private boolean ignoreIntersectLeft = false;
	private boolean ignoreIntersectRight = false;
	
	public Ball(String name, double x, double y, double width, double height, Color color) {
		super(name, x, y, width, height, color);
	}
	
	/**
	 * ifall bollen är utanför planen så att någon ska få en poäng.
	 * @return
	 */
	public boolean isOutside(){return outside;}
	
	/**
	 * ifall bollen åkte ut på vänstersida eller högersida.
	 * @return
	 */
	public boolean leftSide(){return leftSide;}
	
	/**
	 * flyttar på bollen, kollar ifall den har åkt ut samt ser till så att den studsar på över- och underkanterna. 
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
	 * återställer bollen.
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
	 * fixar hastigheterna när den nuddar en pinne.
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
	 * skjuter bollen åt en viss vinkel med en viss hastighet. 
	 * @param grader
	 * @param speed
	 */
	public void shootDir(double grader, double speed){
		vx = Math.cos(Math.toRadians(grader))*speed;
		vy = -Math.sin(Math.toRadians(grader))*speed;
		this.speed = speed;
	}
	
	/**
	 * fixar bollens y-positioner när den krockar i någonting ovanför eller nedanför sig. 
	 * 
	 * @param dir
	 * @param py
	 */
	private void bounceY(int dir, double py){ //0 = up, 1 = down; up är studsa på överkanten
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
	 * fixar bollens x-positioner när den krockar i någonting bredvid sig. 
	 * 
	 * @param dir
	 * @param px
	 */
	private void bounceX(int dir, double px){ //0 = left, 1 = right; left är studsa på vänsterkanten
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
	 * fixar hastigheterna när bollen krockar över eller under pinnen
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
			//ändra inte vy
		}else if(under && !ballUp){
			//ändra inte
		}else{
			vy *= -1;
		}
		
	}
	
	/**
	 * kollar ifall bollen krockar med en pinne och hanterar det ifall den gör det.
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
