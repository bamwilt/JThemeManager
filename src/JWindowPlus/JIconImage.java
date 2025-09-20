package JWindowPlus;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.beans.BeanProperty;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class JIconImage extends JLabel {

    //+++++++++++++++++++++ SETTERS+++++++++++++++++++++++++++++++ //
    @BeanProperty(preferred = true, visualUpdate = true)
    public void setImage(File file) {
        if (isValidImageFile(file)) {
            try {
                image = ImageIO.read(file);
                imageLoaded = true;
                revalidate();
                repaint();
            } catch (IOException e) {
                System.err.println("Error al cargar la imagen: " + e.getMessage());
                imageLoaded = false;
            }
        } else {
            System.err.println("Archivo no válido o no es una imagen.");
            imageLoaded = false;
        }
    }

    //+++++++++++++++++++++START CODE MAIN+++++++++++++++++++++++++++++++ //
    public JIconImage() {
        setBounds(0, 0, 100, 100);
        setPreferredSize(new Dimension(100, 100));
    }

    public JIconImage(File file) {
        setBounds(0, 0, 100, 100);
        setPreferredSize(new Dimension(100, 100));
        setImage(file);
    }

    //+++++++++++++++++++++ IMAGEN +++++++++++++++++++++++++++++++ //
    private transient Image image;
    private boolean imageLoaded = false;
    private float opacity = 1.0f;
    private float rotation = 0.0f;
    private float scaleX = 1.0f;
    private float scaleY = 1.0f;
    private float hueShift = 0.0f;
    private float saturation = 1.0f;
    private float brightness = 1.0f;

    public static enum TextPosition {
        CENTER,
        TOP,
        BOTTOM,
        LEFT,
        RIGHT
    }

    // ================== MANIPULACIÓN DE IMAGEN ================== //
    @BeanProperty(preferred = true, visualUpdate = true, description = "Posicion del texto")
    public void setTextPosition(TextPosition pos) {
        switch (pos) {
            case CENTER:
                setHorizontalAlignment(SwingConstants.CENTER);
                setVerticalAlignment(SwingConstants.CENTER);
                break;
            case TOP:
                setHorizontalAlignment(SwingConstants.CENTER);
                setVerticalAlignment(SwingConstants.TOP);
                break;
            case BOTTOM:
                setHorizontalAlignment(SwingConstants.CENTER);
                setVerticalAlignment(SwingConstants.BOTTOM);
                break;
            case LEFT:
                setHorizontalAlignment(SwingConstants.LEFT);
                setVerticalAlignment(SwingConstants.CENTER);
                break;
            case RIGHT:
                setHorizontalAlignment(SwingConstants.RIGHT);
                setVerticalAlignment(SwingConstants.CENTER);
                break;
        }
    }

    @BeanProperty(preferred = true, visualUpdate = true, description = "Opacidad de la imagen (0.0f transparente - 1.0f opaco)")
    public void setOpacity(float opacity) {
        this.opacity = Math.max(0.0f, Math.min(1.0f, opacity));
        repaint();
    }

    @BeanProperty(preferred = true, visualUpdate = true, description = "Rotación de la imagen en grados")
    public void setRotation(float rotation) {
        this.rotation = rotation % 360;
        repaint();
    }

    @BeanProperty(preferred = true, visualUpdate = true, description = "Escala horizontal (distorsión)")
    public void setScaleX(float scaleX) {
        this.scaleX = Math.max(0.1f, scaleX);
        repaint();
    }

    @BeanProperty(preferred = true, visualUpdate = true, description = "Escala vertical (distorsión)")
    public void setScaleY(float scaleY) {
        this.scaleY = Math.max(0.1f, scaleY);
        repaint();
    }

    @BeanProperty(preferred = true, visualUpdate = true, description = "Cambio de tono (0-360 grados)")
    public void setHueShift(float hueShift) {
        this.hueShift = hueShift % 360;
        repaint();
    }

    @BeanProperty(preferred = true, visualUpdate = true, description = "Saturación (0.0f - 2.0f)")
    public void setSaturation(float saturation) {
        this.saturation = Math.max(0.0f, Math.min(2.0f, saturation));
        repaint();
    }

    @BeanProperty(preferred = true, visualUpdate = true, description = "Brillo (0.0f - 2.0f)")
    public void setBrightness(float brightness) {
        this.brightness = Math.max(0.0f, Math.min(2.0f, brightness));
        repaint();
    }

    public void setImageIcon(String folder, String filename) {
        if (folder != null && filename != null) {
            setImageIcon("/" + folder + "/" + filename);
        } else {
            System.err.println("Ruta inválida.");
            imageLoaded = false;
        }
    }

    public void setText(String text, int size) {
        setText(text, size, TextPosition.CENTER);
    }

    public void setText(String text, int size, TextPosition position) {
        setText(text);
        setFontSize(size);
        setTextPosition(position);
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

    public void setImageIcon(String path) {
        if (path != null) {
            try {
                Image img = new ImageIcon(getClass().getResource(path.startsWith("/") ? path : "/" + path)).getImage();
                image = img;
                imageLoaded = true;
                revalidate();
                repaint();
            } catch (Exception e) {
                System.err.println("Error cargando imagen desde el recurso: " + e.getMessage());
                imageLoaded = false;
            }
        } else {
            System.err.println("Ruta de imagen inválida.");
            imageLoaded = false;
        }
    }

    private boolean isValidImageFile(File file) {
        try {
            return ImageIO.read(file) != null;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imageLoaded && image != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            BufferedImage tempImage = createProcessedImage();

            applyGeometricTransformations(g2d, tempImage);

            g2d.dispose();
        }
    }

    private BufferedImage createProcessedImage() {
        BufferedImage sourceImg;
        if (image instanceof BufferedImage bufferedImage) {
            sourceImg = bufferedImage;
        } else {
            sourceImg = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = sourceImg.createGraphics();
            g2d.drawImage(image, 0, 0, null);
            g2d.dispose();
        }

        if (hueShift != 0 || saturation != 1 || brightness != 1) {
            return applyColorEffects(sourceImg);
        }
        return sourceImg;
    }

    private BufferedImage applyColorEffects(BufferedImage source) {
        int width = source.getWidth();
        int height = source.getHeight();
        BufferedImage dest = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = source.getRGB(x, y);
                float[] hsv = rgbToHsv(argb);

                hsv[0] = (hsv[0] + hueShift) % 360;
                hsv[1] = Math.max(0, Math.min(1, hsv[1] * saturation));
                hsv[2] = Math.max(0, Math.min(1, hsv[2] * brightness));

                dest.setRGB(x, y, hsvToRgb(hsv[0], hsv[1], hsv[2], (argb >> 24) & 0xFF));
            }
        }
        return dest;
    }

    private void applyGeometricTransformations(Graphics2D g2d, BufferedImage image) {
        AffineTransform originalTransform = g2d.getTransform();

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));

        g2d.translate(centerX, centerY);

        g2d.rotate(Math.toRadians(rotation));

        int newWidth = (int) (getWidth() * scaleX);
        int newHeight = (int) (getHeight() * scaleY);
        int x = -newWidth / 2;
        int y = -newHeight / 2;

        g2d.drawImage(image, x, y, newWidth, newHeight, null);
        g2d.setTransform(originalTransform);
    }

    // ================== HELPERS DE CONVERSIÓN DE COLOR ================== //
    private float[] rgbToHsv(int argb) {
        int r = (argb >> 16) & 0xFF;
        int g = (argb >> 8) & 0xFF;
        int b = argb & 0xFF;

        float[] hsv = new float[3];
        Color.RGBtoHSB(r, g, b, hsv);
        hsv[0] *= 360;
        return hsv;
    }

    private int hsvToRgb(float h, float s, float v, int alpha) {
        h = (h % 360) / 360f;
        s = Math.max(0, Math.min(1, s));
        v = Math.max(0, Math.min(1, v));

        int rgb = Color.HSBtoRGB(h, s, v);
        return (alpha << 24) | (rgb & 0x00FFFFFF);
    }
    //+++++++++++++++++++++END CODE MAIN+++++++++++++++++++++++++++++++ //
}
