
public class Problem24 {
	
	public static int counter = 0;
	public static char[] items = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
	public static boolean[] visited = new boolean[10];
	
	public static void main(String[] args) {
		System.out.println(search(""));
	}
	
	public static String search(String cur){
		if(cur.length() == items.length){
			counter++;
			if(counter == 1000000){
				return cur;
			}
		}
		
		for(int i = 0; i < items.length; i++){
			if(!visited[i]){
				visited[i] = true;
				String s = search(cur+items[i]);
				visited[i] = false;
				if(s.length() != 0)
					return s;
			}
		}
		
		return "";
	}

}
