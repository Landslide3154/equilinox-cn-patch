/*
 * Decompiled with CFR 0.152.
 */
package errors;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class ErrorPopUp {
    private static final int PAD = 8;
    private static final int WIDTH = 330;
    private static final int HEIGHT = 450;

    public static void showPopUp(String title, String message, String errorMessage) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        JFrame frame = ErrorPopUp.createFrame(title);
        ErrorPopUp.addMessage(message, frame);
        JTextArea textField = ErrorPopUp.createTextArea(errorMessage);
        ErrorPopUp.addScrollPanel(textField, frame);
        frame.setVisible(true);
    }

    private static JFrame createFrame(String title) {
        JFrame frame = new JFrame();
        frame.setResizable(false);
        frame.setTitle(title);
        frame.setSize(330, 450);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(3);
        return frame;
    }

    private static void addMessage(String message, JFrame frame) {
        JTextArea label = new JTextArea(message);
        label.setFont(new Font("SansSerif", 1, 12));
        label.setEditable(false);
        label.setLineWrap(true);
        label.setWrapStyleWord(true);
        label.setMargin(new Insets(8, 8, 8, 8));
        frame.add((Component)label, "North");
    }

    private static JTextArea createTextArea(String errorMessage) {
        JTextArea field = new JTextArea(errorMessage);
        field.setMargin(new Insets(8, 8, 8, 8));
        field.setForeground(Color.RED);
        field.setEditable(false);
        field.setLineWrap(true);
        field.setWrapStyleWord(true);
        return field;
    }

    private static void addScrollPanel(JTextArea textField, JFrame frame) {
        JScrollPane scrollPane = new JScrollPane(textField);
        scrollPane.getInsets(new Insets(8, 8, 8, 8));
        scrollPane.setBorder(new EmptyBorder(8, 8, 8, 8));
        scrollPane.setHorizontalScrollBarPolicy(31);
        frame.add((Component)scrollPane, "Center");
    }
}

