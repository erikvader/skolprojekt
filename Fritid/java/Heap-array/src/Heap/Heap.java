package Heap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Heap <T extends Comparable<T>> {
	
   //heap
   ArrayList<T> heap;
   
   public static final int MAX_HEAP = 0;
   public static final int MIN_HEAP = 1;
	
   private int mode = MIN_HEAP;

   /**
    * Att skapa är O(n) av någon anledning
    * <br>
    * Peek är O(1)
    * <br>
    * removeRoot är O(log(n))
    * <br>
    * sort är O(nlog(n))
    * <br>
    * add är O(log(n))
    * <br>
    * update är worst case O(n) (pga array-implementation)
    * borde prestera ganska bra.}
    * 
    * @param mode
    */
   public Heap(int mode){
      heap = new ArrayList<T>();
      this.mode = mode;
   }
	
   public Heap(){
      this(MIN_HEAP);
   }
	
   /**
    * tar heap och slår ihop den med denna.
    * @param heap
    */
   public void merge(Heap<T> heap){
      addArray(heap.heap);
   }
	
   public void addArray(T[] a){
      List<T> array = Arrays.asList(a);
      addArray(array);
   }
	
   /**
    * skapar en heap av en array
    * @param a
    */
   public void addArray(List<T> a){
      heap.addAll(a);
		
      //hitta näst understa raden
      int rad = (int)Math.ceil(Math.log(heap.size()+1)/Math.log(2)-1)-1;
      //hitta index
      int index = (int)(Math.pow(2, rad+1)-1-1);
      for(int i = index; i >= 0; i--){
         heapifyChildren(i);
      }
   }

   protected void swap(int a, int b){
      Collections.swap(heap, a, b);
   }
	
   public void add(T ele){
      heap.add(ele);
      heapifyParent(heap.size()-1);
   }
	
   //går uppåt
   private void heapifyParent(int node){
      int parent = getParent(node);
      if(parent == node) return; //rooten nåddes
      if(compare(node, parent)){
         swap(parent, node);
         heapifyParent(parent);
      }
   }
	
   public T removeRoot(){
      T value = heap.get(0);
      swap(0, heap.size()-1);
      heap.remove(heap.size()-1);
      heapifyChildren(0);
      return value;
   }
	
   /**
    * tittar på den översta talet
    * @return
    */
   public T peek(){
      return heap.get(0);
   }
	
   //går neråt
   private void heapifyChildren(int node){
      int l = getLeft(node);
      int r = getRight(node);
		
      if(l == -1 && r == -1){
         return;
      }
		
      int toHeapify;
      if(l == -1){
         toHeapify = r;
      }else if(r == -1){
         toHeapify = l;
      }else{
         if(compare(l, r)){
            toHeapify = l;
         }else{
            toHeapify = r;
         }
      }
		
      if(compare(toHeapify, node)){
         swap(toHeapify, node);
         heapifyChildren(toHeapify);
      }
   }
	
   // /**
   //  * returnar true om child är större än parent, alltså ett byte behövs
   //  * @param child
   //  * @param parent
   //  * @return
   //  */
   // private boolean maxHeap(int child, int parent){
   // 	return heap.get(child) > heap.get(parent);
   // }
	
   // /**
   //  * returnar true om child är mindre än parent, alltså ett byte behövs
   //  * @param child
   //  * @param parent
   //  * @return
   //  */
   // private boolean minHeap(int child, int parent){
   // 	return heap.get(child) < heap.get(parent);
   // }

   private boolean compare(T child, T parent){
      if(mode == MIN_HEAP){
         return child.compareTo(parent) < 0;
      }else{
         return child.compareTo(parent) > 0;
      }
   }
   
   private boolean compare(int child, int parent){
      return compare(heap.get(child), heap.get(parent));
   }

   private boolean equals(T a, T b){
      return a.compareTo(b) == 0;
   }
   
   /**
    * i en min_hea   er den att sortera minsta till största. tvärtom för max_heap, störst till minst
    * poppar hela heapen btw.
    * @return
    */
   public ArrayList<T> sort(){
      ArrayList<T> sorted = new ArrayList<T>(heap.size());
      while(heap.size() > 0){
         sorted.add(removeRoot());
      }
      return sorted;
   }
	
   private int getLeft(int node){
      int temp = 2*node+1;
      return temp >= heap.size() ? -1 : temp;
   }
	
   private int getRight(int node){
      int temp = 2*node+2;
      return temp >= heap.size() ? -1 : temp;
   }
	
   private int getParent(int node){
      int temp = (node-1)/2;
      return temp >= heap.size() ? -1 : temp;
   }
	
   public void print(){
      print("", true, 0);
   }
	
   private void print(String prefix, boolean isTail, int node){
      System.out.println(prefix + (isTail ? "V-- " : "|-- ") + heap.get(node));
      int r = getRight(node);
      if (r != -1) {
         print(prefix + (isTail ?"    " : "|   "), false, r);
      }
      int l = getLeft(node);
      if(l != -1) {
         print(prefix + (isTail ? "    " : "|   "), true, l);
      }
   }
	
   /* original som kan skriva ut träd med fler barn
      public void print() {
      print("", true);
      }
	
      private void print(String prefix, boolean isTail) {
      System.out.println(prefix + (isTail ? "└── " : "├── ") + name);
      for (int i = 0; i < children.size() - 1; i++) {
      children.get(i).print(prefix + (isTail ? "    " : "│   "), false);
      }
      if (children.size() > 0) {
      children.get(children.size() - 1).print(prefix + (isTail ?"    " : "│   "), true);
      }
      }
   */

   //hittar vilket index ett element är på
   private int find(T e, int i){
      T temp = heap.get(i);
      if(e == temp){
         return i;
      }

      int res;
      int child;
      for(int j = 0; j < 2; j++){
         child = j == 0 ? getLeft(i) : getRight(i);
         if(child == -1) continue;
         
         temp = heap.get(child);
         if(compare(temp, e) || equals(temp, e)){
            res = find(e, child);
            if(res != -1){
               return res;
            }
         }
      }

      return -1;
      
   }

   public int find(T e){
      return find(e, 0);
   }

	/**
	 *Tar ett index till ett element som finns i Heapen 
     *vars prio har ändrats
     *på ett sådant sätt att elementet möjligtvist skall flyttas
     *uppåt i trädet.<br>
     *returnar false ifall elementet inte finns i heapen.<br>
     *Denna funktion är inte så effektiv eftersom man måste leta efter
     *elementet i heapen.
	 *
	 * @param e
	 * @return
	 */
	public void update(int i){
      heapifyParent(i);
   }
   
}
