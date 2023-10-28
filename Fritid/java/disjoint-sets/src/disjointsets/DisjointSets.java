package disjointsets;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * T implements equals and hashCode for use in HashMap
 * 
 * This implements 'Forest of Disjoint Sets' from 'Introduction to algorithms'.
 * 
 * @author
 * @version
 */
public class DisjointSets<T> {

   private HashMap<T, SetNode> map;

   
	/**
	 * Initializes everything
	 *
	 * @param l - ArrayList to create single-element sets from
	 */
	public DisjointSets(ArrayList<T> l){
      makeSets(l);
   }
   
   private void makeSets(ArrayList<T> l){
      map = new HashMap<T, SetNode>((int)(l.size()/0.75));
      for(int i = 0; i < l.size(); i++){
         map.put(l.get(i), new SetNode());
      }
   }
   
	/**
	 * Merges the sets a and b belongs to.
     * a and b must be in differents sets
	 *
	 * @param a
	 * @param b
	 */
	public void union(T a, T b){
      SetNode aa = findSet(map.get(a));
      SetNode bb = findSet(map.get(b));
      if(aa.rank == bb.rank){
         aa.parent = bb;
         bb.rank++;
      }else if(aa.rank > bb.rank){
         bb.parent = aa;
      }else{
         aa.parent = bb;
      }
   }

	/**
	 * Checks if a and b belong to the same set
	 *
	 * @param a
	 * @param b
	 * @return
	 */
	public boolean inSameSet(T a,T b){
      return findSet(map.get(a)) == findSet(map.get(b));
   }

   
   private SetNode findSet(SetNode a){
      if(a == a.parent){
         return a;
      }else{
         a.parent = findSet(a.parent);
         return a.parent;
      }
   }
}
