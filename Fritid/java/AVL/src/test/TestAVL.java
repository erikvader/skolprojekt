package test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import avl.AVLtree;

public class TestAVL {

	public static void main(String[] args) {
		new TestAVL().run();
	}
	
	public void run(){
		
		for(int i = 0; i < 5; i++){
			if(!runTests()){
				break;
			}
		}
		
	}
	
	public boolean runTests(){
		if(test1()){
			System.out.println("test1 lyckades :D");
		}else{
			System.out.println("test1 misslyckades :((((((((((");
			return false;
		}
		
		if(test2()){
			System.out.println("test2 lyckades :D");
		}else{
			System.out.println("test2 misslyckades :((((((((((");
			return false;
		}
		
		if(test3()){
			System.out.println("test3 lyckades :D");
		}else{
			System.out.println("test3 misslyckades :((((((((((");
			return false;
		}
		
		if(test4()){
			System.out.println("test4 lyckades :D");
		}else{
			System.out.println("test4 misslyckades :((((((((((");
			return false;
		}
		
		return true;
	}
	
	//raaaandooooom!
	public boolean test4(){
		AVLtree<Integer> tree = new AVLtree<Integer>();
		ArrayList<Integer> list = new ArrayList<Integer>();
		int t = 5000;
		
		for(int i = 0; i < t; i++){
			int action = (int)(Math.random()*4);
			if(tree.isEmpty() || action == 0 || action == 3){
				int r = (int)(Math.random()*1000-500);
				tree.add(r);
				list.add(r);
				Collections.sort(list);
			}else if(action == 1){
				int r = (int)(Math.random()*1000-500);
				tree.remove((Integer)r);
				list.remove((Integer)r);
			}else if(action == 2){
				int r = (int)(Math.random()*tree.size());
				tree.remove(r);
				list.remove(r);
			}
			if(!validateState(tree, list)) return false;
		}
		
		return true;
	}
	
	//removing
	public boolean test3(){
		AVLtree<Integer> tree = new AVLtree<Integer>();
		ArrayList<Integer> list = new ArrayList<Integer>();
		int t = 1000;
		
		fillRandom(tree, list, t);
		
		//by index
		for(int i = 0; i < t; i++){
			int r = (int)(Math.random()*tree.size());
			tree.remove(r);
			list.remove(r);
			if(!validateState(tree, list)) return false;
		}
		
		//by value
		fillRandom(tree, list, t);
		for(int i = 0; i < t; i++){
			int r = (int)(Math.random()*tree.size());
			Integer arrrr = tree.get(r);
			tree.remove(arrrr);
			list.remove(arrrr);
			if(!validateState(tree, list)) return false;
		}
		
		return true;
	}
	
	//getting
	public boolean test2(){
		AVLtree<Integer> tree = new AVLtree<Integer>();
		ArrayList<Integer> list = new ArrayList<Integer>();
		int t = 1000;
		
		fillRandom(tree, list, t);
		
		//contains
		for(int i = 0; i < 2*t; i++){
			int r = (int)(Math.random()*2*t-t);
			if(tree.contains(r) != list.contains(r)) return false;
		}
		
		//get
		for(int i = 0; i < 10*t; i++){
			int r = (int)(Math.random()*t);
			Integer a = tree.get(r);
			Integer b = list.get(r);
			if(!a.equals(b)){
				return false;
			}
		}
		
		return true;
	}
	
	//adding
	public boolean test1(){
		AVLtree<Integer> tree = new AVLtree<Integer>();
		ArrayList<Integer> list = new ArrayList<Integer>();
		int t = 1000;
		
		//increasing
		for(int i = 0; i < t; i++){
			tree.add(i);
			list.add(i);
			if(!validateState(tree, list)) return false;
		}
		
		tree.clear();
		list.clear();
		
		//decreasing
		for(int i = t; i >= 0; i--){
			tree.add(i);
			list.add(i);
			Collections.sort(list);
			if(!validateState(tree, list)) return false;
		}
		
		tree.clear();
		list.clear();
		
		//random
		for(int i = 0; i < t; i++){
			int r = (int)(Math.random()*t);
			tree.add(r);
			list.add(r);
			Collections.sort(list);
			if(!validateState(tree, list)) return false;
		}
		
		return true;
		
	}
	
	public void fillRandom(AVLtree<Integer> at, ArrayList<Integer> al, int t){
		//random
		for(int i = 0; i < t; i++){
			int r = (int)(Math.random()*t);
			at.add(r);
			al.add(r);
		}
		Collections.sort(al);
	}
	
	public boolean validateState(AVLtree<Integer> at, ArrayList<Integer> al){
		return isCorrect(at) && equals(at, al);
	}
	
	public boolean equals(AVLtree<Integer> at, ArrayList<Integer> al){
		ArrayList<Integer> a = at.getSorted();
		if(a.size() != al.size()) return false;
		
		for(int i = 0; i < a.size(); i++){
			if(!a.get(i).equals(al.get(i))) return false;
		}
		
		return true;
	}
	
	public boolean isCorrect(AVLtree<Integer> at){
		ArrayList<Integer> l = at.getSorted();
		if(!l.isEmpty()){
			boolean isIncreasing = true;
			Iterator<Integer> ite = l.iterator();
			int last = ite.next();
			int cur;
			while(ite.hasNext()){
				cur = ite.next();
				if(cur < last){
					isIncreasing = false;
					break;
				}
				last = cur;
			}
			/*
			boolean b = at.validateAVL();
			if(!b){
				System.out.println("hej");
			}
			*/
			return isIncreasing && at.validateAVL();
		}else{
			return true;
		}
	}

}

