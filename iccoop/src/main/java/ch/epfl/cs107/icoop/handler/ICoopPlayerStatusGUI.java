package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.icoop.actor.ICoopPlayer;
import ch.epfl.cs107.play.engine.actor.Graphics;
import ch.epfl.cs107.play.engine.actor.ImageGraphics;
import ch.epfl.cs107.play.io.ResourcePath;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

/**
 * A GUI that displays player status information on the screen.
 */
public class ICoopPlayerStatusGUI implements Graphics {

    /** The rendering depth for the GUI elements. */
    private static final int DEPTH = 2000;

    /** The player whose status is displayed in the GUI. */
    private final ICoopPlayer player;

    /** Indicates whether the GUI is flipped to the left-hand side. */
    private final boolean flipped;

    /** The current item to be displayed in the GUI. */
    private ICoopItem item = null;

    /**
     * Constructs a GUI for displaying the player's status.
     *
     * @param player  (ICoopPlayer): The player for whom the GUI is displayed. Not null.
     * @param flipped (boolean): Determines if the GUI is flipped to the left-hand side (true for left).
     */
    public ICoopPlayerStatusGUI(ICoopPlayer player, boolean flipped) {
        this.player = player;
        this.flipped = flipped;
    }

    /**
     * Sets the current item to be displayed in the GUI.
     *
     * @param item (ICoopItem): The item to display in the GUI. Can be null.
     */
    public void setCurrentItem(ICoopItem item) {
        this.item = item;
    }

    /**
     * Draws the player status GUI on the canvas.
     *
     * @param canvas (Canvas): The canvas on which the GUI is drawn. Not null.
     */
    @Override
    public void draw(Canvas canvas) {
        // Compute canvas dimensions and aspect ratio
        float width = canvas.getTransform().getX().getX();
        float height = canvas.getTransform().getY().getY();

        float ratio = canvas.getWidth() / (float) canvas.getHeight();
        if (ratio > 1) {
            height = width / ratio;
        } else {
            width = height * ratio;
        }

        // Determine anchor position based on flipping
        Vector anchor = canvas.getTransform().getOrigin().sub(new Vector(
                flipped ? (-width / 2 + 2) : width / 2,
                height / 2
        ));

        // Draw the gear display background
        ImageGraphics gearDisplay = new ImageGraphics(
                ResourcePath.getSprite("icoop/gearDisplay"),
                1.5f, 1.5f,
                new RegionOfInterest(0, 0, 32, 32),
                anchor.add(new Vector(0, height - 1.75f)),
                1, DEPTH
        );
        gearDisplay.draw(canvas);

        // Draw the selected item if available
        if (item != null) {
            ImageGraphics itemDisplay = new ImageGraphics(
                    ResourcePath.getSprite(item.getSpriteName()),
                    0.5f, 0.5f,
                    new RegionOfInterest(0, 0, 16, 16),
                    anchor.add(new Vector(0.5f, height - 1.25f)),
                    1, DEPTH
            );
            itemDisplay.draw(canvas);
        }
    }
}
