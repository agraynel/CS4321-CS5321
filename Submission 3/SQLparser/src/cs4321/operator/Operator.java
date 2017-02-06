package cs4321.operator;


/** this is the class which is the superclass of all kinds of operators.
 * 
 * @author jz699 JUNCHEN ZHAN
 *
 */
public abstract class Operator {
    
	
	/**
	 * an abstract method, return the next tuple.
	 */
	public abstract Tuple getNextTuple();
	
	/**
	 * an abstract method, reset the pointer to the beginning of the table.
	 */
	public abstract void reset();
	
	/**
	 * an empty method which will be overridden by operators that will need 
	 * to perform dump().
	 * @param s the string indicates the location of files.
	 * @param index the index of the output file.
	 */
	public void dump(String s, int index){
		
	}
}
