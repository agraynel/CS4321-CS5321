package cs4321.operator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cs4321.project2.EvaluationMkll;
import cs4321.support.Catalog;
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
import net.sf.jsqlparser.statement.select.SelectItem;

/**
 * this class handles the join operator of the SQL language.
 * @author jz699 JUNCHEN ZHAN
 *
 */
public class JoinOperator extends Operator {

	private Map<String,Expression> map; // maps the table with their expression trees.
	private Map<String,SelectOperator> map2; // maps the table with their selection counterpart.
	private Map<String,Tuple> map3; // map the table with their respective tuples.
	private Map<String,String> hash; // map the aliases with their respective tables.
	private Map<String,Integer> location;
	// the location of a column to the tuple index. Used in the sort operator.
	private List<String> list1; // stores the left part of a compare expression
	private List<String> list2; // stores the right part of a compare expression
	private List<String> list3; // stores the comparator of a compare expression
	private Expression express; 
	private String[] array;
	private boolean isAsterik = true;
    private List<SelectItem> list;
	
    /**
     * constructor: build a join constructor, in this constructor, call methods
     * to rebuild the expression tree where all the selections come first and 
     * all joins come next.  
     * @param array an array of table names.
     * @param ps the plain select of a query.
     * @param hash the map between aliases and original table name.
     */
	@SuppressWarnings("unchecked")
	public JoinOperator(String[] array, PlainSelect ps, Map<String,String> hash) {
		map = new HashMap<>();
		map2 = new HashMap<>();
		map3 = new HashMap<>();
		this.hash = hash;
		location = new HashMap<>();
		list1 = new ArrayList<>();
		list2 = new ArrayList<>();
		list3 = new ArrayList<>();
		this.array = array;
		Expression exp = ps.getWhere();
		// get all the selections first.
		if(exp!=null){
			String str = exp.toString();
		    String[] temp = str.split("AND");
		    for(int i=0;i<temp.length;i++){
			    setExpression(temp[i]);
		    }
		}
		for(String strin : array){
			String string = hash.get(strin);
			// is there is a selection exists in this table.
			if(map.containsKey(strin)){
			    map2.put(strin, new SelectOperator
					(new File(Catalog.getInstance().getFileLocation(string)),map.get(strin),hash));
			}
			else{
				File file = new File(Catalog.getInstance().getFileLocation(string));
				SelectOperator seo = new SelectOperator(file,null,hash);
				map2.put(strin, seo);
			}
		}
		Set<String> set = new HashSet<>();
		boolean[] marked = new boolean[list1.size()];
		// now handles the join expressions.
		express = setJoinTree(set,array,marked);
		list = ps.getSelectItems();
		// now handles the projection of the query.
		if(!list.get(0).toString().equals("*")){
			isAsterik = false;
			for(int i=0;i<list.size();i++)
				location.put(list.get(i).toString(), i);
		}
		else{
			int index = 0;
			for(int i=0;i<array.length;i++){
				Map<String,Integer> dum = Catalog.getInstance().getSchema(hash.get(array[i]));
				for(Map.Entry<String, Integer> entry: dum.entrySet()){
					String[] s1 = entry.getKey().split("\\.");
					location.put(array[i]+"."+s1[1], index+entry.getValue());
				}
				index += dum.size();
			}
		}
	}
	
	/**
	 * get the next tuple of the operator.
	 * @return the next tuple.
	 */
	@Override
	public Tuple nextTuple() {
		// TODO Auto-generated method stub
		Tuple tuple = null;
		EvaluationMkll eva2 = new EvaluationMkll(map2,array,map3,hash);
		express.accept(eva2);
		// check whether there is a tuple.
		if(eva2.hasTuple()){
			if(isAsterik){
			    List<Long> arraylist = new ArrayList<>();
			    for(int j=0;j<array.length;j++){
				    Tuple temp = map2.get(array[j]).getTuple();
				    for(int i=0;i<temp.length();i++){
					    arraylist.add(temp.getData(i));
				    }
			    }
			    tuple = new Tuple(arraylist.size());
		        for(int i=0;i<arraylist.size();i++)
		    	    tuple.setData(i, arraylist.get(i));
			}else{
		    	long[] data = new long[list.size()];
		    	for(int i=0;i<data.length;i++){
		    		String[] buff = list.get(i).toString().split("\\.");
		    		String last = hash.get(buff[0]) + "." + buff[1];
		    		// recreate the string to be used in the catalog.
		    		Tuple tu = map2.get(buff[0]).getTuple();
		    		data[i] = tu.getData(Catalog.getInstance().getColumn(last));
		    	}
		    	tuple = new Tuple(data);
		    }
		}
		return tuple;
	}

	/**
	 * reset the operator
	 */
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		for(int i=0;i<array.length;i++)
			map2.get(array[i]).reset();
		map3 = new HashMap<>();
	}

	/**
	 * for debugging, get all the tuples at once and put them in a file.
	 * @param index the index of the output file.
	 */
	@Override
	public void dump(String s, int index) {
		// TODO Auto-generated method stub
		Tuple tuple = null;
		BufferedWriter output = null;
		try{
		    File file = new File(s + index);
		    StringBuilder sb = new StringBuilder();
		    output = new BufferedWriter(new FileWriter(file));
			while((tuple = nextTuple())!=null){
	            sb.append(tuple.toString());
	            sb.append("\n");
	            System.out.println(tuple);
			}
			output.write(sb.toString());
			output.close();
	    }catch(IOException e){
			System.out.println("An exception occurs!");
		}
		reset();
	}
	
	/**
	 * get the location map which will be used by sort operator.
	 * @return the location map.
	 */
	public Map<String, Integer> getLocation() {
		return location;
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
	    String s = "";
	    // check whether this expression is a selection.
	    if(get1[0].equals(get2[0])||isNumber(get2[0])||isNumber(get1[0])){
			Expression left = null, right = null, result = null;
	    	if(isNumber(get2[0])){
	    		long num1 = Long.parseLong(get2[0]);
	    		right = new LongValue(num1);
	    	}
	        if(isNumber(get1[0])){
	        	long num2 = Long.parseLong(get1[0]);
	        	left = new LongValue(num2);
	        }
	        if(!isNumber(get2[0])){
	        	s = get2[0];
	        	right = new Column(new Table(null,get2[0]),get2[1]);
	        }
	        if(!isNumber(get1[0])){
	        	s = get1[0];
	        	left = new Column(new Table(null,get1[0]),get1[1]);
	        }
	        if(dummy[1].equals("=")) result = new EqualsTo(left,right);
	        else if(dummy[1].equals(">=")) result = new GreaterThanEquals(left,right);
	        else if(dummy[1].equals(">")) result = new GreaterThan(left,right);
	        else if(dummy[1].equals("<=")) result = new MinorThanEquals(left,right);
	        else if(dummy[1].equals("<")) result = new MinorThan(left,right);
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
