package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.play.areagame.handler.InventoryItem;

public enum ICoopItem implements InventoryItem {
    BOMB("bomb", "explosive", true),
    SWORD("sword", "sword.icon", false),
    STAFF_WATER("staff", "staff_water.icon", false),
    STAFF_FIRE("staff", "staff_fire.icon", false);

    private final String name;
    private final int pocketId;
    private final String spriteName;
    private final boolean consumable;

    /**
     * @param name       (String): Name of item
     * @param spriteName (String): name used for the item sprite
     * @param consumable (boolean) : defines if it disappears up when player uses it
     */
    ICoopItem(String name, String spriteName, boolean consumable) {
        this.name = name;
        this.pocketId = 0;
        this.spriteName = spriteName;
        this.consumable = consumable;
    }

    @Override
    public int getPocketId() {
        return pocketId;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * @return name used for sprite
     */
    public String getSpriteName() {
        return "icoop/" + spriteName;
    }

    /**
     * @return true if item disappears when used
     */
    public boolean isConsumable() {
        return consumable;
    }

    /**
     * @param item
     * @return returns inventory index of the current item
     */

    public static int getIndex(ICoopItem item) {
        int index = 0;
        for (ICoopItem curItem : values()) {
            if (curItem.equals(item))
                return index;

            index++;
        }

        return index;
    }
}
