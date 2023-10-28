package operator;

import exceptions.MatteException;
import exceptions.TreeException;

public class Division extends BinaryOperator{

	public Division(Operator left, Operator right) throws TreeException {
		super(left, right, "Division");
	}

	@Override
	public double calculate() throws MatteException{
		double l = getLeft().calculate();
		double r = getRight().calculate();
		
		if(r == 0){
			throw new MatteException("Division with 0");
		}
		
		return l / r;
	}

}
