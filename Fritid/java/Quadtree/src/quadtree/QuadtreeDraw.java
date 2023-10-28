package quadtree;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

public class QuadtreeDraw {

	public static void painTree(Graphics g, Quadtree tree){
		paintNode(g, tree.root);
	}
	
	private static void paintNode(Graphics g, QuadtreeNode tree){
		Rectangle.Double bounds = tree.getBounds();
		ArrayList<QuadObject> objects = tree.getObjects();
		ArrayList<QuadtreeNode> children = tree.getChildren();
		
		g.setColor(Color.BLACK);
		g.drawRect((int)bounds.x, (int)bounds.y, (int)bounds.width, (int)bounds.height);
		
		g.setColor(Color.RED);
		for(int i = 0; i < objects.size(); i++){
			Rectangle.Double box = objects.get(i).getBounds();
			g.fillRect((int)box.x, (int)box.y, (int)box.width, (int)box.height);
		}
		
		if(children != null){
			for(int i = 0; i < children.size(); i++){
				paintNode(g, children.get(i));
			}
		}
	}
}
