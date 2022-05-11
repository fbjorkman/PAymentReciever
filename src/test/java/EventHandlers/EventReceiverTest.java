package EventHandlers;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class EventReceiverTest {

    @Test
    public void betalningsserviceNoExceptionTest(){
        EventReceiver eventReceiver = new EventReceiver();
        File test = new File("src/test/resources/Exempelfil_betalningsservice.txt");
        assertDoesNotThrow(() -> eventReceiver.processEvent(test));
    }

    @Test
    public void inbetalingstjanstenNoExceptionTest(){
        EventReceiver eventReceiver = new EventReceiver();
        File test = new File("src/test/resources/Exempelfil_inbetalningstjansten.txt");
        assertDoesNotThrow(() -> eventReceiver.processEvent(test));
    }

    @Test
    public void unsupportedFileTypeExceptionTest(){
        EventReceiver eventReceiver = new EventReceiver();
        File test = new File("src/test/resources/Example_DoesNotExist.txt");
        RuntimeException exception = assertThrows(RuntimeException.class, () -> eventReceiver.processEvent(test));
        String expectedErrorMessage = "Exceptions.UnsupportedFileTypeException: The filetype received is not supported: DoesNotExist.txt";
        assertEquals(expectedErrorMessage, exception.getMessage());
    }
}