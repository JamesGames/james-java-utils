package org.jamesgames.jamesjavautils.graphics;

import java.awt.*;

/**
 * An interface that specifies a method that will draw some graphics to a passed Graphics2D object. Useful for when you
 * want to draw the Graphics of some object, but do not want to burden the drawer of details of the different classes
 * that may have graphics to draw.
 */
public interface Drawable {

    public void draw(Graphics2D g);
}
