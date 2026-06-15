package edu.kit.kastel.parser;

import edu.kit.kastel.model.Graph;
import edu.kit.kastel.model.Node;
import edu.kit.kastel.model.RelationType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
/**
 * Utility class for reading and parsing a database file to construct a graph.
 * This class provides methods to clean input data, validate relations, and load
 * the graph structure from a file.
 * @author uljyv
 * @version 1.0
 */
public final class ReadDataBase {

    private static final String ERROR_FILE_NOT_FOUND = "Error, such file does not exist";
    private static final String ERROR_INVALID_DATABASE = "Error, wrong database";

    private ReadDataBase() {
        throw new UnsupportedOperationException("Utility class");
    }

    private static String lineReplacement(String line, String strToReplace, String strToReplaceWith) {
        String oldLine;
        String lineReplaced = line;
        do {
            oldLine = lineReplaced;
            lineReplaced = lineReplaced.replace(strToReplace, strToReplaceWith);
        } while (lineReplaced.length() < oldLine.length());
        return lineReplaced;
    }

    private static String replaceSpaces(String line) {
        String newLine = line;
        newLine = lineReplacement(newLine, "  ", " ");
        newLine = lineReplacement(newLine, " (", "(");
        newLine = lineReplacement(newLine, " )", ")");
        newLine = lineReplacement(newLine, " =", "=");
        newLine = lineReplacement(newLine, "( ", "(");
        newLine = lineReplacement(newLine, "= ", "=");
        return newLine;
    }
    /**
     * Loads a graph from a database file.
     *
     * @param fileName The name of the file containing the database
     * @return A Graph object constructed from the file data, or null if an error occurs
     */
    public static Graph loadDataBase(String fileName) {

        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(fileName));
        } catch (IOException e) {
            System.out.println(ERROR_FILE_NOT_FOUND);
            return null;
        }
        for (String line : lines) {

            System.out.println(line);
        }

        Graph graph = new Graph();

        for (String line : lines) {

            String newLine = replaceSpaces(line);

            String[] parts = newLine.trim().split(" ");

            if (parts.length != 3) {
                System.out.println(ERROR_INVALID_DATABASE);
                return null;
            }

            Node subject = Node.parseNode(parts[0]);

            Node object = Node.parseNode(parts[2]);

            RelationType type = RelationType.parseRelationType(parts[1]);

            if (!graph.addEdge(subject, object, type)) {
                return null;
            }
        }
        return graph;
    }
}