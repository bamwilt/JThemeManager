package UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class SimpleScrollBarUI extends BasicScrollBarUI {

    private Color trackColor_;
    private Color thumbColor_;
    private int round = 25;
    private int scrollWidth = 10;

    public SimpleScrollBarUI(Color trackColor, Color thumbColor, int width, int round) {
        this.trackColor_ = trackColor;
        this.thumbColor_ = thumbColor;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getScrollWidth() {
        return scrollWidth;
    }

    public void setScrollWidth(int scrollWidth) {
        this.scrollWidth = scrollWidth;
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(trackColor_);
        g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        g2.dispose();
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
            return;
        }
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(thumbColor_);
        g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, round, round);
        g2.dispose();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        JButton btn = new JButton();
        btn.setPreferredSize(new Dimension(20, 20));
        btn.setBorder(null);
        btn.setOpaque(true);
        btn.setBackground(thumbColor);
        return btn;
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        JButton btn = new JButton();
        btn.setPreferredSize(new Dimension(20, 20));
        btn.setBorder(null);
        btn.setBackground(thumbColor);
        return btn;
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
        if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
            return new Dimension(scrollWidth, 100);
        } else {
            return new Dimension(100, scrollWidth);
        }
    }
}