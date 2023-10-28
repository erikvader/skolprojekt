package Heap;

import java.util.HashMap;
import java.util.List;

/** Heap-extended
 * har lite mer overhead på alla operationer, men nu kan
 * man köra update på Ο(lg(n))
 * <br>
 * Nu behöver dock classen implementera hashcode och equals
 * @author
 */
public class HeapE <T extends Comparable<T>> extends Heap<T>{
   private HashMap<T, Integer> map;

   public HeapE(){
      this(MIN_HEAP);
   }

   public HeapE(int mode){
      super(mode);
      map = new HashMap<T, Integer>();
   }

   @Override
   public void addArray(List<T> a) {
      int start = heap.size();
      for(int i = 0; i < a.size(); i++){
         map.put(a.get(i), start);
         start++;
      }
      
      super.addArray(a);
   }

   @Override
   protected void swap(int a, int b) {
      map.put(heap.get(a), b);
      map.put(heap.get(b), a);
      super.swap(a, b);
   }

   @Override
   public void add(T ele) {
      map.put(ele, heap.size());
      super.add(ele);
   }

	@Override
   public T removeRoot() {
      T res = super.removeRoot();
      map.remove(res);
      return res;
   }

   public void update(T ele){
      super.update(map.get(ele));
   }

   @Override
   public void print() {
      super.print();
      System.out.println(heap);
      System.out.println(map);
   }


}
