package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Coin extends ICoopCollectable{
    private Animation coinSprite;
    private final static int ANIMATION_DURATION = 6;

    public Coin(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        this.coinSprite =  new Animation(
                "icoop/coin",
                4, 1f, 1f,
                this , 16, 16,
                ANIMATION_DURATION , true
        );
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
    public void collect(){
        getOwnerArea().unregisterActor(this);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this,isCellInteraction);
    }
}
