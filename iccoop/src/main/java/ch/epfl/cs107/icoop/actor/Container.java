package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;

import java.util.Collections;
import java.util.List;

/**
 * Represents a Container, an AreaEntity that can interact with other entities
 * and define a field of view. It implements both Interactable and Interactor interfaces.
 */
public class Container extends AreaEntity implements Interactable, Interactor {

    /**
     * Default Container constructor
     *
     * @param area        (Area): The owner area. Not null.
     * @param orientation (Orientation): Initial orientation of the Container in the Area. Not null.
     * @param position    (DiscreteCoordinates): Initial position of the Container in the Area. Not null.
     */
    public Container(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
    }

    /**
     * @return A list containing the current main cell coordinates of the Container.
     */
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    /**
     * Defines the field of view of the Container.
     *
     * @return A list containing the coordinates of the cell directly above the Container.
     */
    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates().jump(new Vector(0, -1)));
    }

    /**
     * @return False, as the Container does not require cell-level interactions.
     */
    @Override
    public boolean wantsCellInteraction() {
        return false;
    }

    /**
     * @return True, as the Container allows view-level interactions.
     */
    @Override
    public boolean wantsViewInteraction() {
        return true;
    }

    /**
     * Handles interactions with other Interactable entities.
     *
     * @param other             (Interactable): The other entity interacting with the Container.
     * @param isCellInteraction (boolean): Indicates if the interaction is at the cell level.
     */
    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        // No specific interaction behavior is defined.
    }

    /**
     * @return True, indicating the Container takes up space in its cell.
     */
    @Override
    public boolean takeCellSpace() {
        return true;
    }

    /**
     * @return False, indicating the Container cannot be interacted with at the cell level.
     */
    @Override
    public boolean isCellInteractable() {
        return false;
    }

    /**
     * @return True, indicating the Container can be interacted with at the view level.
     */
    @Override
    public boolean isViewInteractable() {
        return true;
    }

    /**
     * Accepts an interaction from a visitor.
     *
     * @param v                 (AreaInteractionVisitor): The visitor interacting with the Container.
     * @param isCellInteraction (boolean): Indicates if the interaction is at the cell level.
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        // No specific visitor behavior is defined.
    }
}
