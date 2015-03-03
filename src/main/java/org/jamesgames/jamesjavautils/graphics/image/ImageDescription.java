package org.jamesgames.jamesjavautils.graphics.image;

import org.jamesgames.jamesjavautils.graphics.Drawable;

import java.util.Objects;

/**
 * ImageDescription describes what a potential Image object should look like, by what graphics should be drawn to it,
 * the width and height of the potential image, as well as the initial transparency of the image.
 */
public class ImageDescription {
    private final Drawable drawableImageGraphics;
    private final int imageWidth;
    private final int imageHeight;
    private final int transparency;

    /**
     * Creates an ImageDescription which describes a potential image may look like
     *
     * @param drawableImageGraphics
     *         Graphic calls to use on the returned BufferedImage
     * @param imageWidth
     *         Width of the image to create
     * @param imageHeight
     *         Height of the image to create
     * @param transparency
     *         Transparency of the image to create, this would be a valid transparency value from java.awt.Transparency
     */
    public ImageDescription(Drawable drawableImageGraphics, int imageWidth, int imageHeight, int transparency) {
        this.drawableImageGraphics = Objects.requireNonNull(drawableImageGraphics, "Drawable cannot be null");
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.transparency = transparency;
    }

    public Drawable getDrawableImageGraphics() {
        return drawableImageGraphics;
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
