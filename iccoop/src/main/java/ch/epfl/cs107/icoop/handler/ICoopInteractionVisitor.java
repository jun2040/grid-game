package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.icoop.actor.*;
import ch.epfl.cs107.icoop.area.ICoopBehavior;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;

/**
 * InteractionVisitor interface for handling interactions with ICoop entities.
 */
public interface ICoopInteractionVisitor extends AreaInteractionVisitor {

    /**
     * Generic interaction method for any Interactable.
     *
     * @param other              (Interactable): The interactable entity.
     * @param isCellInteraction (boolean): True if the interaction is at the cell level.
     */
    @Override
    void interactWith(Interactable other, boolean isCellInteraction);

    /** Interaction methods for specific ICoop entities. */

    /**
     * Interacts with an ICoopPlayer.
     *
     * @param player             (ICoopPlayer): The player entity.
     * @param isCellInteraction (boolean): True if the interaction is at the cell level.
     */
    default void interactWith(ICoopPlayer player, boolean isCellInteraction) {
    }

    /**
     * Interacts with an Enemy.
     *
     * @param enemy              (Enemy): The enemy entity.
     * @param isCellInteraction (boolean): True if the interaction is at the cell level.
     */
    default void interactWith(Enemy enemy, boolean isCellInteraction) {
    }

    /**
     * Interacts with an ElementalItem.
     *
     * @param elementalItem      (ElementalItem): The elemental item entity.
     * @param isCellInteraction (boolean): True if the interaction is at the cell level.
     */
    default void interactWith(ElementalItem elementalItem, boolean isCellInteraction) {
    }

    /**
     * Interacts with an Orb.
     *
     * @param orb                (Orb): The orb entity.
     * @param isCellInteraction (boolean): True if the interaction is at the cell level.
     */
    default void interactWith(Orb orb, boolean isCellInteraction) {
    }

    /**
     * Interacts with a Staff.
     *
     * @param staff              (Staff): The staff entity.
     * @param isCellInteraction (boolean): True if the interaction is at the cell level.
     */
    default void interactWith(Staff staff, boolean isCellInteraction) {
    }

    /**
     * Interacts with a Key.
     *
     * @param key                (Key): The key entity.
     * @param isCellInteraction (boolean): True if the interaction is at the cell level.
     */
    default void interactWith(Key key, boolean isCellInteraction) {
    }

    /**
     * Interacts with a Heart.
     *
     * @param heart              (Heart): The heart entity.
     * @param isCellInteraction (boolean): True if the interaction is at the cell level.
     */
    default void interactWith(Heart heart, boolean isCellInteraction) {
    }

    /**
     * Interacts with a Coin.
     *
     * @param coin               (Coin): The coin entity.
     * @param isCellInteraction (boolean): True if the interaction is at the cell level.
     */
    default void interactWith(Coin coin, boolean isCellInteraction) {
    }

    /**
     * Interacts with an Explosive.
     *
     * @param explosive          (Explosive): The explosive entity.
     * @param isCellInteraction (boolean): True if the interaction is at the cell level.
     */
    default void interactWith(Explosive explosive, boolean isCellInteraction) {
    }

    /**
     * Interacts with a Door.
     *
     * @param door               (Door): The door entity.
     * @param isCellInteraction (boolean): True if the interaction is at the cell level.
     */
    default void interactWith(Door door, boolean isCellInteraction) {
    }

    /**
     * Interacts with an ElementalWall.
     *
     * @param elementalWall      (ElementalWall): The elemental wall entity.
     * @param isCellInteraction (boolean): True if the interaction is at the cell level.
     */
    default void interactWith(ElementalWall elementalWall, boolean isCellInteraction) {
    }

    /**
     * Interacts with a PressurePlate.
     *
     * @param pressurePlate      (PressurePlate): The pressure plate entity.
     * @param isCellInteraction (boolean): True if the interaction is at the cell level.
     */
    default void interactWith(PressurePlate pressurePlate, boolean isCellInteraction) {
    }

    /**
     * Interacts with a Chest.
     *
     * @param chest              (Chest): The chest entity.
     * @param isCellInteraction (boolean): True if the interaction is at the cell level.
     */
    default void interactWith(Chest chest, boolean isCellInteraction) {
    }

    /**
     * Interacts with a Rock.
     *
     * @param rock               (Rock): The rock entity.
     * @param isCellInteraction (boolean): True if the interaction is at the cell level.
     */
    default void interactWith(Rock rock, boolean isCellInteraction) {
    }

    /**
     * Interacts with an Obstacle.
     *
     * @param obstacle           (Obstacle): The obstacle entity.
     * @param isCellInteraction (boolean): True if the interaction is at the cell level.
     */
    default void interactWith(Obstacle obstacle, boolean isCellInteraction) {
    }

    /**
     * Interacts with Grass.
     *
     * @param grass              (Grass): The grass entity.
     * @param isCellInteraction (boolean): True if the interaction is at the cell level.
     */
    default void interactWith(Grass grass, boolean isCellInteraction) {
    }
}
