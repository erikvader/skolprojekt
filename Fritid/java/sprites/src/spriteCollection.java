import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class spriteCollection implements Runnable {

	public List<sprite> sprites = new ArrayList<sprite>();
	public sprite[] returnTable = new sprite[2];

	public void add(sprite s) {
		sprites.add(s);
	}

	public void delete(sprite s) {
		sprites.remove(s);
	}

	public void updateXY(sprite spr, int x, int y) {

	}

	public List<sprite> getList() {
		return sprites;
	}

	public void run() {

	}

	public boolean oldCheckCollision(int[][] hx1, int[][] hx2) {
		// check x axis
		// System.out.println("new checkKollison");
		for (int a = 0; a < hx2[0].length; a += 1) {
			// System.out.println("new loop");
			for (int i = 0; i < hx1[0].length; i += 1) {
				// System.out.printf("1: %d 2: %d\n", hx1[0][i], hx2[0][a]);
				if (hx1[0][i] == hx2[0][a]) {
					// check y axis
					for (int b = 0; b < hx2[1].length; b++) {
						for (int c = 0; c < hx1[1].length; c++) {
							if (hx1[1][c] == hx2[1][b]) {
								return true;
							}
						}
					}
				}
			}
		}

		return false;
	}
	
	public boolean checkCollision(int[] hx1, int[] hx2){
		
		
		return false;
	}

	public sprite[] checkAllCollision() {
		for (int d = 0; d < sprites.size(); d++) {
			for (int e = 0; e < sprites.size(); e++) {
				if (sprites.get(d).getName().equals(sprites.get(e).getName()) == false) {
					if (checkCollision(sprites.get(d).getHitbox(),
							sprites.get(e).getHitbox())) {
						returnTable[0] = sprites.get(d);
						returnTable[1] = sprites.get(e);
						return returnTable;
					}
				}
			}
		}
		return null;
	}

	public void checkAllCollisionAutoBounce() {
		sprite[] tempTable = checkAllCollision();
		if (tempTable != null) {
			tempTable[0].setVX(tempTable[0].vx *= -1);
			tempTable[0].setVY(tempTable[0].vy *= -1);
			tempTable[1].setVX(tempTable[1].vx *= -1);
			tempTable[1].setVY(tempTable[1].vy *= -1);
		}
	}

	public void printAll(Graphics g) {
		for (int p = 0; p < sprites.size(); p++) {
			sprites.get(p).print(true, g);
		}
	}

	public void printAllHitbox(Graphics g) {
		for (int p = 0; p < sprites.size(); p++) {
			sprites.get(p).printHitbox(true, g);
		}
	}

	public sprite checkAllWallCollision(int w, int h) {
		for (int p = 0; p < sprites.size(); p++) {
			if (sprites.get(p).hitBottom(h) || sprites.get(p).hitTop()) {
				return sprites.get(p);
			}
			if (sprites.get(p).hitLeft() || sprites.get(p).hitRight(w)) {
				return sprites.get(p);
			}
		}
		
		return null;
	}

	public void checkAllWallCollisionAutoBounce(int w, int h) {
		for (int p = 0; p < sprites.size(); p++) {
			if (sprites.get(p).hitBottom(h) || sprites.get(p).hitTop()) {
				sprites.get(p).vy *= -1;
			}
			if (sprites.get(p).hitLeft() || sprites.get(p).hitRight(w)) {
				sprites.get(p).vx *= -1;
			}
		}
	}

	public void moveAll() {
		for (int p = 0; p < sprites.size(); p++) {
			sprites.get(p).move();
		}
	}

}
