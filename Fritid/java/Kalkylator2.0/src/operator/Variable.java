package operator;

import java.util.HashMap;

import exceptions.MatteException;
import exceptions.TreeException;

public class Variable extends Operator {

	private HashMap<String, Operator> calculated;
	private HashMap<String, Double> cache;
	private String varName;
	
	/**
	 * är en grej som säger att den är en variabel. den kommer att räkna ut vad den blir eller
	 *  ta ett värden ur en cache.
	 * 
	 * @param varName
	 * @param calculated
	 * @param cache
	 */
	public Variable(String varName, HashMap<String, Operator> calculated, HashMap<String, Double> cache) {
		super("Variable");
		this.calculated = calculated;
		this.cache = cache;
		this.varName = varName;
	}

	@Override
	public double calculate() throws MatteException {
		if(!calculated.containsKey(varName)){
			throw new MatteException("\""+varName+"\" finns inte eller är inte uträknad.");
		}
		if(cache.containsKey(varName)){
			return cache.get(varName);
		}
		
		double val = calculated.get(varName).calculate();
		cache.put(varName, val);
		
		return val;
	}

	@Override
	protected void checkExceptions() throws TreeException {
		if(!calculated.containsKey(varName)){
			throw new TreeException(varName+" har inget värde, var vänlig räkna ut det först.");
		}
	}
	
	protected void print(String prefix, boolean isTail){
		System.out.println(prefix + (isTail ? "v-- " : "|-- ") + varName+": "+calculated.get(varName));
	};


}
