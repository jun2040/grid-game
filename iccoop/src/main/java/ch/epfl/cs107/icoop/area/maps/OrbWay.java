package ch.epfl.cs107.icoop.area.maps;

import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

import static ch.epfl.cs107.play.math.Orientation.*;

public class OrbWay extends ICoopArea {
    private final DiscreteCoordinates[] coordsDoor1 = new DiscreteCoordinates[] {new DiscreteCoordinates(0,13),new DiscreteCoordinates(0,12),new DiscreteCoordinates(0,11), new DiscreteCoordinates(0,10)};
    private final DiscreteCoordinates[] coordsDoor2 = new DiscreteCoordinates[] {new DiscreteCoordinates(0,7), new DiscreteCoordinates(0,6), new DiscreteCoordinates(0,5), new DiscreteCoordinates(0,4)};
    private final DiscreteCoordinates[] destinationCoords = new DiscreteCoordinates[] {new DiscreteCoordinates(18,16), new DiscreteCoordinates(18,15)};
    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));
//        registerActor(new Door(this, Orientation.RIGHT, new DiscreteCoordinates(0,14), "Spawn", coordsDoor1 , Logic.TRUE, destinationCoords ));
//        registerActor(new Door(this, Orientation.RIGHT, new DiscreteCoordinates(0,8), "Spawn", coordsDoor2 , Logic.TRUE, destinationCoords ));
        registerActor(new Door(
                this, RIGHT, "Spawn", Logic.TRUE,
                new DiscreteCoordinates[] { new DiscreteCoordinates(18, 16), new DiscreteCoordinates(18, 15) },
                new DiscreteCoordinates(0, 14),
                new DiscreteCoordinates(0, 13),
                new DiscreteCoordinates(0, 12),
                new DiscreteCoordinates(0, 11),
                new DiscreteCoordinates(0, 10)
        ));
        registerActor(new Door(
                this, RIGHT, "Spawn", Logic.TRUE,
                new DiscreteCoordinates[] { new DiscreteCoordinates(18, 16), new DiscreteCoordinates(18, 15) },
                new DiscreteCoordinates(0, 8),
                new DiscreteCoordinates(0, 7),
                new DiscreteCoordinates(0, 6),
                new DiscreteCoordinates(0, 5),
                new DiscreteCoordinates(0, 4)
        ));
    }

    @Override
    public boolean isViewCentered() { return true; }

    @Override
    public DiscreteCoordinates getPlayerSpawnPosition() {
        return new DiscreteCoordinates(1, 12);
    }

    @Override
    public String getTitle() { return "OrbWay"; }
}
