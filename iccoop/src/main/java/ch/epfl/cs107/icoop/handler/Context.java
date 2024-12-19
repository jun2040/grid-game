package ch.epfl.cs107.icoop.handler;

/**
 * The Context class encapsulates a DialogHandler to facilitate its passing and management across entities.
 */
public class Context {

    /** The DialogHandler instance associated with this context. */
    private final DialogHandler dialogHandler;

    /**
     * Constructs a Context instance with the specified DialogHandler.
     *
     * @param dialogHandler (DialogHandler): The DialogHandler to be encapsulated. Not null.
     */
    public Context(DialogHandler dialogHandler) {
        this.dialogHandler = dialogHandler;
    }

    /**
     * Retrieves the DialogHandler associated with this context.
     *
     * @return (DialogHandler): The encapsulated DialogHandler.
     */
    public DialogHandler getDialogHandler() {
        return dialogHandler;
    }
}
