package operator;

import exceptions.MatteException;
import exceptions.TreeException;

public class Squareroot extends UnaryOperator{

	public Squareroot(Operator child) throws TreeException {
		super(child, "Square root");
	}

	@Override
	public double calculate() throws MatteException{
		double v = getChild().calculate();
		if(v < 0){
			throw new MatteException("Squareroot gives unreal answer");
		}
		return Math.sqrt(v);
	}

}
