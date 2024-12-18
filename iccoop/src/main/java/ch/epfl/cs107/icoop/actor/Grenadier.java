package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
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

    private final OrientedAnimation idleAnimation;
    private final OrientedAnimation protectingAnimation;

    private final GrenadierInteractionHandler handler;

    private GrenadierState currentState;
    private int idleTimer = 0;
    private int protectTimer = 0;
    private ICoopPlayer target = null;

    /**
     * Default MovableAreaEntity constructor
     *
     * @param area           (Area): Owner area. Not null
     * @param orientation    (Orientation): Initial orientation of the entity. Not null
     * @param position       (Coordinate): Initial position of the entity. Not null
     */
    public Grenadier(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position, 1, new ArrayList<>(Arrays.asList(ICoopPlayer.DamageType.WATER)));

        final Vector anchor = new Vector(-0.5f, 0);
        final Orientation[] orders = {DOWN , RIGHT , UP, LEFT};

        this.idleAnimation = new OrientedAnimation("icoop/bombFoe", ANIMATION_DURATION / 3,
                this , anchor , orders , 4, 2, 2, 32, 32,
                true);
        this.protectingAnimation = new OrientedAnimation("icoop/bombFoe.protecting", ANIMATION_DURATION / 3,
                this, anchor, orders, 4, 2, 2, 32, 32,
                false);

        this.currentState = GrenadierState.IDLE;

        this.handler = new GrenadierInteractionHandler();
    }

    @Override
    public void draw(Canvas canvas) {
        if (!isDead()) {
            switch (currentState) {
                case IDLE:
                case ATTACK:
                    idleAnimation.draw(canvas);
                    break;
                case PROTECT:
                    protectingAnimation.draw(canvas);
                    break;
            }
        } else {
            deathAnimation.draw(canvas);
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (idleTimer > 0) {
            idleTimer--;
        } else {
            switch (currentState) {
                case IDLE:
                    moveRandom(2);

                    boolean goIdle = RandomGenerator.getInstance().nextDouble() < 0.1;

                    if (goIdle)
                        idleTimer = RandomGenerator.getInstance().nextInt(48);

                    break;
                case ATTACK:
                    moveToTarget();

                    Vector displacement = target.getPosition().sub(getPosition());

                    if (displacement.getLength() < 2) {
                        placeExplosive();
                        currentState = GrenadierState.PROTECT;
                        protectTimer = RandomGenerator.getInstance().nextInt(75, 200);
                    }

                    break;
                case PROTECT:
                    moveRandom(1);

                    if (protectTimer > 0) {
                        protectTimer--;
                    } else {
                        currentState = GrenadierState.IDLE;
                        idleTimer = RandomGenerator.getInstance().nextInt(48);
                    }

                    break;
            }
        }
    }

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

        /*
         * Use values over Orientation.fromVector method to prevent null pointer exception
         */
        Orientation horizontal = v.x > 0 ? RIGHT : LEFT;
        Orientation vertical = v.y > 0 ? UP : DOWN;

        orientate(Math.abs(v.x) > Math.abs(v.y) ? horizontal : vertical);

        boolean isMoved = move(ANIMATION_DURATION / 3);

        if (!isMoved) {
            orientate(Math.abs(v.x) > Math.abs(v.y) ? vertical : horizontal);
            move(ANIMATION_DURATION / 3);
        }
    }

    private boolean placeExplosive() {
        Explosive explosive = new Explosive(getOwnerArea(), LEFT, getFieldOfViewCells().getFirst(), 100);

        // FIXME: Bombs can be placed in the same tile since they are walkable & does not take cell space
        if (getOwnerArea().canEnterAreaCells(explosive, getFieldOfViewCells())) {
            // FIXME: Prevent activation of bomb immediately after placing
            getOwnerArea().registerActor(explosive);
            explosive.activate();
            return true;
        }

        return false;
    }

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
        other.acceptInteraction(handler, isCellInteraction);
    }

    private enum GrenadierState {
        IDLE,
        PROTECT,
        ATTACK
        ;
    }

    private class GrenadierInteractionHandler implements ICoopInteractionVisitor {
        @Override
        public void interactWith(Interactable other, boolean isCellInteraction) {}

        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            if (currentState == GrenadierState.PROTECT)
                return;

            currentState = GrenadierState.ATTACK;
            target = player;
        }
    }
}
