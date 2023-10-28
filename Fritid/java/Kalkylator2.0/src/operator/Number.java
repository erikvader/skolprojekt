package operator;

public class Number extends Operator{

	private double number;
	
	public Number(double number){
		super("Number");
		this.number = number;
	}

	@Override
	public double calculate() {
		return number;
	}
	
	@Override
	protected void print(String prefix, boolean isTail) {
		System.out.println(prefix + (isTail ? "v-- " : "|-- ") + Double.toString(number));
	}

}
