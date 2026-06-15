package edu.kit.kastel.model;

import java.util.Objects;
/**
 * Represents a node in the graph. A node can be either a category or an entity with an ID.
 * @author uljyv
 * @version 1.0
 */
public class Node {

    private static final String ERROR_INVALID_NUMBER = "Error, wrong number.";
    private static final String ERROR_NEGATIVE_ID = "Error, invalid number ";
    private static final String ERROR_INVALID_NAME = "Error, invalid name ";

    private int id;
    private final String name;
    private final boolean isCategory;
    /**
     * Constructs a node with an ID and name, representing a non-category node.
     * @param id The unique identifier of the node.
     * @param name The name of the node.
     */
    public Node(int id, String name) {
        this.id = id;
        this.name = name.toLowerCase();
        this.isCategory = false;
    }
    /**
     * Constructs a category node with only a name.
     * @param name The name of the category.
     */
    public Node(String name) {
        this.name = name.toLowerCase();
        this.isCategory = true;
    }
    /**
     * Gets the name of the node.
     * @return The node's name.
     */
    public String getName() {
        return name;
    }
    /**
     * Gets the ID of the node. Returns -1 if the node is a category.
     * @return The node's ID.
     */
    public int getId() {
        return id;
    }
    /**
     * Checks if the node is a category.
     * @return True if the node is a category, false otherwise.
     */
    public boolean isCategory() {
        return isCategory;
    }
    /**
     * Gets the formatted name for edge representation.
     * @return The name formatted for edges.
     */
    public String getNameForEdges() {
        if (isCategory()) {
            return getName();
        }
        return getName() + ":" + getId();
    }
    /**
     * Parses a node from a string representation.
     * @param part The string to parse.
     * @return A Node object if parsing is successful, null otherwise.
     */
    public static Node parseNode(String part) {
        if (part.contains("(id=")) {
            int firstIdIndex = part.indexOf("=");
            int lastIdIndex = part.indexOf(")");

            int id = 0;
            try {
                id = Integer.parseInt(part.substring(firstIdIndex + 1, lastIdIndex));
            } catch (NumberFormatException nfe) {
                System.out.println(ERROR_INVALID_NUMBER);
                return  null;
            }
            if (id < 0) {
                System.out.println(ERROR_NEGATIVE_ID + id);
                return null;
            }

            int lastNameIndex = part.indexOf("(");
            String name = part.substring(0, lastNameIndex);
            if (!name.matches("[a-zA-Z0-9]+")) {
                System.out.println(ERROR_INVALID_NAME + name);
                return null;
            }

            return new Node(id, name);
        } else {
            if (!part.matches("[a-zA-Z0-9]+")) {
                System.out.println(ERROR_INVALID_NAME + part);
                return null;
            }
            return new Node(part);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Node product = (Node) o;
        return Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id);
    }
}