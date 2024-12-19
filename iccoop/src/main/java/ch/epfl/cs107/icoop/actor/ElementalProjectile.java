package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

public class ElementalProjectile extends Projectile {
    private static final int ANIMATION_DURATION = 12;

    private final Animation animation;
    private final ElementType elementType;

    private int hitCount;

    private final ElementalProjectileInteractionHandler interactionHandler;

    /**
     * Default MovableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     * @param speed       (int): Initial speed of the entity. Not null
     * @param range       (int): Total range of entity. Not null
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

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        animation.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        animation.draw(canvas);
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(interactionHandler, isCellInteraction);
    }

    /**
     * ElementalProjectileInteractionHandler: activates event when foreign actor interacts with it
     */
    private class ElementalProjectileInteractionHandler implements ICoopInteractionVisitor {
        @Override
        public void interactWith(Interactable other, boolean isCellInteraction) {
        }

        /**
         * @param enemy
         * @param isCellInteraction Description : While the enemy is still alive and interacts with the projectile,
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
         * @param rock
         * @param isCellInteraction Description : Destroys rock with which it comes into contact with
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
         * @param obstacle
         * @param isCellInteraction Description : Destroy projectile when colliding with obstacle
         */
        @Override
        public void interactWith(Obstacle obstacle, boolean isCellInteraction) {
            if (isCellInteraction)
                destroy();
        }
    }
}
