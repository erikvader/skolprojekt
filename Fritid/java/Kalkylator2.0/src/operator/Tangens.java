package operator;

import exceptions.MatteException;
import exceptions.TreeException;

public class Tangens extends UnaryOperator{

	public Tangens(Operator child) throws TreeException {
		super(child, "Tangens");
	}

	@Override
	public double calculate() throws MatteException{
		double v = getChild().calculate();
		if((v % Math.PI) % Math.PI/2 == 0){
			throw new MatteException("Division with 0 on tangens");
		}
		return Math.tan(v);
	}

}
