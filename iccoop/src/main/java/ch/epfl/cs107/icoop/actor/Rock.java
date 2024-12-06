package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;

public class Rock extends Obstacle {
    private final Sprite sprite;

    private boolean isDestroyed;

    /**
     * Default AreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
    public Rock(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);

        this.sprite = new Sprite("rock.1", 1, 1, this);
        this.isDestroyed = false;
    }

    @Override
    public void draw(Canvas canvas) {
        if (isDestroyed) return;

        sprite.draw(canvas);
    }
}
