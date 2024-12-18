package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.play.areagame.handler.InventoryItem;

public enum ICoopItem implements InventoryItem {
    BOMB("bomb", "explosive", true),
    SWORD("sword", "sword.icon", false),
    // FIXME: Handle staff icon for different element
    STAFF_WATER("staff", "staff_water.icon", false),
    STAFF_FIRE("staff", "staff_fire.icon", false)
    ;

    private final String name;
    private final int pocketId;
    private final String spriteName;
    private boolean consumable;
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

    public String getSpriteName() {
        return "icoop/" + spriteName;
    }

    public boolean isConsumable() {
        return consumable;
    }

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
