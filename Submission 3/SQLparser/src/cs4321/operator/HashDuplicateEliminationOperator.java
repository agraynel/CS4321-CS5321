package cs4321.operator;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * this class handles the distinct case when the tuples are not sorted.
 * use a hash set to know whether the tuple is a duplicate or not.
 * @author jz699 JUNCHEN ZHAN
 *
 */
public class HashDuplicateEliminationOperator extends Operator implements TupleWriter{

	private List<Tuple> list; // list that handles the tuples.
	private Set<String> set; // set that handles the converted tuples.
	private int index; // index that indicates the index of the list.
	
	/**
	 * constructor: get all the tuples from the operator.
	 * @param op the operator to be handled.
	 */
	public HashDuplicateEliminationOperator(ProjectOperator op) {
		list = new ArrayList<>();
		set = new HashSet<>();
		Tuple tuple = null;
		while((tuple=op.getNextTuple())!=null){
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
	public Tuple getNextTuple() {
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
	 * @param s the location of the output files.
	 * @param index the index of the output file.
	 */
	@Override
	public void dump(String s, int index) {
		// TODO Auto-generated method stub
		try{
		    File file = new File(s + index);
		    FileOutputStream fout = new FileOutputStream(file);
		    FileChannel fc = fout.getChannel();
		    ByteBuffer byt = null;
			while((byt=writePage())!=null){
				byt.limit(byt.capacity());
		        byt.position(0);
	            fc.write(byt);
			}
			fout.close();
	    }catch(Exception e){
			System.out.println("An exception occurs!");
		}
		reset();
	}

	/**
     * fetch the tuples iteratively and write them on a page.
     * @return the byte buffer contains data.
     */
	@Override
	public ByteBuffer writePage() {
		// TODO Auto-generated method stub
		ByteBuffer buffer = ByteBuffer.allocate(4096);
		int index = 8, times = 0;
		Tuple tuple =  null;
		while((tuple=getNextTuple())!=null&&index+tuple.length()*8<=4096){
			buffer.putInt(0,tuple.length());
			for(int i=0;i<tuple.length();i++){
				buffer.putInt(index,tuple.getData(i));
				index += 4;
			}
			times++;
		}
		if(tuple!=null){
		    for(int i=0;i<tuple.length();i++){
			    buffer.putInt(index,tuple.getData(i));
			    index += 4;
		    }
		    times++;
		}
		if(times==0) return null; // no new tuples, no new byte buffers.
		while(index<4096){
			buffer.putInt(index,0);
			index += 4;
		}
		buffer.putInt(4,times);
		return buffer;
	}

}
