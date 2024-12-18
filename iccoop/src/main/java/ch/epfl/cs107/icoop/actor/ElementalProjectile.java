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

    private final ElementalProjectileInteractionHandler handler;

    /**
     * Default MovableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     * @param speed
     * @param range
     */
    public ElementalProjectile(Area area, Orientation orientation, DiscreteCoordinates position, int speed, int range, ElementType elementType) {
        super(area, orientation, position, speed, range);

        // FIXME: Encapsulate in enum;
        String name = "icoop/" + (elementType.getName().equals("feu") ? "magicFireProjectile" : "magicWaterProjectile");
        this.animation = new Animation(name , 4, 1, 1, this , 32, 32,
                ANIMATION_DURATION / 4, true);
        this.elementType = elementType;
        this.handler = new ElementalProjectileInteractionHandler();
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

    private void unregister() {
        getOwnerArea().unregisterActor(this);
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }

    private class ElementalProjectileInteractionHandler implements ICoopInteractionVisitor {
        @Override
        public void interactWith(Interactable other, boolean isCellInteraction) {}

        @Override
        public void interactWith(Enemy enemy, boolean isCellInteraction) {
            if (isCellInteraction && !enemy.isDead()) {
                // FIXME: Only damage the enemy with the same element (reversed right now)
                enemy.hit(ICoopPlayer.DamageType.toType(elementType.getName()));
                destroy();
            }
        }

        @Override
        public void interactWith(Rock rock, boolean isCellInteraction) {
            if(isCellInteraction){
                rock.destroy();
                unregister();
            }

        }
    }
}
