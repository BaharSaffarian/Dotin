package deposit;

import java.math.BigDecimal;
import java.math.MathContext;

public class QarzDeposit extends Deposit {
	final static float interestRate=0;
	public QarzDeposit(long cn, String db, long dd)throws WrongInputException{
		super(cn,db,dd);
	}
	@Override
	public void calculatePayedInterest() {
		BigDecimal db=new BigDecimal(getDepositBalance());
		BigDecimal pi=db.multiply(new BigDecimal(getDurationInDay())).multiply(new BigDecimal(interestRate)).divide(new BigDecimal(36500),MathContext.DECIMAL128);
		setPayedInterest(pi.toString());
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
