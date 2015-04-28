package deposit;

import java.io.*;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.util.*;

import javax.xml.parsers.*;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class SaxParser {
	
	InputStream xmlInput;
	List objects;
	
	SaxParser(String fileName) throws FileNotFoundException{
		
		xmlInput=new FileInputStream(fileName);		
	}
	
	private class Temp{
		String depositTyp;
		long costomerNumber;
		String depositBalance;
		long durationInDay;
	}
	
	private DefaultHandler saxHandler(){
		
		return new DefaultHandler(){
			{
				objects=new LinkedList();
			}
			private Stack<String> elementStack = new Stack<String>();
		    private Stack<Object> objectStack  = new Stack<Object>();
			
		    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		    	this.elementStack.push(qName);
		    	if("deposit".equals(qName))
		            this.objectStack.push(new Temp());
		    	
		    }

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
		    		/*System.out.print(((Temp)obj).costomerNumber);
		    		System.out.print(" , "+((Temp)obj).depositTyp);
		    		System.out.print(" , "+((Temp)obj).depositBalance);
		    		System.out.println(" , "+((Temp)obj).durationInDay);*/
		    	}
		    }

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
		    
		    private Object createDepositObject(Class c,long cn, String db, long dd) throws Throwable  {
		    	try{
		    		Constructor constructor=c.getConstructor(new Class[]{long.class,String.class,long.class});
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
	public static void main(String[] args) {
		SaxParser sp=null;
		try{
			sp=new SaxParser("xmlFile.xml");
		}catch(FileNotFoundException e){
			throw new RuntimeException(e);
		}
		try{
			sp.extractObjects();
		}catch(IOException e){
			throw new RuntimeException(e);
		}

	}

}
