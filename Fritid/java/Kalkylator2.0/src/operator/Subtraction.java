package operator;

import exceptions.MatteException;
import exceptions.TreeException;

public class Subtraction extends BinaryOperator{

	public Subtraction(Operator left, Operator right) throws TreeException {
		super(left, right, "Subtraction");
		
	}

	@Override
	public double calculate() throws MatteException{
		if(getLeft() == null){
			return -getRight().calculate();
		}
		
		return getLeft().calculate() - getRight().calculate();
	}
	
	@Override
	protected void checkExceptions() throws TreeException {
		if(getRight() == null)
			throw new TreeException("Right is null on "+name);
	}

}
