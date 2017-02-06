package cs4321.operator;

import java.io.File;
import java.util.Map;

import cs4321.project2.EvaluationCustom;
import cs4321.project2.Interpreter;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.select.PlainSelect;

/**
 * This class handles the block join operator,
 * it extends the join operator to generate the same constructor.
 * @author jz699 JUNCHEN ZHAN
 *
 */
public class BlockJoinOperator extends JoinOperator {

	private ScanOperator ex; // the scan operator used for output.
	
	/**
	 * Constructor: extends the constructor from the join operator 
	 * and pass the number of pages to the evaluation test.
	 * @param array an array of table names
	 * @param ps the plain select
	 * @param hash the connection of table name and scheme name
	 * @param pages the number of pages used for block nested loop join.
	 */
	public BlockJoinOperator(String[] array, PlainSelect ps,
			Map<String, String> hash, int pages, Map<String, SelectOperator> map2,
			Map<String, Tuple> map3, Expression express) {
		super(array, ps, hash, map2, map3, express);
		EvaluationCustom eva = new EvaluationCustom(map2,array,map3,hash,pages);
		express.accept(eva);
		File file = new File(Interpreter.getTemp() + "dummy " + (array.length-1));
		ex = new ScanOperator(file);
	}

	/**
	 * return the next Tuple.
	 * @return the tuple on the next pointer.
	 */
	@Override
	public Tuple getNextTuple(){
		return ex.getNextTuple();
	}
	
	/**
	 * reset the point back to the start of the table.
	 */
	@Override
	public void reset() {
		ex.reset();
	}
	
}
