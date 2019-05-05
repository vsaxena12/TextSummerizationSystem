package document.gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class NewUser extends JPanel {

    public static Connection conn;
    public static Statement st;
    PreparedStatement pst;
    private final JFrame jf;
    private JPanel jPanel1;
    private JLabel unameTextLabel, pswdTextLabel, confirmpswdTextLabel;
    private JTextField usernameText, passwordText, confirmpswdText;
    private JButton submitButton;
    private JButton cancelButton;
    private String username;

    public NewUser() {
        initComponents();
        jf = new JFrame();
        jf.add(this);
        jf.setTitle("New User");
        setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setBounds(200, 100, 500, 500);
//        System.exit(0);
    }

    public void setVisible(boolean status) {
        jf.setVisible(status);
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        unameTextLabel = new javax.swing.JLabel();
        pswdTextLabel = new javax.swing.JLabel();
        confirmpswdTextLabel = new javax.swing.JLabel();
        usernameText = new javax.swing.JTextField();
        passwordText = new javax.swing.JPasswordField();
        confirmpswdText = new javax.swing.JPasswordField();
        submitButton = new javax.swing.JButton();
        cancelButton = new JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "SignUp", 0, 0, new java.awt.Font("Agency FB", 1, 18), new java.awt.Color(51, 153, 255))); 
        jPanel1.setToolTipText("SignUP");
        jPanel1.setName("SignUp");

        unameTextLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); 
        unameTextLabel.setText("Username:");

        submitButton.setFont(new java.awt.Font("Tahoma", 1, 14)); 
        submitButton.setText("Submit");
        submitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitButtonActionPerformed(evt);
            }
        });

        cancelButton.setFont(new java.awt.Font("Tahoma", 1, 14)); 
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        usernameText.setName(""); 
        passwordText.setName("");
        confirmpswdText.setName("");

        pswdTextLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); 
        pswdTextLabel.setText("Password:");
        confirmpswdTextLabel.setFont(new java.awt.Font("Tahoma", 1, 14));
        confirmpswdTextLabel.setText("Confirm Password");

        javax.swing.GroupLayout jPanelLayout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanelLayout);
        jPanelLayout.setHorizontalGroup(
                jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelLayout.createSequentialGroup()
                .addComponent(pswdTextLabel)
                .addGap(18, 18, 18)
                .addComponent(passwordText, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelLayout.createSequentialGroup()
                .addComponent(confirmpswdTextLabel)
                .addGap(18, 18, 18)
                .addComponent(confirmpswdText, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelLayout.createSequentialGroup()
                .addComponent(unameTextLabel)
                .addGap(18, 18, 18)
                .addComponent(usernameText, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(submitButton)
                .addGap(89, 89, 89))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cancelButton)
                .addGap(89, 89, 89)));

        jPanelLayout.setVerticalGroup(
                jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(usernameText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(unameTextLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(pswdTextLabel)
                .addComponent(passwordText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(confirmpswdTextLabel)
                .addComponent(confirmpswdText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(submitButton)
                .addGap(18, 18, 18)
                .addComponent(cancelButton)
                .addContainerGap(26, Short.MAX_VALUE)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(108, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(104, 104, 104)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                .addGap(117, 117, 117)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(124, Short.MAX_VALUE)));
    }

    public boolean validateMatchPassword() throws SQLException, ClassNotFoundException {
        if (!usernameText.getText().equals("")) {
            conn = Login.connectDB();
            st = conn.createStatement();
            String query = "SELECT * FROM users WHERE username='" + this.usernameText.getText() + "';";
            ResultSet rs = st.executeQuery(query);

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, " User Already Exit  !!!");
                return false;
            }
            if (!passwordText.getText().equals("") || !confirmpswdText.getText().equals("")) {
                if (passwordText.getText().equals(confirmpswdText.getText())) {
                    return true;
                } else {
                    JOptionPane.showMessageDialog(this, "Password does not match  !!!");
                }  
                return false;
            } else {
                JOptionPane.showMessageDialog(this, "Password or Confirm password should not be empty  !!!");
            }
            return false;
        } else {
            JOptionPane.showMessageDialog(this, "Username should not be empty  !!!  !!!");
        }
        return false;
    }

    private void submitButtonActionPerformed(ActionEvent evt) {
        System.out.println("hi :");
        try {
            if (validateMatchPassword() == true) {
                conn = Login.connectDB();
                st = conn.createStatement();
//             String qry = "insert into users values( username='"+this.usernameText.getText()+"' "
//                    + "AND password='"+this.passwordText.getText()+"');";
                //Login.connectDB();
                String qry = "insert into users(`username`,`password`) values('" + this.usernameText.getText() + "','" + this.passwordText.getText() + "');";
                int rs = Login.st.executeUpdate(qry);
//            ResultSet rs = st.executeQuery(qry);


                String foldername = this.usernameText.getText();
                String workingDir = System.getProperty("user.dir");
                System.out.println("workingDir:" + workingDir);
                File dir = new File(workingDir + "/USERs/" + foldername);
                boolean mkdir = dir.mkdir();
                System.out.println("mkdir:" + mkdir);
                if (mkdir) {
                    System.out.println("Created:");
                }
                System.out.println("filepath:" + dir);


                if (rs > 0) {
                    JOptionPane.showMessageDialog(this, "SignUp Successful !!!");
                    this.setVisible(false);
                    this.username = this.usernameText.getText();
                    openLoginForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password !!!");
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Could not connect database. Check database status.");
            ex.printStackTrace();
            System.exit(0);
        }
    }

    private void openLoginForm() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                (new Login()).setVisible(true);
            }
        });
    }

    private void cancelButtonActionPerformed(ActionEvent evt) {
        openLoginForm();
        jf.setVisible(false);
    }
}
