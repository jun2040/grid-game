package ch.epfl.cs107.icoop.handler;

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
}
