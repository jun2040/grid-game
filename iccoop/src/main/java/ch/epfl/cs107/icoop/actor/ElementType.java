package ch.epfl.cs107.icoop.actor;

public enum ElementType {
    FIRE("feu"),
    WATER("eau"),
    NONE("");

    private final String name;

    /**
     * @param name (String) : enum element name. can be Null
     */
    ElementType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * @param name
     * @return Description : will return enum type for respective element name (String)
     */
    public static ElementType fromString(String name) {
        for (ElementType type : ElementType.values()) {
            if (type.name.equals(name))
                return type;
        }

        return NONE;
    }
}
