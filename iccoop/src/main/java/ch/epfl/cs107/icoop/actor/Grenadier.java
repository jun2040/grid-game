package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.icoop.utility.Timer;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.OrientedAnimation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.math.random.RandomGenerator;
import ch.epfl.cs107.play.window.Canvas;

import java.util.*;

import static ch.epfl.cs107.play.math.Orientation.*;

/**
 * Represents a Grenadier enemy in the game.
 * The Grenadier has multiple states including idle, walking, protecting, and attacking.
 * It can place explosives and interact with players.
 */
public class Grenadier extends Enemy {

    /**
     * The duration of each frame in the animations.
     */
    private static final int ANIMATION_DURATION = 24;

    /**
     * The animation used in the default states (idle, walking, attacking).
     */
    private final OrientedAnimation defaultAnimation;

    /**
     * The animation used when the Grenadier is protecting itself.
     */
    private final OrientedAnimation protectAnimation;

    /**
     * Timer for managing idle state duration.
     */
    private final Timer idleTimer;

    /**
     * Timer for managing walk state duration.
     */
    private final Timer walkTimer;

    /**
     * Timer for managing protect state duration.
     */
    private final Timer protectTimer;

    /**
     * The current state of the Grenadier (Idle, Walk, Protect, or Attack).
     */
    private GrenadierState currentState;

    /**
     * The target player for the Grenadier to attack.
     */
    private ICoopPlayer target = null;

    /**
     * Handles interactions between the Grenadier and other entities.
     */
    private final GrenadierInteractionHandler interactionHandler;

    /**
     * Constructs a Grenadier with the specified parameters.
     *
     * @param area        (Area): The area to which the Grenadier belongs. Not null.
     * @param orientation (Orientation): The initial orientation of the Grenadier. Not null.
     * @param position    (DiscreteCoordinates): The initial position of the Grenadier. Not null.
     */
    public Grenadier(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position, 1, new ArrayList<>(Arrays.asList(ICoopPlayer.DamageType.WATER)));

        final Vector anchor = new Vector(-0.5f, 0);
        final Orientation[] orders = {DOWN, RIGHT, UP, LEFT};

        this.defaultAnimation = new OrientedAnimation(
                "icoop/bombFoe", ANIMATION_DURATION / 3,
                this, anchor, orders, 4,
                2, 2,
                32, 32,
                true
        );

        this.protectAnimation = new OrientedAnimation(
                "icoop/bombFoe.protecting", ANIMATION_DURATION / 3,
                this, anchor, orders, 4,
                2, 2, 32, 32,
                false
        );

        this.currentState = GrenadierState.IDLE;

        this.idleTimer = new Timer();
        this.walkTimer = new Timer();
        this.protectTimer = new Timer();

        this.interactionHandler = new GrenadierInteractionHandler();
    }

    /**
     * Draws the Grenadier based on its current state.
     *
     * @param canvas (Canvas): The canvas to draw the Grenadier on.
     */
    @Override
    public void draw(Canvas canvas) {
        if (isDead()) {
            deathAnimation.draw(canvas);
            return;
        }

        switch (currentState) {
            case WALK:
            case IDLE:
            case ATTACK:
                defaultAnimation.draw(canvas);
                break;
            case PROTECT:
                protectAnimation.draw(canvas);
                break;
        }
    }

    /**
     * Updates the Grenadier's state machine and behavior based on its current state.
     *
     * @param deltaTime (float): The time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        switch (currentState) {
            case IDLE:
                idle(deltaTime);
                break;
            case WALK:
                walk(deltaTime);
                break;
            case ATTACK:
                attack(deltaTime);
                break;
            case PROTECT:
                protect(deltaTime);
                break;
        }
    }

    /***** ACTIONS *****/

    /**
     * Handles the Grenadier's idle behavior.
     *
     * @param deltaTime (float): The time elapsed since the last update.
     */
    private void idle(float deltaTime) {
        idleTimer.update(deltaTime);

        if (idleTimer.isCompleted()) {
            gotoWalkState();
        }
    }

    /**
     * Handles the Grenadier's walking behavior.
     *
     * @param deltaTime (float): The time elapsed since the last update.
     */
    private void walk(float deltaTime) {
        defaultAnimation.update(deltaTime);

        walkTimer.update(deltaTime);

        moveRandom(2);

        if (walkTimer.isCompleted()) {
            gotoIdleState();
        }
    }

    /**
     * Handles the Grenadier's attacking behavior.
     * Guides the Grenadier to the target and places an explosive if close enough.
     *
     * @param deltaTime (float): The time elapsed since the last update.
     */
    private void attack(float deltaTime) {
        defaultAnimation.update(deltaTime);

        moveToTarget();

        Vector displacement = target.getPosition().sub(getPosition());

        if (displacement.getLength() < 2) {
            placeExplosive();
            gotoProtectState();
        }
    }

    /**
     * Handles the Grenadier's protecting behavior.
     *
     * @param deltaTime (float): The time elapsed since the last update.
     */
    private void protect(float deltaTime) {
        if (!protectAnimation.isCompleted()) {
            protectAnimation.update(deltaTime);
        }

        moveRandom(1);

        protectTimer.update(deltaTime);

        if (protectTimer.isCompleted()) {
            gotoIdleState();
        }
    }

    /***** STATE HANDLERS *****/

    /**
     * Transitions the Grenadier to the Idle state.
     */
    private void gotoIdleState() {
        defaultAnimation.reset();
        currentState = GrenadierState.IDLE;
        idleTimer.setTimer(generateRandomTime(5));
    }

    /**
     * Transitions the Grenadier to the Walk state.
     */
    private void gotoWalkState() {
        currentState = GrenadierState.WALK;
        walkTimer.setTimer(generateRandomTime(2, 3));
    }

    /**
     * Transitions the Grenadier to the Attack state.
     */
    private void gotoAttackState() {
        currentState = GrenadierState.ATTACK;
    }

    /**
     * Transitions the Grenadier to the Protect state.
     */
    private void gotoProtectState() {
        protectAnimation.reset();
        currentState = GrenadierState.PROTECT;
        protectTimer.setTimer(generateRandomTime(3, 5));
    }

    /***** BEHAVIOR *****/

    /**
     * Moves the Grenadier in a random direction.
     *
     * @param speedFactor (int): The speed of the movement.
     */
    private void moveRandom(int speedFactor) {
        if (isDisplacementOccurs()) {
            return;
        }

        boolean changeDirection = RandomGenerator.getInstance().nextDouble() < 0.4;
        if (changeDirection) {
            int directionId = RandomGenerator.getInstance().nextInt(0, 4);
            orientate(Orientation.values()[directionId]);
        }

        move(ANIMATION_DURATION / speedFactor);
    }

    /**
     * Moves the Grenadier towards its target.
     */
    private void moveToTarget() {
        if (isDisplacementOccurs()) {
            return;
        }

        Vector v = target.getPosition().sub(getPosition());

        if (v.getLength() <= 1.5f) {
            return;
        }

        Orientation horizontal = v.x > 0 ? RIGHT : LEFT;
        Orientation vertical = v.y > 0 ? UP : DOWN;

        boolean axis = Math.abs(v.x) > Math.abs(v.y);
        orientate(axis ? horizontal : vertical);

        boolean isMoved = move(ANIMATION_DURATION / 3);

        if (!isMoved) {
            orientate(axis ? vertical : horizontal);
            move(ANIMATION_DURATION / 3);
        }
    }

    /**
     * Places an explosive at the Grenadier's position.
     */
    private void placeExplosive() {
        Explosive explosive = new Explosive(getOwnerArea(), LEFT, getFieldOfViewCells().getFirst(), 100);

        if (getOwnerArea().canEnterAreaCells(explosive, getFieldOfViewCells())) {
            getOwnerArea().registerActor(explosive);
            explosive.activate();
        }
    }

    /***** RANDOM GENERATOR *****/

    /**
     * Generates a random time within a maximum range.
     *
     * @param max (float): The maximum time.
     * @return (float): A random time.
     */
    private float generateRandomTime(float max) {
        return RandomGenerator.getInstance().nextFloat(0, max);
    }

    /**
     * Generates a random time within a range.
     *
     * @param min (float): The minimum time.
     * @param max (float): The maximum time.
     * @return (float): A random time.
     */
    private float generateRandomTime(float min, float max) {
        return RandomGenerator.getInstance().nextFloat(min, max);
    }

    /***** IMPLEMENTATIONS *****/

    @Override
    public void hit(ICoopPlayer.DamageType damageType) {
        if (!currentState.equals(GrenadierState.PROTECT)) {
            super.hit(damageType);
        }
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        if (currentState != GrenadierState.ATTACK) {
            List<DiscreteCoordinates> perceptionField = new ArrayList<>(8);

            for (int i = 1; i <= 8; i++) {
                perceptionField.add(getCurrentMainCellCoordinates().jump(getOrientation().toVector().mul(i)));
            }

            return perceptionField;
        }

        return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
    }

    @Override
    public boolean wantsCellInteraction() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return !currentState.equals(GrenadierState.PROTECT);
    }

    @Override
    public boolean wantsViewInteraction() {
        return !currentState.equals(GrenadierState.PROTECT);
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(interactionHandler, isCellInteraction);
    }

    /***** STATES *****/

    /**
     * Enum representing the finite states of the Grenadier.
     */
    private enum GrenadierState {
        IDLE,
        WALK,
        PROTECT,
        ATTACK
    }

    /***** INTERACTION HANDLERS *****/

    /**
     * Handles specific interactions for the Grenadier.
     */
    private class GrenadierInteractionHandler implements ICoopInteractionVisitor {

        /**
         * Default interaction with generic interactable entities.
         *
         * @param other             (Interactable): The interacting entity.
         * @param isCellInteraction (boolean): True if the interaction is at the cell level.
         */
        @Override
        public void interactWith(Interactable other, boolean isCellInteraction) {
        }

        /**
         * Handles interaction with a player, transitioning the Grenadier to attack mode.
         *
         * @param player            (ICoopPlayer): The player interacting with the Grenadier.
         * @param isCellInteraction (boolean): True if the interaction is at the cell level.
         */
        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            if (currentState == GrenadierState.PROTECT) {
                return;
            }

            gotoAttackState();

            target = player;
        }
    }
}
