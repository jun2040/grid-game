package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

/**
 * Represents a Rock entity, which acts as an obstacle in the game.
 */
public class Rock extends Obstacle {

    /** The sprite representing the visual appearance of the rock. */
    private final Sprite sprite;

    /** Indicates whether the rock is destroyed. */
    private boolean isDestroyed;

    /**
     * Constructs a Rock entity.
     *
     * @param area        (Area): The area to which the rock belongs. Not null.
     * @param orientation (Orientation): The initial orientation of the rock. Not null.
     * @param position    (DiscreteCoordinates): The initial position of the rock in the area. Not null.
     */
    public Rock(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        this.sprite = new Sprite("rock.1", 1, 1, this);
    }

    /**
     * Destroys the rock by unregistering it from the area.
     */
    public void destroy() {
        unregister();
    }

    /**
     * Unregisters the rock from the current area.
     */
    private void unregister() {
        getOwnerArea().unregisterActor(this);
    }

    /**
     * Draws the rock's sprite on the canvas.
     *
     * @param canvas (Canvas): The canvas to draw on.
     */
    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }

    /**
     * Handles interactions with the rock.
     *
     * @param v                (AreaInteractionVisitor): The visitor managing the interaction.
     * @param isCellInteraction (boolean): True if the interaction is at the cell level.
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }
}
