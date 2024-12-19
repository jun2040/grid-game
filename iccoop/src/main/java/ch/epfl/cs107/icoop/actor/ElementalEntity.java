package ch.epfl.cs107.icoop.actor;

/**
 * Represents an entity with an elemental property.
 * This can be used to define behaviors or interactions based on the element of an entity.
 */
public interface ElementalEntity {
    /**
     * Retrieves the elemental type of the entity.
     *
     * @return (String): The element associated with the entity (e.g., \"Fire\", \"Water\", etc.).
     */
    String element();
}
