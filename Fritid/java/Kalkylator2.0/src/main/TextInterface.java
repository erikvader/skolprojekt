package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import exceptions.MatteException;
import exceptions.TreeException;
import operator.Operator;

/**
 * Denna klass tar endast handom kommunikationen mellan minir�knaren och anv�ndaren. Den kommunicerar genom
 * att skriva ut text i standard output och genom att ta emot indata genom standard input. 
 * 
 * @author ErRi0401
 *
 */
public class TextInterface {

	private boolean running = true;
	
	private Kalkylator k;
	
	/**
	 * Startar mainloopen f�r programmet
	 */
	public void run(){
		System.out.println("Okaerinasaimase, goshujinsama!"); //"v�lkommen tillbaka m�stare", n�gonting som maids i maid caf�s i japan s�ger
		System.out.println("\"?\" for help");
		System.out.println("Var v�nlig skriv in ett matematiskt uttryck:");
		k = new Kalkylator();
		Scanner scan = new Scanner(System.in);
		while(running){
			String input = scan.nextLine();
			
			input = input.trim();
			
			if(input.isEmpty()) continue;
			
			if(input.contains("=")){
				int i = input.indexOf("=");
				String var = input.substring(0, i).trim();
				String val = input.substring(i+1, input.length()).trim();
				
				setVariable(var, val);
				
			}else if(input.equals("quit")){
				running = false;
			}else if(input.equals("vars")){
				printVariables();
			}else if(input.equals("operators")){
				printOperators();
			}else if(input.startsWith("removeVar")){
				k.removeVar(input.substring(10));
			}else if(input.equals("?")){
				printHelp();
			}else{
				try{
					Operator tree = k.createTree(input);
					k.calculateVariables();
					double v = k.calculateTree(tree);
					//tree.print();
					System.out.println("= "+v);
					setVariable("ANS", Double.toString(v));
				}catch(MatteException me){ //matematiska fel med utr�ckningen h�nde
					System.out.println("You broke math! "+me.getMessage());
				}catch(TreeException e){ //syntaxfel h�nde
					printIndexArrow(e.getIndex());
					System.out.println("Could not generate tree. "+e.getMessage());
				}catch(Exception e){ //n�got annat konstigt fel h�nde
					e.printStackTrace();
					running = false;
				}
			}
		}
		
		scan.close();
		System.out.println("itterasshai!"); //Ganska s�ker p� att det betyder "hejd�" eller n�got liknande
	}
	
	/**
	 * S�tter en variabel till ett v�rde p� minir�knarobjektet. 
	 * F�ngar exceptions som kommer och skriver ut dem.
	 * <p>
	 * v�rdet kan vara ett uttryck
	 * 
	 * @param var variabelnamnet
	 * @param val v�rdet som variabeln ska ha
	 */
	private void setVariable(String var, String val){
		try{
			k.setVariable(var, val);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Skriver ut en pil under uttrycket som ska visa vart felet �r n�gonstans.
	 * 
	 * @param index Platsen d�r pilen ska skrivas ut
	 */
	private void printIndexArrow(int index){
		for(int i = 0; i < index; i++){
			System.out.print(" ");
		}
		System.out.println("^");
	}
	
	/**
	 * Skriver ut alla variabler i minir�knarobjektet i en snygg tabell med hj�lp av {@link TablePrint}
	 */
	private void printVariables(){
		HashMap<String, String> map = k.getVariables();
		Iterator<String> ite = map.keySet().iterator();
		
		System.out.println("Current variables:");
		
		TablePrint tp = new TablePrint(2, map.size()+1);
		tp.setRow(0, new String[]{"Name", ": Value"});
		
		String val;
		String key;
		int count = 1;
		while(ite.hasNext()){
			key = ite.next();
			val = map.get(key);
			tp.setRow(count++, new String[]{key, ": "+val});
		}
		
		tp.print();
	}
	
	/**
	 * Skriver ut alla operators som minir�knarobjektet kan hantera.
	 */
	private void printOperators(){
		Set<String> ope = k.getOperators();
		ArrayList<String> oper = new ArrayList<>(ope);
		Collections.sort(oper);
		
		System.out.println("Available operators:");
		for(String s : oper)
			System.out.println(s);
	}
	
	/**
	 * Skriver ut en fin hj�lpmeny med alla kommandon man kan g�ra.
	 * <p>
	 * Anv�nder {@link TablePrint} f�r att f� en snygg utskrift.
	 */
	private void printHelp(){
		System.out.println("Eriks fantastiska hj�lpmeny!");
		System.out.println();
		TablePrint tp = new TablePrint(2, 12);
		tp.setRow(0, new String[]{"Command", "Action"});
		tp.setRow(1, new String[]{"quit", "Quits the program"});
		tp.setRow(2, new String[]{"vars", "Prints all variables"});
		tp.setRow(3, new String[]{"operators", "Prints all available operators"});
		tp.setRow(4, new String[]{"[var] = [expression]", "Sets [var] to equal [expression]"});
		tp.setRow(5, new String[]{"", "x = y"});
		tp.setRow(6, new String[]{"", "nils = 2*jakob+COS(4)"});
		tp.setRow(7, new String[]{"[expression]", "calculates [expression]"});
		tp.setRow(8, new String[]{"", "(5*SQRT(2))/pi"});
		tp.setRow(9, new String[]{"removeVar [variable]", "removes [variable]"});
		tp.setRow(10, new String[]{"", "removeVar x"});
		tp.setRow(11, new String[]{"ANS", "use latest answer"});
		
		tp.print();
	}
	
}
