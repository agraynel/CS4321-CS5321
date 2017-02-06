package cs4321.operator;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cs4321.support.Catalog;
import logicalqueryplan.DuplicateEliminationOperators;
import logicalqueryplan.JoinOperators;
import logicalqueryplan.ProjectOperators;
import logicalqueryplan.SortOperators;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
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
	protected Map<String,Expression> map; // maps the table with their expression trees.
	protected Map<String,SelectOperator> map2; // maps the table with their selection counterpart.
	protected Map<String,Tuple> map3; // map the table with their respective tuples.
	protected List<String> list1; // stores the left part of a compare expression
	protected List<String> list2; // stores the right part of a compare expression
	protected List<String> list3; // stores the comparator of a compare expression
	protected Expression express; 
    protected Map<String, Integer> location; // maps the string with the column number.
    protected double[][] markers; // this array stores the marker of the two binary trees.
    private Map<String, Integer> map4; // this map connects tables with their indexes.
    private int temp; // this integer tells whether to build the index or not.
    private Catalog catalog = Catalog.getInstance();
	
	/**
	 * Constructor: get all those parameters into fields for the sake of
	 * creating physical operators.
	 * @param array the array of tables.
	 * @param ps the plain select language.
	 * @param hash the connection between aliases.
	 * @param index the index indicates which join method should be used.
	 */
	public PhysicalPlanBuilder(String[] array, PlainSelect ps, Map<String,String> hash,
			int index1, int index2, int index3, int index4, int temp){
	    this.array = array;
	    map4 = new HashMap<>();
	    for(int i=0;i<array.length;i++)
	    	map4.put(array[i], i);
	    this.ps = ps;
	    this.hash = hash;
	    this.index1 = index1;
	    this.index2 = index2;
	    this.index3 = index3;
	    this.index4 = index4;
	    this.temp = temp;
	    markers = new double[array.length][2];
	    for(int i=0;i<markers.length;i++){
	    	markers[i][0] = Double.NEGATIVE_INFINITY;
	    	markers[i][1] = Double.POSITIVE_INFINITY;
	    }
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
		map = new HashMap<>();
		map2 = new HashMap<>();
		map3 = new HashMap<>();
		list1 = new ArrayList<>();
		list2 = new ArrayList<>();
		list3 = new ArrayList<>();
		Expression exp = ps.getWhere();
		// get all the selections first.
		if(exp!=null){
			String str = exp.toString();
		    String[] temp = str.split("AND");
		    for(int i=0;i<temp.length;i++){
			    setExpression(temp[i]);
		    }
		}
		for(int i=0;i<array.length;i++){
			String string = hash.get(array[i]);
			// is there is a selection exists in this table.
			if(map.containsKey(array[i])){
			    map2.put(array[i], new SelectOperator
					(new File(Catalog.getInstance().getFileLocation(string)),
							array[i],map.get(array[i]),hash,markers[i],temp));
			}
			else{
				File file = new File(Catalog.getInstance().getFileLocation(string));
				SelectOperator seo = new SelectOperator(file,array[i],null,hash,markers[i],temp);
				map2.put(array[i], seo);
			}
		}
		Set<String> set = new HashSet<>();
		boolean[] marked = new boolean[list1.size()];
		// now handles the join expressions.
		express = setJoinTree(set,array,marked);
		if(index1==0||ps.getJoins()==null) op = new JoinOperator(array,ps,hash,map2,map3,express);
		else if(index1==1) op = new BlockJoinOperator(array,ps,hash,index2,map2,map3,express);
		else op = new SortMergeJoinOperator(array,ps,hash,index4,map2,map3,express);
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
	public void dump(String s, String index){
		op.dump(s, index);
	}
	
	/**
	 * test whether the string is a long integer.
	 * @param s the tested string.
	 * @return whether s is a long integer or not.
	 */
	private boolean isNumber(String s) {
		try{
			Long.parseLong(s);
		}catch(Exception e){
			return false;
		}
		return true;
	}
	
	/**
	 * set the selection tree for their respective tuples.
	 * @param temp the selection language to be handled.
	 */
	private void setExpression(String temp) {
		String[] dummy = temp.trim().split("\\s+");
	    String[] get1 = dummy[0].split("\\.");
	    String[] get2 = dummy[2].split("\\.");
	    String s = "", str = "", col = "";
	    int states = 0, num = 0;
	    // check whether this expression is a selection.
	    if(get1[0].equals(get2[0])||isNumber(get2[0])||isNumber(get1[0])){
			Expression left = null, right = null, result = null;
			if(isNumber(get1[0])){
	        	get1 = dummy[2].split("\\.");
	        	get2 = dummy[0].split("\\.");
	        	if(dummy[1].equals("<=")) dummy[1] = ">=";
	        	else if(dummy[1].equals("<")) dummy[1] = ">";
	        	else if(dummy[1].equals(">")) dummy[1] = "<";
	        	else if(dummy[1].equals(">=")) dummy[1] = "<=";
	        }
	    	if(isNumber(get2[0])){
	    		long num1 = Long.parseLong(get2[0]);
	    		num = Integer.parseInt(get2[0]);
	    		right = new LongValue(num1);
	    		states++;
	    	}
	        if(!isNumber(get2[0])){
	        	s = get2[0];
	        	col = get2[1];
	        	right = new Column(new Table(null,get2[0]),get2[1]);
	        }
	        if(!isNumber(get1[0])){
	        	s = get1[0];
	        	col = get1[1];
	        	left = new Column(new Table(null,get1[0]),get1[1]);
	        }
	        str = hash.get(s) + "." + col;
	        if(dummy[1].equals("=")){
	        	result = new EqualsTo(left,right);
	        	if(states==1&&catalog.hasColumn(str)){
	        		int index = map4.get(s);
	        	    markers[index][0] = Math.max(markers[index][0],num - 1);
	        	    markers[index][1] = Math.min(markers[index][1],num + 1);
	        	    return;
	        	}
	        }
	        else if(dummy[1].equals(">=")){
	        	result = new GreaterThanEquals(left,right);
	        	if(states==1&&catalog.hasColumn(str)){
	        		int index = map4.get(s);
	        	    markers[index][0] = Math.max(markers[index][0],num - 1);
	        	    return;
	        	}	        	
	        }
	        else if(dummy[1].equals(">")){
	        	result = new GreaterThan(left,right);
	        	if(states==1&&catalog.hasColumn(str)){
	        		int index = map4.get(s);
	        	    markers[index][0] = Math.max(markers[index][0],num);
	        	    return;
	        	}
	        }
	        else if(dummy[1].equals("<=")){
	        	result = new MinorThanEquals(left,right);
	        	if(states==1&&catalog.hasColumn(str)){
	        		int index = map4.get(s);
	        	    markers[index][1] = Math.min(markers[index][1],num + 1);
	        	    return;
	        	}
	        }
	        else if(dummy[1].equals("<")){
	        	result = new MinorThan(left,right);
	        	if(states==1&&catalog.hasColumn(str)){
	        		int index = map4.get(s);
	        	    markers[index][1] = Math.min(markers[index][1],num);
	        	    return;
	        	}
	        }
	        else if(dummy[1].equals("<>")) result = new NotEqualsTo(left,right);
	        if(!map.containsKey(s)) map.put(s, result);
	        else{
	        	// set the old root to the left part, the new result to the right part.
	        	// and create a new root to connect them.
	        	// in order words, create a left inclined tree for selections.
	        	Expression exp = new AndExpression(map.get(s),result);
	        	map.put(s, exp);
	        }
	    }
	    // if not, put them in respective lists.
	    else{
            list1.add(dummy[0]);
            list2.add(dummy[2]);
            list3.add(dummy[1]);
	    }
	}
	
	/**
	 * construct a left inclined tree for all the joins.
	 * @param set check whether the tables are included
	 * @param array the list of tables in the from clause
	 * @param marked check whether the element in the list is already in the tree.
	 * @return the left inclined join tree.
	 */
	private Expression setJoinTree(Set<String> set, String[] array, boolean[] marked) {
		Expression left = null, right = null, answer = new Parenthesis(null);
		set.add(array[0]);
		for(int i=1;i<array.length;i++){
			set.add(array[i]);
			Expression result = null;
			for(int j=0;j<list1.size();j++){
				if(!marked[j]){
				    String[] dummy1 = list1.get(j).split("\\.");
				    String[] dummy2 = list2.get(j).split("\\.");
				    // check whether the tables are valid to be used.
				    if(set.contains(dummy1[0])&&set.contains(dummy2[0])){
					    left = new Column(new Table(null,dummy1[0]), dummy1[1]);
					    right = new Column(new Table(null,dummy2[0]), dummy2[1]);
					    Expression temp1 = result, temp2 = null;
					    if(list3.get(j).equals("=")) temp2 = new EqualsTo(left,right);
			            else if(list3.get(j).equals(">=")) temp2 = new GreaterThanEquals(left,right);
			            else if(list3.get(j).equals(">")) temp2 = new GreaterThan(left,right);
			            else if(list3.get(j).equals("<=")) temp2 = new MinorThanEquals(left,right);
			            else if(list3.get(j).equals("<")) temp2 = new MinorThan(left,right);
			            else if(list3.get(j).equals("<>")) temp2 = new NotEqualsTo(left,right);
					    // construct a left inclined tree for the expressions.
					    if(result==null) result = temp2;
					    else result = new AndExpression(temp1,temp2);
					    marked[j] = true;
				    }
				}
			}
			// if the join is simply a cross product, create an artificial node.
			if(result==null) result = new LongValue(0);
			// create a left inclined join tree.
			Expression temp = new AndExpression(answer,result);
			// use a parenthesis node as check point.
			answer = new Parenthesis(temp);
		}
		return answer;
	}
	
}
