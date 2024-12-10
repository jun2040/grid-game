package ch.epfl.cs107.icoop;


import ch.epfl.cs107.icoop.actor.CenterOfMass;
import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.icoop.actor.ICoopPlayer;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.area.maps.Maze;
import ch.epfl.cs107.icoop.area.maps.OrbWay;
import ch.epfl.cs107.icoop.area.maps.Spawn;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.icoop.utility.event.DoorTeleportEventListener;
import ch.epfl.cs107.play.areagame.AreaGame;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.io.FileSystem;
import static ch.epfl.cs107.play.math.Orientation.*;

import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

import java.util.ArrayList;
import java.util.List;


public class ICoop extends AreaGame implements DoorTeleportEventListener, DialogHandler {
    private final String[] areas = {"Spawn", "OrbWay"};
    private final List<ICoopPlayer> players = new ArrayList<>();
    private int areaIndex;

    private Dialog dialog = null;

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
        Spawn spawn = new Spawn();
        spawn.setDialogHandler(this);

        addArea(spawn);
        addArea(new OrbWay(this));
        addArea(new Maze());
    }

    private void initArea(String areaKey) {
        ICoopArea area = (ICoopArea) setCurrentArea(areaKey, true);

        players.clear();
        addPlayer("player", "feu", KeyBindings.RED_PLAYER_KEY_BINDINGS);
        addPlayer("player2", "eau", KeyBindings.BLUE_PLAYER_KEY_BINDINGS);

        CenterOfMass centerOfMass = new CenterOfMass(players.getFirst(), players.subList(1, players.size()).toArray(new ICoopPlayer[0]));
        area.setViewCandidate(centerOfMass);
    }

    private void addPlayer(String spriteName, String element, KeyBindings.PlayerKeyBindings keyBindings) {
        ICoopArea area = (ICoopArea) getCurrentArea();
        ICoopPlayer player = new ICoopPlayer(
                area, DOWN, area.getPlayerSpawnPosition(players.size()),
                spriteName, element, keyBindings, players.size()
        );

        player.enterArea(area, area.getPlayerSpawnPosition(player.getId()));
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

    @Override
    public void draw() {
        super.draw();

        if (dialog != null)
            dialog.draw(getWindow());
    }

    // TODO: Pause when dialog opens
    @Override
    public void update(float deltaTime) {
        Keyboard keyboard = getCurrentArea().getKeyboard();

        if (dialog != null && keyboard.get(KeyBindings.NEXT_DIALOG).isPressed()) {
            dialog.update(deltaTime);

            if (dialog.isCompleted())
                dialog = null;
        }

        if (keyboard.get(KeyBindings.RESET_GAME).isDown())
            resetGame();

        if (keyboard.get(KeyBindings.RESET_AREA).isDown())
            resetArea();

        /*
        * Use index-based for-loop over iteration:
        * Avoid concurrentModificationException caused by
        * attempting to access Collection while iterating
        */
        for (int i = 0; i < players.size(); ++i) {
            if (players.get(i).isDead())
                resetArea();
        }

        super.update(deltaTime);
    }

    public void resetGame() { begin(getWindow(), getFileSystem()); }

    public void resetArea() {
        getCurrentArea().begin(getWindow(), getFileSystem());
        initArea(getCurrentArea().getTitle());
    }

    @Override
    public void publish(Dialog dialog) { this.dialog = dialog; }
}
