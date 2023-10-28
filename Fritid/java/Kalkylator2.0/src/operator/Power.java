package operator;

import exceptions.MatteException;
import exceptions.TreeException;

public class Power extends BinaryOperator{

	public Power(Operator left, Operator right) throws TreeException {
		super(left, right, "Power");
	}

	@Override
	public double calculate() throws MatteException{
		return Math.pow(getLeft().calculate(), getRight().calculate());
	}

}
