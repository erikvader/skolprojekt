package mediansAndOrderStatistics;

public class MAOS {

	public static void main(String[] args) {
		new MAOS().run();
	}
	
	public void run(){
		/*
		int[] in = {-20, 4, 5, 20, -18};
		int[] out = new int[2];
		findMaxAndMin(in, out);
		System.out.println(Arrays.toString(out));
		*/
		
		int[] in = {};
		int q = randomizedSelect(in, 1);
		System.out.println(q);
	}
	
	/**
	 * returnar den i:te minste talet i en array.
	 * <br>
	 * arrayen kommer att modifieras
	 * <br>
	 * expected körtid är O(n), kan i värsta fall vara O(n^2)
	 * <br>
	 * i = 1 : minsta talet
	 * <br>
	 * i = 2 : näst minsta talet
	 * <br>
	 * i = in.length : största talet
	 * 
	 * @param in
	 * @return
	 */
	public int randomizedSelect(int[] in, int i){
		return randomizedSelectHelp(in, i, 0, in.length-1);
	}
	
	private int randomizedSelectHelp(int[] in, int i, int l, int r){
		if(l == r){
			return in[l];
		}
		
		int q = randomizedPartition(in, l, r);
		int k = q-l+1; //antalet tal mindre än pivot
		
		if(k == i){
			return in[q];
		}else if(k > i){
			return randomizedSelectHelp(in, i, l, q-1);
		}else{
			return randomizedSelectHelp(in, i-k, q+1, r);
		}
	}
	
	/**
	 * gör partition på a mellan indexen [l, r] (inclusive) på ett random pivot. returnar sen var pivoten hamnade.
	 * <br>
	 * saker som är mindre än pivot hamnar till vänster och större till höger
	 * @param a
	 * @param l
	 * @param r
	 */
	public int randomizedPartition(int[] a, int l, int r){
		
		//random pivot
		int rand = (int)(Math.random()*(r-l+1));
		swap(a, r, l+rand);
		
		//partition
		/* min egna från minne, den i boken är bättre
		int pivot = r;
		int comp = l;
		while(pivot != comp){
			if(comp < pivot){
				if(a[pivot] < a[comp]){
					swap(a, pivot, comp);
					int temp = comp;
					comp = pivot-1;
					pivot = temp;
				}else{
					comp++;
				}
			}else{
				if(a[pivot] > a[comp]){
					swap(a, pivot, comp);
					int temp = comp;
					comp = pivot+1;
					pivot = temp;
				}else{
					comp--;
				}
			}
		}
		
		return pivot;
		*/
		
		int p = a[r];
		int i = l-1; //alla tal mellan l och i är mindre än eller lika med pivot, tal mellan i och j är större
		for(int j = l; j < r; j++){
			if(a[j] <= p){
				i++;
				swap(a, i, j);
			}
		}
		swap(a, i+1, r);
		return i+1;
	}
	
	private void swap(int[] a, int i, int j){
		int temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}
	
	/**
	 * hittar max och minsta i en lista på O(3n/2)
	 * @param in
	 * @param out {i, j} - index för minsta och index för största
	 */
	public void findMaxAndMin(int[] in, int[] out){
		if(in.length == 0) return;
		
		int maxInd = 0, minInd = 0;
		int startInd = 0;
		int[] temp = new int[2];
		if(in.length % 2 == 1){
			startInd = 1;
		}else{
			findMaxAndMinCompare(temp, 0, 1, in);
			maxInd = temp[1];
			minInd = temp[0];
		}
		
		for(int i = startInd; i < in.length; i += 2){
			findMaxAndMinCompare(temp, i, i+1, in);
			if(in[temp[0]] < in[minInd]){
				minInd = temp[0];
			}
			
			if(in[temp[1]] > in[maxInd]){
				maxInd = temp[1];
			}
		}
		
		out[0] = minInd;
		out[1] = maxInd;
	}
	
	private void findMaxAndMinCompare(int[] res, int a, int b, int[] in){
		if(in[a] <= in[b]){
			res[0] = a;
			res[1] = b;
		}else{
			res[0] = b;
			res[1] = a;
		}
	}

}
