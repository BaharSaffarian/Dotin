package depositPayedInterestCalculator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;


public class Deposit implements Comparable {

    final static int scale = 5;
    private String customerNumber;
    private BigDecimal depositBalance;
    private int durationInDay;
    private DepositType depositType;
    private BigDecimal payedInterest;

    public Deposit(String customerNo, String balance, int duration, String depositTypeName) throws WrongInputException {
        setCustomerNumber(customerNo);
        setDepositBalance(balance);
        setDurationInDay(duration);
        setDepositType(depositTypeName);
        calculatePayedInterest();
    }

    public void calculatePayedInterest() {
        BigDecimal balance = new BigDecimal(getDepositBalance());
        BigDecimal payedInterest = balance.multiply(new BigDecimal(getDurationInDay())).multiply(new BigDecimal(depositType.getInterestRate())).divide(new BigDecimal(36500), MathContext.DECIMAL128);
        setPayedInterest(payedInterest.toString());
    }

    protected void setCustomerNumber(String customerNo) {
        customerNumber = customerNo;
    }

    protected String getCustomerNumber() {
        return customerNumber;
    }

    protected void setDepositBalance(String balance) throws WrongInputException {
        BigDecimal bigBalance = new BigDecimal(balance);
        if (bigBalance.compareTo(new BigDecimal(0)) == -1) {
            throw new WrongInputException("Deposit balance value entered for for customer No : '" + getCustomerNumber() + "' is negative ");
        }
        depositBalance = bigBalance;
    }

    protected String getDepositBalance() {
        return depositBalance.setScale(scale, RoundingMode.HALF_EVEN).toString();
    }

    protected void setPayedInterest(String interest) {
        payedInterest = new BigDecimal(interest);
    }

    public String getPayedInterest() {
        return payedInterest.setScale(scale, RoundingMode.HALF_EVEN).toString();
    }

    protected void setDurationInDay(int duration) throws WrongInputException {
        if (duration <= 0)
            throw new WrongInputException("Duration in day value entered for for customer No : '" + getCustomerNumber() + "' is not valid ");
        durationInDay = duration;
    }

    protected int getDurationInDay() {
        return durationInDay;
    }

    protected void setDepositType(String depositTypeName) throws WrongInputException {
        depositType = createDepositTypeObject(depositTypeName);

    }

    private DepositType createDepositTypeObject(String depositTypeName) throws WrongInputException {
        try {
            String depositTypeClassName = "depositPayedInterestCalculator." + depositTypeName + "DepositType";
            return (DepositType) Class.forName(depositTypeClassName).newInstance();
        } catch (ClassNotFoundException e) {
            throw new WrongInputException("Deposit Type entered for for customer No : '" + getCustomerNumber() + "' is not valid ");
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int compareTo(Object comparingObject) {

        BigDecimal myInterest = new BigDecimal(getPayedInterest());
        BigDecimal comparingObjectInterest = new BigDecimal(((Deposit) comparingObject).getPayedInterest());
        return (myInterest.compareTo(comparingObjectInterest) < 0 ? 1 : (myInterest.compareTo(comparingObjectInterest) == 0 ? 0 : -1));

    }

    @Override
    public String toString() {
        return getCustomerNumber() + "#" + getPayedInterest();
    }

}
