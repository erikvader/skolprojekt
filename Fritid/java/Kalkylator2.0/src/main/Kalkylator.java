package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

import associativity.Associativity;
import exceptions.MatteException;
import exceptions.TreeException;
import operator.Addition;
import operator.ArcCosinus;
import operator.ArcSinus;
import operator.ArcTangens;
import operator.Cosinus;
import operator.Division;
import operator.E;
import operator.Factorial;
import operator.Logarithm;
import operator.Modulo;
import operator.Multiplication;
import operator.NaturalLogarithm;
import operator.Number;
import operator.Operator;
import operator.Power;
import operator.Sinus;
import operator.Squareroot;
import operator.Subtraction;
import operator.Tangens;
import operator.Variable;
import variableCheck.Node;

/**
 * Denna klass tar emot matematiska uttryck som en String, g�r om dem till tr�d som sedan enkelt kan r�knas ut.
 * Denna klass har ocks� alla variabler som de matematiska uttrycken kan anv�nda. 
 * 
 * @author ErRi0401
 *
 */
public class Kalkylator {

	//f�r kalkylatorn
	private HashMap<String, Integer> priorities;
	private HashMap<String, String> variables; //alla variablers string-reperesentationer
	private HashMap<String, Operator> calculated; // alla utr�knade tr�d till variablerna
	private HashMap<String, Double> cache; //h�ller numeriska v�rden p� variablerna
	private HashMap<String, Associativity> associativity;
	
	private Associativity fromLeft = new Associativity(){
		@Override
		public boolean determine(int highestPrio, int prio) {
			return prio >= highestPrio;
		}
	};
	
	private Associativity fromRight = new Associativity(){
		@Override
		public boolean determine(int highestPrio, int prio) {
			return prio > highestPrio;
		}
	};
	
	//check left och right
	private String[] leftIgnores;
	private String[] rightIgnores;
	
	//f�r check variables
	private Stack<Node> stack = new Stack<Node>();
	private ArrayList<Node> nodes = new ArrayList<Node>();
	private int index = 0;
	private ArrayList<ArrayList<Node>> scc = new ArrayList<>();
	
	//kolal vilka som ska resettas
	private HashSet<Node> marked;
	private HashSet<Node> visited;
	
	/**
	 * Skapar och initierar ett minir�knarobjekt
	 */
	public Kalkylator(){
		init();
	}

	/**
	 * Initierar alla variabler och v�rden som beh�vs f�r att denna minir�knare ska funka.
	 */
	private void init(){
		//init
		priorities = new HashMap<>();//h�gre siffra betyder att splitta tr�der vid en s�dan f�rst. l�gre siffra betyder d� att man ska ta dem sist och hamnar d�rmed l�ngst  ner i tr�det d�rmed att de �r viktigast att r�kna ut f�rst. 
		priorities.put("+", 6);
		priorities.put("-", 5);
		priorities.put("/", 4);//fr�n v�nster till h�ger
		priorities.put("*", 3);//fr�n v�nster till h�ger
		priorities.put("%", 2);
		priorities.put("^", 1);
		priorities.put("E", 1);
		priorities.put("COS", 0);
		priorities.put("SIN", 0);
		priorities.put("TAN", 0);
		priorities.put("ACOS", 0);
		priorities.put("ASIN", 0);
		priorities.put("ATAN", 0);
		priorities.put("SQRT", 0);
		priorities.put("!", 0);
		priorities.put("LN", 0);
		priorities.put("LOG", 0);
		
		associativity = new HashMap<>(); //true �r fr�n v�nster
		associativity.put("+", fromLeft);
		associativity.put("-", fromLeft);
		associativity.put("/", fromLeft);
		associativity.put("*", fromLeft);
		associativity.put("%", fromRight);
		associativity.put("^", fromRight);
		associativity.put("E", fromRight);
		associativity.put("COS", fromLeft);
		associativity.put("SIN", fromLeft);
		associativity.put("TAN", fromLeft);
		associativity.put("ACOS", fromLeft);
		associativity.put("ASIN", fromLeft);
		associativity.put("ATAN", fromLeft);
		associativity.put("SQRT", fromLeft);
		associativity.put("!", fromRight);
		associativity.put("LN", fromLeft);
		associativity.put("LOG", fromLeft);
		
		//variables
		variables = new HashMap<>();
		
		variables.put("pi", Double.toString(Math.PI));
		variables.put("c", Double.toString(299792458d));
		variables.put("e", Double.toString(Math.E));
		variables.put("massaProton", "1.6727E(-27)");
		variables.put("massaElektron", "9.109E(-31)");
		variables.put("laddningPartikel", "1.602E(-19)");
		variables.put("plancksKonstant", "6.626E(-34)");
		variables.put("newtonsGravitationskonstant", "6.67E(-11)");
		variables.put("elektriskaKonstanten", "8.85E(-12)");
		variables.put("magnetiskaKonstanten", "12.566E(-7)");
		variables.put("coulombsKonstant", "8.987E9");
		variables.put("u", "1.66E(-27)");
		variables.put("phi", "(1+SQRT(5))/2"); //1.618033988749
		
		rightIgnores = new String[]{};
		leftIgnores = new String[]{"CO", "SI", "TA", "ACO", "ATA", "ASI", "SQR", "L", "LO"}; //sista bokstaven tas bort i substring
		
		calculated = new HashMap<>();
		cache = new HashMap<>();
		
	}
	
	/**
	 * Skapar ett tr�d och returnar grundnoden.
	 * 
	 * @param expre uttrycket som ska r�knas ut
	 * @return en {@link Operator} som �r grundnoden.
	 * @throws TreeException Om ett fel uppstod i skapningen av tr�det
	 */
	public Operator createTree(String expre) throws TreeException{
		checkValidUttryck(expre); //sl�nger en exception om n�got inte st�mmer
		
		return generateTree(expre, 0);
	}
	
	/**
	 * Genererar ett tr�d fr�n 'expre'. Denna metod k�rs rekursivt, s� den kommer att k�ras p� alla substr�ngar tills ett tr�d har skapats. 
	 * Den hittar r�tt {@link Operator} i 'expre' och returnar den med r�tt subtr�d som genererats med hj�lp av denna metod.
	 * 
	 * @param expre substr�ngen som ska g�ras om till ett tr�d. 
	 * @param globalIndex indexet som denna substr�ng har i huvuduttrycket.
	 * @return En operator med r�tt child-operatorer.
	 * @throws TreeException Sl�ngs ifall ett fel med skapningen av detta tr�d uppstod.
	 * @see metoden getCorrectOperator()
	 */
	private Operator generateTree(String expre, int globalIndex) throws TreeException{
		expre = expre.trim();
		
		int lengthInnan = expre.length();
		expre = removeParanteser(expre);
		
		if(expre.isEmpty()){
			return null;
		}
		
		int lengthEfter = expre.length();
		
		globalIndex += (lengthInnan-lengthEfter)/2;
		
		//leta efter alla operators p� samma level
		
		String word = "";
		String operator = "";
		int highestIndex = -1;
		int highestPrio = -1;
		int parantesCount = 0;
		for(int i = 0; i < expre.length(); i++){
			char c = expre.charAt(i);
			//r�kna paranteser
			if(c == '('){
				parantesCount++;
			}else if(c == ')'){
				parantesCount--;
			}
			//kolla operators (singlar)
			if(parantesCount == 0){
				if(priorities.containsKey(Character.toString(c))){
					int prio = priorities.get(Character.toString(c));
					Associativity asso = associativity.get(Character.toString(c));
					if(asso.determine(highestPrio, prio)){
						highestPrio = prio;
						highestIndex = i;
						operator = Character.toString(expre.charAt(i));
					}
				}
			}else if(parantesCount == 1 && c == '(' && !word.equals("")){ //operators (ord)
				if(priorities.containsKey(word)){
					int prio = priorities.get(word);
					Associativity asso = associativity.get(word);
					if(asso.determine(highestPrio, prio)){
						highestPrio = prio;
						highestIndex = i-1;
						operator = word;
					}
				}
			}
			//kolla ord
			if(Character.isAlphabetic(c)){
				word += c;
			}else{
				word = "";
			}
			
		}
		
		//ta den med l�gst prioritet + - / *
		if(highestPrio == -1){
			return getNumber(expre, globalIndex);
		}else{
			return getCorrectOperator(expre, highestIndex, operator, globalIndex);
		}
		
	}
	
	/**
	 * Kollar ifall 'expre' �r en variabel eller om det �r en siffra, om det inte �r n�got av det s� sl�ngs ett {@link TreeException}
	 * Om det �r en variabel k�r den 'generateTree(String, int)' och returnar det tr�det.
	 * 
	 * @param expre siffran eller variabeln
	 * @param globalIndex globalindex d�r denna siffra �r i huvudstr�ngen
	 * @return en {@link Operator} som �r ett nummer eller ett tr�d.
	 * @throws TreeException Ifall ett fel i skapelsen av detta tr�d uppstod, eller om 'expre' inte �r en siffra eller en variabel.
	 */
	private Operator getNumber(String expre, int globalIndex) throws TreeException{
		if(variables.containsKey(expre)){
			//return evaluateVariable(expre, globalIndex);
			return new Variable(expre, calculated, cache);
		}
		
		try{
			return new Number(Double.parseDouble(expre));
		}catch(Exception e){
			throw new TreeException("\""+expre+"\" �r ingen siffra, variabel eller ett korrekt uttryck on index "+globalIndex, globalIndex);
		}
	}
	
	/**
	 * Genererar ett tr�d fr�n variablen i str�ngen 's'.
	 * 
	 * @param s variabeln
	 * @param globalIndex indexet i grundstr�ngen
	 * @return ett tr�d som genererats av variabeln
	 * @throws TreeException ifall ett fel intr�ffade i skapningen av tr�det. 
	 */
	private Operator evaluateVariable(String s) throws TreeException{
		String exp = variables.get(s);
		
		try {
			return generateTree(exp, 0);
		} catch (TreeException e) {
			//e.printStackTrace();
			throw new TreeException("Variable \""+s+"\" didn't generate a valid tree "+e.getMessage());
		}
	}
	
	/**
	 * Genererar tr�d alla tr�d f�r alla variabler. m�ste k�ras innan man f�rs�ker
	 *  att f� v�rdet av hela tr�det som har variabler i sig. 
	 * 
	 * @throws TreeException
	 * @throws MatteException
	 */
	public void calculateVariables() throws TreeException, MatteException{
		Iterator<String> ite = variables.keySet().iterator();
		while(ite.hasNext()){
			String n = ite.next();
			if(!calculated.containsKey(n)){
				Operator tree = evaluateVariable(n);
				calculated.put(n, tree);
			}
		}
	}
	
	/**
	 * 
	 * Tar emot substr�ngen 'exp' och splittar den vid operatorn som finns p� indexet 'hi', den returnar sedan
	 * korrekt 'Operator' med r�tt children som genererats av generateTree()
	 * 
	 * @param exp substr�ngen som inneh�ller yttrycket
	 * @param hi indexet d�r substr�ngen ska plittas p�
	 * @param operator vad operatorn heter
	 * @param globalIndex indexet d�r substr�ngen finns p� i grundstr�ngen
	 * @return korrekt Operator
	 * @throws TreeException ifall ett fel uppstod i skapningen av tr�det. 
	 */
	private Operator getCorrectOperator(String exp, int hi, String operator, int globalIndex) throws TreeException{
		String left = exp.substring(0, hi);
		String right = exp.substring(hi+1, exp.length());
	
		
		Operator leftOpe = null;
		Operator rightOpe = null;
		
		//kolla om left ska ignoreras
		if(checkLeft(left)){
			leftOpe = generateTree(left, globalIndex);
		}
		
		//kolla om right ska ignoreras
		//finns inget r�knes�tt som beh�ver denna, �n.
		if(checkRight(right))
			rightOpe = generateTree(right, globalIndex+hi+1);
		
		try{
			if(operator.equals("+")){
				return new Addition(leftOpe, rightOpe);
			}else if(operator.equals("-")){
				return new Subtraction(leftOpe, rightOpe);
			}else if(operator.equals("*")){
				return new Multiplication(leftOpe, rightOpe);
			}else if(operator.equals("/")){
				return new Division(leftOpe, rightOpe);
			}else if(operator.equals("COS")){
				return new Cosinus(rightOpe);
			}else if(operator.equals("SIN")){
				return new Sinus(rightOpe);
			}else if(operator.equals("TAN")){
				return new Tangens(rightOpe);
			}else if(operator.equals("^")){
				return new Power(leftOpe, rightOpe);
			}else if(operator.equals("E")){
				return new E(leftOpe, rightOpe);
			}else if(operator.equals("ACOS")){
				return new ArcCosinus(rightOpe);
			}else if(operator.equals("ASIN")){
				return new ArcSinus(rightOpe);
			}else if(operator.equals("ATAN")){
				return new ArcTangens(rightOpe);
			}else if(operator.equals("SQRT")){
				return new Squareroot(rightOpe);
			}else if(operator.equals("%")){
				return new Modulo(leftOpe, rightOpe);
			}else if(operator.equals("!")){
				return new Factorial(leftOpe);
			}else if(operator.equals("LN")){
				return new NaturalLogarithm(rightOpe);
			}else if(operator.equals("LOG")){
				return new Logarithm(rightOpe);
			}else{
				return null;
			}
		}catch(TreeException te){
			throw new TreeException(te.getMessage()+" on index "+(hi+globalIndex), (hi+globalIndex));
		}
	}
	
	/**
	 * kollar om left p� getCorrectOperator ska ignoreras ifall en UnaryOperator ska bli ett tr�d d�r v�nstersidan inte beh�vs.
	 * 
	 * @param left
	 * @return if �r okej eller inte
	 */
	private boolean checkLeft(String left){
		if(left.length() == 0) return false;
		
		for(int i = 0; i < leftIgnores.length; i++)
			if(left.equals(leftIgnores[i]))
				return false;
		return true;
	}
	
	/**
	 * kollar om right p� getCorrectOperator ska ignoreras ifall en UnaryOperator ska bli ett tr�d d�r h�gersidan inte beh�vs.
	 * 
	 * @param right
	 * @return if �r okej eller inte
	 */
	private boolean checkRight(String right){
		if(right.length() == 0) return false;
		
		for(int i = 0; i < rightIgnores.length; i++)
			if(right.equals(rightIgnores[i]))
				return false;
		return true;
	}
	
	/**
	 * Tar bort alla on�diga paranteser som omringar hela uttrycket.
	 * 
	 * @param expre uttrycket
	 * @return uttrycket utan on�diga omringande paranteser
	 */
	private String removeParanteser(String expre){ //ta bort on�diga paranteset som omringar hela uttrycket
		boolean loop = true;
		char c;
		while(loop){
			int counter = 1;
			//int start = 0;
			loop = false;
			if(expre.startsWith("(") && expre.endsWith(")")){
				for(int i = 1; i < expre.length(); i++){
					c = expre.charAt(i);
					if(c == '('){
						counter++;
					}else if(c == ')'){
						counter--;
					}
					if(counter == 0){
						if(i == expre.length()-1){
							loop = true;
							expre = expre.substring(1, expre.length()-1);
						}else{
							break;
						}
					}
				}
			}
		}
		return expre;
	}
	
	/**
	 * Kollar n�gra enkla syntaxfel som ett uttryck kan ha. F�r tillf�llet kollar den endast
	 * ifall alla paranteser �r korrekt utsatta. 
	 * 
	 * @param s
	 * @throws TreeException
	 */
	private void checkValidUttryck(String s) throws TreeException{
		//kolla paranteset
		int count = 0;
		char c;
		for(int i = 0; i < s.length(); i++){
			c = s.charAt(i);
			if(c == '(')
				count++;
			else if(c == ')')
				count--;
		}
		
		if(count != 0)
			throw new TreeException("Paranteser g�r inte ihop");
		
		
		
	}
	
	/**
	 * Tar emot ett tr�d och r�knar ut v�rdet p� det. 
	 * 
	 * @param tree tr�det som ska r�knas ut.
	 * @return v�rdet som tr�det hade
	 * @throws MatteException ifall ett matematiskt fel uppstod
	 */
	public double calculateTree(Operator tree) throws MatteException{
		return tree.calculate();
	}
	
	/**
	 * 
	 * tittar om det finns variabler som kommer att skapa problem
	 * sl�nger ut alla variabler i en graf och kollar om det finns strongly connected components (typ loopar),
	 *  om det finns loopar s� kommer man att fastna i en o�ndlig loop och programmet kraschar.
	 *  <p>
	 *  Denna metod skapar alla noder och k�r strongConnect() p� alla noder. 
	 *  
	 *  @return returnar true om inga problem hittades
	 */
	private boolean checkVariables(){ 
		nodes.clear();
		
		//l�gg till alla noder
		Iterator<String> keys = variables.keySet().iterator();
		while(keys.hasNext()){
			nodes.add(new Node(keys.next()));
		}
		
		//fixa bridges
		for(int i = 0; i < nodes.size(); i++){
			Node curNode = nodes.get(i);
			findLinks(curNode, variables.get(curNode.getName()));
		}
		
		//titta om det finns loopar
		
		//leta efter strong connections
		index = 0;
		stack.clear();
		scc.clear();
		for(int i = 0; i < nodes.size(); i++){
			Node n = nodes.get(i);
			if(n.getIndex() == -1){ //not visited
				strongConnect(n);
			}
		}
		
		//kolla om n�gra strong connections �r st�rre �n 1
		for(ArrayList<Node> s : scc){
			if(s.size() > 1){
				return false;
			}
		}
		
		return true;
	}

	/**
	 * K�r algoritmen f�r Strongly connected components.
	 * 
	 * @param node
	 */
	private void strongConnect(Node node){
		node.setIndex(index);
		node.setLowlink(index);
		node.setOnStack(true);
		stack.push(node);
		index++;
		
		//leta bland typ barn
		ArrayList<Node> links = node.accessLinks();
		Node child;
		for(int i = 0; i < links.size(); i++){
			child = links.get(i);
			if(child.getIndex() == -1){//not visited
				strongConnect(child);
				node.setLowlink(Math.min(node.getLowlink(), child.getLowlink()));
			}else if(child.isOnStack()){
				node.setLowlink(Math.min(node.getLowlink(), child.getIndex()));
			}
		}
		
		//om det �r en root node, generera SSC
		if(node.getIndex() == node.getLowlink()){
			Node n = null;
			ArrayList<Node> newSCC = new ArrayList<Node>();
			do{
				n = stack.pop();
				n.setOnStack(false);
				newSCC.add(n);
			}while(n != node);
			scc.add(newSCC);
		}
		
	}
	
	/**
	 * hittar alla noder som 'curNode' beh�ver f�r att kunna utr�knas. Den g�r genom
	 * 'expre' efter andra variabler.
	 * 
	 * @param curNode noden att kolla
	 * @param expre uttrycket d�r alla links finns.
	 */
	private void findLinks(Node curNode, String expre){
		String word = "";
		ArrayList<String> words = new ArrayList<String>();
		for(int i = 0; i < expre.length(); i++){
			char c = expre.charAt(i);
			if(Character.isAlphabetic(c)){
				word += c;
			}else{
				words.add(word);
				word = "";		
			}
		}
		if(!word.equals("")) words.add(word);
		
		for(int i = 0; i < words.size(); i++){
			Node targetNode = findNode(words.get(i));
			
			if(curNode == targetNode){ //om x = x
				throw new RuntimeException("Variable equal to itself");
			}
			
			if(targetNode != null)
				curNode.accessLinks().add(targetNode);
		}
		
		
	}
	
	/**
	 * G�r om String till {@link Node}. Loopar genom alla noder och returnar den som st�mmer �verens med 'word'.
	 * 
	 * @param word namnet p� noden som ska hittas.
	 * @return noden som har namnet i 'word'
	 */
	private Node findNode(String word){
		Node n;
		for(int i = 0; i < nodes.size(); i++){
			n = nodes.get(i);
			if(n.getName().equals(word)){
				return n;
			}
		}
		
		return null;
	}
	
	/**
	 * resettar cache p� alla variabler som beror p� varc.
	 * <br>
	 * m�ste generara nodes p� annat s�tt, s�som via checkVariables().
	 * 
	 * @param varc
	 */
	private void resetVariables(String varc){
		marked = new HashSet<Node>();
		visited = new HashSet<Node>();
		Node n = findNode(varc);
		marked.add(n);
		visited.add(n);
		
		for(int i = 0; i < nodes.size(); i++){
			walk(nodes.get(i));
		}
	}
	
	/**
	 * g�r p� varje nod och kollar saker
	 * 
	 * @param n
	 * @return
	 */
	private boolean walk(Node n){
		if(marked.contains(n)) return true;
		if(visited.add(n) == false) return false;
		
		Node go;
		boolean reset = false;
		for(int i = 0; i < n.accessLinks().size(); i++){
			go = n.accessLinks().get(i);
			reset = walk(go);
			if(reset) break;
		}
		
		if(reset){
			cache.remove(n.getName());
			marked.add(n);
			return true;
		}
		return false;
	}
	
	/**
	 * S�tter variabeln 'var' till att ha v�rdet 'val', den kollar sedan med hj�lp av
	 * checkVariables() ifall det kommer att uppst� problem. Om problem hittades sl�nger den en {@link Exception} 
	 * och �terst�ller variablen 'var' till dess gamla v�rde.
	 * 
	 * @param var namnet p� variablen
	 * @param val v�rdet p� variabeln
	 * @throws Exception 
	 */
	public void setVariable(String var, String val) throws Exception{
		String oldVal = variables.get(var);
		variables.put(var, val);
		boolean willWork = checkVariables(); //kollar om det funkar
		if(!willWork){ //om det inte funkade s� ta bort den senaste variablen igen
			variables.remove(var);
			if(oldVal != null) variables.put(var, oldVal);
			
			throw new Exception("Can not assign that variable, an infinate loop is most likely created");
		}else{
			//kolla vilka variabler som beh�ver r�knas om
			resetVariables(var);
			calculated.remove(var);
			cache.remove(var);
		}
		
	}
	
	/**
	 * Tar bort variabeln 'var' ifall den finns eller inte.
	 * 
	 * @param var variablen som ska tas bort.
	 */
	public void removeVar(String var){
		try {
			setVariable(var, "");
			variables.remove(var);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * returnar alla variabler med dess v�rden.
	 * 
	 * @return alla variabler
	 */
	public HashMap<String, String> getVariables(){
		return variables;
	}
	
	/**
	 * returnar ett {@link Set} med alla tillg�ngliga operators som denna klass kan hantera.
	 * 
	 * @return
	 */
	public Set<String> getOperators(){
		return priorities.keySet();
	}
	
}
