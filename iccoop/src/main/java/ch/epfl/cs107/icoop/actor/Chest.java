package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.icoop.handler.ICoopInventory;
import ch.epfl.cs107.icoop.handler.ICoopItem;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Chest in the game world that can store items, interact with players, and display animations.
 * The chest can be opened, and when opened, it displays an animation and provides items to the player's inventory.
 */
public class Chest extends Container implements Logic {

    /**
     * Duration of the chest's opening animation in frames
     */
    private final static int ANIMATION_DURATION = 10;

    /**
     * Animation displayed when the chest is being opened
     */
    private final Animation openingAnimation;

    /**
     * List of items stored inside the chest
     */
    private final List<ICoopItem> items = new ArrayList<>();

    /**
     * Indicates whether the chest is open
     */
    private boolean isOpen;

    /**
     * Indicates whether the dialog associated with the chest has been completed
     */
    private boolean completedDialog = false;

    /**
     * Handles dialogs triggered by the chest
     */
    private final DialogHandler dialogHandler;

    /**
     * Constructs a Chest entity in the game.
     *
     * @param area        (Area): The area to which the chest belongs. Not null.
     * @param orientation (Orientation): The initial orientation of the chest. Not null.
     * @param position    (DiscreteCoordinates): The initial position of the chest. Not null.
     * @param isOpen      (boolean): True if the chest is initially open, false otherwise.
     * @param dialog      (DialogHandler): Mediates the dialog creation. Sends to ICoop. Not null.
     */
    public Chest(Area area, Orientation orientation, DiscreteCoordinates position, boolean isOpen, DialogHandler dialog) {
        super(area, orientation, position);

        this.isOpen = isOpen;
        this.dialogHandler = dialog;
        this.openingAnimation = new Animation(
                "external/fantasy_chest_animation",
                12, 1.5f, 1.5f,
                this, 32, 32,
                ANIMATION_DURATION / 8, false);

        // Initialize the chest with a bomb item
        this.items.add(ICoopItem.BOMB);
    }

    /**
     * Draws the chest's animation on the canvas.
     *
     * @param canvas (Canvas): The canvas to draw on. Not null.
     */
    @Override
    public void draw(Canvas canvas) {
        openingAnimation.draw(canvas);
    }

    /**
     * Updates the chest's state. If the chest is open, the animation is updated.
     * When the animation completes, it triggers a dialog.
     *
     * @param deltaTime (float): Elapsed time since the last update, in seconds. Non-negative.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (isOpen)
            openingAnimation.update(deltaTime);

        if (openingAnimation.isCompleted() && !completedDialog) {
            dialogHandler.publish(new Dialog("first_item"));
            completedDialog = true;
        }
    }

    /**
     * Checks if the chest is open.
     *
     * @return (boolean): True if the chest is open, false otherwise.
     */
    public boolean isOpen() {
        return this.isOpen;
    }

    /**
     * Opens the chest by setting its state to open.
     */
    public void open() {
        this.isOpen = true;
    }

    /**
     * Transfers the items in the chest to the player's inventory.
     *
     * @param inventory (ICoopInventory): The player's inventory where items will be added. Not null.
     */
    public void giftItem(ICoopInventory inventory) {
        for (ICoopItem item : items)
            inventory.addPocketItem(item, 5);
    }

    /**
     * Accepts an interaction with a visitor, delegating the behavior to the interaction handler.
     *
     * @param v                 (AreaInteractionVisitor): The visitor handling the interaction. Not null.
     * @param isCellInteraction (boolean): True if the interaction is cell-based.
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    /**
     * Checks if the chest is logically "on" (open).
     *
     * @return (boolean): True if the chest is open, false otherwise.
     */
    @Override
    public boolean isOn() {
        return isOpen;
    }

    /**
     * Checks if the chest is logically "off" (closed).
     *
     * @return (boolean): True if the chest is closed, false otherwise.
     */
    @Override
    public boolean isOff() {
        return !isOpen;
    }
}
