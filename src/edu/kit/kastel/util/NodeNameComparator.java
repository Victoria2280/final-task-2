package edu.kit.kastel.util;

import edu.kit.kastel.model.Node;

import java.util.Comparator;
/**
 * Comparator for sorting Nodes by their names in lexicographical order.
 * @author uljyv
 * @version 1.0
 */
public class NodeNameComparator implements Comparator<Node> {

    @Override
    public int compare(Node node1, Node node2) {

        return node1.getName().compareTo(node2.getName());
    }
}