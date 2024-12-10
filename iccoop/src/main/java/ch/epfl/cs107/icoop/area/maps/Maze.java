package ch.epfl.cs107.icoop.area.maps;

import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Maze extends ICoopArea {
    public static DiscreteCoordinates[] ARRIVAL_POINTS =
            new DiscreteCoordinates[] { new DiscreteCoordinates(2, 39), new DiscreteCoordinates(3, 39) };

    @Override
    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));
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
