package btd;

import java.awt.Graphics;

import javax.swing.JPanel;

import avl.VisualTree;

public class Panel extends JPanel{

	private static final long serialVersionUID = 1L;

	private RectTree tree;
	public static final int spaceing = 8;
	public static final double ratio = 0.3;
	
	public Panel(VisualTree<Integer> tree){
		super();
		this.tree = generateRectTree(tree);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int x = getWidth()/2 - tree.w/2;
		int y = getHeight()/2 - tree.h/2;
		tree.draw(g, x, y);
		
	}
	
	public RectTree generateRectTree(VisualTree<Integer> vt){
		RectTree nrt = new RectTree();
		if(vt.getLeft() == null && vt.getRight() == null){
			nrt.x = 0;
			nrt.y = 0;
			nrt.w = RectTree.nodew;
			nrt.h = RectTree.nodeh;
			nrt.nx = 0;
		}else{
			nrt.x = 0;
			nrt.y = 0;
		
			RectTree lrt, rrt;
			if(vt.getLeft() != null){
				lrt = generateRectTree(vt.getLeft());
				nrt.left = lrt;
			}else{
				lrt = new RectTree(0, 0, 0, 0);
				nrt.left = null;
			}
			
			if(vt.getRight() != null){
				rrt = generateRectTree(vt.getRight());
				nrt.right = rrt;
			}else{
				rrt = new RectTree(0, 0, 0, 0);
				nrt.right = null;
			}
			
			int widest = Math.max(lrt.w, rrt.w);
			
			nrt.w = spaceing + 2*widest;
			nrt.h = (int)(nrt.w * ratio) + RectTree.nodeh + Math.max(lrt.h, rrt.h);
			nrt.nx = widest + spaceing/2 - RectTree.nodew/2;
			
			lrt.x = widest/2 - lrt.w/2;
			lrt.y = rrt.y = RectTree.nodeh + (int)(nrt.w * ratio); 
			rrt.x = widest + spaceing;
		}
		
		nrt.label = vt.getFirstLabel().toString();
		return nrt;
	}
	
}
