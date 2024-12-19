package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

/**
 * Represents a projectile in the game world with elemental properties.
 * The projectile interacts with various entities, applying effects based on its elemental type.
 */
public class ElementalProjectile extends Projectile {

    /**
     * The duration of each frame in the animation sequence.
     */
    private static final int ANIMATION_DURATION = 12;

    /**
     * The animation used to visually represent the projectile.
     */
    private final Animation animation;

    /**
     * The elemental type of the projectile (e.g., Fire, Water, etc.).
     */
    private final ElementType elementType;

    /**
     * The number of times the projectile has interacted with rocks.
     */
    private int hitCount;

    /**
     * Handles interactions between the projectile and other entities.
     */
    private final ElementalProjectileInteractionHandler interactionHandler;

    /**
     * Constructs an ElementalProjectile with the specified parameters.
     *
     * @param area        (Area): The area this projectile belongs to. Not null.
     * @param orientation (Orientation): The initial orientation of the projectile. Not null.
     * @param position    (DiscreteCoordinates): The initial position of the projectile. Not null.
     * @param speed       (int): The speed of the projectile. Not null.
     * @param range       (int): The range of the projectile. Not null.
     * @param elementType (ElementType): The elemental type of the projectile. Not null.
     */
    public ElementalProjectile(
            Area area,
            Orientation orientation,
            DiscreteCoordinates position,
            int speed,
            int range,
            ElementType elementType
    ) {
        super(area, orientation, position, speed, range);

        String name = "icoop/" + (elementType.getName().equals("feu") ? "magicFireProjectile" : "magicWaterProjectile");
        this.animation = new Animation(name, 4, 1, 1, this, 32, 32,
                ANIMATION_DURATION / 4, true);

        this.elementType = elementType;

        this.interactionHandler = new ElementalProjectileInteractionHandler();
    }

    /**
     * Updates the state of the projectile and its animation.
     *
     * @param deltaTime (float): The time since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        animation.update(deltaTime);
    }

    /**
     * Draws the projectile's animation on the canvas.
     *
     * @param canvas (Canvas): The canvas on which to draw the projectile.
     */
    @Override
    public void draw(Canvas canvas) {
        animation.draw(canvas);
    }

    /**
     * Handles interactions between the projectile and other interactable entities.
     *
     * @param other             (Interactable): The entity interacting with the projectile.
     * @param isCellInteraction (boolean): True if the interaction is at the cell level.
     */
    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(interactionHandler, isCellInteraction);
    }

    /**
     * Handles specific interactions for the ElementalProjectile.
     */
    private class ElementalProjectileInteractionHandler implements ICoopInteractionVisitor {

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
         * Handles interaction with an enemy.
         *
         * @param enemy             (Enemy): The enemy interacting with the projectile.
         * @param isCellInteraction (boolean): True if the interaction is at the cell level.
         *                          Description: While the enemy is still alive and interacts with the projectile,
         *                          it will receive damage based on the enemy's elemental type.
         */
        @Override
        public void interactWith(Enemy enemy, boolean isCellInteraction) {
            if (isCellInteraction && !enemy.isDead()) {
                enemy.hit(ICoopPlayer.DamageType.toType(elementType.getName()));
                destroy();
            }
        }

        /**
         * Handles interaction with a rock.
         *
         * @param rock              (Rock): The rock interacting with the projectile.
         * @param isCellInteraction (boolean): True if the interaction is at the cell level.
         *                          Description: Destroys the rock after contact and increments the hit counter.
         *                          If the hit count reaches 3, the projectile destroys itself.
         */
        @Override
        public void interactWith(Rock rock, boolean isCellInteraction) {
            if (isCellInteraction) {
                if (hitCount >= 3) {
                    destroy();
                    return;
                }

                rock.destroy();
                hitCount++;
            }
        }

        /**
         * Handles interaction with an obstacle.
         *
         * @param obstacle          (Obstacle): The obstacle interacting with the projectile.
         * @param isCellInteraction (boolean): True if the interaction is at the cell level.
         *                          Description: Destroys the projectile when it collides with an obstacle.
         */
        @Override
        public void interactWith(Obstacle obstacle, boolean isCellInteraction) {
            if (isCellInteraction) {
                destroy();
            }
        }
    }
}
