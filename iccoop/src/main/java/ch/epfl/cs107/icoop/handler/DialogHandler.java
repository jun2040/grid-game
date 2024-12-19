package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.play.engine.actor.Dialog;

public interface DialogHandler {
    /**
     *
     * @param dialog, interface for actors to publish dialogs while encapsulated
     */
    void publish(Dialog dialog);
}
