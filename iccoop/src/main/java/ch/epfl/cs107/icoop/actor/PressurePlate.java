package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.icoop.utility.event.EventArgs;
import ch.epfl.cs107.icoop.utility.event.WallActivateEvent;
import ch.epfl.cs107.icoop.utility.event.WallActivateEventArgs;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.RPGSprite;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import static ch.epfl.cs107.play.math.Orientation.*;

import java.util.Collections;
import java.util.List;

public class PressurePlate extends AreaEntity implements Logic {
    private boolean isPressed = false;

    private final WallActivateEvent wallActivateEvent = new WallActivateEvent();
    private final Sprite plateSprite;

    /**
     * Default AreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
    public PressurePlate(Area area, DiscreteCoordinates position) {
        super(area, DOWN, position);

        this.plateSprite = new RPGSprite("GroundPlateOff", 1, 1, this);
    }

    @Override
    public void draw(Canvas canvas) {
        plateSprite.draw(canvas);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (isPressed)
            wallActivateEvent.emit(new WallActivateEventArgs(false));
        else
            wallActivateEvent.emit(new WallActivateEventArgs(true));

        deactivate();
    }

    public void activate() {
        isPressed = true;
    }

    public void deactivate() {
        isPressed = false;
    }

    public void linkWall(ElementalWall elementalWall) {
        wallActivateEvent.addEventListener(elementalWall);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
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
        return isPressed;
    }

    @Override
    public boolean isOff() {
        return !isPressed;
    }
}
