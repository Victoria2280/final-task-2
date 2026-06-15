package edu.kit.kastel.util;

import edu.kit.kastel.model.Relation;

import java.util.Comparator;
/**
 * Comparator for {@link Relation} objects.
 * Compares relations based on the name of the source node,
 * then by the name of the target node, and finally by the relation type.
 * @author uljyv
 * @version 1.0
 */
public class RelationComparator implements Comparator<Relation> {

    @Override
    public int compare(Relation relation1, Relation relation2) {
        int result = relation1.source.getName().compareTo(relation2.source.getName());
        if (result != 0) {
            return result;
        }
        result = relation1.edge.getTarget().getName().compareTo(relation2.edge.getTarget().getName());
        if (result != 0) {
            return result;
        }
        return relation1.edge.getRelation().compareTo(relation2.edge.getRelation());
    }
}