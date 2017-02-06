package cs4321.project2;

import static org.junit.Assert.*;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

import org.junit.Test;

import cs4321.operator.Tuple;

public class EvaluationTest {

	private static final String queriesFile = "C:/Users/messfish/Desktop/solami.txt";
	private Map<String,String> map = new HashMap<>();
	
	@Test
	public void test() {
		Tuple[] array = new Tuple[5];
		array[0] = new Tuple("101,2,3");
		array[1] = new Tuple("102,3,4");
		array[2] = new Tuple("104,104,202");
		array[3] = new Tuple("103,1,1");
		array[4] = new Tuple("107,2,8");
		map.put("S", "Sailors");
		try {
			CCJSqlParser parser = new CCJSqlParser(new FileReader(queriesFile));
			Statement statement;
			while ((statement = parser.Statement()) != null) {
				System.out.println("Read statement: " + statement);
				Select select = (Select) statement;
				PlainSelect sb = (PlainSelect)select.getSelectBody();
				Expression exp = sb.getWhere();
				String[] str = exp.toString().split("\\s+");
				for(int i=0;i<str.length;i++){
					String[] get = str[i].split("\\.");
					System.out.println(get[0]);
				}
				if(exp!=null){
				    for(int i=0;i<5;i++){
				        Evaluation eva = new Evaluation(array[i],map);
				        exp.accept(eva);
				        if(i==2) assertEquals(false,eva.getResult());
				        else assertEquals(true,eva.getResult());
				    }
				}
			}
		} catch (Exception e) {
			System.err.println("Exception occurred during parsing");
			e.printStackTrace();
		}
	}

}
