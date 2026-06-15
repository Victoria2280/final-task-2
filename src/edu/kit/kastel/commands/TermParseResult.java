package edu.kit.kastel.commands;

import edu.kit.kastel.util.NodeNameComparator;
import edu.kit.kastel.model.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
/**
 * Represents the result of parsing a term, containing a set of nodes and a remaining string.
 * @author uljyv
 * @version 1.0
 */
public class TermParseResult {

    /** The set of parsed nodes. */
    public Set<Node> nodes;

    /** The remaining unparsed part of the string. */
    public String restString;

    /**
     * Constructs a new {@code TermParseResult} with the specified nodes and remaining string.
     *
     * @param nodes the set of parsed nodes
     * @param restString the remaining unparsed part of the input string
     */
    public TermParseResult(Set<Node> nodes, String restString) {
        this.nodes = nodes;
        this.restString = restString;
    }

    /**
     * Prints a sorted recommendation list of nodes based on their names.
     * The output format is "name:id" for each node.
     */
    public void printRecommendation() {
        List<Node> nodeList = new ArrayList<>();
        for (Node node : nodes) {
            nodeList.add(node);
        }

        boolean isFirst = true;
        nodeList.sort(new NodeNameComparator());
        for (Node node : nodeList) {
            if (!isFirst) {
                System.out.print(" ");
            } else {
                isFirst = false;
            }
            System.out.print(node.getNameForEdges());
        }
        System.out.println();
    }
}