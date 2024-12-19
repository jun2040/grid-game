package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.RPGSprite;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import static ch.epfl.cs107.play.math.Orientation.*;

import java.util.Collections;
import java.util.List;

/**
 * Represents a Pressure Plate that activates when pressed and deactivates when released.
 * It implements the Logic interface to provide state information.
 */
public class PressurePlate extends AreaEntity implements Logic {

    /** Indicates whether the plate is currently pressed. */
    private boolean isPressed = false;

    /** Indicates whether the plate is currently active. */
    private boolean isActive = false;

    /** Sprite representing the pressure plate. */
    private final Sprite plateSprite;

    /**
     * Constructs a PressurePlate at the specified position in the given area.
     *
     * @param area     (Area): The area to which the pressure plate belongs. Not null.
     * @param position (DiscreteCoordinate): The initial position of the pressure plate. Not null.
     */
    public PressurePlate(Area area, DiscreteCoordinates position) {
        super(area, DOWN, position);
        this.plateSprite = new RPGSprite("GroundPlateOff", 1, 1, this);
    }

    /**
     * Draws the pressure plate on the canvas.
     *
     * @param canvas (Canvas): The canvas to draw the pressure plate on. Not null.
     */
    @Override
    public void draw(Canvas canvas) {
        plateSprite.draw(canvas);
    }

    /**
     * Updates the state of the pressure plate.
     * If pressed, the plate becomes active. Otherwise, it deactivates.
     *
     * @param deltaTime (float): The time elapsed since the last update, in seconds.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        isActive = isPressed;
        depress();
    }

    /**
     * Activates the pressure plate, marking it as pressed.
     */
    public void press() {
        isPressed = true;
    }

    /**
     * Deactivates the pressure plate, marking it as not pressed.
     */
    public void depress() {
        isPressed = false;
    }

    /**
     * Retrieves the current cell occupied by the pressure plate.
     *
     * @return (List<DiscreteCoordinates>): A list containing the current main cell coordinates.
     */
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    /**
     * Indicates whether the pressure plate occupies cell space.
     *
     * @return (boolean): False, as the plate does not block other entities from occupying the cell.
     */
    @Override
    public boolean takeCellSpace() {
        return false;
    }

    /**
     * Indicates whether the pressure plate can be interacted with at the cell level.
     *
     * @return (boolean): True, as the plate can interact with entities on its cell.
     */
    @Override
    public boolean isCellInteractable() {
        return true;
    }

    /**
     * Indicates whether the pressure plate can be interacted with at the view level.
     *
     * @return (boolean): False, as the plate does not support view interactions.
     */
    @Override
    public boolean isViewInteractable() {
        return false;
    }

    /**
     * Accepts interaction from an interaction visitor.
     *
     * @param v               (AreaInteractionVisitor): The visitor performing the interaction. Not null.
     * @param isCellInteraction (boolean): True if the interaction is at the cell level, false otherwise.
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    /**
     * Checks if the pressure plate is currently active.
     *
     * @return (boolean): True if the plate is active, false otherwise.
     */
    @Override
    public boolean isOn() {
        return isActive;
    }

    /**
     * Checks if the pressure plate is currently inactive.
     *
     * @return (boolean): True if the plate is inactive, false otherwise.
     */
    @Override
    public boolean isOff() {
        return !isActive;
    }
}
