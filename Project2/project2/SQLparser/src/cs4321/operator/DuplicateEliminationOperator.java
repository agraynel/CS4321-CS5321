package cs4321.operator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * this is the class which handles the distinct operator
 * when the tuples are sorted.
 * @author jz699 JUNCHEN ZHAN
 *
 */
public class DuplicateEliminationOperator extends Operator{

	private List<Tuple> list; // the list stores the tuples.
	private int index = 0; // the index of the list.
	
	/**
	 * constructor: construct the distinct operator 
	 * which the tuples are sorted.
	 * @param sop the operator which tuples are sorted.
	 */
	public DuplicateEliminationOperator(SortOperator sop) {
		list = sop.getList();
	}

    /**
     * method that gets the next tuple.
     * @return the next tuple.
     */
	@Override
	public Tuple nextTuple() {
		// TODO Auto-generated method stub
		Tuple tuple = null;
		while(index<list.size()){
			if(index==0||!list.get(index).equals(list.get(index-1))){
				tuple = list.get(index);
				index++;
				break;
			}
			index++;
		}
		return tuple;
	}

	/**
	 * method that reset the operator.
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
