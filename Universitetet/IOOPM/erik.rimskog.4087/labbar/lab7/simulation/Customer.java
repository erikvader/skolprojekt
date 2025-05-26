package simulation;

public class Customer{
   private int bornTime;
   private int groceries;

   public Customer(int bornTime, int groceries){
      this.bornTime = bornTime;
      this.groceries = groceries;
   }

   public void serve(){
      this.groceries--;
   }

   public boolean isDone(){
      return groceries <= 0;
   }

   public int getBornTime(){
      return this.bornTime;
   }

   public int getGroceries(){
      return groceries;
   }
}
