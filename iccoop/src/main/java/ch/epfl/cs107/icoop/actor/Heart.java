package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

/**
 * Represents a collectible heart in the game that restores a player's health.
 */
public class Heart extends ICoopCollectable {

    /**
     * The duration of each frame in the heart's animation.
     */
    private static final int ANIMATION_DURATION = 24;

    /**
     * The animation used to visually represent the heart.
     */
    private final Animation animation;

    /**
     * The amount of health restoration the heart provides.
     */
    private final int restorationFactor;

    /**
     * Constructs a Heart with the specified parameters.
     *
     * @param area              (Area): The area to which the heart belongs. Not null.
     * @param orientation       (Orientation): The orientation of the heart. Not null.
     * @param position          (DiscreteCoordinates): The initial position of the heart. Not null.
     * @param restorationFactor (int): The amount by which the player's health will be restored. Not null.
     */
    public Heart(Area area, Orientation orientation, DiscreteCoordinates position, int restorationFactor) {
        super(area, orientation, position);

        this.restorationFactor = restorationFactor;
        this.animation = new Animation("icoop/heart", 4, 1, 1, this, 16, 16,
                ANIMATION_DURATION / 4, true);
    }

    /**
     * Draws the heart's animation on the canvas.
     *
     * @param canvas (Canvas): The canvas to draw the heart on.
     */
    @Override
    public void draw(Canvas canvas) {
        animation.draw(canvas);
    }

    /**
     * Updates the heart's animation state.
     *
     * @param deltaTime (float): The time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        animation.update(deltaTime);
    }

    /**
     * Retrieves the amount of health restoration the heart provides.
     *
     * @return (int): The restoration factor.
     */
    public int getRestorationFactor() {
        return restorationFactor;
    }

    /**
     * Handles interactions with the heart, delegating to the visitor.
     *
     * @param v                 (AreaInteractionVisitor): The visitor handling the interaction.
     * @param isCellInteraction (boolean): True if the interaction is at the cell level.
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }
}
