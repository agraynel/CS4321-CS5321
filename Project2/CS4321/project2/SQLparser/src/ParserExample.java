import java.io.FileReader;
import java.util.List;

import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

/**
 * Example class for getting started with JSQLParser. Reads SQL statements from
 * a file and prints them to screen; then extracts SelectBody from each query
 * and also prints it to screen.
 * 
 * @author Lucja Kot
 */
public class ParserExample {

	private static final String queriesFile = "C:/Users/messfish/Desktop/queries.sql";

	public static void main(String[] args) {
		try {
			CCJSqlParser parser = new CCJSqlParser(new FileReader(queriesFile));
			Statement statement;
			while ((statement = parser.Statement()) != null) {
				System.out.println("Read statement: " + statement);
				Select select = (Select) statement;
				PlainSelect sb = (PlainSelect)select.getSelectBody();
				@SuppressWarnings({ "unused", "unchecked" })
				List<FromItem> join = sb.getJoins();
				@SuppressWarnings("rawtypes")
				List order = sb.getOrderByElements();
				if(order!=null){
				    for(int i=0;i<order.size();i++)
				        System.out.println("Order By is " +  order.get(i));
				}
			}
		} catch (Exception e) {
			System.err.println("Exception occurred during parsing");
			e.printStackTrace();
		}
		
	}
}