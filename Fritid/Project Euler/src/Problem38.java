
public class Problem38 {

	public static void main(String[] args) {
		String largest = "";
		for(int i = 1; i <= 9999; i++){
			boolean[] visited = new boolean[9];
			int n = 1;
			int counter = 0;
			String progress = "";
			boolean running = true;
			while(running){
				int newTal = n * i;
				progress += newTal;
				n++;
				while(newTal > 0){
					int t = newTal % 10;
					newTal /= 10;
					if(t != 0 && !visited[t-1]){
						visited[t-1] = true;
						counter++;
					}else{
						counter = -1;
						running = false;
						break;
					}
				}
				if(counter == 9){
					running = false;
				}
			}
			
			if(counter == 9){
				if(largest.compareTo(progress) < 0){
					largest = progress;
				}
			}
		}
		
		System.out.println(largest);
	}

}
