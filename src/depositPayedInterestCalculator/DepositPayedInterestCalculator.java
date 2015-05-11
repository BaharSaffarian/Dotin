package depositPayedInterestCalculator;

import java.io.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class DepositPayedInterestCalculator {

    static void descendingSort(List<Deposit> depositList) {
        Object[] depositArray = depositList.toArray();
        Arrays.sort(depositArray);
        depositList.clear();
        for (Object aDeposit : depositArray) {
            depositList.add((Deposit) aDeposit);
        }
    }


    public static void main(String[] args) throws IOException {

        SaxParser saxParser;
        saxParser = new SaxParser("xmlFile.xml");
        List<Deposit> objectsList = saxParser.extractObjects();
        descendingSort(objectsList);
        Iterator it = objectsList.iterator();

        RandomAccessFile rf = new RandomAccessFile("output.txt", "rw");
        while (it.hasNext()) {
            rf.writeUTF(it.next().toString() + "\n");
        }

        rf.close();

    }

}
