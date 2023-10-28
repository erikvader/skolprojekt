package simpleTest;

import genetic.DNA;

public class Creature extends DNA{

	private String body, target;
	
	public Creature(int length, String target){
		char[] temp = new char[length];
		for(int i = 0; i < length; i++){
			temp[i] = getRandomChar();
		}
		body = new String(temp);
		this.target = target;
	}
	
	public Creature(String body, String target){
		this.body = body;
		this.target = target;
	}
	
	public void calcFitness(String target){
		int score = 0;
		for(int i = 0; i < target.length(); i++){
			if(target.charAt(i) == body.charAt(i))
				score++;
		}
		this.fitness = (double)score/target.length();
		//this.fitness = (1/(Math.E-1))*(Math.exp(this.fitness)-1);
		this.fitness = Math.pow(fitness, 4);
	}
	
	@Override
	public void calcFitness() {
		calcFitness(target);
	}
	
	public Creature mate2(Creature partner, double mutateFactor){
		char[] offspring = new char[partner.body.length()];
		
		//slå ihop föräldrarna
		/*
		int rand = (int)(Math.random()*2);
		for(int i = 0; i < offspring.length; i++){
			offspring[i] = i % 2 == rand ? this.body.charAt(i) : partner.body.charAt(i);
		}
		*/
		int m = (int)(Math.random()*(offspring.length-1)+1);
		for(int i = 0; i < offspring.length; i++){
			offspring[i] = i < m ? this.body.charAt(i) : partner.body.charAt(i);
		}
		
		//mutate
		for(int i = 0; i < offspring.length; i++){
			boolean mutate = Math.random() < mutateFactor;
			if(!mutate) continue;
			char randChar = getRandomChar();
			offspring[i] = randChar;
		}
		
		return new Creature(new String(offspring), target);
	}
	
	@Override
	public DNA mate(DNA partner, double mutateFactor) {
		return mate2((Creature)partner, mutateFactor);
	}
	
	protected char getRandomChar(){
		int rand = (int)(Math.random()*27);
		if(rand == 26){
			return ' ';
		}else{
			return (char)(97+rand);
		}
	}
	
	public String getBody(){return body;}

	@Override
	public String toString() {
		return "" + body + ", fit: "+Double.toString(fitness);
	}
	
}
