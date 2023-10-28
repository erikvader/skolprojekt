package operator;

import exceptions.TreeException;

public abstract class BinaryOperator extends Operator{

	private Operator left, right;
	
	public BinaryOperator(Operator left, Operator right, String name) throws TreeException{
		super(name);
		this.left = left;
		this.right = right;
		
		checkExceptions(); //slänger exception om något inte stämmer
		
	}
	
	protected void checkExceptions() throws TreeException{
		if(left == null && right == null){
			throw new TreeException("Left and Right is null on "+name);
		}else if(left == null)
			throw new TreeException("Left is null on "+name);
		else if(right == null)
			throw new TreeException("Right is null on "+name);
	}
	
	public Operator getRight(){return right;}
	public Operator getLeft(){return left;}
	
	//public abstract double calculate(){};
	protected void print(String prefix, boolean isTail) {
        System.out.println(prefix + (isTail ? "v-- " : "|-- ") + name);
        left.print(prefix + (isTail ? "    " : "|   "), false);
        right.print(prefix + (isTail ?"    " : "|   "), true);
    }
}
