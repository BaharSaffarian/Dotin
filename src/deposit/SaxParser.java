package deposit;

import java.io.*;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.util.*;

import javax.xml.parsers.*;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *@author Bahar Saffarian <zohreh.saffarian@gmail.com>
 * @version 1
 * @since 2015-04-21
 * Gets an XML input file via its constructor and extract the deposit class fields from the XML file when calling the
 * {@link #extractObjects()} method and return a List of Deposit class objects
 */
public class SaxParser {

	/**
	 * path of the XML input file
	 */
	InputStream xmlInput;

	/**
	 * The list resulted by parsing the XML input file
	 */
	List objects;

	/**
	 * The constructor gets the path of XML file to pars it when calling {@link #extractObjects()} method of this object
	 * @param fileName the path of input XML file
	 * @throws FileNotFoundException if the fileName argument contains an invalid path for XML file
	 */
	SaxParser(String fileName) throws FileNotFoundException{
		
		xmlInput=new FileInputStream(fileName);		
	}

	/**
	 * a Temporary class to hold the values needed to create an object of the Deposit class
	 */
	private class Temp{
		String depositTyp;
		long costomerNumber;
		String depositBalance;
		long durationInDay;
	}

	/**
	 * implements the 'DefaultHandler' Interface
	 * @return an Object of implemented 'DefaultHandler' interface
	 */
	private DefaultHandler saxHandler(){
		
		return new DefaultHandler(){
			{
				objects=new LinkedList();
			}

			/**
			 * an Stack of the elements which are not closed yet
			 */
			private Stack<String> elementStack = new Stack<String>();

			/**
			 * an Stack of Objects which their required fields are not extracted yet to be created completely
			 */
		    private Stack<Object> objectStack  = new Stack<Object>();
			
		    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		    	this.elementStack.push(qName);
		    	if("deposit".equals(qName))
		            this.objectStack.push(new Temp());
		    	
		    }

			/**
			 * if a completed Unite of an Deposit type is extracted from the XML file the related object is created via this method
			 * by calling {@link #createDepositObject(Class, long, String, long)}
			 * and added to the objects list
			 * if the type of Deposit was wrong or the balance value was negative or the duration vas zero or negative,
			 * an exception with appropriate message will be thrown and catch to wrap as a runtime exception and be thrown to print in console
			 * @param uri
			 * @param localName
			 * @param qName the name of the end tag
			 * @throws SAXException
			 */
		    public void endElement(String uri, String localName, String qName)throws SAXException {
		    	this.elementStack.pop();
		    	Object obj;
		    	if("deposit".equals(qName)){
		    		obj=this.objectStack.pop();
		    		try{
		    		if("ShortTerm".equals(((Temp)obj).depositTyp)){
		    			objects.add(createDepositObject(ShortTermDeposit.class,((Temp)obj).costomerNumber,((Temp)obj).depositBalance, ((Temp)obj).durationInDay));
		    		}else if("Qarz".equals(((Temp)obj).depositTyp)){
		    			objects.add(createDepositObject(QarzDeposit.class,((Temp)obj).costomerNumber,((Temp)obj).depositBalance, ((Temp)obj).durationInDay));
		    		}else if("LongTerm".equals(((Temp)obj).depositTyp)){
		    			objects.add(createDepositObject(LongTermDeposit.class,((Temp)obj).costomerNumber,((Temp)obj).depositBalance, ((Temp)obj).durationInDay));
		    		}else
		    			throw new  WrongInputException("Deposit Type entered for customer No: '"+((Temp)obj).costomerNumber+"' is not valid");
		    		}catch( WrongInputException e){
		    			System.err.println(e.getMessage());
		    		}catch(Throwable e){
		    			throw new RuntimeException(e);
		    		}

		    	}
		    }

			/**
			 * If the current element was a Deposit class field such as customer number, deposit balance or duration,
			 * or the element defines the type of deposit{long term, short term, Qarz} this method will complete the temporary object information
			 * that is placed at top of the objects stack
			 * @param ch value of current element
			 * @param start
			 * @param length
			 * @throws SAXException
			 */
		    public void characters(char ch[], int start, int length) throws SAXException {
		    	String value = new String(ch, start, length).trim();
		        if(value.length() == 0) return;
		        if("customerNumber".equals(currentElement())){
		            Temp t = (Temp) this.objectStack.peek();
		            t.costomerNumber = Long.parseLong(value);
		        }else if("depositType".equals(currentElement())){
		            Temp t = (Temp) this.objectStack.peek();
		            t.depositTyp = value;
		        }else if("depositBalance".equals(currentElement())){
		            Temp t = (Temp) this.objectStack.peek();
		            t.depositBalance = value;
		        }else if("durationInDays".equals(currentElement())){
		            Temp t = (Temp) this.objectStack.peek();
		            t.durationInDay = Long.parseLong(value);
		        }
		    }
		    
		    private String currentElement() {
		        return this.elementStack.peek();
		    }

			/**
			 * this method uses 'reflection' to create an object of a type that is sent via the 'c' argument
			 * @param c specifies the name of class which we want to create an object of it
			 * @param cn the input needed to create an abject of passed class: customer name
			 * @param db the input needed to create an abject of passed class: deposit balance
			 * @param dd the input needed to create an abject of passed class: duration in day
			 * @return
			 * @throws Throwable if the input of the Deposit constructor were not valid or the defined constructor were not find for the 'c' class
			 */
		    private Object createDepositObject(Class c,long cn, String db, long dd) throws Throwable  {
				try {
					Constructor constructor = c.getConstructor(new Class[]{long.class, String.class, long.class});
		    		return constructor.newInstance(new Object[]{new Long(cn),db,new Long(dd)});
		    	}catch(InvocationTargetException e){		    		
		    		throw e.getCause();
		    	}
		    	catch(Exception e){
		    		throw new RuntimeException(e);
		    	}
		    }

		};
	}

	/**
	 * The only public method of this class used to extract fields of Deposit class from the input XML file{@link #xmlInput}
	 * and create appropriate Deposit abjects related to the defined type in XML file and put the created objects in a list{@link #objects}
	 * @return a list of Deposit Objects extracted from XML input file
	 * @throws IOException
	 */
	List extractObjects() throws IOException{
		
		try{
		SAXParserFactory spf=SAXParserFactory.newInstance();
		SAXParser sp=spf.newSAXParser();
		sp.parse(xmlInput, saxHandler());
		//System.out.println(objects);
		}catch(ParserConfigurationException e){
			throw new RuntimeException(e);
		}catch(SAXException e){
			throw new RuntimeException(e);
		}
		return objects;
	}


}
