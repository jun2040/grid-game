package ch.epfl.cs107.icoop;


import ch.epfl.cs107.icoop.actor.CenterOfMass;
import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.icoop.actor.ICoopPlayer;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.area.maps.OrbWay;
import ch.epfl.cs107.icoop.area.maps.Spawn;
import ch.epfl.cs107.icoop.utility.event.DoorTeleportEventListener;
import ch.epfl.cs107.play.areagame.AreaGame;
import ch.epfl.cs107.play.io.FileSystem;
import static ch.epfl.cs107.play.math.Orientation.*;
import ch.epfl.cs107.play.window.Window;

import java.util.ArrayList;
import java.util.List;


public class ICoop extends AreaGame implements DoorTeleportEventListener {
    private final String[] areas = {"Spawn", "OrbWay"};
    private final List<ICoopPlayer> players = new ArrayList<>();
    private int areaIndex;

    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            createAreas();
            areaIndex = 0;
            initArea(areas[areaIndex]);
            return true;
        }

        return false;
    }

    private void createAreas() {
        addArea(new Spawn());
        addArea(new OrbWay());
    }

    private void initArea(String areaKey) {
        ICoopArea area = (ICoopArea) setCurrentArea(areaKey, true);

        addPlayer(new ICoopPlayer(
                area, DOWN, area.getPlayerSpawnPosition(),
                "player", "feu",
                KeyBindings.RED_PLAYER_KEY_BINDINGS
        ));

        addPlayer(new ICoopPlayer(
                area, DOWN, area.getPlayerSpawnPosition(),
                "player2", "feu",
                KeyBindings.BLUE_PLAYER_KEY_BINDINGS
        ));

        CenterOfMass centerOfMass = new CenterOfMass(players.getFirst(), players.subList(1, players.size()).toArray(new ICoopPlayer[0]));
        area.setViewCandidate(centerOfMass);
    }

    private void addPlayer(ICoopPlayer player) {
        ICoopArea area = (ICoopArea) getCurrentArea();

        player.enterArea(area, area.getPlayerSpawnPosition());
        player.centerCamera();
        player.getDoorTeleportEvent().addEventListener(this);

        players.add(player);
    }

    @Override
    public String getTitle() { return "ICoop"; }

    @Override
    public void teleport(Door door) {
        players.forEach(ICoopPlayer::leaveArea);

        ICoopArea area = (ICoopArea) setCurrentArea(door.getDestinationAreaName(), true);

        players.forEach(player -> {
            player.enterArea((ICoopArea) getCurrentArea(), door.getTargetCoords()[player.getId()]);
        });

        CenterOfMass centerOfMass = new CenterOfMass(players.getFirst(), players.subList(1, players.size()).toArray(new ICoopPlayer[0]));
        area.setViewCandidate(centerOfMass);
    }
}
