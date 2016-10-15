package com.ba;
import javax.swing.*;
import java.awt.*;


public class ChatBot extends JFrame {

    JPanel panel = new JPanel();
    JTextArea textArea = new JTextArea(20, 50);
    JTextArea inputArea = new JTextArea(1, 50);
    JScrollPane scrollPane = new JScrollPane(
            textArea,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
    );

    String[][] chatBot = {
            {},
            {},
            {},
            {},
            {}
    };

    public ChatBot() {
        super("Super Cool Chat Bot");
        setSize(600, 400);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        textArea.setEditable(false);
        //inputArea.addKeyListener(this);

        panel.add(scrollPane);
        panel.add(inputArea);
        panel.setBackground(new Color(255,200,0));
        add(panel);

        setVisible(true);
    }

    public static void main(String[] args) { new ChatBot(); }
}
