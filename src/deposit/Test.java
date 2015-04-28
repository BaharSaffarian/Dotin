package deposit;
import java.io.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.parsers.*;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.*;

public class Test {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		/**/
		
		String ss="hi world";
		String s;
		
		try{
			BufferedReader in4=new BufferedReader(new StringReader(ss));
			FileWriter fw=new FileWriter("outFile.txt");
			PrintWriter out1=new PrintWriter(fw);
			int lc=1;
			while((s=in4.readLine())!=null){
				System.out.println(lc + ":"+s);
				//out1.println(lc++ + ":"+s);
			}
			
			out1.close();
			//fw.write("salam");
			fw.close();
		}catch(EOFException e){
			System.err.println("End of Stream");
		}
		try{
			DataOutputStream out2=new DataOutputStream(new BufferedOutputStream(new FileOutputStream("data.txt")));
			out2.writeDouble(3.14);
			out2.writeUTF("thats pi");
			out2.writeDouble(1.4141);
			out2.writeUTF("square root of 2");
			out2.close();
			DataInputStream in5=new DataInputStream(new BufferedInputStream(new FileInputStream("data.txt")));
			System.out.println(in5.readDouble());
			System.out.println(in5.readUTF());
		}catch(EOFException e){
			throw new RuntimeException(e);
		}
		RandomAccessFile rf=new RandomAccessFile("rtest.dat","rw");
		rf.writeDouble(3.14d);
		rf.close();
		rf=new RandomAccessFile("rtest.dat","r");
		System.out.println(rf.readDouble());
	}

}
