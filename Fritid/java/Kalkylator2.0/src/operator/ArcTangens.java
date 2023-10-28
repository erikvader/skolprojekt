package operator;

import exceptions.MatteException;
import exceptions.TreeException;

public class ArcTangens extends UnaryOperator{

	public ArcTangens(Operator child) throws TreeException {
		super(child, "ArcTangens");
	}

	@Override
	public double calculate() throws MatteException{
		return Math.atan(getChild().calculate());
	}

	
	
}
