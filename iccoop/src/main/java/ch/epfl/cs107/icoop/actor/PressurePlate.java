package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.icoop.utility.event.EventArgs;
import ch.epfl.cs107.icoop.utility.event.WallActivateEvent;
import ch.epfl.cs107.icoop.utility.event.WallActivateEventArgs;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.RPGSprite;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

import static ch.epfl.cs107.play.math.Orientation.DOWN;

/**
 * Represents a Pressure Plate that can be activated to trigger linked events, such as activating an Elemental Wall.
 */
public class PressurePlate extends AreaEntity implements Logic {

    /** Indicates whether the plate is currently pressed. */
    private boolean isPressed = false;

    /** The event triggered when the pressure plate is activated or deactivated. */
    private final WallActivateEvent wallActivateEvent = new WallActivateEvent();

    /** The sprite representing the pressure plate visually. */
    private final Sprite plateSprite;

    /**
     * Constructs a PressurePlate entity.
     *
     * @param area     (Area): The area to which the pressure plate belongs. Not null.
     * @param position (DiscreteCoordinates): The initial position of the pressure plate in the area. Not null.
     */
    public PressurePlate(Area area, DiscreteCoordinates position) {
        super(area, DOWN, position);
        this.plateSprite = new RPGSprite("GroundPlateOff", 1, 1, this);
    }

    /**
     * Draws the pressure plate on the canvas.
     *
     * @param canvas (Canvas): The canvas to draw on.
     */
    @Override
    public void draw(Canvas canvas) {
        plateSprite.draw(canvas);
    }

    /**
     * Updates the pressure plate's state and triggers linked events.
     *
     * @param deltaTime (float): The time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // Emit an event to update the state of linked walls.
        if (isPressed) {
            wallActivateEvent.emit(new WallActivateEventArgs(false));
        } else {
            wallActivateEvent.emit(new WallActivateEventArgs(true));
        }

        // Reset the plate to its default inactive state.
        deactivate();
    }

    /**
     * Activates the pressure plate.
     */
    public void activate() {
        isPressed = true;
    }

    /**
     * Deactivates the pressure plate.
     */
    public void deactivate() {
        isPressed = false;
    }

    /**
     * Links an Elemental Wall to this pressure plate, allowing the plate to control the wall.
     *
     * @param elementalWall (ElementalWall): The wall to be linked to this pressure plate.
     */
    public void linkWall(ElementalWall elementalWall) {
        wallActivateEvent.addEventListener(elementalWall);
    }

    /**
     * Retrieves the current cell occupied by the pressure plate.
     *
     * @return (List<DiscreteCoordinates>): A singleton list containing the plate's main cell coordinates.
     */
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    /**
     * Indicates whether the pressure plate occupies cell space.
     *
     * @return (boolean): Always false, as entities can step on it.
     */
    @Override
    public boolean takeCellSpace() {
        return false;
    }

    /**
     * Indicates whether the pressure plate is interactable at the cell level.
     *
     * @return (boolean): Always true, as entities can interact with it.
     */
    @Override
    public boolean isCellInteractable() {
        return true;
    }

    /**
     * Indicates whether the pressure plate is interactable at the view level.
     *
     * @return (boolean): Always false, as it only interacts at the cell level.
     */
    @Override
    public boolean isViewInteractable() {
        return false;
    }

    /**
     * Handles interactions with the pressure plate.
     *
     * @param v                (AreaInteractionVisitor): The visitor managing the interaction.
     * @param isCellInteraction (boolean): True if the interaction is at the cell level.
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    /**
     * Checks if the pressure plate is currently active.
     *
     * @return (boolean): True if the plate is pressed, false otherwise.
     */
    @Override
    public boolean isOn() {
        return isPressed;
    }

    /**
     * Checks if the pressure plate is currently inactive.
     *
     * @return (boolean): True if the plate is not pressed, false otherwise.
     */
    @Override
    public boolean isOff() {
        return !isPressed;
    }
}
