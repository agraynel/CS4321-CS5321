package cs4321.operator;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import net.sf.jsqlparser.statement.select.PlainSelect;


/**
 * class that handles the order by query.
 * use collections.sort() to sort the query.
 * @author jz699 JUNCHEN ZHAN
 *
 */
public class SortOperator extends Operator implements TupleWriter{

	private List<Tuple> list; // list that handles the tuples.
	private final PlainSelect pls; 
	private final Map<String,Integer> man; 
	// map contains the connection of column name to their respective index.
	private int index; // the index of the list.
	
	/**
	 * constructor: get all the tuples in the list and sort them.
	 * @param operator the operator needs to be sorted.
	 * @param ps the query language.
	 * @param map the map contains the connection of column name to their respective index.
	 */
	public SortOperator(ProjectOperator operator, PlainSelect ps, Map<String, Integer> map) {
		list = new ArrayList<Tuple>();
		pls = ps;
		man = map;
		Tuple tuple = null;
		while((tuple=operator.getNextTuple())!=null)
			list.add(tuple);
		Collections.sort(list,new Comparator<Tuple>(){
			@SuppressWarnings("rawtypes")
		    List order = pls.getOrderByElements();
			@Override
			public int compare(Tuple tup1, Tuple tup2) {
				// TODO Auto-generated method stub
				// sort tuples from the order by language first.
				for(int i=0;i<order.size();i++){
					String str = order.get(i).toString();
					int index = man.get(str);
					if(tup1.getData(index)>tup2.getData(index)) return 1;
					else if(tup1.getData(index)<tup2.getData(index)) return -1;
				}
				// sort tuples by the order of the tuples.
				for(int i=0;i<man.size();i++){
					if(tup1.getData(i)>tup2.getData(i)) return 1;
					else if(tup1.getData(i)<tup2.getData(i)) return -1;
				}
				return 0;
			}
			
		});
		operator.reset();
	}
	
	/**
	 * get the next tuple of the operator.
	 */
	@Override
	public Tuple getNextTuple() {
		// TODO Auto-generated method stub
		Tuple tuple = null;
		if(index<list.size()) tuple = list.get(index);
		index++;
		return tuple;
	}

	/**
	 * reset the operator.
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

	/**
	 * get the list of tuples used for distinct query.
	 * @return the list contains all tuples.
	 */
	public List<Tuple> getList(){
		return list;
	}

}
