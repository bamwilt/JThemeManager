package UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JButton;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

public class SimpleSplitPaneUI extends BasicSplitPaneUI {

    private final Color dividerColor;
    private final Color dividerTextColor;

    public SimpleSplitPaneUI(Color dividerColor, Color dividerTextColor) {
        this.dividerColor = dividerColor;
        this.dividerTextColor = dividerTextColor;
    }

    @Override
    public BasicSplitPaneDivider createDefaultDivider() {
        return new CustomSplitDivider(this, dividerColor, dividerTextColor);
    }

    private static class CustomSplitDivider extends BasicSplitPaneDivider {

        private final Color backgroundColor;
        private final Color textColor;

        public CustomSplitDivider(BasicSplitPaneUI ui, Color backgroundColor, Color textColor) {
            super(ui);
            this.backgroundColor = backgroundColor;
            this.textColor = textColor;
            setBorder(null);

            configureButton(leftButton);
            configureButton(rightButton);
        }

        private void configureButton(JButton button) {
            button.setContentAreaFilled(false);  
            button.setBorderPainted(false);      
            button.setFocusPainted(false);      
            button.setOpaque(false);           
        }

        @Override
        public void doLayout() {
            super.doLayout();
            this.setBorder(null);

            int buttonSize = 25;

            int middleY = getHeight() / 2;
            int buttonSpacing = 10;

            leftButton.setBounds(
                    getWidth() - buttonSize,
                    middleY - buttonSize - buttonSpacing / 2,
                    buttonSize,
                    buttonSize
            );

            rightButton.setBounds(
                    getWidth() - buttonSize,
                    middleY + buttonSpacing / 2,
                    buttonSize,
                    buttonSize
            );
        }

        @Override
        public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setColor(backgroundColor);
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            Font font = new Font("Dialog", Font.BOLD, 18);
            g2.setFont(font);
            g2.setColor(textColor);

            FontMetrics fm = g2.getFontMetrics();

            int x = getWidth() - fm.stringWidth("»") ;
            int height = getHeight();

            int totalHeight = fm.getHeight() * 2 + 10; 
            int startY = (height - totalHeight) / 2 + fm.getAscent();

            g2.drawString("«", x, startY);
            g2.drawString("»", x, startY + fm.getHeight() + 10);

            g2.dispose();
        }
    }
}
