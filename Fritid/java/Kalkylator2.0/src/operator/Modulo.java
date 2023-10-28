package operator;

import exceptions.MatteException;
import exceptions.TreeException;

public class Modulo extends BinaryOperator{

	public Modulo(Operator left, Operator right) throws TreeException {
		super(left, right, "Modulo");
	}

	@Override
	public double calculate() throws MatteException{
		double r = getRight().calculate();
		if(r == 0){
			throw new MatteException("Division with 0 on modulus");
		}
		return getLeft().calculate() % r;
	}

}
