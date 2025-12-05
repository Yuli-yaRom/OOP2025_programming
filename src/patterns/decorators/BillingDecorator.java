package patterns.decorators;


import service.BillingService;

public abstract class BillingDecorator implements BillingService {
    protected BillingService decoratedBillingService;

    public BillingDecorator(BillingService decoratedBillingService) {
        this.decoratedBillingService = decoratedBillingService;
    }

    @Override
    public double calculateCost() {
        return decoratedBillingService.calculateCost();
    }
}

