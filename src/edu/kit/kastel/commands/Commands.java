package edu.kit.kastel.commands;

import edu.kit.kastel.parser.ReadDataBase;
import edu.kit.kastel.model.Graph;
import edu.kit.kastel.model.Node;
import edu.kit.kastel.model.RelationType;

import java.util.Scanner;
import java.util.Set;
/**
 * Utility class containing command processing for a graph-based application.
 * This class provides methods to handle user commands and interact with the graph.
 * @author uljyv
 * @version 1.0
 */
public final class Commands {

    private static final String ERROR_WRONG_INPUT = "Error, wrong input.";
    private static final String INTERSECTION = "INTERSECTION";
    private static final String UNION = "UNION";
    private static final String S1 = "S1";
    private static final String S2 = "S2";
    private static final String S3 = "S3";
    private static final String COMMAND_LOAD = "load";
    private static final String COMMAND_QUIT = "quit";
    private static final String COMMAND_NODES = "nodes";
    private static final String COMMAND_EDGES = "edges";
    private static final String COMMAND_ADD = "add";
    private static final String COMMAND_REMOVE = "remove";
    private static final String COMMAND_RECOMMEND = "recommend";
    private static final String COMMAND_EXPORT = "export";
    private static final String ERROR_NO_DATABASE = "Error, no database";
    private static final String PREFIX_RECOMMEND = "recommend ";

    private Commands() {
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

    private static String replaceSpaces(String command) {
        String commandWithoutSpaces = command;
        commandWithoutSpaces = lineReplacement(commandWithoutSpaces, "  ", " ");
        commandWithoutSpaces = lineReplacement(commandWithoutSpaces, " (", "(");
        commandWithoutSpaces = lineReplacement(commandWithoutSpaces, "( ", "(");
        commandWithoutSpaces = lineReplacement(commandWithoutSpaces, " =", "=");
        commandWithoutSpaces = lineReplacement(commandWithoutSpaces, "= ", "=");
        commandWithoutSpaces = lineReplacement(commandWithoutSpaces, " )", ")");
        commandWithoutSpaces = lineReplacement(commandWithoutSpaces, " ,", ",");
        commandWithoutSpaces = lineReplacement(commandWithoutSpaces, ", ", ",");
        return commandWithoutSpaces;
    }

    private static TermParseResult parseTerm(String term, Graph graph) throws TermException {
        if (term.startsWith(S1) || term.startsWith(S2) || term.startsWith(S3)) {
            String startsWithId = term.substring(3);
            String[] termParts = startsWithId.split("[,)]");
            if (termParts.length == 0) {
                throw new TermException("wrong id.");
            }

            if (term.startsWith(S1)) {
                Set<Node> nodes = graph.findSibling(termParts[0]);
                return new TermParseResult(nodes, term.substring(3 + termParts[0].length()));
            }

            if (term.startsWith(S2)) {
                Set<Node> nodes = graph.recommendS2(termParts[0]);
                return new TermParseResult(nodes, term.substring(3 + termParts[0].length()));
            }

            if (term.startsWith(S3)) {
                Set<Node> nodes = graph.recommendS3(termParts[0]);
                return new TermParseResult(nodes, term.substring(3 + termParts[0].length()));
            }

        } else if (term.startsWith(INTERSECTION)) {
            TermParseResult firstTerm = parseTerm(term.substring("INTERSECTION(".length()), graph);
            TermParseResult secondTerm = parseTerm(firstTerm.restString.substring(1), graph);
            firstTerm.nodes.retainAll(secondTerm.nodes);
            return new TermParseResult(firstTerm.nodes, secondTerm.restString.substring(1));

        } else if (term.startsWith(UNION)) {
            TermParseResult firstTerm = parseTerm(term.substring("UNION(".length()), graph);
            TermParseResult secondTerm = parseTerm(firstTerm.restString.substring(1), graph);
            firstTerm.nodes.addAll(secondTerm.nodes);
            return new TermParseResult(firstTerm.nodes, secondTerm.restString.substring(1));
        }

        throw new TermException("wrong command");
    }

    /**
     * Starts the command-line interface for user interaction.
     */
    public static void start() {
        Scanner scanner = new Scanner(System.in);
        Graph graph = null;
        while (true) {
            String input = scanner.nextLine().trim();
            input = replaceSpaces(input);
            String[] parts = input.split(" ");
            if (graph == null && (!parts[0].equals(COMMAND_LOAD) && !parts[0].equals(COMMAND_QUIT))) {
                System.out.println(ERROR_NO_DATABASE);
                continue;
            }
            switch (parts[0]) {
                case COMMAND_LOAD:
                    if (parts.length != 3 || !parts[1].equals("database")) {
                        System.out.println(ERROR_WRONG_INPUT);
                        continue;
                    }
                    Graph newGraph = ReadDataBase.loadDataBase(parts[2]);
                    if (newGraph != null) {
                        graph = newGraph;
                    }
                    break;
                case COMMAND_QUIT:
                    if (parts.length != 1) {
                        System.out.println(ERROR_WRONG_INPUT);
                        continue;
                    }
                    return;
                case COMMAND_NODES:
                    nodes(parts, graph);
                    break;
                case COMMAND_EDGES:
                    edges(parts, graph);
                    break;
                case COMMAND_ADD:
                    add(parts, graph);
                    break;
                case COMMAND_REMOVE:
                    remove(parts, graph);
                    break;
                case COMMAND_RECOMMEND:
                    recommend(graph, input);
                    break;
                case COMMAND_EXPORT:
                    export(parts, graph);
                    break;
                default:
                    System.out.println(ERROR_WRONG_INPUT);
                    break;
            }
        }
    }

    private static void nodes(String[] parts, Graph graph) {
        if (parts.length != 1) {
            System.out.println(ERROR_WRONG_INPUT);
            return;
        }
        graph.printNodes();
    }

    private static void edges(String[] parts, Graph graph) {
        if (parts.length != 1) {
            System.out.println(ERROR_WRONG_INPUT);
            return;
        }
        graph.printEdges();
    }

    private static void add(String[] parts, Graph graph) {
        if (parts.length < 4) {
            System.out.println(ERROR_WRONG_INPUT);
            return;
        }
        graph.addEdge(Node.parseNode(parts[1]), Node.parseNode(parts[3]), RelationType.parseRelationType(parts[2]));
    }

    private static void remove(String[] parts, Graph graph) {
        if (parts.length < 4) {
            System.out.println(ERROR_WRONG_INPUT);
            return;
        }
        graph.removeEdge(Node.parseNode(parts[1]), Node.parseNode(parts[3]), RelationType.parseRelationType(parts[2]));
    }

    private static void recommend(Graph graph, String input) {
        if (input.length() <= PREFIX_RECOMMEND.length()) {
            System.out.println(ERROR_WRONG_INPUT);
            return;
        }
        String term = input.substring(PREFIX_RECOMMEND.length());
        try {
            TermParseResult result = parseTerm(term, graph);
            result.printRecommendation();
        } catch (TermException e) {
            System.out.println("Error, " + e.getError());
        }
    }

    private static void export(String[] parts, Graph graph) {
        if (parts.length != 1) {
            System.out.println(ERROR_WRONG_INPUT);
            return;
        }
        graph.printExport();
    }
}