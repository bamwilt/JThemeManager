package JWindowPlus;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class windowResizer extends MouseAdapter {

    private final JFrame frame;
    private final JComponent JframePanel;
    private final JComponent TitleBar;
    private int MARGIN;
    private Dimension MIN_SIZE;
    private Dimension MAX_SIZE;
    private final Map<String, Cursor> cursorMap = new HashMap<>(){
        {
            put("SE", Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR)); // Southeast (Bottom-Right)
            put("NE", Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR)); // Northeast (Top-Right)
            put("SW", Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR)); // Southwest (Bottom-Left)
            put("NW", Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR)); // Northwest (Top-Left)
            put("E", Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));   // East (Right)
            put("W", Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));   // West (Left)
            put("S", Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));   // South (Bottom)
            put("N", Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));   // North (Top)
        }
    };
    
    private Point dragStart;
    private Rectangle frameStart;
    private Point lastResizePoint;
    private Cursor lastResizeCursor;
    private boolean isDragged = false;
    private boolean f11Pressed = false;
    private javax.swing.Timer resizeTimer;

    private Border lineBorder = new LineBorder(new Color(187, 187, 187, 50), 1);
    private Border marginBorder;

    private boolean isFullScreen = false;
    private Rectangle windowFrame;

    public windowResizer(JFrame frame, JComponent panel, JComponent titlebar,
                             int margin, Dimension minSize, Dimension maxSize) {
        this.frame = frame;
        this.JframePanel = panel;
        this.TitleBar = titlebar;
        this.MARGIN = margin;
        this.MIN_SIZE = minSize;
        this.MAX_SIZE = maxSize;

        marginBorder = BorderFactory.createEmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN);
        JframePanel.setBorder(BorderFactory.createCompoundBorder(lineBorder, marginBorder));

        setupTimer();
        setupF11Event();
    }

    private void setupTimer() {
        resizeTimer = new javax.swing.Timer(16, e -> performResize());
        resizeTimer.setRepeats(true);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        dragStart = e.getLocationOnScreen();
        frameStart = frame.getBounds();
        lastResizeCursor = getCursorFromPosition(e.getPoint());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        isDragged = false;
        JframePanel.setCursor(Cursor.getDefaultCursor());
        if (resizeTimer.isRunning()) resizeTimer.stop();
        lastResizePoint = null;
        lastResizeCursor = null;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point current = e.getLocationOnScreen();
        boolean resizing = lastResizeCursor != null && lastResizeCursor.getType() != Cursor.DEFAULT_CURSOR;

        if (resizing) {
            lastResizePoint = current;
            if (!resizeTimer.isRunning()) resizeTimer.start();
        } else {
            int dx = current.x - dragStart.x;
            int dy = current.y - dragStart.y;
            frame.setLocation(frameStart.x + dx, frameStart.y + dy);
        }
        isDragged = true;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!isDragged) lastResizeCursor = getCursorFromPosition(e.getPoint());
    }

    private Cursor getCursorFromPosition(Point p) {
        String key = "";
        if (p.y <= MARGIN) key += "N";
        if (p.y >= JframePanel.getHeight() - MARGIN) key += "S";
        if (p.x >= JframePanel.getWidth() - MARGIN) key += "E";
        if (p.x <= MARGIN) key += "W";

        Cursor c = cursorMap.getOrDefault(key, Cursor.getDefaultCursor());
        JframePanel.setCursor(c);
        return c;
    }

    private void performResize() {
        if (lastResizePoint == null || lastResizeCursor == null) return;

        Rectangle b = frame.getBounds();
        int type = lastResizeCursor.getType();

        int dx = lastResizePoint.x - (b.x + b.width);
        int dy = lastResizePoint.y - (b.y + b.height);
        int dxLeft = lastResizePoint.x - b.x;
        int dyTop = lastResizePoint.y - b.y;

        switch (type) {
            case Cursor.E_RESIZE_CURSOR -> b.width += dx;
            case Cursor.W_RESIZE_CURSOR -> { b.width -= dxLeft; b.x += dxLeft; }
            case Cursor.S_RESIZE_CURSOR -> b.height += dy;
            case Cursor.N_RESIZE_CURSOR -> { b.height -= dyTop; b.y += dyTop; }
            case Cursor.SE_RESIZE_CURSOR -> { b.width += dx; b.height += dy; }
            case Cursor.SW_RESIZE_CURSOR -> { b.width -= dxLeft; b.x += dxLeft; b.height += dy; }
            case Cursor.NE_RESIZE_CURSOR -> { b.width += dx; b.height -= dyTop; b.y += dyTop; }
            case Cursor.NW_RESIZE_CURSOR -> { b.width -= dxLeft; b.x += dxLeft; b.height -= dyTop; b.y += dyTop; }
        }

        Dimension size = new Dimension(b.width, b.height);
        if (isMinSize(size) && isMaxSize(size) && isInUsableArea(b))  {
            frame.setBounds(b);
            frame.revalidate();
            frame.repaint();
        }
    }

    private boolean isMinSize(Dimension size) {
        return size.width >= MIN_SIZE.width && size.height >= MIN_SIZE.height;
    }

    private boolean isMaxSize(Dimension size) {
        return size.width <= MAX_SIZE.width && size.height <= MAX_SIZE.height;
    }

    private boolean isInUsableArea(Rectangle bounds) {
        Rectangle usable = getUsableScreenArea();
        return bounds.x >= usable.x
                && bounds.y >= usable.y
                && bounds.x + bounds.width <= usable.x + usable.width
                && bounds.y + bounds.height <= usable.y + usable.height;
    }

    private Rectangle getBoundsFullScreen() {
        GraphicsConfiguration config = frame.getGraphicsConfiguration();
        return config.getBounds();
    }

    private Insets getScreenInsets() {
        GraphicsConfiguration config = frame.getGraphicsConfiguration();
        return Toolkit.getDefaultToolkit().getScreenInsets(config);
    }

    public Rectangle getUsableScreenArea() {
        Rectangle screenBounds = getBoundsFullScreen();
        Insets insets = getScreenInsets();
        return new Rectangle(
                screenBounds.x + insets.left,
                screenBounds.y + insets.top,
                screenBounds.width - insets.left - insets.right,
                screenBounds.height - insets.top - insets.bottom
        );
    }
    private void setupF11Event() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            if (e.getKeyCode() == KeyEvent.VK_F11) {
                if (e.getID() == KeyEvent.KEY_PRESSED && !f11Pressed) {
                    f11Pressed = true;
                    toggleFullScreen();
                    return true;
                }
                if (e.getID() == KeyEvent.KEY_RELEASED) {
                    f11Pressed = false;
                    return true;
                }
            }
            return false;
        });
    }

    private int margin_temp;
   private int  previousState;
   
    private void toggleFullScreen() {
        if (!isFullScreen) {
            previousState = frame.getExtendedState();
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            if (TitleBar != null) JframePanel.remove(TitleBar);
            margin_temp = MARGIN;
                setMarginBorder(0);
        } else {
            frame.setExtendedState(previousState);
            if (TitleBar != null) JframePanel.add(TitleBar, BorderLayout.NORTH);
                setMarginBorder(margin_temp);
        }

        JframePanel.revalidate();
        JframePanel.repaint();
        isFullScreen = !isFullScreen;
    }


    public void setMarginBorder(int margin) {
        MARGIN = Math.max(0, Math.min(300, margin));
        MIN_SIZE = new Dimension(320, TitleBar.getHeight() + (MARGIN * 2) - 1);
        marginBorder = BorderFactory.createEmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN);
        JframePanel.setBorder(BorderFactory.createCompoundBorder(lineBorder, marginBorder));
    }
    
}
