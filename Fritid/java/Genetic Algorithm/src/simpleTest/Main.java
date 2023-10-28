package simpleTest;

import java.util.ArrayList;

import genetic.DNA;
import genetic.Population;

public class Main {

	public static void main(String[] args) {
		new Main().run();
		//new Main().test();
		
	}
	
	String target = "to be or not to be that is the question";
	int population = 200;
	double mutateFactor = 0.01; 
	int generation = 0;
	ArrayList<DNA> creatures;
	
	public void run(){
		creatures = new ArrayList<DNA>();
		//init population
		for(int i = 0; i < population; i++){
			creatures.add(new Creature(target.length(), target));
		}
		
		Population pop = new Population(creatures, Population.SELECTION_MODE_STOCHASTIC);
		
		while(true){
			pop.calcFitnesses();
			pop.prepareFitnessProportionateSelection();
			Creature best = (Creature) pop.getDNAs().get(0);
			System.out.println(pop.getGeneration() + " : "+best.getBody() + ", fit "+best.getFitness()+", avg "+pop.getAvgFitness());
			if(best.getBody().equals(target)) break;
			pop.advanceToNextGeneration(mutateFactor);
		}
	}

}
