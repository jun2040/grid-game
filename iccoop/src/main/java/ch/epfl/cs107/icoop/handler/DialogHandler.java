package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.play.engine.actor.Dialog;

/**
 * The DialogHandler interface provides a mechanism for actors to publish dialogs.
 */
public interface DialogHandler {

    /**
     * Publishes a dialog, allowing encapsulated entities to communicate messages or interactions.
     *
     * @param dialog (Dialog): The dialog to be published. Not null.
     */
    void publish(Dialog dialog);
}
