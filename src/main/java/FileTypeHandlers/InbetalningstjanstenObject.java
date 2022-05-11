package FileTypeHandlers;

import PaymentHandlers.Payment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class InbetalningstjanstenObject {
    private int clearingNumber;
    private long accountNumber;
    private String accountAndClearingNumber;
    private BigDecimal sum;
    private int numTransactions;
    private List<Payment> validPayments;

    public InbetalningstjanstenObject(String openingPost, String endPost){
        parsePosts(openingPost, endPost);
        validPayments = new ArrayList<>();
    }

    /**
     * Parse the opening and end post and extract information from them to this object.
     * @param openingPost The opening post in string format.
     * @param endPost The end post in string format.
     */
    private void parsePosts(String openingPost, String endPost) {
        this.clearingNumber = setClearingNumber(openingPost);
        this.accountNumber = setAccountNumber(openingPost);
        this.accountAndClearingNumber = concatAccountNumber();
        this.sum = setSum(endPost);
        this.numTransactions = setNumTransactions(endPost);
    }

    /**
     * Extracts the clearing number from the opening post and make sure that it is an integer.
     * @param openingPost The opening post in string format.
     * @return The clearing number as an integer.
     */
    private int setClearingNumber(String openingPost) {
        try{
            return Integer.parseInt(openingPost.substring(10, 14));
        } catch (NumberFormatException e){
            throw new RuntimeException("The clearing number was in an invalid format.", e);
        }
    }

    /**
     * Extracts the account number from the opening post and make sure that it is of type long as it contains
     * 10 characters the size could be larger than an integer.
     * @param openingPost The opening post in string format.
     * @return The account number as a long.
     */
    private long setAccountNumber(String openingPost) {
        try{
            return Long.parseLong(openingPost.substring(14, 24));
        } catch (NumberFormatException e){
            throw new RuntimeException("The account number was in an invalid format.", e);
        }
    }

    /**
     * This method takes the clearing number and accountnumber and concatinates them into a string with
     * a white space as delimiter. (Assumption that the account number format for the PaymentReceiver
     * is "CLEARINGNUMBER ACCOUNTNUMBER").
     *
     * @return The account and clearing number as a string separated with a white space.
     */
    private String concatAccountNumber(){
        return clearingNumber + " " + accountNumber;
    }

    /**
     * Extracts the sum of all the transaction from the end post and make sure that it is in a BidDecimal format.
     * Also replaces potential commas with dots a decimal delimiter.
     * @param endPost The end post in string format.
     * @return The extracted sum in Bid decimal format.
     */
    private BigDecimal setSum(String endPost) {
        try{
            return new BigDecimal(endPost.substring(2, 22).replace(",", ".").strip());
        } catch (NumberFormatException e) {
            throw new RuntimeException("The sum was in an invalid format.", e);
        }
    }

    /**
     * Extracts the number of transactions from the end post and makes sure that it is in a valid format.
     * @param endPost The end post in string format.
     * @return The number of transaction as an integer.
     */
    private int setNumTransactions(String endPost) {
        try{
            return Integer.parseInt(endPost.substring(30, 38).strip());
        } catch(NumberFormatException e) {
            throw new RuntimeException("The number of transactions was in an invalid format.", e);
        }
    }

    public void addPayment(Payment payment){
        validPayments.add(payment);
    }

    public int getClearingNumber() {
        return clearingNumber;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public String getAccountAndClearingNumber() {
        return accountAndClearingNumber;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public int getNumTransactions() {
        return numTransactions;
    }

    public List<Payment> getValidPayments() {
        return validPayments;
    }
}

