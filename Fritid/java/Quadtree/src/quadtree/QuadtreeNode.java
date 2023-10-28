package quadtree;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

class QuadtreeNode {

	private ArrayList<QuadtreeNode> children;
	private QuadtreeNode parent;
	private Rectangle2D.Double bounds;
	private ArrayList<QuadObject> objects;
	private int level;
	private int childrenCount; //antalet element i alla children
	
	QuadtreeNode(int level, Rectangle2D.Double bounds){
		this.level = level;
		this.bounds = bounds;
		objects = new ArrayList<QuadObject>();
		childrenCount = 0;
	}
	
	private QuadtreeNode(int level, Rectangle2D.Double bounds, QuadtreeNode parent){
		this(level, bounds);
		this.parent = parent;
	}
	
	void clear(){
		childrenCount = 0;
		for(QuadObject qo : objects) qo.clearParent();
		objects.clear();
		
		if(!isLeaf()){
			for(int i = 0; i < 4; i++){
				children.get(i).clear();
			}
			children.clear();
			children = null;
		}
	}
	
	void add(QuadObject obj, int maxObjects, int maxLevel){
		int ind = getIndex(obj.getBounds());
		if(ind == -1){
			objects.add(obj);
			obj.setParent(this);
		}else{
			children.get(ind).add(obj, maxObjects, maxLevel);
			childrenCount++;
		}
		
		if(isLeaf() && objects.size() > maxObjects && level < maxLevel){
			split(maxObjects, maxLevel);
		}
	}
	
	void retrieve(Rectangle2D.Double rect, ArrayList<QuadObject> returnList){
		if(!isLeaf()){
			int ind = getIndex(rect);
			if(ind != -1){
				children.get(ind).retrieve(rect, returnList);
			}else{
				for(int i = 0; i < 4; i++){
					children.get(i).retrieve(rect, returnList);
				}
			}
		}
		
		returnList.addAll(objects);
		
	}

	void removeObject(QuadObject obj, int maxObjects){
		for(int i = 0; i < objects.size(); i++){
			if(obj == objects.get(i)){
				objects.remove(i);
				obj.clearParent();
				break;
			}
		}
		
		//fixa childrenCount
		if(!isRoot())
			parent.changeChildrenCount(-1);
		
		//fixa desplit
		checkDesplit(maxObjects);
	}
	
	void updateObject(QuadObject obj, QuadtreeNode root, int maxObjects, int maxLevel){
		boolean inThis = this.contains(obj.getBounds());
		int ind = getIndex(obj.getBounds());
		
		if(inThis && ind == -1){
			//är i samma ruta som förut
		}else{
			removeObject(obj, maxObjects);
			root.add(obj, maxObjects, maxLevel);
		}
		
	}
	
	private void checkDesplit(int maxObjects){
		if(!isLeaf() && getTotalElements() <= maxObjects){
			boolean allLeaf = true;
			for(int i = 0; i < 4; i++){
				if(!children.get(i).isLeaf()){
					allLeaf = false;
					break;
				}
			}
			
			if(allLeaf){
				desplit();
			}
		}
		
		if(isLeaf() && !isRoot()){
			parent.checkDesplit(maxObjects);
		}
	}
	
	private void changeChildrenCount(int d){
		this.childrenCount += d;
		if(!isRoot()) this.parent.changeChildrenCount(d);
	}
	
	private boolean contains(Rectangle2D.Double rect){
		return bounds.contains(rect);
	}
	
	private int getIndex(Rectangle2D.Double rect){
		if(isLeaf()) return -1;
		
		for(int i = 0; i < 4; i++){
			if(children.get(i).contains(rect)){
				return i;
			}
		}
		
		return -1;
	}
	
	private void desplit(){
		for(int child = 0; child < 4; child++){
			ArrayList<QuadObject> objs = children.get(child).objects;
			for(QuadObject qo : objs) qo.setParent(this);
			objects.addAll(objs);
		}
		
		children.clear();
		children = null;
		
		childrenCount = 0;
	}
	
	private void split(int maxObjects, int maxLevel){
		//skapa nya
		children = new ArrayList<QuadtreeNode>(4);
		
		double w = bounds.width/2;
		double h = bounds.height/2;
		
		children.add(new QuadtreeNode(level+1, new Rectangle2D.Double(bounds.x, bounds.y, w, h), this));
		children.add(new QuadtreeNode(level+1, new Rectangle2D.Double(bounds.x+w, bounds.y, w, h), this));
		children.add(new QuadtreeNode(level+1, new Rectangle2D.Double(bounds.x, bounds.y+h, w, h), this));
		children.add(new QuadtreeNode(level+1, new Rectangle2D.Double(bounds.x+w, bounds.y+h, w, h), this));
		
		//reinsert gamla
		ArrayList<QuadObject> old = objects;
		objects = new ArrayList<QuadObject>();
		
		for(QuadObject t : old) add(t, maxObjects, maxLevel);
	}
	
	private boolean isLeaf(){return children == null;}
	
	ArrayList<QuadtreeNode> getChildren(){return children;}
	ArrayList<QuadObject> getObjects(){return objects;}
	Rectangle2D.Double getBounds(){return bounds;}
	int getTotalElements(){return childrenCount + objects.size();}
	boolean isRoot(){return parent == null;}
	
}
