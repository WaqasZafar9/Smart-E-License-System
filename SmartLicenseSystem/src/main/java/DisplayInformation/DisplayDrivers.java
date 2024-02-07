package DisplayInformation;

import Admin.AdminPannel;
import driverForm.DrivingInfo;
import DataBase.MongoDataBase;
import org.bson.Document;
import org.bson.types.Binary;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

public class DisplayDrivers {
    JFrame mainFrame;
    JPanel mainPanel;
    JButton backButton;
    ArrayList<Document> drivers;
    MongoDataBase q1;
    Boolean isDriverCall;
    public DisplayDrivers(Boolean isDriverCall){
        this.isDriverCall=isDriverCall;
        initGUI();
    }

    private void initGUI() {
        mainFrame=new JFrame();
        mainPanel=new JPanel(new BorderLayout(2,2));


        backButton=new JButton("Back");
        mainPanel.add(backButton,BorderLayout.SOUTH);
        if(isDriverCall) {
            mainPanel.add(addScrollBarDrivers(), BorderLayout.CENTER);
        }
        else{
            mainPanel.add(addScrollBarUsers(), BorderLayout.CENTER);
        }
        Border etchedBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);



        TitledBorder titledBorder = BorderFactory.createTitledBorder(etchedBorder, "Admin Portal");
        titledBorder.setTitleColor(Color.BLACK);
        titledBorder.setTitleJustification(TitledBorder.CENTER);
        Font labelFont = new Font("Arial", Font.BOLD, 18);
        titledBorder.setTitleFont(labelFont);
        mainPanel.setBorder(titledBorder);


        mainFrame.setSize(1500, 700);
        mainFrame.add(mainPanel);
        mainFrame.setTitle("Drivers Data");

        mainFrame.setVisible(true);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose();
                new AdminPannel();
            }
        });
    }

    private JScrollPane addScrollBarUsers() {

        q1= new MongoDataBase("Driving_Center", "usersInfo");

        drivers = new ArrayList<>();
        drivers = q1.fetchAllDocuments();
        DefaultTableModel model=new DefaultTableModel();
        JTable table=new JTable(model);
        JScrollPane scrollBar=new JScrollPane(table);

        scrollBar.setBounds(100,250,750,300);
        model.addColumn("UserID");
        model.addColumn("Name");
        model.addColumn("Cnic");
        model.addColumn("Father Name");
        model.addColumn("Father Cnic");
        model.addColumn("Date of Birth");
        model.addColumn("Age");
        model.addColumn("Phone No");
        model.addColumn("City");
        model.addColumn("Blood Group");




        for (Document d:drivers) {
            Vector<String> row = new Vector<>();
            row.add(String.valueOf(d.get("userID")));
            row.add(String.valueOf(d.get("Name")));
            row.add(String.valueOf(d.get("Cnic")));
            row.add(String.valueOf(d.get("Father Name")));
            row.add(String.valueOf(d.get("Father Cnic")));
            row.add(String.valueOf(d.get("Date of Birth")));
            row.add(DrivingInfo.calculateAge(d.getString("Date of Birth")));
            row.add(String.valueOf(d.get("Phone No")));
            row.add(String.valueOf(d.get("city")));
            row.add(String.valueOf(d.get("Blood Group")));

            model.addRow(row);
        }


        return scrollBar;
    }

    private JScrollPane addScrollBarDrivers() {

            q1 = new MongoDataBase("Driving_Center", "Drivers");

        drivers = new ArrayList<>();
        drivers = q1.fetchAllDocuments();
        DefaultTableModel model=new DefaultTableModel();
        JTable table=new JTable(model);
        JScrollPane scrollBar=new JScrollPane(table);

        scrollBar.setBounds(100,250,750,300);
        model.addColumn("Learner No");
        model.addColumn("Name");
        model.addColumn("Cnic");
        model.addColumn("Father Name");
        model.addColumn("Father Cnic");
        model.addColumn("Date of Birth");
        model.addColumn("Age");
        model.addColumn("Phone No");
        model.addColumn("City");
        model.addColumn("Blood Group");
        model.addColumn("PIC");




        for (Document d:drivers) {
            Vector<Object> row = new Vector<>();
            row.add(String.valueOf(d.get("LearnerNo")));
            row.add(String.valueOf(d.get("Name")));
            row.add(String.valueOf(d.get("Cnic")));
            row.add(String.valueOf(d.get("Father Name")));
            row.add(String.valueOf(d.get("Father Cnic")));
            row.add(String.valueOf(d.get("Date of Birth")));
            row.add(DrivingInfo.calculateAge(d.getString("Date of Birth")));
            row.add(String.valueOf(d.get("Phone No")));
            row.add(String.valueOf(d.get("city")));
            row.add(String.valueOf(d.get("Blood Group")));
            JLabel j1=new JLabel("");
            j1.setIcon(new ImageIcon(MongoDataBase.fetchImage((Binary) d.get("Image"))));

            row.add(j1);

            model.addRow(row);
        }


        return scrollBar;
    }

    public static void main(String[] args) {
        new DisplayDrivers(true);
    }

}
