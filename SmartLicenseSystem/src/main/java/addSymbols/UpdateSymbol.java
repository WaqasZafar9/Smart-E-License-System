package addSymbols;

import Admin.AdminPannel;
import DataBase.MongoDataBase;
import org.bson.Document;
import org.bson.types.Binary;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UpdateSymbol implements Runnable{

    JFrame mainFrame;
    JLabel questionIDInputLabel;
    JButton retrieveSymbol;
    JTextField questionIDInputText;
    JPanel questionPanel;
    JLabel questionLabel;
    JLabel symbolLabel;
    JTextField questionText;
    JTextField[] optionText;
    JButton submitButton;
    JButton addSymbol;
    JRadioButton[] correctOption;
    ButtonGroup correct;
    String correctAnswer;
    String selectedFilePath;
    JLabel heading;
    JLabel correctOptionLabel;
    JLabel correctOptionText;
    JLabel questionID;
    JButton delete;
    boolean isImageUpdate;
    boolean isDeleteCall;
    JButton backButton;
    MongoDataBase admin;
    public UpdateSymbol(boolean isDeleteCall) {
        this.isDeleteCall=isDeleteCall;
        Thread t1=new Thread(this);
        t1.start();
    }
    @Override
    public void run() {
        createConnection();
        initGUI();
    }

    private void createConnection() {
        admin = new MongoDataBase("Driving_Center", "symbolTest");

    }

    void initGUI() {

        mainFrame = new JFrame();
        mainFrame.setTitle("Driving License");
        questionPanel = new JPanel(null);

        Border etchedBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);


        TitledBorder titledBorder = BorderFactory.createTitledBorder(etchedBorder, "Driving License Admin LOGIN");
        titledBorder.setTitleColor(Color.BLACK);
        Font labelFont = new Font("Arial", Font.BOLD, 18);
        titledBorder.setTitleJustification(TitledBorder.CENTER);
        titledBorder.setTitleFont(labelFont);

        questionPanel.setBorder(titledBorder);

        Font f2 = new Font("Arial", Font.BOLD, 14);

        heading = new JLabel("Question Addition for Test");
        heading.setFont(labelFont);
        questionPanel.add(heading);

        questionLabel = new JLabel("Question :-");
        questionLabel.setFont(f2);
        questionText = new JTextField(10);
        questionPanel.add(questionLabel);
        questionPanel.add(questionText);

        questionIDInputLabel=new JLabel("Enter Question ID : ");
        questionIDInputLabel.setFont(f2);
        questionPanel.add(questionIDInputLabel);

        questionIDInputText=new JTextField();
        questionPanel.add(questionIDInputText);

        retrieveSymbol=new JButton("Retrieve");
        questionPanel.add(retrieveSymbol);

        isImageUpdate=false;

        questionID = new JLabel("  Question ID : -- " );
        questionID.setFont(f2);
        questionID.setBorder(new LineBorder(Color.gray, 2, true));
        questionPanel.add(questionID);

        symbolLabel = new JLabel("");
        questionPanel.add(symbolLabel);
        optionText = new JTextField[5];
        correctOption = new JRadioButton[4];
        correct = new ButtonGroup();
        correctOptionLabel = new JLabel("Correct option");
        correctOptionLabel.setFont(f2);
        correctOptionText = new JLabel("---");
        correctOptionText.setFont(f2);
        questionPanel.add(correctOptionLabel);
        questionPanel.add(correctOptionText);
        addOptions();

        correctAnswer = "";

        backButton=new JButton("Back");
        backButton.setBorder(new LineBorder(Color.gray, 2, true));
        questionPanel.add(backButton);

        addSymbol = new JButton("Upload Symbol");
        addSymbol.setBorder(new LineBorder(Color.gray, 2, true));
        questionPanel.add(addSymbol);
        submitButton = new JButton("Update");
        submitButton.setBorder(new LineBorder(Color.gray, 2, true));
        questionPanel.add(submitButton);

         delete= new JButton("Delete");
        delete.setBorder(new LineBorder(Color.gray, 2, true));
        questionPanel.add(delete);

        setPositions();
        setEnable(false);

        mainFrame.add(questionPanel);
        mainFrame.setVisible(true);
        mainFrame.setSize(790, 480);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (!isFormValid()) {
                    return;
                }
                MongoDataBase admin = new MongoDataBase("Driving_Center", "symbolTest");
                Map<String, Object> documentMap = new HashMap<>();
                try {
                    Document newQuestion;
                    if(!isImageUpdate) {
                        newQuestion = new Document("Question", questionText.getText())
                                .append("questionID", Integer.parseInt(questionIDInputText.getText().trim()))
                                .append("Option1", optionText[0].getText())
                                .append("Option2", optionText[1].getText())
                                .append("Option3", optionText[2].getText())
                                .append("Option4", optionText[3].getText())
                                .append("Correct", correctAnswer);
                    }
                    else {
                        newQuestion = new Document("Question", questionText.getText())
                                .append("questionID", Integer.parseInt(questionIDInputText.getText().trim()))
                                .append("Option1", optionText[0].getText())
                                .append("Option2", optionText[1].getText())
                                .append("Option3", optionText[2].getText())
                                .append("Option4", optionText[3].getText())
                                .append("Symbol", MongoDataBase.storeImage(selectedFilePath))
                                .append("Correct", correctAnswer);
                    }
                    if (admin.updateSymbol(newQuestion,isImageUpdate)) {
                        JOptionPane.showMessageDialog(mainFrame, "Symbol Updated Successfully Against ID : " + questionIDInputText.getText().trim());
                    } else {
                        JOptionPane.showMessageDialog(mainFrame, "Symbol Not Updated Successfully");
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose();
                new AdminPannel();
            }
        });

        retrieveSymbol.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!isNumeric(questionIDInputText.getText())){
                    JOptionPane.showMessageDialog(mainFrame,"Please Enter Question ID","No Input Enter",JOptionPane.ERROR_MESSAGE);
                }
                else {
                    try {
                        Document symbolData = admin.searchDocument("questionID", Integer.parseInt(questionIDInputText.getText()));
                        questionText.setText(symbolData.getString("Question"));
                        optionText[0].setText(symbolData.getString("Option1"));
                        optionText[1].setText(symbolData.getString("Option2"));
                        optionText[2].setText(symbolData.getString("Option3"));
                        optionText[3].setText(symbolData.getString("Option4"));
                        questionID.setText("Question Id : "+ Integer.parseInt(questionIDInputText.getText()));
                        byte[] imageData = MongoDataBase.fetchImage(symbolData.get("Symbol", Binary.class));
                        ImageIcon imageIcon = new ImageIcon(imageData);
                        Image scaledImage = imageIcon.getImage().getScaledInstance(190, 165, Image.SCALE_SMOOTH);
                        ImageIcon scaledImageIcon = new ImageIcon(scaledImage);
                        symbolLabel.setBorder(new LineBorder(Color.gray, 2, true));
                        symbolLabel.setIcon(scaledImageIcon);

                        correctAnswer = symbolData.getString("Correct");
                        correctOptionText.setText(correctAnswer);
                        if (correctAnswer.equals(optionText[0].getText())) {
                            correctOption[0].setSelected(true);
                        } else if (correctAnswer.equals(optionText[1].getText())) {
                            correctOption[1].setSelected(true);
                        } else if (correctAnswer.equals(optionText[2].getText())) {
                            correctOption[2].setSelected(true);
                        } else if (correctAnswer.equals(optionText[3].getText())) {
                            correctOption[3].setSelected(true);
                        }
                        setEnable(!isDeleteCall);
                    }
                    catch (Exception ex){
                        System.out.println(ex.getStackTrace());
                        JOptionPane.showMessageDialog(mainFrame,"No Data Found! Wrong ID","No Smbol Founded",JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(admin.deleteDocument("questionID",Integer.parseInt(questionIDInputText.getText()))){
                    JOptionPane.showMessageDialog(mainFrame,"Document Deleted Successfully");
                }
                else{
                    JOptionPane.showMessageDialog(mainFrame,"Error While Deleting Symbol");
                }
            }
        });

        addSymbol.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();

                FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "png", "jpg");
                fileChooser.setFileFilter(filter);

                int result = fileChooser.showOpenDialog(mainFrame);


                if (result == JFileChooser.APPROVE_OPTION) {
                    // User selected a file
                    selectedFilePath = fileChooser.getSelectedFile().getAbsolutePath();

                    if (isImageFile(selectedFilePath)) {
                        try {
                            byte[] imageData = MongoDataBase.storeImage(selectedFilePath);
                            ImageIcon imageIcon = new ImageIcon(imageData);
                            Image scaledImage = imageIcon.getImage().getScaledInstance(190, 165, Image.SCALE_SMOOTH);
                            ImageIcon scaledImageIcon = new ImageIcon(scaledImage);
                            symbolLabel.setBorder(new LineBorder(Color.gray, 2, true));
                            symbolLabel.setIcon(scaledImageIcon);
                            isImageUpdate=true;

                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    } else {
                        JOptionPane.showMessageDialog(mainFrame, "Please select a valid image file (png or jpg).");
                    }
                }
            }
        });

    }

    private void setEnable(boolean is) {
            for (int i=0;i<4;i++) {
                optionText[i].setEnabled(is);
                correctOption[i].setEnabled(is);
            }
            questionText.setEnabled(is);
            addSymbol.setEnabled(is);
            if(!isDeleteCall) {
                submitButton.setEnabled(is);
                delete.setVisible(false);
            }
            else {
                delete.setEnabled(is);
                submitButton.setVisible(false);
            }
    }

    private boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }

        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void setPositions() {

        addSymbol.setBounds(575, 325, 170, 30);
        submitButton.setBounds (370, 385, 180, 40);
        delete.setBounds (130, 385, 180, 40);
        backButton.setBounds(575,375,140,35);
        correctOption[0].setBounds(105, 195, 170, 30);
        correctOption[1].setBounds(105, 245, 170, 30);
        correctOption[2].setBounds(105, 290, 170, 30);
        correctOption[3].setBounds(105, 335, 170, 30);
        optionText[0].setBounds(335, 195, 170, 30);
        optionText[1].setBounds(335, 245, 170, 30);
        optionText[2].setBounds(335, 290, 170, 30);
        optionText[3].setBounds(335, 335, 170, 30);
        questionLabel.setBounds(105, 105, 133, 30);
        questionText.setBounds(260, 105, 300, 30);
        symbolLabel.setBounds(565, 155, 190, 165);
        heading.setBounds(270, 30, 350, 50);
        correctOptionLabel.setBounds(130, 150, 130, 25);
        correctOptionText.setBorder(new LineBorder(Color.gray, 2, true));
        correctOptionText.setHorizontalAlignment(SwingConstants.CENTER);
        correctOptionText.setBounds(322, 145, 195, 30);
        questionID.setBounds(110, 40, 130, 30);
        questionIDInputLabel.setBounds(110,70,150, 30);
        questionIDInputText.setBounds(270,70,100, 25);
        retrieveSymbol.setBounds(400,70,90,25);


    }

    private boolean isFormValid() {
        if (questionText.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "Please fill out Question Description.");
            return false;
        }
        for (int i = 0; i < 4; i++) {
            if (optionText[i].getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(mainFrame, "Please fill out Option " + (i + 1) + ".");
                return false;
            }
        }

        if (symbolLabel.getIcon() == null) {
            JOptionPane.showMessageDialog(mainFrame, "Please Add Symbol Picture.");
            return false;
        }
        if (correctAnswer == null || correctAnswer.trim().isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "Please Select Correct Answer of the Question.");
            return false;
        }

        return true;
    }

    private static boolean isImageFile(String filePath) {
        String lowercaseFilePath = filePath.toLowerCase();
        return lowercaseFilePath.endsWith(".png") || lowercaseFilePath.endsWith(".jpg");
    }

    void addOptions() {
        for (int i = 0; i < 4; i++) {
            optionText[i] = new JTextField(20);
            correctOption[i] = new JRadioButton("Option " + (i + 1));
            Font f2 = new Font("Arial", Font.BOLD, 13);
            correctOption[i].addActionListener(buttonListener);
            correctOption[i].setFont(f2);
            ;
            questionPanel.add(correctOption[i]);
            questionPanel.add(optionText[i]);
            correct.add(correctOption[i]);
        }


    }

    ActionListener buttonListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String actionCommand = e.getActionCommand();
            System.out.println("Action Command: " + actionCommand);
            correctAnswer = actionCommand;

            switch (actionCommand) {
                case "Option 1":
                    correctAnswer = optionText[0].getText();
                    break;
                case "Option 2":
                    correctAnswer = optionText[1].getText();
                    break;
                case "Option 3":
                    correctAnswer = optionText[2].getText();
                    break;
                case "Option 4":
                    correctAnswer = optionText[3].getText();
                    break;
            }
            correctOptionText.setText(correctAnswer);
            System.out.println(correctAnswer);
        }
    };



}
