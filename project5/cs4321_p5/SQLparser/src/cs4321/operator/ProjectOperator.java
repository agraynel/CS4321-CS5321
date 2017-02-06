package cs4321.operator;

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
public class ProjectOperator extends Operator{

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
		        String str = jo.list.get(i).toString();
		        data[i] = tuple.getData(jo.location.get(str));
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
     * returns the location map which connect the column with their indexes.
     * @return the location map.
     */
    public Map<String,Integer> getMap(){
    	return location;
    }

}
