import java.util.Arrays;
import java.util.Scanner;

public class Gauss {

	public static void main(String[] args) {
		new Gauss().run();
	}
	
	public double[][] matrix; //rad, col
	
	public void run(){
		Scanner scan = new Scanner(System.in);
		System.out.println("hur många ekvationer?");
		int antal = Integer.parseInt(scan.nextLine());
		
		matrix = new double[antal][antal+1];
		
		for(int i = 0; i < antal; i++){
			for(int j = 0; j < antal+1; j++){
				if(j == antal)
					System.out.print("= ");
				else
					System.out.print((char)(65+j)+""+(i+1)+" ");
				matrix[i][j] = scan.nextDouble();
			}
		}
		
		scan.close();
		
		gaussaJudarna();
		
		for(int i = 0; i < antal; i++){
			System.out.println((char)(65+i)+"="+matrix[i][antal]);
		}
	}
	
	public void gaussaJudarna(){
		//gauss eliminering
		for(int i = 0; i < matrix.length-1; i++){ //nuvarande raden
			double cur = matrix[i][i];
			for(int j = i+1; j < matrix.length; j++){//rad vi ta bort ifrån
				double taBort = matrix[j][i];
				addToRow(i, j, -(taBort/cur));
			}
		}
		
		//andra direction
		for(int i = matrix.length-1; i > 0 ; i--){ //nuvarande raden
			double cur = matrix[i][i];
			matrix[i][i] = 1;
			matrix[i][matrix[i].length-1] /= cur;
			cur = 1;
			for(int j = i-1; j >= 0 ; j--){//rad vi ta bort ifrån
				double taBort = matrix[j][i];
				addToRow(i, j, -(taBort/cur));
			}
		}
	}
	
	private void addToRow(int src, int target, double c){
		double[] tempRow = Arrays.copyOf(matrix[src], matrix[src].length);
		for(int i = 0; i < tempRow.length; i++){
			matrix[target][i] += tempRow[i] * c;
		}
		
	}

}
