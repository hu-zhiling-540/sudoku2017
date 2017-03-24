import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;


public class SudokuSolver {
	
	protected final static int ROWS = 9;
	protected final static int COLS = 9;
	
	protected static Cell[][] grid;	// two dimensional grid: R, C
	
	File inputFile;
	
	// keep track
	static long startTime;
	static long endTime;
	int nodesVisited = 0;		// number of nodes visited ( the number of variables assigned)
	
	protected ArrayList<Cell> freeVars;

	
	/**
	 * constructor
	 * @param filename
	 * @throws IOException 
	 */
	public SudokuSolver(File file) throws IOException	{
		inputFile = file;
		grid = new Cell[ROWS][COLS];	// Instantiate the grid
		readFile(inputFile);
		printGrid();
		
		// naive backtracking
//		naiveBktrk();
		
		// backtracking with forward checking
		bktrkFwdCkg();
		
		printGrid();
	}
	

	/**
	 * Reads a file and creates a two-dimensional grid based on the input values
	 * @param file
	 * @throws IOException
	 * Ref.:https://docs.oracle.com/javase/tutorial/essential/io/scanning.html
	 */
	protected void readFile(File file) throws IOException	{
		
		Scanner s = null;
		try {
            s = new Scanner(new BufferedReader(new FileReader(file)));
            
            for (int i = 0; i < ROWS; i++)		{
            	for (int j = 0 ; j < COLS; j++)		{
            		if (s.hasNext()) {
                    	String str = s.next(); 
                    	grid[i][j] = new Cell(i, j, Integer.parseInt(str));
                    }
            	}
            }
        }
		
		finally {
			if (s != null) {
                s.close();
            }
        }	
	}
	

	protected void initList()		{
		
		freeVars = new ArrayList<Cell>();
		
		for (int i = 0; i < ROWS; i++)		{
        	for (int j = 0 ; j < COLS; j++)		{
        		if (grid[i][j].val.equals(0))	{
        			freeVars.add(grid[i][j]);
        		}
        	}
		}
		
		System.out.println("Number of cells to be filled is " + freeVars.size() + " .");
	}
	
	
	/**
	 * Selects a free variable and branches out on this node with value from its domain.
	 * If a constraint check fails, the next domain value will be tried.
	 */
	protected void naiveBktrk()		{
		// put all unassigned variables in a list
        initList();	
		if (!freeVars.isEmpty())	{
			Cell head = freeVars.remove(0);
			naiveBacktracking(head);
		}
	}
	

	/**
	 * Recursive call on free variables until a solution is found
	 * @param currCell
	 * @return
	 */
	protected boolean naiveBacktracking(Cell currCell)	{
		
		// if no 0 entry in the grid
		if (isSolved())
			return true;
		
		// loop through the domain
		for (int i = 0; i < currCell.domain.size(); i++)	{
			
			Integer val = currCell.domain.get(i);
			
			// check if no connected variables already assigned with the same value
			if (!hasConflict(currCell, val))	{
				
				grid[currCell.row][currCell.col].setValue(val);		// set temporary value
				
				// based on this assignment: check the next variable
				if (freeVars.isEmpty())
					return true;			// by the time all the assignments are finished
				
				// else: pop up a free variable from the list
				Cell next = freeVars.get(0);
				freeVars.remove(0);
				
				if (naiveBacktracking(next))
					return true;
				
				// else: naive is wrong after, so we go back
				grid[currCell.row][currCell.col].setValue(0);
				freeVars.add(0, next);
				}	
		}
		return false;	
	}

		
	/**
	 * Checks if any connected cell has the same value
	 * @param cell
	 * @param val
	 * @return
	 */
	protected boolean hasConflict(Cell cell, Integer val)	{
		
		// same col
		for (int i = 0; i < ROWS; i++)	{
			if (grid[i][cell.col].val.equals(val))	{
				return true;
			}
		}
		
		// same row
		for (int j = 0; j < COLS; j++)	{
			if (grid[cell.row][j].val.equals(val))
				return true;
		}
		
		// same block
		int block_r = (cell.row/3)*3;
		int block_c = (cell.col/3)*3;
		for (int i = 0; i < 3 ; i++)	{
			for (int j = 0; j < 3; j++)
				if ( grid[i+block_r][j+block_c].val.equals(val))
					return true;
		}
		
		return false;
	}


	/**
	 * Backtracking with forward checking
	 */
	protected void bktrkFwdCkg()	{
		// put all unassigned variables in a list
        initList();	
		if (!freeVars.isEmpty())	{
			Cell head = freeVars.get(0);
			backtracking(head);
		}
	}
	
	
	/**
	 * Runs backtracking recursively
	 * @param currCell
	 * @return
	 */
	protected boolean backtracking(Cell currCell)	{
		
		// if no 0 entry in the grid, it is solved
		if (isSolved())
			return true;
	
		freeVars.remove(0);
		
		for (int i = 0; i < currCell.domain.size(); i++)	{
			
			Integer val = currCell.domain.get(i);
			
			// if connected cells don't contain the same value
			if (!hasConflict(currCell, val))		{
				
				grid[currCell.row][currCell.col].setValue(val);
				
				// base case
				if (freeVars.isEmpty())
					return true;			// by the time all the assignments are finished
				
				if (forwardChecking(currCell, val))		{
					// else: pop up a free variable from the list
					Cell next = freeVars.get(0);
					
					if (backtracking(next))
						return true;
					else	{
						freeVars.add(0, next);
						// put value back to domains
						undo(currCell,val);
					}
				}
			}
			grid[currCell.row][currCell.col].setValue(0);
			// move on the next possible value in the domain
		}
		
		return false;
	}
	
	protected void undo(Cell cell, Integer val)	{
		for (int i = 0; i < freeVars.size(); i++)	{
			if( isConnected(cell, freeVars.get(i)))
				grid[freeVars.get(i).row][freeVars.get(i).col].domain.add(val);
		}

		
	}
	
	/**
	 * If a variable is assigned with certain value,
	 * deletes value from the domains of the connected variables.
	 * @param currCell
	 * @param tempVal
	 * @return
	 */
	protected boolean forwardChecking(Cell currCell, Integer tempVal)	{
		
		// a list to keep track of connected cells, domain of which has been cleared
		ArrayList<Cell> involved = new ArrayList<Cell>();
		
		for (int i = 0; i < freeVars.size(); i++)	{
			if (isConnected(currCell, freeVars.get(i)))		{
				if (freeVars.get(i).domain.contains(tempVal))	{
					freeVars.get(i).domain.remove(tempVal);
					involved.add(freeVars.get(i));
				}
			}
		}
		
		// as soon as the domain of any variable becomes empty, return to backtrack
		if (emptyDomain(involved))	{
			for (int i = 0; i < involved.size(); i++)	{
				// put value back to the domain
				int index = findCellIndex(freeVars, involved.get(i));
				if ( index != -1)
					freeVars.get(index).domain.add(tempVal);
			}
			return false;		// not valid for the assigned value
		}
		
		return true;
	}
	
	protected int findCellIndex(ArrayList<Cell> list, Cell cell)	{
		for (int i = 0; i < list.size(); i++ )	{
			if (list.get(i).row == cell.row && list.get(i).col == cell.col)
				return i;
		}
		return -1;
	}
	/**
	 * Checks two cells passed in if they are connected
	 * @param main
	 * @param cell
	 * @return
	 */
	protected boolean isConnected(Cell main, Cell cell)	{
		
		// main cell
		int r1 = main.row;
		int c1 = main.col;
		
		// cell to be checked
		int r2 = cell.row;
		int c2 = cell.col;			
		
		// same row or same col
		if (c1 == c2|| r1 == r2)
			return true;
		
		// same block
		for (int i = (r1/3)*3; i < (r1/3)*3 + 3; i++)	{
			for (int j = (c1/3)*3; j < (c1/3)*3 + 3; j++)
				if ( i == r2 && j == c2 && !( r1 == r2 && c1 == c2 ))		// don't count self in
					return true;
		}
		
		return false;
		
	}


	/**
	 * If all cells are filled in with a value other than 0
	 * @return
	 */
	protected boolean isSolved() {
		for (int i = 0; i < ROWS; i++)		{
	    	for (int j = 0 ; j < COLS; j++)		{
	    		if (grid[i][j].val.equals(0))
	    			return false;
	    	}
		}
		return true;
	}

		
	/**
	 * Checks domains of free cells
	 * if at least one cell has an empty domain,
	 * which means it has no value to be assigned to,
	 * returns false
	 * @return
	 */
	protected boolean emptyDomain(ArrayList<Cell> list)	{
		for (int i = 0; i < list.size(); i++)	{
			if (list.get(i).domain.isEmpty())
				return true;
		}
		return false;
	}


	protected Cell smallestDomain(ArrayList<Cell> list)		{
		Cell temp = list.get(0);
		for (int i = 1; i < list.size(); i++)	{
			if (list.get(i).domain.size() < temp.domain.size())
				temp = list.get(i);
		}
		return temp;
	}


	/**
	 * Finds the least-constraining-value,
	 * which rules out the fewest choices for the connected variables
	 * @param list
	 * @return
	 */
	protected Cell mostVariables(ArrayList<Cell> list)		{
		return null;

	}


	/**
	 * Prints the grid out to the console
	 */
	protected void printGrid() {
		String output = "";
		for (int i = 0; i < ROWS; i++)		{
	    	for (int j = 0 ; j < COLS; j++)		{
	    		if ( j%3 == 0 && j != 0)
	    			output += "| ";
	    		output +=grid[i][j].val + " ";
			}
	    	
	    	if ( i == 2 || i == 5)	{
	    		output += "\n";
	    		output += "- - -   - - -   - - - ";
	    	}
	    	
	    	output += "\n";
		}
		System.out.println(output);
	}

	

	/**
	 * Main method that takes arguments from command line
	 */
	public static void main(String[] args) throws IOException	{
		
//		File file = new File(args[0]);
		File file = new File("TestCase1.txt");
		
		SudokuSolver sudoku = new SudokuSolver(file);
		if (args.length == 1)	{
			System.out.println("Naive Backtracking");
			SudokuSolver puzzle = new SudokuSolver(file);
			
			puzzle.naiveBktrk();
			puzzle.printGrid();
		}
			
		else if (args.length == 2){
			if(args[1].equals("FC")){
				System.out.println("Backtracking with Forward Checking");
				SudokuSolver puzzle = new SudokuSolver(file);
				
				puzzle.bktrkFwdCkg();
				puzzle.printGrid();
			}
			else
				System.out.println("Argument is not legal.");
		}
		
		else if (args.length == 3)
			if(args[1].equals("FC")&& args[2].equals("MRV")
					|| args[2].equals("FC")&& args[1].equals("MRV")){
				System.out.println("Backtracking with Forward Checking");
				SudokuSolver puzzle = new SudokuSolver(file);
				
//				puzzle.bktrkFwdCkg();
				puzzle.printGrid();
			}
			else
				System.out.println("Argument is not legal.");
		
	}  
	
}

