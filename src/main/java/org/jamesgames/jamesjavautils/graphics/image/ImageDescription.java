package org.jamesgames.jamesjavautils.graphics.image;

import net.jcip.annotations.Immutable;
import net.jcip.annotations.NotThreadSafe;
import org.jamesgames.jamesjavautils.graphics.Drawable;

/**
 * ImageDescription describes what a potential Image object should look like, by what graphics should be drawn to it,
 * the width and height of the potential image, as well as the initial transparency of the image.
 */
public class ImageDescription {
    private final Drawable getDrawableImageGraphics;
    private final int imageWidth;
    private final int imageHeight;
    private final int transparency;

    /**
     * Creates an ImageDescription which describes a potential image may look like
     *
     * @param getDrawableImageGraphics
     *         Graphic calls to use on the returned BufferedImage
     * @param imageWidth
     *         Width of the image to create
     * @param imageHeight
     *         Height of the image to create
     * @param transparency
     *         Transparency of the image to create, this would be a valid transparency value from java.awt.Transparency
     */
    public ImageDescription(Drawable getDrawableImageGraphics, int imageWidth, int imageHeight, int transparency) {
        this.getDrawableImageGraphics = getDrawableImageGraphics;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.transparency = transparency;
    }

    public Drawable getGetDrawableImageGraphics() {
        return getDrawableImageGraphics;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public int getTransparency() {
        return transparency;
    }
}
