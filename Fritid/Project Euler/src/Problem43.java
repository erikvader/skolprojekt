public class Problem43 {

	public static void main(String[] args){
		long t = System.nanoTime();
		new Problem43().run();
		System.out.println((System.nanoTime() - t)/1000000.0);
	}
	
	boolean[] inUse;
	String word = "0123456789";
	long sum = 0;
	
	public void run(){
		inUse = new boolean[10];
		recurs("");
		System.out.println(sum);
	}
	
	public void recurs(String prev){
		if(prev.length() == 10){
			if(Integer.parseInt(prev.substring(7)) % 17 != 0) return;
			sum += Long.parseLong(prev);
		}else if(prev.length() == 4){
			if(Integer.parseInt(prev.substring(1)) % 2 != 0) return;
		}else if(prev.length() == 5){
			if(Integer.parseInt(prev.substring(2)) % 3 != 0) return;
		}else if(prev.length() == 6){
			if(Integer.parseInt(prev.substring(3)) % 5 != 0) return;
		}else if(prev.length() == 7){
			if(Integer.parseInt(prev.substring(4)) % 7 != 0) return;
		}else if(prev.length() == 8){
			if(Integer.parseInt(prev.substring(5)) % 11 != 0) return;
		}else if(prev.length() == 9){
			if(Integer.parseInt(prev.substring(6)) % 13 != 0) return;
		}
		
		for(int i = 0; i < word.length(); i++){
			char c = word.charAt(i);
			if(!inUse[i]){
				inUse[i] = true;
				recurs(prev+c);
				inUse[i] = false;
			}
		}
		
	}

}
