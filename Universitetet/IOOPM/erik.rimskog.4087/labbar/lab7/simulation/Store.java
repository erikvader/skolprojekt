package simulation;

import java.util.ArrayList;

public class Store {
   private Register registers[];
   private ArrayList<Customer> doneCustomers;

   public Store(int numRegisters){
      this.registers = new Register[numRegisters];
      for(int i = 0; i < numRegisters; i++){
         registers[i] = new Register();
      }
      this.registers[0].open();
      doneCustomers = new ArrayList<Customer>();
   }

   public double getAverageQueueLength(){
      double avg = 0;
      int numOpen = 0;
      for(int i = 0; i < registers.length; i++){
         if(registers[i].isOpen()){
            numOpen++;
            avg += registers[i].getQueueLength();
         }
      }
      avg /= numOpen;
      return avg;
   }

   private Register getShortestRegister(){
      Register sho = registers[0];
      for(int i = 1; i < registers.length; i++){
         if(registers[i].isOpen() && registers[i].getQueueLength() < sho.getQueueLength()){
            sho = registers[i];
         }
      }
      return sho;
   }

   public void newCustomer(Customer c){
      getShortestRegister().addToQueue(c);
   }

   public ArrayList<Customer> getDoneCustomers(){
      return doneCustomers;
   }

   public void step(){
      if(doneCustomers.size() != 0)
         doneCustomers = new ArrayList<Customer>();

      for(Register r : registers){
         if(r.getQueueLength() == 0) continue;
         r.step();
         if(r.currentCustomerIsDone()){
            doneCustomers.add(r.removeCurrentCustomer());
         }
      }
   }

   public void openNewRegister(){
      for(int i = 0; i < registers.length; i++){
         if(!registers[i].isOpen()){
            registers[i].open();
            break;
         }
      }
   }

   @Override
   public String toString(){
      String display = "";

      for(int i = 0; i < registers.length; i++){
         display += registers[i] + "\n";
      }
      
      return display;
      
   }
   
}
