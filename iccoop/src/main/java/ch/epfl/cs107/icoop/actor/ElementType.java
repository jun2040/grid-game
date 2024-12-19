package ch.epfl.cs107.icoop.actor;

/**
 * Enum representing different elemental types for entities or objects in the game.
 */
public enum ElementType {

    /**
     * Fire element type.
     */
    FIRE("feu"),

    /**
     * Water element type.
     */
    WATER("eau"),

    /**
     * Default element type when no specific element is assigned.
     */
    NONE("");

    /**
     * The name associated with the elemental type.
     */
    private final String name;

    /**
     * Constructs an ElementType with the given name.
     *
     * @param name (String): The name of the element. Can be null.
     */
    ElementType(String name) {
        this.name = name;
    }

    /**
     * Retrieves the name of the elemental type.
     *
     * @return (String): The name of the element.
     */
    public String getName() {
        return name;
    }

    /**
     * Finds the corresponding ElementType for a given name.
     *
     * @param name (String): The name of the element to search for.
     * @return (ElementType): The matching ElementType, or NONE if no match is found.
     */
    public static ElementType fromString(String name) {
        for (ElementType type : ElementType.values()) {
            if (type.name.equals(name)) {
                return type;
            }
        }
        return NONE;
    }
}
