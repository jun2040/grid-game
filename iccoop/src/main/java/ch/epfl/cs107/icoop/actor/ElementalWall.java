package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.icoop.utility.event.WallActivateEventListener;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.RPGSprite;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

// FIXME: Verify the use of Logic interface
public class ElementalWall extends AreaEntity implements ElementalEntity, Interactable, Interactor, Logic, WallActivateEventListener {
    private final Sprite[] wallSprites;
    private final ElementType elementType;
    private ElementType currentElementType;// TODO: Replace with enum

    private boolean isActive;

    /**
     * Default AreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
    public ElementalWall(
            Area area,
            Orientation orientation,
            DiscreteCoordinates position,
            boolean isActive,
            String spriteName,
            ElementType elementType
    ) {
        super(area, orientation, position);

        this.isActive = isActive; // TODO: Activate only when this attribute is true

        this.wallSprites = RPGSprite.extractSprites(
                spriteName, 4, 1, 1,
                this, Vector.ZERO, 256, 256
        );
        this.elementType = elementType;
        this.currentElementType = elementType;

    }

    @Override
    public void draw(Canvas canvas) {
        if (isActive)
            wallSprites[getOrientation().ordinal()].draw(canvas);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    /*
     * ElementalEntity implementation
     */
    @Override
    public String element() {
        return currentElementType.getName();
    }

    /*
     * Interactor implementation
     */
    @Override
    public boolean wantsCellInteraction() { return true; }

    @Override
    public boolean wantsViewInteraction() { return false; }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) { }

    /*
     * Interactable implementation
     */
    @Override
    public List<DiscreteCoordinates> getCurrentCells() { return Collections.singletonList(getCurrentMainCellCoordinates()); }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() { return Collections.emptyList(); }

    @Override
    public boolean takeCellSpace() { return true; } //TODO should it not be true?

    @Override
    public boolean isCellInteractable() { return true; }

    @Override
    public boolean isViewInteractable() { return true; }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    /*
     * Listener implementation
     */

    @Override
    public void activate() {
        isActive = true;
        currentElementType = elementType;
    }

    @Override
    public void deactivate() {
        isActive = false;
        currentElementType = ElementType.NONE;
    }

    // TODO: Consider a Destructable interface for further abstraction
    public void destroy() {
        unregister();
    }

    // TODO: Consider extending AreaEntity to include this method
    private void unregister() {
        getOwnerArea().unregisterActor(this);
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
