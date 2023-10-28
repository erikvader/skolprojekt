import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class spriteCollection {

	public List<sprite> sprites = new ArrayList<sprite>();

	public void add(sprite s) {
		sprites.add(s);
	}

	public void delete(sprite s) {
		sprites.remove(s);
	}

	public sprite getSprite(int index) {
		return sprites.get(index);
	}

	public List<sprite> getList() {
		return sprites;
	}

	public void resetList() {
		sprites = new ArrayList<sprite>();
	}

	public boolean checkCollision(int[][] hx1, int[][] hx2) {
		// check x axis
		// System.out.println("new checkCollison");
		if (hx1[2][0] == 0 && hx2[2][0] == 0) { // check for ignoreHitbox
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
		}

		return false;
	}

	private List<sprite> deleteDuplicates(List<sprite> l) {
		List<sprite> d = new ArrayList<sprite>();

		for (int i = 0; i < l.size(); i++) {
			if (check(l.get(i).getName(), d)) {//kollar om det är fritt frama tt lägga till den(det finns ingen som redan heter så)
				d.add(l.get(i));
			}
		}

		return d;
	}

	//kollar om det finns något som redan heter så
	private boolean check(String n, List<sprite> d) {
		for (int i = 0; i < d.size(); i++) {
			if(n.equals(d.get(i).getName())){
				return false;
			}
		}

		return true;
	}

	//returnar en table med alla sprites som krockar med en annan
	public sprite[] checkAllCollision() {
		List<sprite> returnList = new ArrayList<sprite>();
		sprite[] table = new sprite[0];
		
		for (int d = 0; d < sprites.size(); d++) {
			for (int e = 0; e < sprites.size(); e++) {
				if (sprites.get(d).getName().equals(sprites.get(e).getName()) == false) {
					if (checkCollision(sprites.get(d).getHitbox(), sprites.get(e).getHitbox())) {
						returnList.add(sprites.get(d));
						returnList.add(sprites.get(e));
					}
				}
			}
		}

		if (returnList.size() != 0) {
			List<sprite> listlist = deleteDuplicates(returnList);
			table = listlist.toArray(new sprite[listlist.size()]);
		}
		return table.length == 0 ? null : table;
	}

	public void checkAllCollisionAutoBounce() {
		sprite[] tempTable = checkAllCollision();
		if (tempTable != null) {
			for (int i = 0; i < tempTable.length; i++) {
				tempTable[i].setVX(tempTable[i].vx *= -1);
				tempTable[i].setVY(tempTable[i].vy *= -1);
			}
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

	public sprite checkAllMouseClick(int xpos, int ypos) {
		for (int i = 0; i < sprites.size(); i++) {
			if (sprites.get(i).hitMouse(xpos, ypos)) {
				return sprites.get(i);
			}
		}
		return null;
	}

}
