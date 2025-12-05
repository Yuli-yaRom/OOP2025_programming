package Main.service;


public class CreditCardPayment implements PaymentStrategy {
    private String cardNumber;

    public CreditCardPayment(String cardNumber) {

        this.cardNumber = cardNumber;
    }

    @Override
    public String pay(double amount) {

        return "Paid " + amount + " using credit card " + cardNumber;
    }
}

