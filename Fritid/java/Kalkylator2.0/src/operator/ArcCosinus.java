package operator;

import exceptions.MatteException;
import exceptions.TreeException;

public class ArcCosinus extends UnaryOperator{

	public ArcCosinus(Operator child) throws TreeException {
		super(child, "ArcCosinus");
	}

	@Override
	public double calculate() throws MatteException{
		double v = getChild().calculate();
		if(v > 1 || v < -1){
			throw new MatteException("ArcCosinus value greater than 1 or smaller than -1");
		}
		return Math.acos(v);
	}

}
