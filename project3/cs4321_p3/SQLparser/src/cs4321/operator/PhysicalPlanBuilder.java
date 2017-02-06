package cs4321.operator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import logicalqueryplan.DuplicateEliminationOperators;
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
	private int index1; // index to decide which method of joins shall be used.
	private int index2; // index to decide the number of pages for joins shall be used.
	private int index3; // index to decide which method of sort shall be used.
	private int index4; // index to decide the number of pages for sort shall be used.
	
	/**
	 * Constructor: get all those parameters into fields for the sake of
	 * creating physical operators.
	 * @param array the array of tables.
	 * @param ps the plain select language.
	 * @param hash the connection between aliases.
	 * @param index the index indicates which join method should be used.
	 */
	public PhysicalPlanBuilder(String[] array, PlainSelect ps, Map<String,String> hash,
			int index1, int index2, int index3, int index4){
	    this.array = array;
	    this.ps = ps;
	    this.hash = hash;
	    this.index1 = index1;
	    this.index2 = index2;
	    this.index3 = index3;
	    this.index4 = index4;
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
	 * visitor method for join operator.
	 * do a post traversal of the tree and create a physical operator.
	 * @param operator the operator needs to be visited.
	 */
	public void visit(JoinOperators operator){
		if(index1==0||ps.getJoins()==null) op = new JoinOperator(array,ps,hash);
		else if(index1==1) op = new BlockJoinOperator(array,ps,hash,index2);
		else op = new SortMergeJoinOperator(array,ps,hash,index4);
	}
	
	/**
	 * visitor method for duplicate elimination operator.
	 * do a post traversal of the tree and create a physical operator.
	 * @param operator the operator needs to be visited.
	 */
	public void visit(DuplicateEliminationOperators operator) {
		operator.getChild().accept(this);
		op = new DuplicateEliminationOperator(op);
	}
	
	/**
	 * visitor method for sort operator.
	 * do a post traversal of the tree and create a physical operator.
	 * @param operator the operator needs to be visited.
	 */
	public void visit(SortOperators operator) {
		operator.getChild().accept(this);
		ProjectOperator po = (ProjectOperator)op;
		List<String> list = new ArrayList<>();
		String[] array = null;
		if(ps.getOrderByElements()!=null){
			for(int i=0;i<ps.getOrderByElements().size();i++)
				list.add(ps.getOrderByElements().get(i).toString());
			array = new String[list.size()];
			for(int i=0;i<array.length;i++)
				array[i] = list.get(i);
		}else array = new String[0];
		if(index3==1) op = new EXSortOperator(po,array,po.getMap(),index4,1);
		else op = new SortOperator(po,array,po.getMap());
	}
	
	/**
	 * spit all the tuples at once, put the results in a file.
	 * @param s the file location in a string form.
	 * @param index the index to identify the query.
	 */
	public void dump(String s, int index){
		op.dump(s, index);
	}
	
}
