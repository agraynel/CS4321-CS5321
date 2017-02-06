package cs4321.operator;

import java.util.Map;

import net.sf.jsqlparser.statement.select.PlainSelect;

/**
 * This class handles the sort join operator.
 * It extends from Join Operator and use the constructor
 * from that class.
 * @author jz699 JUNCHEN ZHAN
 *
 */
public class SortMergeJoinOperator extends JoinOperator {

	public SortMergeJoinOperator(String[] array, PlainSelect ps,
			Map<String, String> hash) {
		super(array, ps, hash);
		// TODO Auto-generated constructor stub
	}

}
