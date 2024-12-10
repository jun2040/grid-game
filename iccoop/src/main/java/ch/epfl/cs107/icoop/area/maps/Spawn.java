package ch.epfl.cs107.icoop.area.maps;

import ch.epfl.cs107.icoop.actor.*;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

import static ch.epfl.cs107.play.math.Orientation.*;

public class Spawn extends ICoopArea {
    public static DiscreteCoordinates[] SPAWN_POINTS =
            new DiscreteCoordinates[] { new DiscreteCoordinates(11, 6), new DiscreteCoordinates(13, 6) };
    public static DiscreteCoordinates[] ARRIVAL_POINTS =
            new DiscreteCoordinates[] { new DiscreteCoordinates(18, 16), new DiscreteCoordinates(18, 15) };

    private DialogHandler dialogHandler;
    private boolean isDirty = false;

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (dialogHandler != null && !isDirty) {
            dialogHandler.publish(new Dialog("welcome"));
            isDirty = true;
        }
    }

    @Override
    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));
        registerActor(new Door(
                this, LEFT, "OrbWay", Logic.TRUE,
                OrbWay.ARRIVAL_POINTS,
                new DiscreteCoordinates(19, 15),
                new DiscreteCoordinates(19, 16)
                ));

        registerActor(new Rock(this, LEFT, new DiscreteCoordinates(9, 9)));
        registerActor(new Rock(this, LEFT, new DiscreteCoordinates(11, 9)));
        registerActor(new Rock(this, LEFT, new DiscreteCoordinates(10, 10)));
        registerActor(new Explosive(this, LEFT, new DiscreteCoordinates(10, 9), 100));
    }

    @Override
    public boolean isViewCentered() { return true; }

    @Override
    public DiscreteCoordinates getPlayerSpawnPosition(int id) { return SPAWN_POINTS[id];}

    @Override
    public String getTitle() { return "Spawn"; }

    // FIXME: Consider using constructor initialization if the handler does not need to be set again
    public void setDialogHandler(DialogHandler dialogHandler) { this.dialogHandler = dialogHandler; }
}
