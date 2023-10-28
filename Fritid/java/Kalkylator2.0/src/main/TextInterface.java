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
 * Denna klass tar endast handom kommunikationen mellan miniräknaren och användaren. Den kommunicerar genom
 * att skriva ut text i standard output och genom att ta emot indata genom standard input. 
 * 
 * @author ErRi0401
 *
 */
public class TextInterface {

	private boolean running = true;
	
	private Kalkylator k;
	
	/**
	 * Startar mainloopen för programmet
	 */
	public void run(){
		System.out.println("Okaerinasaimase, goshujinsama!"); //"välkommen tillbaka mästare", någonting som maids i maid cafés i japan säger
		System.out.println("\"?\" for help");
		System.out.println("Var vänlig skriv in ett matematiskt uttryck:");
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
				}catch(MatteException me){ //matematiska fel med uträckningen hände
					System.out.println("You broke math! "+me.getMessage());
				}catch(TreeException e){ //syntaxfel hände
					printIndexArrow(e.getIndex());
					System.out.println("Could not generate tree. "+e.getMessage());
				}catch(Exception e){ //något annat konstigt fel hände
					e.printStackTrace();
					running = false;
				}
			}
		}
		
		scan.close();
		System.out.println("itterasshai!"); //Ganska säker på att det betyder "hejdå" eller något liknande
	}
	
	/**
	 * Sätter en variabel till ett värde på miniräknarobjektet. 
	 * Fångar exceptions som kommer och skriver ut dem.
	 * <p>
	 * värdet kan vara ett uttryck
	 * 
	 * @param var variabelnamnet
	 * @param val värdet som variabeln ska ha
	 */
	private void setVariable(String var, String val){
		try{
			k.setVariable(var, val);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Skriver ut en pil under uttrycket som ska visa vart felet är någonstans.
	 * 
	 * @param index Platsen där pilen ska skrivas ut
	 */
	private void printIndexArrow(int index){
		for(int i = 0; i < index; i++){
			System.out.print(" ");
		}
		System.out.println("^");
	}
	
	/**
	 * Skriver ut alla variabler i miniräknarobjektet i en snygg tabell med hjälp av {@link TablePrint}
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
	 * Skriver ut alla operators som miniräknarobjektet kan hantera.
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
	 * Skriver ut en fin hjälpmeny med alla kommandon man kan göra.
	 * <p>
	 * Använder {@link TablePrint} för att få en snygg utskrift.
	 */
	private void printHelp(){
		System.out.println("Eriks fantastiska hjälpmeny!");
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
