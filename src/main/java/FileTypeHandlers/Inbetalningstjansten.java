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
import java.util.Date;
import java.util.List;

/**
 * Class that handles files of type "inbetalningstjansten.txt"
 */
public class Inbetalningstjansten {
    BufferedReader fileReader;
    String openingPost;
    String endPost;
    List<String> payments;
    InbetalningstjanstenObject inbetalningsObj;

    public Inbetalningstjansten(File file){
        splitPosts(file);
        inbetalningsObj = new InbetalningstjanstenObject(openingPost, endPost);
        validatePayments();
        processPayments();
    }

    /**
     * This method extracts the opening post, the transactions and the end post. It makes sure that the files starts
     * with an opening post and ends with an end post.
     * @param file The input file in File format.
     */
    private void splitPosts(File file) {
        String line;
        payments = new ArrayList<>();
        try {
            fileReader = new BufferedReader(new FileReader(file));
            openingPost = fileReader.readLine();
            if(openingPost == null){
                throw new EmptyFileException("The file was empty and could not be red.");
            }

           if(openingPost.length() != 80){
                throw new InvalidFormatException("The opening post must be of fixed size 80 characters");
            } else if(!openingPost.startsWith("00")) {
               throw new InvalidFormatException("The opening post must start with \"00\"");
           }
            while((line = fileReader.readLine()) != null){
                if(line.startsWith("30") && line.length() >= 65) {
                    payments.add(line);
                } else if (line.startsWith("99") && line.length() == 80) {
                    endPost = line;
                    break;
                } else{
                    throw new InvalidFormatException("The format of a line was invalid.");
                }
            }
            if(endPost == null){
                throw new InvalidFormatException("The file must contain an end post.");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not find the file", e);
        } catch (IOException e) {
            throw new RuntimeException("I/O Exception occurred while trying to read from the file.", e);
        } catch (EmptyFileException | InvalidFormatException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method checks that the payments are in a correct format and adds the valid payments to the
     * InbetalningstjanstenObject.
     */
    private void validatePayments() {
        try {
            for (String paymentLine : payments) {
                BigDecimal amountNoDecimal = new BigDecimal(paymentLine.substring(2, 22));
                BigDecimal amountWithDecimal = amountNoDecimal.scaleByPowerOfTen(-2);
                String reference = paymentLine.substring(40, 65).strip();
                inbetalningsObj.addPayment(new Payment(amountWithDecimal, reference));
            }
            if(inbetalningsObj.getValidPayments().size() != inbetalningsObj.getNumTransactions()){
                throw new InvalidFormatException("The number of payments: " + inbetalningsObj.getValidPayments().size() +
                        " does not corresponds to the specified number of payments in the opening post: " + inbetalningsObj.getNumTransactions());
            }
        } catch(NumberFormatException e){
            throw new RuntimeException("The payment amount could not be parsed", e);
        } catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Processing payments and makes calls to the PaymentReceiverHandler to execute the payments.
     */
    private void processPayments() {
        PaymentReceiverHandler paymentHandler = new PaymentReceiverHandler();
        paymentHandler.startPaymentBundle(inbetalningsObj.getAccountAndClearingNumber(), new Date(), "SEK");
        for(Payment p : inbetalningsObj.getValidPayments()){
            paymentHandler.payment(p.getAmount(), p.getReference());
        }
        paymentHandler.endPaymentBundle();
    }
}