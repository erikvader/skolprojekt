package btd;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import avl.AVLtree;
import avl.VisualTree;

public class Main {

	public static void main(String[] args) {
		
		//tree
		AVLtree<Integer> tree = new AVLtree<Integer>();
		for(int i = 0; i < 20; i++){
			tree.add(i);
		}
		VisualTree<Integer> vtree = tree.getVisualTree();
		
		//window
		JFrame win = new JFrame("drawdrawdraw");
		win.setSize(600, 600);
		win.add(new Panel(vtree), BorderLayout.CENTER);
		win.setLocationRelativeTo(null);
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		win.setVisible(true);
		
	}

}
