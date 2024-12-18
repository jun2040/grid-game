package ch.epfl.cs107.icoop.actor;

public enum ElementType {
    FIRE("feu"),
    WATER("eau"),
    NONE("")
    ;

    private final String name;
    ElementType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ElementType fromString(String name) {
        for (ElementType type : ElementType.values()) {
            if (type.name.equals(name))
                return type;
        }

        return NONE;
    }
}
