package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.icoop.handler.TeleportHandler;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.*;

/**
 * Represents a Door in the game world. A Door acts as an entry or exit point
 * between different areas, supporting teleportation and interaction. The Door
 * can be in an open or closed state, determining whether teleportation is
 * possible.
 */
public class Door extends AreaEntity implements Interactable, Logic, TeleportHandler {

    /**
     * The name of the destination area this Door leads to.
     */
    private final String destinationAreaName;

    /**
     * The coordinates in the destination area where the player will spawn.
     */
    private final DiscreteCoordinates[] targetCoords;

    /**
     * The list of cell positions occupied by this Door in the current area.
     */
    private final List<DiscreteCoordinates> positions;

    /**
     * Indicates whether the Door is open (true) or closed (false).
     */
    private boolean isOpen;

    /**
     * Constructs a Door with a single position in the owner area.
     *
     * @param area                (Area): The area this Door belongs to. Not null.
     * @param orientation         (Orientation): The orientation of the Door. Not null.
     * @param destinationAreaName (String): The name of the destination area. Not null.
     * @param isOpen              (boolean): Initial state of the Door (true if open).
     * @param targetCoords        (DiscreteCoordinates[]): Array of target coordinates in the destination area.
     * @param mainPosition        (DiscreteCoordinates): The primary position of the Door in the current area. Not null.
     */
    public Door(
            Area area,
            Orientation orientation,
            String destinationAreaName,
            boolean isOpen,
            DiscreteCoordinates[] targetCoords,
            DiscreteCoordinates mainPosition
    ) {
        super(area, orientation, mainPosition);
        this.destinationAreaName = destinationAreaName;
        this.targetCoords = targetCoords;
        this.isOpen = isOpen;

        /**
         * Initializes the list of positions and adds the main position.
         */
        this.positions = new ArrayList<>();
        this.positions.add(mainPosition);
    }

    /**
     * Constructs a Door with multiple positions in the owner area.
     *
     * @param area                (Area): The area this Door belongs to. Not null.
     * @param orientation         (Orientation): The orientation of the Door. Not null.
     * @param destinationAreaName (String): The name of the destination area. Not null.
     * @param isOpen              (boolean): Initial state of the Door (true if open).
     * @param targetCoords        (DiscreteCoordinates[]): Array of target coordinates in the destination area.
     * @param mainPosition        (DiscreteCoordinates): The primary position of the Door in the current area. Not null.
     * @param otherPositions      (DiscreteCoordinates...): Additional positions occupied by the Door. Not null.
     */
    public Door(
            Area area,
            Orientation orientation,
            String destinationAreaName,
            boolean isOpen,
            DiscreteCoordinates[] targetCoords,
            DiscreteCoordinates mainPosition,
            DiscreteCoordinates... otherPositions
    ) {
        this(area, orientation, destinationAreaName, isOpen, targetCoords, mainPosition);
        /**
         * Adds other positions to the list of occupied positions.
         */
        this.positions.addAll(Arrays.asList(otherPositions));
    }

    @Override
    public void update(float deltaTime) {
        /**
         * Updates the Door's state if needed (placeholder for future functionality).
         */
        super.update(deltaTime);
    }

    /**
     * Opens the Door, allowing teleportation.
     */
    public void open() {
        isOpen = true;
    }

    /**
     * Closes the Door, preventing teleportation.
     */
    public void close() {
        isOpen = false;
    }

    /**
     * Checks if the Door has a valid destination area.
     *
     * @return (boolean): True if the destination area name is non-empty.
     */
    public boolean teleportable() {
        return !destinationAreaName.isEmpty();
    }

    @Override
    public String getDestinationAreaName() {
        return destinationAreaName;
    }

    @Override
    public Orientation getDestinationOrientation() {
        /**
         * Retrieves the spawn orientation for the destination area.
         */
        return ((ICoopArea) getOwnerArea()).getSpawnOrientation();
    }

    @Override
    public DiscreteCoordinates[] getTargetCoords() {
        return targetCoords;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        /**
         * Returns the list of positions occupied by the Door in the current area.
         */
        return positions;
    }

    @Override
    public boolean takeCellSpace() {
        /**
         * Indicates that the Door does not block cell space.
         */
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        /**
         * Indicates that the Door can be interacted with at the cell level.
         */
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        /**
         * Indicates that the Door cannot be interacted with from a distance.
         */
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        /**
         * Handles interactions with the Door, delegating to the provided visitor.
         */
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    @Override
    public boolean isOn() {
        /**
         * Logic signal indicating the Door is "on" (open).
         */
        return isOpen;
    }

    @Override
    public boolean isOff() {
        /**
         * Logic signal indicating the Door is "off" (closed).
         */
        return !isOpen;
    }
}
