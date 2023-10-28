package test;

import binomialHeap.BinomialHeap;

public class TestBinomiHeap {

	public static void main(String[] args) {
		
		BinomialHeap<Integer> bh = new BinomialHeap<Integer>();
		for(int i = 0; i < 100; i++){
			bh.add((int)(Math.random()*500));
		}
		
		for(int i = 0; i < 100; i++){
			System.out.println(bh.pop());
		}
		
		
	}

}
