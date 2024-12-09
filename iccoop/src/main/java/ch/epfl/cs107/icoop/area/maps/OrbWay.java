package ch.epfl.cs107.icoop.area.maps;

import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.icoop.actor.ElementalWall;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

import static ch.epfl.cs107.play.math.Orientation.*;

public class OrbWay extends ICoopArea {
    public static DiscreteCoordinates[] TP_POINTS =
            new DiscreteCoordinates[] { new DiscreteCoordinates(1, 12), new DiscreteCoordinates(1, 5) };

    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));

        registerActor(new Door(
                this, RIGHT, "Spawn", Logic.TRUE,
                Spawn.TP_POINTS,
                new DiscreteCoordinates(0, 14),
                new DiscreteCoordinates(0, 13),
                new DiscreteCoordinates(0, 12),
                new DiscreteCoordinates(0, 11),
                new DiscreteCoordinates(0, 10)
        ));
        registerActor(new Door(
                this, RIGHT, "Spawn", Logic.TRUE,
                Spawn.TP_POINTS,
                new DiscreteCoordinates(0, 8),
                new DiscreteCoordinates(0, 7),
                new DiscreteCoordinates(0, 6),
                new DiscreteCoordinates(0, 5),
                new DiscreteCoordinates(0, 4)
        ));

        for (int i = 0; i < 4; ++i) {
            registerActor(new ElementalWall(
                    this, LEFT, new DiscreteCoordinates(12, 10 + i),
                    Logic.TRUE, "fire_wall", "feu"
            ));
            registerActor(new ElementalWall(
                    this, LEFT, new DiscreteCoordinates(12, 4 + i),
                    Logic.TRUE, "water_wall", "eau"
            ));
        }
    }

    @Override
    public boolean isViewCentered() { return true; }

    @Override
    public DiscreteCoordinates getPlayerSpawnPosition(int id) { return TP_POINTS[id]; }

    @Override
    public String getTitle() { return "OrbWay"; }
}
