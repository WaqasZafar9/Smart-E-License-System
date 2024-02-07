package users;

import Admin.AdminPannel;
import com.infobip.sms.SendSMS;
import com.toedter.calendar.JDateChooser;
import DataBase.MongoDataBase;
import org.bson.Document;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UpdateUser implements Runnable {
    private JFrame mainFrame;
    private JPanel User_Form;
    private JDateChooser dateOfBirthInput;
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
    private JDateChooser dateOfJoining;
    private JSeparator separator1;
    private JLabel dateOfIssue1;
    private JLabel dateOfIssue1Label;
    private JLabel dateOfExpiry1Label;
    private JComboBox<String> type1List;
    private JLabel password;
    private JLabel rePassword;
    private JPasswordField passwordText;
    private JPasswordField rePasswordText;
    private JLabel previousPassword;
    private JPasswordField previousPasswordInput;
    int xalignD =0, yalignD =0;
    int xalignL=0, yalignL =0;
    MongoDataBase conncetionUsers;
    MongoDataBase conncetionids;
    private JLabel userID;
    private JTextField userIdInput;
    private JButton retrieve;
    private String prevPassword;
    private boolean isDelete;
    private JButton backButton;
    public UpdateUser(boolean isDelete){
        this.isDelete=isDelete;
        Thread t1 = new Thread(this);
        t1.run();
    }

    void createConnection(){
        conncetionUsers=new MongoDataBase("Driving_Center","usersInfo");
    }

    public void run() {
        initGUI();
        createConnection();
    }
    private void initGUI(){

        mainFrame=new JFrame();
        mainFrame.setTitle("User Info");

        xalignL=-20;
        xalignD=-20;
        yalignL=-20;
        yalignD=-20;

        User_Form =new JPanel();
        User_Form.setLayout(null);

        addDriverInfo();
        separator1=new JSeparator();
        separator1.setBounds (50+ xalignD, 310+yalignD, 725, 20);
        User_Form.add(separator1);


        if(isDelete) {
            submitButton = new JButton("Delete Now");
        }
        else {
            submitButton = new JButton("Update Now");
        }
        submitButton.setBounds (165+ xalignL, 515+ yalignL, 540, 35);
        User_Form.add(submitButton);
        backButton=new JButton("Back");
        backButton.setBorder(new LineBorder(Color.gray, 2, true));
        User_Form.add(backButton);
        backButton.setBounds(720+xalignL,515+yalignL,120,35);
        setEnabled(false);
        mainFrame.add(User_Form);
        mainFrame.setVisible(true);
        mainFrame.setSize(899, 592);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        dateOfBirthInput.getDateEditor().addPropertyChangeListener(e -> {
            if ("date".equals(e.getPropertyName())) {
                handleSelectedDate();
            }

        });
        dateOfJoining.getDateEditor().addPropertyChangeListener(e -> {
            if ("date".equals(e.getPropertyName())) {
                Date selectedDate = dateOfJoining.getDate();
                if (selectedDate != null) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date currentDate = new Date();

                    if (selectedDate.after(currentDate)) {
                        JOptionPane.showMessageDialog(mainFrame,"Cannot Select Upcoming Date","Wrong Date",JOptionPane.ERROR_MESSAGE);
                        dateOfJoining.setDate(currentDate);
                    }
                }

            }

        });

        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isDelete) {
                    try {
                        conncetionUsers.deleteDocument("userID", Integer.parseInt(userIdInput.getText()));
                        JOptionPane.showMessageDialog(null,"User Deleted Successfully");
                        mainFrame.dispose();
                        new AdminPannel();
                    }
                    catch (Exception ex){
                        JOptionPane.showMessageDialog(null,"User Not Deleted Successfully");
                    }
                } else {
                    if (isFormValid()) {
                    updateData();

                    String message = "Dear " + nameInput.getText() + ",\nRegistration Confirmed .Your User ID is "+userIdInput.getText()+".";
                    SendSMS.send(message);
                        mainFrame.dispose();
                        new AdminPannel();
                    }
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

        retrieve.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!isNumeric(userIdInput.getText())){
                    JOptionPane.showMessageDialog(mainFrame,"Please Enter Question ID","No Input Enter",JOptionPane.ERROR_MESSAGE);
                }
                else {
                    try {
                        Document userData = conncetionUsers.searchDocument("userID", userIdInput.getText());
                        nameInput.setText(userData.getString("Name"));
                        cnicInput.setText(userData.getString("Cnic"));
                        fatherNameInput.setText(userData.getString("Father Name"));
                        fatherCnicInput.setText(userData.getString("Father Cnic"));
                        phoneNoInput.setText(userData.getString("Phone No"));
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            dateOfBirthInput.setDate(dateFormat.parse(userData.getString("Date of Birth")));
                            dateOfJoining.setDate(dateFormat.parse(userData.getString("Date of Joining")));
                        } catch (ParseException ex) {
                            throw new RuntimeException(ex);
                        }
                        prevPassword = userData.getString("password");
                        if (!isDelete) {
                            setEnabled(true);
                        }
                        submitButton.setEnabled(true);
                    }
                    catch (Exception ex){
                        JOptionPane.showMessageDialog(mainFrame,"User Not Found","User Not Found",JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

    }


    private boolean handleSelectedDate() {
        Date currentDate = new Date();

        if (Integer.parseInt(calculateAge(selectedDate(true)))<18) {
            // The selected date is in the future
            JOptionPane.showMessageDialog(
                    mainFrame,
                    "Age must be Greater than 18.",
                    "Invalid Date",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        else {
            ageLabel.setText(calculateAge(selectedDate(true)));
            return true;
        }

    }

    public static boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }

        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    void setEnabled(boolean isEnable){

        nameInput.setEnabled(isEnable);
        cnicInput.setEnabled(isEnable);
        fatherCnicInput.setEnabled(isEnable);
        fatherNameInput.setEnabled(isEnable);
        dateOfBirthInput.setEnabled(isEnable);
        ageLabel.setEnabled(isEnable);
        bloodGroupsList.setEnabled(isEnable);
        phoneNoInput.setEnabled(isEnable);
        passwordText.setEnabled(isEnable);
        rePasswordText.setEnabled(isEnable);
        dateOfJoining.setEnabled(isEnable);



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


        String phonePattern = "^(030[1-9]|031[0-9]|032[0-9]|033[0-5])[0-9]{7}$";
        if (!phoneNoInput.getText().matches(phonePattern)) {
            JOptionPane.showMessageDialog(mainFrame, "Invalid phone number. Please use the format: 03XXXXXXXXX.", "Form Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(!handleSelectedDate()){
            return false;
        }
        if (!(previousPasswordInput.getPassword().length==0)) {

            if (!BCrypt.checkpw(String.valueOf(previousPasswordInput.getPassword()), prevPassword)) {

                JOptionPane.showMessageDialog(mainFrame, "Previous Password Doesnot Match", "Form Validation Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (!String.valueOf(passwordText.getPassword()).equals(String.valueOf(rePasswordText.getPassword()))) {
                JOptionPane.showMessageDialog(mainFrame, "Password Does Not Match", "Form Validation Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

    private void updateData() {

        Map<String, Object> documentMap = new HashMap<>();
        String path="symbolImages\\placeholder2.png";
        try {
            documentMap.put("Name", nameInput.getText());
            documentMap.put("Cnic", cnicInput.getText());
            documentMap.put("Father Name", fatherNameInput.getText());
            documentMap.put("Father Cnic", fatherCnicInput.getText());
            documentMap.put("Phone No", phoneNoInput.getText());
            documentMap.put("Blood Group", bloodGroupsList.getSelectedItem());
            documentMap.put("city", "Lahore");
            documentMap.put("Image", MongoDataBase.storeImage(path));
            documentMap.put("Date of Birth",selectedDate(true));
            documentMap.put("userID",Integer.parseInt(userIdInput.getText()));
            if(previousPasswordInput.getPassword() != null) {
                documentMap.put("password", BCrypt.hashpw(String.valueOf(passwordText.getPassword()), BCrypt.gensalt()));
            }
            documentMap.put("Date of Joining", selectedDate(false));
            if( conncetionUsers.updateUser(documentMap,false,"userID","userID")){
                JOptionPane.showMessageDialog(null,"User Updated Successfully");
            }


        }
        catch (Exception ex){
            JOptionPane.showMessageDialog(null,"Form ");
            System.out.println(ex);
        }
    }

    private String selectedDate(boolean is) {
        if(is) {
            Date selectedDate = dateOfBirthInput.getDate();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.format(selectedDate);
        }
        else {
            Date selectedDate = dateOfJoining.getDate();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.format(selectedDate);
        }
    }

    private JLabel addImage(){
        JLabel pic=new JLabel();
        ImageIcon imageIcon = new ImageIcon("symbolImages\\placeholder2.png");
        Image image = imageIcon.getImage();
        Image scaledImage = image.getScaledInstance(170, 160, Image.SCALE_SMOOTH);
        ImageIcon scaledImageIcon = new ImageIcon(scaledImage);
        pic.setText("");
        pic.setIcon(scaledImageIcon);
        return pic;
    }

    private void addDriverInfo(){

        userID = new JLabel ("User ID:");
        Font f2 = new Font("Arial", Font.BOLD, 16);
        userID.setFont(f2);
        userIdInput = new JTextField (5);
        retrieve = new JButton ("Retrieve");


        User_Form.add (userID);
        User_Form.add (userIdInput);
        User_Form.add (retrieve);

        userID.setBounds (180+ xalignD, 60+ yalignD, 80, 30);
        userIdInput.setBounds (265+ xalignD, 60+ yalignD, 140, 30);
        retrieve.setBounds (435+ xalignD, 60+ yalignD, 130, 30);

        name=new JLabel("Name");
        name.setBounds (65+ xalignD, 110+ yalignD, 100, 30);
        User_Form.add(name);

        nameInput=new JTextField();
        nameInput.setBounds (210+ xalignD, 110+ yalignD, 170, 30);
        User_Form.add(nameInput);

        picture=addImage();
        picture.setBounds (720+ xalignD, 65-10, 170, 160);
        picture.setBorder(new LineBorder(Color.gray, 2, true));
        User_Form.add(picture);

        cnic=new JLabel("Cnic No :");
        cnic.setBounds (415+ xalignD, 110+ yalignD, 100, 25);
        User_Form.add(cnic);

        cnicInput=new JTextField();
        cnicInput.setBounds (540+ xalignD, 110+ yalignD, 170, 30);
        User_Form.add(cnicInput);

        fatherName=new JLabel("Father Name");
        fatherName.setBounds (65+ xalignD, 155+ yalignD, 100, 30);
        User_Form.add(fatherName);

        fatherNameInput=new JTextField();
        fatherNameInput.setBounds (210+ xalignD, 155+ yalignD, 170, 30);
        User_Form.add(fatherNameInput);

        fatherCnic=new JLabel("Father CNIC No :");
        fatherCnic.setBounds (415+ xalignD, 155+ yalignD, 100, 30);
        User_Form.add(fatherCnic);

        fatherCnicInput=new JTextField();
        fatherCnicInput.setBounds (540+ xalignD, 155+ yalignD, 170, 30);
        User_Form.add(fatherCnicInput);

        dateofBirth=new JLabel("Date of Birth");
        dateofBirth.setBounds (65+ xalignD, 200+ yalignD, 100, 30);
        User_Form.add(dateofBirth);

        dateOfBirthInput=new JDateChooser();
        dateOfBirthInput.getDateEditor().setEnabled(false);
        dateOfBirthInput.setBounds (210+ xalignD, 200+ yalignD, 170, 30);
        User_Form.add(dateOfBirthInput);

        age=new JLabel("AGE");
        age.setBounds (415+ xalignD, 200+ yalignD, 100, 25);
        User_Form.add(age);

        ageLabel=new JLabel("0");
        ageLabel.setBounds (540+ xalignD, 200+ yalignD, 170, 30);
        User_Form.add(ageLabel);

        phoneNo=new JLabel("Phone No : ");
        phoneNo.setBounds (415+ xalignD, 245+ yalignD, 100, 25);
        User_Form.add(phoneNo);

        phoneNoInput=new JTextField();
        phoneNoInput.setBounds (540+ xalignD, 245+ yalignD, 170, 30);
        User_Form.add(phoneNoInput);

        bloodGroup=new JLabel("Blood Group : ");
        bloodGroup.setBounds (65+ xalignD, 245+ yalignD, 100, 25);
        User_Form.add(bloodGroup);

        String[] items = {"A+", "A-", "B+","B-","O+","O-","AB+","AB-"};

        bloodGroupsList= new JComboBox<>(items);
        bloodGroupsList.setBounds (210+ xalignD, 245+ yalignD, 170, 30);
        User_Form.add(bloodGroupsList);


        password = new JLabel ("Enter New Password : ");
        rePassword = new JLabel ("Re-Enter New Password : ");
        passwordText = new JPasswordField (5);
        rePasswordText = new JPasswordField (5);
        previousPassword = new JLabel ("Previous Password");
        previousPasswordInput = new JPasswordField(5);

        User_Form.add (password);
        User_Form.add (rePassword);
        User_Form.add (passwordText);
        User_Form.add (rePasswordText);
        User_Form.add (previousPassword);
        User_Form.add (previousPasswordInput);

        dateOfIssue1=new JLabel("Date of Joining");
        User_Form.add(dateOfIssue1);
        dateOfJoining = new JDateChooser();
        dateOfJoining.getDateEditor().setEnabled(false);
        dateOfJoining.setBounds (210+ xalignD, 200+ yalignD, 170, 30);
        User_Form.add(dateOfJoining);

        password.setBounds (230, 350, 150, 30);
        rePassword.setBounds (230, 390, 150, 30);
        passwordText.setBounds (445, 350, 130, 30);
        rePasswordText.setBounds (445, 390, 130, 30);
        previousPassword.setBounds (230, 310, 130, 25);
        previousPasswordInput.setBounds (445, 310, 130, 25);
        dateOfIssue1.setBounds (230, 430, 100, 30);
        dateOfJoining.setBounds(445, 430, 130, 30);



        Border etchedBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);


        TitledBorder titledBorder = BorderFactory.createTitledBorder(etchedBorder, "User Information");
        titledBorder.setTitleColor(Color.BLACK);
        Font labelFont = new Font("Arial", Font.BOLD, 18);
        titledBorder.setTitleJustification(TitledBorder.CENTER);
        titledBorder.setTitleFont(labelFont);

        User_Form.setBorder(titledBorder);



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

}
