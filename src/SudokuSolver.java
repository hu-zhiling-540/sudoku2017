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
	
	ArrayList<Cell> validCells;
	ArrayList<Cell> invalidCells;
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
	 * Backtracking with forward checking
	 */
	public void bktrkFwdCkg()	{
		// start with a cell that has the largest domain
		Cell toExamine = largestDomain(invalidCells);
		backtracking(toExamine);
		
	}
	
	
	/**
	 * Runs backtracking on a cell
	 * @param currCell
	 * @return
	 */
	public boolean backtracking(Cell currCell)	{
		freeVars.remove(currCell);
		for (int i = 0; i < currCell.domain.size(); i++)	{
			Integer val = currCell.domain.get(0);
			if (forwardChecking(currCell, val))		{
				Cell next = largestDomain(freeVars);
				if (backtracking(next))
					return true;
			}
		}
		return false;
	}
	
	
	/**
	 * If a variable is assigned with certain value,
	 * deletes value from the domains of the connected variables.
	 * @param currCell
	 * @param tempVal
	 * @return
	 */
	public boolean forwardChecking(Cell currCell, Integer tempVal)	{
		
		// a list to keep track of connected cells, domain of which has been cleared
		ArrayList<Cell> involved = new ArrayList<Cell>();
		
		for (int i = 0; i < freeVars.size(); i++)	{
			if (checkConnected(currCell, freeVars.get(i)))	{
				if (freeVars.get(i).domain.contains(tempVal))	{
					freeVars.get(i).domain.remove(tempVal);
					involved.add(freeVars.get(i));
				}
			}
		}
		
		// as soon as the domain of any variable becomes empty, return to backtrack
		if (!checkDomains())	{
			for (int j = 0; j < involved.size(); j++)	{
				// put value back to the domain
				involved.get(j).domain.add(tempVal);
			}
			return false;	// not valid for the assigned value
		}
		
		
		return true;
		
	}
	
	/**
	 * Finds the least-constraining-value,
	 * which rules out the fewest choices for the connected variables
	 * @param list
	 * @return
	 */
	public Cell largestDomain(ArrayList<Cell> list)		{
		Cell temp = list.get(0);
		for (int i = 1; i < list.size(); i++)	{
			if (list.get(i).domain.size() > temp.domain.size())
				temp = list.get(i);
		}
		return temp;
	}


	/**
	 * Checks domains of free cells
	 * if at least one cell has an empty domain,
	 * which means it has no value to be assigned to,
	 * returns false
	 * @return
	 */
	public boolean checkDomains()	{
		for (int i = 0; i < freeVars.size(); i++)	{
			if (freeVars.get(i).domain.isEmpty())
				return false;
		}
		return true;
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


	/**
	 * Main method that takes arguments from command line
	 */
	public static void main(String[] args) throws IOException	{
		
		File file = new File(args[0]);
		SudokuSolver sudoku = new SudokuSolver(file);
        
	}  
	
}

