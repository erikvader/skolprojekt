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
 * Denna klass tar emot matematiska uttryck som en String, gör om dem till träd som sedan enkelt kan räknas ut.
 * Denna klass har också alla variabler som de matematiska uttrycken kan använda. 
 * 
 * @author ErRi0401
 *
 */
public class Kalkylator {

	//för kalkylatorn
	private HashMap<String, Integer> priorities;
	private HashMap<String, String> variables; //alla variablers string-reperesentationer
	private HashMap<String, Operator> calculated; // alla uträknade träd till variablerna
	private HashMap<String, Double> cache; //håller numeriska värden på variablerna
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
	
	//för check variables
	private Stack<Node> stack = new Stack<Node>();
	private ArrayList<Node> nodes = new ArrayList<Node>();
	private int index = 0;
	private ArrayList<ArrayList<Node>> scc = new ArrayList<>();
	
	//kolal vilka som ska resettas
	private HashSet<Node> marked;
	private HashSet<Node> visited;
	
	/**
	 * Skapar och initierar ett miniräknarobjekt
	 */
	public Kalkylator(){
		init();
	}

	/**
	 * Initierar alla variabler och värden som behövs för att denna miniräknare ska funka.
	 */
	private void init(){
		//init
		priorities = new HashMap<>();//högre siffra betyder att splitta träder vid en sådan först. lägre siffra betyder då att man ska ta dem sist och hamnar därmed längst  ner i trädet därmed att de är viktigast att räkna ut först. 
		priorities.put("+", 6);
		priorities.put("-", 5);
		priorities.put("/", 4);//från vänster till höger
		priorities.put("*", 3);//från vänster till höger
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
		
		associativity = new HashMap<>(); //true är från vänster
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
	 * Skapar ett träd och returnar grundnoden.
	 * 
	 * @param expre uttrycket som ska räknas ut
	 * @return en {@link Operator} som är grundnoden.
	 * @throws TreeException Om ett fel uppstod i skapningen av trädet
	 */
	public Operator createTree(String expre) throws TreeException{
		checkValidUttryck(expre); //slänger en exception om något inte stämmer
		
		return generateTree(expre, 0);
	}
	
	/**
	 * Genererar ett träd från 'expre'. Denna metod körs rekursivt, så den kommer att köras på alla substrängar tills ett träd har skapats. 
	 * Den hittar rätt {@link Operator} i 'expre' och returnar den med rätt subträd som genererats med hjälp av denna metod.
	 * 
	 * @param expre substrängen som ska göras om till ett träd. 
	 * @param globalIndex indexet som denna substräng har i huvuduttrycket.
	 * @return En operator med rätt child-operatorer.
	 * @throws TreeException Slängs ifall ett fel med skapningen av detta träd uppstod.
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
		
		//leta efter alla operators på samma level
		
		String word = "";
		String operator = "";
		int highestIndex = -1;
		int highestPrio = -1;
		int parantesCount = 0;
		for(int i = 0; i < expre.length(); i++){
			char c = expre.charAt(i);
			//räkna paranteser
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
		
		//ta den med lägst prioritet + - / *
		if(highestPrio == -1){
			return getNumber(expre, globalIndex);
		}else{
			return getCorrectOperator(expre, highestIndex, operator, globalIndex);
		}
		
	}
	
	/**
	 * Kollar ifall 'expre' är en variabel eller om det är en siffra, om det inte är något av det så slängs ett {@link TreeException}
	 * Om det är en variabel kör den 'generateTree(String, int)' och returnar det trädet.
	 * 
	 * @param expre siffran eller variabeln
	 * @param globalIndex globalindex där denna siffra är i huvudsträngen
	 * @return en {@link Operator} som är ett nummer eller ett träd.
	 * @throws TreeException Ifall ett fel i skapelsen av detta träd uppstod, eller om 'expre' inte är en siffra eller en variabel.
	 */
	private Operator getNumber(String expre, int globalIndex) throws TreeException{
		if(variables.containsKey(expre)){
			//return evaluateVariable(expre, globalIndex);
			return new Variable(expre, calculated, cache);
		}
		
		try{
			return new Number(Double.parseDouble(expre));
		}catch(Exception e){
			throw new TreeException("\""+expre+"\" är ingen siffra, variabel eller ett korrekt uttryck on index "+globalIndex, globalIndex);
		}
	}
	
	/**
	 * Genererar ett träd från variablen i strängen 's'.
	 * 
	 * @param s variabeln
	 * @param globalIndex indexet i grundsträngen
	 * @return ett träd som genererats av variabeln
	 * @throws TreeException ifall ett fel inträffade i skapningen av trädet. 
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
	 * Genererar träd alla träd för alla variabler. måste köras innan man försöker
	 *  att få värdet av hela trädet som har variabler i sig. 
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
	 * Tar emot substrängen 'exp' och splittar den vid operatorn som finns på indexet 'hi', den returnar sedan
	 * korrekt 'Operator' med rätt children som genererats av generateTree()
	 * 
	 * @param exp substrängen som innehåller yttrycket
	 * @param hi indexet där substrängen ska plittas på
	 * @param operator vad operatorn heter
	 * @param globalIndex indexet där substrängen finns på i grundsträngen
	 * @return korrekt Operator
	 * @throws TreeException ifall ett fel uppstod i skapningen av trädet. 
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
		//finns inget räknesätt som behöver denna, än.
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
	 * kollar om left på getCorrectOperator ska ignoreras ifall en UnaryOperator ska bli ett träd där vänstersidan inte behövs.
	 * 
	 * @param left
	 * @return if är okej eller inte
	 */
	private boolean checkLeft(String left){
		if(left.length() == 0) return false;
		
		for(int i = 0; i < leftIgnores.length; i++)
			if(left.equals(leftIgnores[i]))
				return false;
		return true;
	}
	
	/**
	 * kollar om right på getCorrectOperator ska ignoreras ifall en UnaryOperator ska bli ett träd där högersidan inte behövs.
	 * 
	 * @param right
	 * @return if är okej eller inte
	 */
	private boolean checkRight(String right){
		if(right.length() == 0) return false;
		
		for(int i = 0; i < rightIgnores.length; i++)
			if(right.equals(rightIgnores[i]))
				return false;
		return true;
	}
	
	/**
	 * Tar bort alla onödiga paranteser som omringar hela uttrycket.
	 * 
	 * @param expre uttrycket
	 * @return uttrycket utan onödiga omringande paranteser
	 */
	private String removeParanteser(String expre){ //ta bort onödiga paranteset som omringar hela uttrycket
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
	 * Kollar några enkla syntaxfel som ett uttryck kan ha. För tillfället kollar den endast
	 * ifall alla paranteser är korrekt utsatta. 
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
			throw new TreeException("Paranteser går inte ihop");
		
		
		
	}
	
	/**
	 * Tar emot ett träd och räknar ut värdet på det. 
	 * 
	 * @param tree trädet som ska räknas ut.
	 * @return värdet som trädet hade
	 * @throws MatteException ifall ett matematiskt fel uppstod
	 */
	public double calculateTree(Operator tree) throws MatteException{
		return tree.calculate();
	}
	
	/**
	 * 
	 * tittar om det finns variabler som kommer att skapa problem
	 * slänger ut alla variabler i en graf och kollar om det finns strongly connected components (typ loopar),
	 *  om det finns loopar så kommer man att fastna i en oändlig loop och programmet kraschar.
	 *  <p>
	 *  Denna metod skapar alla noder och kör strongConnect() på alla noder. 
	 *  
	 *  @return returnar true om inga problem hittades
	 */
	private boolean checkVariables(){ 
		nodes.clear();
		
		//lägg till alla noder
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
		
		//kolla om några strong connections är större än 1
		for(ArrayList<Node> s : scc){
			if(s.size() > 1){
				return false;
			}
		}
		
		return true;
	}

	/**
	 * Kör algoritmen för Strongly connected components.
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
		
		//om det är en root node, generera SSC
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
	 * hittar alla noder som 'curNode' behöver för att kunna uträknas. Den går genom
	 * 'expre' efter andra variabler.
	 * 
	 * @param curNode noden att kolla
	 * @param expre uttrycket där alla links finns.
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
	 * Gör om String till {@link Node}. Loopar genom alla noder och returnar den som stämmer överens med 'word'.
	 * 
	 * @param word namnet på noden som ska hittas.
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
	 * resettar cache på alla variabler som beror på varc.
	 * <br>
	 * måste generara nodes på annat sätt, såsom via checkVariables().
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
	 * går på varje nod och kollar saker
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
	 * Sätter variabeln 'var' till att ha värdet 'val', den kollar sedan med hjälp av
	 * checkVariables() ifall det kommer att uppstå problem. Om problem hittades slänger den en {@link Exception} 
	 * och återställer variablen 'var' till dess gamla värde.
	 * 
	 * @param var namnet på variablen
	 * @param val värdet på variabeln
	 * @throws Exception 
	 */
	public void setVariable(String var, String val) throws Exception{
		String oldVal = variables.get(var);
		variables.put(var, val);
		boolean willWork = checkVariables(); //kollar om det funkar
		if(!willWork){ //om det inte funkade så ta bort den senaste variablen igen
			variables.remove(var);
			if(oldVal != null) variables.put(var, oldVal);
			
			throw new Exception("Can not assign that variable, an infinate loop is most likely created");
		}else{
			//kolla vilka variabler som behöver räknas om
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
	 * returnar alla variabler med dess värden.
	 * 
	 * @return alla variabler
	 */
	public HashMap<String, String> getVariables(){
		return variables;
	}
	
	/**
	 * returnar ett {@link Set} med alla tillgängliga operators som denna klass kan hantera.
	 * 
	 * @return
	 */
	public Set<String> getOperators(){
		return priorities.keySet();
	}
	
}
