package operator;

import exceptions.MatteException;
import exceptions.TreeException;

public class ArcSinus extends UnaryOperator{

	public ArcSinus(Operator child) throws TreeException {
		super(child, "ArcSinus");
	}

	@Override
	public double calculate() throws MatteException{
		double v = getChild().calculate();
		if(v > 1 || v < -1){
			throw new MatteException("ArcSinus value greater than 1 or smaller than -1");
		}
		return Math.asin(v);
	}

	
	
}
