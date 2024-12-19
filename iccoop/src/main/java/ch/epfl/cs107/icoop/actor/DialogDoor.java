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
    private final Dialog openedDialog;
    private final Dialog closedDialog;

    private final DialogDoorInteractionHandler interactionHandler;

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
        this.openedDialog = new Dialog(openedDialogPath);
        this.closedDialog = new Dialog(closedDialogPath);

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

    private class DialogDoorInteractionHandler implements ICoopInteractionVisitor {
        @Override
        public void interactWith(Interactable other, boolean isCellInteraction) {}

        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            if (isCellInteraction && !player.isDisplacementOccurs()) {
                if (isOn())
                    dialogHandler.publish(openedDialog);
                else
                    dialogHandler.publish(closedDialog);
            }
        }
    }
}
