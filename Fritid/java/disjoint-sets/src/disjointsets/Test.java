package disjointsets;

import java.util.ArrayList;

class Test {
   
   public static void main(String args[]){

      ArrayList<Integer> ints = new ArrayList<Integer>();
      for(int i = 0; i < 10; i++){
         ints.add(new Integer(i));
      }
      DisjointSets<Integer> ds = new DisjointSets<>(ints);

      System.out.println(ds.inSameSet(ints.get(0), ints.get(1)));
      ds.union(ints.get(0), ints.get(1));
      System.out.println(ds.inSameSet(ints.get(0), ints.get(1)));


   }
}
