package ch.epfl.cs107.icoop;


import ch.epfl.cs107.icoop.actor.CenterOfMass;
import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.icoop.actor.ElementType;
import ch.epfl.cs107.icoop.actor.ICoopPlayer;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.area.maps.*;
import ch.epfl.cs107.icoop.handler.Context;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.icoop.handler.TeleportController;
import ch.epfl.cs107.play.areagame.AreaGame;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.io.FileSystem;

import static ch.epfl.cs107.play.math.Orientation.*;

import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
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
    private SanctumEntrance sanctumEntrance;
    private Sanctum sanctum;

    public final static float DEFAULT_SCALE_FACTOR = 13.f;
    private boolean paused = false;

    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            createAreas();
            setupArea("Spawn");
            return true;
        }

        return false;
    }

    /**
     * generate and add to existing area list the wished areas
     */
    private void createAreas() {
        Context context = new Context(this);

        spawn = new Spawn(context);
        orbWay = new OrbWay(context);
        maze = new Maze(context);
        arena = new Arena(context);
        sanctumEntrance = new SanctumEntrance(context);
        sanctum = new Sanctum(context);


        addArea(spawn);
        addArea(orbWay);
        addArea(maze);
        addArea(arena);
        addArea(sanctumEntrance);
        addArea(sanctum);
    }

    /**
     * Start area
     *
     * @param areaKey (String) String key of area
     *                <p>
     *                registers the new players to the current area and set the center of mass between them for the camera
     */
    private void setupArea(String areaKey) {
        ICoopArea area = (ICoopArea) setCurrentArea(areaKey, true);

        if (player1 == null)
            player1 = new ICoopPlayer(area, DOWN, area.getPlayerSpawnPosition(0), "player", "feu", KeyBindings.RED_PLAYER_KEY_BINDINGS, teleportController, 0);

        if (player2 == null)
            player2 = new ICoopPlayer(area, DOWN, area.getPlayerSpawnPosition(1), "player2", "eau", KeyBindings.BLUE_PLAYER_KEY_BINDINGS, teleportController, 1);

        player1.enterArea(area, area.getPlayerSpawnPosition(0));
        player2.enterArea(area, area.getPlayerSpawnPosition(1));


        CenterOfMass centerOfMass = new CenterOfMass(player1, player2);
        area.setViewCandidate(centerOfMass);
    }

    @Override
    public String getTitle() {
        return "ICoop";
    }

    /**
     * register players to new area once teleported, in the correct orienation
     */
    // TODO: Remove repetition in teleport and setupArea
    public void teleport() {
        player1.leaveArea();
        player2.leaveArea();

        ICoopArea area = (ICoopArea) setCurrentArea(teleportController.getTargetDestination(), true);

        player1.enterArea(area, teleportController.getTeleportPosition(0));
        player2.enterArea(area, teleportController.getTeleportPosition(1));

        player1.setOrienation(teleportController.getTargetOrientation());
        player2.setOrienation(teleportController.getTargetOrientation());

        CenterOfMass centerOfMass = new CenterOfMass(player1, player2);
        area.setViewCandidate(centerOfMass);

        teleportController.resetTeleport();
    }

    /**
     * draw the dialog on top of the current area, only when it is assigned
     */
    @Override
    public void draw() {
        super.draw();

        if (dialog != null && !dialog.isCompleted())
            dialog.draw(getWindow());
        else
            ((ICoopArea) getCurrentArea()).setIsDirty(Logic.FALSE);
    }

    /**
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     *                  check for keyboard strokes to pause, reset game, reset area
     *                  check if players died
     *                  update dialog if existent and not completed
     *                  update the camera scale factor
     *                  complete spawn once maze and area are generated
     */
    // TODO: Pause when dialog opens
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        Keyboard keyboard = getCurrentArea().getKeyboard();

        if (dialog != null && keyboard.get(KeyBindings.NEXT_DIALOG).isPressed()) {
            dialog.update(deltaTime);

            if (dialog.isCompleted()) {
                dialog = null;
                ((ICoopArea) getCurrentArea()).setIsDirty(Logic.FALSE);
            }

        }

        if (keyboard.get(KeyBindings.RESET_GAME).isPressed())
            resetGame();

        if (keyboard.get(KeyBindings.RESET_AREA).isPressed())
            resetArea();

        if (teleportController.isTeleportRequested())
            teleport();

        if (player1.isDead() || player2.isDead()) {
            resetArea();
            player1.resetHealth();
            player2.resetHealth();
        }

        if (new And(maze, arena).isOn())
            spawn.complete();

        if (keyboard.get(Keyboard.ESCAPE).isPressed()) {
            ICoopArea area = (ICoopArea) getCurrentArea();
            if (!paused) {
                area.setIsDirty(Logic.TRUE);
                paused = true;
            } else {
                area.setIsDirty(Logic.FALSE);
                paused = false;
            }
        }

        calculateCameraScaleFactor();
    }

    /**
     * hard resets game (as if new boot up)
     */
    public void resetGame() {
        begin(getWindow(), getFileSystem());
    }

    /**
     * soft reset area, as when entering it
     */
    public void resetArea() {
        getCurrentArea().begin(getWindow(), getFileSystem());
        setupArea(getCurrentArea().getTitle());
    }

    /**
     * @param dialog publish the incoming dialog and stop the updating of the current area
     */
    @Override
    public void publish(Dialog dialog) {
        this.dialog = dialog;
        ((ICoopArea) getCurrentArea()).setIsDirty(Logic.TRUE);
    }

    /**
     * calculate camera scale factor depending on the default value, the player's distanc to each other
     */
    public void calculateCameraScaleFactor() {
        if (!((ICoopArea) getCurrentArea()).isViewCentered()) {
            ((ICoopArea) getCurrentArea()).setCameraScaleFactor(
                    (float) Math.max(
                            DEFAULT_SCALE_FACTOR, DEFAULT_SCALE_FACTOR * 0.75
                                    + distance(
                                    player1.getPosition(),
                                    player2.getPosition()
                            ) / 1.5
                    )
            );
        }

    }

    /**
     * @param position_playerA
     * @param position_playerB
     * @return double
     * calculates distance between 2 vectors (in this case the distance between players
     */
    public double distance(Vector position_playerA, Vector position_playerB) {
        double deltaX = position_playerB.x - position_playerA.x;
        double deltaY = position_playerB.y - position_playerA.y;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }
}
