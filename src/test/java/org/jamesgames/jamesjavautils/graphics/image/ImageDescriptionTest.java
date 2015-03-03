package org.jamesgames.jamesjavautils.graphics.image;

import org.jamesgames.jamesjavautils.graphics.Drawable;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;


public class ImageDescriptionTest {
    private final Drawable exampleDrawable = g -> {
    };

    private final int exampleImageWidth = 111;
    private final int exampleImageHeight = 222;
    private final int exampleTransparency = Transparency.TRANSLUCENT;


    private final ImageDescription exampleDescription =
            new ImageDescription(exampleDrawable, exampleImageWidth, exampleImageHeight, exampleTransparency);

    @Test
    public void testGetGetDrawableImageGraphics() throws Exception {
        assertSame(exampleDescription.getDrawableImageGraphics(), exampleDrawable);
    }

    @Test
    public void testGetImageWidth() throws Exception {
        assertEquals(exampleDescription.getImageWidth(), exampleImageWidth);
    }

    @Test
    public void testGetImageHeight() throws Exception {
        assertEquals(exampleDescription.getImageHeight(), exampleImageHeight);
    }

    @Test
    public void testGetTransparency() throws Exception {
        assertEquals(exampleDescription.getTransparency(), exampleTransparency);
    }
}