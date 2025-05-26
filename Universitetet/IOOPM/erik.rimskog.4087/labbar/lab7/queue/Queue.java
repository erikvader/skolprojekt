package queue;

public class Queue<T>{

   private Node first;
   private Node last;
   private int length;

   public Queue(){
      length = 0;
      first = last = null;
   }

   public int getLength(){
      return this.length;
   }

   public T peek(){
      if(length == 0){
         throw new EmptyQueueException();
      }
      return first.data;
   }

   public void enqueue(T data){
      Node n = new Node(data);
      if(last != null) 
    	  last.next = n;
      else
    	  first = n;
      last = n;
      length++;
   }

   public T dequeue(){
      if(length == 0){
         throw new EmptyQueueException();
      }
      Node n = first;
      first = first.next;
      if(first == null)
    	  last = null;
      length--;
      return n.data;
   }

   private class Node{
      T data;
      Node next;
      public Node(T data){
         this.data = data;
      }
   }
}
