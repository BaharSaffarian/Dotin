package deposit;

import java.math.BigDecimal;
import java.math.RoundingMode;
import deposit.WrongInputException;

/**
 * @author Bahar Saffarian <zohreh.saffarian@gmail.com>
 * @version 1
 * @since 2015-04-21
 */
public abstract class Deposit{
	/**
	 * Used to specify a printing format for decimal numbers
	 */
	final static int scale=5;

	/**
	 * The number of the customer who owns this deposit
	 */
	private long customerNumber;

	/**
	 * this deposit balance value
	 */
	private BigDecimal depositBalance;

	/**
	 * this deposit duration
	 */
	private long durationInDay;

	/**
	 * this deposit payed interest which is related to the type of deposit and is calculated via
	 * 'calculatePayedInterest' abstarct function (@see #calculatePayedInterest) that will be define in the derived classes
	 * this field is initialized in this class constructor by calling the implemented method of the derived class which calls this constructor
	 */
	private BigDecimal payedInterest;

	/**
	 * @constructor
	 * @param cn used to set the {@link #customerNumber} field
	 * @param db used to set the {@link #depositBalance} field
	 * @param dd used to set the {@link #durationInDay} field
	 * @throws WrongInputException when a false initialization value send to fill the field:
	 * -negative value for depositBalance field
	 * -zero or negative value for durationInDay field
	 */
	public Deposit(long cn, String db, long dd)throws WrongInputException{
		setCustomerNumber(cn);
		setDepositBalance(db);
		setDurationInDay(dd);
		calculatePayedInterest();
	}

	/**
	 * Calculates this deposit payed interest
	 * <p>
	 * The payed interest is computed via an equation which is related to three factors:
	 * This deposit balance value {@link #depositBalance}
	 * This deposit duration {@link #durationInDay}
	 * This Deposit type which is identified by the inherited class who implements this absracte method
	 * </p>
	 */
	public abstract void calculatePayedInterest(); 
	
	protected void setCustomerNumber(long cn){
		customerNumber=cn;
	}
	protected long getCustomerNumber(){
		return customerNumber;
	}

	/**
	 * set the {@link #depositBalance} field
	 * @param db is an String value which should be casted to the bigDecimal type that is the appropriate type for {@link #depositBalance}
	 * @throws WrongInputException in the case that the db argument is negative
	 */
	protected void setDepositBalance(String db)throws WrongInputException{
		BigDecimal big=new BigDecimal(db);
		if(big.compareTo(new BigDecimal(0))==-1)
			throw new WrongInputException("Deposit balance value entered for for costomer No : '"+getCustomerNumber()+"' is negative ");
			//NegativeDepositBalanceException();
		depositBalance=big;
	}

	/**
	 * gets the {@link #depositBalance} field value
	 * @return is an String type, out side of this class the type of the depositBalance field is String
	 */
	protected String getDepositBalance(){
		return depositBalance.setScale(scale, RoundingMode.HALF_EVEN).toString();
	}
	
	protected void setPayedInterest(String pi){
		payedInterest=new BigDecimal(pi);
	}
	public String getPayedInterest(){
		return payedInterest.setScale(scale, RoundingMode.HALF_EVEN).toString();
	}

	/**
	 * set the {@link #durationInDay} field
	 * @param dd
	 * @throws WrongInputException in the case that the argument is zero or negative
	 */
	protected void setDurationInDay(long dd)throws WrongInputException{
		if(dd<=0)
			throw new WrongInputException("Duration in day value entered for for costomer No : '"+getCustomerNumber()+"' is not valid ");
		durationInDay=dd;
	}
	protected long getDurationInDay(){
		return durationInDay;
	}

	@Override
	/**
	 * this method is customized for our special purpose to write specific fields in a file in a desire format
	 */
	public String toString(){
		return getCustomerNumber()+"#"+getPayedInterest();
	}

}
