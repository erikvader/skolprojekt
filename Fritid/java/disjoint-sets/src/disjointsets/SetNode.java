package disjointsets;

class SetNode {

   SetNode parent;
   int rank;

   SetNode(){
      parent = this;
      rank = 0;
   }
   
}
