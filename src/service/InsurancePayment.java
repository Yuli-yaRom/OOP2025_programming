package service;

public class InsurancePayment implements PaymentStrategy {
    private String policyNumber;

    public InsurancePayment(String policyNumber) {

        this.policyNumber = policyNumber;
    }

    @Override
    public String pay(double amount) {

        return "Paid " + amount + " using insurance policy " + policyNumber;
    }
}
