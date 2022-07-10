/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package chanmingli_tp060774_roombookingsystem;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.*;
import java.text.ParseException;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.UIManager;
import javax.swing.table.TableRowSorter;
/**
 *
 * @author Adam Chan
 */
public class ManageBookingPage extends javax.swing.JFrame {

    class BookedRooms {
        public String roomID;
        private String line;
        int selectRow = tblBookedRoom.getSelectedRow();
        DefaultTableModel model = (DefaultTableModel)tblBookedRoom.getModel();
        File bookedFile = new File("booking.txt");
        public void viewBookedRooms() {            
            model.setRowCount(0);
            try {
                BufferedReader br = new BufferedReader(new FileReader(bookedFile));
               //Read Lines from the text file
               String infoLines = br.readLine();
               String columns[] = infoLines.split(",");
               String[] columnsSelected = new String[4];
               //Select the columns to display in the table
               columnsSelected[0] = columns[0];
               columnsSelected[1] = columns[4];
               columnsSelected[2] = columns[5];
               columnsSelected[3] = columns[7];
               model.setColumnIdentifiers(columnsSelected);         
               Object[] bookingLines = br.lines().toArray();
               for(int i = 0;i < bookingLines.length; i++) {  
                   String line = bookingLines[i].toString().trim();
                   String[] dataRows = line.split(",");
                   if(dataRows[3].equals("N")){
                   String[] dataSelected = new String[4];
                       dataSelected[0] = dataRows[0];
                       dataSelected[1] = dataRows[4];
                       dataSelected[2] = dataRows[5];
                       dataSelected[3] = dataRows[7];               
                       model.addRow(dataSelected);
                   }               
                }
               br.close();               
            }
            catch(IOException E) {}  
        }
        
        public void previewData() {
            Charges objCharges = new Charges();
            try {
                txtCustomerName.setText(tblBookedRoom.getValueAt(selectRow, 3).toString());
                roomID = tblBookedRoom.getValueAt(selectRow, 0).toString();
                txtRoomID.setText(roomID);            
                Date checkIn = new SimpleDateFormat("dd-MM-yyyy").parse((String)tblBookedRoom.getValueAt(selectRow, 1));
                dcCheckInDate.setDate(checkIn);
                Date checkOut = new SimpleDateFormat("dd-MM-yyyy").parse((String)tblBookedRoom.getValueAt(selectRow, 2));
                dcCheckOutDate.setDate(checkOut);
            } catch (ParseException ex) {
                Logger.getLogger(ManageBookingPage.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                BufferedReader br = new BufferedReader(new FileReader(bookedFile));       
                Object[] bookingLines = br.lines().toArray();
                for(int i = 0;i < bookingLines.length; i++) {  
                    String line = bookingLines[i].toString().trim();
                    String[] dataRows = line.split(",");
                    if(dataRows[0].equals(roomID)){
                        txtTypeOfRoom.setText(dataRows[1]);
                        if(dataRows[9].equals("Male")) {
                            cbGender.setSelectedIndex(0);
                        }
                        else {
                            cbGender.setSelectedIndex(1);
                        }
                        txtDuration.setText(dataRows[6]);
                        txtCustomerIC.setText(dataRows[8]);
                        txtContactNumber.setText(dataRows[10]);
                        txtEmail.setText(dataRows[11]);
                        txtPaymentMethod.setText(dataRows[12]);
                        objCharges.setDuration(dataRows[6]);
                        lblRoomCharges.setText(objCharges.calculateRoomCharges());
                        lblTourismTax.setText(objCharges.calculateTourismTax());
                        lblServiceTax.setText(objCharges.calculateServiceTax());
                        lblTotalPrice.setText(objCharges.getTotalPrice());
                    }               
                 }
                br.close();  
            }
            catch(IOException e) {}
        }
        
        // Create a method to delete data from text file
        public void deleteData() {
            String renewData = "";
            // Get the room id of the selected table row
            String ID = model.getValueAt(selectRow, 0).toString();
            try {
                BufferedReader br = new BufferedReader(new FileReader(bookedFile));
                // Create a temporary file to store the data
                File tempfile = new File("temp.txt");
                BufferedWriter bw = new BufferedWriter(new FileWriter(tempfile, true));
                PrintWriter pw = new PrintWriter(bw);

                while((line = br.readLine()) != null) {
                    String[] data = line.split(",");   
                    // Add line if the room id does not matches
                    if(!data[0].equals(ID)) {
                        pw.println(line);
                    }
                    else {
                        // Change the deleted data with a "Y" availability
                        renewData = data[0] + "," + data[1] + "," + data[2] + ",Y,";  
                    }                                     
                }
                // Print the renewed data into the temp text file
                pw.println(renewData);
                br.close();
                pw.close(); 
                bw.close();
                //replace the original file with the temp file
                bookedFile.delete();
                tempfile.renameTo(bookedFile);
            }
            catch(IOException ex) {
                ex.printStackTrace();
            }
        }             
    }
    
    // Create a new class that inherits from BookedRooms 
    class Function extends BookedRooms {
        public String getModifiedData() {
            dates objDate = new dates();
            String[] dates = objDate.getDate();
            // Check the validity of check in and check out date and duration
            if (Integer.parseInt(dates[2]) > 7) {
                JOptionPane.showMessageDialog(null, "Unfortunately, The Blueming Resort ONLY allows at most 7-days room booking. "
                        + "Please RESELECT your check in and check out date to book the room successfully",
                        "Room Booking Duration Warning", JOptionPane.WARNING_MESSAGE);  
            }
            else if (Integer.parseInt(dates[2]) < 1) {
                JOptionPane.showMessageDialog(null, "Invalid Date. Please confirm that your check out date is after your check in date.",
                        "Invalid Date Warning", JOptionPane.WARNING_MESSAGE);  
            }            
            else {
                // Ensure all the input fields have a valid input
                if((!txtCustomerName.getText().equals("") && !txtEmail.getText().equals("") && !txtCustomerIC.getText().replaceAll("\\s","").equals("--")) &&
                        !txtContactNumber.getText().trim().equals("-")) {
                    // Declare a string with all the information comma-seperated
                    String modifiedInfo = txtRoomID.getText() + "," + txtTypeOfRoom.getText() + "," + "350.00," + "N," + dates[0] + "," +
                    dates[1] + "," + dates[2] + "," + txtCustomerName.getText() + "," + txtCustomerIC.getText() + "," + cbGender.getSelectedItem().toString()
                    + "," + txtContactNumber.getText() + "," + txtEmail.getText() + "," + txtPaymentMethod.getText();
                    return modifiedInfo;
                }
                else {
                    JOptionPane.showMessageDialog(null,"Please Enter All Modified Details or Click the Data In Table Once More",
                            "Missing Modified Data Warning",JOptionPane.WARNING_MESSAGE);
                }                
            }            
            String end = null;
            return end;
        }
        
        public void Search(String search) {
            TableRowSorter<DefaultTableModel> tableRowSorter = new TableRowSorter<>(model);
            tblBookedRoom.setRowSorter(tableRowSorter);
            tableRowSorter.setRowFilter(RowFilter.regexFilter(search));            
        }
                       
        public void enableInputs(boolean choice) {
            txtCustomerName.setEnabled(choice);
            cbGender.setEnabled(choice);
            txtCustomerIC.setEnabled(choice);
            txtEmail.setEnabled(choice);
            txtContactNumber.setEnabled(choice);
            dcCheckInDate.setEnabled(choice);
            dcCheckOutDate.setEnabled(choice);
        }
        
        // A method to save all modified data to text file        
        public void saveChanges(String modifiedData) {
            String dataLine;
            String[] arrModifiedData = modifiedData.split(",");
            deleteData();
            try {
                File bookingfile = new File("booking.txt");
                BufferedReader br = new BufferedReader(new FileReader(bookingfile));
                
                File tempfile = new File("temp.txt");
                BufferedWriter bw = new BufferedWriter(new FileWriter(tempfile, true));
                PrintWriter pw = new PrintWriter(bw);
                
                while((dataLine = br.readLine()) != null) {
                    String[] data = dataLine.split(",");
                    // Find the room id that matches the room id of the modified data
                    if(!data[0].equals(arrModifiedData[0])) {
                        pw.println(dataLine);
                    }
                }
                pw.println(modifiedData);
                br.close();
                pw.close(); 
                bw.close();
                // Replace the original file with temporary file and rename it
                bookingfile.delete();
                tempfile.renameTo(bookingfile);
            }
            catch(IOException ex) {
                ex.printStackTrace();
            }            
        }
        
        public void checkOut(int selectedRow) {
            String checkOutInfo = getModifiedData();
            try {
                File bookingHistoryFile = new File("bookingHistory.txt");
                BufferedWriter bw = new BufferedWriter(new FileWriter(bookingHistoryFile, true));
                bw.write(checkOutInfo);  
                bw.newLine();
                bw.close();
            }
            catch(IOException E) {}
            deleteData();
        }
    }
    
    class Charges {
        private int roomChargePerNight = 350;
        private int tourismChargePerNight = 10;
        private int roomCharges;
        private int tourismTax;
        private int serviceTax;
        private int totalPrice;
        private int duration;
        public void setDuration(String dur) {
            String strDuration = dur;
            duration = Integer.parseInt(strDuration);
        }

        public String calculateRoomCharges() {            
            roomCharges = roomChargePerNight * duration;
            return Integer.toString(roomCharges);
        }

        public String calculateTourismTax() {            
            tourismTax = tourismChargePerNight * duration;
            return Integer.toString(tourismTax);
        }

        public String calculateServiceTax() {
            serviceTax = (int)(roomCharges * (10/100.0f));
            return Integer.toString(serviceTax);
        }

        public String getTotalPrice() {
            totalPrice = roomCharges + tourismTax + serviceTax;
            return Integer.toString(totalPrice);
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
     * Creates new form ManageBookingPage
     */
    public ManageBookingPage() {
        initComponents();
        Toolkit toolkit = getToolkit();
        Dimension size = toolkit.getScreenSize();
        setLocation(size.width/2-getWidth()/2,size.height/2-getHeight()/2);
        BookedRooms objBookedRooms = new BookedRooms();
        objBookedRooms.viewBookedRooms();
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

        lblBR = new javax.swing.JLabel();
        btnHome = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblBR1 = new javax.swing.JLabel();
        btnHome1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblBookedRoom = new javax.swing.JTable();
        btnDelete = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        btnSaveChanges = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnModify = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        btnCheckOut = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        lblCustomerIC = new javax.swing.JLabel();
        lblGender = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        lblContactNumber = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        lblCustomerName = new javax.swing.JLabel();
        txtCustomerName = new javax.swing.JTextField();
        lblNumberOfRooms = new javax.swing.JLabel();
        lblCheckInDate = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        lblCheckOutDate = new javax.swing.JLabel();
        dcCheckOutDate = new com.toedter.calendar.JDateChooser();
        dcCheckInDate = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        lblTypeOfRoom = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        txtEmail = new javax.swing.JTextField();
        txtRoomID = new javax.swing.JTextField();
        lblCheckInDate1 = new javax.swing.JLabel();
        txtDuration = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        cbGender = new javax.swing.JComboBox<>();
        lblNumberOfRooms1 = new javax.swing.JLabel();
        lblNumberOfRooms2 = new javax.swing.JLabel();
        lblNumberOfRooms3 = new javax.swing.JLabel();
        lblRoomCharges = new javax.swing.JLabel();
        lblTourismTax = new javax.swing.JLabel();
        lblServiceTax = new javax.swing.JLabel();
        lblNumberOfRooms7 = new javax.swing.JLabel();
        lblTotalPrice = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtTypeOfRoom = new javax.swing.JTextField();
        txtCustomerIC = new javax.swing.JFormattedTextField();
        txtContactNumber = new javax.swing.JFormattedTextField();
        jLabel16 = new javax.swing.JLabel();
        lblNumberOfRooms4 = new javax.swing.JLabel();
        txtPaymentMethod = new javax.swing.JTextField();

        lblBR.setBackground(new java.awt.Color(102, 102, 255));
        lblBR.setFont(new java.awt.Font("Cambria", 3, 12)); // NOI18N
        lblBR.setForeground(new java.awt.Color(255, 255, 255));
        lblBR.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBR.setText("THE BLUEMING RESORT");
        lblBR.setToolTipText("");
        lblBR.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        lblBR.setOpaque(true);

        btnHome.setBackground(new java.awt.Color(0, 255, 51));
        btnHome.setFont(new java.awt.Font("Nirmala UI", 1, 12)); // NOI18N
        btnHome.setText("Home");
        btnHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHomeActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));
        jPanel1.setPreferredSize(new java.awt.Dimension(911, 542));
        jPanel1.setRequestFocusEnabled(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 28)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Manage Booking / Check Out Page");

        lblBR1.setBackground(new java.awt.Color(102, 102, 255));
        lblBR1.setFont(new java.awt.Font("Cambria", 3, 12)); // NOI18N
        lblBR1.setForeground(new java.awt.Color(255, 255, 255));
        lblBR1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBR1.setText("THE BLUEMING RESORT");
        lblBR1.setToolTipText("");
        lblBR1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 2, true));
        lblBR1.setOpaque(true);

        btnHome1.setBackground(new java.awt.Color(0, 255, 51));
        btnHome1.setFont(new java.awt.Font("Nirmala UI", 1, 12)); // NOI18N
        btnHome1.setText("Home");
        btnHome1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHome1ActionPerformed(evt);
            }
        });

        jSeparator1.setForeground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 204, 204));
        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        tblBookedRoom.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Room ID", "Check In Date", "Check Out Date", "Customer Name"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblBookedRoom.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                tblBookedRoomAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        tblBookedRoom.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBookedRoomMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblBookedRoom);

        btnDelete.setText("DELETE");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Booked Room: ");

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/chanmingli_tp060774_roombookingsystem/magnifier_image.png"))); // NOI18N
        jLabel3.setText("Search: ");

        txtSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchActionPerformed(evt);
            }
        });

        btnSaveChanges.setText("SAVE CHANGES");
        btnSaveChanges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveChangesActionPerformed(evt);
            }
        });

        btnReset.setText("RESET");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        btnModify.setText("MODIFY");
        btnModify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModifyActionPerformed(evt);
            }
        });

        jSeparator4.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator4.setForeground(new java.awt.Color(0, 0, 0));

        btnCheckOut.setBackground(new java.awt.Color(51, 51, 51));
        btnCheckOut.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCheckOut.setForeground(new java.awt.Color(255, 255, 255));
        btnCheckOut.setText("CHECK OUT");
        btnCheckOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckOutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(114, 114, 114)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(18, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(btnModify)
                                .addGap(18, 18, 18)
                                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnSaveChanges))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(110, 110, 110)
                                .addComponent(btnCheckOut, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnModify)
                    .addComponent(btnDelete)
                    .addComponent(btnReset)
                    .addComponent(btnSaveChanges))
                .addGap(33, 33, 33)
                .addComponent(btnCheckOut, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43))
        );

        jPanel3.setBackground(new java.awt.Color(204, 255, 204));
        jPanel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        lblCustomerIC.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblCustomerIC.setText("Customer IC/Passport:");

        lblGender.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblGender.setText("Gender:");

        lblEmail.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblEmail.setText("Email:");

        lblContactNumber.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblContactNumber.setText("Contact Number:");

        jLabel14.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        jLabel14.setText("Customer Details");

        lblCustomerName.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblCustomerName.setText("Customer Name:");

        txtCustomerName.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        txtCustomerName.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtCustomerName.setEnabled(false);
        txtCustomerName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCustomerNameActionPerformed(evt);
            }
        });

        lblNumberOfRooms.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNumberOfRooms.setText("Duration:");

        lblCheckInDate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblCheckInDate.setText("Check In Date:");

        jSeparator2.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator2.setForeground(new java.awt.Color(0, 0, 0));

        lblCheckOutDate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblCheckOutDate.setText("Check Out Date:");

        dcCheckOutDate.setBackground(new java.awt.Color(255, 255, 255));
        dcCheckOutDate.setEnabled(false);
        dcCheckOutDate.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N

        dcCheckInDate.setBackground(new java.awt.Color(255, 255, 255));
        dcCheckInDate.setForeground(new java.awt.Color(51, 51, 51));
        dcCheckInDate.setToolTipText("");
        dcCheckInDate.setEnabled(false);
        dcCheckInDate.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        jLabel5.setText("Room Details");

        lblTypeOfRoom.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTypeOfRoom.setText("Type of Room: ");

        jSeparator5.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator5.setForeground(new java.awt.Color(0, 0, 0));

        txtEmail.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        txtEmail.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtEmail.setEnabled(false);
        txtEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailActionPerformed(evt);
            }
        });

        txtRoomID.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        txtRoomID.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtRoomID.setEnabled(false);
        txtRoomID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRoomIDActionPerformed(evt);
            }
        });

        lblCheckInDate1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblCheckInDate1.setText("Room ID:");

        txtDuration.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        txtDuration.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtDuration.setEnabled(false);
        txtDuration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDurationActionPerformed(evt);
            }
        });

        jSeparator3.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator3.setForeground(new java.awt.Color(0, 0, 0));

        cbGender.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        cbGender.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Male", "Female" }));
        cbGender.setEnabled(false);
        cbGender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbGenderActionPerformed(evt);
            }
        });

        lblNumberOfRooms1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNumberOfRooms1.setText("Service Tax      : RM");

        lblNumberOfRooms2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNumberOfRooms2.setText("Tourism Tax    : RM");

        lblNumberOfRooms3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNumberOfRooms3.setText("Room Charges: RM ");

        lblRoomCharges.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        lblRoomCharges.setText("RM");

        lblTourismTax.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        lblTourismTax.setText("RM");

        lblServiceTax.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        lblServiceTax.setText("RM");

        lblNumberOfRooms7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblNumberOfRooms7.setText("Total Price: RM ");

        lblTotalPrice.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        lblTotalPrice.setText("RM");

        jLabel15.setFont(new java.awt.Font("Cambria", 1, 16)); // NOI18N
        jLabel15.setText("PREVIEW DATA");

        txtTypeOfRoom.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        txtTypeOfRoom.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtTypeOfRoom.setEnabled(false);
        txtTypeOfRoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTypeOfRoomActionPerformed(evt);
            }
        });

        try {
            txtCustomerIC.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("######-##-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtCustomerIC.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtCustomerIC.setEnabled(false);
        txtCustomerIC.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
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
        txtContactNumber.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtContactNumber.setEnabled(false);
        txtContactNumber.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        txtContactNumber.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtContactNumberKeyTyped(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        jLabel16.setText("Payment Details");

        lblNumberOfRooms4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblNumberOfRooms4.setText("Payment Method: ");

        txtPaymentMethod.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        txtPaymentMethod.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtPaymentMethod.setEnabled(false);
        txtPaymentMethod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPaymentMethodActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(183, 183, 183)
                                .addComponent(jLabel15))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel16)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator5)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(lblNumberOfRooms2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblNumberOfRooms3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblNumberOfRooms1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(lblServiceTax, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lblNumberOfRooms7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblTotalPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblRoomCharges, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lblTourismTax, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, Short.MAX_VALUE))))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(lblCustomerName)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(lblCustomerIC)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtCustomerIC)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(lblGender)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cbGender, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(lblEmail)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(jSeparator2)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(lblCheckInDate, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(dcCheckInDate, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(lblCheckOutDate, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(dcCheckOutDate, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(lblNumberOfRooms)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtDuration, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(181, 181, 181))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                        .addComponent(lblContactNumber)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtContactNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                        .addComponent(lblCheckInDate1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtRoomID, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(29, 29, 29)
                                        .addComponent(lblTypeOfRoom)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtTypeOfRoom, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblNumberOfRooms4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPaymentMethod, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblGender, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbGender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCustomerIC, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCustomerIC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblContactNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtContactNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addGap(2, 2, 2)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCheckInDate1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtRoomID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTypeOfRoom)
                    .addComponent(txtTypeOfRoom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCheckInDate, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dcCheckInDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(lblCheckOutDate, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblNumberOfRooms, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDuration, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(dcCheckOutDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNumberOfRooms4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPaymentMethod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNumberOfRooms3, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblRoomCharges, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNumberOfRooms2, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTourismTax, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNumberOfRooms1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblServiceTax, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNumberOfRooms7, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotalPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(17, 17, 17))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(lblBR1, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(88, 88, 88)
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 538, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(btnHome1)
                .addGap(36, 36, 36))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHome1)
                    .addComponent(lblBR1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1008, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 645, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnHomeActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Delete the Rows Choosen in the Table from the text file
        int confirmDelete = JOptionPane.showConfirmDialog(null, "Are you sure to delete the booking data selected?", "Booked Room Deletion", JOptionPane.YES_NO_CANCEL_OPTION);
        if(confirmDelete == JOptionPane.YES_OPTION){
            BookedRooms objBookedRooms = new BookedRooms();
            objBookedRooms.deleteData();
            objBookedRooms.viewBookedRooms();
            JOptionPane.showMessageDialog(null,"Booked Room Data Delete Successfully", "Booked Room Data Deletion", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void tblBookedRoomMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBookedRoomMouseClicked
        BookedRooms objInfo = new BookedRooms();
        objInfo.previewData();
    }//GEN-LAST:event_tblBookedRoomMouseClicked

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // Clear All Data in the Preview Data Section
        txtTypeOfRoom.setText("");
        cbGender.setSelectedIndex(0);
        dcCheckInDate.setCalendar(null);
        dcCheckOutDate.setCalendar(null);
        txtCustomerName.setText("");
        txtCustomerIC.setText("");
        txtContactNumber.setText("");
        txtEmail.setText("");
        txtDuration.setText("");
        txtRoomID.setText("");
        lblRoomCharges.setText("");
        lblTourismTax.setText("");
        lblServiceTax.setText("");
        lblTotalPrice.setText("");
        tblBookedRoom.clearSelection();
        
    }//GEN-LAST:event_btnResetActionPerformed

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
        Function objFunc = new Function();
        objFunc.Search(txtSearch.getText());
    }//GEN-LAST:event_txtSearchActionPerformed

    private void tblBookedRoomAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_tblBookedRoomAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_tblBookedRoomAncestorAdded

    private void btnModifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModifyActionPerformed
        // Call the Modify method from the Function class
        Function objFunc = new Function();
        // Ensure all the input fields have valid inputs
        if((!txtCustomerName.getText().equals("") && !txtCustomerIC.getText().equals("") && !txtEmail.getText().equals("") && !txtContactNumber.getText().equals("")
                && !txtRoomID.getText().equals("") && !txtTypeOfRoom.getText().equals("") && !txtDuration.getText().equals("") && !dcCheckInDate.getDate().equals("")
                && !dcCheckOutDate.getDate().equals(""))) {
            // Calling the enableInputs method from object of Function class
            objFunc.enableInputs(true);
        }
        else {
            JOptionPane.showMessageDialog(null, "Please Select Room in the table to Modify!", "Room Modification Warning", JOptionPane.WARNING_MESSAGE);  
        }        
    }//GEN-LAST:event_btnModifyActionPerformed

    private void btnSaveChangesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveChangesActionPerformed
        // TODO add your handling code here:
        BookedRooms objBookedRooms = new BookedRooms();
        Function objFunc = new Function();
        // Declare a variable to store the modified data got from the method
        String modifiedData = objFunc.getModifiedData();
        if(modifiedData != null) {
           // Pass the modified data into saveChanges method
           objFunc.saveChanges(modifiedData);
           // Disable all the input fields
           objFunc.enableInputs(false);
           // Import all the data from the text file into table again
           objBookedRooms.viewBookedRooms();
           objBookedRooms.previewData();
           JOptionPane.showMessageDialog(null, "Modify Successfully!", "Booked Room Modification Message", JOptionPane.INFORMATION_MESSAGE);  
        }       
    }//GEN-LAST:event_btnSaveChangesActionPerformed

    private void btnHome1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHome1ActionPerformed
        // Return back to homepage
        HomePage homepage = new HomePage();
        this.dispose();
        homepage.show();
    }//GEN-LAST:event_btnHome1ActionPerformed

    private void btnCheckOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckOutActionPerformed
        int checkOutConfirmation = JOptionPane.showConfirmDialog(null, "Please Select Yes to Confirm Your Room Check Out Process", "Room Check Out Confirmation Message", JOptionPane.YES_NO_OPTION);  
        if(checkOutConfirmation == JOptionPane.YES_OPTION) {
            BookedRooms objBookedRoom = new BookedRooms();
            Function objFunc = new Function();
            int selectedRow = tblBookedRoom.getSelectedRow();
            if(selectedRow >= 0) {
                objFunc.checkOut(selectedRow);
                objBookedRoom.viewBookedRooms();
                JOptionPane.showMessageDialog(null, "Room Checked Out Successfully", "Room Check Out Successfully", JOptionPane.INFORMATION_MESSAGE);

            }
            else {
                JOptionPane.showMessageDialog(null, "Please Select Room in the table to Check Out!", "Room Check Out Warning", JOptionPane.WARNING_MESSAGE);  
            }
        }        
    }//GEN-LAST:event_btnCheckOutActionPerformed

    private void txtContactNumberKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtContactNumberKeyTyped
        // Ensure Numeric Input from Users for Customer Contact Number
        char c = evt.getKeyChar();
        if(!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || c == KeyEvent.VK_DELETE)) {
            JOptionPane.showMessageDialog(null, "Please Enter Only Numeric Number", "Invalid Input Warning", JOptionPane.WARNING_MESSAGE);
            evt.consume();
        }
    }//GEN-LAST:event_txtContactNumberKeyTyped

    private void txtCustomerICKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCustomerICKeyTyped
        // Ensure Numeric Input from Users for Customer IC
        char c = evt.getKeyChar();
        if(!(Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || c == KeyEvent.VK_DELETE)) {
            JOptionPane.showMessageDialog(null, "Please Enter Only Numeric Number", "Invalid Input Warning", JOptionPane.WARNING_MESSAGE);
            evt.consume();
        }
    }//GEN-LAST:event_txtCustomerICKeyTyped

    private void txtTypeOfRoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTypeOfRoomActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTypeOfRoomActionPerformed

    private void cbGenderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbGenderActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbGenderActionPerformed

    private void txtDurationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDurationActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDurationActionPerformed

    private void txtRoomIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRoomIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRoomIDActionPerformed

    private void txtEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailActionPerformed

    private void txtCustomerNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCustomerNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCustomerNameActionPerformed

    private void txtPaymentMethodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPaymentMethodActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPaymentMethodActionPerformed

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
            java.util.logging.Logger.getLogger(ManageBookingPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManageBookingPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManageBookingPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManageBookingPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManageBookingPage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCheckOut;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnHome;
    private javax.swing.JButton btnHome1;
    private javax.swing.JButton btnModify;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSaveChanges;
    private javax.swing.JComboBox<String> cbGender;
    private com.toedter.calendar.JDateChooser dcCheckInDate;
    private com.toedter.calendar.JDateChooser dcCheckOutDate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JLabel lblBR;
    private javax.swing.JLabel lblBR1;
    private javax.swing.JLabel lblCheckInDate;
    private javax.swing.JLabel lblCheckInDate1;
    private javax.swing.JLabel lblCheckOutDate;
    private javax.swing.JLabel lblContactNumber;
    private javax.swing.JLabel lblCustomerIC;
    private javax.swing.JLabel lblCustomerName;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblGender;
    private javax.swing.JLabel lblNumberOfRooms;
    private javax.swing.JLabel lblNumberOfRooms1;
    private javax.swing.JLabel lblNumberOfRooms2;
    private javax.swing.JLabel lblNumberOfRooms3;
    private javax.swing.JLabel lblNumberOfRooms4;
    private javax.swing.JLabel lblNumberOfRooms7;
    private javax.swing.JLabel lblRoomCharges;
    private javax.swing.JLabel lblServiceTax;
    private javax.swing.JLabel lblTotalPrice;
    private javax.swing.JLabel lblTourismTax;
    private javax.swing.JLabel lblTypeOfRoom;
    private javax.swing.JTable tblBookedRoom;
    private javax.swing.JFormattedTextField txtContactNumber;
    private javax.swing.JFormattedTextField txtCustomerIC;
    private javax.swing.JTextField txtCustomerName;
    private javax.swing.JTextField txtDuration;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtPaymentMethod;
    private javax.swing.JTextField txtRoomID;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtTypeOfRoom;
    // End of variables declaration//GEN-END:variables
}
