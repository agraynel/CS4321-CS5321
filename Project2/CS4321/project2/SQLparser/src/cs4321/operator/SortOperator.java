package cs4321.operator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import net.sf.jsqlparser.statement.select.PlainSelect;


/**
 * class that handles the order by query.
 * @author jz699 JUNCHEN ZHAN
 *
 */
public class SortOperator extends Operator{

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
	public SortOperator(Operator operator, PlainSelect ps, Map<String, Integer> map) {
		list = new ArrayList<Tuple>();
		pls = ps;
		man = map;
		Tuple tuple = null;
		while((tuple=operator.nextTuple())!=null)
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
	public Tuple nextTuple() {
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
	 * get the list of tuples used for distinct query.
	 * @return the list contains all tuples.
	 */
	public List<Tuple> getList(){
		return list;
	}
}
