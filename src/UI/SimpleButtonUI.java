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
import javax.swing.plaf.basic.BasicButtonUI;

public class SimpleButtonUI extends BasicButtonUI {

    private Color colorBackground;
    private Color colorHover;
    private Color colorClick;
    private Color borderColor;
    private int borderSize;
    private int radius;

    public SimpleButtonUI() {
        this(new Color(230, 230, 255), new Color(50, 50, 50), 1, 10);
    }

    public SimpleButtonUI(Color background, Color borderColor, int borderSize, int radius) {
        this.colorBackground = background;
        this.colorHover = background.brighter();
        this.colorClick = background.darker();
        this.borderColor = borderColor;
        this.borderSize = borderSize;
        this.radius = radius;
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        AbstractButton button = (AbstractButton) c;
        button.setOpaque(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
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
        RoundRectangle2D rect = new RoundRectangle2D.Double(0, 0, width - 1, height - 1, radius, radius);

        Color[] highlights = getHoverAndClickColors(colorBackground);
        Color colorHover_ = highlights[0];
        Color colorClick_ = highlights[1];

        if (!b.isEnabled()) {
            g2.setColor(new Color(200, 200, 200, 120));
            g2.fill(rect);
            g2.setColor(Color.GRAY);
        } else if (b.getModel().isPressed()) {
            g2.setColor(colorClick_);
            g2.fill(rect);
            g2.setColor(b.getForeground());
        } else if (b.getModel().isRollover()) {
            g2.setColor(colorHover_);
            g2.fill(rect);
            g2.setColor(b.getForeground());
        } else {
            g2.setColor(colorBackground);
            g2.fill(rect);
            g2.setColor(b.getForeground());
        }

        if (borderSize > 0) {
            g2.setStroke(new BasicStroke(borderSize));
            g2.setColor(borderColor);
            g2.draw(rect);
        }

        String text = b.getText();
        FontMetrics fm = g2.getFontMetrics();
        int x = (width - fm.stringWidth(text)) / 2;
        int y = (height - fm.getHeight()) / 2 + fm.getAscent();

        if (!b.isEnabled()) {
            g2.setColor(Color.GRAY);
        }

        g2.setFont(b.getFont());
        g2.drawString(text, x, y);

        g2.dispose();
    }

    private boolean isColorDark(Color c) {
        double luminance = (0.299 * c.getRed() + 0.587 * c.getGreen() + 0.114 * c.getBlue()) / 255;
        return luminance < 0.5;
    }

    private Color adjustHighlight(Color base, float factor, boolean lighten) {
        float[] hsb = Color.RGBtoHSB(base.getRed(), base.getGreen(), base.getBlue(), null);
        float brightness = hsb[2];

        if (lighten) {
            brightness = Math.min(1f, brightness + factor);
        } else {
            brightness = Math.max(0f, brightness - factor);
        }

        return Color.getHSBColor(hsb[0], hsb[1], brightness);
    }

    private Color[] getHoverAndClickColors(Color base) {
        boolean isDark = isColorDark(base);

        if (isDark) {
            return new Color[]{
                adjustHighlight(base, 0.2f, true),
                adjustHighlight(base, 0.4f, true)
            };
        } else {
            return new Color[]{
                adjustHighlight(base, 0.1f, false),
                adjustHighlight(base, 0.2f, false)
            };
        }
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
        AbstractButton b = (AbstractButton) c;
        FontMetrics fm = b.getFontMetrics(b.getFont());
        int width = fm.stringWidth(b.getText()) + 30;
        int height = fm.getHeight() + 10;
        return new Dimension(width, height);
    }
}
