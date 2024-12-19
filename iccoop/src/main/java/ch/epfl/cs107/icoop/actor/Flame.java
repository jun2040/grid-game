package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

/**
 * Represents a flame projectile in the game.
 * The flame deals fire damage to players and activates explosives upon contact.
 */
public class Flame extends Projectile {

    /**
     * The animation used to visually represent the flame.
     */
    private final Animation animation;

    /**
     * Handles interactions between the flame and other entities.
     */
    private final FlameProjectileInteractionHandler interactionHandler;

    /**
     * Constructs a Flame projectile with the specified parameters.
     *
     * @param area        (Area): The area to which the flame belongs. Not null.
     * @param orientation (Orientation): The initial orientation of the flame. Not null.
     * @param position    (DiscreteCoordinates): The initial position of the flame. Not null.
     * @param speed       (int): The speed of the flame. Not null.
     * @param range       (int): The range of the flame. Not null.
     */
    public Flame(Area area, Orientation orientation, DiscreteCoordinates position, int speed, int range) {
        super(area, orientation, position, speed, range);

        this.animation = new Animation(
                "icoop/fire",
                7, 1, 1,
                this, 16, 16,
                4, true);

        interactionHandler = new FlameProjectileInteractionHandler();
    }

    /**
     * Updates the state of the flame, including its animation.
     *
     * @param deltaTime (float): The time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        animation.update(deltaTime);
    }

    /**
     * Draws the flame's animation on the canvas.
     *
     * @param canvas (Canvas): The canvas to draw the flame on.
     */
    @Override
    public void draw(Canvas canvas) {
        animation.draw(canvas);
    }

    /**
     * Handles interactions between the flame and other interactable entities.
     *
     * @param other             (Interactable): The entity interacting with the flame.
     * @param isCellInteraction (boolean): True if the interaction is at the cell level.
     */
    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(interactionHandler, isCellInteraction);
    }

    /**
     * Handles specific interactions for the Flame projectile.
     * Deals damage to players and activates bombs upon contact.
     */
    private class FlameProjectileInteractionHandler implements ICoopInteractionVisitor {

        /**
         * Default interaction with generic interactable entities.
         *
         * @param other             (Interactable): The interacting entity.
         * @param isCellInteraction (boolean): True if the interaction is at the cell level.
         */
        @Override
        public void interactWith(Interactable other, boolean isCellInteraction) {
        }

        /**
         * Handles interaction with a player, dealing fire damage and destroying the flame.
         *
         * @param player            (ICoopPlayer): The player interacting with the flame.
         * @param isCellInteraction (boolean): True if the interaction is at the cell level.
         */
        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            if (isCellInteraction) {
                player.hit(ICoopPlayer.DamageType.FIRE);
                destroy();
            }
        }

        /**
         * Handles interaction with an explosive, activating it and destroying the flame.
         *
         * @param explosive         (Explosive): The explosive interacting with the flame.
         * @param isCellInteraction (boolean): True if the interaction is at the cell level.
         */
        @Override
        public void interactWith(Explosive explosive, boolean isCellInteraction) {
            if (isCellInteraction) {
                explosive.activate();
                destroy();
            }
        }
    }
}
