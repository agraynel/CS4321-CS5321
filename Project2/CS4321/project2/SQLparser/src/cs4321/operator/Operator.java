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
	public abstract Tuple nextTuple();
	
	/**
	 * an abstract method, reset the pointer to the beginning of the table.
	 */
	public abstract void reset();
	
	/**
	 * an abstract method, get all of the tuples at once.
	 * @param index the index of the output file.
	 */
	public abstract void dump(String s, int index);
}
