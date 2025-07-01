package JFramePlus;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class JavaWindowPlus extends JFrame {

    // <editor-fold desc="ɢᴇᴛ&ꜰᴏɴᴛ">
    public void addPlus(Component component) {
        CONTENTPANEL.add(component);
        repaintFrame();
    }

    public void addPlusinBorder(Component component, String directionBorderLayout) {
        if (!(CONTENTPANEL.getLayout() instanceof BorderLayout)) {
            CONTENTPANEL.setLayout(new BorderLayout());
        }
        CONTENTPANEL.add(component, directionBorderLayout);
        repaintFrame();
    }

    public void setContentPanelPlus(JPanel newPanel) {
        if (CONTENTPANEL != null) {
            JFramePanel.remove(CONTENTPANEL);
        }
        CONTENTPANEL = newPanel;
        CONTENTPANEL.setDoubleBuffered(true);
        JFramePanel.add(CONTENTPANEL, BorderLayout.CENTER);
        repaintFrame();
    }

    public JPanel getContentPanelPlus() {
        return CONTENTPANEL;
    }

    public void setBackgroundPlus(Color color) {
        this.COLORBORDER = color;
        repaintFrame();
    }

    public void setForegroundPlus(Color color) {
        this.COLORFOREICON = color;
        TitleBar.repaintForeIcon();
    }

    public void setResizeSubtractBorders() {
        this.setSize(JFRAME_SIZE.width - (MARGIN * 2), JFRAME_SIZE.height - (MARGIN * 2) - TITLEBAR_HEIGHT);
    }

    public void setMarginBorder(int margin) {
        MARGIN = Math.max(0, Math.min(300, margin));
        MIN_SIZE = new Dimension(320, TITLEBAR_HEIGHT + (MARGIN * 2) - 1);
        marginBorder = BorderFactory.createEmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN);
        JFramePanel.setBorder(BorderFactory.createCompoundBorder(lineBorder, marginBorder));
    }

    public int getMarginBorder() {
        return MARGIN;
    }

    public void setSizePlus(int width, int height) {
        width = Math.max(width + (MARGIN * 2), MIN_SIZE.width);
        height = Math.max(height + (MARGIN * 2) + TITLEBAR_HEIGHT, MIN_SIZE.height);
        JFRAME_SIZE = new Dimension(width, height);
        this.setSize(JFRAME_SIZE);
    }

    public void setFontTitleBar(Font font) {
        FONT_TITLE = font;
        TitleBar.revalidateTitle();
    }

    public void setTitlePlus(String title) {
        TITLE = title;
        setTitle(title);
        TitleBar.revalidateTitle();
    }

    public void setSizeTitleBar(int sizeTitle) {
        TITLEBAR_HEIGHT = sizeTitle;
        SIZE_BUTTONS = new Dimension(TITLEBAR_HEIGHT, TITLEBAR_HEIGHT);
        TitleBar.revalidateTitlebar();
        revalidate();
    }

    public void setResizablePlus(boolean isResizable) {
        RESIZABLE = isResizable;
    }

    public void setMaximumSizePlus(int width, int heigth) {
        MAX_SIZE = new Dimension(Math.max(MIN_SIZE.width, width), Math.max(MIN_SIZE.height, heigth));
        this.setSize(new Dimension(Math.min(MAX_SIZE.width, JFRAME_SIZE.width), Math.min(MAX_SIZE.height, JFRAME_SIZE.height)));
    }

    public void setMinimumSizePlus(int width, int heigth) {
        MIN_SIZE = new Dimension(Math.min(MAX_SIZE.width, width), Math.min(MAX_SIZE.height, heigth));
        this.setSize(new Dimension(Math.max(MIN_SIZE.width, JFRAME_SIZE.width), Math.max(MIN_SIZE.height, JFRAME_SIZE.height)));
    }

    public void setVisiblePlusRelativeTo(JComponent component) {
        this.setLocationRelativeTo(component);
        JFramePanel.revalidate();
        JFramePanel.repaint();
        this.setVisible(true);
    }

    public void setIconSimbol(String simbol) {
        TEXT_ICON = simbol;
    }

    public void setColorExitButton(Color color) {
        COLOR_EXIT = color;
        TitleBar.repaintButtons();
    }

    public void setSizeIconSimbol(int sizeFont) {
        setSizeIconSimbol(sizeFont, POINTPAIN_ICON);
    }

    public void setSizeIconSimbol(int sizeFont, Point location) {
        SIZEFONT_ICON = sizeFont;
        POINTPAIN_ICON = location;
        TitleBar.iconPanel.repaint();
        TitleBar.iconPanel.revalidate();
    }

    public void setTextButton(int numButton, String simbol) {
        numButton = Math.min(Math.min(numButton - 1, (BUTTONS.length - 1)), TEXTBUTTONS.size() - 1);

        TEXTBUTTONS.set(numButton, new JLabel(simbol));
        TEXTBUTTONS.get(numButton).setHorizontalAlignment(SwingConstants.CENTER);
        TEXTBUTTONS.get(numButton).setFont(new Font("Sungui IU", 0, 14));
        TEXTBUTTONS.get(numButton).setForeground(COLORFOREICON);

        BUTTONS[Math.min(numButton, BUTTONS.length - 1)].removeAll();
        BUTTONS[Math.min(numButton, BUTTONS.length - 1)].add(TEXTBUTTONS.get(numButton));
    }

    public void setNumButtons(int numButtons) {
        NUMBUTTONS = Math.min(numButtons, 14);
        TitleBar.removeButtons();
        BUTTONS = new JPanel[NUMBUTTONS];
        TitleBar.addButtons();
        TitleBar.updateSizeContentButtons();
        corretionNumButtons();
    }
    // </editor-fold> 
    //+++++++++++++++++++++START+++++++++++++++++++++++++++++++ //
    // Constructor
    private Color COLORFOREICON = Color.WHITE;// color Icono, Texto de Botones y titulos
    private Color COLORBORDER = new Color(25, 25, 30);//Color de Borde y title bar
    private Color COLORBUTTON_ENTERED = COLORBORDER.brighter();//Color Mouse Encima de los Botones 
    private Color COLOR_EXIT = new Color(255, 50, 50);

    private String TITLE = "";
    private Font FONT_TITLE = new Font("Dialog", Font.BOLD, 12);
    private int MARGIN = 5;//Margen del Borde Con el Contenido      

    private int NUMBUTTONS = 4;
    private int TITLEBAR_HEIGHT = 40;
    private Point POINTPAIN_ICON = new Point(12, 26);
    private int SIZEFONT_ICON = 16;
    private String TEXT_ICON = "♜";

    private Dimension SIZE_BUTTONS = new Dimension(TITLEBAR_HEIGHT, TITLEBAR_HEIGHT);

    private List<JLabel> TEXTBUTTONS = new ArrayList<>(Arrays.asList(
            new JLabel("✕"),
            new JLabel("🗖"),
            new JLabel("―"),
            new JLabel("🕂")));

    public JPanel[] BUTTONS = new JPanel[NUMBUTTONS];

    private JPanel CONTENTPANEL;
    private JTitleBar TitleBar;
    private JPanel JFramePanel = new JPanel();
    private Dimension MIN_SIZE = new Dimension(320, TITLEBAR_HEIGHT + (MARGIN * 2) - 1);
    private Dimension MAX_SIZE = new Dimension(32767, 32767);
    private Dimension JFRAME_SIZE = new Dimension(600, 400);
    private boolean RESIZABLE = true;

    public JavaWindowPlus() {
        CONTENTPANEL = new JPanel();
        CONTENTPANEL.setPreferredSize(new Dimension(500, 300));
        CONTENTPANEL.setLayout(new BorderLayout());
        initComponents();
    }

    public JavaWindowPlus(String title_) {
        TITLE = title_;
        CONTENTPANEL = new JPanel();
        CONTENTPANEL.setPreferredSize(new Dimension(500, 300));
        CONTENTPANEL.setLayout(new BorderLayout());
        initComponents();
    }

    public JavaWindowPlus(JPanel ContentPanel) {
        CONTENTPANEL = ContentPanel;
        initComponents();
    }

    public JavaWindowPlus(JPanel ContentPanel, String title_) {
        TITLE = title_;
        CONTENTPANEL = ContentPanel;
        initComponents();
    }

    Border lineBorder = new LineBorder(new Color(187, 187, 187, 50), 1);
    Border marginBorder = BorderFactory.createEmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN);

    private void initComponents() {
        getContentPane().setBackground(Color.RED);

        setLayout(new BorderLayout());
        int width = Math.max(CONTENTPANEL.getPreferredSize().width + (MARGIN * 2), MIN_SIZE.width);
        int height = Math.max(CONTENTPANEL.getPreferredSize().height + (MARGIN * 2) + TITLEBAR_HEIGHT, MIN_SIZE.height);
        JFRAME_SIZE = new Dimension(width, height);

        setSize(JFRAME_SIZE);
        setMinimumSize(MIN_SIZE);

        JFramePanel.setBorder(BorderFactory.createCompoundBorder(lineBorder, marginBorder));
        JFramePanel.setLayout(new BorderLayout());

        JFramePanel.setBackground(COLORBORDER);
        JFramePanel.add(CONTENTPANEL, BorderLayout.CENTER);

        TitleBar = new JTitleBar(this);
        JFramePanel.add(TitleBar, BorderLayout.NORTH);
        JFramePanel.setDoubleBuffered(true);
        TitleBar.setDoubleBuffered(true);
        CONTENTPANEL.setDoubleBuffered(true);
        add(JFramePanel);
        corretionNumButtons();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setLocationRelativeTo(null);
        setEventResize();
    }

    private void corretionNumButtons() {
        NUMBUTTONS = NUMBUTTONS - 1;//Correction array
    }

    private void repaintFrame() {
        JFramePanel.setBackground(COLORBORDER);
        TitleBar.setBackground(COLORBORDER);
        COLORBUTTON_ENTERED //  = COLOR if
                = (calculateLightness(COLORBORDER) >= 50) ? COLORBORDER.darker()
                : ((calculateLightness(COLORBORDER) >= 20) ? COLORBORDER.brighter()
                : illuminateColor(COLORBORDER));
        TitleBar.repaintButtons();
    }

    private MouseMotionAdapter listenerRedimension;
    private boolean isDragged;
    private boolean f11Pressed = false;

    private void setEventResize() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher((KeyEvent e) -> {
            if (e.getKeyCode() == KeyEvent.VK_F11) {
                if (e.getID() == KeyEvent.KEY_PRESSED && !f11Pressed) {
                    f11Pressed = true;
                    toggleFullScreen(this);
                    return true;
                }
                if (e.getID() == KeyEvent.KEY_RELEASED) {
                    f11Pressed = false;
                    return true;
                }
            }
            return false;
        });
        JFramePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                isDragged = false;
                JFramePanel.setCursor(Cursor.getPredefinedCursor((Cursor.DEFAULT_CURSOR)));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (isDragged) {
                    return;
                }
                JFramePanel.setCursor(Cursor.getPredefinedCursor((Cursor.DEFAULT_CURSOR)));
            }
        });

        listenerRedimension = new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!RESIZABLE) {
                    return;
                }

                SwingUtilities.convertPointToScreen(e.getPoint(), e.getComponent());
                resizeWindow(e);
                isDragged = true;
            }

            @Override
            public void mouseMoved(MouseEvent e) {

                if (isDragged || !RESIZABLE) {
                    return;
                }
                Point mouseinFrame = e.getPoint();
                getPositionBorder(mouseinFrame, JFramePanel);
            }
        };

        JFramePanel.addMouseMotionListener(listenerRedimension);
    }

    private boolean isFullScreen = false;
    private int margin = MARGIN;

    private void toggleFullScreen(JFrame frame) {

        if (!isFullScreen) {
            WindowFrame = getBounds();
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
            JFramePanel.remove(TitleBar);
            margin = MARGIN;
            setMarginBorder(0);
        } else {
            if (WindowFrame != null) {
                this.setExtendedState(JFrame.NORMAL);
                this.setMarginBorder(margin);
                JFramePanel.add(TitleBar, BorderLayout.NORTH);
            }
        }
        repaint();
        revalidate();
        isFullScreen = !isFullScreen;
    }

    private Cursor newCursor;
    private final Map<String, Cursor> cursorMap = new HashMap<>() {
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

    public void getPositionBorder(Point mouse, JComponent panel) {
        StringBuilder key = new StringBuilder();

        key.append(mouse.y <= MARGIN ? "N" : "");
        key.append(mouse.y >= panel.getHeight() - MARGIN ? "S" : "");
        key.append(mouse.x >= panel.getWidth() - MARGIN ? "E" : "");
        key.append(mouse.x <= MARGIN ? "W" : "");

        newCursor = cursorMap.getOrDefault(key.toString(), Cursor.getDefaultCursor());

        if (!panel.getCursor().equals(newCursor)) {
            panel.setCursor(newCursor);
        }
    }

    private final Map<Cursor, Runnable> ResizeMethod = new HashMap<>() {
        {
            put(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR), () -> setTopResize());
            put(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR), () -> setButtonResize());
            put(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR), () -> setRightResize());
            put(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR), () -> setLeftResize());

            put(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR), () -> setNEresize());
            put(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR), () -> setSWresize());
            put(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR), () -> setSEresize());
            put(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR), () -> setNWresize());
        }
    };

    Rectangle WindowFrame;
    MouseEvent MousePosition;

    private void resizeWindow(MouseEvent e) {
        MousePosition = e;
        WindowFrame = getBounds();

        if (JFramePanel.getCursor().getType() == Cursor.DEFAULT_CURSOR) {
            return;
        }
        ResizeMethod.get(JFramePanel.getCursor()).run();
        this.setBounds(WindowFrame);
        this.validate();
    }

    private void setNEresize() {
        setTopResize();
        setRightResize();
    }

    private void setNWresize() {
        setTopResize();
        setLeftResize();
    }

    private void setSWresize() {
        setButtonResize();
        setLeftResize();
    }

    private void setSEresize() {
        setButtonResize();
        setRightResize();
    }

    private void setRightResize() {
        int resizeX = MousePosition.getXOnScreen() - (WindowFrame.x + WindowFrame.width);
        if (checkOutSize(resizeX, POSITION_LEFT)) {
            return;
        }
        WindowFrame.width += resizeX;
    }

    private void setLeftResize() {
        int resizeX = MousePosition.getXOnScreen() - WindowFrame.x;
        if (checkOutSize(resizeX, POSITION_RIGHT)) {
            return;
        }

        WindowFrame.width -= resizeX;
        WindowFrame.x += resizeX;
    }

    private void setTopResize() {
        int resizeY = MousePosition.getYOnScreen() - (WindowFrame.y);
        if (checkOutSize(resizeY, POSITION_TOP)) {
            return;
        }
        WindowFrame.height -= resizeY;
        WindowFrame.y += resizeY;
    }

    private void setButtonResize() {
        int resizeY = MousePosition.getYOnScreen() - (WindowFrame.y + WindowFrame.height);
        if (checkOutSize(resizeY, POSITION_BUTTON)) {
            return;
        }
        WindowFrame.height += resizeY;
    }

    private static final int POSITION_LEFT = 1;
    private static final int POSITION_RIGHT = 2;
    private static final int POSITION_TOP = 3;
    private static final int POSITION_BUTTON = 4;

    private boolean checkOutSize(int resize, int position) {
        int newSize;

        switch (position) {
            case POSITION_LEFT:
                newSize = Math.max(MIN_SIZE.width, Math.min(WindowFrame.width + resize, MAX_SIZE.width));
                break;
            case POSITION_RIGHT:
                newSize = Math.max(MIN_SIZE.width, Math.min(WindowFrame.width - resize, MAX_SIZE.width));
                break;
            case POSITION_TOP:
                newSize = Math.max(MIN_SIZE.height, Math.min(WindowFrame.height - resize, MAX_SIZE.height));
                break;
            default: // POSITION_BUTTON
                newSize = Math.max(MIN_SIZE.height, Math.min(WindowFrame.height + resize, MAX_SIZE.height));
                break;
        }

        return (position == POSITION_LEFT || position == POSITION_RIGHT)
                ? (newSize == MIN_SIZE.width || newSize == MAX_SIZE.width)
                : (newSize == MIN_SIZE.height || newSize == MAX_SIZE.height);
    }

    public class JTitleBar extends JPanel {

        private final JFrame mainF;
        private JLabel title;
        private IconPanel iconPanel = new IconPanel();
        private JPanel buttonPanelContent;

        public JTitleBar(JFrame Frame) {
            mainF = Frame;
            configTitleBar();
            addContentButtons();
            addComponents();
        }
//--
        private int xMouse, yMouse;

        private void configTitleBar() {
            setLayout(new BorderLayout());
            setBackground(COLORBORDER);
            setTitle();
            addEvents();
            InitWindowTemp();
        }
        JWindow windowTemp = new JWindow();

        private void InitWindowTemp() {
            windowTemp.setBackground(new Color(150, 250, 250, 150));
        }

        private void setTitle() {
            title = new JLabel();
            title.setFont(FONT_TITLE);
            title.setForeground(COLORFOREICON);
            title.setText(TITLE.isEmpty() ? "JavaWindowPlus" : TITLE);
            mainF.setTitle(TITLE.isEmpty() ? "JavaWindowPlus" : TITLE);
            title.setBorder(BorderFactory.createEmptyBorder(1, 5, MARGIN, 1));
        }

        //===Evento de Arrastre
        private void addEvents() {

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    xMouse = e.getX();
                    yMouse = e.getY();
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    setCursor(Cursor.getDefaultCursor());
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (windowTemp.isVisible()) {
                        windowTemp.setVisible(false);
                    }
                    setFullScreenInDirection(e, mainF);
                }
            }
            );

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    int venX = e.getXOnScreen();
                    int venY = e.getYOnScreen();
                    mainF.setLocation(venX - xMouse, venY - yMouse);
                    setFullScreenInDirection(e, windowTemp);
                }
            }
            );
        }

        private void setFullScreenInDirection(MouseEvent mouse, Window window) {
            String border = getNameBorder(mouse.getXOnScreen(), mouse.getYOnScreen());

            if (border != null) {
                if (window instanceof JWindow) {
                    window.setVisible(true);
                }
                maximiceInScreen(window, border);
            } else {
                if (window instanceof JWindow) {
                    window.setVisible(false);
                }
            }
        }

        private String getNameBorder(int x, int y) {
            Rectangle screen = getBoundsFullScreen();
            int screenWidth = (int) (screen.getWidth() - 1);
            int screenHeigth = (int) (screen.getHeight() - 1);
            if (x < 1) {
                return "left";
            } else if (x >= screenWidth) {
                return "right";
            }
            if (y < 1) {
                return "top";
            } else if (y >= screenHeigth) {
                return "bottom";
            }
            return null;
        }

        private void addContentButtons() {
            buttonPanelContent = new JPanel();
            buttonPanelContent.setBackground(getBackground());
            buttonPanelContent.setLayout(new BoxLayout(buttonPanelContent, BoxLayout.LINE_AXIS));
            updateSizeContentButtons();
            addButtons();
        }

        public void updateSizeContentButtons() {
            buttonPanelContent.setPreferredSize(new Dimension(NUMBUTTONS * SIZE_BUTTONS.width, SIZE_BUTTONS.height));
        }

        public void addButtons() {
            int i = 0;

            while (TEXTBUTTONS.size() < BUTTONS.length) {
                TEXTBUTTONS.add(new JLabel("?"));
            }

            for (JPanel button : BUTTONS) {
                button = new JPanel();
                button.setLayout(new BorderLayout());
                button.setPreferredSize(SIZE_BUTTONS);

                TEXTBUTTONS.get(i).setHorizontalAlignment(SwingConstants.CENTER);
                TEXTBUTTONS.get(i).setForeground(COLORFOREICON);
                TEXTBUTTONS.get(i).setFont(new Font("Dialog", 0, 14));
                button.add(TEXTBUTTONS.get(i));

                button.addMouseListener(iluminateButton(button, (i < 1)));
                if (i <= setButtonListener.size() - 1) {
                    setButtonListener.get(Math.min(i, setButtonListener.size() - 1)).accept(button);
                }

                button.setBackground(getBackground());
                buttonPanelContent.add(button, 0);

                BUTTONS[i] = button;
                i++;
            }
        }

        public void removeButtons() {
            for (JPanel button : BUTTONS) {
                buttonPanelContent.remove(button);
            }
        }

        private Map<Integer, Consumer<JPanel>> setButtonListener = new HashMap<>() {
            {
                put(0, p -> p.addMouseListener(funtionExit(p)));
                put(1, p -> p.addMouseListener(funtionMaximized(p)));
                put(2, p -> p.addMouseListener(funtionMinimized(p)));
                put(3, p -> p.addMouseListener(funtionResized(p)));
            }
        };

        private MouseAdapter funtionExit(JPanel button) {
            return new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    System.exit(0);
                }
            };
        }

        private boolean isMaximized = false;

        private int SearchButton(String TextButton) {
            for (int i = 0; i < TEXTBUTTONS.size(); i++) {
                String Text = (TEXTBUTTONS.get(i) != null) ? TEXTBUTTONS.get(i).getText() : "error";
                if (Text.equals(TextButton)) {
                    return i;
                }
            }
            return -1;
        }

        private MouseAdapter funtionMaximized(JPanel button) {
            int maximizedNumButton = SearchButton("🗖");
            return new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (isMaximized) {
                        mainF.setBounds(frameBoundsOriginal);
                    } else {
                        frameBoundsOriginal = mainF.getBounds();
                        maximiceInScreen(mainF, "center");
                    }

                    TEXTBUTTONS.get(maximizedNumButton).setText((isMaximized) ? "🗖" : "🗗");
                    isMaximized = !isMaximized;
                }
            };
        }

        Rectangle frameBoundsOriginal;

        private Rectangle getBoundsFullScreen() {
            GraphicsConfiguration config = mainF.getGraphicsConfiguration();
            return config.getBounds();
        }

        private Insets getScreenInsets() {
            GraphicsConfiguration config = mainF.getGraphicsConfiguration();
            return Toolkit.getDefaultToolkit().getScreenInsets(config);
        }

        private Rectangle getFullScreenInsets() {
            Rectangle screenBounds = getBoundsFullScreen();
            Insets screenInsets = getScreenInsets();

            int x = screenBounds.x + screenInsets.left;
            int y = screenBounds.y + screenInsets.top;
            int width = screenBounds.width - screenInsets.left - screenInsets.right;
            int height = screenBounds.height - screenInsets.top - screenInsets.bottom;
            return new Rectangle(x, y, width, height);
        }

        private void maximiceInScreen(Window window, String border) {
            Rectangle screen = getFullScreenInsets();
            int x = (int) screen.getX();
            int y = (int) screen.getY();
            int width = (int) screen.getWidth();
            int height = (int) screen.getHeight();

            switch (border.toLowerCase()) {
                case "right" ->
                    window.setBounds(x + width / 2, y, width / 2, height);
                case "left" ->
                    window.setBounds(x, y, width / 2, height);
                case "bottom" ->
                    window.setBounds(x, y + height / 2, width, height / 2);
                case "top" ->
                    window.setBounds(x, y, width, height / 2);
                case "center", "full", "maximize", "" ->
                    window.setBounds(x, y, width, height);
                default ->
                    throw new IllegalArgumentException("Opción inválida: " + border);
            }
        }

        private MouseAdapter funtionMinimized(JPanel button) {
            return new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    mainF.setState(JFrame.ICONIFIED);
                }
            };
        }

        private MouseAdapter iluminateButton(JPanel button, boolean isButtonExit) {
            return new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBackground((isButtonExit) ? COLOR_EXIT : COLORBUTTON_ENTERED);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    button.setBackground(COLORBORDER);
                }
            };
        }

        private boolean isMessageShow = false;
        private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        private void ChangeResizeMessage(String message) {
            if (isMessageShow) {
                return;
            }

            isMessageShow = true;

            title.setText(String.format(message + " - %s", title.getText()));
            scheduler.schedule(() -> {
                SwingUtilities.invokeLater(() -> {
                    title.setText(title.getText().replace(message + " - ", ""));
                    isMessageShow = false;
                });
            }, 700, TimeUnit.MILLISECONDS);
        }

        private MouseAdapter funtionResized(JPanel button) {
            int resizeNumButton = SearchButton("🕂");
            return new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    RESIZABLE = !RESIZABLE;
                    TEXTBUTTONS.get(resizeNumButton).setText((RESIZABLE) ? "🕂" : "🕀");
                    ChangeResizeMessage((RESIZABLE) ? "ResizeMode" : "ResizeBlocked");
                }
            };
        }

        private void addComponents() {
            add(title, BorderLayout.CENTER);
            add(iconPanel, BorderLayout.LINE_START);
            add(buttonPanelContent, BorderLayout.LINE_END);
        }

        public void revalidateTitlebar() {
            this.setPreferredSize(new Dimension(0, TITLEBAR_HEIGHT));
            if (TITLEBAR_HEIGHT < 1) {
                return;
            }

            buttonPanelContent.setPreferredSize(new Dimension((NUMBUTTONS + 1) * SIZE_BUTTONS.width, SIZE_BUTTONS.height));
            iconPanel.setPreferredSize(SIZE_BUTTONS);
            SIZEFONT_ICON = Math.max(2, TITLEBAR_HEIGHT / 2 - 2);
            POINTPAIN_ICON = new Point(TITLEBAR_HEIGHT / 2 - (SIZEFONT_ICON / 2), TITLEBAR_HEIGHT / 2 + (SIZEFONT_ICON / 3));
            iconPanel.repaint();
        }

        public void repaintButtons() {
            for (int i = 0; i <= NUMBUTTONS; i++) {
                BUTTONS[i].setBackground(COLORBORDER);
            }
        }

        public void repaintForeIcon() {
            title.setForeground(COLORFOREICON);
            iconPanel.repaint();
            for (int i = 0; i <= NUMBUTTONS; i++) {
                TEXTBUTTONS.get(Math.min(i, TEXTBUTTONS.size() - 1)).setForeground(COLORFOREICON);
            }
        }

        public void revalidateTitle() {
            title.setFont(FONT_TITLE);
            title.setText((TITLE != null && !TITLE.isEmpty()) ? TITLE : title.getText());
        }

    }
//=====

    public class IconPanel extends JPanel {

        private String NameImagen = null;
        private String CarpetaDeProyecto = null;

        public IconPanel() {
            setPreferredSize(new Dimension(TITLEBAR_HEIGHT, TITLEBAR_HEIGHT));
            setBackground(new Color(0, 0, 0, 0));
        }

        public void setIcon(String carpetadeProyecto, String nameImg) {
            this.NameImagen = nameImg;
            CarpetaDeProyecto = carpetadeProyecto;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            if (NameImagen != null && CarpetaDeProyecto != null) {
                try {
                    Image img = new ImageIcon(getClass().getResource("/" + CarpetaDeProyecto + "/" + NameImagen)).getImage();
                    g2d.drawImage(img, 0, 0, getWidth(), getHeight(), null);
                    setIconImage(img);
                } catch (Exception e) {
                    System.err.println("Error cargando imagen: " + e.getMessage());
                    drawDefaultIcon(g2d);
                }
            } else {
                drawDefaultIcon(g2d);
            }
        }

        private void drawDefaultIcon(Graphics2D g2d) {
            g2d.setColor(COLORFOREICON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.setFont(new Font("Dialog", Font.BOLD, SIZEFONT_ICON)); // Fuente negrita
            FontMetrics metrics = g2d.getFontMetrics(FONT_TITLE);
            int textWidth = metrics.stringWidth(TEXT_ICON);
            int textHeight = metrics.getHeight();
            int x = (getWidth() - textWidth) / 2;
            int y = (getHeight() - textHeight) / 2 + metrics.getAscent();
            POINTPAIN_ICON = new Point(x, y);
            g2d.drawString(TEXT_ICON, (int) POINTPAIN_ICON.getX(), (int) POINTPAIN_ICON.getY()); // Ajuste de posición
        }
    }

    public static int calculateLightness(Color colorRGB) {
        float R = colorRGB.getRed() / 255f;
        float G = colorRGB.getGreen() / 255f;
        float B = colorRGB.getBlue() / 255f;
        float max = Math.max(R, Math.max(G, B));
        float min = Math.min(R, Math.min(G, B));

        return Math.round(((max + min) / 2) * 100);
    }

    private Color illuminateColor(Color colorDark) {
        int B = colorDark.getBlue() + 20;
        int R = colorDark.getRed() + 20;
        int G = colorDark.getGreen() + 20;
        return new Color(R, G, B);
    }
    //++++++++++++++++++++++END++++++++++++++++++++++++++++++ //
}
