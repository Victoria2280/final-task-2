package edu.kit.kastel.model;

import edu.kit.kastel.commands.TermException;
import edu.kit.kastel.util.NodeNameComparator;
import edu.kit.kastel.util.RelationComparator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
/**
 * Represents a graph consisting of nodes and directed edges with relation types.
 * @author uljyv
 * @version 1.0
 */
public class Graph {

    private static final String ERROR_INVALID_RELATION = "Error, wrong relation.";
    private static final String ERROR_SELF_RELATION = "Error, self relation.";
    private static final String ERROR_NODE_EXISTS_ID = "Error, node with such id already exists.";
    private static final String ERROR_NODE_EXISTS_NAME = "Error, node with such name already exists.";
    private static final String ERROR_EDGE_EXISTS = "Error, such edge already exists.";
    private static final String ERROR_NODE_NOT_FOUND = "Error, source or target node not found in the graph.";
    private static final String ERROR_PRODUCT_NOT_FOUND = "product not found";
    private static final String ERROR_WRONG_NUMBER = "wrong number.";
    private final Map<Node, Set<Edge>> graph;
    private final Map<Integer, Node>  idToNode;
    /**
     * Constructs an empty graph.
     */
    public Graph() {
        this.graph = new HashMap<>();
        this.idToNode = new HashMap<>();
    }
    /**
     * Prints all edges in the graph in a formatted output.
     */
    public void printEdges() {
        List<Relation> relations = new ArrayList<>();
        for (Map.Entry<Node, Set<Edge>> entry : graph.entrySet()) {
            for (Edge edge : entry.getValue()) {
                relations.add(new Relation(entry.getKey(), edge));
            }
        }
        relations.sort(new RelationComparator());

        for (Relation relation : relations) {
            Node source = relation.source;
            Node target = relation.edge.getTarget();
            RelationType type = relation.edge.getRelation();
            System.out.println(source.getNameForEdges() + "-[" + type.getRelationType() + "]->" + target.getNameForEdges());
        }
    }
    /**
     * Prints the graph in DOT format for visualization.
     */
    public void printExport() {
        List<Relation> relations = new ArrayList<>();
        for (Map.Entry<Node, Set<Edge>> entry : graph.entrySet()) {
            for (Edge edge : entry.getValue()) {
                relations.add(new Relation(entry.getKey(), edge));
            }
        }
        relations.sort(new RelationComparator());

        System.out.println("digraph {");
        for (Relation relation : relations) {
            String sourceName = relation.source.getName();
            String relationName = relation.edge.getRelation().getRelationForExport();
            String targetName = relation.edge.getTarget().getName();
            System.out.println(sourceName + " -> " + targetName + " [label=" + relationName + "]");
        }

        List<Node> nodes = new ArrayList<>();
        for (Node node : graph.keySet()) {
            if (node.isCategory()) {
                nodes.add(node);
            }
        }
        nodes.sort(new NodeNameComparator());

        for (Node node: nodes) {
            System.out.println(node.getName() + " [shape=box]");
        }
        System.out.println("}");
    }
    /**
     * Prints all nodes in the graph.
     */
    public void printNodes() {
        List<Node> nodes = new ArrayList<>();
        for (Node node : graph.keySet()) {
            nodes.add(node);
        }
        nodes.sort(new NodeNameComparator());

        boolean isFirst = true;
        for (Node node : nodes) {
            if (!isFirst) {
                System.out.print(" ");
            } else {
                isFirst = false;
            }
            if (!node.isCategory()) {
                System.out.print(node.getName() + ":" + node.getId());
            } else {
                System.out.print(node.getName());
            }
        }
        System.out.println();
    }

    private boolean addNode(Node node) {

        if (!node.isCategory()) {
            Node existingNode = idToNode.get(node.getId());
            if (existingNode != null && !existingNode.equals(node)) {
                System.out.println(ERROR_NODE_EXISTS_ID);
                return false;
            }
            for (Map.Entry<Node, Set<Edge>> entry : graph.entrySet()) {
                existingNode = entry.getKey();
                if (existingNode.equals(node)) {
                    if (existingNode.isCategory() | (!existingNode.isCategory() && existingNode.getId() != node.getId())) {
                        System.out.println(ERROR_NODE_EXISTS_NAME);
                        return false;
                    }
                    return true;
                }
            }
        }
        graph.putIfAbsent(node, new HashSet<Edge>());

        if (!node.isCategory()) {
            idToNode.putIfAbsent(node.getId(), node);
        }
        return true;
    }

    private static boolean isCorrectRelation(Node subject, RelationType type, Node object) {

        if (!subject.isCategory() && type.equals(RelationType.CONTAINS)) {
            System.out.println(ERROR_INVALID_RELATION);
            return false;
        }

        if (!object.isCategory() && type.equals(RelationType.CONTAINED_IN)) {
            System.out.println(ERROR_INVALID_RELATION);
            return false;
        }

        if ((subject.isCategory() || object.isCategory()) && (type.equals(RelationType.PART_OF) || type.equals(RelationType.HAS_PART))) {
            System.out.println(ERROR_INVALID_RELATION);
            return false;
        }

        if ((subject.isCategory() || object.isCategory()) && (type.equals(RelationType.SUCCESSOR_OF)
                || type.equals(RelationType.PREDECESSOR_OF))) {
            System.out.println(ERROR_INVALID_RELATION);
            return false;
        }
        return true;
    }
    /**
     * Adds an edge between two nodes with a specified relation type.
     *
     * @param source The source node
     * @param target The target node
     * @param type   The type of relation
     * @return true if edge was added, otherwise false
     */
    public boolean addEdge(Node source, Node target, RelationType type) {

        if (source == null) {
            return false;
        }

        if (target == null) {
            return false;
        }

        if (type == null) {
            return false;
        }

        if (source.equals(target)) {
            System.out.println(ERROR_SELF_RELATION);
            return false;
        }

        if (!isCorrectRelation(source, type, target)) {
            return false;
        }

        if (!addNode(source) || !addNode(target)) {
            return false;
        }

        Set<Edge> sourceEdges = graph.get(source);

        Edge edge = new Edge(target, type);

        if (sourceEdges.contains(edge)) {
            System.out.println(ERROR_EDGE_EXISTS);
            return false;
        }

        graph.get(source).add(edge);
        graph.get(target).add(edge.createInverseEdge(source));

        return true;
    }
    /**
     * Removes an edge between two nodes with a specified relation type.
     *
     * @param source The source node
     * @param target The target node
     * @param type   The type of relation
     */
    public void removeEdge(Node source, Node target, RelationType type) {
        if (!graph.containsKey(source) || !graph.containsKey(target)) {
            System.out.println(ERROR_NODE_NOT_FOUND);
            return;
        }

        Edge edge = new Edge(target, type);

        Set<Edge> sourceEdges = graph.get(source);

        if (!sourceEdges.remove(edge)) {
            return;
        }

        Set<Edge> targetEdges = graph.get(target);

        edge = edge.createInverseEdge(source);
        if (!targetEdges.remove(edge)) {
            return;
        }

        if (sourceEdges.isEmpty()) {
            graph.remove(source);

            if (!source.isCategory()) {
                idToNode.remove(source.getId());
            }
        }

        if (targetEdges.isEmpty()) {
            graph.remove(target);

            if (!target.isCategory()) {
                idToNode.remove(target.getId());
            }
        }
    }

    private Node findNodeById(String productId) throws TermException {
        int id = 0;
        try {
            id = Integer.parseInt(productId.trim());
        } catch (NumberFormatException nfe) {
            throw new TermException(ERROR_WRONG_NUMBER);
        }

        return idToNode.getOrDefault(id, null);
    }
    /**
     * Finds sibling nodes of a product node.
     *
     * @param productId The product ID
     * @return A set of sibling nodes
     * @throws TermException If the product is invalid
     */
    public Set<Node> findSibling(String productId) throws TermException {

        Node product = findNodeById(productId);

        if (product == null || product.isCategory()) {
            throw new TermException(ERROR_PRODUCT_NOT_FOUND);
        }

        Set<Node> result = new HashSet<>();
        for (Edge edge : graph.get(product)) {
            if (edge.getRelation().equals(RelationType.CONTAINED_IN)) {
                Node category = edge.getTarget();

                for (Edge categoryEdge : graph.get(category)) {
                    if (categoryEdge.getRelation().equals(RelationType.CONTAINS) && !categoryEdge.getTarget().isCategory()) {
                        Node sibling = categoryEdge.getTarget();

                        if (sibling.getId() != product.getId()) {
                            result.add(sibling);
                        }
                    }
                }
            }
        }
        return result;
    }
    /**
     * Recommends nodes based on successors (S2 recommendation).
     *
     * @param productId The ID of the product to get recommendations for
     * @return Set of recommended successor nodes
     * @throws TermException if the product ID is invalid or not found, or if the node is a category
     */
    public Set<Node> recommendS2(String productId) throws TermException {

        Node product = findNodeById(productId);

        if (product == null || product.isCategory()) {
            throw new TermException(ERROR_PRODUCT_NOT_FOUND);
        }
        Set<Node> recommendNodes = new HashSet<>();

        findSuccessor(product, recommendNodes);
        recommendNodes.remove(product);
        return recommendNodes;
    }

    private void findSuccessor(Node node, Set<Node> recommendNode) {
        for (Edge edge : graph.get(node)) {
            if (edge.getRelation().equals(RelationType.PREDECESSOR_OF)) {
                Node successor = edge.getTarget();
                if (!recommendNode.contains(successor)) {
                    recommendNode.add(successor);
                    findSuccessor(successor, recommendNode);
                }
            }
        }
    }

    private void findPredecessor(Node node, Set<Node> recommendNode) {
        for (Edge edge : graph.get(node)) {
            if (edge.getRelation().equals(RelationType.SUCCESSOR_OF)) {
                Node predecessor = edge.getTarget();
                if (!recommendNode.contains(predecessor)) {
                    recommendNode.add(predecessor);
                    findPredecessor(predecessor, recommendNode);
                }
            }
        }
    }
    /**
     * Generates S3 recommendations - finds all predecessor products (direct and indirect).
     *
     * @param productId The ID of the product to get recommendations for
     * @return Set of recommended predecessor nodes
     * @throws TermException if the product ID is invalid or not found, or if the node is a category
     */
    public Set<Node> recommendS3(String productId) throws TermException {

        Node product = findNodeById(productId);

        if (product == null || product.isCategory()) {
            throw new TermException(ERROR_PRODUCT_NOT_FOUND);
        }

        Set<Node> recommendNodes = new HashSet<>();

        findPredecessor(product, recommendNodes);
        recommendNodes.remove(product);
        return recommendNodes;
    }
}