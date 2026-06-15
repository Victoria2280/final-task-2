package edu.kit.kastel.model;
/**
 * Enum representing different types of relations.
 * Provides methods to retrieve inverse relations,
 * parse relation types from strings, and export relation types in different formats.
 * @author uljyv
 * @version 1.0
 */
public enum RelationType {
    /** Represents a "contains" relation. */
    CONTAINS,
    /** Represents a "contained-in" relation. */
    CONTAINED_IN,
    /** Represents a "part-of" relation. */
    PART_OF,
    /** Represents a "has-part" relation. */
    HAS_PART,
    /** Represents a "successor-of" relation. */
    SUCCESSOR_OF,
    /** Represents a "predecessor-of" relation. */
    PREDECESSOR_OF;

    /**
     * Returns the inverse of the current relation type.
     *
     * @return the inverse relation type
     */
    public RelationType getInverseRelation() {
        return switch (this) {
            case CONTAINS -> CONTAINED_IN;
            case CONTAINED_IN -> CONTAINS;
            case PART_OF -> HAS_PART;
            case HAS_PART -> PART_OF;
            case SUCCESSOR_OF -> PREDECESSOR_OF;
            case PREDECESSOR_OF -> SUCCESSOR_OF;
        };
    }

    /**
     * Parses a string into a corresponding {@link RelationType}.
     *
     * @param part the string representation of the relation type
     * @return the corresponding {@link RelationType}, or null if invalid
     */
    public static RelationType parseRelationType(String part) {
        return switch (part) {
            case "contains" -> CONTAINS;
            case "contained-in" -> CONTAINED_IN;
            case "part-of" -> PART_OF;
            case "has-part" -> HAS_PART;
            case "successor-of" -> SUCCESSOR_OF;
            case "predecessor-of" -> PREDECESSOR_OF;
            default -> {
                System.out.println("Error, such relation does not exist");
                yield null;
            }
        };
    }

    /**
     * Returns the string representation of the relation type.
     *
     * @return the string representation of the relation type
     */
    public String getRelationType() {
        return switch (this) {
            case CONTAINS -> "contains";
            case CONTAINED_IN -> "contained-in";
            case PART_OF -> "part-of";
            case HAS_PART -> "has-part";
            case SUCCESSOR_OF -> "successor-of";
            case PREDECESSOR_OF -> "predecessor-of";
        };
    }

    /**
     * Returns a version of the relation type suitable for export.
     *
     * @return the exported string representation of the relation type
     */
    public String getRelationForExport() {
        return switch (this) {
            case CONTAINS -> "contains";
            case CONTAINED_IN -> "containedin";
            case PART_OF -> "partof";
            case HAS_PART -> "haspart";
            case SUCCESSOR_OF -> "successorof";
            case PREDECESSOR_OF -> "predecessorof";
        };
    }
}