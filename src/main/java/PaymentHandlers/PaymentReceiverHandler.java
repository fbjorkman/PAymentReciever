package PaymentHandlers;

import java.math.BigDecimal;
import java.util.Date;

/**
 * This class will be the connection that interacts with the program/database that handles the payments.
 * Currently, there are just prints that confirms what is sent to the functions.
 */
public class PaymentReceiverHandler implements PaymentReceiver{

    /**
     *
     * @param accountNumber The account number where funds are deposited. This is assumed to be on format:
     *                      "CLEARINGNUMBER ACCOUNTNUMBER" (With a white space as delimiter)
     * @param paymentDate The date at which the funds were/will be deposited on the specified account.
     * @param currency The currency of the payments in the bundle.
     */
    @Override
    public void startPaymentBundle(String accountNumber, Date paymentDate, String currency) {
        System.out.println("Payment bundle opened to account: " + accountNumber + " Date: " + paymentDate + " Currency: " + currency);
    }

    @Override
    public void payment(BigDecimal amount, String reference) {
        System.out.println("Payment made with reference: " + reference + " Amount: " + amount);
    }

    @Override
    public void endPaymentBundle() {
        System.out.println("Ended payment bundle.");
    }
}
