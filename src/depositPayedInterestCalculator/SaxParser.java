package depositPayedInterestCalculator;

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
import java.util.LinkedList;
import java.util.List;


public class SaxParser {

    InputStream xmlInput;
    List<Deposit> depositObjects;

    SaxParser(String fileName) throws FileNotFoundException {

        xmlInput = new FileInputStream(fileName);
    }


    private DefaultHandler saxHandler() {


        return new DefaultHandler() {
            class DepositElementInformation {
                String depositTyp;
                String customerNumber;
                String depositBalance;
                int durationInDay;
            }

            boolean isDepositType = false;
            boolean isCustomerNumber = false;
            boolean isDepositBalance = false;
            boolean isDurationInDay = false;
            DepositElementInformation tempDepositElementInf = new DepositElementInformation();

            {
                depositObjects = new LinkedList<Deposit>();

            }


            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

                if (qName.equals("customerNumber")) {
                    isCustomerNumber = true;
                } else if (qName.equals("depositType")) {
                    isDepositType = true;
                } else if (qName.equals("depositBalance")) {
                    isDepositBalance = true;
                } else if (qName.equals("durationInDays")) {
                    isDurationInDay = true;
                }

            }

            public void endElement(String uri, String localName, String qName) throws SAXException {

                if ("deposit".equals(qName)) {

                    try {
                        depositObjects.add(new Deposit(tempDepositElementInf.customerNumber, tempDepositElementInf.depositBalance, tempDepositElementInf.durationInDay, tempDepositElementInf.depositTyp));

                    } catch (WrongInputException e) {
                        System.err.println(e.getMessage());
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }


            public void characters(char ch[], int start, int length) throws SAXException {
                String value = new String(ch, start, length).trim();
                if (value.length() == 0) return;
                if (isCustomerNumber) {
                    tempDepositElementInf.customerNumber = value;
                    isCustomerNumber = false;
                } else if (isDepositType) {
                    tempDepositElementInf.depositTyp = value;
                    isDepositType = false;
                } else if (isDepositBalance) {
                    tempDepositElementInf.depositBalance = value;
                    isDepositBalance = false;
                } else if (isDurationInDay) {
                    tempDepositElementInf.durationInDay = Integer.parseInt(value);
                    isDurationInDay = false;
                }
            }


        };
    }


    List<Deposit> extractObjects() throws IOException {

        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            sp.parse(xmlInput, saxHandler());

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return depositObjects;
    }


}
