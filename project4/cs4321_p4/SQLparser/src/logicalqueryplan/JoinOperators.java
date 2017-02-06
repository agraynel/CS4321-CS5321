package logicalqueryplan;

import cs4321.operator.PhysicalPlanBuilder;

/**
 * this is the super class of all join operators.
 * @author jz699 JUNCHEN ZHAN
 *
 */
public class JoinOperators extends Operators {
	
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
