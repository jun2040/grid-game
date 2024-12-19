package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.icoop.utility.Timer;
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

/**
 * Represents an enemy in the game world.
 * Enemies can interact with other entities, take damage, and play death animations upon defeat.
 */
public abstract class Enemy extends MovableAreaEntity implements Interactable, Interactor {

    /**
     * The duration of each frame in the death animation.
     */
    private static final int ANIMATION_DURATION = 24;

    /**
     * The grace period (in frames) during which the enemy is immune to damage after being hit.
     */
    private static final int GRACE_PERIOD = 10;

    /**
     * The maximum health points the enemy had when created. This also serves as the maximum health.
     */
    private int healthPoint;

    /**
     * The animation displayed when the enemy dies.
     */
    protected final Animation deathAnimation;

    /**
     * Indicates whether the enemy is currently dead.
     */
    private boolean isDead = false;

    /**
     * Timer used to track the grace period for immunity after taking damage.
     */
    private final Timer gracePeriodTimer;

    /**
     * A list of damage types that the enemy is immune to.
     */
    private final List<ICoopPlayer.DamageType> immunityType = new ArrayList<>();

    /**
     * Constructs an Enemy with the specified parameters.
     *
     * @param area           (Area): The area this enemy belongs to. Not null.
     * @param orientation    (Orientation): The initial orientation of the enemy. Not null.
     * @param position       (DiscreteCoordinates): The initial position of the enemy. Not null.
     * @param maxHealthPoint (int): The maximum health points of the enemy. Not null.
     * @param immunityType   (List<ICoopPlayer.DamageType>): A list of damage types the enemy is immune to. Not null.
     */
    public Enemy(
            Area area,
            Orientation orientation,
            DiscreteCoordinates position,
            int maxHealthPoint,
            List<ICoopPlayer.DamageType> immunityType
    ) {
        super(area, orientation, position);

        this.healthPoint = maxHealthPoint;

        this.deathAnimation =
                new Animation(
                        "icoop/vanish", 7, 2, 2, this, 32, 32,
                        new Vector(-0.5f, 0f), ANIMATION_DURATION / 7, false
                );

        this.gracePeriodTimer = new Timer();

        this.immunityType.addAll(immunityType);
    }

    /**
     * Updates the state of the enemy.
     *
     * @param deltaTime (float): The time since the last update, in seconds. Non-negative.
     *                  Description: Checks if the enemy is dead, recovering, or if its death animation is complete,
     *                  and makes the appropriate responses.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (deathAnimation.isCompleted()) {
            getOwnerArea().unregisterActor(this);
        }

        if (healthPoint <= 0) {
            die();
        }

        if (isDead && !deathAnimation.isCompleted()) {
            deathAnimation.update(deltaTime);
        }

        gracePeriodTimer.update(deltaTime);
    }

    /**
     * Marks the enemy as dead.
     */
    private void die() {
        isDead = true;
    }

    /**
     * Applies damage to the enemy if it is not immune to the damage type and not within the grace period.
     *
     * @param damageType (ICoopPlayer.DamageType): The type of damage being applied.
     */
    public void hit(ICoopPlayer.DamageType damageType) {
        if (!gracePeriodTimer.isCompleted() || isDead) {
            return;
        }

        for (ICoopPlayer.DamageType t : immunityType) {
            if (damageType.equals(t)) {
                return;
            }
        }

        healthPoint -= damageType.damage;

        gracePeriodTimer.setTimer(GRACE_PERIOD);
    }

    /**
     * Checks if the enemy is dead.
     *
     * @return (boolean): True if the enemy is dead, false otherwise.
     */
    public boolean isDead() {
        return isDead;
    }

    /**
     * Indicates that the enemy wants to interact with cell-based interactions.
     *
     * @return (boolean): True if the enemy wants cell interaction, false otherwise.
     */
    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    /**
     * Indicates that the enemy wants to interact with view-based interactions.
     *
     * @return (boolean): True if the enemy wants view interaction, false otherwise.
     */
    @Override
    public boolean wantsViewInteraction() {
        return true;
    }

    /**
     * Determines whether the enemy occupies cell space.
     *
     * @return (boolean): True if the enemy occupies cell space, false if it is dead.
     */
    @Override
    public boolean takeCellSpace() {
        return !isDead;
    }

    /**
     * Indicates whether the enemy is interactable at the cell level.
     *
     * @return (boolean): True if the enemy is cell-interactable, false otherwise.
     */
    @Override
    public boolean isCellInteractable() {
        return true;
    }

    /**
     * Indicates whether the enemy is interactable at the view level.
     *
     * @return (boolean): True if the enemy is view-interactable, false otherwise.
     */
    @Override
    public boolean isViewInteractable() {
        return true;
    }

    /**
     * Accepts an interaction with the enemy, delegating to the appropriate visitor.
     *
     * @param v                 (AreaInteractionVisitor): The visitor handling the interaction.
     * @param isCellInteraction (boolean): True if the interaction is at the cell level.
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }
}
