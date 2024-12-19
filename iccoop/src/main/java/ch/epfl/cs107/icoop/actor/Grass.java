package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

public class Grass extends Obstacle {
    private final static int ANIMATION_DURATION = 4;

    private final Animation slicedAnimation;
    private final Sprite sprite;

    private boolean isDestroyed;

    /**
     * Default AreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
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

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (isDestroyed)
            slicedAnimation.update(deltaTime);

        if (slicedAnimation.isCompleted()) {
            unregister();
            dropCoin();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (!isDestroyed)
            sprite.draw(canvas);
        else
            slicedAnimation.draw(canvas);
    }

    /**
     * drops coin based on a sudo random probability generator
     */
    private void dropCoin() {
        if (Math.random() < 0.5)
            getOwnerArea().registerActor(new Coin(getOwnerArea(), Orientation.UP, this.getCurrentMainCellCoordinates()));
    }

    /**
     * Destroys grass bush
     */
    public void destroy() {
        isDestroyed = true;
    }

    /**
     * unregisters actor from area
     */
    private void unregister() {
        getOwnerArea().unregisterActor(this);
    }

    /**
     * @return whether it has been destroyed
     */
    public boolean isDestroyed() {
        return isDestroyed;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }
}
