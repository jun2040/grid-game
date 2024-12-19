package ch.epfl.cs107.icoop.area.maps;

import ch.epfl.cs107.icoop.actor.*;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.handler.Context;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;

import static ch.epfl.cs107.play.math.Orientation.*;

public class Spawn extends ICoopArea {
    public static DiscreteCoordinates[] SPAWN_POINTS =
            new DiscreteCoordinates[]{new DiscreteCoordinates(11, 6), new DiscreteCoordinates(13, 6)};
    public static DiscreteCoordinates[] ARRIVAL_POINTS =
            new DiscreteCoordinates[]{new DiscreteCoordinates(18, 16), new DiscreteCoordinates(18, 15)};
    public static DiscreteCoordinates[] TEMP_ARRIVAL_POINTS =
            new DiscreteCoordinates[]{new DiscreteCoordinates(10, 0), new DiscreteCoordinates(9, 0)};

    public static final Orientation SPAWN_ORIENTATION = DOWN;
    public static final Orientation TOP_RIGHT_ORIENTATION = LEFT;
    private boolean isDirty = false;

    private DialogDoor manorDoor;

    public Spawn(Context context) {
        super(context);
    }

    @Override
    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));

        registerActor(new Door(
                this, LEFT, "OrbWay", true,
                OrbWay.ARRIVAL_POINTS,
                new DiscreteCoordinates(19, 15),
                new DiscreteCoordinates(19, 16)
        ));
        registerActor(new Door(
                this, UP, "Maze", true,
                Maze.ARRIVAL_POINTS,
                new DiscreteCoordinates(4, 0),
                new DiscreteCoordinates(5, 0)
        ));

        manorDoor = new DialogDoor(
                this, DOWN, false,
                new DiscreteCoordinates(6, 11),
                getContext().getDialogHandler(),
                "victory", "key_required","SanctumEntrance", SanctumEntrance.ARRIVAL_POINTS
        );

        registerActor(manorDoor);

        registerActor(new Rock(this, LEFT, new DiscreteCoordinates(7, 7)));
        registerActor(new Rock(this, LEFT, new DiscreteCoordinates(9, 7)));
        registerActor(new Rock(this, LEFT, new DiscreteCoordinates(8, 8)));

        registerActor(new Explosive(this, LEFT, new DiscreteCoordinates(8, 7), 3));

        registerActor(new Chest(this, UP, new DiscreteCoordinates(13, 16), false, getContext().getDialogHandler()));

        for (int i = 0; i < 8; i++) {
            registerActor(new Grass(this, UP, new DiscreteCoordinates(12, 8+i)));
            registerActor(new Grass(this, UP, new DiscreteCoordinates(14, 8+i)));
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (!isDirty) {
            getContext().getDialogHandler().publish(new Dialog("welcome"));
            isDirty = true;
        }

        if (isOn())
            manorDoor.open();
    }

    @Override
    public boolean isViewCentered() {
        return true;
    }

    @Override
    public DiscreteCoordinates getPlayerSpawnPosition(int id) {
        return SPAWN_POINTS[id];
    }

    @Override
    public Orientation getSpawnOrientation() {
        return OrbWay.SPAWN_ORIENTATION;
    }

    @Override
    public String getTitle() {
        return "Spawn";
    }
}
