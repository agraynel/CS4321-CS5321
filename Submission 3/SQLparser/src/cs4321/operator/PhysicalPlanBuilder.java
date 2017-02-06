package cs4321.operator;

import java.util.Map;

import logicalqueryplan.DuplicateEliminationOperators;
import logicalqueryplan.HashDuplicateEliminationOperators;
import logicalqueryplan.JoinOperators;
import logicalqueryplan.ProjectOperators;
import logicalqueryplan.SortOperators;
import net.sf.jsqlparser.statement.select.PlainSelect;

/**
 * this is the physical plan builder. build a tree to 
 * get the result by calling the tree recursively.
 * @author jz699 JUNCHEN ZHAN
 *
 */
public class PhysicalPlanBuilder {
	
	private String[] array; // the array of tables.
	private PlainSelect ps; // the selection query of the physical plan.
	private Map<String,String> hash; // the conversion between aliases.
	private Operator op; // the operator that will be used.
	private int index; // index to decide which method of joins shall be used.
	
	/**
	 * Constructor: get all those parameters into fields for the sake of
	 * creating physical operators.
	 * @param array the array of tables.
	 * @param ps the plain select language.
	 * @param hash the connection between aliases.
	 * @param index the index indicates which join method should be used.
	 */
	public PhysicalPlanBuilder(String[] array, PlainSelect ps, Map<String,String> hash, int index){
	    this.array = array;
	    this.ps = ps;
	    this.hash = hash;
	    this.index = index;
	}

	/**
	 * visitor method for project operator.
	 * do a post traversal of the tree and create a physical operator.
	 * @param operator the operator needs to be visited.
	 */
	public void visit(ProjectOperators operator){
		operator.getChild().accept(this);
		op = new ProjectOperator((JoinOperator)op);
	}
	
	/**
	 * visitor method for project operator.
	 * do a post traversal of the tree and create a physical operator.
	 * @param operator the operator needs to be visited.
	 */
	public void visit(JoinOperators operator){
		op = new JoinOperator(array,ps,hash);
	}
	
	/**
	 * visitor method for project operator.
	 * do a post traversal of the tree and create a physical operator.
	 * @param operator the operator needs to be visited.
	 */
	public void visit(HashDuplicateEliminationOperators operator){
		operator.getChild().accept(this);
		op = new HashDuplicateEliminationOperator((ProjectOperator)op);
	}
	
	/**
	 * visitor method for project operator.
	 * do a post traversal of the tree and create a physical operator.
	 * @param operator the operator needs to be visited.
	 */
	public void visit(DuplicateEliminationOperators operator) {
		operator.getChild().accept(this);
		op = new DuplicateEliminationOperator((SortOperator)op);
	}
	
	/**
	 * visitor method for project operator.
	 * do a post traversal of the tree and create a physical operator.
	 * @param operator the operator needs to be visited.
	 */
	public void visit(SortOperators operator) {
		operator.getChild().accept(this);
		ProjectOperator po = (ProjectOperator)op;
		op = new SortOperator(po,ps,po.getMap());
	}
	
	/**
	 * visitor method for project operator.
	 * do a post traversal of the tree and create a physical operator.
	 * @param operator the operator needs to be visited.
	 */
	public void dump(String s, int index){
		op.dump(s, index);
	}
	
}
