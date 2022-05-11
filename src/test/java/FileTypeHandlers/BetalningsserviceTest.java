package FileTypeHandlers;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class BetalningsserviceTest {

    @Test
    public void betalningsserviceNoExceptionTest(){
        File test = new File("src/test/resources/Exempelfil_betalningsservice.txt");
        assertDoesNotThrow(() -> new Betalningsservice(test));
    }

    @Test
    public void emptyFileException(){
        File test = new File("src/test/resources/Empty_betalningsservice.txt");
        RuntimeException exception = assertThrows(RuntimeException.class, () -> new Betalningsservice(test));
        String expectedErrorMessage = "Exceptions.EmptyFileException: The file was empty and could not be red.";
        assertEquals(expectedErrorMessage, exception.getMessage());
    }

    @Test
    public void invalidFormatException(){
        File test = new File("src/test/resources/InvalidFormat_betalningsservice.txt");
        RuntimeException exception = assertThrows(RuntimeException.class, () -> new Betalningsservice(test));
        String expectedErrorMessage = "Exceptions.InvalidFormatException: The opening post must be of fixed size 51 characters";
        assertEquals(expectedErrorMessage, exception.getMessage());
    }

}