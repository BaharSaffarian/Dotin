package deposit;

import java.math.*;

/**
 *@author Bahar Saffarian <zohreh.saffarian@gmail.com>
 * @version 1
 * @since 2015-04-21
 */
public class LongTermDeposit extends Deposit {

	/**
	 * contains the interest rate value needed to compute the deposit balance value {@link #depositBalance}
	 */
	final static float interestRate=0.2f;

	/**
	 * this object constructor
	 * Calls the super class constructor to initialize this class fields
	 * @param cn Customer Number
	 * @param db Deposit Balance
	 * @param dd Duration in Days
	 * @throws WrongInputException if the deposit balance is negative or the duration in days is zero or negative
	 */
	public LongTermDeposit(long cn, String db, long dd)throws WrongInputException{
		super(cn,db,dd);
	}

	@Override
	public void calculatePayedInterest() {
		BigDecimal db=new BigDecimal(getDepositBalance());
		BigDecimal pi=db.multiply(new BigDecimal(getDurationInDay())).multiply(new BigDecimal(interestRate)).divide(new BigDecimal(36500),MathContext.DECIMAL128);
		setPayedInterest(pi.toString());
	}

	
}
