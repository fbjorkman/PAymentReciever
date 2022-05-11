package Exceptions;

public class UnsupportedFileTypeException extends Exception{
    public UnsupportedFileTypeException(String errorMessage){
        super(errorMessage);
    }
}
