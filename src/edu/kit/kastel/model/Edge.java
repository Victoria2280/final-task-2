package edu.kit.kastel.model;

import java.util.Objects;
/**
 * Represents an edge in a graph, consisting of a target node and a relation type.
 * @author uljyv
 * @version 1.0
 */
public class Edge {

    private final Node target;
    private final RelationType type; // через строку или enum?
    /**
     * Constructs an edge with the given target node and relation type.
     *
     * @param target The target node of the edge
     * @param type   The type of relation represented by the edge
     */
    public Edge(Node target, RelationType type) {
        this.target = target;
        this.type = type;
    }
    /**
     * Creates an inverse edge from the given source node.
     *
     * @param source The source node for the inverse edge
     * @return A new edge representing the inverse relation
     */
    public Edge createInverseEdge(Node source) {
        return new Edge(source, type.getInverseRelation());
    }
    /**
     * Gets the target node of the edge.
     *
     * @return The target node
     */
    public Node getTarget() {
        return target;
    }
    /**
     * Gets the relation type of the edge.
     *
     * @return The relation type
     */
    public RelationType getRelation() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Edge edge = (Edge) o;
        return type == edge.getRelation() && target.equals(edge.getTarget());
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, target);
    }
}