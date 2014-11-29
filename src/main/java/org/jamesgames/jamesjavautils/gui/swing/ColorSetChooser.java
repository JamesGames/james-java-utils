package org.jamesgames.jamesjavautils.gui.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ColorSetChooser is a Swing {@link javax.swing.JPanel} stores a {@link java.util.Set} of colors that a user can
 * selects. A future improvement would be to change ColorSetChooser to be a JComponent instead of a JPanel.
 *
 * @author James Murphy
 */
public class ColorSetChooser extends JPanel {
    private static final int preferableSizeBetweenColorButtons = 75;

    private final JLabel warningOnAlreadyPickedColorLabel = new JLabel();
    private int currentNumberOfColumnsForColorButtons = 1;
    private final JPanel colors = new JPanel(new GridLayout(0, currentNumberOfColumnsForColorButtons));
    private final Set<Color> colorsSelected = new HashSet<>();
    private final List<JPanel> panelsHoldingColorButtons = new ArrayList<>();

    /**
     * Creates a new ColorSetChooser
     *
     * @param titleOfColorListChooser
     *         Title that will be in a border around the component
     * @param preferredWidthOfColorSet
     *         Preferred width of the scrollable set of colors
     * @param preferredHeightOfColorSet
     *         Preferred height of the scrollable set of colors
     */
    public ColorSetChooser(String titleOfColorListChooser, int preferredWidthOfColorSet,
            int preferredHeightOfColorSet) {
        setLayout(new BorderLayout());
        warningOnAlreadyPickedColorLabel.setForeground(Color.red);
        colors.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                tryToRepositionColorButtons();
            }
        });
        JScrollPane scrollPaneContainingColorsPanel = new JScrollPane(colors);
        scrollPaneContainingColorsPanel
                .setPreferredSize(new Dimension(preferredWidthOfColorSet, preferredHeightOfColorSet));
        JButton addColor = new JButton("Add a color");
        addColor.addActionListener(e -> {
            Color newColorChosen = JColorChooser.showDialog(this, "Choose Color", Color.WHITE);
            if (newColorChosen != null) {
                boolean notAlreadyInSet = colorsSelected.add(newColorChosen);
                if (notAlreadyInSet) {
                    JButton buttonForColor = new JButton("X");
                    JPanel panelWithFlowLayoutContainingColorButton = new JPanel(new FlowLayout());
                    panelWithFlowLayoutContainingColorButton.add(buttonForColor);
                    buttonForColor.setBackground(newColorChosen);
                    buttonForColor.addActionListener(e1 -> {
                        colorsSelected.remove(newColorChosen);
                        colors.remove(panelWithFlowLayoutContainingColorButton);
                        panelsHoldingColorButtons.remove(panelWithFlowLayoutContainingColorButton);
                        ColorSetChooser.this.revalidate();
                        ColorSetChooser.this.repaint();
                    });
                    colors.add(panelWithFlowLayoutContainingColorButton);
                    panelsHoldingColorButtons.add(panelWithFlowLayoutContainingColorButton);
                    tryToRepositionColorButtons();
                    ColorSetChooser.this.revalidate();
                    ColorSetChooser.this.repaint();

                    warningOnAlreadyPickedColorLabel.setText("");
                } else {
                    warningOnAlreadyPickedColorLabel.setText("Color already chosen");
                }
            }
        });
        add(scrollPaneContainingColorsPanel,
                BorderLayout.NORTH);
        add(SwingHelper.putComponentsInFlowLayoutPanel(FlowLayout.CENTER, false, addColor),
                BorderLayout.CENTER);
        add(SwingHelper.putComponentsInFlowLayoutPanel(FlowLayout.CENTER, false, warningOnAlreadyPickedColorLabel),
                BorderLayout.SOUTH);
        setBorder(BorderFactory.createTitledBorder(titleOfColorListChooser));
    }

    private void tryToRepositionColorButtons() {
        int numberOfColorCols = Math.max(1, colors.getWidth() / preferableSizeBetweenColorButtons);
        if (numberOfColorCols != currentNumberOfColumnsForColorButtons) {
            colors.setLayout(new GridLayout(0, numberOfColorCols));
            colors.removeAll();
            panelsHoldingColorButtons.forEach(colors::add);
            currentNumberOfColumnsForColorButtons = numberOfColorCols;
        }
    }

    /**
     * @return A {@link java.util.Set} containing the colors selected by this ColorListChooser (set returned is free to
     * be modified).
     */
    public Set<Color> getColors() {
        return new HashSet<>(colorsSelected);
    }

}
