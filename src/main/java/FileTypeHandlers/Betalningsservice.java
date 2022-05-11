package FileTypeHandlers;

import Exceptions.EmptyFileException;
import Exceptions.InvalidFormatException;
import PaymentHandlers.Payment;
import PaymentHandlers.PaymentReceiverHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that handles the files of type "betalningsservice.txt"
 */
public class Betalningsservice {
    BufferedReader fileReader;
    String openingPost;
    List<String> payments;
    BetalningsserviceObject betalObj;

    public Betalningsservice(File file){
        splitOpeningPostAndPayments(file);
        betalObj = new BetalningsserviceObject(openingPost);
        validatePayments();
        processPayments();
    }

    /**
     * Method that splits the opening post and the payments from the input file.
     * @param file The input file in File format.
     */
    private void splitOpeningPostAndPayments(File file){
        String line;
        payments = new ArrayList<>();
        try {
            fileReader = new BufferedReader(new FileReader(file));
            openingPost = fileReader.readLine();
            if(openingPost == null){
                throw new EmptyFileException("The file was empty and could not be red.");
            }

            while((line = fileReader.readLine()) != null){
                payments.add(line);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not find the file", e);
        } catch (IOException e) {
            throw new RuntimeException("I/O Exception occurred while trying to read from the file.", e);
        } catch (EmptyFileException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method checks that the payments are in a correct format and adds the valid payments to the
     * BetalningsserviceObject.
     */
    private void validatePayments() {
        try {
            for (String paymentLine : payments) {
                BigDecimal amount;
                String reference;
                if (paymentLine.charAt(0) != 'B') {
                    throw new InvalidFormatException("The payment post must begin with 'B'");
                }
                amount = new BigDecimal(paymentLine.substring(1, 15).replace(",",".").strip());
                reference = paymentLine.substring(15);
                betalObj.addPayment(new Payment(amount, reference));
            }
            if(betalObj.getValidPayments().size() != betalObj.getNumTransactions()){
                throw new InvalidFormatException("The number of payments: " + betalObj.getValidPayments().size() +
                        " does not corresponds to the specified number of payments in the opening post: " + betalObj.getNumTransactions());
            }
        } catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        } catch(NumberFormatException e){
            throw new RuntimeException("The payment amount could not be parsed", e);
        }
    }

    /**
     * Processing payments and makes calls to the PaymentReceiverHandler to execute the payments.
     */
    private void processPayments() {
        PaymentReceiverHandler paymentHandler = new PaymentReceiverHandler();
        paymentHandler.startPaymentBundle(betalObj.getAccountNumber(), betalObj.getDate(), betalObj.getCurrency());
        for(Payment p : betalObj.getValidPayments()){
            paymentHandler.payment(p.getAmount(), p.getReference());
        }
        paymentHandler.endPaymentBundle();
    }
}
