package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

public class Flame extends Projectile {
    private final Animation animation;

    private final FlameProjectileInteractionHandler interactionHandler;

    /**
     * Default MovableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     * @param speed       (int): Initial speed of the entity. Not null
     * @param range       (int): Total range of entity. Not null
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
     * FlameProjectileInteractionHandler : deals damage to the player stepping into it,
     * activates bombs on contact
     */
    private class FlameProjectileInteractionHandler implements ICoopInteractionVisitor {
        @Override
        public void interactWith(Interactable other, boolean isCellInteraction) {
        }

        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            if (isCellInteraction) {
                player.hit(ICoopPlayer.DamageType.FIRE);
                destroy();
            }
        }

        @Override
        public void interactWith(Explosive explosive, boolean isCellInteraction) {
            if (isCellInteraction) {
                explosive.activate();
                destroy();
            }
        }
    }
}
