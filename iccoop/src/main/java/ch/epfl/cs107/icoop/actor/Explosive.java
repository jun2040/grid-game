package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.icoop.handler.ICoopItem;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
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
import java.util.Collections;
import java.util.List;

public class Explosive extends ICoopCollectable implements Interactor {
    private final static int ANIMATION_DURATION = 24;

    private final Animation explosive;
    private final Animation explosion;
    private final ExplosiveInteractionHandler handler;

    private int timer;
    private boolean isActivated = false;
    private boolean isExploded = false;

    /**
     * Default AreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity in the Area. Not null
     * @param position    (DiscreteCoordinate): Initial position of the entity in the Area. Not null
     */
    public Explosive(Area area, Orientation orientation, DiscreteCoordinates position, int timer) {
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

    public boolean isActivated() {
        return isActivated;
    }

    public boolean isExploded() {
        return isExploded;
    }

    @Override
    public void draw(Canvas canvas) {
        if (isExploded && !explosion.isCompleted()) {
            explosion.draw(canvas);
            return;
        }

        if (!isExploded)
            explosive.draw(canvas);
    }

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

    public void activate() {
        isActivated = true;
    }

    private void explode() {
        timer = 0;
        isExploded = true;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        List<DiscreteCoordinates> cells = new ArrayList<>();
        cells.add(getCurrentMainCellCoordinates().jump(new Vector(1.0f, 0.0f)));
        cells.add(getCurrentMainCellCoordinates().jump(new Vector(0.0f, 1.0f)));
        cells.add(getCurrentMainCellCoordinates().jump(new Vector(-1.0f, 0.0f)));
        cells.add(getCurrentMainCellCoordinates().jump(new Vector(0.0f, -1.0f)));

        return cells;
    }

    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {
        return true;
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }

    @Override
    public boolean isCellInteractable() {
        return !isActivated || !isExploded;
    }

    @Override
    public boolean isViewInteractable() {
        return !isActivated;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    private class ExplosiveInteractionHandler implements ICoopInteractionVisitor {
        @Override
        public void interactWith(Interactable other, boolean isCellInteraction) {
        }

        @Override
        public void interactWith(Rock rock, boolean isCellInteraction) {
            if (isExploded)
                rock.destroy();
        }

        @Override
        public void interactWith(ICoopPlayer player, boolean isCellInteraction) {
            if (isExploded)
                player.hit(ICoopPlayer.DamageType.EXPLOSIVE);
        }

        @Override
        public void interactWith(Explosive explosive, boolean isCellInteraction) {
            // FIXME: Does not affect already activated bombs (see isViewInteractable method)
            if (!isCellInteraction && isExploded)
                explosive.explode();
        }

        @Override
        public void interactWith(ElementalWall elementalWall, boolean isCellInteraction) {
            if (isExploded)
                elementalWall.destroy();
        }
    }
}
