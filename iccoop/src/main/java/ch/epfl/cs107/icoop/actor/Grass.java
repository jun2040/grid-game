package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

/**
 * Represents a grass obstacle in the game.
 * The grass can be destroyed, triggering a sliced animation and a chance to drop a coin.
 */
public class Grass extends Obstacle {

    /**
     * The duration of each frame in the sliced animation.
     */
    private final static int ANIMATION_DURATION = 4;

    /**
     * The animation displayed when the grass is sliced.
     */
    private final Animation slicedAnimation;

    /**
     * The sprite representing the grass in its initial state.
     */
    private final Sprite sprite;

    /**
     * Tracks whether the grass has been destroyed.
     */
    private boolean isDestroyed;

    /**
     * Constructs a Grass entity with the specified parameters.
     *
     * @param area        (Area): The area to which the grass belongs. Not null.
     * @param orientation (Orientation): The initial orientation of the grass. Not null.
     * @param position    (DiscreteCoordinates): The initial position of the grass. Not null.
     */
    public Grass(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);

        this.sprite = new Sprite("icoop/grass", 1.2f, 1.2f, this);
        this.slicedAnimation = new Animation(
                "icoop/grass.sliced",
                4, 1f, 1f,
                this, 32, 32,
                ANIMATION_DURATION / 2, false);
    }

    /**
     * Updates the state of the grass, including the sliced animation and coin drop logic.
     *
     * @param deltaTime (float): The time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (isDestroyed) {
            slicedAnimation.update(deltaTime);
        }

        if (slicedAnimation.isCompleted()) {
            unregister();
            dropCoin();
        }
    }

    /**
     * Draws the appropriate representation of the grass based on its state.
     *
     * @param canvas (Canvas): The canvas to draw the grass on.
     */
    @Override
    public void draw(Canvas canvas) {
        if (!isDestroyed) {
            sprite.draw(canvas);
        } else {
            slicedAnimation.draw(canvas);
        }
    }

    /**
     * Drops a coin with a 50% chance upon grass destruction.
     */
    private void dropCoin() {
        if (Math.random() < 0.5) {
            getOwnerArea().registerActor(new Coin(getOwnerArea(), Orientation.UP, this.getCurrentMainCellCoordinates()));
        }
    }

    /**
     * Marks the grass as destroyed, triggering the sliced animation.
     */
    public void destroy() {
        isDestroyed = true;
    }

    /**
     * Unregisters the grass from its owner area.
     */
    private void unregister() {
        getOwnerArea().unregisterActor(this);
    }

    /**
     * Checks whether the grass has been destroyed.
     *
     * @return (boolean): True if the grass is destroyed, false otherwise.
     */
    public boolean isDestroyed() {
        return isDestroyed;
    }

    /**
     * Handles interactions with other entities, delegating to the visitor.
     *
     * @param v                 (AreaInteractionVisitor): The visitor handling the interaction.
     * @param isCellInteraction (boolean): True if the interaction is at the cell level.
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }
}
