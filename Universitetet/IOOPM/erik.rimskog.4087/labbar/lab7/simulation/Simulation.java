package simulation;

import java.util.Random;
import java.util.ArrayList;

public class Simulation {

   private Store store;
   private int time;
   private int intensity;
   private int maxGroceries;
   private int thresholdForNewRegister;
   private int numRegisters;

   private Random rand;

   private int longestWait;
   private double avgWait;
   private int customersServed;
   private int combinedWait;

   public Simulation(){
      numRegisters = 3;
      thresholdForNewRegister = 3;
      maxGroceries = 4;
      intensity = 90;
      time = 0;
      store = new Store(numRegisters);
      rand = new Random();
      longestWait = 0;
      avgWait = 0;
      customersServed = 0;
      combinedWait = 0;
   }

   public void step() {
      time++;
      int r = rand.nextInt(100);
      if(r < intensity){
         Customer c = new Customer(time, rand.nextInt(maxGroceries) + 1);
         store.newCustomer(c);
      }

      store.step();
      
      double avglen = store.getAverageQueueLength();
      if(avglen > thresholdForNewRegister){
         store.openNewRegister();
      }

      ArrayList<Customer> doneCustomers = store.getDoneCustomers();
      int wtime;
      for(Customer c : doneCustomers){
         wtime = time - c.getBornTime();
         if(wtime > longestWait){
            longestWait = wtime;
         }
         combinedWait += wtime;
      }
      customersServed += doneCustomers.size();

      if(customersServed > 0)
         avgWait = combinedWait / (double) customersServed;

   }

   public int getLongestWait(){
      return longestWait;
   }

   public double getAvgWait(){
      return avgWait;
   }
   
   @Override
   public String toString() {
      String display = "";

      display += store;

      display += "Number of customers served: " + customersServed + "\n";
      display += "Max wait time: " + longestWait + "\n";
      display += "Average wait time: " + avgWait + "\n";
      
      return display;
   }
}
