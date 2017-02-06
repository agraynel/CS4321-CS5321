package cs4321.project2;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs4321.operator.DuplicateEliminationOperator;
import cs4321.operator.HashDuplicateEliminationOperator;
import cs4321.operator.JoinOperator;
import cs4321.operator.Operator;
import cs4321.operator.ProjectOperator;
import cs4321.operator.ScanOperator;
import cs4321.operator.SelectOperator;
import cs4321.operator.SortOperator;
import cs4321.support.Catalog;
import net.sf.jsqlparser.statement.select.PlainSelect;

/**
 * This class shows how to parse a query and fetch the desired information out.
 * @author jz699 JUNCHEN ZHAN
 *
 */
public class Parser {
	
	private static Map<String, String> map;
	
	/**
	 * handle the plain select language and construct the query plan
	 * to handle the result. 
	 * @param ps the plainSelect needed to be parsed.
	 * @param op the operator to be handled.
	 * @param index the index of the output files.
	 */
	public static void handle(PlainSelect ps, Operator op, String s, int index){
		map = new HashMap<>();
		String str = ps.getFromItem().toString();
		String[] orz = str.split("\\s+");
		String dummy = Catalog.getInstance().getFileLocation(orz[0]);
		if(orz.length==3) map.put(orz[2], orz[0]);
		else map.put(orz[0], orz[0]);
		if(ps.getJoins()==null){
		    op = new ProjectOperator(new File(dummy), ps, map);
		    if(ps.getSelectItems().get(0).equals("*")){
		    	if(ps.getWhere()!=null)
		    	op = new SelectOperator(new File(dummy), ps.getWhere(), map);
		    	else op = new ScanOperator(new File(dummy));
		    }
		    @SuppressWarnings("rawtypes")
			List order = ps.getOrderByElements();
		    if(order!=null){
		    	op = new SortOperator(op,ps,((ProjectOperator)op).getMap());
		    	if(ps.getDistinct()!=null)
		    		op = new DuplicateEliminationOperator((SortOperator)op);
		    }else if(ps.getDistinct()!=null){
		    	op = new HashDuplicateEliminationOperator((ProjectOperator)op);
		    }
		}
		else{
			@SuppressWarnings("rawtypes")
			List list = ps.getJoins();
			String[] temp = new String[list.size()+1];
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
			op = new JoinOperator(temp,ps,map);
			@SuppressWarnings("rawtypes")
			List order = ps.getOrderByElements();
			if(order!=null){
		    	op = new SortOperator(op,ps,((JoinOperator)op).getLocation());
		    	if(ps.getDistinct()!=null)
		    		op = new DuplicateEliminationOperator((SortOperator)op);
			}else if(ps.getDistinct()!=null){
		    	op = new HashDuplicateEliminationOperator((JoinOperator)op);
		    }
		}
		op.dump(s, index);
	}
	
}
