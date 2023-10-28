package genetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Population {

	private ArrayList<DNA> dnas;
	private int popSize;
	private int generation;
	private double[] probabilities;
	private double avgFitness;
	
	public static final int SELECTION_MODE_STOCHASTIC = 0;
	public static final int SELECTION_MODE_CDF = 1;
	//public static final int SELECTION_MODE_TRUNCATION = 2; //döda en procent av de sämsta.
	private int selectionMode;
	
	public Population(ArrayList<DNA> dnas, int selectionMode){
		this.dnas = dnas;
		popSize = dnas.size();
		probabilities = new double[popSize];
		generation = 0;
		this.selectionMode = selectionMode;
	}
	
	public void calcFitnesses(){
		for(int i = 0; i < popSize; i++){
			dnas.get(i).calcFitness();
		}
	}
	
	public void prepareFitnessProportionateSelection(){
		//gör om all fitness till sannolikheter mellan 0 och 1
		double total = 0;
		for(int i = 0; i < popSize; i++){
			double temp = dnas.get(i).getFitness();
			total += temp;
		}
		
		//avg
		avgFitness = total/popSize;
		
		//if(total == 0) return; //alla har noll fitness och det spelar ingen roll vilken man tar.
		
		//sortera efter fitness
		Collections.sort(dnas, dnaComparator);
		
		//för selection
		if(selectionMode == SELECTION_MODE_CDF){
			for(int i = 0; i < popSize; i++){
				double prev = 0;
				if(i > 0) prev = probabilities[i-1];
				probabilities[i] = prev + dnas.get(i).getFitness()/total;
			}
			probabilities[popSize-1] = 1;
		}else if(selectionMode == SELECTION_MODE_STOCHASTIC){
			for(int i = 0; i < popSize; i++){
				probabilities[i] = dnas.get(i).getFitness()/dnas.get(0).getFitness();
			}
			
		}
	}
	
	public void advanceToNextGeneration(double mutateFactor){
		generation++;
		
		ArrayList<DNA> newGeneration = new ArrayList<DNA>();
		for(int i = 0; i < popSize; i++){
			int hane = -1, hona = -1;
			if(selectionMode == SELECTION_MODE_CDF){
				hane = getRandomCDF();
				hona = getRandomCDF();
			}else if(selectionMode == SELECTION_MODE_STOCHASTIC){
				hane = getRandomStochasticAcceptance();
				hona = getRandomStochasticAcceptance();
			}
			newGeneration.add(dnas.get(hane).mate(dnas.get(hona), mutateFactor));
		}
		
		dnas = newGeneration;
	}
	
	public int getGeneration(){return generation;}
	public double getAvgFitness(){return avgFitness;}
	public ArrayList<DNA> getDNAs(){return dnas;}
	
  ////////////////////////////////////////////////////////////////////////////////////////////////
 ///////////////////////////////////////////privata interna saker////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Comparator<DNA> dnaComparator = new Comparator<DNA>(){
		@Override
		public int compare(DNA o1, DNA o2) {
			return -1*Double.compare(o1.getFitness(), o2.getFitness());
		}
	};
	
	//lägger probabilities på en linje och hittar en weighted index med hjälp av det.
	//TODO uppgradera till binärsökning
	private int getRandomCDF(){
		int ind = -1;
		double rand = Math.random();
		for(int i = 0; i < popSize; i++){
			if(probabilities[i] >= rand){
				ind = i;
				break;
			}
		}
		return ind;
	}
	
	//använder sig av accept/reject
	private int getRandomStochasticAcceptance(){
		int ind = -1;
		while(ind == -1){
			int newInd = (int)(Math.random()*popSize);
			double prob = Math.random();
			if(prob < probabilities[newInd]){
				ind = newInd;
			}
		}
		return ind;
	}

}
