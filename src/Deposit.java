import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;


public class Deposit implements Comparable<Deposit> {

    final static int scale = 5;
    private String customerNumber;
    private BigDecimal depositBalance;
    private int durationInDay;
    private DepositType depositType;
    private BigDecimal payedInterest;

    public Deposit(String customerNumber, BigDecimal depositBalance, int durationInDay, DepositType depositType) {
        this.customerNumber = customerNumber;
        this.depositBalance = depositBalance;
        this.durationInDay = durationInDay;
        this.depositType = depositType;
        calculatePayedInterest();
    }

    public void calculatePayedInterest() {
        payedInterest = depositBalance.multiply(new BigDecimal(durationInDay))
                                        .multiply(new BigDecimal(depositType.getInterestRate()))
                                        .divide(new BigDecimal(36500), MathContext.DECIMAL128);
    }

    BigDecimal getPayedInterest(){
        return payedInterest;
    }
    @Override
    public int compareTo(Deposit comparingObject) {

        return comparingObject.getPayedInterest().compareTo(payedInterest);

    }

    @Override
    public String toString() {
        return customerNumber + "#" + payedInterest.setScale(scale, RoundingMode.HALF_EVEN).toString();
    }

}
