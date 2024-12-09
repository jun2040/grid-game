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
}
