package ch.epfl.cs107.play.icoop.area.maps;

import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.icoop.actor.ICoopPlayer;
import ch.epfl.cs107.play.icoop.area.ICoopArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Spawn extends ICoopArea {
    @Override
    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));
    }

    @Override
    public DiscreteCoordinates getPlayerSpawnPosition() {
        return new DiscreteCoordinates(13, 6);
    }
    public DiscreteCoordinates getBluePlayerSpawnPosition() {
        return new DiscreteCoordinates(14, 6);
    }

    @Override
    public String getTitle() {
        return "Spawn";
    }
}
