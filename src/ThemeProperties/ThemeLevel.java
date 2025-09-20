package ThemeProperties;

import UI.UIClass;
import Utils.ThemeUtils;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.JTextComponent;

public class ThemeLevel {

    private final Theme theme;

    public ThemeLevel(Theme theme) {
        this.theme = theme;
    }

    public void applyTheme(Container container, int level) {
        if (container == null || theme == null) {
            return;
        }

        boolean isDarkTheme = ThemeUtils.isColorDark(theme.getColor(theme.PRIMARY));
        Color primary = ThemeUtils.adjustColor(theme.getColor(theme.PRIMARY), level, isDarkTheme);
        Color secondary = ThemeUtils.adjustColor(theme.getColor(theme.SECONDARY), level, isDarkTheme);
        Color tertiary = ThemeUtils.adjustColor(theme.getColor(theme.TERTIARY), level, isDarkTheme);
        Color text = theme.getColor(theme.TEXT);
        Color neutral = ThemeUtils.adjustColor(theme.getColor(theme.NEUTRAL), level, isDarkTheme);
        Color neutral2 = ThemeUtils.adjustColor(theme.getColor(theme.NEUTRAL_2), level, isDarkTheme);
        Color success = ThemeUtils.adjustColor(theme.getColor(theme.SUCCESS), level, isDarkTheme);

        for (Component comp : container.getComponents()) {
            if (comp == null || (comp instanceof JComponent jc
                    && Boolean.TRUE.equals(jc.getClientProperty("excluded")))) {
                continue;
            }
            applyComponentTheme(comp, text, neutral, neutral2, primary, secondary, tertiary, success, isDarkTheme);
            if (comp instanceof Container innerContainer) {
                applyTheme(innerContainer, (level + 1) % 3);
            }
        }
    }

    private void applyComponentTheme(Component comp,
            Color text, Color neutral, Color neutral2,
            Color primary, Color secondary, Color tertiary,
            Color success, boolean isDarkTheme) {

        Color neutralTransparent = new Color(neutral.getRed(), neutral.getGreen(), neutral.getBlue(), 20);

        if (comp instanceof JPanel panel) {
            panel.setBackground(primary);
            if (panel.getBorder() == null) {
                panel.setBorder(new LineBorder(neutralTransparent, 1));
            }
            return;
        }
        Color backvar = ThemeUtils.adjustColor(primary, 2, isDarkTheme);
        switch (comp) {
            case JLabel lbl ->
                lbl.setForeground(text);
            case JRadioButton radio -> {
                UIClass.setUIRadioButton(radio, primary, text, 1, 5, false);
                radio.setBackground(primary);
                radio.setForeground(text);
            }
            case JCheckBox check -> {
                UIClass.setUICheckBox(check, text, text, 1, 5, false);
            }
            case AbstractButton btn -> {
                UIClass.setUIButton(btn, primary, text, 0, 5);
                btn.setBackground(primary);
                btn.setForeground(btn.isEnabled() ? text : neutral2);
                if (btn.getBorder() == null) {
                    btn.setBorder(new LineBorder(neutralTransparent, 2));
                }
            }
            case JComboBox<?> combo -> {
                if (combo.getRenderer() == null) {
                    combo.setRenderer(new DefaultListCellRenderer());
                }
                UIClass.setUICombo(combo, primary, text);
                combo.setBackground(primary);
                combo.setForeground(text);
            }
            case JScrollBar scroll -> {
                UIClass.setUIScrollBar(scroll, backvar, success, 8, 4);
                scroll.setBackground(backvar);
                scroll.setForeground(success);
                scroll.setUnitIncrement(10);
            }
            case JScrollPane scroll -> {
                scroll.setBackground(backvar);
                scroll.setForeground(success);
            }
            case JSlider slider -> {
                UIClass.setUISlider(slider, backvar, success, 12, 10);
                slider.setBackground(backvar);
                slider.setForeground(success);
            }
            case JFormattedTextField ftf -> {
                ftf.setBackground(primary);
                ftf.setForeground(text);
                ftf.setCaretColor(success);
            }
            case JTextComponent txt -> {
                txt.setBackground(tertiary);
                txt.setForeground(text);
                txt.setCaretColor(secondary);
            }
            case JSplitPane split -> {
                UIClass.setSplitPaneUI(split, backvar, success, 10, true);
            }
            case JProgressBar progress -> {
                progress.setBackground(secondary);
                progress.setForeground(success);
            }
            default -> {
                comp.setBackground(primary);
                comp.setForeground(text);
            }
        }
    }
}
