package FileTypeHandlers;

import Exceptions.InvalidFormatException;
import PaymentHandlers.Payment;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Object to store information about a Betalningsservice.
 */
public class BetalningsserviceObject {
    private String accountNumber;
    private BigDecimal sum;
    private int numTransactions;
    private Date date;
    private String currency;
    private List<Payment> validPayments;

    public BetalningsserviceObject(String openingPost){
        try{
            parseOpeningPost(openingPost);
        } catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        }
        validPayments = new ArrayList<>();
    }

    /**
     * Parses the first line in the file and checks that it is a valid opening post. It then extracts the information
     * from the string and parses the values.
     * @param openingPost The first line of the file in string format.
     * @throws InvalidFormatException A custom exception that is thrown if the post is not in the specified format.
     */
    private void parseOpeningPost(String openingPost) throws InvalidFormatException {
        if(openingPost.charAt(0) != 'O'){
            throw new InvalidFormatException("The opening post must begin with 'O'");
        }
        this.accountNumber = setAccountNumber(openingPost.substring(1, 16));
        this.sum = setSum(openingPost.substring(16, 30));
        this.numTransactions = setNumTransactions(openingPost.substring(30, 40));
        this.date = setDate(openingPost.substring(40, 48));
        this.currency = setCurrency(openingPost.substring(48, 51));
    }

    /**
     * Checks if the account number is in a valid format.
     * @param accountNumber The account number in string format.
     * @return The account number if valid.
     */
    private String setAccountNumber(String accountNumber) {
        String[] clearingAndAccountNum = accountNumber.split(" ");
        try{
            if(clearingAndAccountNum.length > 2){
                throw new InvalidFormatException("The account number can only contain a clearing number and an account number separated by an empty space.");
            }
            for(String num : clearingAndAccountNum){
                Long.parseLong(num);
            }
        } catch(NumberFormatException e){
            throw new RuntimeException("The account number did not only contain numbers.", e);
        } catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        }
        return accountNumber;
    }

    /**
     * Parses and sets the sum.
     * @param sumString The sum in string format.
     * @return The sum as a double.
     */
    private BigDecimal setSum(String sumString) {
        try{
            return new BigDecimal(sumString.replace(",", ".").strip());
        } catch (NumberFormatException e) {
            throw new RuntimeException("The sum was in an invalid format.", e);
        }
    }

    /**
     * Parses and sets the number of transactions.
     * @param numTransactions The number of transactions in string format.
     * @return The number of transactions as an integer.
     */
    private int setNumTransactions(String numTransactions) {
        try{
            return Integer.parseInt(numTransactions.strip());
        } catch(NumberFormatException e) {
            throw new RuntimeException("The number of transactions was in an invalid format.", e);
        }
    }

    /**
     * Reads and sets the date from a string. The date is on the format YYYYMMDD
     * @param dateString The date in string format
     * @return A Date object.
     */
    private Date setDate(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        try {
            return formatter.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException("Failed to parse the date.", e);
        }
    }
    private String setCurrency(String currency) {
        // TODO: Could add a check to here to validate that the currency is a supported currency.
        return currency;
    }

    public void addPayment(Payment payment){
        validPayments.add(payment);
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public int getNumTransactions() {
        return numTransactions;
    }

    public Date getDate() {
        return date;
    }

    public String getCurrency() {
        return currency;
    }

    public List<Payment> getValidPayments(){
        return validPayments;
    }
}