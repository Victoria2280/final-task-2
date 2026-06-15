package edu.kit.kastel.model;

/**
 * Represents a relation between a source node and an edge.
 * @author uljyv
 * @version 1.0
 */
public class Relation {

    /**
     * The source node of the relation.
     */
    public Node source;

    /**
     * The edge associated with the relation.
     */
    public Edge edge;

    /**
     * Constructs a new Relation with the specified source node and edge.
     *
     * @param source the source node of the relation
     * @param edge   the edge associated with the relation
     */
    public Relation(Node source, Edge edge) {
        this.source = source;
        this.edge = edge;
    }
}