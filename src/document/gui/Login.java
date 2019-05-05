package document.gui;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Login extends javax.swing.JPanel {

    public static Connection conn = null;
    public static Statement st = null;
    PreparedStatement pst = null;
    static String url = "jdbc:mysql://localhost:3306/";
    static String dbName = "textsummarization";
    static String driver = "com.mysql.jdbc.Driver";
    static String dbUserName = "root";
    static String dbPassword = "";
    static String username = null;
    static JFrame jf = null;
    private JButton NewUserButton;

    /**
     * Creates new form Login
     */
    public Login() {
        initComponents();
        jf = new JFrame();
        jf.add(this);
        jf.setTitle("Login");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setBounds(200, 100, 500, 500);
        try {
            connectDB();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Could not connect DB. Check database configuration and DB running status");
            System.exit(0);
        }
    }

    public void setVisible(boolean status) {
        super.setVisible(status);
        jf.setVisible(status);
    }

    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        unameText = new javax.swing.JLabel();
        loginButton = new javax.swing.JButton();
        NewUserButton = new javax.swing.JButton();
        passwordText = new javax.swing.JPasswordField();
        usernameText = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();


        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Login", 0, 0, new java.awt.Font("Agency FB", 1, 18), new java.awt.Color(51, 153, 255))); 
        jPanel1.setToolTipText("Login");
        jPanel1.setName("Login"); 

        unameText.setFont(new java.awt.Font("Tahoma", 1, 14)); 
        unameText.setText("Username:");

        loginButton.setFont(new java.awt.Font("Tahoma", 1, 14)); 
        loginButton.setText("LOGIN");
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });
        NewUserButton.setFont(new java.awt.Font("Tahoma", 1, 14));
        NewUserButton.setText("New User");
        NewUserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                NewUserButtonActionPerformed(evt);
            }
        });

        usernameText.setName(""); 

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); 
        jLabel2.setText("Password:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(passwordText, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(unameText)
                .addGap(18, 18, 18)
                .addComponent(usernameText, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(loginButton)
                .addGap(89, 89, 89))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(NewUserButton)
                .addGap(89, 89, 89)));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(usernameText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(unameText))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel2)
                .addComponent(passwordText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(loginButton)
                .addGap(18, 18, 18)
                .addComponent(NewUserButton)
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

    public static Connection connectDB() throws SQLException, ClassNotFoundException {
        if (conn == null) {
            Class.forName(driver);
            conn = DriverManager.getConnection(url + dbName, dbUserName, dbPassword);
            st = conn.createStatement();
        }
        return conn;

    }

    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            
            String  name=this.usernameText.getName();
              
            String query = "SELECT * FROM users WHERE username='" + this.usernameText.getText() + "' "
                    + "AND password='" + this.passwordText.getText() + "';";
            ResultSet rs = st.executeQuery(query);

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login Successful !!!");
                this.setVisible(false);
                this.username = this.usernameText.getText();
                openMainForm();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password !!!");
                this.usernameText.setText("");
                this.passwordText.setText(""); 

            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Could not connect database. Check database status.");
            System.exit(0);
        }
    }

    private void NewUserButtonActionPerformed(ActionEvent evt) {
        new NewUser();
        jf.setVisible(false);
    }

    public void openMainForm() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    (new MainGUI()).setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton loginButton;
    private javax.swing.JPasswordField passwordText;
    private javax.swing.JLabel unameText;
    private javax.swing.JTextField usernameText;
}
