import java.util.ArrayList;
import java.util.Scanner;

public class Brandvagg_new {

	public static void main(String args[]){
		new Brandvagg_new().run();
	}
	
	//historik
	public ArrayList<String> historik = new ArrayList<String>();
	//public HashMap<String, AtomicInteger> historikCount = new HashMap<String, AtomicInteger>();
	public int historikIndex = 0;
	
	//rule
	public static final int ACCEPT = 0;
	public static final int DROP = 1;
	public static final int LOG = 2;
	
	public int numRules, numPaket;
	
	ArrayList<Rule> rules = new ArrayList<Rule>();
	
	public void run(){
		Scanner scan = new Scanner(System.in);
		
		int numRules = Integer.parseInt(scan.nextLine());
		
		String input;
		int action = 0;
		for(int i = 0; i < numRules; i++){
			input = scan.nextLine();
			if(input.startsWith("a")){ //accept
				action = ACCEPT;
			}else if(input.startsWith("d")){
				action = DROP;
			}else if(input.startsWith("l")){
				action = LOG;
			}
			
			int space = input.indexOf(' ');
			String cond = "";
			if(space != -1){
				cond = input.substring(space+1, input.length());
			}
			Rule r = new Rule(action, cond);
			if(!r.worthless){
				rules.add(r);
			}
		}
		
		numPaket = Integer.parseInt(scan.nextLine());
		
		/*
		String[] paketen = new String[numPaket];
		for(int i = 0; i < numPaket; i++){
			paketen[i] = scan.nextLine();
		}
		*/
		for(int i = 0; i < numPaket; i++){
			input = scan.nextLine(); //paketen[i];
			addToHistorik(input);
			
			Rule r;
			for(int j = 0; j < numRules; j++){
				r = rules.get(j);
				if(r.check(input)){
					if(r.action == ACCEPT){
						System.out.println("accept "+(i+1));
						break;
					}else if(r.action == DROP){
						System.out.println("drop "+(i+1));
						break;
					}else if(r.action == LOG){
						System.out.println("log "+(i+1));
					}
				}
			}
		}
		
		scan.close();
	}
	
	public void addToHistorik(String s){
		String ip = s.substring(0, s.indexOf(':'));
		if(historik.size() < 1000){
			historik.add(ip);
		}else{
			//String oldIp = historik.get(historikIndex);
			historik.set(historikIndex, ip);
			historikIndex++;
			if(historikIndex >= 1000){
				historikIndex = 0;
			}
			
		}
	}
	
	public int countHistorik(String s){
		int svar = 0;
		for(int i = 0; i < historik.size(); i++){
			if(historik.get(i).equals(s)){
				svar++;
			}
		}
		
		return svar;
	}
	
	public class Rule{
		public int action;
		public boolean worthless = false;
		
		//ArrayList<Integer> ports;
		//ArrayList<Integer> limits;
		//ArrayList<String> adresses;
		String ports;
		String adresses;
		int limits = -1;
		
		public Rule(int action, String conditions){
			this.action = action;
			//ports = new ArrayList<Integer>();
			//limits = new ArrayList<Integer>();
			//adresses = new ArrayList<String>();
			
			//StringTokenizer st = new StringTokenizer(conditions);
			conditions += ' ';
			String s;
			int space = -1;
			int oldSpace = -1;
			while((space = conditions.indexOf(' ', space+1)) != -1){
				s = conditions.substring(oldSpace+1, space);
				oldSpace = space;
				if(s.startsWith("i")){//ip
					if(adresses == null){
						adresses = s.substring(s.indexOf('=')+1, s.length());
					}else{
						worthless = true;
						break;
					}
				}else if(s.startsWith("p")){
					if(ports == null){
						ports = s.substring(s.indexOf('=')+1, s.length());
					}else{
						worthless = true;
						break;
					}
				}else if(s.startsWith("l")){
					int newLimit = Integer.parseInt(s.substring(s.indexOf('=')+1, s.length()));
					if(newLimit > limits){
						limits = newLimit;
					}
				}
			
			}
		}
		
		public boolean check(String s){
			int i = s.indexOf(':');
			String ip = s.substring(0, i);
			String port = s.substring(i+1, s.length());
			
			if(!checkPorts(port)) return false;
			if(!checkIP(ip)) return false;
			if(!checkLimits(ip)) return false;
			
			return true;
			
		}
		
		private boolean checkLimits(String ip){
			if(limits == -1) return true;
			
			int count = countHistorik(ip);
			if(count < limits) return false; //count >= j
			
			
			return true;
		}
		
		private boolean checkIP(String ip){
			if(adresses == null) return true;
			
			if(!adresses.equals(ip)) return false;
			
			return true;
		}
		
		private boolean checkPorts(String port){
			if(ports == null) return true;
			
			if(!ports.equals(port)){
				return false;
			}
			
			return true;
		}
	}
	
}
