package com.chatbot;
/*  Name: ChatBot
*   Authors: Ben White
*            Amit Jassi
*/
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

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
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
            );
    private JLabel inputLabel = new JLabel("Question the bot here:");

    // String constants for headers of Bot and Human(name)
    private final String bot = "TechBot :\t";
    private final String human = "Human :\t";

    // New String Processor for processing input
    private StringProcessor sp = new StringProcessor();

    // -- Constructor: Add title to Frame and run initialize -- //
    public ChatBot() {
        // Set Title of Frame, run the bot using initialize()
        super("Much Chat, Such Bot, Very Smart, Awesome Smart watch");
        initialize();
    } // End Constructor

    // -- initialize: initial output and grab users name -- //
    void initialize() {
        // Using JOP: to start conversation with the user
        JOptionPane.showMessageDialog(null, "Hello!");
        String name = JOptionPane.showInputDialog(null, "What's your name?");
        if(name.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Welcome, lets begin!");
        } else {
            JOptionPane.showMessageDialog(null, "Welcome " + name + ", lets begin!");
        }
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

        // Initial output by Bot to user
        addTextToArea(bot + "Welcome " + name + ", I can tell you all about Smart watches. Ask me a question!");
    } // End Method: initialize

   // -- addListener: add keyListener to TextField: call processing methods -- //
   void addListener(Component c) {
       // Add new Keylistener: Anonymous Inner Class
       c.addKeyListener(new KeyListener() {
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
                   inputField.setText(null);

                   // add text grabbed to running conversation with bot
                   addTextToArea(human + text);

                   // If match is found and String is not empty:
                   // Call processing methods: with processed input as params and
                   // generate response: store in string variables response
                   String edited = sp.processInput(text);
                   if(sp.findMatch(edited, sp.expectedInputs) && !edited.isEmpty()) {
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
               // IF ENTER key released: allow editing of JTextField
               if(e.getKeyCode() == KeyEvent.VK_ENTER) inputField.setEditable(true);
           } // End Method: keyReleased
       }); // End Anonymous Inner Class
   } // End Method: addListener

    // -- addTextToArea: add text to conversation with bot -- //
    public void addTextToArea(String s) { dialogText.append(s + "\n"); }

    // -- main: new anonymous instance of ChatBotV2 -- //
    public static void main(String[] args) { new ChatBot(); }

} // End Class: ChatBotV2

// ---- StringProcessor: class to Process text input and generate a response ---- //
class StringProcessor{
    // Array of questions bot can reponse to
     String[][] expectedInputs = {
            // Greetings
            {"hello", "hi", "hey", "hallo", "good morning", "goeie more", "hiya", "harro", "ello guvner"},
            // Generic Questions
            {"how r you", "how are you", "how r u", "how are ya", "how have you been"},
            {"whats up", "wassup", "sup", "what are you doing"},
            // Specific Questions
            {"what is a smart watch",  "what is a smartwatch", "what is android wear", " what is androidwear"},
            {"how many smart watches are there", "how many are there"},
            {"what are they", "list them", "what are the best ones", ""},
            {"what are the specs", "what are their specs", "why are they so good"},
            // Goodbye
            {"bye", "good night", "goodnight", "syl", "syt", "fairwell", "fair well", "have a good one", "see you later", "goodbye"},
            // Thanks
            {"thank you", "thanks", "cheers", "awesome", "u da real mvp"}
    };
    // Array of responses to questions by Human
    private String[][] botResponse = {
            // Greetings
            {"Hello", "Hey there", "Hi", "Sup", "Hiya", "Harro"},
            // Generic Answers
            {"I'm good", "Good, thank you", "Not bad", "Not too bad"},
            {"Not much", "Waiting for a question, actually", "Bored, actually", "You are boring", "Not a lot"},
            // Specific Answers
            {"A great way of texting your friends in class", "A great way to cheat on a test",
                    "Good means of playing games in glass", "They are awesome, whats what they are", "Cool technology"},
            {"There are 4 smart watches worth mentioning"},
            {"Huawei watch, Moto 360 V2, Asus Zenwatch 3, Apple Watch 2"},
            {"Android Wear watches have a Snapdragon processor, 512mb of RAM and between 300-400 mAh battery"},
            // Goodbye
            {"Goodbye", "Syl", "Totsiens", "Auf Wiedersehen", "See ya"},
            // Thanks
            {"No problem", "No worries", "No issue", "You too"}
    };

    // Position of array (which section of responses to decide from)
    int index = 0;

    // -- processInput: of text entered by user -- //
    String processInput(String str) {
        // Set input to lowercase and remove all special character (!?. ,)
        str = str.toLowerCase().trim().replaceAll("[!?.,'%&*]", "");
        // Return processed string
        return str;
    } // End Method: processInput

    // -- findMatch: of processed input in list of questions -- //
    boolean findMatch(String s, String[][] arr) {
        // Looping through all array items to find a match
        for(int i = 0; i < arr.length; i++) {
            for(int j = 0; j < arr[i].length; j++) {
                if(arr[i][j].equals(s)) {
                    // Return true, and capture index of correct response array
                    index = i;
                    return true;
                    } // End If
                } // End For: j
            } // End For: i
        // Return false if doesn't match
        return false;
    } // End Method: findMatch

    // -- generateRandom: make a new random number from 0 - length of responses array --//
    int generateRandom() {
        Random r = new Random();
        return r.nextInt(botResponse[index].length);
    }// End Method: generateRandom

    // -- generateResponse: of bot to question/statement after finding match -- //
    String generateResponse() {
        int random = generateRandom();
        return botResponse[index][random];
    } // End Method: generateResponse

} // End Class: StringProcessor