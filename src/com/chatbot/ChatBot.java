package com.chatbot;
/*
*   Authors: Ben White
*            Amit Jassi
*/
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// ---- ChatBotV2: main class of ChatBot to handle GUI ---- //
public class ChatBot extends JFrame {

    // Create variables needed for GUI:
    // TextArea, TextField, Label, Scroll
    private JPanel panel = new JPanel();
    private JTextArea dialogText = new JTextArea(45, 50);
    private JTextField inputField = new JTextField(35);
    private JScrollPane scroll = new JScrollPane(
            dialogText,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
            );
    private JLabel inputLabel = new JLabel("Question the bot here:");

    // String constants for headers of Bot and Human
    private final String bot = "TechBot :\t";
    private final String human = "Human :\t";

    // New String Processor for processing input
    private StringProcessor sp = new StringProcessor();

    // -- Constructor: Set up GUI, assign default params, housekeeping -- //
    public ChatBot() {
        // Set Title of Frame, run the bot using startBot()
        super("Much Chat, Such Bot");
        startBot();

        // Set params for TextArea and TextField:
        // Editable and listener
        dialogText.setEditable(false);
        addListener(inputField);

        // Add all GUI items to panel
        panel.add(inputLabel);
        panel.add(inputField);
        panel.add(scroll);

        //Add panel to JFrame
        add(panel);

        // Config JFrame: Set default params for:
        // Size, resizable, visible, default close, location relative to
        setSize(600, 800);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    } // End Constructor

    // -- startBot: initial output and grab users name -- //
    void startBot() {
        // Using JOP: to start conversation with the user
        JOptionPane.showMessageDialog(null, "Hello!");
        String name = JOptionPane.showInputDialog(null, "What's your name?");
        if(name.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Welcome, lets begin!");
        } else {
            JOptionPane.showMessageDialog(null, "Welcome " + name + ", lets begin!");
        }

        // Initial output by Bot to user
        addTextToArea(bot + "Welcome " + name + ", I can tell you all about Smart watches. Ask me a question!");
    } // End Method: startBot

   // -- addListener: add keyListener to TextField: call processing methods -- //
   void addListener(JTextField field) {
       // Add new Keylistener: Anonymous Inner Class
       field.addKeyListener(new KeyListener() {
           @Override
           public void keyTyped(KeyEvent e) {}

           // -- keyPressed: grab pressed key and process input + output -- //
           @Override
           public void keyPressed(KeyEvent e) {
               // Grabbing the ENTER key
               if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                   // Grabbing next in TextField and
                   // creating response String variable for use later
                   String text = inputField.getText();
                   String response;
                   // housekeeping: set TextField to uneditable while
                   // in use and replace text with null
                   inputField.setEditable(false);
                   inputField.setText("");

                   // add text grabbed to running conversation with bot
                   addTextToArea(human + text);

                   // Call processing methods: with processed input as params and
                   // generate response: store in string variables
                   String edited = sp.processInput(text);
                   if(sp.findMatch(edited, sp.expectedInputs)) {
                       response = sp.generateResponse();
                   } else {
                       response = "I don't understand this :-(";
                   }

                   // Add generated response to conversation with bot
                   addTextToArea(bot + response);

               } // End If
           } // End Method: keyPressed

           // -- keyReleased: grab released key and process -- //
           @Override
           public void keyReleased(KeyEvent e) {
               // Once ENTER key released: allow editing of field
               if(e.getKeyCode() == KeyEvent.VK_ENTER) inputField.setEditable(true);
           } // End Method: keyReleased
       }); // End Anonymous Inner Class
   } // End Method: addListener

    // -- addTextToArea: add text to conversation with bot -- //
    public void addTextToArea(String s) { dialogText.setText(dialogText.getText() + s + "\n");}

    // -- main: new anonymous instance of ChatBotV2
    public static void main(String[] args) { new ChatBot(); }

} // End Class: ChatBotV2

// ---- StringProcessor: class to Process text input and generate a response ---- //
class StringProcessor{
    // Array of questions bot can reponse to
     String[][] expectedInputs = {
            // Greetings
            {"hello", "hi", "hey", "hallo", "good morning", "goeie more", "hiya"},
            // Generic Questions
            {"how r you", "how are you", "how r u", "how are ya", "how have you been"},
            {"whats up", "wassup", "sup", "what are you doing", "thats good"},
            // Specific Questions
            {"what is a smart watch",  "what is a smartwatch", "what is android wear", " what is androidwear"},
            {"how many smart watch are there", ""},
            // Goodbye
            {"bye", "good night", "goodnight", "syl", "syt", "fairwell", "have a good one", "see you later"},
            // Thanks
            {"thank you", "thanks", "cheers", "awesome", " u da real mvp"}
    };
    // Array of responses to questions by Human
    private String[][] botResponse = {
            // Greetings
            {"Hello", "Hey there", "Hi", "Sup", "Hiya"},
            // Generic Answers
            {"I'm good", "Good, thank you", "Not bad", "Not too bad"},
            {"Not much", "Waiting for a question, actually", "Bored, actually", "You are boring", "Not a lot"},
            // Specific Answers
            {"A great way of texting your friends in class", "Great way to cheat on a test",
                    "Good means of playing games in glass", "They are awesome, whats what they are", "Cool technology"},
            {"There are 10 smart watches of note"},
            // Goodbye
            {"Goodbye", "Syl", "Totsiens", "Auf Wiedersehen", "See ya"},
            // Thanks
            {"No problem", "No worries", "No issue", "You too"}
    };

    // Position of array (which section of responses to decide from)
    int[] index = {0,0};

    // -- processInput: of text entered by user -- //
    String processInput(String str) {
        // Set input to lowercase and remove all special character (!?. ,)
        str = str.toLowerCase().trim();
        str = str.replaceAll("[!?.,']", "");
        // Return processed string
        return str;
    } // End Method: processInput


    // -- findMatch: of processed input in list of questions -- //
    boolean findMatch(String s, String[][] arr) {
        // Looping through all array items to find a match
        for(int i = 0; i < arr.length; i++) {
            for(int j = 0; j < arr[i].length; j++) {
                if(arr[i][j].equals(s)) {
                    // Return string, and capture index
                    index[0] = i;
                    index[1] = j;
                    return true;
                    } // End IF
                } // End For: j
            } // End For: i
        // Return false if doesn't match
        return false;
    } // End Method: findMatch

    // -- generateResponse: of bot to question/statement after finding match -- //
    String generateResponse() {
        int rand = (int) (Math.random()*(index[0]-1) +1);
        return botResponse[index[0]][rand];
    } // End Method: generateResponse

} // End Class: StringProcessor