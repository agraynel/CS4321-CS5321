package cs4321.operator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs4321.project2.EvaluationMkll;
import cs4321.support.Catalog;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectItem;

/**
 * this class handles the join operator of the SQL language.
 * We reconstruct the join tree to optimize the speed of join operator.
 * @author jz699 JUNCHEN ZHAN
 *
 */
public class JoinOperator extends Operator {

	protected Map<String,SelectOperator> map2; // maps the table with their selection counterpart.
	protected Map<String,Tuple> map3; // map the table with their respective tuples.
	protected Map<String,String> hash; // map the aliases with their respective tables.
	protected Expression express; 
	protected String[] array;
    protected PlainSelect ps;
    protected List<SelectItem> list;
    protected Map<String, Integer> location; // maps the string with the column number.
	
    /**
     * constructor: build a join constructor, in this constructor, call methods
     * to rebuild the expression tree where all the selections come first and 
     * all joins come next.  
     * @param array an array of table names.
     * @param ps the plain select of a query.
     * @param hash the map between aliases and original table name.
     */
	@SuppressWarnings("unchecked")
	public JoinOperator(String[] array, PlainSelect ps, Map<String,String> hash,
			Map<String, SelectOperator> map2, Map<String, Tuple> map3, Expression express) {
		this.ps = ps;
		this.hash = hash;
		this.array = array;
		this.map2 = map2;
		this.map3 = map3;
		this.express = express;
		location = new HashMap<>();
		list = ps.getSelectItems();
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
	
	/**
	 * get the next tuple of the operator.
	 * @return the next tuple.
	 */
	@Override
	public Tuple getNextTuple() {
		// TODO Auto-generated method stub
		Tuple tuple = null;
		EvaluationMkll eva2 = new EvaluationMkll(map2,array,map3,hash);
		express.accept(eva2);
		// check whether there is a tuple.
		List<Integer> arraylist = new ArrayList<>();
		for(int j=0;j<array.length;j++){
			Tuple temp = map2.get(array[j]).getTuple();
			if(temp==null) return null;
			for(int i=0;i<temp.length();i++){
				arraylist.add(temp.getData(i));
			}
		}
		tuple = new Tuple(arraylist.size());
		for(int i=0;i<arraylist.size();i++){
		    tuple.setData(i, arraylist.get(i));
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
	
}
