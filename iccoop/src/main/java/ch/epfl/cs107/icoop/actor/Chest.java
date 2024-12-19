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

public class Chest extends Container implements Logic {
    private final static int ANIMATION_DURATION = 10;

    private final Animation openingAnimation;

    private final List<ICoopItem> items = new ArrayList<>();

    private boolean isOpen;
    private boolean completedDialog = false;

    private final DialogHandler dialogHandler;

    /**
     * Default AreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     * @param dialog      (DialogHandler): Mediates the dialog creation. Sends to ICoop. Not null
     * @param isOpen      (Logic): True when the chest is opened. Not null
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

        this.items.add(ICoopItem.BOMB);
    }

    @Override
    public void draw(Canvas canvas) {
        openingAnimation.draw(canvas);
    }

    /**
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     *                  updates animation if opened. Once complete pushes new dialog to DialogHandler
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
     * @return opened state
     */
    public boolean isOpen() {
        return this.isOpen;
    }

    /**
     * opens chest, sets TRUE
     */
    public void open() {
        this.isOpen = true;
    }

    /**
     * @param inventory player inventory, to which the contained collectible(s) is/are added
     */
    public void giftItem(ICoopInventory inventory) {
        for (ICoopItem item : items)
            inventory.addPocketItem(item, 5);
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    @Override
    public boolean isOn() {
        return isOpen;
    }

    @Override
    public boolean isOff() {
        return !isOpen;
    }
}
