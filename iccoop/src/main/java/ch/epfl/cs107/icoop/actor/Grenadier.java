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

public class Grenadier extends Enemy {
    private static final int ANIMATION_DURATION = 24;

    private final OrientedAnimation defaultAnimation;
    private final OrientedAnimation protectAnimation;

    private final Timer idleTimer;
    private final Timer walkTimer;
    private final Timer protectTimer;

    private GrenadierState currentState;
    private ICoopPlayer target = null;

    private final GrenadierInteractionHandler interactionHandler;

    /**
     * Default MovableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
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

    private void idle(float deltaTime) {
        idleTimer.update(deltaTime);

        if (idleTimer.isCompleted())
            gotoWalkState();
    }

    private void walk(float deltaTime) {
        defaultAnimation.update(deltaTime);

        walkTimer.update(deltaTime);

        moveRandom(2);

        if (walkTimer.isCompleted())
            gotoIdleState();
    }

    private void attack(float deltaTime) {
        defaultAnimation.update(deltaTime);

        moveToTarget();

        Vector displacement = target.getPosition().sub(getPosition());

        if (displacement.getLength() < 2) {
            placeExplosive();
            gotoProtectState();
        }
    }

    private void protect(float deltaTime) {
        if (!protectAnimation.isCompleted())
            protectAnimation.update(deltaTime);

        moveRandom(1);

        protectTimer.update(deltaTime);

        if (protectTimer.isCompleted())
            gotoIdleState();
    }

    /***** STATE HANDLERS *****/

    private void gotoIdleState() {
        defaultAnimation.reset();
        currentState = GrenadierState.IDLE;
        idleTimer.setTimer(generateRandomTime(5));
    }

    private void gotoWalkState() {
        currentState = GrenadierState.WALK;
        walkTimer.setTimer(generateRandomTime(2, 3));
    }

    private void gotoAttackState() {
        currentState = GrenadierState.ATTACK;
    }

    private void gotoProtectState() {
        protectAnimation.reset();
        currentState = GrenadierState.PROTECT;
        protectTimer.setTimer(generateRandomTime(3, 5));
    }

    /***** BEHAVIOR *****/

    private void moveRandom(int speedFactor) {
        if (isDisplacementOccurs())
            return;

        boolean changeDirection = RandomGenerator.getInstance().nextDouble() < 0.4;
        if (changeDirection) {
            int directionId = RandomGenerator.getInstance().nextInt(0, 4);
            orientate(Orientation.values()[directionId]);
        }

        move(ANIMATION_DURATION / speedFactor);
    }

    private void moveToTarget() {
        if (isDisplacementOccurs())
            return;

        Vector v = target.getPosition().sub(getPosition());

        if (v.getLength() <= 1.5f)
            return;

        // Use values over Orientation.fromVector method to prevent null pointer exception
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

    private void placeExplosive() {
        Explosive explosive = new Explosive(getOwnerArea(), LEFT, getFieldOfViewCells().getFirst(), 100);

        // FIXME: Bombs can be placed in the same tile since they are walkable & does not take cell space
        if (getOwnerArea().canEnterAreaCells(explosive, getFieldOfViewCells())) {
            // FIXME: Prevent activation of bomb immediately after placing
            getOwnerArea().registerActor(explosive);
            explosive.activate();
        }
    }

    /***** RANDOM GENERATOR *****/

    private float generateRandomTime(float max) {
        return RandomGenerator.getInstance().nextFloat(0, max);
    }

    private float generateRandomTime(float min, float max) {
        return RandomGenerator.getInstance().nextFloat(min, max);
    }

    /***** IMPLEMENTATIONS *****/

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        if (currentState != GrenadierState.ATTACK) {
            List<DiscreteCoordinates> perceptionField = new ArrayList<>(8);

            for (int i = 1; i <= 8; i++)
                perceptionField.add(getCurrentMainCellCoordinates().jump(getOrientation().toVector().mul(i)));

            return perceptionField;
        }

        return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
    }

    @Override
    public boolean wantsCellInteraction() {
        return false;
    }

    @Override
    public boolean wantsViewInteraction() {
        return currentState == GrenadierState.IDLE || currentState == GrenadierState.ATTACK;
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(interactionHandler, isCellInteraction);
    }

    /***** STATES *****/

    private enum GrenadierState {
        IDLE,
        WALK,
        PROTECT,
        ATTACK,
        ;
    }

    /***** INTERACTION HANDLERS *****/

    private class GrenadierInteractionHandler implements ICoopInteractionVisitor {
        @Override
        public void interactWith(Interactable other, boolean isCellInteraction) {
        }

        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            if (currentState == GrenadierState.PROTECT)
                return;

            gotoAttackState();

            target = player;
        }
    }
}
