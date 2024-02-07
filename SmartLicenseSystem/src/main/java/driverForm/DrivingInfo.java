package driverForm;

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
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.infobip.sms.SendSMS;
import com.toedter.calendar.JDateChooser;
import java.text.SimpleDateFormat;

import licenseTestForm.LicenseTestForm;
import DataBase.MongoDataBase;
import org.bson.Document;
import org.bson.types.Binary;
import users.UserPannel;

import static addSymbols.AddQuestion.isImageFile;


public class DrivingInfo implements Runnable {

    private JFrame mainFrame;
    private JPanel Driving_Form;
    private JDateChooser  dateOfBirthInput;
    private JTextField phoneNoInput;
    private JLabel cnic;
    private JLabel fatherName;
    private JLabel fatherCnic;
    private JTextField fatherCnicInput;
    private JTextField cnicInput;
    private JTextField nameInput;
    private JTextField fatherNameInput;
    private JLabel dateofBirth;
    private JLabel name;
    private JLabel age;
    private JLabel ageLabel;
    private JLabel phoneNo;
    private JLabel bloodGroup;
    JComboBox<String> bloodGroupsList;
    private JButton submitButton;
    private JLabel picture;
    private JSeparator separator1;
    private JLabel learnerNo1;
    private JLabel dateOfIssue1;
    private JLabel dateOfIssue1Label;
    private JLabel dateOfExpiry1;
    private JLabel dateOfExpiry1Label;
    private JLabel type1;
    private JComboBox<String> type1List;
    private JLabel learnerNo1Label;
    private JLabel learnerHeading;
    private JButton backButton;
    private String picturePath;
    private Boolean isImageAdded;
    JButton picPath;
    MongoDataBase userData;
    MongoDataBase temp;
    int xalignD =0, yalignD =0;
    int xalignL=0, yalignL =0;
    public DrivingInfo() {

        Thread t1=new Thread(this);
        t1.start();
    }

    public void run() {
        createConnection();
        initGUI();
    }

    private void createConnection() {
        userData=new MongoDataBase("Driving_Center","Drivers");
        temp=new MongoDataBase("Driving_Center","id_Collection");
    }


    void initGUI(){

        mainFrame=new JFrame();
        mainFrame.setTitle("Driver Info");

        xalignL=-20;
        xalignD=-20;
        yalignL=-20;
        yalignD=-50;

        Driving_Form=new JPanel();
        Driving_Form.setLayout(null);

        addDriverInfo();


        separator1=new JSeparator();
        separator1.setBounds (50+ xalignD, 330+yalignD, 725, 20);
        Driving_Form.add(separator1);

        addLearner();
        submitButton=new JButton("Submit Now");
        submitButton.setBounds (165+ xalignL, 515+ yalignL, 540, 35);
        Driving_Form.add(submitButton);

        backButton=new JButton("Back");
        backButton.setBorder(new LineBorder(Color.gray, 2, true));

        backButton.setBounds(720+xalignL,515+yalignL,120,35);
        Driving_Form.add(backButton);

        mainFrame.add(Driving_Form);
        mainFrame.setVisible(true);
        mainFrame.setSize(899, 592);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        dateOfBirthInput.getDateEditor().addPropertyChangeListener(e -> {
            if ("date".equals(e.getPropertyName())) {
                handleSelectedDate();
            }

        });

        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (isFormValid()) {
                    int a= 0;
                    try {
                        a = isDriverExists();
                    } catch (ParseException ex) {
                        throw new RuntimeException(ex);
                    }

                    if(isLicensExists()){
                        JOptionPane.showMessageDialog(mainFrame,"Regular Driving License Already Exists","License Exists",JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }

                    if(a==0) {
                        saveData();
                        JOptionPane.showMessageDialog(mainFrame,"Learner Created Successfully");
                    }
                    else if(a==1){
                        JOptionPane.showMessageDialog(mainFrame,"Driver Already Exists");
                        printDocument();
                        return;
                    }
                    else if(a==2){
                        JOptionPane.showMessageDialog(mainFrame,"Learner Updated Successfully");
                    }
                    temp.updateId("learnerNo", true);
                    String message = "Dear " + nameInput.getText() + ",\nRegistration Confirmed .Your Learner has been issued.\nLearner No: "+learnerNo1Label.getText();
                    SendSMS.send(message);
                    learnerNo1Label.setText(String.valueOf(temp.updateId("learnerNo",false)));
                    printDocument();

                }
            }

            private boolean isLicensExists() {

                try {
                    MongoDataBase temp = new MongoDataBase("Driving_Center", "Licenses");
                    Document data = temp.readDocument("Cnic", cnicInput.getText());
                    if (data.getString("Type").equals(type1List.getSelectedItem())) {
                        return true;
                    }
                    return false;
                }
                catch (Exception e) {
                    return false;
                }
            }

            private int isDriverExists() throws ParseException {

                Document data=userData.readDocument("Cnic",cnicInput.getText());
                try {
                    String type = data.getString("Type");
                    if (type.equals(type1List.getSelectedItem())) {
                        if (isExpired(data.getString("Date of Expiry"))) {
                            userData.updateDriverDocument("LearnerNo", data.getString("LearnerNo"), "Date of Issue", dateOfIssue1Label.getText(), "Date of Expiry", dateOfExpiry1Label.getText(), "LearnerNo", learnerNo1Label.getText());
                            return 2;
                        }

                        nameInput.setText(data.getString("Name"));

                        cnicInput.setText(data.getString("Cnic"));

                        fatherNameInput.setText(data.getString("Father Name"));

                        fatherCnicInput.setText(data.getString("Father Cnic"));

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        dateOfBirthInput.setDate(dateFormat.parse(data.getString("Date of Birth")));

                        phoneNoInput.setText(data.getString("Phone No"));

                        type1List.setSelectedItem(data.getString("Type"));

                        learnerNo1Label.setText(data.getString("LearnerNo"));

                        dateOfExpiry1Label.setText(data.getString("Date of Expiry"));

                        dateOfIssue1Label.setText(data.getString("Date of Issue"));
                        byte[] imageData = MongoDataBase.fetchImage(data.get("Image", Binary.class));
                        picture.setIcon(addImage(imageData));

                        setEnable(false);
                        return 1;
                    }
                }
                catch (Exception e){
                    return 0;
                }
                return 0;
            }

            private boolean isExpired(String dateOfExpiry) {
                return Integer.parseInt(LicenseTestForm.calculateExpiryDuration(dateOfExpiry)) <= 0;
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose();
                new UserPannel();
            }
        });

    }
    private boolean handleSelectedDate() {
        Date currentDate = new Date();

        if (Integer.parseInt(calculateAge(selectedDate()))<18) {
            JOptionPane.showMessageDialog(
                    mainFrame,
                    "Age must be Greater than 18.",
                    "Invalid Date",
                    JOptionPane.ERROR_MESSAGE);
                    return false;
        }
        else {
           ageLabel.setText(calculateAge(selectedDate()));
           return true;
        }

    }

    private boolean isFormValid() {
        if (nameInput.getText().trim().isEmpty() || cnicInput.getText().trim().isEmpty() ||
                fatherNameInput.getText().trim().isEmpty() || phoneNoInput.getText().trim().isEmpty() ||
                dateOfBirthInput.getDate() == null) {
            JOptionPane.showMessageDialog(mainFrame, "Please fill in all the required fields.", "Form Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String cnicPattern = "^[0-9]{13}$";
        if (!cnicInput.getText().matches(cnicPattern)) {
            JOptionPane.showMessageDialog(mainFrame, "CNIC must be 13 digits long.", "Form Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }


        String phonePattern = "^(030[0-9]|031[0-9]|032[0-9]|033[0-5])[0-9]{7}$";
        if (!phoneNoInput.getText().matches(phonePattern)) {
            JOptionPane.showMessageDialog(mainFrame, "Invalid phone number. Please use the format: 03XXXXXXXXX.", "Form Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(!handleSelectedDate()){
            return false;
        }

        if(!isImageAdded){
            JOptionPane.showMessageDialog(mainFrame, "Please Add Image", "Form Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;

    }

    void setEnable(Boolean is){

        nameInput.setEnabled(is);

        cnicInput.setEnabled(is);

        fatherNameInput.setEnabled(is);

        fatherCnicInput.setEnabled(is);

        dateOfBirthInput.setEnabled(is);

        phoneNoInput.setEnabled(is);

        type1List.setEnabled(is);
        picPath.setVisible(is);

    }

    private void saveData() {


        Map<String, Object> documentMap = new HashMap<>();
        String path=picturePath;
        try {
            documentMap.put("Name", nameInput.getText());
            documentMap.put("Cnic", cnicInput.getText());
            documentMap.put("Father Name", fatherNameInput.getText());
            documentMap.put("Father Cnic", fatherCnicInput.getText());
            documentMap.put("Phone No", phoneNoInput.getText());
            documentMap.put("Blood Group", bloodGroupsList.getSelectedItem());
            documentMap.put("city", "Lahore");
            documentMap.put("Image", userData.storeImage(path));
            documentMap.put("Date of Birth",selectedDate());
            documentMap.put("Date of Issue",dateOfIssue1Label.getText());
            documentMap.put("Date of Expiry",dateOfExpiry1Label.getText());
            documentMap.put("Type",type1List.getSelectedItem());
            documentMap.put("LearnerNo",learnerNo1Label.getText());
            userData.createDocument(documentMap);

            JOptionPane.showMessageDialog(null,"Form Submitted Successfully");
        }
        catch (Exception ex){
            System.out.println(ex);
        }
    }

    private String selectedDate() {
        Date selectedDate = dateOfBirthInput.getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(selectedDate);
    }
    private JLabel addImage(){
        isImageAdded=false;
        JLabel pic=new JLabel();
        ImageIcon imageIcon = new ImageIcon("symbolImages\\placeholder2.png");
        Image image = imageIcon.getImage();
        Image scaledImage = image.getScaledInstance(170, 160, Image.SCALE_SMOOTH);
        ImageIcon scaledImageIcon = new ImageIcon(scaledImage);
        pic.setText("");
        pic.setIcon(scaledImageIcon);
        return pic;
    }

    private ImageIcon addImage(byte[] imageData){
        JLabel pic=new JLabel();
        pic.setText("");
        ImageIcon imageIcon = new ImageIcon(imageData);
        Image image = imageIcon.getImage();
        Image scaledImage = image.getScaledInstance(170, 160, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }
    private void addDriverInfo(){


        name=new JLabel("Name");
        name.setBounds (65+ xalignD, 110+ yalignD, 100, 30);
        Driving_Form.add(name);

        nameInput=new JTextField();
        nameInput.setBounds (210+ xalignD, 110+ yalignD, 170, 30);
        Driving_Form.add(nameInput);

        picPath=new JButton("Add Image");
        picPath.setBounds(740+xalignD,275+yalignD,135,25);
        Driving_Form.add(picPath);

        picture=addImage();
        picture.setBounds (720+ xalignD, 65-10, 170, 160);
        picture.setBorder(new LineBorder(Color.gray, 2, true));
        Driving_Form.add(picture);

        cnic=new JLabel("Cnic No :");
        cnic.setBounds (415+ xalignD, 110+ yalignD, 100, 25);
        Driving_Form.add(cnic);

        cnicInput=new JTextField();
        cnicInput.setBounds (540+ xalignD, 110+ yalignD, 170, 30);
        Driving_Form.add(cnicInput);

        fatherName=new JLabel("Father Name");
        fatherName.setBounds (65+ xalignD, 155+ yalignD, 100, 30);
        Driving_Form.add(fatherName);

        fatherNameInput=new JTextField();
        fatherNameInput.setBounds (210+ xalignD, 155+ yalignD, 170, 30);
        Driving_Form.add(fatherNameInput);

        fatherCnic=new JLabel("Father CNIC No :");
        fatherCnic.setBounds (415+ xalignD, 155+ yalignD, 100, 30);
        Driving_Form.add(fatherCnic);

        fatherCnicInput=new JTextField();
        fatherCnicInput.setBounds (540+ xalignD, 155+ yalignD, 170, 30);
        Driving_Form.add(fatherCnicInput);

        dateofBirth=new JLabel("Date of Birth");
        dateofBirth.setBounds (65+ xalignD, 200+ yalignD, 100, 30);
        Driving_Form.add(dateofBirth);

        dateOfBirthInput=new JDateChooser();
        dateOfBirthInput.getDateEditor().setEnabled(false);
        dateOfBirthInput.setBounds (210+ xalignD, 200+ yalignD, 170, 30);
        Driving_Form.add(dateOfBirthInput);

        age=new JLabel("AGE");
        age.setBounds (415+ xalignD, 200+ yalignD, 100, 25);
        Driving_Form.add(age);

        ageLabel=new JLabel("0");
        ageLabel.setBounds (540+ xalignD, 200+ yalignD, 170, 30);
        Driving_Form.add(ageLabel);

        phoneNo=new JLabel("Phone No : ");
        phoneNo.setBounds (415+ xalignD, 245+ yalignD, 100, 25);
        Driving_Form.add(phoneNo);

        phoneNoInput=new JTextField();
        phoneNoInput.setBounds (540+ xalignD, 245+ yalignD, 170, 30);
        Driving_Form.add(phoneNoInput);

        bloodGroup=new JLabel("Blood Group : ");
        bloodGroup.setBounds (65+ xalignD, 245+ yalignD, 100, 25);
        Driving_Form.add(bloodGroup);

        String[] items = {"A+", "A-", "B+","B-","O+","O-","AB+","AB-"};

         bloodGroupsList= new JComboBox<>(items);
        bloodGroupsList.setBounds (210+ xalignD, 245+ yalignD, 170, 30);
        Driving_Form.add(bloodGroupsList);

        Border etchedBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);


        TitledBorder titledBorder = BorderFactory.createTitledBorder(etchedBorder, "Learner Holder Information");
        titledBorder.setTitleColor(Color.BLACK);
        Font labelFont = new Font("Arial", Font.BOLD, 18);
        titledBorder.setTitleJustification(TitledBorder.CENTER);
        titledBorder.setTitleFont(labelFont);

        Driving_Form.setBorder(titledBorder);



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
                            isImageAdded=true;

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

    private JPanel addLearner(){


        learnerHeading=new JLabel("Leaner Information");
        learnerHeading.setBounds (270+xalignL, 335+ yalignL, 290, 40);
        Font labelFont = new Font("Arial", Font.BOLD, 18);
        learnerHeading.setFont(labelFont);
        learnerHeading.setHorizontalAlignment(SwingConstants.CENTER);
        Driving_Form.add(learnerHeading);

        learnerNo1=new JLabel("Leaner No : ");
        learnerNo1.setBounds (115+xalignL, 390+ yalignL, 100, 30);
        Driving_Form.add(learnerNo1);


        learnerNo1Label = new JLabel(String.valueOf(temp.updateId("learnerNo",false)));
        learnerNo1Label.setBounds (260+xalignL, 390+ yalignL, 100, 30);
        Driving_Form.add(learnerNo1Label);

        dateOfIssue1=new JLabel("Date of issue");
        dateOfIssue1.setBounds (420+xalignL, 390+ yalignL, 100, 30);
        Driving_Form.add(dateOfIssue1);

        dateOfIssue1Label=new JLabel(currentDate(false));
        dateOfIssue1Label.setBounds (565+xalignL, 390+ yalignL, 100, 30);
        Driving_Form.add(dateOfIssue1Label);

        type1=new JLabel("Type");
        type1.setBounds (115+xalignL, 445+ yalignL, 100, 30);
        Driving_Form.add(type1);

        String[] types = {"M.Cycle","Car/Jeep","LTV","HTV"};
        type1List=new JComboBox<>(types);
        type1List.setBounds (260+xalignL, 445+ yalignL, 100, 30);
        Driving_Form.add(type1List);

        dateOfExpiry1=new JLabel("Date of Expiry");
        dateOfExpiry1.setBounds (420+xalignL, 445+ yalignL, 100, 30);
        Driving_Form.add(dateOfExpiry1);

        dateOfExpiry1Label=new JLabel(currentDate(true));
        dateOfExpiry1Label.setBounds(565+xalignL, 445+ yalignL, 100, 30);
        Driving_Form.add(dateOfExpiry1Label);

        return Driving_Form;

    }


    String currentDate(Boolean interval){


        LocalDate currentDate = LocalDate.now();
        if (interval){
            currentDate= currentDate.plusMonths(6);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return currentDate.format(formatter);

    }
    public static String  calculateAge(String dateOfBirth) {
        LocalDate birthDate = LocalDate.parse(dateOfBirth);

        LocalDate currentDate = LocalDate.now();

        Period period = Period.between(birthDate, currentDate);

        return String.valueOf(period.getYears());
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

            Font originalFont = g.getFont();
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Learner Form", 200, 50);

            Stroke originalStroke = g2d.getStroke();
            g2d.setStroke(new BasicStroke(3));

            g2d.drawRect(300, 175, 90, 23);
            String  text="Learner No :  "+ learnerNo1Label.getText();
            g.drawString(text, 180, 195);

            g2d.setStroke(originalStroke);

            g.setFont(originalFont);
            // Info Panel content
            String nameToPrint = nameInput.getText().length() > 15 ? nameInput.getText().substring(0, 15) : nameInput.getText();
            g.drawString("Name: " + nameInput.getText(), 100, 100);
            Icon icon = picture.getIcon();
            if (icon instanceof ImageIcon) {
                Image image = ((ImageIcon) icon).getImage();
                int imageWidth = 100;
                int imageHeight = 100;
                // Draw the image at coordinates (150, 160)
                g.drawImage(image, 480, 50, imageWidth, imageHeight,this);
            } else {
                // Handle the case when the icon is not an ImageIcon
                g.drawString("No image available", 480, 50);
            }
            g.drawString("CNIC: " + cnicInput.getText(), 300, 100);
            // Add other fields from the info panel as needed

            g.drawString("Father Name : " + fatherNameInput.getText(), 100, 130);
            g.drawString("AGE : " + calculateAge(selectedDate()), 300, 130);
            g.drawString("Phone No : " + phoneNoInput.getText(), 100, 160);
            g.drawString("Blood Group : " + bloodGroupsList.getSelectedItem(), 300, 160);
            g.drawString("Type : " + type1List.getSelectedItem(), 100, 220);
            g.drawString("Date of Issue : " + dateOfIssue1Label.getText(), 100, 250);
            g.drawString("Date of Expiry: " + dateOfExpiry1Label.getText(), 300, 250);

            g.drawLine(100, 270, 500, 270);

            int x = 50;
            int y = 290;
            int width = 500;
            int height = 85;


            g2d.setStroke(new BasicStroke(3));

            g2d.drawRect(x, y, width, height);

//        g2d.setStroke(originalStroke);

            text = "For First Time you can take Driving Test after 41 days of issuing Learner.";
            Font font = new Font("Arial", Font.PLAIN, 12);
            g.setFont(font);
            g.drawString(text, x + 10, y + 30);
            text="This codition doesn't Apply on Leraner Renewal !";
            g.drawString(text, x + 10, y + 50);
            g2d.drawRect(199, 345, 90, 20);
            text="Eligible For Driving Test :    "+ LocalDate.now().plusDays(41);
            g.drawString(text, x + 10, y + 70);


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
        DrivingInfo us=new DrivingInfo();

    }
}
