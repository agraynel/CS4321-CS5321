package cs4321.support;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * This class is used to generate a lot of Tuples that will
 * be used for the benchmarking of this program.
 * @author jz699 JUNCHEN ZHAN
 *
 */
public class TupleGenerator {
    
	private static String location;
	private String[] str;
	private int[] column;
	
	public TupleGenerator(){
		for(int i=0;i<3;i++){
			try{
				File file1 = new File(location+str[i]+1);
				File file2 = new File(location+str[i]+2);
				StringBuilder sb = new StringBuilder();
				FileOutputStream fout = new FileOutputStream(file1);
				FileChannel fc = fout.getChannel();
				ByteBuffer buffer = ByteBuffer.allocate(4096);
				buffer.putInt(0,column[i]);
				int index = 8, times = 0;
				for(int j=0;j<10000;j++){
					String s = "";
					if(index+column[i]*4>4096){
						buffer.putInt(4,times);
					    buffer.position(0);
					    fc.write(buffer);
					    index = 8;
					    times = 0;
					}
					for(int k=0;k<column[i];k++){
						int random = (int)(Math.random()*10000);
                        buffer.putInt(index,random);	
                        index += 4;
                        s = s + random + ",";
					}
					sb.append(s.substring(0,s.length()-1));
					sb.append("\n");
					times++;
				}
			    BufferedWriter output = new BufferedWriter(new FileWriter(file2));
				output.write(sb.toString());
				output.close();
				buffer.putInt(4,times);
			    buffer.position(0);
			    fc.write(buffer);
			    fout.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
}
