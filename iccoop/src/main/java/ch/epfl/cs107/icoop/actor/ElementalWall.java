package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.RPGSprite;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

/**
 * Represents an ElementalWall that can change its state based on its activity.
 * The wall has an elemental type that is active or inactive based on logic signals.
 */
public class ElementalWall extends AreaEntity implements ElementalEntity, Interactable, Interactor, Logic {

    /** Sprites for the wall, based on its orientation. */
    private final Sprite[] wallSprites;

    /** The elemental type of the wall. */
    private final ElementType elementType;

    /** The current active elemental type of the wall. */
    private ElementType currentElementType;

    /** Logic to determine if the wall is active or inactive. */
    private final Logic isActive;

    /**
     * Constructs an ElementalWall in the given area at the specified position and orientation.
     *
     * @param area        (Area): The owner area of the wall. Not null.
     * @param orientation (Orientation): The orientation of the wall. Not null.
     * @param position    (DiscreteCoordinate): The position of the wall in the area. Not null.
     * @param isActive    (Logic): The initial state of the wall, active or inactive. Not null.
     * @param spriteName  (String): The name of the sprite resource used for the wall. Not null.
     * @param elementType (ElementType): The elemental type of the wall. Not null.
     */
    public ElementalWall(
            Area area,
            Orientation orientation,
            DiscreteCoordinates position,
            Logic isActive,
            String spriteName,
            ElementType elementType
    ) {
        super(area, orientation, position);

        this.isActive = isActive;

        this.wallSprites = RPGSprite.extractSprites(
                spriteName, 4, 1, 1,
                this, Vector.ZERO, 256, 256
        );
        this.elementType = elementType;
        this.currentElementType = elementType;
    }

    /**
     * Draws the wall on the canvas if it is active.
     *
     * @param canvas (Canvas): The canvas to draw the wall on. Not null.
     */
    @Override
    public void draw(Canvas canvas) {
        if (isActive.isOn())
            wallSprites[getOrientation().ordinal()].draw(canvas);
    }

    /**
     * Updates the state of the wall based on its logic signal.
     *
     * @param deltaTime (float): The time elapsed since the last update, in seconds.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (isActive.isOn()) {
            currentElementType = elementType;
        } else {
            currentElementType = ElementType.NONE;
        }
    }

    /**
     * Retrieves the elemental type of the wall.
     *
     * @return (String): The name of the current elemental type.
     */
    @Override
    public String element() {
        return currentElementType.getName();
    }

    /**
     * Checks if the wall wants cell-level interaction.
     *
     * @return (boolean): True, as the wall can interact at the cell level.
     */
    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    /**
     * Checks if the wall wants view-level interaction.
     *
     * @return (boolean): False, as the wall does not support view interactions.
     */
    @Override
    public boolean wantsViewInteraction() {
        return false;
    }

    /**
     * Handles interaction with another interactable entity.
     *
     * @param other (Interactable): The entity interacting with the wall. Not null.
     * @param isCellInteraction (boolean): True if the interaction is at the cell level.
     */
    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
    }

    /**
     * Retrieves the current cell occupied by the wall.
     *
     * @return (List<DiscreteCoordinates>): A list containing the main cell coordinates of the wall.
     */
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    /**
     * Retrieves the field of view cells of the wall.
     *
     * @return (List<DiscreteCoordinates>): An empty list, as the wall has no field of view.
     */
    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return List.of();
    }

    /**
     * Indicates whether the wall occupies its cell space.
     *
     * @return (boolean): True, as the wall occupies cell space.
     */
    @Override
    public boolean takeCellSpace() {
        return true;
    }

    /**
     * Indicates whether the wall can interact at the cell level.
     *
     * @return (boolean): True, as the wall can interact at the cell level.
     */
    @Override
    public boolean isCellInteractable() {
        return true;
    }

    /**
     * Indicates whether the wall can interact at the view level.
     *
     * @return (boolean): True, as the wall can interact at the view level.
     */
    @Override
    public boolean isViewInteractable() {
        return true;
    }

    /**
     * Accepts an interaction from an interaction visitor.
     *
     * @param v (AreaInteractionVisitor): The visitor performing the interaction. Not null.
     * @param isCellInteraction (boolean): True if the interaction is at the cell level.
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    /**
     * Destroys the wall by unregistering it from the area.
     */
    public void destroy() {
        unregister();
    }

    /**
     * Unregisters the wall from its owner area.
     */
    private void unregister() {
        getOwnerArea().unregisterActor(this);
    }

    /**
     * Checks if the wall is currently active.
     *
     * @return (boolean): True if the wall is active, false otherwise.
     */
    @Override
    public boolean isOn() {
        return isActive.isOn();
    }

    /**
     * Checks if the wall is currently inactive.
     *
     * @return (boolean): True if the wall is inactive, false otherwise.
     */
    @Override
    public boolean isOff() {
        return isActive.isOff();
    }
}
