package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.icoop.actor.*;
import ch.epfl.cs107.icoop.area.ICoopBehavior;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;

/**
 * InteractionVisitor for the ICoop entities
 */

public interface ICoopInteractionVisitor extends AreaInteractionVisitor {
    /// Add Interaction method with all non Abstract Interactable
    @Override
    void interactWith(Interactable other, boolean isCellInteraction);

    /**
     * @param door
     * @param isCellInteraction Description : sets default interctwith methods for each ojbect, creating encapsulation for each interaction
     */

    default void interactWith(ICoopPlayer player, boolean isCellInteraction) {
    }

    default void interactWith(Enemy enemy, boolean isCellInteraction) {
    }

    default void interactWith(ElementalItem elementalItem, boolean isCellInteraction) {
    }

    default void interactWith(Orb orb, boolean isCellInteraction) {
    }

    default void interactWith(Staff staff, boolean isCellInteraction) {
    }

    default void interactWith(Key key, boolean isCellInteraction) {
    }

    default void interactWith(Heart heart, boolean isCellInteraction) {
    }

    default void interactWith(Coin coin, boolean isCellInteraction) {
    }

    default void interactWith(Explosive explosive, boolean isCellInteraction) {
    }

    default void interactWith(Door door, boolean isCellInteraction) {
    }

    default void interactWith(ElementalWall elementalWall, boolean isCellInteraction) {
    }

    default void interactWith(PressurePlate pressurePlate, boolean isCellInteraction) {
    }

    default void interactWith(Chest chest, boolean isCellInteraction) {
    }

    default void interactWith(Rock rock, boolean isCellInteraction) {
    }

    default void interactWith(Obstacle obstacle, boolean isCellInteraction) {
    }

    default void interactWith(Grass grass, boolean isCellInteraction) {
    }
}
