package game.entity.level3_boss;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import game.entity.Animation;
import game.entity.Player;
import game.entity.enemies.enemyProjectile.EnemyProjectile;
import game.entity.enemies.enemyProjectile.patterns.PatternBeam1;
import game.entity.enemies.enemyProjectile.patterns.PatternBeam2;
import game.entity.enemies.enemyProjectile.patterns.PatternCircle;
import game.entity.enemies.enemyProjectile.patterns.PatternRing;
import game.entity.enemies.enemyProjectile.patterns.PatternSpindelben;
import game.handlers.Calc;
import game.handlers.Content;
import game.tileMap.Section;
import game.tileMap.TileMap;

public class Kylare extends EnemyProjectile{

	private ArrayList<EnemyProjectile> ep;
	private Player player;
	
	private boolean begin = false;
	
	private int counter = 0;
	
	private Point atJohannes;
	//private Point atMarken;
	
	private double curPekar = 0;
	
	//movement
	public static final int STILL = 0;
	public static final int BACK_AND_FORTH = 1;
	public static final int SPIN = 2;
	
	private int curMovement = BACK_AND_FORTH;
	
	//grejer till back and forth
	private double maxLeft = 20, maxRight = -20;
	private final double orgTurnSpeed = 0.5;
	private double turnSpeed = orgTurnSpeed;
	private boolean idleTarget = false; //false är maxLeft och true är maxRight
	
	//phases
	public static final int NOTHING = 0;
	public static final int SPECIAL_ATTACK_1 = 1;
	
	private int curPhase = NOTHING;
	
	private boolean canChange = true; //betyder rätt så mycket "upptagen med annat"
	
	//shootSpindel
	private double[] xPunkter;
	
	public Kylare(TileMap tm, ArrayList<EnemyProjectile> ep, Player player) {
		super(tm);
		
		this.ep = ep;
		this.player = player;
		
		width = cwidth = 50;
		height = cheight = 70;
		
		animation = new Animation(Content.loadRow("/Resources/Sprites/Enemies/johannes/kyl.png", 50, 70, 0, 1), -1);
		
		setHitOnPlayer = false;
		setHitOnCollideMap = false;
		removeIfOutsideSection = false;
		checkCollisionWithMap = false;
		
		damage = 1;
		moveSpeed = 2.3;
		
		atJohannes = new Point(324, 110);
		//atMarken = new 
		
		fillXPunkter();
	}
	
	private void fillXPunkter(){
		int antal = 5;
		double right = 15*30-5.5;
		double left = 1*30+5.5+1;
		double step = (right-left)/antal;
		xPunkter = new double[antal+1];
		for(int i = 0; i <= antal; i++){
			xPunkter[i] = left+step*i;;
		}
	}
	
	public void begin(){
		begin = true;
		setIdle();
	}
	
	public void unBegin(){
		begin = false;
	}
	
	public void setIdle(){
		if(curPhase != NOTHING && canChange){
			curPhase = NOTHING;
			counter = 0;
			curMovement = BACK_AND_FORTH;
		}
	}
	
	public void shootAtPlayer(){
		if(!canChange) return;
		spawnCoke(2).shootTarget(player.getx(), player.gety());
	}
	
	public void shootCircle(){
		if(!canChange) return;
		PatternCircle cp = new PatternCircle(tileMap, Content.cokeZero, 10, 11, 15, 2, ep);
		cp.setPosition(x, y);
		cp.setHitOnPlayer = false;
		cp.shootCircle(40, 16, 50, player);
		ep.add(cp);
	}
	
	public void shootBeam(){
		if(!canChange) return;
		PatternBeam1 cp = new PatternBeam1(tileMap, Content.cokeZero, 10, 11, 15, 2, ep);
		cp.setPosition(x, y);
		cp.setHitOnPlayer = false;
		cp.shootBeam(player.getx(), player.gety(), 6, 1.5, 0.4);
		ep.add(cp);
	}
	
	public void shootBeam2(){
		if(!canChange) return;
		PatternBeam2 cp = new PatternBeam2(tileMap, Content.cokeZero, 10, 11, 15, 2, ep);
		cp.setPosition(x, y);
		cp.setHitOnPlayer = false;
		cp.shootBeam(player, 9, 1.5, 0.5, 4);
		ep.add(cp);
	}
	
	public void shootRing(){
		if(!canChange) return;
		PatternRing cp = new PatternRing(tileMap, Content.cokeZero, 10, 11, 15, 1.7, ep);
		cp.setPosition(x, y);
		cp.setHitOnPlayer = false;
		double sAng = Math.random()*30+200;
		cp.shootRing(sAng, 120, 5);
		ep.add(cp);
	}
	
	public void shootSpindel(){
		if(!canChange) return;
		PatternSpindelben cp = new PatternSpindelben(tileMap, Content.cokeZero, 10, 11, 15, 3, ep);
		cp.setPosition(x, y);
		cp.setHitOnPlayer = false;
		cp.shootSpindel(xPunkter, 10*30-7.5, 0.047, 3, 180);
		ep.add(cp);
	}
	
	public void shootSigSelf(){
		if(!canChange) return;
		canChange = false;
		curPhase = SPECIAL_ATTACK_1;
		counter = 0;
	}
	
	private EnemyProjectile spawnCoke(double speed){
		EnemyProjectile e = new EnemyProjectile(tileMap, Content.cokeZero, 10, 11, 15, speed);
		e.setPosition(x, y);
		e.setHitOnPlayer = false;
		ep.add(e);
		return e;
	}
	
	private void setPekar(double ang){
		curPekar = Calc.fixAngle(ang);
	}
	
	public void offerPosition(double x, double y){
		if(!canChange) return;
		setPosition(x, y);
	}
	
	@Override
	public void update(Section s) {
		super.update(s);
		
		if(!begin) return;
		
		counter++;
		
		if(curPhase == SPECIAL_ATTACK_1){
			if(counter == 1){
				shootTarget(player.getx(), player.gety());
				curMovement = SPIN;
			}else if(counter == 220){
				stop();
				setPosition(atJohannes.getX(), -50);
				moveSpeed = 1;
				shootDestination(atJohannes.getX(), atJohannes.getY());
				curMovement = STILL;
				curPekar = 0;
			}else if(counter == 400){
				curMovement = BACK_AND_FORTH;
				canChange = true;
				setIdle();
				moveSpeed = 2.3;
			}
		}
		
		//snurra runt
		if(curMovement == BACK_AND_FORTH){ 
			double delta = Calc.getShortestAngleDistance(idleTarget ? maxRight : maxLeft, curPekar);
			if(Math.abs(delta) > turnSpeed){
				delta = delta < 0 ? -turnSpeed : turnSpeed;
			}else{
				idleTarget = !idleTarget;
			}
			setPekar(curPekar+delta);
		}else if(curMovement == SPIN){
			setPekar(curPekar+5);
		}

	}
	
	@Override
	public void draw(Graphics2D g) {
		setMapPosition();
		if(notOnScreen()) return;
		drawRotate(g, curPekar);
		
		//super.draw(g);
		
		/*
		setMapPosition();
		if(notOnScreen()) return;
		
		g.setColor(Color.BLACK);
		g.fillRect((int)(x+xmap-width/2), (int)(y+ymap-height/2), width, height);
		*/
	}

}
