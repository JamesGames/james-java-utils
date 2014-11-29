package org.jamesgames.jamesjavautils.gui.swing;

import javax.swing.*;
import java.awt.*;

/**
 * JFrameSizedAfterInsets is a {@link javax.swing.JFrame} that will expand it's size to have as much width and height in
 * room available within the borders and title bar of the JFrame that was supplied during construction. Meaning, if a
 * user wanted to construct this JFrame, and supplied a size of 200 by 300, but the {@link java.awt.Insets} added 15
 * pixels to all four sides, the resulting JFrame size would be 230 by 330. JFrameSizedAfterInsets also has the ability
 * to be centered on the display.
 *
 * @author James Murphy
 */
public class JFrameSizedAfterInsets extends JFrame {
    private boolean isAreaResizedToNotCountInsets = false;
    private boolean isJFrameCenteringRequired = false;

    public JFrameSizedAfterInsets(int width, int height) {
        this.setSize(width, height);
    }

    public JFrameSizedAfterInsets(int width, int height, boolean centerJFrameOnDisplay) {
        this(width, height);
        isJFrameCenteringRequired = centerJFrameOnDisplay;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (!isAreaResizedToNotCountInsets && this.isVisible()) {
            Insets insets = this.getInsets();
            int expandWidthBy = insets.left + insets.right;
            int expandHeightBy = insets.top + insets.bottom;
            this.setSize(this.getWidth() + expandWidthBy, this.getHeight() + expandHeightBy);

            isAreaResizedToNotCountInsets = true;
        }
        if (isJFrameCenteringRequired && isAreaResizedToNotCountInsets) {
            // Only center after the frame has been sized correctly
            SwingHelper.centerWindow(this);

            isJFrameCenteringRequired = false;
        }
    }
}
