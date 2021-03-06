package cs4321.operator;


import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.sf.jsqlparser.expression.Expression;
import cs4321.project2.Evaluation;
import cs4321.support.Catalog;

/**
 * The select part of the query. Get the desired attributes.
 * use the evaluation tree to test whether this tuple is a valid one.
 * @author jz699 JUNCHEN ZHAN
 *
 */
public class SelectOperator extends Operator{

	private ScanOperator scan;
	private Expression express;
	private Tuple tup;
	private Map<String,String> hash;
	private Map<String,Integer> location; 
	
	/**
	 * Constructor based on the ScanOperator.
	 * @param file the file needs to be parsed.
	 * @param express the expression tree for the where clause.
	 */
	public SelectOperator(File file, String string, Expression express, Map<String,String> hash){
		scan = new ScanOperator(file);
		this.express = express;
		this.hash = hash;
		location = new HashMap<>();
		Map<String,Integer> dum = Catalog.getInstance().getSchema(hash.get(string));
		for(Map.Entry<String, Integer> entry: dum.entrySet()){
			String[] s1 = entry.getKey().split("\\.");
			location.put(string+"."+s1[1], entry.getValue());
		}
	}
	
	/**
	 * Continue getting the next tuple until the tuple we get 
	 * could match the requirement of the where clause.
	 * @return the next tuple we want.
	 */
	@Override
	public Tuple getNextTuple() {
		// TODO Auto-generated method stub
		Tuple candidate = null;
		while((candidate = scan.getNextTuple())!=null){
			if(express==null) break;
			Evaluation eva = new Evaluation(candidate,hash);
			express.accept(eva);
			if(eva.getResult()) break;
		}
		tup = candidate;
		return candidate;
	}
 
	/**
	 * reset the pointer back to the beginning of the file.
	 */
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		scan.reset();
	}
	
	/**
	 * getter method for the tuple.
	 * @return the tuple.
	 */
	public Tuple getTuple() {
		return tup;
	}
	
	/**
	 * the getter method of a map.
	 * @return the map indicates the location of a string.
	 */
	public Map<String,Integer> getLocation() {
		return location;
	}

}
