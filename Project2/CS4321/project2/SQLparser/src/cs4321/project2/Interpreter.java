package cs4321.project2;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import cs4321.operator.Operator;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

/**
 * This class intends to become the interpreter of those queries.
 * @author jz699, JUNCHEN ZHAN
 *
 */
public class Interpreter {
     
	private CCJSqlParser parser; // the file about the query.
	private static final String inputdir = "C:/Users/messfish/Documents/CS4321/samples/input/queries.sql";
	// this String is used for inputs.
	private final static String outputdir = "C:/Users/messfish/Documents/CS4321/samples/output/query";
    // this String is used for outputs.
	private Operator op;
	
	/**
	 * Constructor: create the parser by the expected query.
	 * @param query the location of the query File.
	 * @throws FileNotFoundException throws this exception out
	 * when the file is not found.
	 */
	public Interpreter(String query) throws FileNotFoundException{
		parser = new CCJSqlParser(new FileReader(query));
	}
	
	/**
	 * get the next line of the queries and fetch the result.
	 * @param the index of the output file.
	 * @return the Statement to check whether it is null.
	 */
	public Statement getLine(int index){
		Statement state = null;
		try{
			if((state = parser.Statement())!=null){
				System.out.println("Read statement: " + state);
				Select select = (Select) state;
				PlainSelect ps = (PlainSelect)select.getSelectBody();
				Parser.handle(ps,op,outputdir,index);
			}
		}catch(Exception e){
			System.out.println("An exception occurs!");
		}
		return state;
	}
	
	/**
	 * get all the queries and deal with them at once.
	 */
	public void dump(){
		int index = 1;
		while(getLine(index)!=null)
			index++;
	}
	
	public static void main(String[] args){
		try{
		    Interpreter ip = new Interpreter(inputdir);
		    ip.dump();
		}catch(IOException e){
			System.out.print("Files not found!");
		}
	}
	
}
