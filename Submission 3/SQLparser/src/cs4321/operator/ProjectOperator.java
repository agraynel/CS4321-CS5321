package cs4321.operator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs4321.support.Catalog;
import net.sf.jsqlparser.statement.select.SelectItem;

/**
 * This is the class that handles the projection part.
 * @author jz699 JUNCHEN ZHAN
 *
 */
public class ProjectOperator extends Operator implements TupleWriter{

	private JoinOperator jo; // the join operator as candidate.
	private boolean isAsterik = true; // check whether we need to modify the tuple.
	private Map<String,String> hash; // map the aliases with their respective tables.
	private Map<String,Integer> location;
	// the location of a column to the tuple index. Used in the sort operator.
	
	/**
	 * Constructor: create a project operator based on a join operator.
	 * store some important message in the location for sorting.
	 * @param jo the JoinOperator.
	 */
	public ProjectOperator(JoinOperator jo) {
		this.jo = jo;
		this.hash = jo.hash;
		location = new HashMap<>();
		@SuppressWarnings("unchecked")
		List<SelectItem> list = jo.ps.getSelectItems();
		if(!list.get(0).toString().equals("*")){
			isAsterik = false;
			for(int i=0;i<list.size();i++){
				location.put(list.get(i).toString(), i);
			}
		}
		else{
			int index = 0;
			for(int i=0;i<jo.array.length;i++){
				Map<String,Integer> dum = Catalog.getInstance().getSchema(hash.get(jo.array[i]));
				for(Map.Entry<String, Integer> entry: dum.entrySet()){
					String[] s1 = entry.getKey().split("\\.");
					location.put(jo.array[i]+"."+s1[1], index+entry.getValue());
				}
				index += dum.size();
			}
		}
	}

	/**
	 * get the nextTuple of the result.
	 * @return the tuple.
	 */
	@Override
	public Tuple getNextTuple() {
		// TODO Auto-generated method stub
		Tuple tuple = jo.getNextTuple();
		if(!isAsterik&&tuple!=null){
		    int[] data = new int[jo.list.size()];
		    for(int i=0;i<data.length;i++){
		        String[] buff = jo.list.get(i).toString().split("\\.");
		        String last = hash.get(buff[0]) + "." + buff[1];
		        // recreate the string to be used in the catalog.
		        Tuple tu = jo.map2.get(buff[0]).getTuple();
		        data[i] = tu.getData(Catalog.getInstance().getColumn(last));
		    }
		    tuple = new Tuple(data);
		}
		return tuple;
	}

	/**
	 * reset the pointer to the starting point.
	 */
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		jo.reset();
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
		        byt.position(0);
	            fc.write(byt);
			}
			fout.close();
	    }catch(IOException e){
			System.out.println("An exception occurs!");
		}
		reset();
	}

    /**
     * returns the location map which connect the column with their indexes.
     * @return the location map.
     */
    public Map<String,Integer> getMap(){
    	return location;
    }
    
    /**
     * set the operator 
     * @param jo
     */
    public void setOperator(JoinOperator jo){
    	this.jo = jo;
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
