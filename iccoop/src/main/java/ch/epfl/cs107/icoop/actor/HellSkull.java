package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.icoop.utility.Timer;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.OrientedAnimation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.math.random.RandomGenerator;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represents the HellSkull enemy, which moves and periodically launches flames.
 */
public class HellSkull extends Enemy {

    /**
     * The duration of each frame in the HellSkull's animation.
     */
    private static final int ANIMATION_DURATION = 12;

    /**
     * The animation used to visually represent the HellSkull.
     */
    private final OrientedAnimation animation;

    /**
     * A timer controlling the periodic flame launch behavior.
     */
    private final Timer flameSpawnTimer;

    /**
     * Handles interactions between the HellSkull and other entities.
     */
    private final HellSkullInteractionHandler interactionHandler;

    /**
     * Constructs a HellSkull with the specified parameters.
     *
     * @param area        (Area): The area to which the HellSkull belongs. Not null.
     * @param orientation (Orientation): The initial orientation of the HellSkull. Not null.
     * @param position    (DiscreteCoordinates): The initial position of the HellSkull. Not null.
     */
    public HellSkull(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position, 1, new ArrayList<>(Arrays.asList(ICoopPlayer.DamageType.FIRE, ICoopPlayer.DamageType.EXPLOSIVE)));

        Orientation[] orders = new Orientation[]{Orientation.UP, Orientation.LEFT, Orientation.DOWN, Orientation.RIGHT};
        this.animation = new OrientedAnimation(
                "icoop/flameskull",
                ANIMATION_DURATION / 3, this,
                new Vector(-0.5f, -0.5f), orders,
                3, 2, 2, 32, 32, true);

        this.flameSpawnTimer = new Timer(RandomGenerator.getInstance().nextFloat(0.5f, 2.0f));

        this.interactionHandler = new HellSkullInteractionHandler();
    }

    /**
     * Draws the HellSkull's animation or death animation based on its state.
     *
     * @param canvas (Canvas): The canvas to draw the HellSkull on.
     */
    @Override
    public void draw(Canvas canvas) {
        if (!isDead()) {
            animation.draw(canvas);
        } else {
            deathAnimation.draw(canvas);
        }
    }

    /**
     * Updates the HellSkull's behavior, including its animation and flame launch logic.
     *
     * @param deltaTime (float): The time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        animation.update(deltaTime);

        flameSpawnTimer.update(deltaTime);

        if (flameSpawnTimer.isCompleted()) {
            launchFlames();
            flameSpawnTimer.setTimer(RandomGenerator.getInstance().nextFloat(0.5f, 2.0f));
        }
    }

    /**
     * Launches a new flame in the HellSkull's current direction.
     * The flame is created in the cell directly in front of the HellSkull.
     */
    private void launchFlames() {
        DiscreteCoordinates target = getFieldOfViewCells().getFirst();

        if (((ICoopArea) getOwnerArea()).isCellFree(target)) {
            getOwnerArea().registerActor(new Flame(getOwnerArea(), getOrientation(), target, 5, 50));
        }
    }

    /**
     * Retrieves the HellSkull's current occupied cell.
     *
     * @return (List < DiscreteCoordinates >): A list containing the current cell coordinates.
     */
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    /**
     * Retrieves the HellSkull's field of view, which is the cell directly in front of it.
     *
     * @return (List < DiscreteCoordinates >): A list containing the target cell coordinates.
     */
    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
    }

    /**
     * Handles interactions between the HellSkull and other entities.
     *
     * @param other             (Interactable): The interacting entity.
     * @param isCellInteraction (boolean): True if the interaction is at the cell level.
     */
    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(interactionHandler, isCellInteraction);
    }

    /**
     * Interaction handler for the HellSkull.
     * Damages the player when in the same cell.
     */
    private class HellSkullInteractionHandler implements ICoopInteractionVisitor {

        /**
         * Default interaction with generic interactable entities.
         *
         * @param other             (Interactable): The interacting entity.
         * @param isCellInteraction (boolean): True if the interaction is at the cell level.
         */
        @Override
        public void interactWith(Interactable other, boolean isCellInteraction) {
        }

        /**
         * Handles interaction with a player, dealing fire damage.
         *
         * @param player            (ICoopPlayer): The player interacting with the HellSkull.
         * @param isCellInteraction (boolean): True if the interaction is at the cell level.
         */
        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            player.hit(ICoopPlayer.DamageType.FIRE);
        }
    }
}
