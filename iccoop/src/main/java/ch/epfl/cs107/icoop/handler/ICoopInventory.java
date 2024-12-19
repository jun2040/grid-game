package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.play.areagame.handler.Inventory;

/**
 * ICoopInventory serves as the core inventory system for the ICoop game.
 */
public class ICoopInventory extends Inventory {

    /**
     * Constructs an ICoopInventory with a default name.
     * This class acts as the backbone of the inventory system.
     */
    public ICoopInventory() {
        super("Inventory");
    }
}
