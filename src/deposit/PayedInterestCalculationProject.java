package deposit;

import java.text.NumberFormat;
import java.util.*;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class PayedInterestCalculationProject {
	
	
	@SuppressWarnings("")
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
	public static void main(String[] args) throws IOException {
		BufferedReader stdin=new BufferedReader(new InputStreamReader(System.in));
		SaxParser sp=null;
		/*while(true){
			System.out.println("Pleas enter the input file name:");
			String fileName=stdin.readLine();
			try{
				sp=new SaxParser(fileName);
				break;
			}catch(FileNotFoundException e){
				System.err.println("Bad file name!");
				//System.exit(1);
			}
		}*/
		sp=new SaxParser("xmlFile.xml");
		List objectsList=sp.extractObjects();
		System.out.println(objectsList);
		descendingSort(objectsList);
		Iterator it=objectsList.iterator();
		PrintWriter outWriter=new PrintWriter(new BufferedWriter(new FileWriter("output.txt")));
		while(it.hasNext()){
			outWriter.println(it.next());
		}
		outWriter.close();
		
	}

}
