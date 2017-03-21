import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class SudokuSolver {
	
	protected final static int ROWS = 9;
	protected final static int COLS = 9;
	
	protected static Cell[][] grid;	// two dimensional grid
	
	File inputFile;
	
	// keep track
	static long startTime;
	static long endTime;
	int nodesVisited = 0;		// number of nodes visited ( the number of variables assigned)
	ArrayList<Cell> freeVars;

	
	/**
	 * constructor
	 * @param filename
	 * @throws IOException 
	 */
	public SudokuSolver(File file) throws IOException
	{
		inputFile = file;
		grid = new Cell[ROWS][COLS];	// Instantiate the grid
		readFile(inputFile);
		printGrid();
		
		// naive backtracking
	}
	

	/**
	 * 
	 * @param file
	 * @throws IOException
	 * Ref.:https://docs.oracle.com/javase/tutorial/essential/io/scanning.html
	 */
	protected static void readFile(File file) throws IOException	{
		
		Scanner s = null;
		try {
            s = new Scanner(new BufferedReader(new FileReader(file)));
            
            for (int i = 0; i < ROWS; i++)		{
            	for (int j = 0 ; j < COLS; j++)		{
            		if (s.hasNext()) {
                    	String str = s.next(); 
                    	grid[i][j] = new Cell(i, j, Integer.parseInt(str));
//                      char[] myChar = str.toCharArray();
                    }
            	}
            }
            
        	} finally {
	            if (s != null) {
	                s.close();
	            }
        }
		
	}
	
	

	/**
	 * Prints the grid out to the console
	 */
	public void printGrid() {
		String output = "";
		for (int i = 0; i < ROWS; i++)		{
        	for (int j = 0 ; j < COLS; j++)		{
				output += grid[i][j].val + " ";
			}
			output += "\n";
		}
		System.out.println(output);
	}
	
	// Backtracking with forward checking
	// i. Whenever a variable X is assigned a value, 
//	check all variables Y connected to X by a constraint and delete from Y’s domain, 
//	any value that is inconsistent with the value chosen for X
	  
//	ii. As soon as the domain of any variable becomes empty, backtrack

	public void backtraking()	{
		
	}
	
	// if a variable is assigned with certain value
	// deletes value from the domains of the connected variables 
	
	public void forwardChecking(Cell currCell, Integer tempVal)	{
		for (int i = 0; i < freeVars.size(); i++)	{
			if (checkConnected(currCell, freeVars.get(i)))	{
				if (freeVars.get(i).domain.contains(tempVal))
					freeVars.get(i).domain.remove(tempVal);
			}
		}
	}
	
	/**
	 * Checks two cells passed in if they are connected
	 * @param main
	 * @param cell
	 * @return
	 */
	public boolean checkConnected(Cell main, Cell cell)	{
		
		// main cell
		int c1 = main.col;
		int r1 = main.row;
		
		// cell to be checked
		int c2 = cell.col;
		int r2 = cell.row;
		
		
		// same block
		for (int i = (c1/3)*3; i < (c1/3)*3 + 3; i++)	{
			for (int j = (r1/3)*3; j < (r1/3)*3 + 3; j++)
				if ( i == c2 && j == r2)
					return true;
		}
		
		// same row or same col
		if (c1 == c2|| r1 == r2)
			return true;
		
		// else
		return false;
		
	}
	
	
	/**
	 * Main method that takes arguments from command line
	 */
	public static void main(String[] args) throws IOException	{
		
		File file = new File(args[0]);
		SudokuSolver sudoku = new SudokuSolver(file);
        
	}  
	
}

