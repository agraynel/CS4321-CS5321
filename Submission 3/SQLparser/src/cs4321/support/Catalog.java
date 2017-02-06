package cs4321.support;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
	
	/**
	 * the constructor of the Catalog. It parse the scheme and set the map.
	 * 
	 */
	private Catalog(){
		map = new HashMap<>();
		index = new HashMap<>();
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
	
}
