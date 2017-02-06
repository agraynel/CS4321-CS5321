package logicalqueryplan;

import cs4321.operator.PhysicalPlanBuilder;

/**
 * This is an abstract class operator and the super class of 
 * all concrete logical operators.
 * @author jz699 JUNCHEN ZHAN
 *
 */
public abstract class Operators {

	private Operators child; // the child of this operator.
	
	/**
	 * Constructor: to let JoinOperators have a super constructor to use.
	 */
	public Operators(){}
	
	/**
	 * Constructor: pass the parameter of operator to the field.
	 * @param child
	 */
	public Operators(Operators child) {
		this.child = child;
	}
	
	/**
	 * Abstract method for accepting visitor.
	 * @param visitor visitor to be accepted.
	 */
	public abstract void accept(PhysicalPlanBuilder visitor);
	
	/**
	 * return the child operator.
	 * @return the operator of which is the child of the caller method.
	 */
	public Operators getChild(){
		return child;
	}
	
}
