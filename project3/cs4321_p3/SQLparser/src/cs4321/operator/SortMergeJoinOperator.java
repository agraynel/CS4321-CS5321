package cs4321.operator;

import java.io.File;
import java.util.Map;

import cs4321.project2.EvaluationMklll;
import cs4321.project2.Interpreter;
import net.sf.jsqlparser.statement.select.PlainSelect;

/**
 * This class handles the sort join operator.
 * It extends from Join Operator and use the constructor
 * from that class.
 * @author jz699 JUNCHEN ZHAN
 *
 */
public class SortMergeJoinOperator extends JoinOperator {

	private ScanOperator ex; // the external sort operator.
	
	/**
	 * Constructor: extends from the join operator and assign the number of pages.
	 * put all the results in a temporary file and 
	 * create a scan operator to do it.
	 * @param array an array of strings.
	 * @param ps the plain select language.
	 * @param hash the connection between aliases.
	 * @param pages the number of pages that shall be used.
	 */
	public SortMergeJoinOperator(String[] array, PlainSelect ps,
			Map<String, String> hash, int pages) {
		super(array, ps, hash);
		EvaluationMklll eva2 = new EvaluationMklll(map2,array,map3,hash,location,pages);
		express.accept(eva2);
		File file = new File(Interpreter.getTemp() + "dummy " + (array.length-1));
		ex = new ScanOperator(file);
	}

	/**
	 * get the next tuple from this operator.
	 * @return the tuple that shall be returned.
	 */
	@Override
	public Tuple getNextTuple() {
		// TODO Auto-generated method stub
		return ex.getNextTuple();
	}

	/**
	 * reset the point back to the start of the table.
	 */
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		ex.reset();
	}

}
