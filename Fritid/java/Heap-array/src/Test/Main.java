package Test;

import java.util.ArrayList;

import Heap.HeapE;

public class Main {

   public static void main(String[] args) {
      new Main().run();
   }
	
   Integer[] array = {11, 5, 8, 3, 4, 15, 7, 7, 5, 43, 1, 11, 5};
	
   public void run(){
      In[] arr = new In[array.length];
      for (int i = 0; i < array.length; i++) {
         arr[i] = new In(array[i], i);
      }
      HeapE<In> heap = new HeapE<In>(HeapE.MAX_HEAP);
      heap.addArray(arr);
      heap.print();
	
      In b = heap.removeRoot();
      System.out.println("tar bort "+b.i);
      heap.print();

      heap.add(new In(100000, 50));

      System.out.println("uppdaterar en etta");
      //int u = heap.find(arr[10]);
      arr[10].i = 500;
      heap.update(arr[10]);
      heap.print();

      ArrayList<In> sorted = heap.sort();
      for(In x : sorted)
         System.out.println(x.i);
   }

   private class In implements Comparable<In>{
      int i;
      int id;
      public In(int i, int id){
         this.i = i;
         this.id = id;
      }
      
      @Override
      public int compareTo(In o) {
         return Integer.compare(this.i, o.i);
      }
      
      @Override
      public String toString() {
         return Integer.toString(i);
      }

      @Override
      public boolean equals(Object obj) {
         //return ((In)obj).i == this.i;
         return obj == this || this.id == ((In)obj).id;
      }

      @Override
      public int hashCode(){
         return Integer.hashCode(id);
      }

	}

}
