package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.icoop.actor.Explosive;
import ch.epfl.cs107.icoop.actor.Rock;
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

    default void interactWith(Door door, boolean isCellInteraction) {}
    default void interactWith(Rock rock, boolean isCellInteraction) {}
    default void interactWith(Explosive explosive, boolean isCellInteraction) {}
}
