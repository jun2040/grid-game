package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.area.ICoopArea;
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
    private final DialogHandler dialogHandler;
    private Dialog openedDialog;
    private Dialog closedDialog;
    private final String openedDialogString;
    private final String closedDialogString;

    private final DialogDoorInteractionHandler interactionHandler;

    /**
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param isOpen      (boolean): Initial open/closed status of door. Not null
     * @param mainPosition  (DiscreteCoordinates): Occupied main cell of door. Not null
     * @param dialogHandler (DialogHandler): Dialog manager interface, for pushing dialogs. Not null
     * @param openedDialogPath  (String): Ressource path String. Not null
     * @param closedDialogPath  (String): Ressource path String. Not null
     * @param destinationAreaName   (String): Door destination area name.  Not null
     * @param targetCoords  (DiscreteCoordinates): Destination spawning coordinates. Not null
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
        super(area, orientation,destinationAreaName, isOpen, targetCoords, mainPosition);

        this.dialogHandler = dialogHandler;
        this.openedDialogString = openedDialogPath;
        this.closedDialogString = closedDialogPath;

        openedDialog = new Dialog(openedDialogString);
        closedDialog = new Dialog(closedDialogString);

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

    @Override
    public Orientation getDestinationOrientation() {
        return null;
    }

    /**
     * DialogInteractionHandler: activates event when foreign actor interacts with it
     */
    private class DialogDoorInteractionHandler implements ICoopInteractionVisitor {
        @Override
        public void interactWith(Interactable other, boolean isCellInteraction) {}

        /**
         *
         * @param player
         * @param isCellInteraction
         *
         * Description: creates new dialogs when first entering cell, then pushes them to Dialog Handler.
         *              destruction then creation necessary to prevent soft lock form dialog reopening.
         */
        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            if(player.isDisplacementOccurs()){
                openedDialog = new Dialog(openedDialogString);
                closedDialog = new Dialog(closedDialogString);
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
