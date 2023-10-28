
import java.util.ArrayList;

import funktioner.Funktioner;
import genetic.DNA;
import genetic.Population;
import graph.Edge;
import graph.Graph;
import graph.Node;
import maxflow.Maxflow;

//funkar inte, fitness är nog inte tillräckligt bra elr nåt.

public class Problem185 {

	public static void main(String[] args) {
		//System.out.println(Character.digit('2', 10));
		new Problem185().run();
	}
	
	String[] lillaSiffror = {
		"90342",
		"70794",
		"39458",
		"34109",
		"51545",
		"12531"	
	};
	
	int[] lillaKorrekt = {2, 0, 2, 1, 2, 1};
	int lillaSvarLength = 5;
	
	String[] storaSiffror = {
			"5616185650518293",
			"3847439647293047",
			"5855462940810587",
			"9742855507068353",
			"4296849643607543",
			"3174248439465858",
			"4513559094146117",
			"7890971548908067",
			"8157356344118483",
			"2615250744386899",
			"8690095851526254",
			"6375711915077050",
			"6913859173121360",
			"6442889055042768",
			"2321386104303845",
			"2326509471271448",
			"5251583379644322",
			"1748270476758276",
			"4895722652190306",
			"3041631117224635",
			"1841236454324589",
			"2659862637316867"	
		};
		
		int[] storaKorrekt = {2, 1, 3, 3, 3, 1, 2, 3, 1, 2, 3, 1, 1, 2, 0, 2, 2, 3, 1, 3, 3, 2};
		int storaSvarLength = 16;
	
	public void run(){
		//solveProblemo2(lillaSiffror, lillaKorrekt, lillaSvarLength);
		solveProblemo2(storaSiffror, storaKorrekt, storaSvarLength);
	}
	
	public void solveProblemo2(String[] siffror, int[] korrekt, int svarLength){
		
		ArrayList<DNA> creatures = new ArrayList<DNA>();
		double mutation = 0.01;
		for(int i = 0; i < 1000000; i++){
			creatures.add(new Creature(randBody(svarLength), siffror, korrekt));
		}
		
		Population pop = new Population(creatures, Population.SELECTION_MODE_STOCHASTIC);
		
		while(true){
			pop.calcFitnesses();
			pop.prepareFitnessProportionateSelection();
			Creature best = (Creature) pop.getDNAs().get(0);
			System.out.println(pop.getGeneration() + " : "+best.body + ", fit "+best.getFitness()+", avg "+pop.getAvgFitness());
			if(best.getFitness() == 22) break;
			pop.advanceToNextGeneration(mutation);
		}
	}
	
	public class Creature extends DNA{
		
		String body;
		String[] siffror;
		int[] korrekt;
		
		public Creature(String body, String[] siffror, int[] korrekt){
			super();
			this.body = body;
			this.siffror = siffror;
			this.korrekt = korrekt;
		}

		@Override
		public void calcFitness() {
			/*
			int away = 0;
			for(int i = 0; i < this.siffror.length; i++){
				int thisScore = 0;
				String s = this.siffror[i];
				for(int j = 0; j < s.length(); j++){
					if(s.charAt(j) == this.body.charAt(j)){
						thisScore++;
					}
				}
				away += Math.abs(korrekt[i] - thisScore);
			}
			
			this.fitness = 1000.0/away;
			*/
			
			int score = 0;
			for(int i = 0; i < this.siffror.length; i++){
				int thisScore = 0;
				String s = this.siffror[i];
				for(int j = 0; j < s.length(); j++){
					if(s.charAt(j) == this.body.charAt(j)){
						thisScore++;
					}
				}
				if(thisScore == korrekt[i]){
					score++;
				}
			}
			
			this.fitness = (double)score/siffror.length;
			this.fitness = Math.pow(this.fitness, 1);
		}

		@Override
		public DNA mate(DNA partner, double mutateFactor) {
			Creature cre = (Creature) partner;
			char[] offspring = new char[cre.body.length()];
			
			//slå ihop föräldrarna
		
			int m = (int)(Math.random()*(offspring.length-1)+1);
			for(int i = 0; i < offspring.length; i++){
				offspring[i] = i < m ? this.body.charAt(i) : cre.body.charAt(i);
			}
			
			//mutate
			for(int i = 0; i < offspring.length; i++){
				boolean mutate = Math.random() < mutateFactor;
				if(!mutate) continue;
				char randChar = Character.forDigit((int)(Math.random()*10), 10);
				offspring[i] = randChar;
			}
			
			return new Creature(new String(offspring), siffror, korrekt);
		}
		
	}
	
	public String randBody(int len){
		String r = "";
		for(int i = 0; i < len; i++){
			char c = Character.forDigit((int)(Math.random()*10), 10);
			r += c;
		}
		return r;
	}
	
	public void solveProblemo(String[] siffror, int[] korrekt, int svarLength){
		int lcm = 1; //så att vi slipper fractions
		int[][] counts = new int[svarLength][10];
		
		//fill counts
		for(int i = 0; i < siffror.length; i++){
			String line = siffror[i];
			for(int j = 0; j < line.length(); j++){
				int tal = Character.digit(line.charAt(j), 10);
				counts[j][tal]++;
			}
		}
		
		//find lcm
		for(int i = 0; i < counts.length; i++){
			for(int j = 0; j < 10; j++){
				int t = counts[i][j];
				if(t != 0){
					lcm = Funktioner.LCM(lcm, t);
				}
			}
		}
		
		//konstruera grafen
		//0 = source, 
		//[U + 1, siffror.length] = korrektNoderna
		//[U + 1, L + svarLength*10 - 1] = siffernoderna för varje position (L+ (10*plats + siffran) för att få en specifik nod)
		//[U + 1, L + svarLength-1] = svarNoderna
		//U + 1 = drain
		
		int sourceIndex = 0;
		int korrektIndex = sourceIndex + 1;
		int sifferNodIndex = korrektIndex + siffror.length;
		int svarNodIndex = sifferNodIndex + svarLength*10;
		int drainIndex = svarNodIndex + svarLength;
		
		Graph g = new Graph();
		g.addNode(drainIndex+1);
		
		//fyll på korrektNoderna
		for(int i = 0; i < siffror.length; i++){
			g.addEdge(sourceIndex, i+korrektIndex, lcm*korrekt[i]);
		}
		
		//fyll på saker i mitten
		for(int col = 0; col < svarLength; col++){
			int svarNod = svarNodIndex + col;
			for(int tal = 0; tal <= 9; tal++){
				if(counts[col][tal] == 0) continue;
				int sifferNod = 10*col + tal + sifferNodIndex;
				for(int i = 0; i < siffror.length; i++){
					if(Character.digit(siffror[i].charAt(col), 10) != tal) continue;
					int korrektNod = korrektIndex+i;
					int kapacitet = lcm/counts[col][tal];
					g.addEdge(korrektNod, sifferNod, kapacitet);
				}
				g.addEdge(sifferNod, svarNod, lcm);
			}
		}
		
		//fyll på svarnoderna till drain
		for(int i = 0; i < svarLength; i++){
			int svarNod = svarNodIndex + i;
			g.addEdge(svarNod, drainIndex, lcm);
		}
		
		//maxflow!!!!
		Maxflow maxflow = new Maxflow(g.getNodes().get(sourceIndex), g.getNodes().get(drainIndex));
		maxflow.go();
		
		//skriva ut
		ArrayList<Node> nodes = g.getNodes();
		for(int i = 0; i < nodes.size(); i++){
			System.out.println("node: "+nodes.get(i).getId());
			
			ArrayList<Edge> edges = nodes.get(i).getEdges();
			for(int j = 0; j < edges.size(); j++){
				Edge e = edges.get(j);
				System.out.println("    "+e.getFlow()+"/"+e.getCapacity()+" : "+e.getTarget().getId());
			}
		}
		
		//hitta resultatet
		String svar = "";
		for(int i = 0; i < svarLength; i++){
			Node svarNod = g.getNodes().get(svarNodIndex + i);
			ArrayList<Edge> edges = svarNod.getEdges();
			for(int j = 0; j < edges.size(); j++){
				Edge e = edges.get(j);
				if(e.getFlow() < 0){
					int nodIndex = e.getTarget().getId();
					int tal = nodIndex - 10*i - sifferNodIndex;
					svar += Integer.toString(tal);
					break;
				}
			}
		}
		
		System.out.println("svaret är " + svar);
		
	}

}
