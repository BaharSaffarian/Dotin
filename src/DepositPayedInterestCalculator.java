import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class DepositPayedInterestCalculator {

    public static void main(String[] args) throws IOException {
        SaxParser saxParser;
        saxParser = new SaxParser("xmlFile.xml");
        List<Deposit> depositList = saxParser.extractObjects();
        Collections.sort(depositList);
        Iterator it = depositList.iterator();

        RandomAccessFile rf = new RandomAccessFile("output.txt", "rw");
        while (it.hasNext()) {
            rf.writeUTF(it.next().toString() + "\n");
        }

        rf.close();

    }

}
