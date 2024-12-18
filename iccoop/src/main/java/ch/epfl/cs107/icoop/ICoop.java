package ch.epfl.cs107.icoop;


import ch.epfl.cs107.icoop.actor.CenterOfMass;
import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.icoop.actor.ICoopPlayer;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.area.maps.Arena;
import ch.epfl.cs107.icoop.area.maps.Maze;
import ch.epfl.cs107.icoop.area.maps.OrbWay;
import ch.epfl.cs107.icoop.area.maps.Spawn;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.icoop.handler.TeleportController;
import ch.epfl.cs107.play.areagame.AreaGame;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.io.FileSystem;

import static ch.epfl.cs107.play.math.Orientation.*;

import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.And;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

import java.util.ArrayList;
import java.util.List;


public class ICoop extends AreaGame implements DialogHandler {
    private final List<ICoopPlayer> players = new ArrayList<>();
    private ICoopPlayer player1;
    private ICoopPlayer player2;
    private Dialog dialog = null;
    private TeleportController teleportController = new TeleportController();

    private Spawn spawn;
    private OrbWay orbWay;
    private Maze maze;
    private Arena arena;

    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            createAreas();
            setupArea("Spawn");
            return true;
        }

        return false;
    }

    private void createAreas() {
        spawn = new Spawn();
        orbWay = new OrbWay(this);
        maze = new Maze();
        arena = new Arena();

        spawn.setDialogHandler(this);

        addArea(spawn);
        addArea(orbWay);
        addArea(maze);
        addArea(arena);
    }

    /**
     * Start area
     * @param areaKey (String) String key of area
     */
    private void setupArea(String areaKey) {
        ICoopArea area = (ICoopArea) setCurrentArea(areaKey, true);

        if (player1 == null)
            player1 = new ICoopPlayer(area, UP, area.getPlayerSpawnPosition(0), "player", "feu", KeyBindings.RED_PLAYER_KEY_BINDINGS, teleportController, 0);

        if (player2 == null)
            player2 = new ICoopPlayer(area, UP, area.getPlayerSpawnPosition(1), "player2", "eau", KeyBindings.BLUE_PLAYER_KEY_BINDINGS, teleportController, 1);

        player1.enterArea(area, area.getPlayerSpawnPosition(0));
        player2.enterArea(area, area.getPlayerSpawnPosition(1));

        CenterOfMass centerOfMass = new CenterOfMass(player1, player2);
        area.setViewCandidate(centerOfMass);
    }

    @Override
    public String getTitle() {
        return "ICoop";
    }

    // TODO: Remove repetition in teleport and setupArea
    public void teleport() {
        player1.leaveArea();
        player2.leaveArea();

        ICoopArea area = (ICoopArea) setCurrentArea(teleportController.getTargetDestination(), true);

        player1.enterArea(area, teleportController.getTeleportPosition(0));
        player2.enterArea(area, teleportController.getTeleportPosition(1));

        CenterOfMass centerOfMass = new CenterOfMass(player1, player2);
        area.setViewCandidate(centerOfMass);

        teleportController.resetTeleport();
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
        super.update(deltaTime);

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

        if (teleportController.isTeleportRequested())
            teleport();

        if (player1.isDead() || player2.isDead())
            resetArea();

        if (new And(maze, arena).isOn())
            spawn.complete();
    }

    public void resetGame() {
        begin(getWindow(), getFileSystem());
    }

    public void resetArea() {
        getCurrentArea().begin(getWindow(), getFileSystem());
        setupArea(getCurrentArea().getTitle());
    }

    @Override
    public void publish(Dialog dialog) {
        this.dialog = dialog;
    }
}
