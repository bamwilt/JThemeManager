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
import javax.swing.plaf.basic.BasicCheckBoxUI;

public class SimpleCheckBoxUI extends BasicCheckBoxUI {

    private int borderSize;
    private int backgroundRadius;
    private boolean paintBackground;
    private int minBoxSize = 8; 

    public SimpleCheckBoxUI() {
        this(1, 8, false);
    }

    public SimpleCheckBoxUI(int borderSize, int backgroundRadius, boolean paintBackground) {
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

        FontMetrics fm = g2.getFontMetrics();
        int textHeight = fm.getHeight();
        
        int boxSize = Math.max(textHeight - 4, minBoxSize);
        
        // Pintar fondo si está habilitado
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

        int boxY = (height - boxSize) / 2;
        int boxX = 8;

        Color fillColor = b.getForeground();
        Color borderColor = b.getForeground();

        if (!b.isEnabled()) {
            fillColor = fillColor.brighter().brighter();
            borderColor = borderColor.brighter().brighter();
        } else if (b.getModel().isPressed()) {
            fillColor = fillColor.darker();
            borderColor = borderColor.darker();
        } else if (b.getModel().isRollover()) {
            fillColor = fillColor.brighter();
            borderColor = borderColor.brighter();
        }

        int margin = borderSize;
        int outerX = boxX + margin;
        int outerY = boxY + margin;
        int outerWidth = boxSize - 2 * margin;
        int outerHeight = boxSize - 2 * margin;

        if (outerWidth > 0 && outerHeight > 0) {
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(borderSize));
            g2.drawRoundRect(outerX, outerY, outerWidth, outerHeight, 4, 4);

            if (b.isSelected()) {
                int innerMargin = margin * 2;
                int innerWidth = Math.max(outerWidth - 2 * innerMargin, 1);
                int innerHeight = Math.max(outerHeight - 2 * innerMargin, 1);
                
                g2.setColor(fillColor);
                g2.fillRect(
                    outerX + innerMargin,
                    outerY + innerMargin,
                    innerWidth,
                    innerHeight
                );
            }
        }

        int textX = boxX + boxSize + 8;
        int textY = (height - textHeight) / 2 + fm.getAscent();

        g2.setFont(b.getFont());
        g2.setColor(b.isEnabled() ? b.getForeground() : b.getForeground().brighter().brighter());
        g2.drawString(b.getText(), textX, textY);

        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
        AbstractButton b = (AbstractButton) c;
        FontMetrics fm = b.getFontMetrics(b.getFont());
        int textHeight = fm.getHeight();
        int boxSize = Math.max(textHeight - 4, minBoxSize);
        int width = fm.stringWidth(b.getText()) + boxSize + 20;
        int height = textHeight + 10;
        return new Dimension(width, height);
    }
    
    public void setMinBoxSize(int minBoxSize) {
        this.minBoxSize = minBoxSize;
    }
    
    public int getMinBoxSize() {
        return minBoxSize;
    }
}