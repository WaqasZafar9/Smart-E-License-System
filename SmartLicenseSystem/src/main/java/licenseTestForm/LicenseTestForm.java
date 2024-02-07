    package licenseTestForm;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import java.awt.print.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import com.infobip.sms.SendSMS;
import DataBase.MongoDataBase;
import org.bson.Document;
import org.bson.types.Binary;
import users.UserPannel;

public class LicenseTestForm implements Runnable {

    private JFrame mainFrame;

    private JTextField textField1;
    private JButton detailsFetchButton;
    JButton print;
    private JLabel name;
    private JLabel fatherName;
    private JLabel cnic;
    private JLabel fatherCnic;
    private JLabel fatherCniclabel;
    private JLabel dateofBirth;
    private JLabel age;
    private JLabel ageLabel;
    private JLabel phoneNo;
    private JLabel bloodGroup;
    private JLabel dateOfIssue;
    private JLabel dateOfExpiry;
    private JLabel dateOfIssueLabel;
    private JLabel dateOfExpiryLabel;
    private JRadioButton drivingPassCheckBox;
    private JRadioButton drivingFailCheckBox;
    private JTextArea remarksTextArea;
    JPanel licenseTestForm;
    private JPanel infoPanel;
    private Boolean isSybolPass=false;
    private JLabel symbolResultLabel;
    private JPanel testDetailPanel;
    private JButton submitButton;
    private JLabel learnerInput;
    private JLabel nameLabel;
    private JLabel cnicLabel;
    private JLabel type;
    private JLabel fatherNameLabel;
    private JLabel phoneNoLabel;
    private JLabel bloodGroupLabel;
    private JLabel typeLabel;
    private JLabel reamainingValidity;
    private JLabel reamainingValidityLabel;
    private JLabel symbolTest;
    private JLabel drivingTest;
    private JLabel reamarks;
    private JLabel picture;
    private JLabel dateOfBirthLabel;
    private JLabel testHeading;
    private JButton checkSymbolTest;
    private JSeparator separator;
    private boolean isRetrieved;
    private MongoDataBase conncetionUsers;
    private MongoDataBase connectionSaveResult;
    private ButtonGroup buttonGroupSymbol = new ButtonGroup();
    private JButton backButton;
    private ButtonGroup buttonGroupDriving = new ButtonGroup();
    public LicenseTestForm() {
        Thread t1 = new Thread(this);
        t1.run();

    }
    void createConnection(){
        connectionSaveResult= new MongoDataBase("Driving_Center", "Test_Results");
        conncetionUsers=new MongoDataBase("Driving_Center", "Drivers");
    }
    @Override
    public void run() {
        initGUI();
        createConnection();
    }

    void initGUI() {
        mainFrame=new JFrame();
        mainFrame.setTitle("License Test Form");

        licenseTestForm=new JPanel();
        licenseTestForm.setLayout(null);

        addInfoPanel();


        separator=new JSeparator();
        licenseTestForm.add(separator);

        testDetail();

        setlayout();

        mainFrame.setSize(868, 620);
        mainFrame.add(licenseTestForm);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        licenseTestForm.setVisible(true);

        mainFrame.setVisible(true);

        addActionListeners();
    }

    private void setlayout() {

        Border etchedBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);


        TitledBorder titledBorder = BorderFactory.createTitledBorder(etchedBorder, "Driving License Test Form");
        titledBorder.setTitleColor(Color.BLACK);
        Font labelFont = new Font("Arial", Font.BOLD, 18);
        titledBorder.setTitleJustification(TitledBorder.CENTER);
        titledBorder.setTitleFont(labelFont);

        licenseTestForm.setBorder(titledBorder);


        detailsFetchButton.setBounds (447, 60, 170, 30);
        learnerInput.setBounds (80, 60, 140, 30);
        textField1.setBounds (240, 60, 170, 30);
        name.setBounds (80, 100, 130, 30);
        nameLabel.setBounds (240, 100, 130, 30);
        cnic.setBounds (400, 100, 90, 30);
        cnicLabel.setBounds (535, 100, 130, 30);
        fatherName.setBounds (80, 130, 130, 30);
        fatherNameLabel.setBounds (240, 130, 130, 30);
        dateOfBirthLabel.setBounds (240, 160, 130, 25);
        age.setBounds (400, 160, 72, 25);
        fatherCnic.setBounds (400, 130, 97, 30);
        dateofBirth.setBounds (80, 160, 130, 30);
        phoneNoLabel.setBounds (240, 190, 130, 30);
        bloodGroup.setBounds (400, 190, 90, 30);
        phoneNo.setBounds (80, 190, 130, 30);
        type.setBounds (80, 220, 130, 30);
        typeLabel.setBounds (240, 220, 130, 30);
        separator.setBounds (70, 305, 610, 35);
        dateOfIssue.setBounds (400, 220, 89, 30);
        bloodGroupLabel.setBounds (535, 190, 130, 30);
        dateOfExpiry.setBounds (80, 250, 130, 30);
        dateOfIssueLabel.setBounds (535, 220, 130, 30);
        dateOfExpiryLabel.setBounds (240, 250, 130, 30);
        ageLabel.setBounds (535, 160, 92, 30);
        reamainingValidity.setBounds (400, 250, 130, 30);
        fatherCniclabel.setBounds (535, 130, 130, 30);
        reamainingValidityLabel.setBounds (535, 250, 130, 30);
        symbolTest.setBounds (200, 375, 130, 30);
        drivingTest.setBounds (200, 410, 130, 30);
        checkSymbolTest.setBounds(325, 375, 115, 30);
        symbolResultLabel.setBounds(510, 375, 115, 30);
//        symbolPassCheckBox.setBounds (325, 375, 115, 30);
//        symbolFailCheckBox.setBounds (450, 375, 115, 30);
        drivingPassCheckBox.setBounds (325, 410, 115, 30);
        drivingFailCheckBox.setBounds (450, 410, 115, 30);
        reamarks.setBounds (200, 460, 94, 30);
        remarksTextArea.setBounds (310, 465, 275, 55);
        picture.setBounds (680, 55, 175, 160);
        submitButton.setBounds (440, 530, 160, 30);
        print.setBounds (260, 530, 160, 30);
        testHeading.setBounds (285, 325, 170, 30);
        backButton.setBounds(120,530,120,30);

    }

    public static String calculateExpiryDuration( String dateOfExpiry){

        LocalDate currentDate = LocalDate.now();
        LocalDate expiryDate = LocalDate.parse(dateOfExpiry);

        Period expiryDuration = Period.between(currentDate, expiryDate);;

        return String.valueOf(expiryDuration.getYears()*12*30 + expiryDuration.getMonths() * 30 + expiryDuration.getDays());
    }

    public static String  calculateAge(String dateOfBirth) {
        LocalDate birthDate = LocalDate.parse(dateOfBirth);

        LocalDate currentDate = LocalDate.now();

        Period period = Period.between(birthDate, currentDate);

        return String.valueOf(period.getYears());
    }
     ImageIcon  addImage(byte[] imageData){
        JLabel pic=new JLabel();
        pic.setText("");
        ImageIcon imageIcon = new ImageIcon(imageData);
        Image image = imageIcon.getImage();
        Image scaledImage = image.getScaledInstance(130, 120, Image.SCALE_SMOOTH);
        ImageIcon scaledImageIcon = new ImageIcon(scaledImage);
        return scaledImageIcon;
    }

    void addInfoPanel(){

        learnerInput=new JLabel("Learner No");
        licenseTestForm.add(learnerInput);

        textField1=new JTextField();

        licenseTestForm.add(textField1);


        licenseTestForm.add(new JLabel());

        detailsFetchButton =new JButton("Retrieve");
        licenseTestForm.add(detailsFetchButton);

        picture=new JLabel("");
        picture.setSize(300,200);
        licenseTestForm.add(picture);

        isRetrieved=false;

        name=new JLabel("Name : ");
        licenseTestForm.add(name);

        nameLabel=new JLabel("---");
        licenseTestForm.add(nameLabel);

        cnic=new JLabel("Cnic No :");
        licenseTestForm.add(cnic);

        cnicLabel=new JLabel("---");
        licenseTestForm.add(cnicLabel);

        fatherName=new JLabel("Father Name");
        licenseTestForm.add(fatherName);

        fatherNameLabel=new JLabel("---");
        licenseTestForm.add(fatherNameLabel);

        fatherCnic=new JLabel("Father CNIC No :");
        licenseTestForm.add(fatherCnic);

        fatherCniclabel=new JLabel("---");
        licenseTestForm.add(fatherCniclabel);

        dateofBirth=new JLabel("Date of Birth");
        licenseTestForm.add(dateofBirth);

        dateOfBirthLabel=new JLabel("00-00-0000");
        licenseTestForm.add(dateOfBirthLabel);

        age=new JLabel("AGE");
        licenseTestForm.add(age);

        ageLabel=new JLabel("0");
        licenseTestForm.add(ageLabel);

        phoneNo=new JLabel("Phone No : ");
        licenseTestForm.add(phoneNo);

        phoneNoLabel=new JLabel("0000-0000000");
        licenseTestForm.add(phoneNoLabel);

        bloodGroup=new JLabel("Blood Group : ");
        licenseTestForm.add(bloodGroup);

        bloodGroupLabel=new JLabel("--");
        licenseTestForm.add(bloodGroupLabel);

        type=new JLabel("Type");
        licenseTestForm.add(type);

        typeLabel=new JLabel("---");
        licenseTestForm.add(typeLabel);

        dateOfIssue=new JLabel("Date of Issue : ");
        licenseTestForm.add(dateOfIssue);

        dateOfIssueLabel=new JLabel("0-0-0000");
        licenseTestForm.add(dateOfIssueLabel);

        dateOfExpiry=new JLabel("Date of Expiry : ");
        licenseTestForm.add(dateOfExpiry);

        dateOfExpiryLabel=new JLabel("0-0-0000");
        licenseTestForm.add(dateOfExpiryLabel);

        reamainingValidity=new JLabel("Validity Remaining : ");
        licenseTestForm.add(reamainingValidity);

        reamainingValidityLabel=new JLabel("0 Days");
        licenseTestForm.add(reamainingValidityLabel);

    }


    void testDetail(){


        symbolTest =new JLabel("Symbol Test : ");
        licenseTestForm.add(symbolTest);

        testHeading=new JLabel("Test Details");
        Font labelFont = new Font("Arial", Font.BOLD, 24);
        testHeading.setFont(labelFont);
        licenseTestForm.add(testHeading);

        licenseTestForm.add(new JLabel());


        symbolResultLabel=new JLabel("");
        licenseTestForm.add(symbolResultLabel);

        checkSymbolTest=new JButton("Check");
        licenseTestForm.add(checkSymbolTest);

        drivingTest=new JLabel("Driving Test : ");
        licenseTestForm.add(drivingTest);

        licenseTestForm.add(new JLabel());

        buttonGroupDriving = new ButtonGroup();

        drivingPassCheckBox=new JRadioButton("Pass");
        buttonGroupDriving.add(drivingPassCheckBox);
        licenseTestForm.add(drivingPassCheckBox);

        drivingFailCheckBox=new JRadioButton("Fail");
        buttonGroupDriving.add(drivingFailCheckBox);
        licenseTestForm.add(drivingFailCheckBox);



        reamarks =new JLabel("Remarks");
        licenseTestForm.add(reamarks);

        remarksTextArea=new JTextArea();
        licenseTestForm.add(remarksTextArea);

        licenseTestForm.add(new JLabel());
        licenseTestForm.add(new JLabel());
        licenseTestForm.add(new JLabel());
        licenseTestForm.add(new JLabel());

        backButton=new JButton("Back");
        backButton.setBorder(new LineBorder(Color.gray, 2, true));
        licenseTestForm.add(backButton);

        submitButton=new JButton("Submit");
        licenseTestForm.add(submitButton);

        print=new JButton("Print");
        licenseTestForm.add(print);

        submitButton.setEnabled(false);

        drivingPassCheckBox.setEnabled(false);
        drivingFailCheckBox.setEnabled(false);
        checkSymbolTest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    MongoDataBase temp = new MongoDataBase("Driving_Center", "signTestResult");
                    Document d = temp.readDocument("cnic", cnicLabel.getText());
                    int marks = d.getInteger("TestMarks");
                    if (marks > 4) {
                        symbolResultLabel.setText("PASS");
                        drivingPassCheckBox.setEnabled(true);
                        drivingFailCheckBox.setEnabled(true);
                        isSybolPass=true;
                    } else {
                        symbolResultLabel.setText("Fail");
                        buttonGroupDriving.setSelected(drivingFailCheckBox.getModel(),false);
                        isSybolPass=false;
                    }
                    submitButton.setEnabled(true);
                }
                catch (Exception ex){
                    JOptionPane.showMessageDialog(mainFrame,"Please Take Symbol Test First","Test Not Valid",JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });


    }

    void addActionListeners() {
        ActionListener buttonListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                if(e.getActionCommand().equals("Retrieve")){

                    if(textField1.getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(mainFrame,"Please Enter Learner No","Learner No not Provide",JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                        try {

                            Document userFetchData = conncetionUsers.readDocument("LearnerNo", textField1.getText().trim());

                            try {
                                MongoDataBase temp = new MongoDataBase("Driving_Center", "Licenses");
                                Document data = temp.readDocument("Cnic", userFetchData.getString("Cnic"));
                                if (!data.isEmpty() && data.getString("Type").equals(userFetchData.getString("Type"))) {
                                    JOptionPane.showMessageDialog(mainFrame, "Regular Driving License Already Exists", "License Exists", JOptionPane.INFORMATION_MESSAGE);
                                    return;
                                }
                            }
                            catch (Exception ignored){
                            }

                            nameLabel.setText(userFetchData.getString("Name"));
                            cnicLabel.setText(userFetchData.getString("Cnic"));
                            fatherNameLabel.setText(userFetchData.getString("Father Name"));
                            fatherCniclabel.setText(userFetchData.getString("Father Cnic"));
                            dateOfBirthLabel.setText(userFetchData.getString("Date of Birth"));
                            dateOfIssueLabel.setText(userFetchData.getString("Date of Issue"));
                            dateOfExpiryLabel.setText(userFetchData.getString("Date of Expiry"));
                            phoneNoLabel.setText(userFetchData.getString("Phone No"));
                            ageLabel.setText(calculateAge(userFetchData.getString("Date of Birth")));
                            typeLabel.setText(userFetchData.getString("Type"));
                            String expiry = String.valueOf(calculateExpiryDuration(userFetchData.getString("Date of Expiry")));
                            reamainingValidityLabel.setText(String.valueOf(expiry + " Days"));
                            bloodGroupLabel.setText(userFetchData.getString("Blood Group"));

                            byte[] imageData = conncetionUsers.fetchImage(userFetchData.get("Image", Binary.class));
                            picture.setIcon(addImage(imageData));
                            isRetrieved = true;
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(mainFrame, "User Not Found", "User Not Fund", JOptionPane.ERROR_MESSAGE);
                        }

                        try {
                            Document d = connectionSaveResult.readDocument("learnerNo", textField1.getText());
                            LocalDate dateOfIssue = LocalDate.parse(d.getString("lastApply"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                            Period period = Period.between(dateOfIssue, LocalDate.now());

                            if (period.getDays() < 41) {
                                JOptionPane.showMessageDialog(mainFrame, "Days after Last Apply is " + period.getDays() + " days.Cannot GIve Test before 41 Days.");
                                return;
                            }
                        }
                        catch (Exception ex){
                            System.out.println("Error");
                        }
                    }

                }
                else if (e.getActionCommand().equals("Submit")){
// TODO: 15/01/2024 Make a Last Check for Crorrectness
                    if(!isRetrieved){
                        JOptionPane.showMessageDialog(mainFrame,"No information provided to Submit","Information not Provided",JOptionPane.ERROR_MESSAGE);
                    }

                    else if (symbolResultLabel.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(mainFrame, "Please Give Symbol Test First", "Test Result", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else if (!drivingPassCheckBox.isSelected() && !drivingFailCheckBox.isSelected()) {
                        JOptionPane.showMessageDialog(mainFrame, "Please Select Driving Test Condtion Pass/Fail", "Test Result", JOptionPane.INFORMATION_MESSAGE);
                    }

                    else {
                        LocalDate dateOfIssue = LocalDate.parse(dateOfIssueLabel.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                        long period = ChronoUnit.DAYS.between(dateOfIssue, LocalDate.now());
                        JOptionPane.showMessageDialog(mainFrame, "Days "+period+ "as  "+dateOfIssueLabel.getText() + " as " + LocalDate.now());

                        if (period < 41) {
                            JOptionPane.showMessageDialog(mainFrame, "Days after issuing Learner is " + period+ " days.Cannot GIve Test before 41 Days.");
                        } else {
                            Map<String, Object> documentMap = new HashMap<>();
                            documentMap.put("learnerNo", learnerInput.getText());
                            if (isSybolPass) {
                                documentMap.put("SymbolTest", true);
                            }
                            else {
                                documentMap.put("SymbolTest", false);
                            }

                            if (drivingPassCheckBox.isSelected()) {
                                documentMap.put("Driving Test", true);
                            } else if (drivingFailCheckBox.isSelected()) {
                                documentMap.put("Driving Test", false);
                            }

                            documentMap.put("lastApply",LocalDate.now());

                            if (connectionSaveResult.createDocument(documentMap)) {
                                if(drivingPassCheckBox.isSelected()&&issueDrivingLicense()){
                                    JOptionPane.showMessageDialog(mainFrame, "License Issued Successfully", "Submitted", JOptionPane.INFORMATION_MESSAGE);
                                }
                                else {
                                    JOptionPane.showMessageDialog(mainFrame, "License Not Issued", "No issuance", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                            else {
                                JOptionPane.showMessageDialog(mainFrame, "Form Submission Error", "Not Submitted", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }

                }
                else if(e.getActionCommand().equals("Print")){

                        printDocument();

                } else if (e.getActionCommand().equals("Back")) {
                    mainFrame.dispose();
                    new UserPannel();
                }
            }

            private Boolean issueDrivingLicense() {

                try {
                    MongoDataBase temp=new MongoDataBase("Driving_Center","id_Collection");
                    int licenseNo=temp.updateId("licenseID",true);
                    temp= new MongoDataBase("Driving_Center", "Licenses");

                    Map<String, Object> data=new HashMap<>();
                    data.put("Cnic", cnicLabel.getText());
                    data.put("LicenseNo",licenseNo);
                    data.put("Type",typeLabel.getText());
                    data.put("Date of issue",currentDate(false));
                    data.put("Date of Expiry",currentDate(true));

                    temp.createDocument(data);
                    String message = "Dear " + nameLabel.getText() + ",\nRegistration Confirmed .Your License has been issued for "+typeLabel.getText()+"\nLicense No: "+licenseNo;
                    SendSMS.send(message);
                    return true;
                }
                catch (Exception e){
                    return false;
                }
            }

        };
        detailsFetchButton.addActionListener(buttonListener);
        print.addActionListener(buttonListener);
        submitButton.addActionListener(buttonListener);
        backButton.addActionListener(buttonListener);

    }

    static String currentDate(Boolean interval){


        LocalDate currentDate = LocalDate.now();
        if (interval){
            currentDate= currentDate.plusYears(5);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return currentDate.format(formatter);

    }

    private void printDocument() {
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        printerJob.setPrintable(new MyPrintable());

        if (printerJob.printDialog()) {
            try {
                printerJob.print();
            } catch (PrinterException ex) {
                ex.printStackTrace();
            }
        }
    }


    private  class MyPrintable implements Printable, ImageObserver {

        public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
            if (pageIndex > 0) {
                return Printable.NO_SUCH_PAGE;
            }

            Graphics2D g2d = (Graphics2D) g;
            g2d.translate(pf.getImageableX(), pf.getImageableY());

            // Draw your content here for printing
            // Header
            Font originalFont = g.getFont();
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("License Test Form", 200, 50);

            Stroke originalStroke = g2d.getStroke();
            g2d.setStroke(new BasicStroke(3));

            g2d.drawRect(300, 220, 90, 23);
            String  text="Learner No :   "+ textField1.getText();
            g.drawString(text, 180, 240);

            g2d.setStroke(originalStroke);

            g.setFont(originalFont);
            // Info Panel content
            String nameToPrint = nameLabel.getText().length() > 15 ? nameLabel.getText().substring(0, 15) : nameLabel.getText();
            g.drawString("Name: " + nameToPrint, 100, 100);
            Icon icon = picture.getIcon();
            if (icon instanceof ImageIcon) {
                Image image = ((ImageIcon) icon).getImage();
                int imageWidth = 100;
                int imageHeight = 100;
                // Draw the image at coordinates (150, 160)
                g.drawImage(image, 480, 50, imageWidth, imageHeight,this);
            } else {
                // Handle the case when the icon is not an ImageIcon
                g.drawString("No image available", 150, 160);
            }
            g.drawString("CNIC: " + cnicLabel.getText(), 300, 100);
            // Add other fields from the info panel as needed

            int gap=50;

            g.drawString("Father Name : " + fatherNameLabel.getText(), 100, 130);
            g.drawString("Father CNIC : " + fatherCniclabel.getText(), 300, 130);
            g.drawString("Date of Birth: " + dateOfBirthLabel.getText(), 100, 160);
            g.drawString("AGE : " + ageLabel.getText(), 300, 160);
            g.drawString("Phone No : " + phoneNoLabel.getText(), 100, 190);
            g.drawString("Blood Group : " + bloodGroupLabel.getText(), 300, 190);
            g.drawLine(100, 210, 500, 210);
            g.drawString("Type : " + typeLabel.getText(), 100, 220+gap);
            g.drawString("Validity Remaining : " + reamainingValidityLabel.getText(), 300, 220+gap);
            g.drawString("Date of Issue : " + dateOfIssueLabel.getText(), 100, 250+gap);
            g.drawString("Date of Expiry: " + dateOfExpiryLabel.getText(), 300, 250+gap);
            // Separator
            g.drawLine(100, 270+gap, 500, 270+gap);

//             Test Detail content

            g.drawString("Symbol Test: " ,100, 320+gap);

            drawCheckbox(g, 230, 308+gap, isSybolPass);
            g.drawString("Pass " ,260, 320+gap);

            drawCheckbox(g, 310, 308+gap,isSybolPass);
            g.drawString("Fail" ,340, 320+gap);

            g.drawString("Driving Test: " ,100, 370+gap);

            drawCheckbox(g, 230, 358+gap, drivingPassCheckBox.isSelected());
            g.drawString("Pass " ,260, 370+gap);

            drawCheckbox(g, 310, 358+gap, drivingFailCheckBox.isSelected());
            g.drawString("Fail" ,340, 370+gap);


            g.drawString("Remarks: " + remarksTextArea.getText(), 100, 410+gap);
            g.drawLine(130, 440+gap, 500, 440+gap);
            g.drawLine(110, 470+gap, 500, 470+gap);
            g.drawLine(110, 500+gap, 500, 500+gap);


            return Printable.PAGE_EXISTS;
        }
        private void drawCheckbox(Graphics g, int x, int y, boolean checked) {
            // Draw a square representing the checkbox
            g.drawRect(x, y, 20, 20);

            // If checked, draw a checkmark
            if (checked) {
                g.drawLine(x + 2, y + 10, x + 8, y + 18);
                g.drawLine(x + 8, y + 18, x + 18, y + 2);
            }
        }

        @Override
        public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
            return false;
        }
    }




    public static void main(String[] args) {
        LicenseTestForm us=new LicenseTestForm();


    }
}
