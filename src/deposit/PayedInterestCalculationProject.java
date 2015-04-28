package deposit;

import java.text.NumberFormat;
import java.util.*;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * This class contains the main function to solve the specified problem
 * @author Bahar Saffarian <zohreh.saffarian@gmail.com>
 * @version 1
 * @since 2015-04-21
 */
public class PayedInterestCalculationProject {

	/**
	 * sorts a list of Deposit objects in a decreasing way
	 * this method implements the 'Comparator' interface by itself
	 * @param ol the list of Deposit objects to be decreasingly sorted
	 */
	static void descendingSort(List ol){
		Object[] objectsArray=ol.toArray();
		Arrays.sort(objectsArray,new Comparator(){
			public int compare(Object obj1, Object obj2) {
				BigDecimal e1=new BigDecimal(((Deposit)obj1).getPayedInterest());
				BigDecimal e2=new BigDecimal(((Deposit)obj2).getPayedInterest());
				return (e1.compareTo(e2)<0 ? 1 : (e1.compareTo(e2)==0 ? 0 : -1));
			}
		});
		ol.clear();
		for(int i=0;i<objectsArray.length;i++){
			ol.add(objectsArray[i]);
		}
	}

	/**
	 * the main method gets the path of input XML file and writes the desired results in a output file named "output.txt"
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		BufferedReader stdin=new BufferedReader(new InputStreamReader(System.in));
		SaxParser sp=null;
		while(true){
			System.out.println("Pleas enter the input file name:");
			String fileName=stdin.readLine();
			try{
				sp=new SaxParser(fileName);
				break;
			}catch(FileNotFoundException e){
				System.err.println("Bad file name!");
				//System.exit(1);
			}
		}
		//sp=new SaxParser("xmlFile.xml");
		List objectsList=sp.extractObjects();
		//System.out.println(objectsList);
		descendingSort(objectsList);
		Iterator it=objectsList.iterator();
		PrintWriter outWriter=new PrintWriter(new BufferedWriter(new FileWriter("output.txt")));
		while(it.hasNext()){
			outWriter.println(it.next());
		}
		outWriter.close();
		
	}

}
