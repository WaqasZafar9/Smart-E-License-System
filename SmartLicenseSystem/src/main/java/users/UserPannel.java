package users;

import Admin.AdminLogin;
import Symboltest.SymbolTest;
import driverForm.DrivingInfo;
import driverForm.UpdateDriverinfo;
import licenseTestForm.LicenseTestForm;
import licenseTestForm.UpdateLicense;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserPannel {
    private JLabel heading;
    private JFrame mainFrame;
    private JPanel mainPanel;
    private JButton licenseTestForm;
    private JButton addDriverInfo;
    private JButton symbolTest;
    private JButton updateLicense;
    private JButton updateDriver;
    private JButton logout;
    public UserPannel(){
        initGUI();
    }

    private void initGUI() {

        mainFrame=new JFrame();
        mainFrame.setSize(810, 449);

        mainPanel=new JPanel();
        mainPanel.setLayout (null);

        heading = new JLabel ("User Portal");
        licenseTestForm = new JButton ("License Test Form");
        addDriverInfo = new JButton ("Add Driver Info");
        symbolTest = new JButton ("Symbol Test");
        updateDriver = new JButton ("Update Driver Data");
        updateLicense=new JButton("Update License");
        logout = new JButton ("Log Out");

        mainPanel.add (heading);
        mainPanel.add (licenseTestForm);
        mainPanel.add (addDriverInfo);
        mainPanel.add (symbolTest);
        mainPanel.add (updateDriver);
        mainPanel.add(updateLicense);
        mainPanel.add (logout);

        heading.setBounds (245, 80, 315, 70);
        licenseTestForm.setBounds (535, 185, 180, 50);
        addDriverInfo.setBounds (115, 185, 180, 50);
        symbolTest.setBounds (325, 185, 185, 50);
        updateDriver.setBounds (115, 275, 180, 50);
        updateLicense.setBounds(325,275,180,50);
        logout.setBounds (535, 275, 180, 50);

        Font headingFont = new Font("Arial", Font.BOLD, 28);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setFont(headingFont);

        Border etchedBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);


        TitledBorder titledBorder = BorderFactory.createTitledBorder(etchedBorder, "User Portal");
        titledBorder.setTitleColor(Color.BLACK);
        titledBorder.setTitleJustification(TitledBorder.CENTER);
        Font labelFont = new Font("Arial", Font.BOLD, 18);
        titledBorder.setTitleFont(labelFont);

        mainPanel.setBorder(titledBorder);



        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);

        addActionListner();
    }

    private void addActionListner() {

        addDriverInfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose();
                new DrivingInfo();
            }
        });
        symbolTest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose();
                new SymbolTest();
            }
        });
        licenseTestForm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose();
                new LicenseTestForm();
            }
        });

        updateDriver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose();
                new UpdateDriverinfo(false);
            }
        });

        updateLicense.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose();
                new UpdateLicense();
            }
        });
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose();
                new AdminLogin();
            }
        });
    }

    public static void main(String[] args) {
        new UserPannel();
    }
}
