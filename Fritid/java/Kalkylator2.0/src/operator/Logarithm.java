package operator;

import exceptions.MatteException;
import exceptions.TreeException;

public class Logarithm extends UnaryOperator{

	public Logarithm(Operator child) throws TreeException {
		super(child, "Logarithm");
	}

	@Override
	public double calculate() throws MatteException{
		double v = getChild().calculate();
		if(v <= 0){
			throw new MatteException("Logarithm value 0 or smaller");
		}
		return Math.log10(v);
	}

}
