package licenseTestForm;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import java.awt.print.*;
import java.time.LocalDate;
import java.time.Period;

import com.infobip.sms.SendSMS;
import DataBase.MongoDataBase;
import org.bson.Document;
import org.bson.types.Binary;
import users.UserPannel;

import static addSymbols.AddQuestion.isImageFile;
import static licenseTestForm.LicenseTestForm.currentDate;

public class UpdateLicense implements Runnable {

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
    private JTextArea remarksTextArea;
    JPanel licenseTestForm;
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
    private JLabel reamarks;
    private JLabel picture;
    private JLabel dateOfBirthLabel;
    private JSeparator separator;
    private boolean isRetrieved;
    private MongoDataBase conncetionUsers;
    private MongoDataBase connectionLicenses;
    private JButton backButton;
    private JButton picPath;
    private static Boolean isExpired;
    private String picturePath="";
    private Boolean isPicUpdated;
    public UpdateLicense() {
        Thread t1 = new Thread(this);
        t1.run();

    }
    void createConnection(){
        connectionLicenses = new MongoDataBase("Driving_Center", "Licenses");
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

        mainFrame.setSize(890, 620);
        mainFrame.add(licenseTestForm);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        licenseTestForm.setVisible(true);

        mainFrame.setVisible(true);

        addActionListeners();
    }

    private void setlayout() {

        Border etchedBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);


        TitledBorder titledBorder = BorderFactory.createTitledBorder(etchedBorder, "Driving License Renewal");
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
        reamarks.setBounds (200, 460, 94, 30);
        remarksTextArea.setBounds (310, 465, 275, 55);
        picture.setBounds (680, 55, 175, 160);
        submitButton.setBounds (440, 530, 160, 30);
        print.setBounds (260, 530, 160, 30);
        backButton.setBounds(120,530,120,30);
        picPath.setBounds(710,260,135,25);

    }

    public static String calculateExpiryDuration( String dateOfExpiry){

        LocalDate currentDate = LocalDate.now();
        LocalDate expiryDate = LocalDate.parse(dateOfExpiry);

        Period expiryDuration = Period.between(currentDate, expiryDate);;

        if(expiryDuration.getYears()>0){
            return expiryDuration.getYears()+" Years";
        }
        if(expiryDuration.getMonths()>0){
            return expiryDuration.getMonths()+" Months";
        }
        if(expiryDuration.getDays()>0){
            return expiryDuration.getDays()+" Days";
        }
        else{
            isExpired=true;
            return "Expired";
        }

    }

    public static String  calculateAge(String dateOfBirth) {
        LocalDate birthDate = LocalDate.parse(dateOfBirth);

        LocalDate currentDate = LocalDate.now();

        Period period = Period.between(birthDate, currentDate);

        return String.valueOf(period.getYears());
    }
    ImageIcon addImage(byte[] imageData){
        JLabel pic=new JLabel();
        pic.setText("");
        ImageIcon imageIcon = new ImageIcon(imageData);
        Image image = imageIcon.getImage();
        Image scaledImage = image.getScaledInstance(130, 120, Image.SCALE_SMOOTH);
        ImageIcon scaledImageIcon = new ImageIcon(scaledImage);
        return scaledImageIcon;
    }

    void addInfoPanel(){

        learnerInput=new JLabel("License No");
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

        picPath=new JButton("Update Image");
        picPath.setEnabled(false);
        licenseTestForm.add(picPath);

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

        picPath.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();

                FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "png", "jpg");
                fileChooser.setFileFilter(filter);

                int result = fileChooser.showOpenDialog(mainFrame);



                if (result == JFileChooser.APPROVE_OPTION) {
                    // User selected a file
                    picturePath = fileChooser.getSelectedFile().getAbsolutePath();

                    if (isImageFile(picturePath)) {
                        try {
                            byte[] imageData = MongoDataBase.storeImage(picturePath);
                            ImageIcon imageIcon = new ImageIcon(imageData);
                            Image scaledImage = imageIcon.getImage().getScaledInstance(190, 165, Image.SCALE_SMOOTH);
                            ImageIcon scaledImageIcon = new ImageIcon(scaledImage);
                            picture.setBorder(new LineBorder(Color.gray, 2, true));
                            picture.setIcon(scaledImageIcon);
                            isPicUpdated=true;
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


    void testDetail(){


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
        print.setEnabled(false);


    }

    void addActionListeners() {
        ActionListener buttonListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                if(e.getActionCommand().equals("Retrieve")){
                    isExpired=false;
                    isPicUpdated=false;

                    if(textField1.getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(mainFrame,"Please Enter License No","Learner No not Provided",JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                        try {

                            Document licenseData = connectionLicenses.searchDocument("LicenseNo", Integer.parseInt(textField1.getText().trim()));
                            if(licenseData==null){
                                JOptionPane.showMessageDialog(mainFrame, textField1.getText().trim(), "User Not Found", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
//                            Document userFetchData=conncetionUsers.readLicenseDocument("Cnic",licenseData.getString("Cnic"),"Type",licenseData.getString("Type"));
                            Document userFetchData=conncetionUsers.searchDocument("Cnic",licenseData.getString("Cnic"));

                            nameLabel.setText(userFetchData.getString("Name"));

                            cnicLabel.setText(userFetchData.getString("Cnic"));
                            fatherNameLabel.setText(userFetchData.getString("Father Name"));
                            fatherCniclabel.setText(userFetchData.getString("Father Cnic"));
                            dateOfBirthLabel.setText(userFetchData.getString("Date of Birth"));
                            dateOfIssueLabel.setText(licenseData.getString("Date of issue"));
                            dateOfExpiryLabel.setText(licenseData.getString("Date of Expiry"));
                            phoneNoLabel.setText(userFetchData.getString("Phone No"));
                            ageLabel.setText(calculateAge(userFetchData.getString("Date of Birth")));
                            typeLabel.setText(userFetchData.getString("Type"));
                            String expiry = String.valueOf(calculateExpiryDuration(licenseData.getString("Date of Expiry")));
                            reamainingValidityLabel.setText(expiry);
                            bloodGroupLabel.setText(userFetchData.getString("Blood Group"));

                            byte[] imageData = conncetionUsers.fetchImage(userFetchData.get("Image", Binary.class));

                            picture.setIcon(addImage(imageData));
                            isRetrieved = true;
                            print.setEnabled(true);
                            if(isExpired){
                                submitButton.setEnabled(true);
                                picPath.setEnabled(true);
                                dateOfIssueLabel.setText(currentDate(false));
                                dateOfExpiryLabel.setText(currentDate(true));
                            }
                            else{
                                JOptionPane.showMessageDialog(mainFrame,"Valid Regular License Already Exists","License Exists",JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(mainFrame, "User Not Found", "User Not Fund", JOptionPane.ERROR_MESSAGE);
                        }
                    }

                }
                else if (e.getActionCommand().equals("Submit")){

                    if(!isRetrieved){
                        JOptionPane.showMessageDialog(mainFrame,"No information provided to Submit","Information not Provided",JOptionPane.ERROR_MESSAGE);
                    }
                    if(isExpired){
                        if(isPicUpdated) {
                            UpdateDrivingLicense();
                        }
                        else{
                            JOptionPane.showMessageDialog(mainFrame,"Please Upload Latest Pic","Pic not Updated",JOptionPane.ERROR_MESSAGE);
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

            private Boolean UpdateDrivingLicense() {

                try {
                    MongoDataBase temp=new MongoDataBase("Driving_Center","id_Collection");
                    int licenseNo=temp.updateId("licenseID",true);
                    temp= new MongoDataBase("Driving_Center", "Licenses");
                    temp.updateLicenseDocument("Cnic",cnicLabel.getText(),"Date of issue",currentDate(false),"Date of Expiry",currentDate(true),"LicenseNo",licenseNo);
                    conncetionUsers.updateImage("Cnic",cnicLabel.getText(),"Image",picturePath);
                    String message = "Dear " + nameLabel.getText() + ",\nRegistration Confirmed .Your License has been Renewed for "+typeLabel.getText()+"\nLicense No: "+licenseNo;
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
            String  text="License No :   "+ textField1.getText();
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

            return Printable.PAGE_EXISTS;
        }

        @Override
        public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
            return false;
        }
    }




    public static void main(String[] args) {
        UpdateLicense us=new UpdateLicense();


    }
}
