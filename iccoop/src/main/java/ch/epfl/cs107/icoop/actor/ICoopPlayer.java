package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.KeyBindings.PlayerKeyBindings;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.icoop.utility.event.DoorTeleportEvent;
import ch.epfl.cs107.icoop.utility.event.DoorTeleportEventArgs;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.*;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import static ch.epfl.cs107.play.math.Orientation.*;

import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.Collections;
import java.util.List;


/**
 * A ICoopPlayer is a player for the ICoop game.
 */
public class ICoopPlayer extends MovableAreaEntity implements ElementalEntity, Interactor {
    private static final int MAX_LIFE = 10;
    private static final int GRACE_PERIOD = 24;

    private final static int MOVE_DURATION = 8;
    private final static int ANIMATION_DURATION = 4;

    //private final TextGraphics message;
    private final int id;
    private final String element;
    private final OrientedAnimation animation;
    private final String spriteName;
    private final Health health;

    private final PlayerKeyBindings keybinds;
    private final DoorTeleportEvent doorTeleportEvent;
    private final ICoopPlayerInteractionHandler handler;

    private DamageType immunityType;
    private boolean isInGracePeriod = false;
    private int gracePeriodTimer = 0;

    //private String prefix;
    /**
     * Default MovableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public ICoopPlayer(
            Area area,
            Orientation orientation,
            DiscreteCoordinates position,
            String spriteName,
            String element,
            PlayerKeyBindings keybinds,
            int id
    ) {
        // Initialize player properties
        super(area, orientation, position);
        this.element = element;

        /*message = new TextGraphics(Integer.toString((int) hp), 0.4f, Color.BLUE);
        message.setParent(this);
        message.setAnchor(new Vector(-0.3f, 0.1f));*/

        // Initialize sprite & animation
        final Orientation[] orders = new Orientation[] { DOWN, RIGHT, UP, LEFT };
        final String prefix = "icoop/" + spriteName;
        final Vector anchor = new Vector(0, 0);

        this.spriteName = spriteName;
        this.animation = new OrientedAnimation(
                prefix, ANIMATION_DURATION,
                this, anchor, orders,
                4, 1, 2,
                16, 32, true
        );

        this.health = new Health(this, Transform.I.translated(0, 1.75f), MAX_LIFE, true);

        this.keybinds = keybinds;
        this.handler = new ICoopPlayerInteractionHandler();
        this.doorTeleportEvent = new DoorTeleportEvent();

        resetMotion();

        this.id = id;

        this.immunityType = null;
    }

    @Override
    public void update(float deltaTime) {
        Keyboard keyboard = getOwnerArea().getKeyboard();

        moveIfPressed(LEFT, keyboard.get(keybinds.left()));
        moveIfPressed(UP, keyboard.get(keybinds.up()));
        moveIfPressed(RIGHT, keyboard.get(keybinds.right()));
        moveIfPressed(DOWN, keyboard.get(keybinds.down()));

        updateAnimation(deltaTime);

        if (isInGracePeriod && gracePeriodTimer >= 0) {
            gracePeriodTimer--;
        } else if (gracePeriodTimer < 0) {
            isInGracePeriod = false;
            gracePeriodTimer = 0;
        }

        super.update(deltaTime);
    }

    private void updateAnimation(float deltaTime) {
        if (isDisplacementOccurs()) {
            if (isInGracePeriod && gracePeriodTimer % 3 != 0)
                return;

            animation.update(deltaTime);
        } else {
            animation.reset();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        this.animation.draw(canvas);
        this.health.draw(canvas);
    }

    public void enterArea(ICoopArea area, DiscreteCoordinates position) {
        area.registerActor(this);

        setOwnerArea(area);
        setCurrentPosition(position.toVector());

        resetMotion();
    }

    public void leaveArea() {
        getOwnerArea().unregisterActor(this);
    }

    public void centerCamera() { getOwnerArea().setViewCandidate(this); }

    public DoorTeleportEvent getDoorTeleportEvent() { return doorTeleportEvent; }

    public int getId() { return this.id; }

    public boolean isDead() { return this.health.getIntensity() <= 0.0f; }

    public void hit(DamageType damageType) {
        if (damageType.equals(immunityType) || isInGracePeriod)
            return;

        health.decrease(damageType.damage);

        gracePeriodTimer = GRACE_PERIOD;
        isInGracePeriod = true;
    }

    @Override
    public String element() { return this.element; }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
    }

    @Override
    public boolean wantsCellInteraction() { return true; }

    @Override
    public boolean wantsViewInteraction() { return true; }

    @Override
    public boolean takeCellSpace() { return true; }

    @Override
    public boolean isCellInteractable() { return true; }

    @Override
    public boolean isViewInteractable() { return true; }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    private void moveIfPressed(Orientation orientation, Button b) {
        if (b.isDown()) {
            if (!isDisplacementOccurs()) {
                orientate(orientation);
                move(MOVE_DURATION);
            }
        }
    }

    private class ICoopPlayerInteractionHandler implements ICoopInteractionVisitor {
        @Override
        public void interactWith(Interactable other, boolean isCellInteraction) {}

        @Override
        public void interactWith(Door door, boolean isCellInteraction) {
            if (isCellInteraction && door.getSignal().isOn())
                doorTeleportEvent.emit(new DoorTeleportEventArgs(door));
        }

        @Override
        public void interactWith(Explosive explosive, boolean isCellInteraction) {
            Keyboard keyboard = getOwnerArea().getKeyboard();

            if (keyboard.get(keybinds.useItem()).isDown() && !explosive.isActivated())
                explosive.activate();
        }

        @Override
        public void interactWith(ElementalWall elementalWall, boolean isCellInteraction) {
            if (isCellInteraction && !elementalWall.getElementDamage().equals(element)) {
                hit(DamageType.toType(element));
            }
        }
    }

    public enum DamageType {
        PHYSICAL(2, "physical"),
        EXPLOSIVE(5, "explosive"),
        FIRE(1, "feu"),
        WATER(1, "eau"),
        NONE(0, "none")
        ;

        final int damage;
        final String damageName;
        DamageType(int damage, String damageName) {
            this.damage = damage;
            this.damageName = damageName;
        }

        public static DamageType toType(String damageName) {
            for (DamageType damageType : DamageType.values()) {
                if (damageType.damageName.equals(damageName))
                    return damageType;
            }

            return NONE;
        }
    }
}
