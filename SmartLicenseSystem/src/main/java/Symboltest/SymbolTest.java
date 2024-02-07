package Symboltest;

import driverForm.DrivingInfo;
import DataBase.MongoDataBase;
import org.bson.Document;
import org.bson.types.Binary;
import users.UserPannel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class SymbolTest {

    private JFrame mainFrame;

    private JPanel testArea;
    private JRadioButton option1Label;
    private JRadioButton option2Label;
    private JRadioButton option3Label;
    private JRadioButton option4Label;

    private ButtonGroup optionCheck;
    private JButton submitButton;
    private JButton nextButton;
    private JButton prevButton;
    private JPanel sysmbolTest;
    private JLabel questionNoLabel;
    private JLabel timeLabel;
    private JLabel questionLabel;
    private JLabel symbolLabel;
    private LocalTime startTime;
    HashMap<Integer, Boolean> testCheck = new HashMap<>();
    int score=0;
    int questionNo=0;
    private int timeAllow = 120;
    int xalign=-40, yalign =-10;
    String correctOption="";
    Document[] questions;
    String selectedOption="";
    String cnic;
    Document userInfo;

    JButton checkButton;
    JTextField cnicInput;
    JLabel cnicLabel;
    JLabel message;
    String date;
    JButton backButton;
    public SymbolTest(){
        iniGUI();

    }


    void iniGUI(){

        mainFrame=new JFrame();
        mainFrame.setTitle("Symbol Test");

        mainFrame.add(checkUser());



        mainFrame.setSize(760, 449);
        mainFrame.setResizable(false);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);



    }

    private JPanel checkUser() {

        JPanel temp=new JPanel(null);


        checkButton = new JButton ("Check");
        cnicInput = new JTextField (5);
        cnicLabel = new JLabel ("Enter Cnic");
        message = new JLabel ("Already Passed");
        backButton=new JButton("Back");
        backButton.setBorder(new LineBorder(Color.gray, 2, true));

        temp.add (checkButton);
        temp.add (cnicInput);
        temp.add (cnicLabel);
        temp.add (message);
        temp.add(backButton);

        checkButton.setBounds (440, 140, 120, 30);
        cnicInput.setBounds (235, 140, 195, 30);
        cnicLabel.setBounds (120, 140, 100, 25);
        message.setBounds (230, 200, 300, 35);
        backButton.setBounds(570,140,120,30);
        message.setVisible(false);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose();
                new UserPannel();
            }
        });

        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                cnic=cnicInput.getText();

                if(cnic.isEmpty()){
                    JOptionPane.showMessageDialog(mainFrame,"Enter Cnic No","No Input",JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if(isTakeTest()) {
                    JOptionPane.showMessageDialog(mainFrame,"Test is Starting Now","Test",JOptionPane.INFORMATION_MESSAGE);
                    mainFrame.getContentPane().removeAll();
                    fetchQuestions();
                    sysmbolTest = addQuestion();

                    initiallizeTestCheck();
                    mainFrame.add(sysmbolTest);
                    mainFrame.revalidate();
                    mainFrame.repaint();
                    Timer timer = new Timer(1000, new ActionListener() {

                        public void actionPerformed(ActionEvent e) {
                            updateElapsedTime();
                        }
                    });
                    timer.start();
                }
                else{
                    if(score!=0){
                        printDocument();
                    }
                }
            }
        });
        return temp;

    }

    private boolean isTakeTest() {

        MongoDataBase user=new MongoDataBase("Driving_Center","Drivers");
        userInfo=user.readDocument("Cnic",cnic);
        if(userInfo==null){
            message.setText("No Learner License Found  Against this Cnic.");
            message.setVisible(true);
            return false;
        }

        LocalDate dateOfIssue = LocalDate.parse(userInfo.getString("Date of Issue"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        long daysBetween = ChronoUnit.DAYS.between(dateOfIssue, LocalDate.now());
        if (daysBetween < 41) {
            JOptionPane.showMessageDialog(mainFrame, "Days after issuing Learner is " + daysBetween + " days.Cannot GIve Test before 41 Days.");
            return false;
        }
        user=new MongoDataBase("Driving_Center","signTestResult");
        Document userTestInfo=user.readDocument("cnic",cnic);

        if(userTestInfo!=null && (userTestInfo.getInteger("TestMarks")>4)){
            score=userTestInfo.getInteger("TestMarks");
            date=userTestInfo.getString("TestDate");
            String sequence=userTestInfo.getString("scoreSequence");
            for(int i=0;i<10;i++) {
                if (String.valueOf(sequence.charAt(i)).equals("1")) {
                    testCheck.put(i, true);
                } else {
                    testCheck.put(i, false);
                }
            }
            message.setText("Test Already Passed...");
            message.setVisible(true);

            return false;
        }
        else if (userTestInfo!=null && (userTestInfo.getInteger("TestMarks")<=4)) {
            LocalDate date = LocalDate.parse(userTestInfo.getString("TestDate"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            daysBetween = ChronoUnit.DAYS.between(date, LocalDate.now());
            if (daysBetween < 41) {
                JOptionPane.showMessageDialog(mainFrame, "Days after Last Test is " + daysBetween + " days.Cannot GIve Test before 41 Days.");
                return false;
            }

        }

        return true;
    }

    private void fetchQuestions() {

        MongoDataBase q1=new MongoDataBase("Driving_Center","symbolTest");
        questions=new Document[10];
        questions=q1.fetchFirst10Documents();

    }

    private void initiallizeTestCheck() {
        for (int i=0;i<10;i++) {
            testCheck.put(i,false);
        }
    }


    private JPanel addQuestion(){

        JPanel tempTestPanel=new JPanel(null);

        optionCheck=new ButtonGroup();

        Border etchedBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);

        TitledBorder titledBorder = BorderFactory.createTitledBorder(etchedBorder, "Traffic Sign Test");
        titledBorder.setTitleColor(Color.BLACK);
        Font labelFont = new Font("Arial", Font.BOLD, 18);
        titledBorder.setTitleJustification(TitledBorder.CENTER);
        titledBorder.setTitleFont(labelFont);

        tempTestPanel.setBorder(titledBorder);

        questionNoLabel=new JLabel("Question No "+ (questionNo+1));
        Font f1 = new Font("Arial", Font.BOLD, 15);
        questionNoLabel.setFont(f1);
        questionNoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        questionNoLabel.setBounds(100+xalign, 40+ yalign, 290, 65);
        tempTestPanel.add(questionNoLabel);

        timeLabel=new JLabel("Time :-  02:00");
        timeLabel.setFont(f1);
        timeLabel.setBounds(590+xalign, 60+ yalign, 95, 25);
        tempTestPanel.add(timeLabel);

        questionLabel=new JLabel(String.valueOf(questions[questionNo].get("Question")));
        Font f2 = new Font("Arial", Font.BOLD, 13);
        questionLabel.setFont(f1);
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        questionLabel.setBounds(95+xalign, 120+ yalign, 290, 40);
        tempTestPanel.add(questionLabel);

        symbolLabel = new JLabel();
        setImage();
        symbolLabel.setBounds(500+xalign, 110+ yalign, 265, 235);
        symbolLabel.setBorder(new LineBorder(Color.gray, 2, true));
        tempTestPanel.add(symbolLabel);


        option1Label=new JRadioButton(String.valueOf(questions[questionNo].get("Option1")));
        option1Label.setBounds(80+xalign, 175+ yalign, 175, 55);
        option1Label.setFont(f2);
        tempTestPanel.add(option1Label);

        option2Label=new JRadioButton(String.valueOf(questions[questionNo].get("Option2")));
        option2Label.setBounds(315+xalign, 175+ yalign, 175, 55);
        option2Label.setFont(f2);
        tempTestPanel.add(option2Label);

        option3Label=new JRadioButton(String.valueOf(questions[questionNo].get("Option3")));
        option3Label.setBounds(80+xalign, 240+ yalign, 175, 55);
        option3Label.setFont(f2);
        tempTestPanel.add(option3Label);

        option4Label=new JRadioButton(String.valueOf(questions[questionNo].get("Option4")));
        option4Label.setBounds(315+xalign, 240+ yalign, 175, 55);
        option4Label.setFont(f2);
        tempTestPanel.add(option4Label);

        correctOption=String.valueOf(questions[questionNo].get("Correct"));

        option1Label.addActionListener(radioButtonListner);
        option2Label.addActionListener(radioButtonListner);
        option3Label.addActionListener(radioButtonListner);
        option4Label.addActionListener(radioButtonListner);

        optionCheck.add(option1Label);
        optionCheck.add(option2Label);
        optionCheck.add(option3Label);
        optionCheck.add(option4Label);

        prevButton=new JButton("Previous");
        prevButton.setFont(f2);
        prevButton.setBounds(105+xalign, 305+ yalign, 155, 40);
        prevButton.setBorder(new LineBorder(Color.gray, 2, true));
        tempTestPanel.add(prevButton);

        nextButton=new JButton("Next");
        nextButton.setFont(f2);
        nextButton.setBounds(285+xalign, 305+ yalign, 155, 40);
        nextButton.setBorder(new LineBorder(Color.gray, 2, true));
        tempTestPanel.add(nextButton);

        submitButton =new JButton("Submit");
        submitButton.setFont(f2);
        submitButton.setBounds(190+xalign, 365+ yalign, 155, 40);
        submitButton.setBorder(new LineBorder(Color.gray, 2, true));
        submitButton.setVisible(false);
        tempTestPanel.add(submitButton);

        nextButton.addActionListener(buttonListner);
        prevButton.addActionListener(buttonListner);
        submitButton.addActionListener(buttonListner);

        return tempTestPanel;
    }

    ActionListener buttonListner = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("Next")){
                if (selectedOption.equals(correctOption)){
                    testCheck.put(questionNo,true);
                }
                else{
                    testCheck.put(questionNo,false);
                }
                questionNo++;
                if (questionNo<10){
                    if(questionNo==9) {
                        nextButton.setEnabled(false);
                        submitButton.setVisible(true);
                    }
                    if(!prevButton.isEnabled()){
                        prevButton.setEnabled(true);
                    }
                }
                updateQuestion();
            }
            else if (e.getActionCommand().equals("Previous")) {

                if(questionNo>=0){
                    if (selectedOption.equals(correctOption)){
                        testCheck.put(questionNo,true);
                    }
                    else{
                        testCheck.put(questionNo,false);
                    }
                    questionNo--;
                    if (questionNo==0){
                        prevButton.setEnabled(false);
                    }
                    if(!nextButton.isEnabled()){
                        nextButton.setEnabled(true);
                    }
                }
                updateQuestion();

            } else if (e.getActionCommand().equals("Submit")) {
                perfomeSubmission();
            }

        }
    };

    private void perfomeSubmission() {
        if (selectedOption.equals(correctOption)){
            testCheck.put(questionNo,true);
        }
        else{
            testCheck.put(questionNo,false);
        }
        for (int i=0;i<10;i++) {
            if(testCheck.get(i)){
                score++;
            }
        }

        mainFrame.dispose();
                saveResult();
        printDocument();
        new UserPannel();
        score=0;
    }

    private void updateQuestion() {

        questionNoLabel.setText("Question No "+ (questionNo+1));

        questionLabel.setText(String.valueOf(questions[questionNo].get("Question")));

        option1Label.setText(String.valueOf(questions[questionNo].get("Option1")));
        option2Label.setText(String.valueOf(questions[questionNo].get("Option2")));
        option3Label.setText(String.valueOf(questions[questionNo].get("Option3")));
        option4Label.setText(String.valueOf(questions[questionNo].get("Option4")));

        correctOption=String.valueOf(questions[questionNo].get("Correct"));


        setImage();

    }

    void setImage(){
        byte[] imageData = MongoDataBase.fetchImage(questions[questionNo].get("Symbol", Binary.class));
        ImageIcon imageIcon = new ImageIcon(imageData);
        Image scaledImage = imageIcon.getImage().getScaledInstance(265, 235, Image.SCALE_SMOOTH);
        ImageIcon scaledImageIcon = new ImageIcon(scaledImage);
        symbolLabel.setIcon(scaledImageIcon);
    }

    private void saveResult() {
        MongoDataBase t1=new MongoDataBase("Driving_Center","signTestResult");
        Map<String, Object> documentMap = new HashMap<>();
        documentMap.put("cnic", cnic);
        documentMap.put("TestMarks", score);
        String seqeuence="";
        for(int i=0;i<10;i++){
            if(testCheck.get(i)) {
                seqeuence+="1";
            }
            else{
                seqeuence+="0";
            }
        }
        documentMap.put("scoreSequence",seqeuence );
        documentMap.put("TestDate", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        t1.createDocument(documentMap);
    }

    ActionListener radioButtonListner = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            selectedOption=e.getActionCommand();
        }
    };


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
            g.drawString("Traffic Signs Test", 200, 50);
            g.setFont(originalFont);
          g.drawString("Name: " + userInfo.getString("Name"), 100, 100);

            byte[] imageData = MongoDataBase.fetchImage(userInfo.get("Image", Binary.class));
            ImageIcon imageIcon = new ImageIcon(imageData);
            Image imaage = imageIcon.getImage();
            Image scaledImage = imaage.getScaledInstance(130, 120, Image.SCALE_SMOOTH);
            ImageIcon scaledImageIcon = new ImageIcon(scaledImage);;

            Icon icon = scaledImageIcon;
            if (icon instanceof ImageIcon) {
                Image image = ((ImageIcon) icon).getImage();
                int imageWidth = 100;
                int imageHeight = 100;
                g.drawImage(image, 480, 50, imageWidth, imageHeight,this);
            } else {
                g.drawString("No image available", 480, 50);
            }
            g.drawString("CNIC: " + cnic, 300, 100);

            g.drawString("Father Name : " + userInfo.getString("Father Name"), 100, 130);
            g.drawString("AGE : " + DrivingInfo.calculateAge(userInfo.getString("Date of Birth")), 300, 130);
            g.drawString("Type : " + userInfo.getString("Type"), 100, 160);
            g.drawString("Date : " + date, 300, 160);

            g.drawLine(100, 210, 500, 210);

            g2d.setStroke(new BasicStroke(3));

            g.setFont(new Font("Arial", Font.BOLD, 20));


            Stroke originalStroke = g2d.getStroke();
            g2d.setStroke(new BasicStroke(3));

            g2d.drawRect(300, 175, 90, 23);
            String  text="Learner No :   "+ userInfo.getString("LearnerNo");
            g.drawString(text, 180, 195);

            g2d.setStroke(originalStroke);



            g.drawString("Test Result", 230, 240);
            if(score>4) {
                g.drawString("Condition : Pass" , 130, 270);
            }
            else {
                g.drawString("Condition : Fail", 130, 270);
            }

            g.drawString("Marks : " +10+"/"+score, 310, 270);
            g.setFont(originalFont);



            int rectangleWidth = 500;
            int rectangleHeight = 30;

            int squareSize = rectangleWidth / 10;

            int startX = 64;
            int startY = 285;

            // Draw the rectangle
//            g.drawRect(startX, startY, rectangleWidth, rectangleHeight);

//             Draw the divided squares
            for (int i = 0; i < 10; i++) {
                int x1 = startX + i * squareSize;
                int y1 = startY;
                g.drawRect(x1, y1, squareSize, squareSize/2);
                Font f1=new Font("Arial",Font.BOLD,18);
                g.setFont(f1);
                g.drawString("Q"+(i+1),x1+13,y1+18);
            }
            for (int i = 0; i < 10; i++) {
                int x1 = startX + i * squareSize;
                int y1 = startY+25;
                g.drawRect(x1, y1, squareSize, squareSize/2);
                if (testCheck.get(i)) {
                    drawTick(g, x1, y1, squareSize, squareSize / 2);
                } else if (!testCheck.get(i)){
                    drawCross(g, x1, y1, squareSize-10, squareSize / 2);
                }
            }

            int x = 150;
            int y = 355;
            int width = 300;
            int height = 40;



            g2d.drawRect(x, y, width, height);

            if(score<5) {


//        g2d.setStroke(originalStroke);
                text = "Retake Test After 41 Days Dated : ";
                Font font = new Font("Arial", Font.PLAIN, 12);
                g.setFont(font);
                g.drawString(text, x + 10, y + 26);
                g2d.drawRect(x + 200, y + 12, 90, 20);
                g.setFont(new Font("Arial", Font.BOLD, 13));
                text = String.valueOf(LocalDate.now().plusDays(41));
                g.drawString(text, x + 200 + 13, y + 26);
            }
            else {
                text = "Congratulations! You can now take Driving Test";
                Font font = new Font("Arial", Font.BOLD, 13);
                g.setFont(font);
                g.drawString(text, x + 3, y + 26);
            }



            return Printable.PAGE_EXISTS;
        }
        private void drawTick(Graphics g, int x, int y, int width, int height) {
            int[] xPoints = {x + width / 4, x + width / 2, x + 3 * width / 4};
            int[] yPoints = {y + height / 2, y + 3 * height / 4, y + height / 4};
            g.drawPolyline(xPoints, yPoints, 3);
        }

        private void drawCross(Graphics g, int x, int y, int width, int height) {
            g.drawLine(x+10, y, x + width-10, y + height);
            g.drawLine(x+10, y + height, x + width-10, y);
        }


        public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
            return false;
        }
    }

    private void updateElapsedTime() {
        if (startTime == null) {
            startTime = LocalTime.now();
        }

        LocalTime currentTime = LocalTime.now();
        int elapsedSeconds = (int) (currentTime.toSecondOfDay() - startTime.toSecondOfDay());
        int remaining = timeAllow - elapsedSeconds;

        if (remaining <= 0) {
            remaining = 0;
            perfomeSubmission();
        }

        int minutes = remaining / 60;
        int seconds = remaining % 60;

        // Update the timeLabel
        timeLabel.setText("Time :-  "+String.format("%02d:%02d", minutes, seconds));
    }

    public static void main(String[] args) {
        SymbolTest us=new SymbolTest();

    }
}
