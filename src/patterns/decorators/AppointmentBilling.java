package patterns.decorators;

import service.BillingService;

public class AppointmentBilling implements BillingService {
    private double cost;

    public AppointmentBilling(double cost) {
        this.cost = cost;
    }

    @Override
    public double calculateCost() {
        return cost;
    }
}

