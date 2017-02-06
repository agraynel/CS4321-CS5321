package logicalqueryplan;

import cs4321.operator.PhysicalPlanBuilder;

/**
 * this class handles the operator that will exclude duplicates out
 * by using hash tables since the tuples are not sorted.
 * @author jz699 JUNCHEN ZHAN
 *
 */
public class HashDuplicateEliminationOperators extends Operators{
	
	/**
	 * Constructor: pass the parameter of operator to the field.
	 * @param child
	 */
	public HashDuplicateEliminationOperators(Operators child) {
		super(child);
	}
	
	/**
	 * method for accepting visitor. just calls back visitor, logic of traversal
	 * will be handled in visitor method
	 * @param visitor visitor to be accepted.
	 */
	@Override
	public void accept(PhysicalPlanBuilder visitor) {
		// TODO Auto-generated method stub
		visitor.visit(this);
	}

}
