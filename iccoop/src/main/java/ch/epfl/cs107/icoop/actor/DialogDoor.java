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

public class DialogDoor extends Door implements Interactor {
    private final String openedDialogPath;
    private final String closedDialogPath;

    private Dialog openedDialog;
    private Dialog closedDialog;

    private final DialogHandler dialogHandler;
    private final DialogDoorInteractionHandler interactionHandler;

    /**
     * @param area                (Area): Owner area. Not null
     * @param orientation         (Orientation): Initial orientation of the entity in the Area. Not null
     * @param isOpen              (boolean): Initial open/closed status of door. Not null
     * @param mainPosition        (DiscreteCoordinates): Occupied main cell of door. Not null
     * @param dialogHandler       (DialogHandler): Dialog manager interface, for pushing dialogs. Not null
     * @param openedDialogPath    (String): Resource path String. Not null
     * @param closedDialogPath    (String): Resource path String. Not null
     * @param destinationAreaName (String): Door destination area name.  Not null
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

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return List.of();
    }

    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {
        return false;
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(interactionHandler, isCellInteraction);
    }

    private class DialogDoorInteractionHandler implements ICoopInteractionVisitor {
        @Override
        public void interactWith(Interactable other, boolean isCellInteraction) {
        }

        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            // Reinitialize dialog on entering
            if (player.isDisplacementOccurs()) {
                openedDialog = new Dialog(openedDialogPath);
                closedDialog = new Dialog(closedDialogPath);
            }

            if (isCellInteraction && !player.isDisplacementOccurs()) {
                if (isOn())
                    dialogHandler.publish(openedDialog);
                else
                    dialogHandler.publish(closedDialog);
            }
        }
    }
}
