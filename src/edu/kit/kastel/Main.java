package edu.kit.kastel;

import edu.kit.kastel.commands.Commands;

/**
 * The entry point of the application.
 * @author uljyv
 * @version 1.0
 */
public final class Main {

    private Main() {
        throw new UnsupportedOperationException("Utility class");
    }
    /**
     * The main method that starts the application.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        Commands.start();
    }
}