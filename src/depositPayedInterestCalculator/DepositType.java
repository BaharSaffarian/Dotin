package depositPayedInterestCalculator;


public abstract class DepositType {

    private float interestRate;

    DepositType(float interestRate) {
        this.interestRate = interestRate;
    }

    public float getInterestRate() {
        return interestRate;
    }


}
