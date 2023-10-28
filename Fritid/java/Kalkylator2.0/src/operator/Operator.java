package operator;

import exceptions.MatteException;
import exceptions.TreeException;

/**
 * En abstrakt klass som alla operatorer ska extenda. 
 * 
 * @author ErRi0401
 *
 */
public abstract class Operator{
	
	protected String name;
	
	public Operator(String name){
		this.name = name;
	}
	
	/**
	 * 
	 * @return namnet på denna operator
	 */
	public String getName(){return name;}
	
	/**
	 * Räkna ut värdet på denna operator.
	 * 
	 * @return värdet
	 * @throws MatteException slängs ifall ett matematiskt fel uppstod
	 */
	public abstract double calculate() throws MatteException;
	
	/**
	 * Kollar ifall det finns tillräckligt med information för att kunna räknas ut, om det inte gör det slängs ett {@link TreeException}.
	 * 
	 * @throws TreeException slängs ifall problem uppstod i skapningen av trädet.
	 */
	protected void checkExceptions() throws TreeException{};
	
	protected void print(String prefix, boolean isTail){
		System.out.println(prefix + (isTail ? "v-- " : "|-- ") + name);
	};
	
	public void print(){
		print("", true);
	}
	
}
