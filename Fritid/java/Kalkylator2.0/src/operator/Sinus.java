package operator;

import exceptions.MatteException;
import exceptions.TreeException;

public class Sinus extends  UnaryOperator{

	public Sinus(Operator child) throws TreeException {
		super(child, "Sinus");
	}

	@Override
	public double calculate() throws MatteException{
		return Math.sin(getChild().calculate());
	}

}
