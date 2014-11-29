package org.jamesgames.jamesjavautils.gui.swing;

import javax.swing.*;
import java.awt.*;

/**
 * SwingHelper is a utility class that contains static methods that help simplify actions against Swing objects
 *
 * @author James Murphy
 */
public final class SwingHelper {

    private SwingHelper() {
        throw new UnsupportedOperationException("SwingHelper is a static utility class, not meant for instantiation.");
    }

    /**
     * Put a series of {@link JComponent}s in a {@link JPanel} which has a {@link FlowLayout}.
     *
     * @param position
     *         Position of components, left, right, or center justified (for example, {@link
     *         java.awt.FlowLayout#CENTER)}
     * @param opaque
     *         True if the returned JPanel should be opaque
     * @param firstComponent
     *         The first {@link JComponent} to add
     * @param remainingComponents
     *         The rest of the {@link JComponent}s to add
     * @return A {@link JPanel} with a {@link FlowLayout} which contains all the passed {@link JComponent}s.
     */
    public static JPanel putComponentsInFlowLayoutPanel(int position, boolean opaque,
            JComponent firstComponent, JComponent... remainingComponents) {
        JPanel flowLayoutPanel = new JPanel(new FlowLayout(position));
        flowLayoutPanel.setOpaque(opaque);
        flowLayoutPanel.add(firstComponent);
        for (JComponent component : remainingComponents) {
            flowLayoutPanel.add(component);
        }
        return flowLayoutPanel;
    }

    /**
     * Change the font of all the passed {@link JComponent}s to the supplied {@link Font}.
     *
     * @param font
     *         The {@link Font} to assign to the passed {@link JComponent}s.
     * @param firstComponent
     *         The first {@link JComponent} to assign the font to.
     * @param remainingComponents
     *         The rest of the {@link JComponent}s to assign the font to.
     */
    public static void changeFontOfComponents(Font font,
            JComponent firstComponent, JComponent... remainingComponents) {
        firstComponent.setFont(font);
        for (JComponent component : remainingComponents) {
            component.setFont(font);
        }
    }


    /**
     * @return A rectangle whose bounds encompass all displays and perhaps more if the displays are not the same
     * resolution or are not positioned adjacently in a line all starting on the same x or y position.
     */
    public static Rectangle getBoundsOfAllDisplays() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        int minX = 0;
        int minY = 0;
        int maxX = 0;
        int maxY = 0;
        for (GraphicsDevice gd : ge.getScreenDevices()) {
            Rectangle b = gd.getDefaultConfiguration().getBounds();
            minX = Math.min(minX, b.x);
            minY = Math.min(minY, b.y);
            maxX = Math.max(maxX, b.x + b.width);
            maxY = Math.max(maxY, b.y + b.height);
        }
        return new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }

    /**
     * Retrieves the bounds represents the available space on the Window's display that is not used up by things like
     * the task bar or dock.
     *
     * @param window
     *         The window to center
     * @return Bounds of the display's available space
     */
    public static Rectangle getBoundsOfAvailableDisplaySpace(Window window) {
        GraphicsConfiguration graphicsConfigToUse = window.getGraphicsConfiguration();
        // If the Window does not have a GraphicsConfiguration yet (perhaps when the Window is not set up yet) then use the default screen's GraphicsConfiguration.
        if (graphicsConfigToUse == null) {
            graphicsConfigToUse = GraphicsEnvironment
                    .getLocalGraphicsEnvironment().getDefaultScreenDevice()
                    .getDefaultConfiguration();
        }
        Rectangle boundsOfEntireScreen = graphicsConfigToUse.getBounds();
        Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(graphicsConfigToUse);

        return new Rectangle(screenInsets.left, screenInsets.top,
                boundsOfEntireScreen.width - screenInsets.left - screenInsets.right,
                boundsOfEntireScreen.height - screenInsets.top - screenInsets.bottom);
    }

    /**
     * Centers the {@link java.awt.Window} on the display
     *
     * @param window
     *         Window to center
     */
    public static void centerWindow(Window window) {
        Rectangle boundsOfAvailableSpace = getBoundsOfAvailableDisplaySpace(window);
        int offsetX = (boundsOfAvailableSpace.width - window.getWidth()) / 2;
        int offsetY = (boundsOfAvailableSpace.height - window.getHeight()) / 2;
        window.setLocation(offsetX, offsetY);
    }

    /**
     * Expands and repositions the {@link java.awt.Window} to stretch across all displays
     *
     * @param window
     *         Window to resize and reposition
     */
    public static void expandWindowAcrossAllDisplays(Window window) {
        Rectangle boundsAcrossAllDisplays = getBoundsOfAllDisplays();
        window.setLocation(boundsAcrossAllDisplays.x, boundsAcrossAllDisplays.y);
        window.setSize(boundsAcrossAllDisplays.width, boundsAcrossAllDisplays.height);
    }
}
