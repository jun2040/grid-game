package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.engine.actor.OrientedAnimation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.math.random.RandomGenerator;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HellSkull extends Enemy {
    private static final int ANIMATION_DURATION = 12;

    private final OrientedAnimation animation;

    private float flameSpawnTimer;

    /**
     * Default MovableAreaEntity constructor
     *
     * @param area           (Area): Owner area. Not null
     * @param orientation    (Orientation): Initial orientation of the entity. Not null
     * @param position       (Coordinate): Initial position of the entity. Not null
     */
    public HellSkull(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position, 1, new ArrayList<>(Arrays.asList(ICoopPlayer.DamageType.FIRE)));

        Orientation[] orders = new Orientation []{ Orientation.UP,
                Orientation.LEFT , Orientation.DOWN , Orientation.RIGHT};
        this.animation = new OrientedAnimation("icoop/flameskull",
                ANIMATION_DURATION / 3, this ,
                new Vector(-0.5f, -0.5f), orders ,
                3, 2, 2, 32, 32, true);

        this.flameSpawnTimer = RandomGenerator.getInstance().nextFloat(0.5f, 2.0f);
    }

    @Override
    public void draw(Canvas canvas) {
        if (!isDead())
            animation.draw(canvas);
        else
            deathAnimation.draw(canvas);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        animation.update(deltaTime);

        if (flameSpawnTimer >= 0.0f) {
            flameSpawnTimer -= deltaTime;
        } else {
            launchFlames();
            flameSpawnTimer =  RandomGenerator.getInstance().nextFloat(0.5f, 2.0f);
        }
    }

    private void launchFlames() {
        DiscreteCoordinates target = getFieldOfViewCells().getFirst();

        // FIXME: Sketchy casting used (Area -> ICoopArea), prone to casting exception
        if (((ICoopArea) getOwnerArea()).isCellFree(target))
            getOwnerArea().registerActor(new Flame(getOwnerArea(), getOrientation(), target, 5, 50));
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
    }
}
