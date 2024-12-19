package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

/**
 * Represents a collectible Coin in the game.
 * The Coin can be animated, drawn, and collected by an actor.
 */
public class Coin extends ICoopCollectable {

    /**
     * Animation for the spinning coin sprite
     */
    private final Animation coinSprite;

    /**
     * Duration for the coin's animation cycle
     */
    private final static int ANIMATION_DURATION = 6;

    /**
     * Constructs a Coin entity with its initial parameters.
     *
     * @param area        (Area): Owner area. Not null.
     * @param orientation (Orientation): Initial orientation of the Coin in the Area. Not null.
     * @param position    (DiscreteCoordinates): Initial position of the Coin in the Area. Not null.
     */
    public Coin(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        this.coinSprite = new Animation(
                "icoop/coin",
                4, 1f, 1f,
                this, 16, 16,
                ANIMATION_DURATION, true);
    }

    /**
     * Updates the animation state of the Coin.
     *
     * @param deltaTime (float): Elapsed time since last update in seconds, non-negative.
     */
    @Override
    public void update(float deltaTime) {
        coinSprite.update(deltaTime);
    }

    /**
     * Draws the Coin's animation on the canvas.
     *
     * @param canvas (Canvas): Target canvas where the coin is rendered. Not null.
     */
    @Override
    public void draw(Canvas canvas) {
        coinSprite.draw(canvas);
    }

    /**
     * Collects the Coin and removes it from the game area.
     * Notifies the Area that this Coin should be unregistered.
     */
    @Override
    public void collect() {
        super.collect();
        getOwnerArea().unregisterActor(this);
    }

    /**
     * Accepts an interaction from a visitor.
     *
     * @param v                 (AreaInteractionVisitor): Visitor interacting with the Coin. Not null.
     * @param isCellInteraction (boolean): Indicates if the interaction is at the cell level.
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }
}
