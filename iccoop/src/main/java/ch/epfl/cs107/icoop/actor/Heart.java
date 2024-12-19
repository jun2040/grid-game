package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

public class Heart extends ICoopCollectable {
    private static final int ANIMATION_DURATION = 24;

    private final Animation animation;

    private final int restorationFactor;

    /**
     * Default AreaEntity constructor
     *
     * @param area              (Area): Owner area. Not null
     * @param orientation       (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position          (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     * @param restorationFactor (int) : Amount by which the player's health will be restored by
     */
    public Heart(Area area, Orientation orientation, DiscreteCoordinates position, int restorationFactor) {
        super(area, orientation, position);

        this.restorationFactor = restorationFactor;
        this.animation = new Animation("icoop/heart", 4, 1, 1, this, 16, 16,
                ANIMATION_DURATION / 4, true);

    }

    @Override
    public void draw(Canvas canvas) {
        animation.draw(canvas);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        animation.update(deltaTime);
    }

    /**
     * @return the amount by which the player's health is restored
     */
    public int getRestorationFactor() {
        return restorationFactor;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }
}
