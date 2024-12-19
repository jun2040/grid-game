package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.engine.actor.RPGSprite;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import static ch.epfl.cs107.play.math.Orientation.*;

import java.util.Collections;
import java.util.List;

public class Staff extends ElementalItem {
    private static final int ANIMATION_DURATION = 32;
    private static final int ANIMATION_FRAMES = 8;

    private final Animation animation;

    /**
     * Default AreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     * @param elementType (ElementType): Elemental type of item. Can be NULL
     * @param spriteName  (String) : sprite name used to get sprite. Not null
     */
    public Staff(Area area, DiscreteCoordinates position, ElementType elementType, String spriteName) {
        super(area, position, elementType);

        final Sprite[] sprites = new Sprite[8];
        for (int i = 0; i < 8; i++) {
            sprites[i] = new RPGSprite("icoop/" + spriteName, 2, 2, this, new RegionOfInterest(i *
                    32, 0, 32, 32), new Vector(-0.5f, 0));
        }

        this.animation = new Animation(ANIMATION_DURATION / ANIMATION_FRAMES, sprites);

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

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }
}
