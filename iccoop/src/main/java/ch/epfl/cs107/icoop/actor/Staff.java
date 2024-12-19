package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.engine.actor.RPGSprite;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

/**
 * Represents an elemental Staff item in the game, with an animated sprite and interactions.
 */
public class Staff extends ElementalItem {

    /** The duration of the animation cycle. */
    private static final int ANIMATION_DURATION = 32;

    /** The number of frames in the animation cycle. */
    private static final int ANIMATION_FRAMES = 8;

    /** The animation object for the staff's appearance. */
    private final Animation animation;

    /**
     * Constructs a Staff entity with a specified sprite and elemental type.
     *
     * @param area        (Area): The area to which the staff belongs. Not null.
     * @param position    (DiscreteCoordinates): The initial position of the staff in the area. Not null.
     * @param elementType (ElementType): The elemental type associated with the staff. Can be null.
     * @param spriteName  (String): The name of the sprite resource. Not null.
     */
    public Staff(Area area, DiscreteCoordinates position, ElementType elementType, String spriteName) {
        super(area, position, elementType);

        final Sprite[] sprites = new Sprite[ANIMATION_FRAMES];
        for (int i = 0; i < ANIMATION_FRAMES; i++) {
            sprites[i] = new RPGSprite(
                    "icoop/" + spriteName,
                    2, 2,
                    this,
                    new RegionOfInterest(i * 32, 0, 32, 32),
                    new Vector(-0.5f, 0)
            );
        }

        this.animation = new Animation(ANIMATION_DURATION / ANIMATION_FRAMES, sprites);
    }

    /**
     * Draws the animated staff on the canvas.
     *
     * @param canvas (Canvas): The canvas to draw on.
     */
    @Override
    public void draw(Canvas canvas) {
        animation.draw(canvas);
    }

    /**
     * Updates the staff's animation over time.
     *
     * @param deltaTime (float): The time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        animation.update(deltaTime);
    }

    /**
     * Handles interactions with the staff.
     *
     * @param v                (AreaInteractionVisitor): The visitor managing the interaction.
     * @param isCellInteraction (boolean): True if the interaction is at the cell level.
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }
}
