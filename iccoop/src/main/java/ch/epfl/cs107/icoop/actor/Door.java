package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.*;

public class Door extends AreaEntity implements Interactable {
    private String destinationAreaName;
    private DiscreteCoordinates[] targetCoords;
    private List<DiscreteCoordinates> positions;
    private Logic openSignal;

    /**
     * Default AreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param mainPosition (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
    public Door(
            Area area,
            Orientation orientation,
            String destinationAreaName,
            Logic openSignal,
            DiscreteCoordinates[] targetCoords,
            DiscreteCoordinates mainPosition
    ) {
        super(area, orientation, mainPosition);
        this.destinationAreaName = destinationAreaName;
        this.targetCoords = targetCoords;
        this.openSignal = openSignal;

        this.positions = new ArrayList<>();
        this.positions.add(mainPosition);
    }

    public Door(
            Area area,
            Orientation orientation,
            String destinationAreaName,
            Logic openSignal,
            DiscreteCoordinates[] targetCoords,
            DiscreteCoordinates mainPosition,
            DiscreteCoordinates ...otherPositions
    ) {
        super(area, orientation, mainPosition);
        this.destinationAreaName = destinationAreaName;
        this.targetCoords = targetCoords;
        this.openSignal = openSignal;

        this.positions = new ArrayList<>();
        this.positions.add(mainPosition);
        this.positions.addAll(Arrays.asList(otherPositions));
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    public Logic getSignal() {
        return openSignal;
    }

    public void setSignal(Logic signal) {
        this.openSignal = signal;
    }

    public String getDestinationAreaName() {
        return destinationAreaName;
    }

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
}
