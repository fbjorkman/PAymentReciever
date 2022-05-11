package EventHandlers;

import Exceptions.UnsupportedFileTypeException;
import FileTypeHandlers.Betalningsservice;
import FileTypeHandlers.Inbetalningstjansten;

import java.io.File;

/**
 * The main class in the API. When a new file arrives the processEvent() will be called.
 */
public class EventReceiver {
    public void processEvent(File inputFile){
        EventType event = new EventType(inputFile);
        try {
            switch (event.getFileType()) {
                case "betalningsservice.txt" -> new Betalningsservice(inputFile);
                case "inbetalningstjansten.txt" -> new Inbetalningstjansten(inputFile);
                default -> throw new UnsupportedFileTypeException("The filetype received is not supported: "
                        + event.getFileType());
            }
        }catch(UnsupportedFileTypeException e){
            System.out.println(e);
        }
    }
}