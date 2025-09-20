package UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;

public class SimpleSliderUI extends BasicSliderUI {

    private final Color trackColor;
    private final Color thumbColor;
    private final int trackHeight;
    private final int thumbRadius;

    public SimpleSliderUI(JSlider slider, Color trackColor, Color thumbColor, int trackHeight, int thumbRadius) {
        super(slider);
        this.trackColor = trackColor;
        this.thumbColor = thumbColor;
        this.trackHeight = trackHeight;
        this.thumbRadius = thumbRadius;
    }

    @Override
    public void paintTrack(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Rectangle trackBounds = trackRect;
        g2.setColor(trackColor);

        if (slider.getOrientation() == JSlider.HORIZONTAL) {
            int cy = trackBounds.y + trackBounds.height / 2 - trackHeight / 2;
            RoundRectangle2D trackShape = new RoundRectangle2D.Double(
                    trackBounds.x, cy, trackBounds.width, trackHeight, trackHeight, trackHeight);
            g2.fill(trackShape);
        } else {
            int cx = trackBounds.x + trackBounds.width / 2 - trackHeight / 2;
            RoundRectangle2D trackShape = new RoundRectangle2D.Double(
                    cx, trackBounds.y, trackHeight, trackBounds.height, trackHeight, trackHeight);
            g2.fill(trackShape);
        }

        g2.dispose();
    }

    @Override
    public void paintThumb(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(thumbColor);

        if (slider.getOrientation() == JSlider.HORIZONTAL) {
            int cy = thumbRect.y + thumbRect.height / 2;
            int cx = thumbRect.x + thumbRect.width / 2;
            g2.fillOval(cx - thumbRadius, cy - thumbRadius, thumbRadius * 2, thumbRadius * 2);
        } else {
            int cy = thumbRect.y + thumbRect.height / 2;
            int cx = thumbRect.x + thumbRect.width / 2;
            g2.fillOval(cx - thumbRadius, cy - thumbRadius, thumbRadius * 2, thumbRadius * 2);
        }

        g2.dispose();
    }

    @Override
    protected Dimension getThumbSize() {
        return new Dimension(thumbRadius * 2, thumbRadius * 2);
    }
    
    private boolean isDragging = false;

    @Override
    protected TrackListener createTrackListener(JSlider slider) {
        return new TrackListener() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (!slider.isEnabled()) {
                    return;
                }

                slider.requestFocus();
                isDragging = true;
                slider.setValueIsAdjusting(true);
                updateSliderValueFromMouse(e);
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                isDragging = false;
                slider.setValueIsAdjusting(false);
            }

            @Override
            public void mouseDragged(java.awt.event.MouseEvent e) {
                if (!slider.isEnabled() || !isDragging) {
                    return;
                }
                updateSliderValueFromMouse(e);
            }

            private void updateSliderValueFromMouse(java.awt.event.MouseEvent e) {
                int value;
                if (slider.getOrientation() == JSlider.HORIZONTAL) {
                    int mouseX = e.getX();
                    int trackStart = trackRect.x;
                    int trackLength = trackRect.width;
                    float percent = (float) (mouseX - trackStart) / (float) trackLength;
                    value = slider.getMinimum() + Math.round(percent * (slider.getMaximum() - slider.getMinimum()));
                } else {
                    int mouseY = e.getY();
                    int trackStart = trackRect.y;
                    int trackLength = trackRect.height;
                    float percent = 1f - (float) (mouseY - trackStart) / (float) trackLength;
                    value = slider.getMinimum() + Math.round(percent * (slider.getMaximum() - slider.getMinimum()));
                }

                value = Math.max(slider.getMinimum(), Math.min(slider.getMaximum(), value));
                slider.setValue(value);
            }
        };
    }

    @Override
    public void paintFocus(Graphics g) {
    }
}
