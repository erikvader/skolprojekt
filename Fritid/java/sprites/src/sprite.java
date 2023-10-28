import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;


public class sprite {

	
	public String name;
	public int x;
	public int y;
	public int vx;
	public int vy;
	public int width;
	public int height;
	public int[] hitBoxw;
	public int[] hitBoxh;
	public int[][] oldHitBox = new int[2][]; 
	public int[] hitBox = new int[4];
	public Image spriteImage;
	
	//initialization functions
	public sprite(String n, int hitw, int hith, int velx, int vely, int xpos, int ypos){
		name = n;
		width = hitw;
		height = hith;
		vx = velx;
		vy = vely;
		x = xpos;
		y = ypos;
		
		oldSetHitbox();
	}
	
	//set functions
	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	public void setVX(int vx){
		this.vx = vx;
	}
	
	public void setVY(int vy){
		this.vy = vy;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void oldSetHitboxWidth(int width){
		this.width = width;
	}
	
	public void oldSetHitboxHeight(int height){
		this.height = height;
	}
	
	public void oldSetHitbox(){
		hitBoxw = new int[width];
		hitBoxh = new int[height];
		
		int ind = 0;
		for(int w = x ; w<width+x ; w++){
			hitBoxw[ind++] = w;
		}
		ind = 0;
		for(int h = y ; h<height+y ; h++){
			hitBoxh[ind++] = h;
		}
		
		oldHitBox[0] = hitBoxw;
		oldHitBox[1] = hitBoxh;
		
	}
	
	public void setHitbox(){
		hitBox[0] = x;
		hitBox[1] = y;
		hitBox[2] = x+width;
		hitBox[3] = y+height;
		
	}
	
	public void setImage(ImageIcon i){
		spriteImage = i.getImage();
	}
	
	//get functions
	public int[] getHitbox(){
		return hitBox;
	}
	
	public int[][] oldGetHitbox(){
		return oldHitBox;
	}
	
	public int[] oldGetHitboxw(){
		for(int p : hitBoxw){
			System.out.println("width "+p);
		}
		return hitBoxw;
		
	}
	
	public int[] oldGetHitboxh(){
		for(int p : hitBoxh){
			System.out.println("height "+p);
		}
		return hitBoxh;
	}
	
	public int getVX(){
		return vx;
	}
	
	public int getVY(){
		return vy;
	}
	
	public String getName(){
		return name;
	}
	
	//hit stuff functions
	public boolean oldHitSprite(int[][] hx2){
		for (int a = 0; a < hx2[0].length; a+=1) {
			for (int i = 0; i < oldHitBox[0].length; i+=1) {
				if(oldHitBox[0][i] == hx2[0][a]){
					// check y axis
					for(int b = 0 ; b<hx2[1].length ; b++){
						for(int c = 0 ; c<oldHitBox[1].length ; c++){
							if(oldHitBox[1][c] == hx2[1][b]){
								return true;
							}
						}
					}
				}
			}
		}
		
		return false;
	}
	public boolean hitLeft(){
		if(x <= 0){
			return true;
		}
		return false;
	}
	
	public boolean hitRight(int w){
		if(x + width >= w){
			return true;
		}
		return false;
	}
	
	public boolean hitTop(){
		if(y <= 0){
			return true;
		}
		return false;
	}
	
	public boolean hitBottom(int h){
		if(y + height >= h){
			return true;
		}
		return false;
	}
	
	public boolean hitSide(int w, int h){
		if(hitLeft()){
			return true;
		}
		if(hitRight(w)){
			return true;
		}
		if(hitTop()){
			return true;
		}
		if(hitBottom(h)){
			return true;
		}
		return false;
	}
	
	//misc functions
	public void move(){
		x += vx;
		y += vy;
		oldGetHitbox();
	}
	
	public void print(boolean v, Graphics g){
		if(v){
			g.drawImage(spriteImage, x, y, null);
		}
	}
	
	public void printHitbox(boolean v, Graphics g){
		if(v){
			g.drawRect(x, y, width, height);
		}
	}
	
	public String toString(){
		return name;
	}
}
