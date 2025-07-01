package UI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicRadioButtonUI;

public class FlexibleRadioButtonUI extends BasicRadioButtonUI {

    private int borderSize;
    private int backgroundRadius;
    private boolean paintBackground;

    public FlexibleRadioButtonUI() {
        this(1, 8, false);
    }

    public FlexibleRadioButtonUI(int borderSize, int backgroundRadius, boolean paintBackground) {
        this.borderSize = borderSize;
        this.backgroundRadius = backgroundRadius;
        this.paintBackground = paintBackground;
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        AbstractButton button = (AbstractButton) c;
        button.setOpaque(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 15));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        AbstractButton b = (AbstractButton) c;
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = b.getWidth();
        int height = b.getHeight();

        if (paintBackground) {
            Color bgColor;
            if (!b.isEnabled()) {
                bgColor = b.getBackground().darker().darker();
            } else if (b.getModel().isPressed()) {
                bgColor = b.getBackground().darker();
            } else if (b.getModel().isRollover()) {
                bgColor = b.getBackground().brighter();
            } else {
                bgColor = b.getBackground();
            }
            g2.setColor(bgColor);
            RoundRectangle2D backgroundRect = new RoundRectangle2D.Double(0, 0, width, height, backgroundRadius, backgroundRadius);
            g2.fill(backgroundRect);
        }

        int diameter = height / 2;
        int circleX = 8;
        int circleY = (height - diameter) / 2;

        Color fillColor = b.getForeground();
        Color borderColor = b.getForeground();

        if (!b.isEnabled()) {
            fillColor = fillColor.brighter().brighter();
            borderColor = borderColor.brighter().brighter();
            g2.setColor(borderColor);
        } else if (b.getModel().isPressed()) {
            fillColor = fillColor.darker();
            borderColor = borderColor.darker();
        } else if (b.getModel().isRollover()) {
            fillColor = fillColor.brighter();
            borderColor = borderColor.brighter();
        }

        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(borderSize));
        g2.drawOval(circleX, circleY, diameter, diameter);

        if (b.isSelected()) {
            int innerDiameter = diameter / 2;
            int innerX = circleX + (diameter - innerDiameter) / 2;
            int innerY = circleY + (diameter - innerDiameter) / 2;
            g2.setColor(fillColor);
            g2.fillOval(innerX, innerY, innerDiameter + 1, innerDiameter + 1);
        }

        String text = b.getText();
        FontMetrics fm = g2.getFontMetrics();
        int textX = circleX + diameter + 8;
        int textY = (height - fm.getHeight()) / 2 + fm.getAscent();

        g2.setFont(b.getFont());
        g2.setColor(b.isEnabled() ? b.getForeground() : b.getForeground().brighter().brighter());
        g2.drawString(text, textX, textY);

        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
        AbstractButton b = (AbstractButton) c;
        FontMetrics fm = b.getFontMetrics(b.getFont());
        int diameter = fm.getHeight();
        int width = fm.stringWidth(b.getText()) + diameter + 20;
        int height = fm.getHeight() + 10;
        return new Dimension(width, height);
    }
}