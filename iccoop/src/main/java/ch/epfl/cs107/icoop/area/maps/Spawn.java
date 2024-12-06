package ch.epfl.cs107.icoop.area.maps;

import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.icoop.actor.Explosive;
import ch.epfl.cs107.icoop.actor.Rock;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

import static ch.epfl.cs107.play.math.Orientation.*;

public class Spawn extends ICoopArea {
    @Override
    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));
        registerActor(new Door(
                this, LEFT, "OrbWay", Logic.TRUE,
                new DiscreteCoordinates[] { new DiscreteCoordinates(1, 12), new DiscreteCoordinates(1, 5) },
                new DiscreteCoordinates(19, 15),
                new DiscreteCoordinates(19, 16)
                ));

        registerActor(new Rock(this, LEFT, new DiscreteCoordinates(9, 10)));
        registerActor(new Rock(this, LEFT, new DiscreteCoordinates(11, 10)));
        registerActor(new Rock(this, LEFT, new DiscreteCoordinates(10, 11)));
        registerActor(new Explosive(this, LEFT, new DiscreteCoordinates(10, 10), 100));
    }

    @Override
    public boolean isViewCentered() { return true; }

    @Override
    public DiscreteCoordinates getPlayerSpawnPosition() {
        return new DiscreteCoordinates(13, 6);
    }
    //have to implement for 2 players

    @Override
    public String getTitle() {
        return "Spawn";
    }
}
