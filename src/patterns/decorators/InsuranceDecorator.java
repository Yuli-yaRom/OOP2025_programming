package patterns.decorators;


import service.BillingService;

public class InsuranceDecorator extends BillingDecorator {
    private static final double INSURANCE_DISCOUNT = 0.8; // 20% discount

    public InsuranceDecorator(BillingService decoratedBillingService) {
        super(decoratedBillingService);
    }

    @Override
    public double calculateCost() {
        return super.calculateCost() * INSURANCE_DISCOUNT;
    }
}

