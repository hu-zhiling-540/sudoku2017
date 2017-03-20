import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
	
	

	
	public void printGrid() {
		
		String output = " ";
		for (int i = 0; i < ROWS; i++)		{
        	for (int j = 0 ; j < COLS; j++)		{
				output += grid[i][j].val + " ";
			}
			output += "\n";
		}
		System.out.println(output);
	}

//		FileReader fr = new FileReader(input);
//		BufferedReader br  = new BufferedReader(fr);
//		Scanner s = new Scanner(br);
	
	
	public static void main(String[] args) throws IOException	{
		
		File file = new File(args[0]);
		SudokuSolver sudoku = new SudokuSolver(file);
        
	}  
	
}

