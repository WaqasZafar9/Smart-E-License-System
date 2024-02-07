package Admin;

import DataBase.MongoDataBase;
import org.bson.Document;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class AddAdmin {

    private JFrame mainFrame;
    private JPanel mainPanel;
    private JLabel userNameLabel;
    private JLabel passwordLabel;
    private JLabel confirmPasswordLabel;
    private JTextField userNameIput;
    private JTextField passwordInput;
    private JTextField passwordConfirmInput;
    private JButton submitButton;
    private JButton backButton;
    private MongoDataBase adminConnection;
    public AddAdmin() {



        mainFrame=new JFrame("Admin");
        mainPanel=new JPanel(null);
        mainFrame.setSize(784, 449);

        initGUI();
        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);

        adminConnection=new MongoDataBase("Driving_Center","AdminInfo");
    }

    private void initGUI() {

        userNameLabel = new JLabel ("User Name");
        passwordLabel = new JLabel ("Password");
        confirmPasswordLabel = new JLabel ("Confirm Password");
        userNameIput = new JTextField (5);
        passwordInput = new JTextField (5);
        passwordConfirmInput = new JTextField (5);
        submitButton = new JButton ("Submit");
        backButton=new JButton("Back");

        mainPanel.add (userNameLabel);
        mainPanel.add (passwordLabel);
        mainPanel.add (confirmPasswordLabel);
        mainPanel.add (userNameIput);
        mainPanel.add (passwordInput);
        mainPanel.add (passwordConfirmInput);
        mainPanel.add (submitButton);
        mainPanel.add(backButton);


        userNameLabel.setBounds (190, 100, 100, 25);
        passwordLabel.setBounds (190, 140, 115, 25);
        confirmPasswordLabel.setBounds (190, 180, 130, 25);
        userNameIput.setBounds (325, 100, 130, 30);
        passwordInput.setBounds (325, 140, 130, 30);
        passwordConfirmInput.setBounds (325, 180, 130, 30);
        backButton.setBounds(400,240,130,30);
        submitButton.setBounds (260, 240, 120, 30);


        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (userNameIput.getText().isEmpty() ||passwordConfirmInput.getText().isEmpty() ||passwordInput.getText().isEmpty() ){
                    JOptionPane.showMessageDialog(mainFrame,"PLease Fill All Details");
                    return;
                }
                else if(!passwordInput.getText().equals(passwordConfirmInput.getText())){
                    JOptionPane.showMessageDialog(mainFrame, "Password and Confirm Password are not Same", "Form Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else{
                    if(!checkUserID()){
                        saveData();
                        JOptionPane.showMessageDialog(mainFrame, "User added Successfully", "User Added", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else{
                        JOptionPane.showMessageDialog(mainFrame, "User ID Already Exists Please Enter another user ID", "User ID Exists", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                }
            }

            private void saveData() {
                Map<String, Object> documentMap = new HashMap<>();
                documentMap.put("Name", userNameIput.getText());
                documentMap.put("password", passwordInput.getText());
                adminConnection.createDocument(documentMap);
            }

            private boolean checkUserID() {
                Document temp=adminConnection.searchDocument("Name",userNameIput.getText());

                return temp != null;
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose();
                new AdminPannel();
            }
        });
    }


    public static void main(String[] args) {
        new AddAdmin();
    }


}
