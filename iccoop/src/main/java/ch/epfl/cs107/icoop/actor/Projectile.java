package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;

import java.util.Collections;
import java.util.List;

/**
 * Abstract class representing a projectile that moves in a straight direction and interacts with entities in its path.
 */
public abstract class Projectile extends MovableAreaEntity implements Interactor, Unstoppable {

    /** The duration of each movement step. */
    private final static int MOVE_DURATION = 10;

    /** The speed of the projectile. */
    private final int speed;

    /** The remaining range of the projectile before it gets destroyed. */
    private int range;

    /**
     * Constructs a Projectile entity.
     *
     * @param area        (Area): The area to which the projectile belongs. Not null.
     * @param orientation (Orientation): The initial orientation of the projectile. Not null.
     * @param position    (DiscreteCoordinates): The initial position of the projectile in the area. Not null.
     * @param speed       (int): The speed of the projectile. Not null.
     * @param range       (int): The total range of the projectile before it gets destroyed. Not null.
     */
    public Projectile(Area area, Orientation orientation, DiscreteCoordinates position, int speed, int range) {
        super(area, orientation, position);

        this.speed = speed;
        this.range = range;
    }

    /**
     * Updates the projectile's position and checks if it should be destroyed.
     *
     * @param deltaTime (float): The time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // Move the projectile if it is not currently in motion.
        if (!isDisplacementOccurs()) {
            move(MOVE_DURATION / speed);
            range--;
        }

        // Destroy the projectile if it has traveled beyond its range.
        if (range < 0) {
            destroy();
        }
    }

    /**
     * Destroys the projectile by unregistering it from the area.
     */
    public void destroy() {
        unregister();
    }

    /**
     * Unregisters the projectile from the current area.
     */
    private void unregister() {
        getOwnerArea().unregisterActor(this);
    }

    /**
     * Retrieves the current cells occupied by the projectile.
     *
     * @return (List<DiscreteCoordinates>): A singleton list containing the projectile's main cell coordinates.
     */
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    /**
     * Retrieves the field of view cells for the projectile.
     *
     * @return (List<DiscreteCoordinates>): An empty list, as projectiles have no field of view.
     */
    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return List.of();
    }

    /**
     * Indicates whether the projectile wants cell-level interactions.
     *
     * @return (boolean): Always true, as projectiles interact with entities in their path.
     */
    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    /**
     * Indicates whether the projectile wants view-level interactions.
     *
     * @return (boolean): Always false, as projectiles do not interact based on view.
     */
    @Override
    public boolean wantsViewInteraction() {
        return false;
    }

    /**
     * Indicates whether the projectile occupies cell space.
     *
     * @return (boolean): Always false, as projectiles do not block movement.
     */
    @Override
    public boolean takeCellSpace() {
        return false;
    }

    /**
     * Indicates whether the projectile is interactable at the cell level.
     *
     * @return (boolean): Always true, as entities can interact with it.
     */
    @Override
    public boolean isCellInteractable() {
        return true;
    }

    /**
     * Indicates whether the projectile is interactable at the view level.
     *
     * @return (boolean): Always false, as projectiles are only interactable at the cell level.
     */
    @Override
    public boolean isViewInteractable() {
        return false;
    }

    /**
     * Handles interactions with the projectile.
     *
     * @param v                (AreaInteractionVisitor): The visitor managing the interaction.
     * @param isCellInteraction (boolean): True if the interaction is at the cell level.
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }
}
