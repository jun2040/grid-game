package ch.epfl.cs107.icoop.handler;

public class Context {
    private final DialogHandler dialogHandler;

    public Context(DialogHandler dialogHandler) {
        this.dialogHandler = dialogHandler;
    }

    public DialogHandler getDialogHandler() {
        return dialogHandler;
    }
}
