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

public class SimpleRadioButtonUI extends BasicRadioButtonUI {

    private int borderSize;
    private int backgroundRadius;
    private boolean paintBackground;
    private Color selectedColor;
    private int minDiameter = 8; 

    public SimpleRadioButtonUI() {
        this(1, 8, false, null);
    }

    public SimpleRadioButtonUI(int borderSize, int backgroundRadius, boolean paintBackground) {
        this(borderSize, backgroundRadius, paintBackground, null);
    }

    public SimpleRadioButtonUI(int borderSize, int backgroundRadius, boolean paintBackground, Color selectedColor) {
        this.borderSize = borderSize;
        this.backgroundRadius = backgroundRadius;
        this.paintBackground = paintBackground;
        this.selectedColor = selectedColor;
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

        FontMetrics fm = g2.getFontMetrics();
        int textHeight = fm.getHeight();
        
        int diameter = Math.max(textHeight - 4, minDiameter);
        int circleX = 8;
        int circleY = (height - diameter) / 2;

        Color borderColor = b.getForeground();
        Color innerColor = (selectedColor != null) ? selectedColor : b.getForeground();

        if (!b.isEnabled()) {
            borderColor = borderColor.brighter().brighter();
            innerColor = innerColor.brighter().brighter();
        } else if (b.getModel().isPressed()) {
            borderColor = borderColor.darker();
            innerColor = innerColor.darker();
        } else if (b.getModel().isRollover()) {
            borderColor = borderColor.brighter();
            innerColor = innerColor.brighter();
        }

        int margin = borderSize;
        int outerX = circleX + margin;
        int outerY = circleY + margin;
        int outerDiameter = diameter - 2 * margin;
        
        if (outerDiameter > 0) {
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(borderSize));
            g2.drawOval(outerX, outerY, outerDiameter, outerDiameter);

            if (b.isSelected()) {
                int innerMargin = margin * 2;
                int innerDiameter = Math.max(outerDiameter - 2 * innerMargin, 1);
                int innerX = outerX + innerMargin;
                int innerY = outerY + innerMargin;
                
                g2.setColor(innerColor);
                g2.fillOval(innerX, innerY, innerDiameter, innerDiameter);
            }
        }

        String text = b.getText();
        int textX = circleX + diameter + 8;
        int textY = (height - textHeight) / 2 + fm.getAscent();

        g2.setFont(b.getFont());
        g2.setColor(b.isEnabled() ? b.getForeground() : b.getForeground().brighter().brighter());
        g2.drawString(text, textX, textY);

        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
        AbstractButton b = (AbstractButton) c;
        FontMetrics fm = b.getFontMetrics(b.getFont());
        int textHeight = fm.getHeight();
        int diameter = Math.max(textHeight - 4, minDiameter);
        int width = fm.stringWidth(b.getText()) + diameter + 20;
        int height = textHeight + 10;
        return new Dimension(width, height);
    }
    
    public void setSelectedColor(Color selectedColor) {
        this.selectedColor = selectedColor;
    }
    
    public Color getSelectedColor() {
        return selectedColor;
    }
    
    public void setMinDiameter(int minDiameter) {
        this.minDiameter = minDiameter;
    }
    
    public int getMinDiameter() {
        return minDiameter;
    }
}