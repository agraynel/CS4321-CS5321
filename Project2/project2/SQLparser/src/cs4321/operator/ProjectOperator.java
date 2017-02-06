package cs4321.operator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs4321.support.Catalog;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectItem;

/**
 * This is the class that handles the projection part.
 * @author jz699 JUNCHEN ZHAN
 *
 */
public class ProjectOperator extends Operator{

	private SelectOperator seo; // the select operator as candidate.
	private ScanOperator sco; // the scan operator as candidate.
	private boolean isAsterik = true; // check whether we need to modify the tuple.
	private boolean isScan; // check whether the object is scan operator or not.
	private int length; // the length of the new tuple.
	private int[] map; // map that connects the indexes between the order in query
	// and the order in scheme.
	private Tuple tup;
	private Map<String,Integer> location;
	// the location of a column to the tuple index. Used in the sort operator.
	
	ProjectOperator() {}
	
	/**
	 * Constructor: check whether the where language is null, if it is, 
	 * create a ScanOperator; if it is not, create a SelectOperator. 
	 * @param file the file that is going to be parsed.
	 * @param ps the SQL language.
	 * @param hash the hash map that handles the conversion of aliases.
	 */
	public ProjectOperator(File file, PlainSelect ps, Map<String,String> hash) {
		if(ps.getWhere()==null){
			sco = new ScanOperator(file);
			isScan = true;
		}else{
			seo = new SelectOperator(file,ps.getWhere(),hash);
		}
		location = new HashMap<>();
		String sng = ps.getFromItem().toString();
		String[] sn = sng.trim().split("\\s+");
		@SuppressWarnings("unchecked")
		List<SelectItem> list = ps.getSelectItems();
		if(!list.get(0).toString().equals("*")){
			map = new int[list.size()];
			for(int i=0;i<list.size();i++){
				String[] str = list.get(i).toString().split("\\.");
				String s = hash.get(str[0])+"."+str[1];
				// s stands for the original column name, used for catalog.
				Catalog catalog = Catalog.getInstance();
				map[i] = catalog.getColumn(s);
				location.put(list.get(i).toString(), i);
			}
			isAsterik = false;
			length = list.size();
		}
		else{
			Map<String,Integer> man = Catalog.getInstance().getSchema(sn[0]);
			for(Map.Entry<String, Integer> entry: man.entrySet()){
				String[] group = entry.getKey().split("\\.");
				location.put(sn[sn.length-1]+"."+group[1], entry.getValue());
				// sn[sn.length-1] is the table name used for sort.
			}
		}
	}
	
	/**
	 * get the nextTuple of the result.
	 * consider the case when the select part is null or when it is not null.
	 */
	@Override
	public Tuple nextTuple() {
		// TODO Auto-generated method stub
		Tuple tuple = null;
		if(isScan) tuple = sco.nextTuple();
		else tuple = seo.nextTuple();
		if(!isAsterik&&tuple!=null){
			Tuple temp = new Tuple(length);
			for(int i=0;i<length;i++)
				temp.setData(i, tuple.getData(map[i]));
			tuple = temp;
		}
	    tup = tuple;
		return tuple;
	}

	/**
	 * reset the pointer to the starting point.
	 */
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		if(isScan) sco.reset();
		else seo.reset();
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
	 * return the tuple that the pointer points.
	 * @return the tuple right now.
	 */
    public Tuple getTuple(){
    	return tup;
    }

    /**
     * returns the location map which connect the column with their indexes.
     * @return the location map.
     */
    public Map<String,Integer> getMap(){
    	return location;
    }
	
}
