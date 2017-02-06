package logicalqueryplan;

import cs4321.operator.PhysicalPlanBuilder;

/**
 * this class deals with the order by language.
 * sort the tuples in the order presented by the Order By language.
 * @author jz699 JUNCHEN ZHAN
 *
 */
public class SortOperators extends Operators {
	
	/**
	 * Constructor: pass the parameter of operator to the field.
	 * @param child
	 */
	public SortOperators(Operators child) {
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
