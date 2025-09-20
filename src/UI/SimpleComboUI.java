package UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.ComboPopup;

public class SimpleComboUI extends BasicComboBoxUI {

    private JButton arrowButton_;
    private Color BACKGROUND;
    private Color FOREGROUND;

    public SimpleComboUI(Color BackG, Color ForeG) {
        BACKGROUND = BackG;
        FOREGROUND = ForeG;
    }

    public void UpdateNewColorUi(Color BackG, Color ForeG) {
        BACKGROUND = BackG;
        FOREGROUND = ForeG;
        arrowButton_.setForeground(FOREGROUND);
        popupList.setBackground(BACKGROUND);
        popupList.setForeground(FOREGROUND);
    }

    @Override
    protected JButton createArrowButton() {
        arrowButton_ = new JButton("⌵");
        arrowButton_.setBorder(BorderFactory.createEmptyBorder());
        arrowButton_.setForeground(FOREGROUND);
        arrowButton_.setBackground(BACKGROUND);
        return arrowButton_;
    }

    private JList<?> popupList;

    @Override
    protected ComboPopup createPopup() {
        ComboPopup popupOptions = super.createPopup();
        popupList = popupOptions.getList();
        popupList.setBackground(BACKGROUND);
        popupList.setForeground(FOREGROUND);

        JScrollPane scrollPane = (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, popupList);
        if (scrollPane != null) {
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.getVerticalScrollBar().setUI(
                    new SimpleScrollBarUI(BACKGROUND, FOREGROUND, 4, 2)
            );
        }

        return popupOptions;
    }

    @Override
    public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
        super.paintCurrentValue(g, bounds, false);
    }
}