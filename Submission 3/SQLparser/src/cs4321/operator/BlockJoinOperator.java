package cs4321.operator;

import java.util.Map;

import net.sf.jsqlparser.statement.select.PlainSelect;

/**
 * This class handles the block join operator,
 * it extends the join operator to generate the same constructor.
 * @author jz699 JUNCHEN ZHAN
 *
 */
public class BlockJoinOperator extends JoinOperator {

	public BlockJoinOperator(String[] array, PlainSelect ps,
			Map<String, String> hash) {
		super(array, ps, hash);
		// TODO Auto-generated constructor stub
	}

}
