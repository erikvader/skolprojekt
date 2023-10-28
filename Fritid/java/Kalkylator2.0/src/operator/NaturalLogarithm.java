package operator;

import exceptions.MatteException;
import exceptions.TreeException;

public class NaturalLogarithm extends UnaryOperator{

	public NaturalLogarithm(Operator child) throws TreeException {
		super(child, "Natural Logarithm");
	}

	@Override
	public double calculate() throws MatteException{
		double v = getChild().calculate();
		if(v <= 0){
			throw new MatteException("Logarithm value 0 or smaller");
		}
		return Math.log(v);
	}

}
