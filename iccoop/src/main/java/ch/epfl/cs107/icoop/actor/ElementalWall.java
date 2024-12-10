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
public class ElementalWall extends AreaEntity implements ElementalEntity, Interactable, Interactor, WallActivateEventListener {
    private final Sprite[] wallSprites;
    private final String elementDamage; // TODO: Replace with enum

    private Logic isActive;

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
            Logic isActive,
            String spriteName,
            String elementDamage
    ) {
        super(area, orientation, position);

        this.isActive = isActive; // TODO: Activate only when this attribute is true

        this.wallSprites = RPGSprite.extractSprites(
                spriteName, 4, 1, 1,
                this, Vector.ZERO, 256, 256
        );

        this.elementDamage = elementDamage;
    }

    @Override
    public void draw(Canvas canvas) {
        if (isActive == Logic.TRUE)
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
        return elementDamage;
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
    public boolean takeCellSpace() { return false; }

    @Override
    public boolean isCellInteractable() { return true; }

    @Override
    public boolean isViewInteractable() { return false; }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    /*
     * Listener implementation
     */

    @Override
    public void activate() {
        isActive = Logic.TRUE;
    }

    @Override
    public void deactivate() {
        isActive = Logic.FALSE;
    }
}
