package operator;

import exceptions.MatteException;
import exceptions.TreeException;

public class Factorial extends UnaryOperator{

	public Factorial(Operator child) throws TreeException {
		super(child, "Factorial");
	}

	@Override
	public double calculate() throws MatteException{
		double num = getChild().calculate();
		if(num % 1 != 0){
			throw new MatteException("Factorial not good enough, can't handle decimal numbers :(");
		}
		return fact(num);
	}
	
	private double fact(double f){
		if(f == 1){
			return f;
		}else{
			return fact(f-1)*f;
		}
	}

}
