package game.entity.level3_boss;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import game.entity.Animation;
import game.entity.MapObject;
import game.entity.Player;
import game.entity.enemies.enemyProjectile.EnemyProjectile;
import game.entity.enemies.enemyProjectile.EnemyProjectileLaser;
import game.handlers.Calc;
import game.handlers.Content;
import game.tileMap.TileMap;

public class Pekare extends MapObject{
	
	private ArrayList<EnemyProjectile> ep;
	private Player player;
	
	private final double standardPekar = 214.23;
	private double curPekar = standardPekar;
	
	//states
	public static final int NOTHING = 0;
	public static final int IDLE = 1;
	public static final int SHOOT_SPARTAN_LASER = 2;
	public static final int SHOOT_AT_PLAYER = 3;
	public static final int SHOOT_AT_RANDOM = 4;
	public static final int SHOOT_ALOT = 5;
	
	private int curState = NOTHING;
	
	private boolean canChange = true;

	//grejer till idle
	private double maxLeft = 210, maxRight = 330;
	private final double orgTurnSpeed = 0.5;
	private double turnSpeed = orgTurnSpeed;
	private boolean idleTarget = false; //false är maxLeft och true är maxRight
	
	private int counter = 0;
	
	//rörelse
	public static final int STILL = 0;
	public static final int FOLLOW_PLAYER = 1;
	public static final int BACK_AND_FORTH = 2;
	public static final int GOTO = 3;
	
	private int curMovement = STILL;
	
	private boolean isGoto = false;
	private double gotoTarget = 0;
	
	private boolean spartanLine = false;
	
	//speeds
	public static final int SLOW = 0;
	public static final int MEDIUM = 1;
	public static final int FAST = 2;
	public static final int OFANTLIGT_SNABBT = 3;
	
	private int curHastighet = 1; //själva delayen
	
	//modes för skjuta singel
	public static final int MODE_SINGLE = 0;
	public static final int MODE_TRIPPLE = 1;
	
	private int curMode = MODE_SINGLE;
	
	public Pekare(TileMap tm, ArrayList<EnemyProjectile> ep, Player p) {
		super(tm);
		
		this.ep = ep;
		this.player = p;
		
		width = cwidth = 60;
		height = cheight = 60;
		
		animation = new Animation();
		animation.setFrames(Content.loadRow("/Resources/Sprites/Enemies/johannes/pekare.png", 60, 60, 0, 1), -1);
		//animation.setEmpty();
		
		
	}
	
	public void begin(){
		setIdle();
	}
	
	public void unBegin(){
		curState = NOTHING;
		curMovement = STILL;
	}
	
	public void setIdle(){
		if(curState != IDLE && canChange){
			curState = IDLE;
			counter = 0;
		}
	}
	
	public void shootSpartan(){
		if(curState != SHOOT_SPARTAN_LASER && canChange){
			curState = SHOOT_SPARTAN_LASER;
			counter = 0;
			canChange = false;
		}
	}
	
	public void shootAlot(){
		if(curState != SHOOT_ALOT && canChange){
			curState = SHOOT_ALOT;
			counter = 0;
			canChange = false;
		}
	}
	
	public void shootAtPlayer(int speed, int mode){
		if(curState != SHOOT_AT_PLAYER && canChange){
			curState = SHOOT_AT_PLAYER;
			counter = 0;
			
			curMode = mode;
			
			switch(speed){
			case SLOW:
				curHastighet = 120;
				break;
			case MEDIUM:
				curHastighet = 90;
				break;
			case FAST:
				curHastighet = 60;
				break;
			case OFANTLIGT_SNABBT:
				curHastighet = 30;
				break;
			}
		}
	}
	
	public void shootAtRandom(int speed, int mode){
		if(curState != SHOOT_AT_RANDOM && canChange){
			curState = SHOOT_AT_RANDOM;
			counter = 0;
			
			curMode = mode;
			
			switch(speed){
			case SLOW:
				curHastighet = 120;
				break;
			case MEDIUM:
				curHastighet = 90;
				break;
			case FAST:
				curHastighet = 60;
				break;
			case OFANTLIGT_SNABBT:
				curHastighet = 30;
				break;
			}
		}
	}
	
	private void setPekar(double angle){
		curPekar = Calc.fixAngle(angle);
	}
	
	public void update(){
		counter++;
		//states
		if(curState == IDLE){
			curMovement = BACK_AND_FORTH;
			
		}else if(curState == SHOOT_SPARTAN_LASER){
			if(counter == 1){
				curMovement = FOLLOW_PLAYER;
				spartanLine = true;
			}else if(counter >= 30 && counter <= 390){
				spartanLine = (int)(counter/(20-(counter-30)*(15/360.0))) % 2 == 0 ? true : false;
				turnSpeed = orgTurnSpeed-((counter-30)*(0.25/360.0));
			}else if(counter == 391){
				curMovement = STILL;
				addSpartan();
				spartanLine = false;
			}else if(counter == 551){
				canChange = true;
				setIdle();
				turnSpeed = orgTurnSpeed;
			}
		}else if(curState == SHOOT_AT_PLAYER){
			if(counter == 1){
				curMovement = FOLLOW_PLAYER;
			}
			
			if(counter % curHastighet == 0){
				if(curMode == MODE_SINGLE)
					addLaser(curPekar);
				else if(curMode == MODE_TRIPPLE)
					addTripple();
			}
		}else if(curState == SHOOT_AT_RANDOM){
			if(counter == 1){
				curMovement = BACK_AND_FORTH;
			}
			
			if(counter % curHastighet == 0){
				if(curMode == MODE_SINGLE)
					addLaser(curPekar);
				else if(curMode == MODE_TRIPPLE)
					addTripple();
			}
		}else if(curState == SHOOT_ALOT){
			if(counter == 1){
				curMovement = GOTO;
				isGoto = true;
				gotoTarget = 270;
			}else if(counter == 2){
				if(isGoto){
					counter--;
				}else{
					counter++;
				}
			}else if(counter == 120){
				canChange = true;
				setIdle();
			}else{
				if(counter % 5 == 0){
					double ang = Math.random()*(maxRight-maxLeft)+maxLeft;
					setPekar(ang);
					addLaser(curPekar);
				}
			}
		}
		
		//movement
		if(curMovement == FOLLOW_PLAYER){
			double target = calcDir(player); 
			double delta = Calc.getShortestAngleDistance(target, curPekar);
			if(Math.abs(delta) > turnSpeed){
				delta = delta < 0 ? -turnSpeed : turnSpeed;
			}
			setPekar(curPekar+delta);
		}else if(curMovement == BACK_AND_FORTH){ 
			double delta = Calc.getShortestAngleDistance(idleTarget ? maxRight : maxLeft, curPekar);
			if(Math.abs(delta) > turnSpeed){
				delta = delta < 0 ? -turnSpeed : turnSpeed;
			}else{
				idleTarget = !idleTarget;
			}
			setPekar(curPekar+delta);
		}else if(curMovement == GOTO){
			double delta = Calc.getShortestAngleDistance(gotoTarget, curPekar);
			if(Math.abs(delta) > turnSpeed){
				delta = delta < 0 ? -turnSpeed : turnSpeed;
			}else{
				isGoto = false;
				curMovement = STILL;
			}
			setPekar(curPekar+delta);
		}
		
	}
	
	private void addSpartan(){
		EnemyProjectileLaser a = new SpartanLaser(tileMap);
		Point2D.Double tip = getTip();
		a.setPosition(tip.getX(), tip.getY());
		a.setAngle(curPekar);
		a.setDamage(5);
		a.setExpiration(120);
		ep.add(a);
	}
	
	private void addLaser(double ang){
		EnemyProjectileLaser a = new EnemyProjectileLaser(tileMap, 10, 20);
		Point2D.Double tip = getTip();
		a.setPosition(tip.getX(), tip.getY());
		a.setAngle(ang);
		a.setDamage(1);
		a.setSpeed(2);
		a.shoot();
		ep.add(a);
	}
	
	private void addTripple(){
		addLaser(curPekar);
		addLaser(curPekar+30);
		addLaser(curPekar-30);
	}
	
	private Point2D.Double getTip(){
		//return new Point2D.Double(x+Math.cos(Math.toRadians(curPekar))*29.83, y-Math.sin(Math.toRadians(curPekar))*29.83);
		return Calc.getCirclePoint(x, y, 29.83, curPekar);
	}
	
	@Override
	public void draw(Graphics2D g) {
		setMapPosition();
		if(notOnScreen()) return;
		drawRotate(g, curPekar-standardPekar);
		
		//spartanline
		if(spartanLine){
			Point2D.Double tip = getTip();
			g.setColor(Color.RED);
			g.drawLine((int)(tip.getX()+xmap), (int)(tip.getY()+ymap), (int)(xmap+x+Math.cos(Math.toRadians(curPekar))*600), (int)(ymap+y-Math.sin(Math.toRadians(curPekar))*600));
		}
		//super.draw(g);
		
		/*
		setMapPosition();
		g.setColor(Color.BLACK);
		g.fillRect((int)(x+xmap-width/2), (int)(y+ymap-height/2), width, height);
		*/
	}

}
