import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

public class sprite {

	public String name;
	public int x;
	public int y;
	private int startX;
	private int startY;
	public float vx;
	public float vy;
	private float addx;
	private float addy;
	public int width;
	public int height;
	public int[] hitBoxw;
	public int[] hitBoxh;
	public int[][] hitBox = new int[3][1];
	public List<Image> images = new ArrayList<Image>();
	public Image curSpriteImage;
	public boolean ignoreHitbox = false;
	public boolean going = true;

	// initialization functions
	public sprite(String n, float velx, float vely, int xpos, int ypos, boolean ignoreHit){
		this(n, 0, 0, velx, vely, xpos, ypos, ignoreHit);
	}
	
	public sprite(String n, int hitw, int hith, float velx, float vely, int xpos, int ypos, boolean ignoreHit) {
		name = n;
		width = hitw;
		height = hith;
		vx = velx;
		vy = vely;
		startX = xpos;
		startY = ypos;
		x = xpos;
		y = ypos;
		ignoreHitbox = ignoreHit;

		setHitbox();
	}

	// set functions
	public void setIgnoreHitbox(boolean t) {
		ignoreHitbox = t;
	}

	public void setX(int x) {
		startX = x;
		addx = 0;
		this.x = x;
	}

	public void setY(int y) {
		startY = y;
		addy = 0;
		this.y = y;
	}

	public void setVX(float vx) {
		this.vx = vx;
	}

	public void setVY(float vy) {
		this.vy = vy;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setHitboxWidth(int width) {
		this.width = width;
	}

	public void setHitboxHeight(int height) {
		this.height = height;
	}

	public void setHitbox() {
		hitBoxw = new int[width];
		hitBoxh = new int[height];

		int ind = 0;
		for (int w = x; w < width + x; w++) {
			hitBoxw[ind++] = w;
		}
		ind = 0;
		for (int h = y; h < height + y; h++) {
			hitBoxh[ind++] = h;
		}

		// System.out.println(name+" hitbox W: "+hitBox[0].length+" hitbox H: "+hitBox[1].length);
		hitBox[0] = hitBoxw;
		hitBox[1] = hitBoxh;
		hitBox[2][0] = ignoreHitbox ? 1 : 0;

	}
	
	public void setHitboxFromImageDimension(){
		ImageIcon imgimg = new ImageIcon(curSpriteImage);
		width = imgimg.getIconWidth();
		height = imgimg.getIconHeight();
		
		setHitbox();
	}

	public void switchImage(int i) {
		curSpriteImage = images.get(i);
	}

	public void addImage(ImageIcon i) {
		 images.add(i.getImage());
	}
	
	public void setCurImage(ImageIcon i){
		curSpriteImage = i.getImage();
	}
	
	public void setGoing(boolean v){
		going = v;
	}

	// get functions
	public int getCurImageIndex(){
		return images.indexOf(curSpriteImage);
	}
	
	public int[][] getHitbox() {
		return hitBox;
	}

	public int[] getHitboxw() {
		for (int p : hitBoxw) {
			System.out.println("width " + p);
		}
		return hitBoxw;

	}

	public int[] getHitboxh() {
		for (int p : hitBoxh) {
			System.out.println("height " + p);
		}
		return hitBoxh;
	}

	public float getVX() {
		return vx;
	}

	public float getVY() {
		return vy;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public String getName() {
		return name;
	}

	// hit stuff functions
	public boolean hitMouse(int mx, int my) {
		if (ignoreHitbox == false) {
			for (int a = 0; a < hitBox[0].length; a++) {
				if (hitBox[0][a] == mx) {
					for (int b = 0; b < hitBox[1].length; b++) {
						if (hitBox[1][b] == my) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public boolean hitSprite(int[][] hx2) {
		if (ignoreHitbox == false && hx2[2][0] == 0) {
			for (int a = 0; a < hx2[0].length; a += 1) {
				for (int i = 0; i < hitBox[0].length; i += 1) {
					if (hitBox[0][i] == hx2[0][a]) {
						// check y axis
						for (int b = 0; b < hx2[1].length; b++) {
							for (int c = 0; c < hitBox[1].length; c++) {
								if (hitBox[1][c] == hx2[1][b]) {
									return true;
								}
							}
						}
					}
				}
			}
		}

		return false;
	}

	public boolean hitLeft() {
		if (x <= 0) {
			return true;
		}
		return false;
	}

	public boolean hitRight(int w) {
		if (x + width >= w) {
			return true;
		}
		return false;
	}

	public boolean hitTop() {
		if (y <= 0) {
			return true;
		}
		return false;
	}

	public boolean hitBottom(int h) {
		if (y + height >= h) {
			return true;
		}
		return false;
	}

	public boolean hitSide(int w, int h) {
		if (hitLeft()) {
			return true;
		}
		if (hitRight(w)) {
			return true;
		}
		if (hitTop()) {
			return true;
		}
		if (hitBottom(h)) {
			return true;
		}
		return false;
	}

	// misc functions
	public void move() {
		if (going) {
			x = startX;
			y = startY;
			addx += vx;
			addy += vy;
			x += Math.round(addx);
			y += Math.round(addy);
			setHitbox();
		}
	}

	public void print(boolean v, Graphics g) {
		if (v) {
			g.drawImage(curSpriteImage, x, y, null);
		}
	}

	public void printHitbox(boolean v, Graphics g) {
		if (v) {
			g.drawRect(x, y, width, height);
		}
	}
	
	/*public void flipImage(){
		
		AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
		tx.translate(0, -curSpriteImage.getHeight(null));
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		curSpriteImage = op.filter(curSpriteImage, null);
	}*/

	public String toString() {
		return name;
	}
}
