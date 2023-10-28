package quadtree;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Quadtree {
	
	private static final int MAX_LEVEL_DEFAULT = 5;
	private static final int MAX_OBJECTS_DEFAULT = 1;
	private int maxLevel;
	private int maxObjects;
	
	public static final int TOP_LEFT = 0;
	public static final int TOP_RIGHT = 1;
	public static final int BOTTOM_LEFT = 2;
	public static final int BOTTOM_RIGHT = 3;
	
	QuadtreeNode root;
	
	public Quadtree(Rectangle2D.Double bounds, int maxLevel, int maxObjects){
		root = new QuadtreeNode(1, bounds);
		this.maxLevel = maxLevel;
		this.maxObjects = maxObjects;
	}
	
	public Quadtree(Rectangle2D.Double bounds){
		this(bounds, MAX_LEVEL_DEFAULT, MAX_OBJECTS_DEFAULT);
	}
	
	public Quadtree(double x, double y, double w, double h){
		this(x, y, w, h, MAX_LEVEL_DEFAULT, MAX_OBJECTS_DEFAULT);
	}
	
	public Quadtree(double x, double y, double w, double h, int maxLevel, int maxObjects){
		this(new Rectangle2D.Double(x, y, w, h), maxLevel, maxObjects);
	}
	
	/**
	 * Clearar hela quadtree:t. Tar bort hela trädet samt rensar alla parent-pointers från QuadObject:en.
	 */
	public void clear(){
		root.clear();
	}
	
	/**
	 * Tar bort det angivna objektet ur trädet.
	 * @param obj
	 */
	public void removeObject(QuadObject obj){
		obj.getParent().removeObject(obj, maxObjects);
	}
	
	/**
	 * lägger till ett QuadObject på rätt ställe. Splittar ifall det behövs.
	 * @param obj
	 */
	public void add(QuadObject obj){
		root.add(obj, maxObjects, maxLevel);
	}
	
	/**
	 * Hämtar alla QuadObjects lagrade i det här trädet som skulle kunna intersecta med rect.
	 * @param rect
	 * @param returnList
	 */
	public void retrieve(Rectangle2D.Double rect, ArrayList<QuadObject> returnList){
		root.retrieve(rect, returnList);
	}
	
	public void retrieve(QuadObject obj, ArrayList<QuadObject> returnList){
		root.retrieve(obj.getBounds(), returnList);
	}
	
	/**
	 * Uppdaterar obj:s position i trädet ifall den har flyttat på sig.
	 * @param obj
	 */
	public void updateObject(QuadObject obj){
		obj.getParent().updateObject(obj, root, maxObjects, maxLevel);
	}
	
	public Rectangle2D.Double getBounds(){return root.getBounds();}
	public int getTotalElements(){return root.getTotalElements();}
}
