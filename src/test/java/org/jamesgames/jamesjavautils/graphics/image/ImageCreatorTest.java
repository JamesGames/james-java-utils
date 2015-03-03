package org.jamesgames.jamesjavautils.graphics.image;

import org.junit.Test;

import java.awt.*;

public class ImageCreatorTest {

    @Test
    public void testCreateImage() throws Exception {
        new ImageCreator()
                .createImage(new ImageDescription(g -> g.drawLine(1, 1, 30, 30), 50, 50, Transparency.OPAQUE));
    }
}