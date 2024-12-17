package ch.epfl.cs107.icoop.area.maps;

import ch.epfl.cs107.icoop.actor.*;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

import static ch.epfl.cs107.play.math.Orientation.*;

public class Maze extends ICoopArea {
    public static DiscreteCoordinates[] ARRIVAL_POINTS =
            new DiscreteCoordinates[] { new DiscreteCoordinates(2, 39), new DiscreteCoordinates(3, 39) };

    private static final DiscreteCoordinates[] hellSkullSpawn = new DiscreteCoordinates[] {
            new DiscreteCoordinates(12, 33), new DiscreteCoordinates(12, 31),
            new DiscreteCoordinates(12, 29), new DiscreteCoordinates(12, 27),
            new DiscreteCoordinates(12, 25), new DiscreteCoordinates(10, 33),
            new DiscreteCoordinates(10, 32), new DiscreteCoordinates(10, 30),
            new DiscreteCoordinates(10, 28), new DiscreteCoordinates(10, 26)
    };

    private static final DiscreteCoordinates[] grenadierSpawn = new DiscreteCoordinates[] {
            new DiscreteCoordinates(5, 15), new DiscreteCoordinates(6, 17),
            new DiscreteCoordinates(10, 17), new DiscreteCoordinates(5, 14)
    };

    @Override
    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));

        for (DiscreteCoordinates coord : hellSkullSpawn)
            registerActor(new HellSkull(this, RIGHT, coord));

        for (DiscreteCoordinates coord : grenadierSpawn)
            registerActor(new Grenadier(this, RIGHT, coord));

        registerActor(new Staff(this, new DiscreteCoordinates(13, 2), ElementType.FIRE, "staff_fire"));
        registerActor(new Staff(this, new DiscreteCoordinates(8, 2), ElementType.WATER, "staff_water"));

        registerActor(new Door(
                this, UP, "Arena", Logic.TRUE,
                Arena.ARRIVAL_POINTS,
                new DiscreteCoordinates(19, 6),
                new DiscreteCoordinates(19, 7)
        ));
    }

    @Override
    public boolean isViewCentered() {
        return true;
    }

    @Override
    public DiscreteCoordinates getPlayerSpawnPosition(int id) {
        return ARRIVAL_POINTS[id];
    }

    @Override
    public String getTitle() {
        return "Maze";
    }
}
