package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
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
    private boolean isActive = false;

    private final Sprite plateSprite;

    /**
     * Default AreaEntity constructor
     *
     * @param area     (Area): Owner area. Not null
     * @param position (DiscreteCoordinate): Initial position of the entity in the Area. Not null
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

        isActive = isPressed;

        depress();
    }

    /**
     * activates the pressure plate ( so animation and desired outcomes will be impacted)
     */
    public void press() {
        isPressed = true;
    }

    /**
     * deactivates the pressure plate ( so animation and desired outcomes will be impacted)
     */
    public void depress() {
        isPressed = false;
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
        return isActive;
    }

    @Override
    public boolean isOff() {
        return !isActive;
    }
}
