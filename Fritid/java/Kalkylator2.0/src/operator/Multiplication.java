package operator;

import exceptions.MatteException;
import exceptions.TreeException;

public class Multiplication extends BinaryOperator{

	public Multiplication(Operator left, Operator right) throws TreeException {
		super(left, right, "Multiplication");
	}

	@Override
	public double calculate() throws MatteException{
		return getLeft().calculate() * getRight().calculate();
	}

}
