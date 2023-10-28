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
	 * @return namnet p� denna operator
	 */
	public String getName(){return name;}
	
	/**
	 * R�kna ut v�rdet p� denna operator.
	 * 
	 * @return v�rdet
	 * @throws MatteException sl�ngs ifall ett matematiskt fel uppstod
	 */
	public abstract double calculate() throws MatteException;
	
	/**
	 * Kollar ifall det finns tillr�ckligt med information f�r att kunna r�knas ut, om det inte g�r det sl�ngs ett {@link TreeException}.
	 * 
	 * @throws TreeException sl�ngs ifall problem uppstod i skapningen av tr�det.
	 */
	protected void checkExceptions() throws TreeException{};
	
	protected void print(String prefix, boolean isTail){
		System.out.println(prefix + (isTail ? "v-- " : "|-- ") + name);
	};
	
	public void print(){
		print("", true);
	}
	
}
