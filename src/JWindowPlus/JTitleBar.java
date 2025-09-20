package JWindowPlus;

import JWindowPlus.JIconImage.TextPosition;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class JTitleBar extends JPanel {

    private JFrame frame;
    private JIconImage iconImage;
    private JLabel titleLabel;
    private JPanel buttonsPanel;
    private List<ControlButton> controlButtons = new ArrayList<>();

    private Color backgroundColor = new Color(25, 25, 30);
    private Color foregroundColor = Color.WHITE;
    private Color buttonHoverColor = new Color(50, 50, 55);
    private Color exitButtonColor = new Color(255, 50, 50);
    Point mouseOffset = null;
    private int height = 40;
    private int buttonSize = height;
    private ControlButton maximizeButton;
    private Consumer<JFrame> minimizeHandler = f -> f.setState(JFrame.ICONIFIED);

    private Consumer<JFrame> maximizeHandler = f -> {
        boolean isMaximized = f.getExtendedState() == JFrame.MAXIMIZED_BOTH;

        if (isMaximized) {
            f.setExtendedState(JFrame.NORMAL);
        } else {
            f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }

        if (maximizeButton != null) {
            maximizeButton.setText(isMaximized ? "🗖" : "🗗");
            maximizeButton.repaint();
        }
    };

    private Consumer<JFrame> closeHandler = f -> System.exit(0);

    public JTitleBar(JFrame frame) {
        this(frame, "JWindowPlus");
    }

    public JTitleBar(JFrame frame, String title) {
        this.frame = frame;
        this.height = 40;
        this.buttonSize = height;
        init(title);
    }

    private void init(String title) {
        setupLayout();
        setupIcon();
        setupTitleLabel(title);
        setupButtonsPanel();
        addControlButtons();
        addMouseListeners();
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        setBackground(backgroundColor);
        setPreferredSize(new Dimension(frame.getWidth(), height));
    }

    private void setupIcon() {
        iconImage = new JIconImage();
        iconImage.setForeground(foregroundColor);
        iconImage.setPreferredSize(new Dimension(buttonSize, buttonSize));
        iconImage.setText("♜", 18); // ícono por defecto
        add(iconImage, BorderLayout.WEST);
    }

    private void setupTitleLabel(String title) {
        titleLabel = new JLabel(title);
        titleLabel.setForeground(foregroundColor);
        titleLabel.setFont(new Font("Noto Sans Symbols", Font.BOLD, 12));

        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        add(titleLabel, BorderLayout.CENTER);
    }

    private void setupButtonsPanel() {
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS));
        buttonsPanel.setBackground(backgroundColor);
        add(buttonsPanel, BorderLayout.EAST);
    }

    private void addControlButtons() {
        addControlButton("─", "Minimizar", minimizeHandler);
        maximizeButton = addControlButton("🗖", "Maximizar", maximizeHandler);
        addControlButton("✕", "Cerrar", closeHandler);
    }

    private void addMouseListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseOffset = e.getPoint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mouseOffset = null;
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (mouseOffset != null) {
                    Point newLocation = e.getLocationOnScreen();
                    newLocation.translate(-mouseOffset.x, -mouseOffset.y);
                    frame.setLocation(newLocation);
                }
            }
        });
    }

    public ControlButton addControlButton(String icon, String tooltip, Consumer<JFrame> action) {
        ControlButton button = new ControlButton(icon, tooltip);
        button.setPreferredSize(new Dimension(buttonSize, buttonSize));
        button.setMaximumSize(new Dimension(buttonSize, Integer.MAX_VALUE));
        button.addActionListener(e -> action.accept(frame));
        controlButtons.add(button);
        buttonsPanel.add(button);
        revalidateButtons();
        return button;
    }

    public void removeControlButton(int index) {
        if (index >= 0 && index < controlButtons.size()) {
            ControlButton button = controlButtons.remove(index);
            buttonsPanel.remove(button);
            revalidateButtons();
        }
    }

    public void clearControlButtons() {
        controlButtons.clear();
        buttonsPanel.removeAll();
        revalidateButtons();
    }

    private void revalidateButtons() {
        buttonsPanel.setPreferredSize(new Dimension(buttonSize * controlButtons.size(), buttonSize));
        buttonsPanel.revalidate();
        buttonsPanel.repaint();
    }

    public class ControlButton extends JButton {

        public ControlButton(String icon, String tooltip) {
            super(icon);
            setToolTipText(tooltip);
            setFont(new Font("Dialog", Font.PLAIN, 14));
            setForeground(foregroundColor);
            setBackground(backgroundColor);
            setContentAreaFilled(false);
            setFocusable(false);
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            setOpaque(true);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (ControlButton.this == controlButtons.get(controlButtons.size() - 1)) {
                        setBackground(exitButtonColor);
                    } else {
                        setBackground(buttonHoverColor);
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(backgroundColor);
                }
            });
        }

        public void setFontSize(int size) {
            Font current = getFont();
            Font f = new Font(current.getFamily(), current.getStyle(), size);
            setFont(f);
        }

        public void setFontStyle(int style) {
            Font current = getFont();
            Font f = new Font(current.getFamily(), style, current.getSize());
            setFont(f);
        }

        public void setFontFamily(String family) {
            Font current = getFont();
            Font f = new Font(family, current.getStyle(), current.getSize());
            setFont(f);
        }

        public void setBackgroundHover(Color hoverColor) {
            buttonHoverColor = hoverColor;
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (getModel().isArmed()) {
                g.setColor(buttonHoverColor.darker());
            } else if (getModel().isRollover()) {
                g.setColor(getBackground());
            } else {
                g.setColor(backgroundColor);
            }
            g.fillRect(0, 0, getWidth(), getHeight());
            super.paintComponent(g);
        }
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    public void setIcon(String iconText) {
        iconImage.setText(iconText);
    }

    public void setIconSize(int Size) {
        iconImage.setFontSize(Size);
   
    }

    
    public void setIconImage(String imagePath) {
        iconImage.setImageIcon(imagePath);
    }
    Color hoverColor;

    public void setBackgroundColor(Color color) {
        backgroundColor = color;
        setBackground(color);
        buttonsPanel.setBackground(color);
        hoverColor = (calculateLightness(color) >= 50) ? color.darker()
                : ((calculateLightness(color) >= 20) ? color.brighter()
                : illuminateColor(color));
        for (ControlButton button : controlButtons) {
            button.setBackground(color);
            button.setBackgroundHover(hoverColor);
        }
        repaint();
    }

    public void setForegroundColor(Color color) {
        foregroundColor = color;
        titleLabel.setForeground(color);
        iconImage.setForeground(color);
        for (ControlButton button : controlButtons) {
            button.setForeground(color);
        }
        repaint();
    }

    public void setHeight(int height) {
        this.height = height;
        this.buttonSize = height;
        setPreferredSize(new Dimension(frame.getWidth(), height));
        for (ControlButton button : controlButtons) {
            button.setPreferredSize(new Dimension(buttonSize, buttonSize));
            button.setMaximumSize(new Dimension(buttonSize, Integer.MAX_VALUE));
        }
        buttonsPanel.setPreferredSize(new Dimension(buttonSize * controlButtons.size(), buttonSize));
        iconImage.setPreferredSize(new Dimension(buttonSize, buttonSize));
        revalidate();
    }

    public void setFontSizeButtons(int size) {
        for (ControlButton button : controlButtons) {
            button.setFontSize(size);
        }
    }

    public void setFontSize(int size) {
        Font current = getFont();
        Font f = new Font(current.getFamily(), current.getStyle(), size);
        setFont(f);
    }

    public void setFontStyle(int style) {
        Font current = getFont();
        Font f = new Font(current.getFamily(), style, current.getSize());
        setFont(f);
    }

    public void setFontFamily(String family) {
        Font current = getFont();
        Font f = new Font(family, current.getStyle(), current.getSize());
        setFont(f);
    }

    public void setMinimizeHandler(Consumer<JFrame> handler) {
        minimizeHandler = handler;
    }

    public void setMaximizeHandler(Consumer<JFrame> handler) {
        maximizeHandler = handler;
    }

    public void setCloseHandler(Consumer<JFrame> handler) {
        closeHandler = handler;
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
}

/*
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡠⢀⢀⢠⣤⣄⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⠤⢒⣭⠉⠥⢌⡋⠐⠀⠠⠦⠍⢉⡃⢴⠂⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⢤⠈⠔⠀⣉⣥⣶⣶⣬⣭⣼⣿⣿⣶⣤⣄⠰⠶⠐⠀⢁⡀⠀⠀⠀⠀⠆⡀⢀⣀⡀⠀⣶
⣀⣀⣀⣚⣒⣒⣋⣛⣚⣡⡤⠁⠴⠠⠗⣸⣿⣿⣿⡟⣻⣿⣿⣿⣿⣿⡿⣿⣿⣿⣶⣿⣷⣄⠁⢀⢼⡴⢰⣾⣷⣿⣿⣿⣶⣶
⢻⣿⣿⣿⣿⣿⣿⣿⣿⠟⣠⠃⣰⣿⣿⡟⣻⣿⣿⢡⣿⣿⣿⣿⣿⣿⡇⢻⣿⣿⣿⣿⣏⢻⣿⠰⠖⠀⠉⠙⠻⣿⣿⣿⣿⣿
⣿⣿⣿⣿⣿⣿⣿⡿⠏⣰⡟⣰⣿⣿⡟⣰⣿⣿⡏⣼⣿⣿⣿⣿⣿⣿⡇⡌⠿⢿⣇⢻⡿⠂⢿⣷⠖⠀⠰⠀⠀⠛⢻⣿⣿⣿
⣿⣿⣿⣿⣿⣿⣿⡟⣰⣿⠁⡁⡌⢻⢃⡿⡀⣿⡇⣿⣿⣿⣿⣿⣿⣿⡇⣿⣄⢠⡤⠀⠀⠺⠘⣿⠀⠀⠀⠀⠀⡄⠀⣿⣿⣿
⣿⣿⣿⣿⣿⣿⣿⠀⢰⡏⣤⣃⣴⣾⢸⣷⣶⣿⢰⣿⣿⣿⣿⡇⣿⣿⢇⣿⣿⣆⠀⣴⢸⣶⡆⣿⠀⠈⠀⠀⠈⠁⢁⣿⣿⣿
⣿⣿⣿⣿⣿⣿⡇⡄⣿⠃⣿⣿⣿⣿⢸⡇⣿⣿⢸⣿⣿⣿⣿⡇⣿⣿⢠⣴⣶⣮⣤⠹⡌⣿⡇⣿⡏⠀⡆⠀⠐⠄⠘⣿⣿⣿
⣿⣿⣿⣿⣿⣿⢁⠁⣿⠀⣿⣿⡏⣿⢸⡇⣿⣿⢸⢿⣿⣿⣿⠇⣿⡇⣜⣩⠽⠻⠿⣷⡀⣿⡇⣿⡧⠀⠀⠀⠀⢀⣼⣿⣿⣿
⣿⣿⣿⣿⣿⣿⢸⠀⣿⡇⣿⣿⡇⣿⢸⣷⢸⣿⡘⡘⣿⣿⣿⢰⣿⢱⢃⡀⠀⠀⠀⠈⠃⢹⠇⣿⠇⠆⡆⢰⠀⢿⣿⣿⣿⣿
⣿⣿⣿⣿⣿⣿⢸⠀⢻⡇⢸⡟⣿⢸⡌⣿⡈⣿⡇⠇⣿⣿⣿⢸⠇⣾⣿⡇⢀⠀⠀⡷⢰⠸⢰⡿⠈⠀⡇⠘⠀⢸⣿⣿⣿⣿
⣿⣿⣿⣿⣿⣿⡄⠰⠘⣇⠈⢧⠹⡄⠳⣘⣃⣹⣃⣀⣹⣿⣟⣘⣸⣿⣿⣿⣦⣭⣼⣷⡏⠂⠁⠠⠘⢰⠃⢀⡄⠸⣿⣿⣿⣿
⣿⣿⣿⣿⣿⣿⣷⡀⠀⠹⡐⡈⢆⠱⠸⣿⣿⣿⣿⣿⣿⣿⣿⣻⣿⣿⣿⣿⣿⣿⣿⣿⣷⢄⣀⠔⡀⡜⠀⢈⡇⡀⣿⣿⣿⣿
⣿⣿⣿⣿⣿⣿⣿⣿⣦⣤⡀⠈⢦⣀⠀⣽⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠇⣬⠁⠌⠀⣰⠀⣼⡇⠀⣿⣿⣿⣿
⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣦⡈⠍⢣⡈⢿⣿⣿⣿⣿⣝⣛⣿⣿⣻⣿⣿⣿⣿⡿⢋⡼⠃⠘⣠⣾⣿⠀⢿⣷⣠⣿⣿⣿⣿
⣿⣿⣿⣿⣿⣿⣿⠿⠿⣿⣿⣿⣿⣦⣀⡑⠀⠉⠻⢿⣿⣿⣿⣿⣿⣿⣿⡿⠟⠉⠀⠛⣡⣴⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
⣿⣿⣿⣿⣿⣿⠏⣴⣷⣦⡙⢿⣿⣿⣿⣿⣿⡆⢀⡀⣌⡉⠛⠿⠛⣋⣥⡆⡨⠐⢶⣿⣿⣿⣿⣿⣿⠟⣋⣥⣦⠸⣿⣿⣿⣿
⣿⣿⣿⣿⣿⣿⢸⣿⣿⣿⣿⣄⢛⠛⢿⣿⣿⡆⢆⢡⣬⡙⠗⣒⠻⢋⣥⡅⠔⠁⣾⣿⣿⡿⠿⠟⣡⣾⣿⣿⣿⢀⣿⣿⣿⣿
⣿⣿⣿⣿⣿⣿⡌⣿⣿⣿⣿⡇⣾⣿⡆⢨⣄⠲⢮⡀⢿⣿⣆⣿⣠⣿⠟⠁⣢⣶⣶⡌⡅⣶⣿⡆⣿⣿⣿⠿⡏⣼⣿⣿⣿⣿
⣿⣿⣿⣿⠏⣿⢈⠍⣷⣬⡁⠙⢿⡇⡜⣿⣿⡦⠉⠀⡁⠀⠀⠀⢀⠈⠛⢿⣿⣿⢃⢣⣿⠟⠐⣋⣿⠀⢄⣤⣙⣿⣿⣿⣿⣿
⣿⣿⣿⣿⢸⣿⣿⣷⢼⣿⣿⡟⠀⣿⣷⡐⡘⢿⡀⠰⠟⠀⠀⣆⠀⠈⠻⠆⢀⡿⢃⢂⣾⣿⠀⢺⣿⣿⣷⠸⣿⠩⣿⣿⣿⣿
⣿⣿⣿⣿⢸⣿⣿⣿⠘⣿⣿⡇⠀⢨⠍⣿⣬⡊⠑⠄⢠⠆⢠⣿⠀⢣⡀⠀⡊⢔⣡⣟⢛⡁⠀⢸⣿⣿⠃⣾⣿⣷⣿⣿⣿⣿
⣿⣿⣿⣿⢸⡬⠵⣭⣷⠌⡃⠁⠀⣿⣷⣌⣛⡛⢶⣬⣤⠀⣀⣀⡀⢐⣢⣥⡖⠿⠿⢋⣼⣧⠀⢈⣬⠠⣄⣛⣛⠰⣿⣿⣿⣿
⣿⣿⣿⣿⢸⣭⣭⣭⣤⡈⢰⣿⠀⣸⣿⣿⣿⣿⣿⣦⣭⡅⠀⣈⣋⡀⠨⣙⣩⣴⣾⣿⣿⣿⣿⡄⠸⣿⠆⠛⣛⣛⣛⣿⣿⣿

*/