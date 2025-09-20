package ThemeProperties;

import UI.UIClass;
import Utils.ThemeUtils;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Insets;
import java.util.Map;
import java.util.WeakHashMap;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.JTextComponent;

public class ThemeTone {

    private final Theme theme;
    private static final Map<Component, int[]> GLOBAL_COMPONENT_BASE = new WeakHashMap<>();

    public ThemeTone(Theme theme) {
        this.theme = theme;
    }

    public void applyThemeByTone(Container container) {
        if (container == null || theme == null) {
            return;
        }

        for (Component comp : container.getComponents()) {
            if (comp == null || (comp instanceof JComponent jc
                    && Boolean.TRUE.equals(jc.getClientProperty("excluded")))) {
                continue;
            }

            registerBaseColor(comp);
            applyComponentThemeByTone(comp);

            if (comp instanceof Container childContainer) {
                applyThemeByTone(childContainer);
            }
        }

        registerBaseColor(container);
        applyComponentThemeByTone(container);
    }

    private void applyComponentThemeByTone(Component comp) {
        int[] lightness = GLOBAL_COMPONENT_BASE.get(comp);
        if (lightness == null) {
            registerBaseColor(comp);
            lightness = GLOBAL_COMPONENT_BASE.get(comp);
        }

        int bgLight = lightness[0];
        int fgLight = lightness[1];
        Color neutral = theme.getColor(theme.NEUTRAL);
        Color neutralTransparent = new Color(neutral.getRed(), neutral.getGreen(), neutral.getBlue(), 20);

        Color background = ThemeUtils.toneToColor(
                bgLight,
                theme.getColor(theme.TERTIARY),
                theme.getColor(theme.SECONDARY),
                theme.getColor(theme.PRIMARY)
        );

        Color foreground = ThemeUtils.toneToColor(
                fgLight,
                theme.getColor(theme.TEXT),
                neutral,
                theme.getColor(theme.NEUTRAL_2)
        );

        boolean isDark = ThemeUtils.isColorDark(background);
        Color backvar = ThemeUtils.adjustColor(background, 2, isDark);
        Color backvar_2 = theme.getColor(Theme.SUCCESS);
        switch (comp) {
            case JRadioButton radio -> {
                UIClass.setUIRadioButton(radio, backvar, foreground, 1, 5, false);
                radio.setBackground(background);
                radio.setForeground(foreground);
            }
            case JCheckBox check -> {
                UIClass.setUICheckBox(check, backvar, foreground, 1, 5, false);
            }
            case AbstractButton btn -> {
                UIClass.setUIButton(btn, background, foreground, 0, 5);
                btn.setBackground(background);
                btn.setForeground(foreground);
            }
            case JComboBox<?> combo -> {
                if (combo.getRenderer() == null) {
                    combo.setRenderer(new DefaultListCellRenderer());
                }
                UIClass.setUICombo(combo, background, foreground);
                combo.setBackground(background);
                combo.setForeground(foreground);
            }
            case JScrollBar scroll -> {
                UIClass.setUIScrollBar(scroll, backvar, backvar_2, 8, 4);
                scroll.setBackground(backvar);
                scroll.setForeground(backvar_2);
                scroll.setUnitIncrement(10);
            }
            case JScrollPane scrollPane -> {
                scrollPane.setBackground(backvar);
                scrollPane.setForeground(foreground);
            }
            case JSlider slider -> {
                UIClass.setUISlider(slider, backvar, backvar_2, 12, 10);
                slider.setBackground(backvar);
                slider.setForeground(backvar_2);
            }
            case JFormattedTextField ftf -> {
                ftf.setBackground(background);
                ftf.setForeground(foreground);
                ftf.setCaretColor(foreground);
            }
            case JLabel lbl -> {
                lbl.setForeground(foreground);
                if (lbl.isOpaque()) {
                    lbl.setBackground(background);
                }
            }
            case JSplitPane split -> {
                UIClass.setSplitPaneUI(split, backvar, foreground, 10, true);
                split.setBackground(background);
                split.setDividerSize(10);
                split.setContinuousLayout(true);
            }
            case JTextComponent txt -> {
                txt.setMargin(new Insets(2, 4, 2, 2));
                txt.setBackground(background);
                txt.setForeground(foreground);
                txt.setCaretColor(foreground);
            }
            case JProgressBar progress -> {
                progress.setBackground(ThemeUtils.adjustColor(background, 1, isDark));
                progress.setForeground(backvar_2);
            }
            case JPanel panel -> {
                panel.setBackground(background);
                if (panel.getBorder() == null) {
                    panel.setBorder(new LineBorder(neutralTransparent, 2));
                }
            }
            default -> {
                comp.setBackground(background);
                comp.setForeground(foreground);
            }
        }
    }

    private void registerBaseColor(Component comp) {
        if (comp == null || GLOBAL_COMPONENT_BASE.containsKey(comp)) {
            return;
        }

        Color bg = getSafeBackground(comp);
        Color fg = getSafeForeground(comp);

        GLOBAL_COMPONENT_BASE.put(comp, new int[]{
            ThemeUtils.calculateLightness(bg),
            ThemeUtils.calculateLightness(fg)
        });
    }

    private Color getSafeBackground(Component comp) {
        if (comp.getBackground() != null) {
            return comp.getBackground();
        }
        if (comp.getParent() != null) {
            return comp.getParent().getBackground();
        }
        return Color.RED;
    }

    private Color getSafeForeground(Component comp) {
        if (comp.getForeground() != null) {
            return comp.getForeground();
        }
        if (comp.getParent() != null) {
            return comp.getParent().getForeground();
        }
        return Color.BLACK;
    }
}
