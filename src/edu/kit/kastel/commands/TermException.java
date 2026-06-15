package edu.kit.kastel.commands;
/**
 * Custom exception class for handling term-related errors.
 * @author uljyv
 * @version 1.0
 */
public class TermException extends Exception {

    private final String message;
    /**
     * Constructs a new {@code TermException} with the specified error message.
     *
     * @param message the error message describing the exception
     */
    public TermException(String message) {
        this.message = message;
    }
    /**
     * Returns the error message associated with this exception.
     *
     * @return the error message
     */
    public String getError() {
        return message;
    }
}