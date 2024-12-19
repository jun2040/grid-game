package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

public class Coin extends ICoopCollectable {
    private final Animation coinSprite;
    private final static int ANIMATION_DURATION = 6;

    /**
     * Default AreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
    public Coin(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        this.coinSprite = new Animation(
                "icoop/coin",
                4, 1f, 1f,
                this, 16, 16,
                ANIMATION_DURATION, true);
    }

    @Override
    public void update(float deltaTime) {
        coinSprite.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        coinSprite.draw(canvas);
    }

    @Override
    public void collect() {
        super.collect();
        getOwnerArea().unregisterActor(this);
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }
}
