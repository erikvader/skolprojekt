package sorting;



public class Main {


	public static void main(String[] args) {
		new Main().run();
	}
	
	public void run(){
		/*
		//int[] lista = {3, 6, 7, 2, 2, 5, 1, 1};
		int n = 8;
		int[] lista = new int[n];
		generateListaOrdning(lista, n);
		merge(lista);
		
		for(int x : lista) System.out.print(x+" ");
		System.out.println();
		*/
		
		//benchmark
		int avg = 1;
		int length = 1000;
		
		int[][] listor = new int[avg][length];
		for(int i = 0; i < avg; i++){
			int[] temp = new int[length];
			generateListaOrdning(temp, length);
			listor[i] = temp;
		}
		
		System.out.println("genererat lista");
		
		Grej[] grejer = {
			new Grej(){
				@Override
				public void hej(int[] lista) {
					insertion(lista);
			}}, 
			new Grej(){
				@Override
				public void hej(int[] lista) {
					bubble(lista);
			}},
			new Grej(){
				@Override
				public void hej(int[] lista) {
					selection(lista);
			}},
			new Grej(){
				@Override
				public void hej(int[] lista) {
					quick(lista);
			}},
			new Grej(){
				@Override
				public void hej(int[] lista) {
					merge(lista);
			}}};
		
		long[] times = new long[grejer.length];
		int[] current;
		
		for(int j = 0; j < grejer.length; j++){
			for(int i = 0; i < avg; i++){
				current = listor[i].clone();
				long before = System.nanoTime();
				grejer[j].hej(current);
				long after = System.nanoTime();
				times[j] += after-before;
			}
			times[j] /= avg;
			System.out.println("klar "+(j+1));
		}
		
		System.out.println("average of "+avg+", length of "+length);
		System.out.println("insertion: "+times[0]/1000000.0);
		System.out.println("bubble: "+times[1]/1000000.0);
		System.out.println("selection: "+times[2]/1000000.0);
		System.out.println("quick: "+times[3]/1000000.0);
		System.out.println("merge: "+times[4]/1000000.0);
		
		
	}
	
	public void generateLista(int[] lista, int n){
		for(int i = 0; i < n; i++){
			lista[i] = 1+(int)(n*Math.random());
		}
	}
	
	
	public void generateListaOrdning(int[] lista, int n){
		for(int i = 0; i < n; i++) lista[i] = i+1;
		
		fisherYates(lista);
	}
	
	//Fisher-Yates Shuffle Algorithm
	public void fisherYates(int[] lista){
		for(int i = lista.length-1; i > 0; i--){
			int k = (int)((i+1)*Math.random());
			int temp = lista[k];
			lista[k] = lista[i];
			lista[i] = temp;
		}
	}
	
	
	//insertion sort
	public void insertion(int[] lista){
		int i;
		if(lista.length < 2) return;
		int temp;

		for(int n = 1; n < lista.length; n++){
			temp = lista[n];
			i = n-1;
			while(i >= 0 && lista[i] > temp){
				lista[i+1] = lista[i];
				i--;
			}
			lista[i+1] = temp;
		}

	}
	
	//bubble sort
	public void bubble(int[] lista){
		for(int m = lista.length-1; m > 0; m--){
			for(int n = 0; n < m; n++){
				if(lista[n] > lista[n+1]){
					int temp = lista[n];
					lista[n] = lista[n+1];
					lista[n+1] = temp;
				}
			}
		}
	}
	
	//selection sort
	public void selection(int[] lista){
		for(int i = 0; i < lista.length-1; i++){
			int min = i;
			for(int n = i+1; n < lista.length; n++){
				if(lista[n] < lista[min]){
					min = n;
				}
			}
			int temp = lista[i];
			lista[i] = lista[min];
			lista[min] = temp;
		}
	}
	
	//quick sort
	public void quick(int[] lista){
		quicksort(lista, 0, lista.length-1);
	}
	
	private void quicksort(int[] lista, int left, int right){
		if(left < right){
			int p = partition(lista, left, right);
			quicksort(lista, left, p-1);
			quicksort(lista, p+1, right);
		}
	}
	
	private int partition(int[] lista, int left, int right){
		int pivot = lista[left];
		int l = left;
		int r = right;
		boolean moveRight = true;
		while(true){
			if(moveRight){
				if(lista[r] >= pivot){
					r--;
				}else{
					moveRight = false;
					int temp = lista[r];
					lista[r] = pivot;
					lista[l] = temp;
				}
			}else{
				if(lista[l] <= pivot){
					l++;
				}else{
					moveRight = true;
					int temp = lista[l];
					lista[l] = pivot;
					lista[r] = temp;
				}
			}
			if(r == l){
				return r;
			}
		}
	}
	
	//merge sort
	public void merge(int[] lista){
		int[] lista2 = new int[lista.length];
		for(int i = 0; i < lista.length; i++){ //kan förbättras genom att kolla vilken man kommer att ta från först
			lista2[i] = lista[i];
		}
		
		mergeSort(lista2, lista, true, 0, lista.length);
	}
	
	private void mergeSort(int[] sorted, int[] work, boolean toggler, int index, int length){
		//base case
		if(length == 1){
			return;
		}
		
		//splitta
		int index1 = index;
		int length1 = (length+1)/2;
		int index2 = index + (length+1)/2;
		int length2 = length - length1;
		
		//recursive
		mergeSort(sorted, work, !toggler, index1, length1);
		mergeSort(sorted, work, !toggler, index2, length2);
		
		//merge
		int[] from, to;
		if(toggler){
			from = sorted; to = work;
		}else{
			from = work; to = sorted;
		}
		int index1Pointer = index1;
		int index2Pointer = index2;
		for(int i = index; i < index+length; i++){
			if(index1Pointer >= index1+length1 || index1Pointer >= index+length){ //om index1 är tom, ta från två
				to[i] = from[index2Pointer];
				index2Pointer++;
				continue;
			}
			if(index2Pointer >= index2+length2 || index2Pointer >= index+length){ //om index2 är tom, ta från ett
				to[i] = from[index1Pointer];
				index1Pointer++;
				continue;
			}
			if(from[index1Pointer] < from[index2Pointer]){ //ingen är tom, gemför storlek
				to[i] = from[index1Pointer];
				index1Pointer++;
				continue;
			}else{
				to[i] = from[index2Pointer];
				index2Pointer++;
				continue;
			}
		}
		
		
	}
	

}











