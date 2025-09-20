package ComponentCustoms;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class JavaTextCompleter {

    private final JTextComponent textComponent;
    private final List<String> suggestions;
    private final boolean popupMode;

    public JavaTextCompleter(JTextComponent tc, boolean popupMode, List<String> suggestions) {
        this.textComponent = tc;
        this.suggestions = new ArrayList<>(suggestions);
        this.popupMode = popupMode;
        setupListeners();
    }

    public JavaTextCompleter(JTextComponent tc, boolean popupMode, String suggestions) {
        this(tc, popupMode, Arrays.asList(suggestions.split("\\s*,\\s*")));
    }

    private void setupListeners() {
        if (popupMode) {
            new PopupSuggester(textComponent, suggestions);
        } else {
            new InlineAutoCompleter(textComponent, suggestions);
        }
    }

    // ======================== UTILITY METHODS ========================
    private static String getCurrentWord(JTextComponent tc) {
        int pos = tc.getCaretPosition();
        String text = tc.getText();

        if (text.isEmpty() || pos > text.length()) {
            return "";
        }

        // Buscar inicio de palabra
        int start = pos;
        while (start > 0 && isValidChar(text.charAt(start - 1))) {
            start--;
        }

        // Buscar fin de palabra
        int end = pos;
        while (end < text.length() && isValidChar(text.charAt(end))) {
            end++;
        }

        return text.substring(start, Math.min(end, text.length()));
    }

    private static boolean isValidChar(char c) {
        return Character.isLetterOrDigit(c) || c == '_';
    }

    // ======================== INLINE AUTOCOMPLETER ========================
    private static class InlineAutoCompleter {
        private final JTextComponent textComponent;
        private final List<String> suggestions;

        public InlineAutoCompleter(JTextComponent tc, List<String> suggestions) {
            this.textComponent = tc;
            this.suggestions = suggestions;
            textComponent.addKeyListener(new AutoCompleteKeyListener());
        }

        private class AutoCompleteKeyListener extends KeyAdapter {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_TAB) {
                    textComponent.setCaretPosition(textComponent.getSelectionEnd());
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (shouldIgnoreKey(e)) return;
                
                String word = getCurrentWord(textComponent);
                if (word.isEmpty()) return;
                
                findSuggestion(word).ifPresent(suggestion -> 
                    insertSuggestion(word, suggestion)
                );
            }

            private boolean shouldIgnoreKey(KeyEvent e) {
                return e.getKeyCode() == KeyEvent.VK_BACK_SPACE ||
                       e.getKeyCode() == KeyEvent.VK_DELETE ||
                       textComponent.getSelectionStart() != textComponent.getSelectionEnd();
            }

            private Optional<String> findSuggestion(String prefix) {
                return suggestions.stream()
                                  .filter(s -> s.startsWith(prefix) && !s.equals(prefix))
                                  .findFirst();
            }

            private void insertSuggestion(String prefix, String suggestion) {
                if (suggestion.length() <= prefix.length()) return;
                
                String completion = suggestion.substring(prefix.length());
                int pos = textComponent.getCaretPosition();
                textComponent.replaceSelection(completion);
                textComponent.select(pos, pos + completion.length());
            }
        }
    }

    // ======================== POPUP SUGGESTER ========================
    private static class PopupSuggester {
        private final JWindow popup = new JWindow();
        private final JList<String> suggestionList = new JList<>();
        private final JTextComponent textComponent;
        private final List<String> suggestions;
        private final PopupKeyListener keyListener = new PopupKeyListener();
        private String lastProcessedWord = "";

        public PopupSuggester(JTextComponent tc, List<String> suggestions) {
            this.textComponent = tc;
            this.suggestions = suggestions;
            
            configurePopup();
            setupListeners();
        }

        private void configurePopup() {
            suggestionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            suggestionList.setBackground(textComponent.getBackground());
            suggestionList.setForeground(textComponent.getForeground());
            suggestionList.setFont(textComponent.getFont());
            
            popup.add(new JScrollPane(suggestionList));
            popup.setSize(200, 120);
            popup.setFocusableWindowState(false);
        }

        private void setupListeners() {
            textComponent.addKeyListener(keyListener);
            textComponent.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    hidePopup();
                }
            });
            
            suggestionList.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() >= 1) {
                        keyListener.selectSuggestion();
                    }
                }
            });
        }

        private void showPopup() {
            if (!popup.isVisible()) {
                positionPopup();
                popup.setVisible(true);
            }
        }

        private void hidePopup() {
            popup.setVisible(false);
            lastProcessedWord = "";
        }

        private void positionPopup() {
            try {
                int caretPos = textComponent.getCaretPosition();
                if (caretPos > textComponent.getDocument().getLength()) return;
                
                Rectangle rect = textComponent.modelToView(caretPos);
                Point screenPos = new Point(rect.x, rect.y + rect.height);
                SwingUtilities.convertPointToScreen(screenPos, textComponent);
                popup.setLocation(screenPos);
            } catch (BadLocationException ignored) {}
        }

        private void updateSuggestions(String prefix) {
            // Filtrar sugerencias que no sean iguales al prefijo actual
            List<String> matches = suggestions.stream()
                    .filter(s -> s.startsWith(prefix) && !s.equals(prefix))
                    .toList();

            suggestionList.setListData(matches.toArray(new String[0]));

            if (matches.isEmpty()) {
                hidePopup();
            } else {
                suggestionList.setSelectedIndex(0);
                showPopup();
            }
        }

        private class PopupKeyListener extends KeyAdapter {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_DOWN -> moveSelection(1);
                    case KeyEvent.VK_UP -> moveSelection(-1);
                    case KeyEvent.VK_ENTER, KeyEvent.VK_TAB -> {
                        selectSuggestion();
                        e.consume();
                    }
                    case KeyEvent.VK_ESCAPE -> hidePopup();
                    default -> updateSuggestionsBasedOnInput();
                }
            }

            private void moveSelection(int direction) {
                if (!popup.isVisible()) return;
                
                int size = suggestionList.getModel().getSize();
                if (size == 0) return;
                
                int newIndex = Math.max(0, Math.min(size - 1, suggestionList.getSelectedIndex() + direction));
                suggestionList.setSelectedIndex(newIndex);
                suggestionList.ensureIndexIsVisible(newIndex);
            }

            private void selectSuggestion() {
                String selected = suggestionList.getSelectedValue();
                if (selected == null) return;
                
                String word = getCurrentWord(textComponent);
                if (selected.length() > word.length()) {
                    String completion = selected.substring(word.length());
                    textComponent.replaceSelection(completion);
                }
                hidePopup();
            }

            private void updateSuggestionsBasedOnInput() {
                String word = getCurrentWord(textComponent);
                
                // Evitar reprocesar la misma palabra
                if (word.equals(lastProcessedWord)) return;
                
                lastProcessedWord = word;

                if (word.isEmpty()) {
                    suggestionList.setListData(new String[0]);
                    hidePopup();
                    return;
                }

                boolean exactMatch = suggestions.stream().anyMatch(s -> s.equals(word));
                if (exactMatch) {
                    hidePopup();
                    return;
                }

                updateSuggestions(word);
            }
        }
    }
}