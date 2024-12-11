package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.ICoop;
import ch.epfl.cs107.icoop.KeyBindings.PlayerKeyBindings;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.icoop.handler.ICoopInventory;
import ch.epfl.cs107.icoop.handler.ICoopItem;
import ch.epfl.cs107.icoop.handler.ICoopPlayerStatusGUI;
import ch.epfl.cs107.icoop.utility.event.DoorTeleportEvent;
import ch.epfl.cs107.icoop.utility.event.DoorTeleportEventArgs;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.areagame.handler.Inventory;
import ch.epfl.cs107.play.areagame.handler.InventoryItem;
import ch.epfl.cs107.play.engine.actor.*;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import static ch.epfl.cs107.play.math.Orientation.*;

import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.*;


/**
 * A ICoopPlayer is a player for the ICoop game.
 */
public class ICoopPlayer extends MovableAreaEntity implements ElementalEntity, Interactor, Inventory.Holder {
    private static final int MAX_LIFE = 10;
    private static final int GRACE_PERIOD = 24;
    private static final int MOVE_DURATION = 8;
    private static final int ANIMATION_DURATION = 4;
    private static final int SWORD_ANIMATION_DURATION = 2;
    private static final int STAFF_ANIMATION_DURATION = 2;

    //private final TextGraphics message;
    private final int id;
    private final String element;
    private final OrientedAnimation idleAnimation;
    private final OrientedAnimation swordAttackAnimation;
    private final OrientedAnimation staffAttackAnimation;
    private final Health health;

    private final ICoopInventory inventory;
    private ICoopItem currentItem;

    private final ICoopPlayerStatusGUI gui;

    private final PlayerKeyBindings keybinds;
    private final DoorTeleportEvent doorTeleportEvent;
    private final ICoopPlayerInteractionHandler handler;

    private DamageType immunityType;
    private boolean isInGracePeriod = false;
    private int gracePeriodTimer = 0;
    private PlayerState currentState;

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

        // Initialize sprite & idleAnimation
        final Orientation[] orders = new Orientation[] { DOWN, RIGHT, UP, LEFT };
        final String prefix = "icoop/" + spriteName;
        final Vector anchor = new Vector(0, 0);

        this.idleAnimation = new OrientedAnimation(
                prefix, ANIMATION_DURATION,
                this, anchor, orders,
                4, 1, 2,
                16, 32, true
        );

        final Vector swordAttackAnimAnchor = new Vector(-.5f, 0);
        this.swordAttackAnimation = new OrientedAnimation(prefix + ".sword",
                SWORD_ANIMATION_DURATION , this ,
                swordAttackAnimAnchor, orders , 4, 2, 2, 32, 32);

        final Vector staffAttackAnimAnchor = new Vector(-.5f, -.20f);
        String name = spriteName.equals("player") ? "player.staff_water" : "player2.staff_water";
        this.staffAttackAnimation = new OrientedAnimation("icoop/" + name, STAFF_ANIMATION_DURATION , this ,
                staffAttackAnimAnchor, orders , 4, 2, 2, 32, 32);

        this.health = new Health(this, Transform.I.translated(0, 1.75f), MAX_LIFE, true);

        this.inventory = new ICoopInventory();
        this.inventory.addPocketItem(ICoopItem.SWORD, 1);
        this.inventory.addPocketItem(ICoopItem.STAFF, 1);
        this.inventory.addPocketItem(ICoopItem.BOMB, 5);

        this.currentItem = ICoopItem.SWORD;

        // FIXME: Temporary placeholder for determining flipped flag
        this.gui = new ICoopPlayerStatusGUI(this, id % 2 == 0);
        this.gui.setCurrentItem(currentItem);

        this.keybinds = keybinds;
        this.handler = new ICoopPlayerInteractionHandler();
        this.doorTeleportEvent = new DoorTeleportEvent();

        resetMotion();

        this.id = id;

        this.immunityType = null;

        this.currentState = PlayerState.IDLE;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        Keyboard keyboard = getOwnerArea().getKeyboard();

        moveIfPressed(LEFT, keyboard.get(keybinds.left()));
        moveIfPressed(UP, keyboard.get(keybinds.up()));
        moveIfPressed(RIGHT, keyboard.get(keybinds.right()));
        moveIfPressed(DOWN, keyboard.get(keybinds.down()));

        if (isInGracePeriod && gracePeriodTimer >= 0) {
            gracePeriodTimer--;
        } else if (gracePeriodTimer < 0) {
            isInGracePeriod = false;
            gracePeriodTimer = 0;
        }

        if (keyboard.get(keybinds.useItem()).isPressed()) {
            boolean success = false;

            switch (currentItem) {
                case BOMB:
                    success = placeExplosive();
                    break;
                case SWORD:
                case STAFF:
                    currentState = PlayerState.ATTACK;
                    break;
                default:
                    break;
            }

            if (currentItem.isConsumable() && success) {
                inventory.removePocketItem(currentItem, 1);
            }

            if (!inventory.contains(currentItem))
                getNextItem();
        }

        if (keyboard.get(keybinds.switchItem()).isPressed())
            getNextItem();

        updateAnimation(deltaTime);
    }

    private boolean placeExplosive() {
        Explosive explosive = new Explosive(getOwnerArea(), LEFT, getFieldOfViewCells().getFirst(), 100);

        // FIXME: Bombs can be placed in the same tile since they are walkable & does not take cell space
        if (getOwnerArea().canEnterAreaCells(explosive, getFieldOfViewCells())) {
            // FIXME: Prevent activation of bomb immediately after placing
            getOwnerArea().registerActor(explosive);
            return true;
        }

        return false;
    }

    private void getNextItem() {
        ICoopItem[] items = ICoopItem.values();
        int index = ICoopItem.getIndex(currentItem);

        for (int i = 0; i < items.length; i++) {
            int x = (i + index + 1) % items.length;

            if (inventory.contains(items[x])) {
                currentItem = items[x];
                break;
            }
        }

        gui.setCurrentItem(currentItem);
    }

    private void updateAnimation(float deltaTime) {
        if (currentState == PlayerState.IDLE) {
            if (isDisplacementOccurs()) {
                if (isInGracePeriod && gracePeriodTimer % 3 != 0)
                    return;

                idleAnimation.update(deltaTime);
            } else {
                idleAnimation.reset();
            }
        } else if (currentState == PlayerState.ATTACK) {
            if (currentItem.equals(ICoopItem.SWORD)) {
                if (swordAttackAnimation.isCompleted()) {
                    swordAttackAnimation.reset();
                    currentState = PlayerState.IDLE;
                } else {
                    swordAttackAnimation.update(deltaTime);
                }
            } else if (currentItem.equals(ICoopItem.STAFF)) {
                if (staffAttackAnimation.isCompleted()) {
                    staffAttackAnimation.reset();
                    currentState = PlayerState.IDLE;
                } else {
                    staffAttackAnimation.update(deltaTime);
                }
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (currentState == PlayerState.IDLE) {
            this.idleAnimation.draw(canvas);
        } if (currentState == PlayerState.ATTACK) {
            if (currentItem.equals(ICoopItem.SWORD))
                this.swordAttackAnimation.draw(canvas);
            else if (currentItem.equals(ICoopItem.STAFF))
                this.staffAttackAnimation.draw(canvas);
        }
        this.health.draw(canvas);
        this.gui.draw(canvas);
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

    public void heal(int restorationFactor) {
        health.increase(restorationFactor);
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

    private void setImmunityType(DamageType damageType) {
        this.immunityType = damageType;
    }

    @Override
    public boolean possess(InventoryItem item) {
        return inventory.contains(item);
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
            if (isCellInteraction) {
                // FIXME: Flatten out if-statements for readability + branching minimization
                if (!explosive.isActivated() && !explosive.isExploded()) {
                    if (!explosive.isCollected())
                        inventory.addPocketItem(ICoopItem.BOMB, 1);

                    explosive.collect();
                }
            } else {
                Keyboard keyboard = getOwnerArea().getKeyboard();
                if (keyboard.get(keybinds.useItem()).isDown() && !explosive.isActivated()) {
                    explosive.activate();
                }
            }
        }

        @Override
        public void interactWith(ElementalWall elementalWall, boolean isCellInteraction) {
            if (isCellInteraction && elementalWall.isOn())
                hit(DamageType.toType(element));
        }

        @Override
        public void interactWith(ElementalItem elementalItem, boolean isCellInteraction) {
            if (isCellInteraction && elementalItem.element().equals(element))
                elementalItem.collect();
        }

        @Override
        public void interactWith(Orb orb, boolean isCellInteraction) {
            if (isCellInteraction && orb.element().equals(element)) {
                // TODO: Verify that orb disappears only after the dialog is completed
                orb.triggerDialog();
                setImmunityType(DamageType.toType(orb.element()));
            }
        }

        @Override
        public void interactWith(Heart heart, boolean isCellInteraction) {
            if (isCellInteraction) {
                heal(heart.getRestorationFactor());
                heart.collect();
            }
        }

        @Override
        public void interactWith(PressurePlate pressurePlate, boolean isCellInteraction) {
            if (isCellInteraction)
                pressurePlate.activate();
        }

        @Override
        public void interactWith(Enemy enemy, boolean isCellInteraction) {
            if (!isCellInteraction && currentState == PlayerState.ATTACK && currentItem.equals(ICoopItem.SWORD))
                enemy.hit(DamageType.PHYSICAL);
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

    private enum PlayerState {
        IDLE,
        ATTACK
        ;
    }
}
