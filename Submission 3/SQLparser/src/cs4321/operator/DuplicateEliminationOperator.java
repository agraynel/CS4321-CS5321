package cs4321.operator;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

/**
 * this is the class which handles the distinct operator
 * when the tuples are sorted.
 * @author jz699 JUNCHEN ZHAN
 *
 */
public class DuplicateEliminationOperator extends Operator implements TupleWriter{

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
	public Tuple getNextTuple() {
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
