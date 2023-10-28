package ppong;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

public class GameObject {

	protected double x, y, width, height, vx, vy, speed;
	protected Color color;
	protected String name;
	protected Rectangle2D.Double hitbox;
	
	/**
	 * constructor
	 * 
	 * @param name
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param color
	 */
	public GameObject(String name, double x, double y, double width, double height, Color color) {
		super();
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		this.color = color;
		this.name = name;
		hitbox = new Rectangle2D.Double(x, y, width, height);
	}
	
	/**
	 * returnar hitboxen för objektet.
	 * @return
	 */
	public Rectangle2D.Double getHitbox(){
		hitbox.x = x;
		hitbox.y = y;
		return hitbox;
	}
	
	/**
	 * kollar ifall detta gameobjekt överlappar med go.
	 * @param go
	 * @return
	 */
	public boolean intersects(GameObject go){
		return getHitbox().intersects(go.getHitbox());
	}
	
	/**
	 * sätter hastigheterna
	 * @param vx
	 * @param vy
	 */
	public void setVelocities(double vx, double vy){
		this.vy = vy;
		this.vx = vx;
	}
	
	/**
	 * sätter positionen för detta objekt
	 * @param x
	 * @param y
	 */
	public void setPos(double x, double y){
		this.y = y;
		this.x = x;
	}
	
	/**
	 * sätter hastigheten i x-led
	 * @param vx
	 */
	public void setVx(double vx){this.vx = vx;}
	
	/**
	 * sätter hastigheyten i y-led
	 * @param vy
	 */
	public void setVy(double vy){this.vy = vy;}
	
	/**
	 * får färdriktningen av detta objekt i grader. 0 är till höger och 180 är till vänster, precis som i enhetscirkeln. 
	 * @return
	 */
	public double getDir(){
		return calcDir(vx, vy, speed);
	}
	
	//från GA
	/**
	 * sätter detta objekt i mitten av en enhetscirkel.
	 * kollar sedan vilken "absolutvinkel" detta objekt har mot en punkt
	 * som ligger vid delta x och delta y i en cirkel med radien radius
	 * funkar bra att kolla vilken riktning man färdas i.
	 * 
	 * @param x
	 * @param y
	 * @param radius
	 * @return
	 */
	public static double calcDir(double x, double y, double radius){
		double t = x/(radius == 0 ? 1 : radius);
		double cosV1 = Math.toDegrees(Math.acos(t));
		double cosV2 = Math.toDegrees(-Math.acos(t) + 2*Math.PI);
		
		boolean down = y > 0;
		
		return down ? cosV2 : cosV1;
	}
	
	/**
	 * uppdaterar objektet en tidsenhet. flyttar på den. 
	 */
	public void tick(){
		x += vx;
		y += vy;
		
		//draw();
		//System.out.println(name+" "+x+" "+y);
	}
	
	/**
	 * ritar objekt.
	 * @param g
	 */
	public void draw(Graphics g){
		g.setColor(Color.WHITE);
		g.fillRect((int)(x*PongGame.SCALE), (int)(y*PongGame.SCALE), (int)(width*PongGame.SCALE), (int)(height*PongGame.SCALE));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GameObject other = (GameObject) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}
