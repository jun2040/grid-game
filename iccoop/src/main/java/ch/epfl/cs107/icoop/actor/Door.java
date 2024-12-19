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

public class Door extends AreaEntity implements Interactable, Logic, TeleportHandler {
    private final String destinationAreaName;
    private final DiscreteCoordinates[] targetCoords;
    private final List<DiscreteCoordinates> positions;

    private boolean isOpen;

    /**
     * AreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param destinationAreaName (String): Destination area's name. Not null
     * @param isOpen    (boolean): True when open for teleportation
     * @param targetCoords  (DiscreteCoordinates): Destination area player spawning coordinates
     * @param mainPosition (DiscreteCoordinate): Initial position of the entity in the Area. Not null
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

        this.positions = new ArrayList<>();
        this.positions.add(mainPosition);
    }

    /**
     * AreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param destinationAreaName (String): Destination area's name. Not null
     * @param isOpen    (boolean): True when open for teleportation
     * @param targetCoords  (DiscreteCoordinates): Destination area player spawning coordinates
     * @param mainPosition (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     * @param otherPositions (DiscreteCoordinate): Other Initial positions of the entity in the Area. Not null
     */
    public Door(
            Area area,
            Orientation orientation,
            String destinationAreaName,
            boolean isOpen,
            DiscreteCoordinates[] targetCoords,
            DiscreteCoordinates mainPosition,
            DiscreteCoordinates ...otherPositions
    ) {
        this(area, orientation, destinationAreaName, isOpen, targetCoords, mainPosition);
        this.positions.addAll(Arrays.asList(otherPositions));
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    /**
     * Opens the Door by setting isOpen true
     */
    public void open() {
        isOpen = true;
    }

    /**
     * Closes the Door by setting isOpen false
     */
    public void close() {
        isOpen = false;
    }

    /**
     * @return boolean checks for suitable destination area
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
        return ((ICoopArea)getOwnerArea()).getSpawnOrientation();
    }

    @Override
    public DiscreteCoordinates[] getTargetCoords() {
        return targetCoords;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return positions;
    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    @Override
    public boolean isOn() {
        return isOpen;
    }

    @Override
    public boolean isOff() {
        return !isOpen;
    }
}
