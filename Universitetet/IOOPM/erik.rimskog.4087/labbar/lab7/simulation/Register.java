package simulation;

import queue.Queue;

public class Register {
   private boolean open;
   private Queue<Customer> queue;

   public Register(){
      this.queue = new Queue<Customer>();
      this.open = false;
   }

   public void open(){
      this.open = true;
   }

   public void close(){
      this.open = false;
   }

   public boolean isOpen(){
      return this.open;
   }

   public void step(){
      this.queue.peek().serve();
   }

   public boolean hasCustomers(){
      return this.queue.getLength() > 0;
   }

   public boolean currentCustomerIsDone(){
      return this.queue.peek().isDone();
   }

   public int getQueueLength(){
      return this.queue.getLength();
   }

   public void addToQueue(Customer c){
      this.queue.enqueue(c);
   }

   public Customer removeCurrentCustomer(){
      return this.queue.dequeue();
   }

   public Customer getCurrentCustomer(){
      return this.queue.peek();
   }

   @Override
   public String toString(){
      String display = "";
      if(!this.isOpen()){
         display += " X [ ]\n";
      }else{
         int len = this.getQueueLength();
         display += "   [";
         if(this.hasCustomers())
            display += this.getCurrentCustomer().getGroceries();
         else
            display += "0";
         display += "]";

         for(int j = 0; j < len; j++){
            display += "@";
         }
         display += "\n";
      }
      return display;
   }

}
