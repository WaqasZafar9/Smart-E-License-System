package Admin;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import DisplayInformation.DisplayDrivers;
import addSymbols.AddQuestion;
import addSymbols.UpdateSymbol;
import driverForm.UpdateDriverinfo;
import users.UpdateUser;
import users.Users;

public class AdminPannel {
    private JFrame mainFrame;
    private JPanel mainPanel;
    private JLabel heading;
    private JButton updateUserInfo;
    private JButton addUser;
    private JButton deleteUser;
    private JButton addSign;
    private JButton deleteSign;
    private JButton updateSign;
    private JButton display;
    private JButton displayDriver;
    private JButton logoutButton;
    private JButton addAdmin;

    public AdminPannel(){
        mainFrame=new JFrame();
        iniGUI();
        mainFrame.setSize(784, 449);
        mainFrame.add(mainPanel);

        mainFrame.setVisible(true);
    }

    private void iniGUI() {
        heading = new JLabel ("Admin Portal");
        updateUserInfo = new JButton ("Update User Info");
        addUser = new JButton ("Add User");
        deleteUser = new JButton ("Delete User");
        addSign = new JButton ("Add Sign Question");
        deleteSign = new JButton ("Delete Sign Question");
        updateSign = new JButton ("Update Sign Question");
        display = new JButton ("Display All Users");
        displayDriver = new JButton ("Display Driver Info");
        addAdmin=new JButton("Add Admin");
        logoutButton = new JButton ("Log Out");
        mainPanel=new JPanel();
        mainPanel.setLayout(null);

        Font headingFont = new Font("Arial", Font.BOLD, 28);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setFont(headingFont);

        mainPanel.add (heading);
        mainPanel.add (updateUserInfo);
        mainPanel.add (addUser);
        mainPanel.add (deleteUser);
        mainPanel.add (addSign);
        mainPanel.add (deleteSign);
        mainPanel.add (updateSign);
        mainPanel.add (display);
        mainPanel.add (displayDriver);
        mainPanel.add(addAdmin);
        mainPanel.add (logoutButton);


        heading.setBounds (245, 80, 315, 70);
        updateUserInfo.setBounds (535, 185, 170, 30);
        addUser.setBounds (115, 185, 170, 30);
        deleteUser.setBounds (325, 185, 170, 30);
        addSign.setBounds (115, 235, 170, 30);
        deleteSign.setBounds (325, 235, 170, 30);
        updateSign.setBounds (535, 235, 170, 30);
        display.setBounds (115, 285, 170, 30);
        displayDriver.setBounds (325, 285, 170, 30);
        addAdmin.setBounds(535,285,170,30);
        logoutButton.setBounds (535, 335, 170, 30);

        Border etchedBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);


        TitledBorder titledBorder = BorderFactory.createTitledBorder(etchedBorder, "Admin Portal");
        titledBorder.setTitleColor(Color.BLACK);
        titledBorder.setTitleJustification(TitledBorder.CENTER);
        Font labelFont = new Font("Arial", Font.BOLD, 18);
        titledBorder.setTitleFont(labelFont);

        addUser.addActionListener(buttonListner);
        deleteUser.addActionListener(buttonListner);
        updateUserInfo.addActionListener(buttonListner);
        addSign.addActionListener(buttonListner);
        deleteSign.addActionListener(buttonListner);
        updateSign.addActionListener(buttonListner);
        display.addActionListener(buttonListner);
        displayDriver.addActionListener(buttonListner);
        addAdmin.addActionListener(buttonListner);
        logoutButton.addActionListener(buttonListner);

        mainPanel.setBorder(titledBorder);

    }

    ActionListener buttonListner=new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("Add User")){
                addUserF();

            }
            else if(e.getActionCommand().equals("Delete User")){
                deleteUserF();

            }
            else if(e.getActionCommand().equals("Update User Info")){
                updateUserInfoF();
            }
            else if(e.getActionCommand().equals("Add Sign Question")){
                addSignF();

            }
            else if(e.getActionCommand().equals("Delete Sign Question")){
                deleteSignF();

            }
            else if(e.getActionCommand().equals("Update Sign Question")){
                updateSignF();

            }
            else if(e.getActionCommand().equals("Display All Users")){
                displayUsersF();
            }
            else if(e.getActionCommand().equals("Display Driver Info")){
                displayDriversF();

            }
            else if(e.getActionCommand().equals("Add Admin")){
                mainFrame.dispose();
                new AddAdmin();
            }
            else if(e.getActionCommand().equals("Log Out")){
                mainFrame.dispose();
                new AdminLogin();
            }
        }
    };

    private void displayDriversF() {
        mainFrame.dispose();
        new DisplayDrivers(true);
    }

    private void displayUsersF() {
        mainFrame.dispose();
        new DisplayDrivers(false);
    }

    private void updateSignF() {
        mainFrame.dispose();
        new UpdateSymbol(false);
    }

    private void deleteSignF() {
        mainFrame.dispose();
        new UpdateSymbol(true);
    }

    private void addSignF() {
        mainFrame.dispose();
        new AddQuestion();
    }

    private void updateUserInfoF() {
        mainFrame.dispose();
        new UpdateUser(false);
    }

    private void deleteUserF() {
        mainFrame.dispose();
        new UpdateUser(true);
    }

    private void addUserF() {
        mainFrame.dispose();
        new Users();
    }

    public static void main(String[] args) {
        new AdminPannel();
    }
}
