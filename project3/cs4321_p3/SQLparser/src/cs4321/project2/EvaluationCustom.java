package cs4321.project2;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.schema.Column;
import cs4321.operator.Operator;
import cs4321.operator.ScanOperator;
import cs4321.operator.SelectOperator;
import cs4321.operator.Tuple;

public class EvaluationCustom extends EvaluationMkll {

	private int pages; // the number of pages that could be used.
	private File file; // the temporary file that will be used.
	private int[] indexes; // this maps the corrext column.
	private Map<String,Integer> connect; // connect the tables to their indexes.
	private Tuple tuple; // a place to store temporary tuples.
	
	/**
	 * constructor: extend the constructor from EvaluationMKll and set the pages.
	 * @param map the connection of tables to select operators.
	 * @param array the array of tables
	 * @param map2 the connection of tables to their respective tuples.
	 * @param hash the connection of aliases to their original tables.
	 * @param pages the number of pages that shall be used.
	 */
	public EvaluationCustom(Map<String, SelectOperator> map, String[] array,
			Map<String, Tuple> map2, Map<String, String> hash, int pages) {
		super(map, array, map2, hash);
		this.pages = pages;
		indexes = new int[length+1];
		connect = new HashMap<>();
		for(int i=0;i<array.length;i++)
			connect.put(array[i], i);
	}

	/**
	 * Override the method of Parenthesis node for the sake of the 
	 * block nested loop join.
	 * @param node the parenthesis node to be visited.
	 */
	@Override
	public void visit(Parenthesis node) {
		Operator seo1 = null;
		SelectOperator seo2 = null;
	    if(length==1){
	    	seo1 = map.get(array[0]);
	    	seo2 = map.get(array[1]);
	    }else{
	    	length--;
	    	AndExpression ae = (AndExpression)(node.getExpression());
	    	ae.getLeftExpression().accept(this);
	    	length++;
	    	seo1 = new ScanOperator(file);
	    	seo2 = map.get(array[length]);
	    }
        Tuple tuple = seo1.getNextTuple();
        file = new File(Interpreter.getTemp() + "dummy " + length);
        if(tuple==null){
        	try {
				FileOutputStream fout = new FileOutputStream(file);
	        	fout.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	return;
        }
        int stupid = tuple.length();
        // do not let local variables and fileds share the variable name!
        int temp = 1022 / stupid;
        int maxVolume = temp * pages;
        // maximum tuples that could be allocated on the block.
        indexes[length] = stupid; 
        try{
            FileOutputStream fout = new FileOutputStream(file);
    		FileChannel fc = fout.getChannel();
    		ByteBuffer byt = ByteBuffer.allocate(4096); 
    		int point = 8, times = 0;
    		// first loop: for each block in the outer table.
            while(tuple!=null){
            	Tuple[] list = new Tuple[maxVolume];
            	int index = 0;
            	while(tuple!=null&&index<maxVolume){
            		list[index++] = tuple;
            		tuple = seo1.getNextTuple();
            	}
            	Tuple tuple2 = null;
            	// second loop: for each tuple in a inner table.
            	while((tuple2=seo2.getNextTuple())!=null){
            		map2.put(array[length], tuple2);
            		isValid = true;
            		// third loop: for each tuple in the block.
            		for(int i=0;i<index;i++){
            			this.tuple = list[i];
            			node.getExpression().accept(this);
            			if(isTuple()){
            				if(point+list[i].length()*4+tuple2.length()*4<=4096){
            					byt.putInt(0,list[i].length()+tuple2.length());
            					for(int j=0;j<list[i].length();j++){
            						byt.putInt(point,list[i].getData(j));
            						point += 4;
            					}
            					for(int j=0;j<tuple2.length();j++){
            						byt.putInt(point,tuple2.getData(j));
            						point += 4;
            					}
            					times++;
            				}else{
            					byt.putInt(4,times);
            					byt.position(0);
            					fc.write(byt);
            					byt = ByteBuffer.allocate(4096);
            					point = 8;
            					times = 0;
            					byt.putInt(0,list[i].length()+tuple2.length());
            					for(int j=0;j<list[i].length();j++){
            						byt.putInt(point,list[i].getData(j));
            						point += 4;
            					}
            					for(int j=0;j<tuple2.length();j++){
            						byt.putInt(point,tuple2.getData(j));
            						point += 4;
            					}
            					times++;
            				}
            			}
            		}
            	}
            	seo2.reset();
            }
            byt.putInt(4,times);
            byt.position(0);
            fc.write(byt);
            fout.close();
        }catch(Exception e){
            e.printStackTrace();
        }
	}
	
	/**
	 * override the method in the visit method of column node.
	 * @param node the column node needs to be visited.
	 */
	@Override
	public void visit(Column node) {
		// TODO Auto-generated method stub
		String str = node.getWholeColumnName();
		String[] dummy = str.split("\\.");
		str = hash.get(dummy[0]) + "." + dummy[1];
		// reset the string to make it available for searching in catalogs.
		String temp = node.getTable().getName();
		int index = catalog.getColumn(str);
		if(connect.get(dummy[0])==length)
			stack1.push((long)map2.get(temp).getData(index));
		else stack1.push((long)tuple.getData(index + indexes[connect.get(dummy[0])]));
	}
	
}
