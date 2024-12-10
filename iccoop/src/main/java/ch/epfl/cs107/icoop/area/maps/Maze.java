package ch.epfl.cs107.icoop.area.maps;

import ch.epfl.cs107.icoop.actor.HellSkull;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

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

    @Override
    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));

        for (DiscreteCoordinates coord : hellSkullSpawn) {
            registerActor(new HellSkull(this, RIGHT, coord));
        }
    }

    @Override
    public boolean isViewCentered() {
        return true;
    }

    @Override
    public DiscreteCoordinates getPlayerSpawnPosition(int id) {
        return new DiscreteCoordinates(0, 0);
    }

    @Override
    public String getTitle() {
        return "Maze";
    }
}
