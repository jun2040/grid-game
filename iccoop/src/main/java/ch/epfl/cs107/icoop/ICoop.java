package ch.epfl.cs107.icoop;


import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.icoop.actor.ICoopPlayer;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.area.maps.OrbWay;
import ch.epfl.cs107.icoop.area.maps.Spawn;
import ch.epfl.cs107.icoop.utility.event.DoorTeleportEventListener;
import ch.epfl.cs107.play.areagame.AreaGame;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Window;


public class ICoop extends AreaGame implements DoorTeleportEventListener {
    private final String[] areas = {"Spawn", "OrbWay"};
    private ICoopPlayer player;
    private int areaIndex;

    private void createAreas() {
        addArea(new Spawn());
        addArea(new OrbWay());
    }

    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            createAreas();
            areaIndex = 0;
            initArea(areas[areaIndex]);
            return true;
        }
        return false;
    }

    private void initArea(String areaKey) {
        ICoopArea area = (ICoopArea) setCurrentArea(areaKey, true);
        DiscreteCoordinates coords = area.getPlayerSpawnPosition();
        player = new ICoopPlayer(area, Orientation.DOWN, coords, "player", "feu", KeyBindings.RED_PLAYER_KEY_BINDINGS);
        player.enterArea(area, coords);
        player.centerCamera();
        player.getDoorTeleportEvent().addEventListener(this);
    }

    @Override
    public String getTitle() { return "ICoop"; }

    @Override
    public void teleport(Door door) {
        player.leaveArea();
        setCurrentArea(door.getDestinationAreaName(), true);
        player.enterArea((ICoopArea) getCurrentArea(), door.getTargetCoords()[0]);
    }
}
