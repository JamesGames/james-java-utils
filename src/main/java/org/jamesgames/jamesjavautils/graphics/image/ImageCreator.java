package org.jamesgames.jamesjavautils.graphics.image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

/**
 * ImageCreator creates a BufferedImage with the drawn graphics from the description of a passed ImageDescription
 * object.
 */
public class ImageCreator {

    /**
     * Creates a compatible BufferedImage and has a data layout and color model compatible with the local graphics
     * environment's default screen device's default graphic configuration (as in: GraphicsEnvironment.
     * getLocalGraphicsEnvironment(). getDefaultScreenDevice(). getDefaultConfiguration() )
     *
     * @param imageDescription
     *         The description of the image to create
     * @return A BufferedImage fitting the description of the passed parameters
     */
    public BufferedImage createImage(ImageDescription imageDescription) {
        GraphicsConfiguration defaultConfiguration = GraphicsEnvironment
                .getLocalGraphicsEnvironment().getDefaultScreenDevice()
                .getDefaultConfiguration();

        BufferedImage createdImage = defaultConfiguration
                .createCompatibleImage(
                        Objects.requireNonNull(imageDescription, "ImageDescription cannot be null").getImageWidth(),
                        imageDescription.getImageHeight(),
                        imageDescription.getTransparency());
        imageDescription.getDrawableImageGraphics().draw(createdImage.createGraphics());

        return createdImage;
    }

}
