package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;

import java.util.ArrayList;
import java.util.List;

public abstract class Enemy extends MovableAreaEntity implements Interactable, Interactor {
    private static final int ANIMATION_DURATION = 24;
    private static final int GRACE_PERIOD = 10;

    private int maxHealthPoint;
    private int healthPoint;

    protected final Animation deathAnimation;

    private boolean isDead = false;

    private List<ICoopPlayer.DamageType> immunityType = new ArrayList<>();
    private boolean isInGracePeriod = false;
    private int gracePeriodTimer = 0;

    /**
     * Default MovableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public Enemy(
            Area area,
            Orientation orientation,
            DiscreteCoordinates position,
            int maxHealthPoint,
            List<ICoopPlayer.DamageType> immunityType
    ) {
        super(area, orientation, position);

        this.maxHealthPoint = maxHealthPoint;
        this.healthPoint = maxHealthPoint;

        this.deathAnimation =
                new Animation(
                        "icoop/vanish", 7, 2, 2, this , 32, 32,
                        new Vector(-0.5f, 0f), ANIMATION_DURATION/7, false
                );

        this.immunityType.addAll(immunityType);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (deathAnimation.isCompleted())
            getOwnerArea().unregisterActor(this);

        if (healthPoint < 0)
            die();

        if (isDead && !deathAnimation.isCompleted())
            deathAnimation.update(deltaTime);

        if (isInGracePeriod && gracePeriodTimer >= 0) {
            gracePeriodTimer--;
        } else if (gracePeriodTimer < 0) {
            isInGracePeriod = false;
            gracePeriodTimer = 0;
        }
    }

    private void die() {
        isDead = true;
    }

    public void hit(ICoopPlayer.DamageType damageType) {
        if (isInGracePeriod || isDead)
            return;

        for (ICoopPlayer.DamageType t : immunityType) {
            if (damageType.equals(t))
                return;
        }

        healthPoint -= damageType.damage;

        gracePeriodTimer = GRACE_PERIOD;
        isInGracePeriod = true;
    }

    public boolean isDead() {
        return isDead;
    }

    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {
        return true;
    }

    // FIXME: Cannot do contact interaction
    @Override
    public boolean takeCellSpace() {
        return !isDead;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }
}
