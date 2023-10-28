package operator;

import exceptions.TreeException;

/**
 * 
 * 
 * @author ErRi0401
 *
 */
public abstract class UnaryOperator extends Operator{

	private Operator child;
	
	public UnaryOperator(Operator child, String name) throws TreeException{
		super(name);
		this.child = child;
		
		checkExceptions(); //slänger exception om något inte stämmer
	}
	
	public Operator getChild(){return child;}
	
	protected void checkExceptions() throws TreeException{
		if(child == null)
			throw new TreeException("Child is null on "+name);

	}
	
	protected void print(String prefix, boolean isTail) {
        System.out.println(prefix + (isTail ? "v-- " : "|-- ") + name);
        if (child != null) {
            child.print(prefix + (isTail ?"    " : "|   "), true);
        }
    }
	
}
