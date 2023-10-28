
public class Problem15 {

	public static void main(String[] args) {
		new Problem15().run();
	}
	
	int size = 20;
	long[][] memo = new long[size+1][size+1];
	
	public void run(){
		memo[0][0] = 1;
		long t = System.nanoTime();
		System.out.println(recurs(size, size));
		t = System.nanoTime()-t;
		System.out.println(t/1000000.0+" ms");
	}
	
	public long recurs(int x, int y){
		if(memo[x][y] != 0){
			return memo[x][y];
		}else{
			long res = 0;
			if(x > 0)
				res += recurs(x-1, y);
			if(y > 0)
				res += recurs(x, y-1);
			memo[x][y] = res;
			return res;
		}
	}

}
