package UI;

import java.awt.Color;
import javax.swing.AbstractButton;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JSlider;
import javax.swing.JSplitPane;

public class UIClass {

    public UIClass() {
        // Constructor
    }

    public static void setUIScrollBar(JScrollBar scrollBar, Color trackColor, Color thumbColor, int width, int round) {
        scrollBar.setUI(new SimpleScrollBarUI(trackColor, thumbColor, width, round));
    }

    public static void setUICombo(JComboBox combo, Color background, Color foreground) {
        combo.setUI(new SimpleComboUI(background, foreground));
        if (combo.getRenderer() == null) {
            combo.setRenderer(new DefaultListCellRenderer());
        }
    }

    public static void setUIButton(AbstractButton button, Color background, Color foreground, int borderSize, int radius) {
        button.setUI(new RoundedButtonUI(background, foreground, borderSize, radius));
    }

    public static void setUIRadioButton(JRadioButton radioButton, Color background, Color selectedColor, int borderSize, int radius, boolean paintBackground) {
        radioButton.setUI(new FlexibleRadioButtonUI(borderSize, radius, paintBackground));
        radioButton.setBackground(background);
        radioButton.setForeground(selectedColor);
    }

    public static void setUISlider(JSlider slider, Color trackColor, Color thumbColor, int trackHeight, int thumbRadius) {
        slider.setUI(new SimpleSliderUI(slider, trackColor, thumbColor, trackHeight, thumbRadius));
        slider.setOpaque(false);
    }

    public static void setSplitPaneUI(JSplitPane splitPane, Color background, Color dividerTextColor, int dividerSize, boolean continuousLayout) {
        splitPane.setBackground(background);
        splitPane.setDividerSize(dividerSize);
        splitPane.setContinuousLayout(continuousLayout);
        splitPane.setUI(new CustomSplitPaneUI(background, dividerTextColor));
    }
}
