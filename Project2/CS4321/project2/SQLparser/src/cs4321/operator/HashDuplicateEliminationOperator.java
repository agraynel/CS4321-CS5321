package cs4321.operator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * this class handles the distinct case when the tuples are not sorted.
 * @author jz699 JUNCHEN ZHAN
 *
 */
public class HashDuplicateEliminationOperator extends Operator{

	private List<Tuple> list; // list that handles the tuples.
	private Set<String> set; // set that handles the converted tuples.
	private int index; // index that indicates the index of the list.
	
	/**
	 * constructor: get all the tuples from the operator.
	 * @param op the operator to be handled.
	 */
	public HashDuplicateEliminationOperator(Operator op) {
		list = new ArrayList<>();
		set = new HashSet<>();
		Tuple tuple = null;
		while((tuple=op.nextTuple())!=null){
		    String s = tuple.toString();
		    if(set.add(s))
			    list.add(tuple);
	    }
		op.reset();
	}
	
	/**
	 * get the next tuple of the operator.
	 * @return the next tuple.
	 */
	@Override
	public Tuple nextTuple() {
		// TODO Auto-generated method stub
		if(index==list.size()) return null;
		else{
			Tuple tuple = list.get(index);
			index++;
			return tuple;
		}
	}

	/**
	 * rest the operator.
	 */
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		index = 0;
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

}
