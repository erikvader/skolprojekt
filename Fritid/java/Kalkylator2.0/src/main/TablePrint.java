package main;

/**
 * Creates a class which prints out data in a nice even table.
 * 
 * @author ErRi0401
 *
 */

public class TablePrint {

	private int width, height;
	private int spacing = 1;
	private String[][] table;
	private int[][] extra;
	private boolean needUpdate = true;
	
	/**
	 * Creates a new TablePrint object with the specified width and height.
	 * TablePrint prints out a table with cells that have an even spacing between them. 
	 * 
	 * @param x - the width of the table
	 * @param y - the height of the table
	 */
	public TablePrint(int x, int y){
		width = x;
		height = y;
		table = new String[x][y];
		extra = new int[width][height];
	}
	
	/**
	 * Changes a single cell in this table at the position X and Y to S.
	 * (0,0) is the top left corner.
	 * 
	 * @param x - the x coordinate
	 * @param y - the y coordinate
	 * @param s - the new value
	 */
	public void setCell(int x, int y, String s){
		table[x][y] = s;
		needUpdate = true;
	}
	
	/**
	 * Changes an entire row to new values. If the array has more elements than the 
	 * width of this table, errors will be thrown. If the array is smaller than the
	 * width of this table only the array.length first items will be changed.
	 * <p>
	 * 0 is the top row.
	 * 
	 * @param y - the row you want to change
	 * @param row - the array with new values
	 */
	public void setRow(int y, String[] row){
		for(int i = 0; i < row.length; i++){
			table[i][y] = row[i];
		}
		needUpdate = true;
	}
	
	/**
	 * Sets the entire table in this table to a new one.
	 * 
	 * @param table - the new table
	 */
	public void setTable(String[][] table){
		this.table = table;
		needUpdate = true;
	}
	
	/**
	 * changes the spacing between each column to a new value.
	 * 
	 * @param s - the new spacing
	 */
	public void setSpacing(int s){spacing = s;}
	
	/**
	 * Prints the entire table to System.out
	 */
	public void print(){
		if(needUpdate){
			needUpdate = false;
			fixSettings();
		}
		
		for(int i = 0; i < height; i++){
			for(int j = 0; j < width; j++){
				System.out.print(table[j][i]+extraSpaces(j, i)+spacingSpaces());
			}
			System.out.println();
		}
		
	}
	
	/**
	 * Helper method for print. Creates a String with enough empty spaces
	 * for the specified cell.
	 * 
	 * @param x
	 * @param y
	 * @return
	 * @see print
	 */
	private String extraSpaces(int x, int y){
		int t = extra[x][y];
		String f = "";
		for(int i = 0; i < t; i++){
			f += " ";
		}
		return f;
	}
	
	/**
	 * Helper method for print. Creates a String for the spacing.
	 * 
	 * @return
	 * @see print
	 */
	private String spacingSpaces(){
		String f = "";
		for(int i = 0; i < spacing; i++){
			f += " ";
		}
		return f;
	}
	
	/**
	 * Helper method for print. Calculates the amount of extra spaces every cell
	 * should have to look nice.
	 * 
	 * @see print
	 */
	private void fixSettings(){
		int[] longest = new int[width];
		
		//hitta de längsta strängarna i varje column
		for(int i = 0; i < width; i++){
			longest[i] = table[i][0].length();
			for(int j = 1; j < height; j++){
				if(table[i][j].length() > longest[i]){
					longest[i] = table[i][j].length();
				}
			}
		}
		//fixa extras
		for(int i = 0; i < width; i++){
			for(int j = 0; j < extra[i].length; j++){
				extra[i][j] = longest[i]-table[i][j].length();
			}
		}
	}
}
