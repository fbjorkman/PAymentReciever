package PaymentHandlers;

import java.math.BigDecimal;

/**
 * Class that represent a payment that is to be processed by the payment method in PaymentReceiverHandler.
 */
public class Payment {
    private final BigDecimal amount;
    private final String reference;

    public Payment(BigDecimal amount, String reference){
        this.amount = amount;
        this. reference = reference;
    }

    public String getReference() {
        return reference;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}