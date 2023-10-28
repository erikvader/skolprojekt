package operator;

import exceptions.MatteException;
import exceptions.TreeException;

public class E extends BinaryOperator{

	public E(Operator left, Operator right) throws TreeException {
		super(left, right, "E");
	}

	@Override
	public double calculate() throws MatteException{
		return getLeft().calculate()*Math.pow(10, getRight().calculate());
	}

}
