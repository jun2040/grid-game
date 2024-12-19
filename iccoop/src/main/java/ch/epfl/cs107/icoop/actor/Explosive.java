package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.icoop.handler.ICoopItem;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an explosive entity in the game.
 * The explosive can be activated, count down to detonation, and interact with other entities upon explosion.
 */
public class Explosive extends ICoopCollectable implements Interactor {

    /**
     * The duration of each frame in the animations.
     */
    private final static int ANIMATION_DURATION = 24;

    /**
     * The animation representing the explosive's idle state.
     */
    private final Animation explosive;

    /**
     * The animation representing the explosive's detonation.
     */
    private final Animation explosion;

    /**
     * Handles interactions with other entities.
     */
    private final ExplosiveInteractionHandler handler;

    /**
     * The timer indicating the countdown to explosion.
     */
    private int timer;

    /**
     * Indicates whether the explosive has been activated.
     */
    private boolean isActivated = false;

    /**
     * Indicates whether the explosive has already detonated.
     */
    private boolean isExploded = false;

    /**
     * Constructs an Explosive with the specified parameters.
     *
     * @param area        (Area): The area to which the explosive belongs. Not null.
     * @param orientation (Orientation): The orientation of the explosive. Not null.
     * @param position    (DiscreteCoordinates): The initial position of the explosive. Not null.
     * @param timer       (int): The countdown duration before the explosive detonates. Not null.
     */
    public Explosive(Area area,
                     Orientation orientation,
                     DiscreteCoordinates position,
                     int timer) {
        super(area, orientation, position, ICoopItem.BOMB);

        this.timer = timer;

        this.explosive =
                new Animation(
                        "icoop/explosive", 2,
                        1, 1,
                        this,
                        16, 16,
                        ANIMATION_DURATION / 2, true
                );

        this.explosion =
                new Animation(
                        "icoop/explosion", 7,
                        2, 2,
                        this,
                        32, 32,
                        ANIMATION_DURATION / 7, false
                );
        explosion.setAnchor(new Vector(-0.5f, -0.5f));

        this.handler = new ExplosiveInteractionHandler();
    }

    /**
     * Checks if the explosive has been activated.
     *
     * @return (boolean): True if activated, false otherwise.
     */
    public boolean isActivated() {
        return isActivated;
    }

    /**
     * Checks if the explosive has already detonated.
     *
     * @return (boolean): True if detonated, false otherwise.
     */
    public boolean isExploded() {
        return isExploded;
    }

    /**
     * Draws the appropriate animation based on the explosive's state.
     *
     * @param canvas (Canvas): The canvas to draw the animation on.
     */
    @Override
    public void draw(Canvas canvas) {
        if (isExploded && !explosion.isCompleted()) {
            explosion.draw(canvas);
            return;
        }

        if (!isExploded) {
            explosive.draw(canvas);
        }
    }

    /**
     * Updates the state of the explosive, including its timer and animations.
     *
     * @param deltaTime (float): The time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (explosion.isCompleted()) {
            getOwnerArea().unregisterActor(this);
            return;
        }

        if (this.timer == 0) {
            explode();
            explosion.update(deltaTime);
        }

        if (isActivated && this.timer > 0) {
            this.timer--;
            explosive.update(deltaTime);
        }
    }

    /**
     * Activates the explosive, starting its countdown.
     */
    public void activate() {
        isActivated = true;
    }

    /**
     * Triggers the explosion, setting the state to exploded.
     */
    private void explode() {
        timer = 0;
        isExploded = true;
    }

    /**
     * Retrieves the cells within the explosive's field of view.
     *
     * @return (List < DiscreteCoordinates >): A list of the coordinates in the explosive's field of view.
     */
    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        List<DiscreteCoordinates> cells = new ArrayList<>();
        cells.add(getCurrentMainCellCoordinates().jump(new Vector(1.0f, 0.0f)));
        cells.add(getCurrentMainCellCoordinates().jump(new Vector(0.0f, 1.0f)));
        cells.add(getCurrentMainCellCoordinates().jump(new Vector(-1.0f, 0.0f)));
        cells.add(getCurrentMainCellCoordinates().jump(new Vector(0.0f, -1.0f)));

        return cells;
    }

    /**
     * Indicates that the explosive wants cell-based interactions.
     *
     * @return (boolean): True if cell interaction is desired, false otherwise.
     */
    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    /**
     * Indicates that the explosive wants view-based interactions.
     *
     * @return (boolean): True if view interaction is desired, false otherwise.
     */
    @Override
    public boolean wantsViewInteraction() {
        return true;
    }

    /**
     * Handles interactions between the explosive and other entities.
     *
     * @param other             (Interactable): The entity interacting with the explosive.
     * @param isCellInteraction (boolean): True if the interaction is at the cell level.
     */
    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }

    /**
     * Indicates whether the explosive is interactable at the cell level.
     *
     * @return (boolean): True if cell-interactable, false otherwise.
     */
    @Override
    public boolean isCellInteractable() {
        return !isActivated || !isExploded;
    }

    /**
     * Indicates whether the explosive is interactable at the view level.
     *
     * @return (boolean): True if view-interactable, false otherwise.
     */
    @Override
    public boolean isViewInteractable() {
        return !isActivated;
    }

    /**
     * Accepts an interaction from another entity, delegating to the visitor.
     *
     * @param v                 (AreaInteractionVisitor): The visitor handling the interaction.
     * @param isCellInteraction (boolean): True if the interaction is at the cell level.
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    /**
     * Handles specific interactions for the Explosive.
     */
    private class ExplosiveInteractionHandler implements ICoopInteractionVisitor {

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
         * Handles interaction with a rock, destroying it upon explosion.
         *
         * @param rock              (Rock): The rock interacting with the explosive.
         * @param isCellInteraction (boolean): True if the interaction is at the cell level.
         */
        @Override
        public void interactWith(Rock rock, boolean isCellInteraction) {
            if (isExploded) {
                rock.destroy();
            }
        }

        /**
         * Handles interaction with a player, dealing explosive damage upon detonation.
         *
         * @param player            (ICoopPlayer): The player interacting with the explosive.
         * @param isCellInteraction (boolean): True if the interaction is at the cell level.
         */
        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            if (isExploded) {
                player.hit(ICoopPlayer.DamageType.EXPLOSIVE);
            }
        }

        /**
         * Handles interaction with another explosive, triggering its detonation if conditions allow.
         *
         * @param explosive         (Explosive): The explosive interacting with this explosive.
         * @param isCellInteraction (boolean): True if the interaction is at the cell level.
         */
        @Override
        public void interactWith(Explosive explosive, boolean isCellInteraction) {
            // FIXME: Does not affect already activated bombs (see isViewInteractable method).
            if (!isCellInteraction && isExploded) {
                explosive.explode();
            }
        }

        /**
         * Handles interaction with an elemental wall, destroying it upon explosion.
         *
         * @param elementalWall     (ElementalWall): The wall interacting with the explosive.
         * @param isCellInteraction (boolean): True if the interaction is at the cell level.
         */
        @Override
        public void interactWith(ElementalWall elementalWall, boolean isCellInteraction) {
            if (isExploded) {
                elementalWall.destroy();
            }
        }
    }
}
