import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;


public class SaxParser {

    InputStream xmlInput;
    List<Deposit> depositObjects;

    SaxParser(String fileName) throws FileNotFoundException {

        xmlInput = new FileInputStream(fileName);
    }

    class SaxHandler extends DefaultHandler {
        String depositTypeName;
        String customerNumber;
        String depositBalance;
        int durationInDay;

        boolean isDepositType = false;
        boolean isCustomerNumber = false;
        boolean isDepositBalance = false;
        boolean isDurationInDay = false;

        SaxHandler() {
            depositObjects = new LinkedList<Deposit>();
        }

        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

            if ("customerNumber".equals(qName)) {
                isCustomerNumber = true;
            } else if ("depositType".equals(qName)) {
                isDepositType = true;
            } else if ("depositBalance".equals(qName)) {
                isDepositBalance = true;
            } else if ("durationInDays".equals(qName)) {
                isDurationInDay = true;
            }

        }

        public void endElement(String uri, String localName, String qName) throws SAXException {
            if ("deposit".equals(qName)) {
                try {
                    BigDecimal bigBalance = new BigDecimal(depositBalance);
                    if (bigBalance.compareTo(new BigDecimal(0)) == -1) {
                        throw new WrongInputException("Deposit balance value entered for for customer No : '" + customerNumber + "' is negative ");
                    }
                    if (durationInDay <= 0)
                        throw new WrongInputException("Duration in day value entered for for customer No : '" + customerNumber + "' is not valid ");
                    DepositType depositType=createDepositTypeObject(depositTypeName);
                    depositObjects.add(new Deposit(customerNumber, bigBalance, durationInDay, depositType));
                } catch (WrongInputException e) {
                    System.err.println(e.getMessage());
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }

        private DepositType createDepositTypeObject(String depositTypeName) throws WrongInputException {
            try {
                String depositTypeClassName = depositTypeName + "DepositType";
                return (DepositType) Class.forName(depositTypeClassName).newInstance();
            } catch (ClassNotFoundException e) {
                throw new WrongInputException("Deposit Type entered for for customer No : '" + customerNumber + "' is not valid ");
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return null;
        }

        public void characters(char ch[], int start, int length) throws SAXException {
            String value = new String(ch, start, length).trim();
            if (value.length() == 0) return;
            if (isCustomerNumber) {
                customerNumber = value;
                isCustomerNumber = false;
            } else if (isDepositType) {
                depositTypeName = value;
                isDepositType = false;
            } else if (isDepositBalance) {
                depositBalance = value;
                isDepositBalance = false;
            } else if (isDurationInDay) {
                durationInDay = Integer.parseInt(value);
                isDurationInDay = false;
            }
        }
    }

    List<Deposit> extractObjects() throws IOException {

        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            sp.parse(xmlInput, new SaxHandler());

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return depositObjects;
    }


}
