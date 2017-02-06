package cs4321.operator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * This class scans the whole table and type out by tuples.
 * @author jz699 JUNCHEN ZHAN
 *
 */
public class ScanOperator extends Operator implements TupleReader{

	private File file; // the file object that needs to be taken.
	private RandomAccessFile br; // the reader object.
	private FileChannel fc; // the file channel to be used for random access file.
	private ByteBuffer byt; // the byte buffer that will be used.
	private int index; // the index indicates which byte will be read.
	private int length; // the length of the tuple.
	private int volume; // the number of tuples this page has.
	
	/**
	 * Constructor: set the file to the ScanOperator
	 * and the buffered reader.
	 * @param file the input file.
	 */
	public ScanOperator(File file){
		this.file = file;
		try{
		    br = new RandomAccessFile(this.file, "r");
		    fc = br.getChannel();
		    byt = readPage();
		}catch(FileNotFoundException e){
			System.out.println("File not found!");
		}
		index = 8;
		length = byt.getInt(0);
		volume = byt.getInt(4);
	}
	
	/**
	 * get the next Tuple of the scan.
	 * @return the Tuple class.
	 */
	@Override
	public Tuple getNextTuple() {
		// TODO Auto-generated method stub
		Tuple tuple = null;
		// check whether we have reached the end of this page.
		if(index<length*volume*4+8){
			int[] data = new int[length];
			for(int i=0;i<length;i++){
				data[i] = byt.getInt(index);
				index += 4;
			}
			tuple = new Tuple(data);
		}else{
			byt = readPage(); // read a new page.
			if(byt==null) return null; // reach the end of file, return null.
			index = 8;
			volume = byt.getInt(4); // stupid mistake! it is getInt(), not get()!
			int[] data = new int[length];
			for(int i=0;i<length;i++){
				data[i] = byt.getInt(index);
				index += 4;
			}
			tuple = new Tuple(data);
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
			br.seek(0);;
			fc = br.getChannel();
			byt = readPage();
		}catch(IOException e){
			System.out.println("Files not found.");
		}
		index = 8;
		length = byt.getInt(0);
		volume = byt.getInt(4);
	}

	/**
	 * this method reads the page from a given file.
	 * @return the byte buffer of the page.
	 */
	@Override
	public ByteBuffer readPage() {
		// TODO Auto-generated method stub
		ByteBuffer buffer = ByteBuffer.allocate(4096);
		int total = 0;
		try {
			total = fc.read(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(total==-1) return null; // at the end of the file, return null.
		return buffer;
	}
	
}
