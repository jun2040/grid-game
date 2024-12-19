package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.play.areagame.handler.InventoryItem;

/**
 * Enum representing items in the ICoop game, their properties, and functionality.
 */
public enum ICoopItem implements InventoryItem {

    /** Represents a bomb item, consumable upon use. */
    BOMB("bomb", "explosive", true),

    /** Represents a sword item, non-consumable. */
    SWORD("sword", "sword.icon", false),

    /** Represents a water staff item, non-consumable. */
    STAFF_WATER("staff", "staff_water.icon", false),

    /** Represents a fire staff item, non-consumable. */
    STAFF_FIRE("staff", "staff_fire.icon", false);

    /** The name of the item. */
    private final String name;

    /** The ID representing the item's pocket position (currently set to 0 for all). */
    private final int pocketId;

    /** The sprite name used to visually represent the item. */
    private final String spriteName;

    /** Whether the item is consumable upon use. */
    private final boolean consumable;

    /**
     * Constructs an ICoopItem with the specified properties.
     *
     * @param name       (String): Name of the item.
     * @param spriteName (String): Sprite name used for the item.
     * @param consumable (boolean): Defines whether the item disappears upon use.
     */
    ICoopItem(String name, String spriteName, boolean consumable) {
        this.name = name;
        this.pocketId = 0;
        this.spriteName = spriteName;
        this.consumable = consumable;
    }

    /**
     * Retrieves the pocket ID of the item.
     *
     * @return (int): Pocket ID.
     */
    @Override
    public int getPocketId() {
        return pocketId;
    }

    /**
     * Retrieves the name of the item.
     *
     * @return (String): Item name.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Retrieves the full sprite path for the item.
     *
     * @return (String): Sprite name including path.
     */
    public String getSpriteName() {
        return "icoop/" + spriteName;
    }

    /**
     * Determines whether the item is consumable upon use.
     *
     * @return (boolean): True if the item is consumable; false otherwise.
     */
    public boolean isConsumable() {
        return consumable;
    }

    /**
     * Retrieves the index of the specified item in the enum list.
     *
     * @param item (ICoopItem): The item whose index is to be determined.
     * @return (int): The index of the item in the enum values.
     */
    public static int getIndex(ICoopItem item) {
        int index = 0;
        for (ICoopItem curItem : values()) {
            if (curItem.equals(item)) {
                return index;
            }
            index++;
        }
        return index;
    }
}
