package ch.epfl.cs107.icoop.handler;

public class Context {
    private final DialogHandler dialogHandler;

    /**
     * @param dialogHandler encapsulation for passing through the dialog handler to other entities
     */
    public Context(DialogHandler dialogHandler) {
        this.dialogHandler = dialogHandler;
    }

    public DialogHandler getDialogHandler() {
        return dialogHandler;
    }
}
