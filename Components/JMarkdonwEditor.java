package ComponentCustoms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

public class JMarkdonwEditor extends JPanel {
    
    private Color[] GithubThemeDark = new Color[]{
        new Color(13, 17, 23),//background--------0
        new Color(248, 248, 242),//ForeLigth------1
        new Color(150, 150, 150),//ForeDeco------2
        new Color(54, 58, 67),//BorderSeparator--3
        new Color(250, 94, 85)//BorderSelect------4
    };

    private JLabel[] labelsDecoration = new JLabel[]{
        new JLabel("📖"),
        new JLabel("README"),
        new JLabel("⚖"),
        new JLabel("️MIT license"),};

    private buttonMarkdonw[] buttonsOptions = new buttonMarkdonw[]{
        new buttonMarkdonw("WRITE"),
        new buttonMarkdonw("GETCONTENT"),
        new buttonMarkdonw("html"),
        new buttonMarkdonw("Mark")
    };

    private buttonMarkdonw[] buttonsTool = new buttonMarkdonw[]{
        new buttonMarkdonw("CE"),
        new buttonMarkdonw("H1"),
        new buttonMarkdonw("H2"),
        new buttonMarkdonw("H3"),
        new buttonMarkdonw("H4"),
        new buttonMarkdonw("H5"),
        new buttonMarkdonw("H6"),
        new buttonMarkdonw("P"),
        new buttonMarkdonw("UL"),
        new buttonMarkdonw("OL"),
        new buttonMarkdonw("BL"),
        new buttonMarkdonw("PRE"),
        new buttonMarkdonw("CODE"),
        new buttonMarkdonw("PL"),
        new buttonMarkdonw("B"),
        new buttonMarkdonw("EM"),
        new buttonMarkdonw("S"),
        new buttonMarkdonw("HR"),
        new buttonMarkdonw("A"),
        new buttonMarkdonw("IMG"),
        new buttonMarkdonw("THEME")
    };
    // Constructor

    public JMarkdonwEditor() {
        initComponents();
    }

    private JDialogUrlAndTitle DialogTitleLink = new JDialogUrlAndTitle(this);
    private int PanelOptionsHeigth = 45;
    private int PanelToolbarHeigth = 40;
    private int MaxinSizeButtonTool = 300;
    private String contentHtmlText = "<h1>Title</h1><p>Hello World</p>";
    private JPanel PanelContentReadme = new JPanel(new BorderLayout());
    private JPanel PanelOptions = new JPanel(new BorderLayout());
    private JPanel PanelRigth = new JPanel(null);
    private JPanel PanelLeft = new JPanel(null);
    private JPanel PanelDecorationSelect = new JPanel(null);
    private JPanel PanelToolBar = new JPanel();

    private Font fontDialog12 = new Font("Dialog", Font.BOLD, 12);

    private JEditorPane HtmlEditor = new JEditorPane("text/html", MakeCss(GithubThemeDark[0], GithubThemeDark[1]) + contentHtmlText);
    private JPopupMenu menuHtmlMark;
    private JTextArea ResultArea = new JTextArea();

    private void initComponents() {
        setBackground(GithubThemeDark[0]);
        setForeground(GithubThemeDark[1]);
        setBorder(new javax.swing.border.EmptyBorder(5, 5, 5, 5));
        setLayout(new BorderLayout());
        initLabelsDecorations();
        add(getPanelToolBarConfigured(), BorderLayout.SOUTH);
        add(LoadContent());
        initResultArea();
        initOptions();
        initButtonsOptionsAndTool();
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateImageWidths(HtmlEditor);
            }
        });
        HtmlEditor.addKeyListener(getKeyCtrlV());

    }

    private void initResultArea() {
        ResultArea.setBackground(getBackground());
        ResultArea.setForeground(getForeground());
        ResultArea.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        ResultArea.setFont(new Font("Arial", Font.BOLD, 16));
    }

    private void initOptions() {
        menuHtmlMark = new JPopupMenu();
        menuHtmlMark.setBackground(Transparent);
        menuHtmlMark.setBorderPainted(false);
        menuHtmlMark.add(buttonsOptions[2]);
        menuHtmlMark.add(buttonsOptions[3]);
    }

    private void initButtonsOptionsAndTool() {
        for (int i = 0; i <= buttonsTool.length - 1; i++) {
            if (i <= buttonsOptions.length - 1) {
                configAppearance(buttonsOptions[i], PanelOptionsHeigth);
            }
            configAppearance(buttonsTool[i], MaxinSizeButtonTool);
            PanelToolBar.add(buttonsTool[i]);
        }
        //HACK: CORRECION DE BORDES
        buttonsOptions[2].setBorder(new javax.swing.border.LineBorder(GithubThemeDark[3], 1));
        buttonsOptions[3].setBorder(new javax.swing.border.LineBorder(GithubThemeDark[3], 1));
    }

    private void configAppearance(buttonMarkdonw button, int MaximSize) {
        button.setMaximumSize(new Dimension(MaximSize, MaximSize));
        button.setPreferredSize(new Dimension(MaximSize, MaximSize));
        button.addMouseListener(AdapterSelectionViaText(button.getText()));
        button.setText(iconOtDefault(button.getText()));
    }

    private String iconOtDefault(String buttonText) {
        return switch (buttonText) {
            case "WRITE" ->
                "🖉";
            case "GETCONTENT" ->
                "⋮☰";
            case "PRE" ->
                "<>";
            case "CODE" ->
                ">_";
            case "OL" ->
                "1☰";
            case "UL" ->
                "⠸☰";
            case "BL" ->
                "❙☰";
            case "B" ->
                "B";
            case "EM" ->
                "i";
            case "S" ->
                "-S-";
            case "HR" ->
                "―";
            case "A" ->
                "🔗";
            case "IMG" ->
                "◳";
            case "THEME" ->
                "☀";
            default ->
                buttonText;
        };
    }

    private Map<String, MouseAdapter> Adapters = new HashMap<String, MouseAdapter>() {
        {
            put("WRITE", getModeEditor());
            put("GETCONTENT", getModeMenu());
            put("html", getModeViewBodyHtml());
            put("Mark", getModeViewMarkdonw());
            put("CE", getModeRemove());
            put("PRE", getAdapterTagCaret(HTML.Tag.PRE));
            put("H1", getAdapterTagCaret(HTML.Tag.H1));
            put("H2", getAdapterTagCaret(HTML.Tag.H2));
            put("H3", getAdapterTagCaret(HTML.Tag.H3));
            put("H4", getAdapterTagCaret(HTML.Tag.H4));
            put("H5", getAdapterTagCaret(HTML.Tag.H5));
            put("H6", getAdapterTagCaret(HTML.Tag.H6));
            put("P", getAdapterTagCaret(HTML.Tag.P));
            put("UL", getAdapterTagCaret(HTML.Tag.UL));
            put("OL", getAdapterTagCaret(HTML.Tag.OL));
            put("BL", getAdapterTagCaret(HTML.Tag.BLOCKQUOTE));
            put("PL", getAdapterTagSelection(HTML.Tag.SPAN));
            put("CODE", getAdapterTagSelection(HTML.Tag.CODE));
            put("B", getAdapterTagSelection(HTML.Tag.STRONG));
            put("EM", getAdapterTagSelection(HTML.Tag.EM));
            put("S", getAdapterTagSelection(HTML.Tag.S));
            put("HR", getAdapterTagCaret(HTML.Tag.HR));
            put("A", getAdapterTagCaret(HTML.Tag.A));
            put("IMG", getAdapterTagCaret(HTML.Tag.IMG));
            put("THEME", getAdapterTheme());
        }
    };

    private MouseAdapter AdapterSelectionViaText(String buttonText) {
        return Adapters.getOrDefault(buttonText, getAdapterTagCaret(HTML.Tag.P));
    }

    private MouseAdapter getAdapterTagCaret(HTML.Tag tag) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                addTagCaret(HtmlEditor, tag);
            }
        };
    }

    private MouseAdapter getAdapterTagSelection(HTML.Tag tag) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                insertHtmlAroundSelection(HtmlEditor, tag);
            }
        };
    }

    private KeyAdapter getKeyCtrlV() {
        return new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_V && e.isControlDown()) {
                    e.consume();
                    try {
                        String textoPegado = (String) Toolkit.getDefaultToolkit()
                                .getSystemClipboard().getData(DataFlavor.stringFlavor);

                        HtmlEditor.replaceSelection(textoPegado);
                    } catch (HeadlessException | UnsupportedFlavorException | IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };
    }

    private MouseAdapter getAdapterTheme() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                buttonMarkdonw button = (buttonMarkdonw) e.getSource();
                contentHtmlText = getBodyContent(HtmlEditor.getText());
                HtmlEditor.setText(MakeCss(invertColor(getBackground()), invertColor(getForeground())) + contentHtmlText);
                HtmlEditor.setBackground(invertColor(getBackground()));
                labelsDecoration[1].setForeground(invertColor(labelsDecoration[1].getForeground()));
                labelsDecoration[3].setForeground(invertColor(labelsDecoration[3].getForeground()));
                setBackground(invertColor(getBackground()));
                setForeground(invertColor(getForeground()));
                button.setText((getBackground().equals(GithubThemeDark[0])) ? "☀" : "🌙");
                for (buttonMarkdonw buttons : buttonsTool) {
                    buttons.setBackgroundPlus(getBackground());
                }
                for (buttonMarkdonw buttons : buttonsOptions) {
                    buttons.setBackgroundPlus(getBackground());
                }
                revalidate();
                repaint();
                PanelToolBar.repaint();
            }
        };
    }

    private MouseAdapter getModeEditor() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                add(PanelToolBar, BorderLayout.SOUTH);
                ScrollHtml.setViewportView(HtmlEditor);
                revalidate();
                repaint();
            }
        };
    }

    private MouseAdapter getModeRemove() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                removeElementAtCaret(HtmlEditor);
            }
        };
    }

    private MouseAdapter getModeMenu() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                menuHtmlMark.show(buttonsOptions[1], 0, PanelOptionsHeigth - 4);
                menuHtmlMark.repaint();
            }
        };
    }

    private MouseAdapter getModeViewMarkdonw() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                remove(PanelToolBar);
                ScrollHtml.setViewportView(ResultArea);
                ResultArea.setText(htmlToMarkdown(HtmlEditor.getText()));
                ResultArea.setBackground(getBackground());
                ResultArea.setForeground(getForeground());
                revalidate();
                repaint();
            }
        };
    }

    private MouseAdapter getModeViewBodyHtml() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                remove(PanelToolBar);
                ScrollHtml.setViewportView(ResultArea);
                ResultArea.setText(getBodyContent(HtmlEditor.getText()));
                ResultArea.setBackground(getBackground());
                ResultArea.setForeground(getForeground());
                revalidate();
                repaint();
            }
        };
    }

    private void updateImageWidths(JEditorPane editorPane) {
        String html = editorPane.getText();
        String updatedHtml = html.replaceAll(
                "<img([^>]+)>",
                "<img$1 width=\"" + Math.min(ScrollHtml.getWidth() - 60, 720) + "\">"
        );
        editorPane.setText(updatedHtml);
    }

    private void initLabelsDecorations() {
        for (int i = 0; i <= labelsDecoration.length - 1; i++) {
            labelsDecoration[i].setForeground((labelsDecoration[i].getText().equalsIgnoreCase("Readme.md")) ? GithubThemeDark[1] : GithubThemeDark[2]);
        }
        labelsDecoration[0].setFont(new Font("Dialog", Font.BOLD, 17));
        labelsDecoration[1].setFont(fontDialog12);
        labelsDecoration[2].setFont(new Font("Dialog", Font.PLAIN, 18));
        labelsDecoration[3].setFont(fontDialog12);
    }

    private JPanel LoadContent() {
        PanelContentReadme.setBackground(Transparent);
        PanelContentReadme.setBorder(new javax.swing.border.LineBorder(GithubThemeDark[3], 1));
        PanelContentReadme.add(getEditorHtmlConfigured());
        PanelContentReadme.add(getPanelOptionsConfigured(), BorderLayout.NORTH);
        return PanelContentReadme;
    }
    JavaScrollPanePlus ScrollHtml = new JavaScrollPanePlus();

    private JavaScrollPanePlus getEditorHtmlConfigured() {
        HtmlEditor.setBackground(getBackground());
        ScrollHtml.setOpaque(true);
        ScrollHtml.setBackground(Transparent);
        ScrollHtml.setViewportView(HtmlEditor);
        return ScrollHtml;
    }

    private JPanel getPanelOptionsConfigured() {
        PanelOptions.setBackground(Transparent);
        PanelOptions.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, GithubThemeDark[3]));
        PanelOptions.setPreferredSize(new Dimension(40, PanelOptionsHeigth));

        PanelOptions.add(getPanelLeftDecorationConfigured(), BorderLayout.LINE_START);
        PanelOptions.add(getPanelRigthOptionsConfigured(), BorderLayout.LINE_END);

        return PanelOptions;
    }

    private JPanel getPanelLeftDecorationConfigured() {
        PanelLeft.setPreferredSize(new Dimension(230, PanelOptionsHeigth));
        PanelLeft.setBackground(Transparent);

        PanelDecorationSelect.setSize(100, PanelOptionsHeigth);
        PanelDecorationSelect.setLocation(10, 0);
        PanelDecorationSelect.setBackground(Transparent);
        PanelDecorationSelect.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 0, GithubThemeDark[4]));

        labelsDecoration[0].setSize(20, 45);
        labelsDecoration[0].setLocation(2, 0);
        PanelDecorationSelect.add(labelsDecoration[0]);

        labelsDecoration[1].setSize(80, 45);
        labelsDecoration[1].setLocation(25, 0);
        PanelDecorationSelect.add(labelsDecoration[1]);

        labelsDecoration[2].setSize(20, 45);
        labelsDecoration[2].setLocation(120, 0);
        PanelLeft.add(labelsDecoration[2]);

        labelsDecoration[3].setSize(80, 45);
        labelsDecoration[3].setLocation(145, 0);
        PanelLeft.add(labelsDecoration[3]);

        PanelLeft.add(PanelDecorationSelect);
        return PanelLeft;
    }

    Color Transparent = new Color(0, 0, 0, 0);

    private JPanel getPanelRigthOptionsConfigured() {
        PanelRigth.setBackground(Transparent);
        PanelRigth.setPreferredSize(new Dimension(PanelOptionsHeigth * 2, PanelOptionsHeigth));
        PanelRigth.setLayout(new BoxLayout(PanelRigth, BoxLayout.X_AXIS));
        PanelRigth.add(buttonsOptions[0]);
        PanelRigth.add(buttonsOptions[1]);
        return PanelRigth;
    }

    private JPanel getPanelToolBarConfigured() {
        PanelToolBar.setPreferredSize(new Dimension(PanelToolbarHeigth, PanelToolbarHeigth));
        PanelToolBar.setLayout(new BoxLayout(PanelToolBar, BoxLayout.X_AXIS));
        PanelToolBar.setBackground(Transparent);
        return PanelToolBar;
    }

    /*=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-+
     | ~ ~ ~ Method:                                                 |
     | ~Include:                                                         |
     +=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=+*/
    private String MakeCss(Color colorBackg, Color colorFore) {
        String CssBackground = "rgb(" + subtractColorString(colorBackg) + ")";
        String CssForeGround = "rgb(" + subtractColorString(colorFore) + ")";

        return "<style> body { background-color:" + CssBackground + "; color:" + CssForeGround + ";"
                + " font-family: 'Arial', sans-serif; margin: 20px; }"
                + " h1, h2 { border-bottom: 1px solid #464650; }"
                + " h1, h2, h3, h4, h5 { padding-bottom: 5px; }"
                + " h1 { font-size: 24px; }"
                + " h2 { font-size: 18px; }"
                + " h3 { font-size: 14px; }"
                + " h4 { font-size: 12px; }"
                + " h5 { font-size: 10px; }"
                + " h6 { font-family: 'Arial'; font-size: 8px; padding-bottom: 5px; }"
                + " ul, ol { font-size: 12px; }"
                + " li { font-size: 12px; padding: 5px; }"
                + " p { font-size: 12px; line-height: 1.6; margin-bottom: 5px; }"
                + " footer { font-size: 12px; color: #fff; }"
                + " a { font-family: 'Arial';  color: #58A6FF;}"
                + " code { font-family: monospace; font-size: 10px; background-color: #141A23; text-align: center; padding: 5px; }"
                + " span { font-size: 12px; font-family: 'Arial', sans-serif; }"
                + " pre { padding: 10px; background-color: #141A23; }"
                + " blockquote { font-size: 12px; border-left: 4px solid #3D454C; padding-left: 5px; }"
                + " table { margin: 0px; border-spacing: 0; }"
                + " th, td { padding: 8px; border: 1px solid #eee; }"
                + " th { font-weight: bold; }"
                + " </style>";
    }

//<editor-fold desc="markdownFuntions">
    public void addTagCaret(JEditorPane editorPane, HTML.Tag tag) {
        HTMLDocument doc = (HTMLDocument) editorPane.getDocument();
        HTMLEditorKit editorKit = (HTMLEditorKit) editorPane.getEditorKit();
        int caretPos = editorPane.getCaretPosition();
        Element paragraph = doc.getParagraphElement(caretPos);
        Element listElement = findParentListElement(paragraph);
        try {
            if (isHeaderTag(tag)) {
                insertBeforeTag(editorKit, doc, paragraph.getStartOffset(), tag);
            } else if (isListTag(tag)) {
                insertListTag(editorPane, editorKit, doc, paragraph, listElement, tag);
            } else if (isURLTag(tag)) {
                DialogTitleLink.showAndUptdateDialog();
                if (DialogTitleLink.getURL().isBlank() && DialogTitleLink.getTitle().isBlank()) {
                    return;
                }
                insertImgOrLinkAtCaret(DialogTitleLink.getURL(), DialogTitleLink.getTitle(), (ScrollHtml.getWidth() - 80), editorPane, editorKit, doc, paragraph, tag);
            } else {
                insertAfterTag(editorPane, editorKit, doc, paragraph, listElement, tag);
            }
        } catch (IOException | BadLocationException e) {
            e.printStackTrace();
        }
    }

    private static boolean isHeaderTag(HTML.Tag tag) {
        return tag.toString().toLowerCase().startsWith("h");
    }

    private boolean isListTag(HTML.Tag tag) {
        String tagName = tag.toString().toLowerCase();
        return tagName.equals("ul") || tagName.equals("ol");
    }

    private boolean isURLTag(HTML.Tag tag) {
        String tagName = tag.toString().toLowerCase();
        return tagName.equals("img") || tagName.equals("a");
    }

    private Element findParentListElement(Element element) {
        Element current = element;
        while (current != null) {
            String name = current.getName().toLowerCase();
            if (name.equals("ul") || name.equals("ol")) {
                return current;
            }
            current = current.getParentElement();
        }
        return null;
    }

    private void insertBeforeTag(HTMLEditorKit editorKit, HTMLDocument doc, int offset, HTML.Tag tag)
            throws IOException, BadLocationException {
        editorKit.insertHTML(doc, offset, buildHtmlTag(tag), 0, 0, null);
    }

    private void insertListTag(JEditorPane editorPane, HTMLEditorKit editorKit, HTMLDocument doc,
            Element paragraph, Element listElement, HTML.Tag tag)
            throws IOException, BadLocationException {
        if (listElement != null) {
            doc.insertBeforeEnd(listElement, "<li>li</li>");
        } else {
            int caretPos = Math.min(paragraph.getEndOffset(), doc.getLength());
            int docLength = doc.getLength();
            editorKit.insertHTML(doc, caretPos,
                    "<" + tag.toString() + "><li>li</li></" + tag.toString() + ">",
                    0, 0, null);
            if (caretPos == docLength) {
                editorPane.setCaretPosition(doc.getLength());
            }
        }
    }

    private void insertAfterTag(JEditorPane editorPane, HTMLEditorKit editorKit, HTMLDocument doc,
            Element paragraph, Element listElement, HTML.Tag tag)
            throws IOException, BadLocationException {
        if (listElement != null) {
            int caretPos = Math.min(paragraph.getEndOffset(), doc.getLength());
            int docLength = doc.getLength();
            editorKit.insertHTML(doc, caretPos, buildHtmlTag(tag), 0, 0, null);
            if (caretPos == docLength) {
                editorPane.setCaretPosition(doc.getLength());
            }
        } else {
            doc.insertAfterEnd(paragraph, buildHtmlTag(tag));
        }
    }

    private String buildHtmlTag(HTML.Tag tag) {
        String tagStr = tag.toString();
        return "<" + tagStr + ">" + ((tag == HTML.Tag.HR) ? "" : tagStr) + "</" + tagStr + ">";
    }

    private void insertImgOrLinkAtCaret(String url, String title, int WidthScroll, JEditorPane editorPane, HTMLEditorKit editorKit, HTMLDocument doc, Element paragraph, HTML.Tag tag)
            throws BadLocationException, IOException {

        int caretPos = Math.min(editorPane.getCaretPosition(), doc.getLength());
        String tagHtml;

        if (tag == HTML.Tag.A) {
            tagHtml = "<a href=\"" + url + "\">" + title + "</a>";
            editorKit.insertHTML(doc, caretPos, tagHtml, 0, 0, HTML.Tag.A);
            return;
        }

        tagHtml = "<img src=\"" + url + "\" alt=\"" + title + "\" width=\"" + Math.min(WidthScroll, 720) + "\"  >";
        if (paragraph != null) {
            int insertPos = Math.min(paragraph.getEndOffset(), doc.getLength());
            editorKit.insertHTML(doc, insertPos, tagHtml, 0, 0, null);
            editorPane.setCaretPosition(doc.getLength());
        }
    }

    private int numTags = 2;

    private void removeElementAtCaret(JEditorPane editorPane) {
        HTMLDocument doc = (HTMLDocument) editorPane.getDocument();
        int caretPos = editorPane.getCaretPosition();
        Element tagPosition = doc.getParagraphElement(caretPos);

        numTags = countTagsHtml(getBodyContent(editorPane.getText()));

        if (numTags < 2) {
            addTagCaret(editorPane, HTML.Tag.P);
        }

        if (doc.getDefaultRootElement().getElementCount() > 1) {
            doc.removeElement(tagPosition);
        }
        numTags = countTagsHtml(getBodyContent(editorPane.getText()));
    }

    private int countTagsHtml(String htmlContent) {
        String regex = "<(?!b|strong|s|em|code)[a-zA-Z0-9]+\\b[^>]*>";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(htmlContent);

        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    private void insertHtmlAroundSelection(JEditorPane editorPane, HTML.Tag tag) {
        String selectedText = editorPane.getSelectedText();
        if (selectedText == null) {
            return;
        }

        try {
            HTMLDocument doc = (HTMLDocument) editorPane.getDocument();
            HTMLEditorKit editorKit = (HTMLEditorKit) editorPane.getEditorKit();
            int caretPos = editorPane.getSelectionStart();
            Element paragraph = doc.getParagraphElement(caretPos);
            int startTag = paragraph.getStartOffset() + 1;

            applyHtmlTag(editorPane, editorKit, doc, tag, selectedText, caretPos, startTag);

        } catch (BadLocationException | IOException ex) {
        }
    }

    private void applyHtmlTag(JEditorPane editorPane, HTMLEditorKit editorKit, HTMLDocument doc,
            HTML.Tag tag, String selectedText, int caretPos, int startTag)
            throws BadLocationException, IOException {

        if (caretPos < startTag) {
            editorPane.replaceSelection(" ");
            editorKit.insertHTML(doc, startTag, "<" + tag + ">" + selectedText + "</" + tag + ">", 0, 0, tag);
            resetSelection(editorPane, startTag);
        } else {
            editorPane.replaceSelection("");
            editorKit.insertHTML(doc, caretPos, "<" + tag + ">" + selectedText + "</" + tag + ">", 0, 0, tag);
        }
    }

    private void resetSelection(JEditorPane editorPane, int startTag) {
        editorPane.setSelectionStart(startTag - 1);
        editorPane.setSelectionEnd(startTag);
        editorPane.replaceSelection("");
    }

    private String htmlToMarkdown(String htmlText) {
        String markdown = getBodyContent(htmlText);

        // Reemplazos directos
        markdown = markdown.replaceAll("</strong>\\s*", "** ")
                .replaceAll("<strong>\\s*", " **")
                .replaceAll("</?em>\\s*", "_")
                .replaceAll("<p>\\s*", "")
                .replaceAll("</?s>\\s*", "~~")
                .replaceAll("<pre>\\s*", "```java\n")
                .replaceAll("</pre>\\s*", "\n```\n")
                .replaceAll("<blockquote>\\s*", "> ")
                .replaceAll("</?code>", "`")
                .replaceAll("<hr>", "***")
                .replaceAll("&quot;", "\"");

        Pattern headerPattern = Pattern.compile("<h([1-6])>\\s*");
        Matcher headerMatcher = headerPattern.matcher(markdown);
        StringBuffer headerBuffer = new StringBuffer();
        while (headerMatcher.find()) {
            headerMatcher.appendReplacement(headerBuffer, "#".repeat(Integer.parseInt(headerMatcher.group(1))) + " ");
        }

        headerMatcher.appendTail(headerBuffer);
        markdown = headerBuffer.toString();

        markdown = markdown.replaceAll("<img[^>]+src=\"([^\"]+)\"[^>]*alt=\"([^\"]*)\"[^>]*>", "![$2]($1)");

        markdown = markdown.replaceAll("<a href=\"([^\"]+)\">([^<]+)</a>", "[$2]($1)");

        Pattern numericPattern = Pattern.compile("&#(\\d+);");
        Matcher numericMatcher = numericPattern.matcher(markdown);
        StringBuffer numericBuffer = new StringBuffer();
        while (numericMatcher.find()) {
            numericMatcher.appendReplacement(numericBuffer, String.valueOf((char) Integer.parseInt(numericMatcher.group(1))));
        }

        numericMatcher.appendTail(numericBuffer);
        markdown = numericBuffer.toString();

        Pattern listPattern = Pattern.compile("<(ol|ul)>(.*?)</\\1>", Pattern.DOTALL);
        Matcher listMatcher = listPattern.matcher(markdown);
        StringBuffer listBuffer = new StringBuffer();
        while (listMatcher.find()) {
            String prefix = listMatcher.group(1).equals("ol") ? "1. " : "* ";
            String listContent = listMatcher.group(2)
                    .replaceAll("<li>\\s*", prefix)
                    .replaceAll("</li>\\s*", "");
            listMatcher.appendReplacement(listBuffer, listContent);
        }
        listMatcher.appendTail(listBuffer);
        markdown = listBuffer.toString();

        markdown = markdown.replaceAll("</?[a-zA-Z0-9]+>", "").replaceAll("\n\n\n", "\n");

        return markdown.trim();
    }

    private String getBodyContent(String htmlContent) {
        int bodyStart = htmlContent.indexOf("<body>") + "<body>".length();
        int bodyEnd = htmlContent.indexOf("</body>");

        if (bodyStart == -1 || bodyEnd == -1 || bodyStart >= bodyEnd) {
            return "";
        }

        String bodyContent = htmlContent.substring(bodyStart, bodyEnd).trim();
        bodyContent = clearBody(bodyContent);

        return bodyContent;
    }

    private String clearBody(String bodyHtml) {
        String bodyContent = bodyHtml;

        bodyContent = bodyContent.replaceAll("</?span>", "");

        Pattern pattern = Pattern.compile("&#(\\d+);");
        Matcher matcher = pattern.matcher(bodyContent);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            int charCode = Integer.parseInt(matcher.group(1));
            matcher.appendReplacement(sb, String.valueOf((char) charCode));
        }

        matcher.appendTail(sb);
        bodyContent = sb.toString();
        bodyContent = bodyContent.replaceAll("(?<=<[a-zA-Z0-9]+>)\\n", " ");
        bodyContent = bodyContent.replaceAll(" width=\"\\d+\"", "");
        bodyContent = bodyContent.replaceAll("(?<=</(?!strong|em|s\\b)[a-zA-Z0-9]+>)", "\n");
        bodyContent = bodyContent.replaceAll("([a-zA-Z0-9-]+)\\n", "$1 ");
        bodyContent = bodyContent.replaceAll(" {2,}", " ");
        bodyContent = bodyContent.replaceAll(" <", "<").replaceAll("> ", ">");

        return bodyContent;
    }

//</editor-fold>
    public static Color invertColor(Color color) {
        int R = 255 - color.getRed();
        int G = 255 - color.getGreen();
        int B = 255 - color.getBlue();
        return new Color(R, G, B);
    }

    public static String subtractColorString(Color color) {
        int R = color.getRed();
        int G = color.getGreen();
        int B = color.getBlue();
        return "" + R + "," + G + "," + B;
    }

    public class JavaScrollPanePlus extends JScrollPane {

        private Color scrollTrackColor = new Color(100, 100, 100);
        private Color scrollThumbColor = new Color(55, 55, 55);
        private int thumbWidth = 10;

        public JavaScrollPanePlus() {
            getVerticalScrollBar().setUI(new SimpleScrollBarUI());
            getHorizontalScrollBar().setUI(new SimpleScrollBarUI());
            setBorder(null);
        }

        // +++++++++++++++++++++ CLASE SIMPLESCROLLBARUI +++++++++++++++++++++ //
        private class SimpleScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {

            @Override
            protected void installDefaults() {
                super.installDefaults();
                scrollbar.setPreferredSize(new Dimension(thumbWidth, thumbWidth));
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(scrollTrackColor);
                g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
                g2.dispose();
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(scrollThumbColor);
                g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 10, 10);
                g2.dispose();
            }

        }
    }

    public class buttonMarkdonw extends JButton {

        public void setBackgroundPlus(Color color) {
            COLORBACKGROUND = color;
            setColorHolderClick();
            repaint();
        }

        public buttonMarkdonw(String text) {
            super(text);
            Config();
        }

        public buttonMarkdonw() {
            super("Click");
            Config();
        }

        private void Config() {
            setForeground(new Color(150, 150, 150));
            setFont(new Font("Dialog", Font.BOLD, 14));
            setFocusable(false);
            setBorder(null);
            setColorHolderClick();
        }

        private void setColorHolderClick() {
            COLORHOVER = COLORBACKGROUND.brighter();
            COLORCLICK = COLORBACKGROUND.darker();
        }

        private Color COLORBACKGROUND = new Color(13, 17, 23);
        private Color COLORCLICK = new Color(130, 130, 255);
        private Color COLORHOVER = new Color(130, 130, 255);

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Rectangle2D RectanguloEsquinas = new Rectangle2D.Double(0, 0, getWidth(), getHeight());
            if (getModel().isPressed()) {
                g2.setColor(COLORCLICK);
            } else if (getModel().isRollover()) {
                g2.setColor(COLORHOVER);
            } else {
                g2.setColor(COLORBACKGROUND);
            }
            g2.fill(RectanguloEsquinas);

            g2.setColor(getForeground());
            g2.setFont(getFont());
            FontMetrics InfoFont = g2.getFontMetrics(getFont()); // Esto proporciona información sobre el tamaño del texto, como su ancho y alto.
            int x = (getWidth() - InfoFont.stringWidth(getText())) / 2; //Centrar =  Boton - Texto / 2
            int y = (getHeight() - InfoFont.getHeight()) / 2 + InfoFont.getAscent(); // Boton - Texto / 2 + Linea de Texto
            g2.drawString(getText(), x, y);

            g2.dispose();
        }
    }

    public class JDialogUrlAndTitle extends JDialog {

        private Font fontDialog12 = new Font("Dialog", Font.BOLD, 12);
        JComponent component;
        JTextField titleText = new JTextField("title");
        JTextField urlText = new JTextField("url");
        JLabel labelTitle = new JLabel("Insert Url");
        buttonMarkdonw buttonCancel = new buttonMarkdonw("Cancel");
        buttonMarkdonw buttonOK = new buttonMarkdonw("OK");
        JPanel panelContent = new JPanel();
        JPanel panelDecoration = new JPanel();
        JLabel labelTitleUrl = new JLabel("Alt Title");
        JLabel labelUrl = new JLabel("URL / DIR");
        JPanel panelTitle = new JPanel();

        public JDialogUrlAndTitle(JComponent component_) {
            component = component_;
            initComponents();
        }

        private void initComponents() {
            configDialog();
            initPanel();
            initLabel();
            initTextFields();
            initButton();
        }

        private void initPanel() {
            panelTitle.setPreferredSize(new Dimension(0, 50));
            panelTitle.setLayout(null);
            panelTitle.setBorder(new javax.swing.border.LineBorder(GithubThemeDark[3], 1));
            panelTitle.add(labelTitle);

            panelDecoration.setBounds(20, 150, 370, 1);

            panelContent.setBorder(new javax.swing.border.LineBorder(GithubThemeDark[3], 1));
            panelContent.add(urlText);
            panelContent.add(titleText);
            panelContent.add(panelDecoration);
            panelContent.setLayout(null);
            panelContent.add(labelUrl);
            panelContent.add(labelTitleUrl);

            add(panelContent);
            add(panelTitle, BorderLayout.NORTH);
        }

        private void initTextFields() {
            urlText.setFont(fontDialog12);
            urlText.setBackground(new Color(20, 30, 40));
            urlText.setForeground(Color.WHITE);
            urlText.setBounds(20, 40, 370, 25);
            urlText.setBorder(new javax.swing.border.LineBorder(GithubThemeDark[2], 1));

            titleText.setFont(fontDialog12);
            titleText.setBackground(new Color(20, 30, 40));
            titleText.setForeground(Color.WHITE);
            titleText.setBounds(20, 105, 370, 25);
            titleText.setBorder(new javax.swing.border.LineBorder(GithubThemeDark[2], 1));
        }

        private void initLabel() {
            labelTitle.setFont(fontDialog12);
            labelTitle.setBounds(20, 15, 380, 20);

            labelUrl.setFont(fontDialog12);
            labelUrl.setBounds(20, 15, 380, 20);

            labelTitleUrl.setFont(fontDialog12);
            labelTitleUrl.setBounds(20, 80, 380, 20);
        }

        private void initButton() {
            buttonOK.setBorder(new javax.swing.border.LineBorder(GithubThemeDark[2], 1));
            buttonOK.setFont(fontDialog12);
            buttonOK.setFocusable(false);
            buttonOK.setBounds(280, 165, 40, 25);
            buttonOK.addMouseListener(getEventDialogUrl());

            buttonCancel.setBorder(new javax.swing.border.LineBorder(GithubThemeDark[2], 1));
            buttonCancel.setFont(fontDialog12);
            buttonCancel.setFocusable(false);
            buttonCancel.setBounds(330, 165, 60, 25);
            buttonCancel.addMouseListener(getEventDialogUrl());
            panelContent.add(buttonOK);
            panelContent.add(buttonCancel);
        }

        private void configDialog() {
            setModal(true);
            setSize(420, 260);
            setUndecorated(true);
            setBackground(component.getBackground());
            setLayout(new BorderLayout());
        }

        public void showAndUptdateDialog() {
            buttonOK.setBackgroundPlus(component.getBackground().brighter().brighter());
            buttonOK.setForeground(component.getForeground());

            buttonCancel.setBackgroundPlus(component.getBackground().brighter().brighter());
            buttonCancel.setForeground(component.getForeground());

            panelContent.setBackground(component.getBackground());
            labelTitle.setForeground(component.getForeground());

            labelTitleUrl.setForeground(component.getForeground());
            labelUrl.setForeground(component.getForeground());
            panelTitle.setBackground(component.getBackground().brighter());

            setLocation(
                    (int) component.getLocationOnScreen().getX() + (component.getWidth() / 2) - (this.getWidth() / 2),
                    (int) component.getLocationOnScreen().getY() + (component.getHeight() / 2) - (this.getHeight() / 2)
            );
            setVisible(true);
        }

        private MouseAdapter getEventDialogUrl() {
            return new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JButton button = (JButton) e.getSource();
                    if (button.getText().equalsIgnoreCase("Cancel")) {
                        setTextUrlTitleError();
                    }
                    DialogTitleLink.dispose();
                }
            };
        }

        private void setTextUrlTitleError() {
            urlText.setText("");
            titleText.setText("");
        }

        @Override
        public String getTitle() {
            return titleText.getText();
        }

        public String getURL() {
            return urlText.getText();
        }
    }

}
