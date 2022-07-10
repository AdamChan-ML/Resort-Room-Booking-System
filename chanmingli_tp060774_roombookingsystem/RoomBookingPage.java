/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package chanmingli_tp060774_roombookingsystem;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import javax.swing.JOptionPane;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
/**
 *
 * @author Adam Chan
 */
public class RoomBookingPage extends javax.swing.JFrame {
    
    class Gender {
        private String gender = "";
        private String noValue = "";
        public String getGender() {
            rbMale.setActionCommand("Male");
            rbFemale.setActionCommand("Female");
            if(buttonGroup1.getSelection() == null) {

                return noValue;
            }
            else {
                gender = buttonGroup1.getSelection().getActionCommand();
                return gender;  
            }                      
        }
    }
    
    class customerDetails {
        private String custName;
        private String custIC;
        private String gender;
        private String custContact;
        private String custEmail;
        public String[] getCustomerDetails(){
            custName = txtCustomerName.getText();
            custIC = txtCustomerIC.getText();
            Gender custgender = new Gender();
            gender = custgender.getGender();
            custContact = txtContactNumber.getText();
            custEmail = txtEmail.getText();
            String[] custDetails = {custName, custIC, gender, custContact, custEmail};
            return custDetails;
        }        
    }
    
    class Booking {
        private String line = null;
        // Create a method for getting all the booking details
        public String[] getBookingDetails() {
            DefaultTableModel model = (DefaultTableModel)tblAvailableRooms.getModel();
            dates objDate = new dates();
            // Declare a variable to store the dates got from getDate method
            String[] tempDates = objDate.getDate();
            int rowSelection = tblAvailableRooms.getSelectedRow();
            if(rowSelection < 0) {
                JOptionPane.showMessageDialog(null, "Please Select Room in the table to Book!", 
                        "Room Selection Warning", JOptionPane.WARNING_MESSAGE);                 
            }
            else if ((tempDates[0] == null) || (tempDates[1] == null)) {
                JOptionPane.showMessageDialog(null, "Please Select Your Check In & Check Out Date", 
                        "Missing Date Warning", JOptionPane.WARNING_MESSAGE);  
            }
            else if (Integer.parseInt(tempDates[2]) < 1) {
                JOptionPane.showMessageDialog(null, "Invalid Date. Please confirm that your check out date is after your check in date.", 
                        "Invalid Date Warning", JOptionPane.WARNING_MESSAGE);  
            }
            else {
                String roomInfo[] = {model.getValueAt(rowSelection, 0).toString(), model.getValueAt(rowSelection, 1).toString(), 
                model.getValueAt(rowSelection, 2).toString(), "N", tempDates[0], tempDates[1], tempDates[2] };
                return roomInfo;
            }
            String[] end = null;
            return end;
        }
    }
    
    class AllInfo {
        public String getAllInfo(String[] bookingInfo, String[] customerDetails) {
            String Infoline = "";
            for(int x = 0; x < bookingInfo.length; x++) {
                Infoline = Infoline + bookingInfo[x] + ",";
            }
            for(int c = 0; c < customerDetails.length; c++) {
                Infoline = Infoline + customerDetails[c];
                if (c != customerDetails.length - 1) {
                    Infoline = Infoline + ",";
                }
            }
            return Infoline;
        }
    }
    
    class dates {
        public String checkInDate;
        public String checkOutDate;
        long duration;
        public Date checkIn;
        public Date checkOut;
        public String[] getDate() {
            // Check the difference between check in and check out date
            SimpleDateFormat simDate = new SimpleDateFormat("dd-MM-yyyy");
            Date date1 = dcCheckInDate.getDate();
            Date date2 = dcCheckOutDate.getDate();
            if((date1 != null) && (date2 != null)) {
                checkInDate = simDate.format(date1);
                checkOutDate = simDate.format(date2);
                try {
                    checkIn = simDate.parse(checkInDate);
                    checkOut = simDate.parse(checkOutDate);
                    long difference = checkOut.getTime() - checkIn.getTime();
                    duration = TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS);         
                }
                catch(Exception E) {}                                
            }
            else {
                duration = 1000;
            }
            String[] dates = {checkInDate, checkOutDate, String.valueOf(duration)};
            return dates;
        }
        
        public void limitSelectableDays() {
            LocalDate currentDate = LocalDate.now();
            LocalDate limitDate = currentDate.plusDays(7);
            Instant instant = currentDate.atTime(LocalTime.MIDNIGHT).atZone(ZoneId.systemDefault()).toInstant();
            Instant instant2 = limitDate.atTime(LocalTime.MIDNIGHT).atZone(ZoneId.systemDefault()).toInstant();
            Date today = Date.from(instant);
            Date sevenDaysFromNow = Date.from(instant2);
            dcCheckInDate.setMinSelectableDate(today);
            dcCheckInDate.setMaxSelectableDate(sevenDaysFromNow);
            dcCheckOutDate.setMinSelectableDate(today);
        }
    }
    
    /**
     * Creates new form RoomBookingPage
     */
    public RoomBookingPage() {
        initComponents();
        Toolkit toolkit = getToolkit();
        Dimension size = toolkit.getScreenSize();
        setLocation(size.width/2-getWidth()/2,size.height/2-getHeight()/2);
        dates objDate = new dates();
        objDate.limitSelectableDays();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jFrame1 = new javax.swing.JFrame();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        btnHome = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        lblBR = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        lblTypeOfRoom = new javax.swing.JLabel();
        lblCheckInDate = new javax.swing.JLabel();
        lblCheckOutDate = new javax.swing.JLabel();
        cbTypeOfRooms = new javax.swing.JComboBox<>();
        dcCheckOutDate = new com.toedter.calendar.JDateChooser();
        dcCheckInDate = new com.toedter.calendar.JDateChooser();
        txtDuration = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        btnBook = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        lblCustomerName = new javax.swing.JLabel();
        txtCustomerName = new javax.swing.JTextField();
        lblCustomerIC = new javax.swing.JLabel();
        lblGender = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        lblContactNumber = new javax.swing.JLabel();
        rbMale = new javax.swing.JRadioButton();
        rbFemale = new javax.swing.JRadioButton();
        btnClear = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        btnDuration = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAvailableRooms = new javax.swing.JTable();
        txtCustomerIC = new javax.swing.JFormattedTextField();
        txtContactNumber = new javax.swing.JFormattedTextField();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        jPanel1.setPreferredSize(new java.awt.Dimension(911, 542));

        btnHome.setBackground(new java.awt.Color(0, 255, 51));
        btnHome.setFont(new java.awt.Font("Nirmala UI", 1, 12)); // NOI18N
        btnHome.setText("Home");
        btnHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHomeActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 28)); // NOI18N
        jLabel1.setText("Room Booking Page");

        lblBR.setBackground(new java.awt.Color(102, 102, 255));
        lblBR.setFont(new java.awt.Font("Cambria", 3, 12)); // NOI18N
        lblBR.setForeground(new java.awt.Color(255, 255, 255));
        lblBR.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBR.setText("THE BLUEMING RESORT");
        lblBR.setToolTipText("");
        lblBR.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        lblBR.setOpaque(true);

        jSeparator1.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));

        lblTypeOfRoom.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTypeOfRoom.setText("Type of Room: ");

        lblCheckInDate.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblCheckInDate.setText("Check In Date:");

        lblCheckOutDate.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblCheckOutDate.setText("Check Out Date:");

        cbTypeOfRooms.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "None", "Jungle View", "Sea View" }));

        dcCheckInDate.setMinSelectableDate(new java.util.Date(1648573306000L));

        txtDuration.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtDuration.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtDuration.setEnabled(false);
        txtDuration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDurationActionPerformed(evt);
            }
        });

        btnSearch.setText("SEARCH");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        btnBook.setText("BOOK");
        btnBook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBookActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Cambria", 1, 16)); // NOI18N
        jLabel2.setText("Room Details");

        lblCustomerName.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblCustomerName.setText("Customer Name:");

        txtCustomerName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCustomerNameActionPerformed(evt);
            }
        });

        lblCustomerIC.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblCustomerIC.setText("Customer IC/Passport:");

        lblGender.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblGender.setText("Gender:");

        lblEmail.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblEmail.setText("Email:");

        txtEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailActionPerformed(evt);
            }
        });
        txtEmail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEmailKeyTyped(evt);
            }
        });

        lblContactNumber.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblContactNumber.setText("Contact Number:");

        buttonGroup1.add(rbMale);
        rbMale.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        rbMale.setText("Male");

        buttonGroup1.add(rbFemale);
        rbFemale.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        rbFemale.setText("Female");
        rbFemale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbFemaleActionPerformed(evt);
            }
        });

        btnClear.setText("CLEAR");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Cambria", 1, 16)); // NOI18N
        jLabel14.setText("Customer Details");

        jSeparator2.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator2.setForeground(new java.awt.Color(0, 0, 0));

        jSeparator3.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator3.setForeground(new java.awt.Color(0, 0, 0));

        jLabel4.setFont(new java.awt.Font("Cambria", 1, 16)); // NOI18N
        jLabel4.setText("Available Rooms:");

        btnDuration.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnDuration.setText("Duration:");
        btnDuration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDurationActionPerformed(evt);
            }
        });

        tblAvailableRooms.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Room ID", "Type of Room", "Price", "Availability"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblAvailableRooms);

        try {
            txtCustomerIC.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("######-##-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtCustomerIC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCustomerICKeyTyped(evt);
            }
        });

        try {
            txtContactNumber.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###-#### ####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtContactNumber.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtContactNumberKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(lblBR, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(163, 163, 163)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnHome)
                .addGap(21, 21, 21))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(84, 84, 84)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblTypeOfRoom)
                                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(btnDuration, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtDuration, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)))
                                .addGap(156, 156, 156)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                            .addComponent(lblEmail)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                            .addComponent(lblContactNumber)
                                            .addGap(40, 40, 40)
                                            .addComponent(txtContactNumber))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(lblCustomerIC)
                                                .addComponent(lblCustomerName))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                    .addComponent(rbMale)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(rbFemale))
                                                .addComponent(txtCustomerName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                                                .addComponent(txtCustomerIC, javax.swing.GroupLayout.Alignment.LEADING))))
                                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(lblCheckOutDate, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(dcCheckOutDate, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(lblCheckInDate, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(dcCheckInDate, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cbTypeOfRooms, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(157, 157, 157)
                                .addComponent(lblGender))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(257, 257, 257)
                        .addComponent(btnClear)
                        .addGap(42, 42, 42)
                        .addComponent(btnSearch)
                        .addGap(37, 37, 37)
                        .addComponent(btnBook))
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 789, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(88, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 426, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(267, 267, 267))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblBR, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnHome))
                        .addGap(0, 14, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel14))
                .addGap(2, 2, 2)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTypeOfRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbTypeOfRooms, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dcCheckInDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblCustomerIC, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblCheckInDate, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtCustomerIC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCheckOutDate, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(rbMale, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(rbFemale))
                            .addComponent(lblGender, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblContactNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtContactNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(dcCheckOutDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDuration, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDuration))))
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSearch)
                    .addComponent(btnBook)
                    .addComponent(btnClear))
                .addGap(21, 21, 21))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 967, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomeActionPerformed
        // Return to Staff Home Page when staffs clicked this button
        HomePage homepage = new HomePage();
        this.dispose();
        homepage.show();
    }//GEN-LAST:event_btnHomeActionPerformed

    private void txtDurationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDurationActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDurationActionPerformed

    private void txtCustomerNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCustomerNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCustomerNameActionPerformed

    private void txtEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailActionPerformed

    private void rbFemaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbFemaleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbFemaleActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // Open the booking text file
        DefaultTableModel model = (DefaultTableModel)tblAvailableRooms.getModel();
        model.setRowCount(0);
        
       File bookingFile = new File("booking.txt");
       try {
           BufferedReader br = new BufferedReader(new FileReader(bookingFile));
           //Read Lines from the text file
           String infoLines = br.readLine();
           String columns[] = infoLines.split(",");
           String[] columnsSelected = new String[4];
            //Select the columns to display in the table
           System.arraycopy(columns, 0, columnsSelected, 0, 4);
           model.setColumnIdentifiers(columnsSelected);         
           Object[] bookingLines = br.lines().toArray();
           String roomType = cbTypeOfRooms.getSelectedItem().toString();
           for(int i = 0;i < bookingLines.length; i++) {  
               String line = bookingLines[i].toString().trim();
               String[] dataRows = line.split(",");
               // Display the rooms that macthes the selected type of room by users
               if(dataRows[1].equals(roomType)) {
                   // Validate if the room is available or not
                   if(dataRows[3].equals("Y")){
                       model.addRow(dataRows);
                   }                   
               }
           }
           br.close();
        }
       catch(IOException Ex) {
           JOptionPane.showMessageDialog(this, "Couldn't Find the Booking Text File", "Error", JOptionPane.ERROR_MESSAGE);
       }
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBookActionPerformed
        int confirmBooking = JOptionPane.showConfirmDialog(null, "Do you want to confirm your booking?", 
                "Room Booking Confirmation", JOptionPane.YES_NO_CANCEL_OPTION);
        if(confirmBooking == JOptionPane.YES_OPTION){
            // Create an object of Booking class
            Booking booking = new Booking();
            // Create an object of AllInfo class
            AllInfo allinfo = new AllInfo();
            // Create an object of customerDetails class
            customerDetails objCustInfo = new customerDetails();
            // Declare a string to store all booking details
            String[] bookingDetails = booking.getBookingDetails();
            // Declare a string to store all customer details
            String[] customerDetails = objCustInfo.getCustomerDetails();
            // Ensure all values are not null or blank values
            if((!customerDetails[0].equals("")) && !customerDetails[1].replaceAll("\\s","").equals("--") && !customerDetails[2].equals("") && 
                    !customerDetails[3].trim().equals("-") && !customerDetails[4].equals("")) {
                if(bookingDetails != null) {
                    String format = ("^(.+)@(.+)$");
                    Pattern emailFormat = Pattern.compile(format);
                    if(emailFormat.matcher(customerDetails[4]).find()) {
                        // Passing booking details & customer details into the method for compilation
                        String bookCustInfo = allinfo.getAllInfo(bookingDetails, customerDetails);
                        PaymentPage objPaymentPage = new PaymentPage(bookCustInfo);
                        this.dispose();
                        objPaymentPage.show();
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Please Enter Your Email in the Format of example@gmail.com", 
                                "Invalid Email", JOptionPane.WARNING_MESSAGE);
                    }
                }                
            }
            else {
                JOptionPane.showMessageDialog(null, "Please Fill In All the Customer Details", 
                        "Incomplete Customer Details Warning", JOptionPane.WARNING_MESSAGE);
            }          
        }         
    }//GEN-LAST:event_btnBookActionPerformed

    private void btnDurationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDurationActionPerformed
        // Create an object of getDate to display the difference between check in and check out date
        dates date = new dates();
        String[] bookingDates = date.getDate();
        int duration = Integer.parseInt(bookingDates[2]);
        if((duration < 8) && (duration > 0)) {
            txtDuration.setText(String.valueOf(duration));
        }
        else if(duration < 0) {
            JOptionPane.showMessageDialog(null, "Invalid Date. Please confirm that your check out date is after your check in date.", "Invalid Date Warning", JOptionPane.WARNING_MESSAGE);
        }
        else if(duration == 1000) {
            JOptionPane.showMessageDialog(null, "Please Select Your Check In & Check Out Date", "Missing Date Warning", JOptionPane.WARNING_MESSAGE);  
        }
        else {
            JOptionPane.showMessageDialog(null, "Unfortunately, The Blueming Resort ONLY allows at most 7-days room booking. Please RESELECT your check in and check out date to book the room successfully", "Room Booking Duration Warning", JOptionPane.WARNING_MESSAGE);
        }  

    }//GEN-LAST:event_btnDurationActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // Clear all text fields or inputs in the form
        cbTypeOfRooms.setSelectedIndex(0);
        dcCheckInDate.setCalendar(null);
        dcCheckOutDate.setCalendar(null);
        buttonGroup1.clearSelection();
        txtCustomerName.setText("");
        txtCustomerIC.setText("");
        txtContactNumber.setText("");
        txtEmail.setText("");
        txtDuration.setText("");
        tblAvailableRooms.clearSelection();
    }//GEN-LAST:event_btnClearActionPerformed

    private void txtEmailKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmailKeyTyped

    }//GEN-LAST:event_txtEmailKeyTyped

    private void txtCustomerICKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCustomerICKeyTyped
        // Ensure Numeric Input from Users for Customer IC
        char c = evt.getKeyChar();
        if(!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || c == KeyEvent.VK_DELETE)) {
            JOptionPane.showMessageDialog(null, "Please Enter Only Numeric Number", "Invalid Input Warning", JOptionPane.WARNING_MESSAGE);
            evt.consume();
        }
    }//GEN-LAST:event_txtCustomerICKeyTyped

    private void txtContactNumberKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtContactNumberKeyTyped
        // Ensure Numeric Input from Users for Customer Contact Number
        char c = evt.getKeyChar();
        if(!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || c == KeyEvent.VK_DELETE)) {
            JOptionPane.showMessageDialog(null, "Please Enter Only Numeric Number", "Invalid Input Warning", JOptionPane.WARNING_MESSAGE);
            evt.consume();
        }
    }//GEN-LAST:event_txtContactNumberKeyTyped

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(RoomBookingPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RoomBookingPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RoomBookingPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RoomBookingPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RoomBookingPage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBook;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDuration;
    private javax.swing.JButton btnHome;
    private javax.swing.JButton btnSearch;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cbTypeOfRooms;
    private com.toedter.calendar.JDateChooser dcCheckInDate;
    private com.toedter.calendar.JDateChooser dcCheckOutDate;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel lblBR;
    private javax.swing.JLabel lblCheckInDate;
    private javax.swing.JLabel lblCheckOutDate;
    private javax.swing.JLabel lblContactNumber;
    private javax.swing.JLabel lblCustomerIC;
    private javax.swing.JLabel lblCustomerName;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblGender;
    private javax.swing.JLabel lblTypeOfRoom;
    private javax.swing.JRadioButton rbFemale;
    private javax.swing.JRadioButton rbMale;
    private javax.swing.JTable tblAvailableRooms;
    private javax.swing.JFormattedTextField txtContactNumber;
    private javax.swing.JFormattedTextField txtCustomerIC;
    private javax.swing.JTextField txtCustomerName;
    private javax.swing.JTextField txtDuration;
    private javax.swing.JTextField txtEmail;
    // End of variables declaration//GEN-END:variables
}
