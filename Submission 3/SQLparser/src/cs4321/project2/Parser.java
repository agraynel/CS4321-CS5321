package cs4321.project2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import logicalqueryplan.DuplicateEliminationOperators;
import logicalqueryplan.HashDuplicateEliminationOperators;
import logicalqueryplan.JoinOperators;
import logicalqueryplan.Operators;
import logicalqueryplan.ProjectOperators;
import logicalqueryplan.SortOperators;
import cs4321.operator.PhysicalPlanBuilder;
import net.sf.jsqlparser.statement.select.PlainSelect;

/**
 * This class shows how to parse a query and fetch the desired information out.
 * @author jz699 JUNCHEN ZHAN
 *
 */
public class Parser {
	
	private static Map<String, String> map;
	private static Operators ops; // the super class of logical operators.
	
	/**
	 * handle the plain select language and construct the query plan
	 * to handle the result. 
	 * @param ps the plainSelect needed to be parsed.
	 * @param op the operator to be handled.
	 * @param index the index of the output files.
	 */
	public static void handle(PlainSelect ps, String s, int index){
		map = new HashMap<>();
		String str = ps.getFromItem().toString();
		String[] orz = str.split("\\s+");
		if(orz.length==3) map.put(orz[2], orz[0]);
		else map.put(orz[0], orz[0]);
		@SuppressWarnings("rawtypes")
		List list = ps.getJoins();
		String[] temp = null;
		if(list!=null) temp = new String[list.size()+1];
		else temp = new String[1];
		String ab = ps.getFromItem().toString();
		String[] ss = ab.split("\\s+");
		temp[0] = ss[ss.length-1];
		for(int i=1;i<temp.length;i++){
			String abc = list.get(i-1).toString();
			String[] sss = abc.split("\\s+");
			temp[i] = sss[sss.length-1];
			if(sss.length==3) map.put(sss[2], sss[0]);
			else map.put(sss[0], sss[0]);
		}
		PhysicalPlanBuilder ppb = new PhysicalPlanBuilder(temp,ps,map,0);
		JoinOperators join = new JoinOperators();
		ops = new ProjectOperators(join);
		if(ps.getOrderByElements()!=null){
			ops = new SortOperators(ops);
			if(ps.getDistinct()!=null) 
			    ops = new DuplicateEliminationOperators(ops);
		}else if(ps.getDistinct()!=null)
		    ops = new HashDuplicateEliminationOperators(ops);
		ops.accept(ppb);
		ppb.dump(s,index);
	}
	
}
