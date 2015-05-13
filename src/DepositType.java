public abstract class DepositType {

    private int interestRate;

    DepositType(int interestRate) {
        this.interestRate = interestRate;
    }

    public float getInterestRate() {
        return interestRate;
    }


}
