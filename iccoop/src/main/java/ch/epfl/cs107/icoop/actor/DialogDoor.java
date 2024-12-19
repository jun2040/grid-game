package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;

import java.util.List;

/**
 * DialogDoor is a special type of Door that interacts with the player by showing context-specific dialogs
 * depending on its open or closed state. It can only interact with ICoopPlayer instances.
 */
public class DialogDoor extends Door implements Interactor {
    // Path to the dialog resource shown when the door is opened
    private final String openedDialogPath;
    // Path to the dialog resource shown when the door is closed
    private final String closedDialogPath;

    // Dialog displayed when the door is open
    private Dialog openedDialog;
    // Dialog displayed when the door is closed
    private Dialog closedDialog;

    // Handler responsible for managing and pushing dialogs to the player
    private final DialogHandler dialogHandler;
    // Interaction handler specific to the DialogDoor, managing interaction with the player
    private final DialogDoorInteractionHandler interactionHandler;

    /**
     * Constructs a DialogDoor instance
     *
     * @param area                (Area): Owner area. Not null
     * @param orientation         (Orientation): Initial orientation of the entity in the Area. Not null
     * @param isOpen              (boolean): Initial open/closed status of door. Not null
     * @param mainPosition        (DiscreteCoordinates): Occupied main cell of door. Not null
     * @param dialogHandler       (DialogHandler): Dialog manager interface, for pushing dialogs. Not null
     * @param openedDialogPath    (String): Resource path String for opened dialog. Not null
     * @param closedDialogPath    (String): Resource path String for closed dialog. Not null
     * @param destinationAreaName (String): Door destination area name. Not null
     * @param targetCoords        (DiscreteCoordinates): Destination spawning coordinates. Not null
     */
    public DialogDoor(
            Area area,
            Orientation orientation,
            boolean isOpen,
            DiscreteCoordinates mainPosition,
            DialogHandler dialogHandler,
            String openedDialogPath,
            String closedDialogPath,
            String destinationAreaName,
            DiscreteCoordinates[] targetCoords
    ) {
        super(area, orientation, destinationAreaName, isOpen, targetCoords, mainPosition);

        this.openedDialogPath = openedDialogPath;
        this.closedDialogPath = closedDialogPath;

        this.dialogHandler = dialogHandler;
        this.interactionHandler = new DialogDoorInteractionHandler();
    }

    /**
     * Gets the cells in the field of view of the door. This door does not have a field of view, so it returns an empty list.
     *
     * @return an empty list of coordinates
     */
    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return List.of();
    }

    /**
     * Indicates that the door wants to interact with other entities in the same cell.
     *
     * @return true, since the door wants cell interactions
     */
    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    /**
     * Indicates that the door does not want to interact with entities in its field of view.
     *
     * @return false, since the door does not require view interactions
     */
    @Override
    public boolean wantsViewInteraction() {
        return false;
    }

    /**
     * Handles interaction with another Interactable entity.
     *
     * @param other             (Interactable): The other entity involved in the interaction. Not null
     * @param isCellInteraction (boolean): True if the interaction is within the same cell
     */
    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(interactionHandler, isCellInteraction);
    }

    /**
     * Handles interactions specific to the DialogDoor.
     * Reinitializes dialogs on player entry and publishes the appropriate dialog based on the door's state.
     */
    private class DialogDoorInteractionHandler implements ICoopInteractionVisitor {
        /**
         * Handles general interactions. Currently does nothing.
         *
         * @param other             (Interactable): The other entity involved in the interaction. Not null
         * @param isCellInteraction (boolean): True if the interaction is within the same cell
         */
        @Override
        public void interactWith(Interactable other, boolean isCellInteraction) {
        }

        /**
         * Handles interactions specifically with ICoopPlayer entities.
         *
         * @param player            (ICoopPlayer): The player entity involved in the interaction. Not null
         * @param isCellInteraction (boolean): True if the interaction is within the same cell
         */
        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            // Reinitialize dialog on entering
            if (player.isDisplacementOccurs()) {
                openedDialog = new Dialog(openedDialogPath);
                closedDialog = new Dialog(closedDialogPath);
            }

            // Publish the appropriate dialog based on the door's state
            if (isCellInteraction && !player.isDisplacementOccurs()) {
                if (isOn()) {
                    dialogHandler.publish(openedDialog);
                } else {
                    dialogHandler.publish(closedDialog);
                }
            }
        }
    }
}
