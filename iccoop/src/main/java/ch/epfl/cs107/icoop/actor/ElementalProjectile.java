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
        // TODO: Figure out why all the fire-element related assets are missing
//        String name = "icoop/" + (elementType.getName().equals("feu") ? "magicFireProjectile" : "magicWaterProjectile");
        String name = "icoop/magicWaterProjectile";
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
                enemy.hit(ICoopPlayer.DamageType.toType(elementType.getName()));
                destroy();
            }
        }
    }
}
