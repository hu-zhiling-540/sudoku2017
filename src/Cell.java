import java.util.ArrayList;

public class Cell {

	public int row;		// row coordinate
	public int col;		// col coordinate
	public Integer val;		// cell value
	public ArrayList<Integer> domain;


	/**
	 * Default Constructor
	 * @param row
	 * @param col
	 */
	public Cell(int row, int col)	{
		this.col = col;
		this.row = row;
		val = 0;
		initDomain();
	}


	/**
	 * Default Constructor that takes in a specific value
	 * @param row
	 * @param col
	 * @param val
	 */
	public Cell(int row, int col, Integer val)	{
		this.row = row;
		this.col = col;
		this.val = val;
		initDomain();
	}


	/**
	 * Initialize the domain, 1 to 9 included
	 */
	public void initDomain()	{
		this.domain = new ArrayList<Integer>();
		for (int i = 1; i <= 9; i++)
			this.domain.add(Integer.valueOf(i));
	}


	/**
	 * Setter for the value
	 * @param val
	 */
	public void setValue(int val)	{
		this.val = Integer.valueOf(val);
	}


	/**
	 * Setter for the value
	 * @param val
	 */
	public void setValue(Integer val)	{
		this.val = val;
	}


	/**
	 * Getter for the domain
	 * @return
	 */
	public ArrayList<Integer> getDomain()	{
		return this.domain;
	}


	/**
	 * Setter for the domain
	 * @param domain
	 */
	public void setDomain(ArrayList<Integer> domain)	{
		this.domain = domain;
	}
}
