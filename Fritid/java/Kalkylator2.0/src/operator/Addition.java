package operator;

import exceptions.MatteException;
import exceptions.TreeException;

public class Addition extends BinaryOperator{

	public Addition(Operator left, Operator right) throws TreeException {
		super(left, right, "Addition");
	}

	@Override
	public double calculate() throws MatteException{
		return getLeft().calculate() + getRight().calculate();
	}

}
