package operator;

import exceptions.MatteException;
import exceptions.TreeException;

public class Cosinus extends UnaryOperator{

	public Cosinus(Operator child) throws TreeException {
		super(child, "Cosinus");
	}

	@Override
	public double calculate() throws MatteException{
		
		return Math.cos(getChild().calculate());
	}

}
