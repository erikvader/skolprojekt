package quadtree;

import java.awt.geom.Rectangle2D;

public class QuadObject {

	private Rectangle2D.Double bounds;
	private QuadtreeNode parent;
	
	public QuadObject(Rectangle2D.Double bounds){
		this.bounds = bounds;
	}
	
	public QuadObject(double x, double y, double w, double h){
		bounds = new Rectangle2D.Double(x, y, w, h);
	}

	/**
	 * med andra ord attached to a tree.
	 * @return
	 */
	public boolean hasParent(){return parent != null;}
	
	void setParent(QuadtreeNode parent){this.parent = parent;}
	QuadtreeNode getParent(){return parent;}
	void clearParent(){setParent(null);}
	
	public Rectangle2D.Double getBounds(){return bounds;}
	public void setBounds(Rectangle2D.Double bounds){this.bounds.setRect(bounds);}
	public void setBounds(double x, double y, double w, double h){bounds.setRect(x, y, w, h);}
	public void translate(double dx, double dy){bounds.x += dx; bounds.y += dy;}

	@Override
	public String toString() {
		return "QuadObject [bounds=" + bounds + "]";
	}
	
	
}
