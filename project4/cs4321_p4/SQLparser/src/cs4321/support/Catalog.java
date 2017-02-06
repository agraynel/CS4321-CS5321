package cs4321.support;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cs4321.project2.Interpreter;

/** 
 * This is a supporting class telling the source files and tables,
 * the possible schemas for different tables.
 * @author jz699 JUNCHEN ZHEN
 *
 */
public class Catalog {

	
	private static final String base = "/db/data/";
	private static final String scheme = "/db/schema.txt";
	// the base tables for the input.
	private Map<String, String> map; // use a map to link to the file location.
	private Map<String, Integer> schema; // use a map to link to the schema.
	private Map<String, Map<String, Integer>> index;
	// use an index map to link the table with their respective schema.
	private static Catalog instance = new Catalog(); // the instance to be returned.
	private static final String indexing = "/db/index_info.txt";
	// the file location to the index file.
	private Map<String, Integer> cluster; // this tells whether the table is clustered or not.
	private Map<String, Integer> order; // this tells the order of the B+ Tree.
	private Set<String> marked; // this tells whether the column is the index one.
	private Map<String, String> map2; // this connects the index to the table.
	
	/**
	 * the constructor of the Catalog. It parse the scheme and set the map.
	 * 
	 */
	private Catalog(){
		map = new HashMap<>();
		index = new HashMap<>();
		cluster = new HashMap<>();
		order = new HashMap<>();
		marked = new HashSet<>();
		map2 = new HashMap<>();
		String s;
		try{
			FileReader file = new FileReader(Interpreter.getInput()+scheme);
		    BufferedReader br = new BufferedReader(file);
		    while((s = br.readLine())!=null){
		    	String[] str = s.split("\\s+");
		    	map.put(str[0], Interpreter.getInput()+base+str[0]);
		    	schema = new HashMap<>();
		    	for(int i=1;i<str.length;i++)
		    		schema.put(str[0]+"."+str[i], i-1);
		    	index.put(str[0], schema);
		    }
		    br.close();
			File file1 = new File(Interpreter.getInput()+indexing);
			BufferedReader buffer = new BufferedReader(new FileReader(file1));
			String s1;
			while((s1=buffer.readLine())!=null){
				String[] str = s1.split("\\s+");
				String temp = str[0] + "." + str[1];
				int index1 = Integer.parseInt(str[2]);
				int index2 = Integer.parseInt(str[3]);
				marked.add(temp);
				cluster.put(str[0], index1);
				order.put(str[0], index2);
				map2.put(str[0], str[1]);
			}
			buffer.close();
		}catch(IOException e){
			System.out.println("Files not found!");
		}
	}
	
	/**
	 * get the instance object.
	 * @return the Catalog object instance.
	 */
	public static Catalog getInstance(){
		return instance;
	}
	
	/**
	 * return the path of the file using the table name.
	 * @param s the input of the table name.
	 * @return the file location.
	 */
	public String getFileLocation(String s){
		if(!map.containsKey(s)) return null;
		return map.get(s);
	}
	
	/**
	 * return the column location of the tuple using the column name. 
	 * @param s is the input of the column name.
	 * @return the column location in the tuple
	 */
	public int getColumn(String s) {
		String[] duck = s.split("\\.");
		if(!index.containsKey(duck[0])) return -1;
		Map<String,Integer> temp = index.get(duck[0]);
		return temp.get(s);
	}
	
	/**
	 * the getter method of schema hash map.
	 * @return the schema hash map.
	 */
	public Map<String,Integer> getSchema(String s) {
		return index.get(s);
	}
	
	/**
	 * return whether the table is clustered or not.
	 * @param s the table name to check.
	 * @return 0 shows the B+ Tree is not clustered, 
	 * 1 shows the B+ Tree is clustered.
	 */
	public int getCluster(String s){
		return cluster.get(s);
	}
	
	/**
	 * return the order of the B+ Tree for the specified table.
	 * @param s the table name to check.
	 * @return the order of the tree.
	 */
	public int getOrder(String s){
		return order.get(s);
	}
	
	/**
	 * return whether this column is the index.
	 * @param s the table and column name to check the result.
	 * @return whether the column is in the index.
	 */
	public boolean hasColumn(String s){
		return marked.contains(s);
	}
	
	/**
	 * return the index of the specific table.
	 * @param s the name of that table.
	 * @return the index of that table.
	 */
	public String getIndex(String s) {
		return map2.get(s);
	}
	
}
