package cs4321.operator;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import net.sf.jsqlparser.expression.Expression;
import cs4321.project2.Evaluation;

/**
 * The select part of the query. Get the desired attributes.
 * @author jz699 JUNCHEN ZHAN
 *
 */
public class SelectOperator extends ProjectOperator{

	private ScanOperator scan;
	private Expression express;
	private Tuple tup;
	private Map<String,String> hash;
	
	SelectOperator() {}
	
	/**
	 * Constructor based on the ScanOperator.
	 * @param file the file needs to be parsed.
	 * @param express the expression tree for the where clause.
	 */
	public SelectOperator(File file, Expression express, Map<String,String> hash){
		scan = new ScanOperator(file);
		this.express = express;
		this.hash = hash;
	}
	
	/**
	 * Continue getting the next tuple until the tuple we get 
	 * could match the requirement of the where clause.
	 * @return the next tuple we want.
	 */
	@Override
	public Tuple nextTuple() {
		// TODO Auto-generated method stub
		Tuple candidate = null;
		while((candidate = scan.nextTuple())!=null){
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
	 * getter method for the tuple.
	 * @return the tuple.
	 */
	public Tuple getTuple() {
		return tup;
	}
	

}
