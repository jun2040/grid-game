package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.KeyBindings.PlayerKeyBindings;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.handler.*;
import ch.epfl.cs107.icoop.utility.Timer;
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

import java.util.Collections;
import java.util.List;

/**
 * A ICoopPlayer is a player for the ICoop game.
 */
public class ICoopPlayer extends MovableAreaEntity implements ElementalEntity, Interactor, Inventory.Holder {
    private static final int MAX_LIFE = 10;
    private static final int MOVE_DURATION = 8;
    private static final int ANIMATION_DURATION = 4;
    private static final int SWORD_ANIMATION_DURATION = 2;
    private static final int STAFF_ANIMATION_DURATION = 2;

    private static final float GRACE_PERIOD = 2.0f;

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
    private final ICoopPlayerInteractionHandler handler;
    private final TeleportController teleportController;

    private DamageType immunityType;
    private final Timer gracePeriodTimer;
    private PlayerState currentState;
    private boolean canFire = true;

    //private String prefix;
    /**
     * Default MovableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     * @param spriteName (String) : sprite name used to get sprite. Not null
     * @param element      (String) : element name associated to each player. Not null
     * @param keybinds      (Keybinds): different Keybinds used by each seperate player. Not null
     * @param teleportController (TeleportController) : class used to dictate teleportation behavior with interacting with doors. Not null
     * @param id
     */
    public ICoopPlayer(
            Area area,
            Orientation orientation,
            DiscreteCoordinates position,
            String spriteName,
            String element,
            PlayerKeyBindings keybinds,
            TeleportController teleportController,
            int id
    ) {
        // Initialize player properties
        super(area, orientation, position);
        this.element = element;

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

        final Orientation[] attackAnimOrders = new Orientation[] { DOWN, UP, RIGHT, LEFT };
        final Vector swordAttackAnimAnchor = new Vector(-.5f, 0);
        this.swordAttackAnimation = new OrientedAnimation(prefix + ".sword",
                SWORD_ANIMATION_DURATION , this ,
                swordAttackAnimAnchor, attackAnimOrders, 4, 2, 2, 32, 32);

        final Vector staffAttackAnimAnchor = new Vector(-.5f, -.20f);
        String name = spriteName.equals("player") ? "player.staff_fire" : "player2.staff_water";
        this.staffAttackAnimation = new OrientedAnimation("icoop/" + name, STAFF_ANIMATION_DURATION , this ,
                staffAttackAnimAnchor, attackAnimOrders, 4, 2, 2, 32, 32);

        this.health = new Health(this, Transform.I.translated(0, 1.75f), MAX_LIFE, true);

        this.inventory = new ICoopInventory();
        this.inventory.addPocketItem(ICoopItem.SWORD, 1);
        this.inventory.addPocketItem(ICoopItem.STAFF_FIRE, 1);

        this.currentItem = ICoopItem.SWORD;

        this.gracePeriodTimer = new Timer();

        // FIXME: Temporary placeholder for determining flipped flag
        this.gui = new ICoopPlayerStatusGUI(this, id % 2 == 0);
        this.gui.setCurrentItem(currentItem);

        this.keybinds = keybinds;
        this.handler = new ICoopPlayerInteractionHandler();

        resetMotion();

        this.teleportController = teleportController;

        this.id = id;

        this.immunityType = null;

        this.currentState = PlayerState.IDLE;
    }

    /**
     *
     * @param orientation, sets orientation of player, used when entering areas
     */
    public void setOrienation(Orientation orientation){
        orientate(orientation);
    }

    /**
     *
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     *
     *  Description : checks key inputs to move, invulnerability to damage,
     *                  key inputs for use of items in inventory, with each inventory item having a different function
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        Keyboard keyboard = getOwnerArea().getKeyboard();

        moveIfPressed(LEFT, keyboard.get(keybinds.left()));
        moveIfPressed(UP, keyboard.get(keybinds.up()));
        moveIfPressed(RIGHT, keyboard.get(keybinds.right()));
        moveIfPressed(DOWN, keyboard.get(keybinds.down()));

        gracePeriodTimer.update(deltaTime);

        if (staffAttackAnimation.isCompleted())
            canFire = true;

        if (keyboard.get(keybinds.useItem()).isPressed()) {
            boolean success = false;

            switch (currentItem) {
                case BOMB:
                    success = placeExplosive();
                    break;
                case SWORD:
                    currentState = PlayerState.ATTACK;
                    break;
                case STAFF_WATER:
                case STAFF_FIRE:
                    if (canFire) {
                        getOwnerArea().registerActor(
                                new ElementalProjectile(
                                        getOwnerArea(), getOrientation(),
                                        getFieldOfViewCells().getFirst(),
                                        5, 50, ElementType.fromString(element)
                                ));
                        canFire = false;
                    }

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

    /**
     *
     * @return true if placing a bomb was a success
     */
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

    /**
     * switches to next current usable item
     */
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

    /**
     *
     * @param deltaTime
     * Description : Updates animation depending on the state of the player
     *              2 states : idle, attack
     *
     */
    private void updateAnimation(float deltaTime) {
        if (currentState == PlayerState.IDLE) {
            if (isDisplacementOccurs())
                idleAnimation.update(deltaTime);
            else
                idleAnimation.reset();
        } else if (currentState == PlayerState.ATTACK) {
            if (currentItem.equals(ICoopItem.SWORD)) {
                if (swordAttackAnimation.isCompleted()) {
                    swordAttackAnimation.reset();
                    currentState = PlayerState.IDLE;
                } else {
                    swordAttackAnimation.update(deltaTime);
                }
            } else if (currentItem.equals(ICoopItem.STAFF_WATER)||currentItem.equals(ICoopItem.STAFF_FIRE)) {
                if (staffAttackAnimation.isCompleted()) {
                    staffAttackAnimation.reset();
                    currentState = PlayerState.IDLE;
                } else {
                    staffAttackAnimation.update(deltaTime);
                }
            }
        }
    }

    /**
     *
     * @param canvas target, not null
     *    Description : will draw the current player state animation
     */

    @Override
    public void draw(Canvas canvas) {
        if (gracePeriodTimer.isCompleted()) {
            drawPlayerAnimation(canvas);
        } else {
            int frame = (int) (gracePeriodTimer.getTime() * 10);
            if (frame % 2 == 0)
                drawPlayerAnimation(canvas);
        }

        this.health.draw(canvas);
        this.gui.draw(canvas);
    }

    public void drawPlayerAnimation(Canvas canvas) {
        switch (currentState) {
            case IDLE:
                this.idleAnimation.draw(canvas);
                break;
            case ATTACK:
                switch (currentItem) {
                    case SWORD:
                        this.swordAttackAnimation.draw(canvas);
                        break;
                    case STAFF_WATER:
                    case STAFF_FIRE:
                        this.staffAttackAnimation.draw(canvas);
                        break;
                }
                break;
        }
    }

    /**
     *
     * @param area
     * @param position
     * Description : will register actor to a new area and reset its position and motion
     */
    public void enterArea(ICoopArea area, DiscreteCoordinates position) {
        area.registerActor(this);

        setOwnerArea(area);
        setCurrentPosition(position.toVector());

        resetMotion();
    }

    /**
     * leaves area through unregistering
     */
    public void leaveArea() {
        getOwnerArea().unregisterActor(this);
    }

    /**
     * centers camera on player
     */
    public void centerCamera() { getOwnerArea().setViewCandidate(this); }

    /**
     *
     * @return player id
     */
    public int getId() { return this.id; }
    /**
     * fully fills health bar
     */
    public void resetHealth(){
        health.resetHealth();
    }

    /**
     *
     * @return boolean true if player's health is <= 0
     */
    public boolean isDead() { return this.health.getIntensity() <= 0.0f; }

    /**
     *
     * @param damageType, is used when damage is dealt to player from a foe
     *                    will decrease its health bar
     */
    public void hit(DamageType damageType) {
        if (damageType.equals(immunityType) || !gracePeriodTimer.isCompleted())
            return;

        health.decrease(damageType.damage);

        gracePeriodTimer.setTimer(GRACE_PERIOD);
    }

    /**
     *
     * @param restorationFactor will heal the player by the restoration factor
     */
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
    public boolean isDisplacementOccurs() {
        return super.isDisplacementOccurs();
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }
    /**
     * will move the player in the orienation given by the keybinds
     */
    private void moveIfPressed(Orientation orientation, Button b) {
        if (b.isDown()) {
            if (!isDisplacementOccurs()) {
                orientate(orientation);
                move(MOVE_DURATION);
            }
        }
    }

    /**
     *
     * @param damageType sets immunity to this damage type
     */
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
            if (isCellInteraction && door.teleportable() && door.isOn())
                teleportController.setTeleport(door);
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
                if (keyboard.get(keybinds.useItem()).isPressed() && !explosive.isActivated()) {
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
            if (isCellInteraction && orb.element().equals(element) && !orb.isCollected()) {
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
        @Override
        public void interactWith(Chest chest, boolean isCellInteraction){
            Keyboard keyboard = getOwnerArea().getKeyboard();
            if (keyboard.get(keybinds.useItem()).isDown() && !chest.isOpen()) {
                chest.open();
                chest.giftItem(inventory);
            }
        }

        @Override
        public void interactWith(Grass grass, boolean isCellInteraction){
            Keyboard keyboard = getOwnerArea().getKeyboard();
            if (keyboard.get(keybinds.useItem()).isDown() && !grass.isDestroyed()) {
                grass.destroy();
            }
        }

        @Override
        public void interactWith(Coin coin, boolean isCellInteraction){
            if(isCellInteraction){
                coin.collect();
            }
        }

        @Override
        public void interactWith(Staff staff, boolean isCellInteraction){
            if(isCellInteraction){
                staff.collect();
                if(staff.element().equals(ElementType.WATER.getName()))
                    inventory.addPocketItem(ICoopItem.STAFF_WATER, 1);
                else
                    inventory.addPocketItem(ICoopItem.STAFF_FIRE, 1);
            }

        }

        @Override
        public void interactWith(Key key, boolean isCellInteraction) {
            key.collect();
        }
    }

    /**
     * enum associates damage type to the damage dealt, and its name
     */
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
