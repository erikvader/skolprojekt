package genetic;

public abstract class DNA {
	
	protected double fitness = 0;
	
	public DNA(){
		
	}
	
	public abstract void calcFitness();
	
	public abstract DNA mate(DNA partner, double mutateFactor);
	
	public double getFitness(){return fitness;}

	@Override
	public String toString() {
		return "DNA : fit="+fitness;
	}
}
