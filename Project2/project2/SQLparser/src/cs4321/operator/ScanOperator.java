package cs4321.operator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * This class scans the whole table and type out by tuples.
 * @author jz699 JUNCHEN ZHAN
 *
 */
public class ScanOperator extends SelectOperator{

	private File file; // the file object that needs to be taken.
	private RandomAccessFile br; // the reader object.
	
	/**
	 * Constructor: set the file to the ScanOperator
	 * and the buffered reader.
	 * @param file the input file.
	 */
	public ScanOperator(File file){
		this.file = file;
		try{
		    br = new RandomAccessFile(this.file, "r");
		}catch(FileNotFoundException e){
			System.out.println("File not found!");
		}
	}
	
	/**
	 * get the next Tuple of the scan.
	 * @return the Tuple class.
	 */
	@Override
	public Tuple nextTuple() {
		// TODO Auto-generated method stub
		Tuple tuple = null;
		try{
			String s = br.readLine();
			if(s==null) return null;
			tuple = new Tuple(s);
		}catch(IOException e){
			System.out.print("No more tuples!");
		}
		return tuple;
	}
	
	/**
	 * reset the pointer to the start point.
	 */
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		try{
			br.seek(0);
		}catch(IOException e){
			System.out.println("Files not found.");
		}
	}
	
	/**
	 * for debugging, get all the tuples at once and put them in a file.
	 * @param index the index of the output file.
	 */
	@Override
	public void dump(String s, int index) {
		// TODO Auto-generated method stub
		Tuple tuple = null;
		BufferedWriter output = null;
		try{
		    File file = new File(s + index);
		    StringBuilder sb = new StringBuilder();
		    output = new BufferedWriter(new FileWriter(file));
			while((tuple = nextTuple())!=null){
	            sb.append(tuple.toString());
	            sb.append("\n");
	            System.out.println(tuple);
			}
			output.write(sb.toString());
			output.close();
	    }catch(IOException e){
			System.out.println("An exception occurs!");
		}
		reset();
	}
	
	
}
