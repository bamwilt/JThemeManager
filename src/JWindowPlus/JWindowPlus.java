package JWindowPlus;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;

import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class JWindowPlus extends JFrame {

    static {
        System.setProperty("sun.java2d.opengl", "true");
        System.setProperty("sun.java2d.xrender", "true");
        System.setProperty("sun.java2d.pmoffscreen", "false");
    }

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
        TitleBar.setForegroundColor(color);
    }

    public Color getBackgroundPlus() {
        return COLORBORDER;
    }

    public Color getForegroundPlus() {
        return COLORFOREICON;
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
    }

    public void setTitlePlus(String title) {
        TITLE = title;
        setTitle(title);
    }

    public void setSizeTitleBar(int sizeTitle) {
        TITLEBAR_HEIGHT = sizeTitle;
        SIZE_BUTTONS = new Dimension(TITLEBAR_HEIGHT, TITLEBAR_HEIGHT);
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

    public void setVisiblePlus() {
        this.setLocationRelativeTo(null);
        JFramePanel.revalidate();
        JFramePanel.repaint();
        this.setVisible(true);
    }

    public void setIconSimbol(String simbol) {
        TEXT_ICON = simbol;
        TitleBar.setIcon(TEXT_ICON);
    }

       public void setIconSimbolSize(int Size) {
        TitleBar.setIconSize(Size);
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

    private javax.swing.Timer resizeTimer;
    private volatile Point lastResizePoint;
    private volatile Cursor lastResizeCursor;

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

    public JWindowPlus() {
        TITLE = "JWindowPlus";
        CONTENTPANEL = new JPanel();
        CONTENTPANEL.setPreferredSize(new Dimension(500, 300));
        CONTENTPANEL.setLayout(new BorderLayout());
        initComponents();
    }

    public JWindowPlus(String title_) {
        TITLE = title_;
        CONTENTPANEL = new JPanel();
        CONTENTPANEL.setPreferredSize(new Dimension(500, 300));
        CONTENTPANEL.setLayout(new BorderLayout());
        initComponents();
    }

    public JWindowPlus(JPanel ContentPanel) {
        CONTENTPANEL = ContentPanel;
        initComponents();
    }

    public JWindowPlus(JPanel ContentPanel, String title_) {
        TITLE = title_;
        CONTENTPANEL = ContentPanel;
        initComponents();
    }

    Border lineBorder = new LineBorder(new Color(187, 187, 187, 50), 1);
    Border marginBorder = BorderFactory.createEmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN);

    private void initComponents() {
        int width = Math.max(CONTENTPANEL.getPreferredSize().width + (MARGIN * 2), MIN_SIZE.width);
        int height = Math.max(CONTENTPANEL.getPreferredSize().height + (MARGIN * 2) + TITLEBAR_HEIGHT, MIN_SIZE.height);
        JFRAME_SIZE = new Dimension(width, height);

        setSize(JFRAME_SIZE);
        setMinimumSize(MIN_SIZE);
        setMaximumSize(MAX_SIZE);

        JFramePanel.setBorder(BorderFactory.createCompoundBorder(lineBorder, marginBorder));
        JFramePanel.setLayout(new BorderLayout());

        JFramePanel.setBackground(COLORBORDER);
        JFramePanel.add(CONTENTPANEL, BorderLayout.CENTER);

        TitleBar = new JTitleBar(this, TITLE);

        JFramePanel.add(TitleBar, BorderLayout.NORTH);
        JFramePanel.setDoubleBuffered(true);
        TitleBar.setDoubleBuffered(true);
        CONTENTPANEL.setBorder(lineBorder);

        CONTENTPANEL.setDoubleBuffered(true);
        getRootPane().setDoubleBuffered(true);
        setContentPane(JFramePanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setBackground(getContentPane().getBackground());
        windowResizer resizer = new windowResizer(this, JFramePanel, TitleBar, MARGIN, MIN_SIZE, MAX_SIZE);
        this.addMouseListener(resizer);        // para eventos de click/press/release
        this.addMouseMotionListener(resizer);
    }

    private void repaintFrame() {
        SwingUtilities.invokeLater(() -> {
            JFramePanel.setBackground(COLORBORDER);
            TitleBar.setBackgroundColor(COLORBORDER);
        });
    }


    //++++++++++++++++++++++END++++++++++++++++++++++++++++++ //
}
