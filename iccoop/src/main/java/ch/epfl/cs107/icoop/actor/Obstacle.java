package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

/**
 * Represents a static obstacle that blocks movement and can interact with other entities.
 */
public class Obstacle extends AreaEntity implements Interactable {

    /**
     * The sprite representing the obstacle visually.
     */
    private final Sprite sprite;

    /**
     * Constructs an Obstacle entity with the specified parameters.
     *
     * @param area        (Area): The area to which the obstacle belongs. Not null.
     * @param orientation (Orientation): The initial orientation of the obstacle. Not null.
     * @param position    (DiscreteCoordinates): The initial position of the obstacle in the area. Not null.
     */
    public Obstacle(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);

        this.sprite = new Sprite("rock.2", 1, 1, this);
    }

    /**
     * Draws the obstacle's sprite on the canvas.
     *
     * @param canvas (Canvas): The canvas to draw on.
     */
    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }

    /**
     * Retrieves the current cell occupied by the obstacle.
     *
     * @return (List<DiscreteCoordinates>): A singleton list containing the obstacle's main cell coordinates.
     */
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    /**
     * Indicates whether the obstacle occupies cell space.
     *
     * @return (boolean): Always true, as obstacles block movement.
     */
    @Override
    public boolean takeCellSpace() {
        return true;
    }

    /**
     * Indicates whether the obstacle is interactable at the cell level.
     *
     * @return (boolean): Always true, as the obstacle interacts with entities at the cell level.
     */
    @Override
    public boolean isCellInteractable() {
        return true;
    }

    /**
     * Indicates whether the obstacle is interactable at the view level.
     *
     * @return (boolean): Always true, allowing interactions from a distance.
     */
    @Override
    public boolean isViewInteractable() {
        return true;
    }

    /**
     * Accepts interaction from a visitor.
     *
     * @param v                (AreaInteractionVisitor): The visitor handling the interaction.
     * @param isCellInteraction (boolean): Indicates whether the interaction is at the cell level.
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }
}
