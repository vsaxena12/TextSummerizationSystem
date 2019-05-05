package document.gui;

import com.aliasi.util.Files;
import document.parser.Parser;
import document.parser.SimpleParser;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.html.HTMLDocument;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

public class MainGUI extends JFrame implements ActionListener {
    String Summury = "";
      String start;
      String end;
    private static String text1;
    JFrame myFrame = null;
    static String Title = null;
    private JMenuItem jMenuItemViewSummary;
    private JMenuItem jMenuItemmakecluster;
    private JButton jButtonView;
    ArrayList<File> selectedfiles = new ArrayList<File>();
    ArrayList<String> Tempfiles = new ArrayList<String>();
    static public ArrayList<String> Title_document = new ArrayList<String>();
    String workingDir = System.getProperty("user.dir");
    static boolean viewsumselected = false;

    public MainGUI() throws IOException {
        
        documentParser = null;
        //  help = new Utilities();
        sourceFile = null;
        summLevel = 1;
        freqThreshold = (-1.0D / 0.0D);
        algorithm = 0;
        isNewSourceFile = false;
        jComboBoxURLContents = new HashSet();

        initComponents();
        setFrameLocation();
        try {
            clearDb();
        } catch (SQLException ex) {
            Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
         String path=getCurrentUserdirectory();
        System.out.println("Path-->"+path);
         cleanDir(path);
        
       
    }
     public static void cleanDir(String pathv) {
        String path=pathv; 
        File file = new File(path);
        File[] files = file.listFiles(); 
        for (File f:files) 
        {if (f.isFile() && f.exists()) 
            { f.delete();
        System.out.println("successfully deleted");
            }else{
        System.out.println("cant delete a file due to open or error");
        } }  }
     public static void clearDb() throws SQLException,ClassNotFoundException
     {
         Connection conn = null;
         try
         {
         conn = Login.connectDB();
         }
         catch(Exception e) 
         {
             e.printStackTrace();
             JOptionPane.showMessageDialog(null,"Couldn't Connect to the Database");
         }
         Statement st=conn.createStatement();
         String str1="TRUNCATE clusterset2";
         String str2="TRUNCATE filedetail";
         String str3="TRUNCATE keyword_data";
         String str4="TRUNCATE sentence_data";
         st.executeUpdate(str1);
         st.executeUpdate(str2);
         st.executeUpdate(str3);
         st.executeUpdate(str4);
     }
    private void initComponents() {
        fc = new JFileChooser();
        fc.setMultiSelectionEnabled(true);

        jToolBar = new JToolBar();
        jButtonOpen = new JButton();
        jButtonView = new JButton();
        jLabelSeparator1 = new JLabel();
        jButton1 = new JButton();
        jLabelURL = new JLabel();
        jLabelSeparator10 = new JLabel();
        jComboBoxURL = new JComboBox();
        jLabelSeparator9 = new JLabel();
        jLabel1 = new JLabel();
        jLabelSeparator12 = new JLabel();
        jComboBoxAlgorithms = new JComboBox();
        jLabelSeparator2 = new JLabel();
        jLabelSummaryLength = new JLabel();
        jSliderSummaryLength = new JSlider();
        jButtonGo = new JButton();
        jLabelSeparator8 = new JLabel();
        jSplitPaneDocuments = new JSplitPane();
        jScrollPaneSummary = new JScrollPane();
        jTextAreaSummary = new JTextArea();
        jScrollPaneSource = new JScrollPane();
        jEditorPaneSource = new JEditorPane();
        jStatusBar = new JToolBar();
        jLabelStatus = new JLabel();
        jLabelTotalNumSentencesLabel = new JLabel();
        jLabelTotalNumSentencesValue = new JLabel();
        jLabelNumSentencesToExtractLabel = new JLabel();
        jLabelNumSentencesToExtractValue = new JLabel();
        jLabelCoverageOfSummaryLabel = new JLabel();
        jLabelCoverageOfSummaryValue = new JLabel();
        jLabelSeparator13 = new JLabel();
        jLabelOrthogonalityOfSummaryLabel = new JLabel();
        jLabelOrthogonalityOfSummaryValue = new JLabel();
        jSeparator2 = new JSeparator();
        jMenuBar = new JMenuBar();
        jMenuFile = new JMenu();
        jMenuItemOpen = new JMenuItem();
        jMenuItemSave = new JMenuItem();
        jMenuItemViewSummary = new JMenuItem();
        jMenuItemmakecluster = new JMenuItem();
        jSeparator1 = new JSeparator();
        jMenuItemExit = new JMenuItem();
        jMenuHelp = new JMenu();
        jMenuItemAbout = new JMenuItem();
        fc.setFocusable(false);
        fc.setFileFilter(new TypeOfFile());
        fc.setAcceptAllFileFilterUsed(false);
        setDefaultCloseOperation(3);
        setTitle("Document Summarization");
        jToolBar.setBorder(BorderFactory.createEtchedBorder());
        jToolBar.setFloatable(false);
        jButtonOpen.setIcon(new ImageIcon("src\\images\\openfolderb24.png"));
        jButtonOpen.setToolTipText("Open File...");
        jButtonOpen.setFocusPainted(false);
        jButtonOpen.setFocusable(false);
        jButtonOpen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    try {
                        jButtonOpenActionPerformed(evt);
                    } catch (SQLException ex) {
                        Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        jToolBar.add(jButtonOpen);
        jLabelSeparator1.setText(" ");
        jLabelSeparator1.setEnabled(false);
        jLabelSeparator1.setFocusable(false);
        jToolBar.add(jLabelSeparator1);
        jButton1.setIcon(new ImageIcon("src/images/save24.png"));
        jButton1.setToolTipText("Save...");
        jButton1.setFocusPainted(false);
        jButton1.setFocusable(false);
        jButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jButtonView.setIcon(new ImageIcon("src/images//view.png"));
        jButtonView.setToolTipText("View Summary");
        jButtonView.setFocusPainted(false);
        jButtonView.setFocusable(false);
        jButtonView.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButtonViewActionPerformed(evt);
            }
        });

        jToolBar.add(jButton1);
        jToolBar.add(jButtonView);

        jLabelURL.setFont(new Font("Tahoma", 1, 11));
        //jLabelURL.setText("URI/URL");
        jToolBar.add(jLabelURL);
        jLabelSeparator10.setText(" ");
        jToolBar.add(jLabelSeparator10);
        
        jLabelSeparator9.setText("  ");
        jToolBar.add(jLabelSeparator9);
        jLabel1.setFont(new Font("Tahoma", 1, 6));

        jToolBar.add(jLabel1);
        jLabelSeparator12.setText(" ");
        jToolBar.add(jLabelSeparator12);
        jComboBoxAlgorithms.setMaximumSize(new Dimension(32767, 20));
        jComboBoxAlgorithms.setMinimumSize(new Dimension(100, 20));
        jComboBoxAlgorithms.setPreferredSize(new Dimension(200, 20));
        jComboBoxAlgorithms.setModel(new DefaultComboBoxModel(readAlgorithmsList()));
        jComboBoxAlgorithms.setSelectedIndex(0);
        jComboBoxAlgorithms.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            
                jComboBoxAlgorithmsActionPerformed(evt);
            
            }
        });

        jLabelSeparator2.setText("  ");
        jLabelSeparator2.setEnabled(false);
        jLabelSeparator2.setFocusable(false);
        jToolBar.add(jLabelSeparator2);
//        jLabelSummaryLength.setFont(new Font("Tahoma", 1, 11));
//        jLabelSummaryLength.setText("Summary Length");
        jToolBar.add(jLabelSummaryLength);

        Hashtable labelTable = new Hashtable();
        labelTable.put(new Integer(0), new JLabel("0%"));
        labelTable.put(new Integer(100), new JLabel("100%"));
  //      jSliderSummaryLength.setLabelTable(labelTable);
        
       // jToolBar.add(jSliderSummaryLength);
        jButtonGo.setIcon(new ImageIcon("src/images/go24.png"));
        jButtonGo.setToolTipText("Run...");
        jButtonGo.setBorder(null);
        jButtonGo.setFocusPainted(false);
        jButtonGo.setFocusable(false);
        jButtonGo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {

                try {
                    try {
                        jButtonGoActionPerformed(evt);
                    } catch (SQLException ex) {
                        Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (URISyntaxException e) {
                   
                    e.printStackTrace();
                } catch (IOException e) {
                 
                    e.printStackTrace();
                } catch (InterruptedException ex) {
                    Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        jToolBar.add(jButtonGo);
        jLabelSeparator8.setText("  ");
        jLabelSeparator8.setEnabled(false);
        jLabelSeparator8.setFocusable(false);
        jToolBar.add(jLabelSeparator8);
        jSplitPaneDocuments.setBorder(null);
        jSplitPaneDocuments.setDividerLocation(250);
        jSplitPaneDocuments.setResizeWeight(0.5D);
        jSplitPaneDocuments.setOneTouchExpandable(true);
        jScrollPaneSummary.setBorder(BorderFactory.createTitledBorder(null, "Summary", 0, 0, new Font("Tahoma", 0, 11), new Color(0, 0, 0)));
        jTextAreaSummary.setColumns(20);
        jTextAreaSummary.setEditable(false);
        jTextAreaSummary.setLineWrap(true);
        jTextAreaSummary.setRows(5);
        jTextAreaSummary.setWrapStyleWord(true);
        jTextAreaSummary.setBorder(BorderFactory.createEtchedBorder());
        jScrollPaneSummary.setViewportView(jTextAreaSummary);
        jSplitPaneDocuments.setLeftComponent(jScrollPaneSummary);
        jScrollPaneSource.setBorder(BorderFactory.createTitledBorder(null, "Source", 0, 0, new Font("Tahoma", 0, 11), new Color(0, 0, 0)));
        jScrollPaneSource.setVerticalScrollBarPolicy(22);
        jEditorPaneSource.setBorder(BorderFactory.createEtchedBorder());
        jEditorPaneSource.setEditable(false);
        jScrollPaneSource.setViewportView(jEditorPaneSource);
        jSplitPaneDocuments.setRightComponent(jScrollPaneSource);
        jStatusBar.setBorder(BorderFactory.createEtchedBorder());
        jStatusBar.setFloatable(false);
        jStatusBar.setFocusable(false);
        jLabelStatus.setText("Statistics:   ");
        jStatusBar.add(jLabelStatus);
        jLabelStatus.getAccessibleContext().setAccessibleName("  Statistics:   ");
        jLabelTotalNumSentencesLabel.setText("#sentences: ");
        jLabelTotalNumSentencesLabel.setFocusable(false);
        jStatusBar.add(jLabelTotalNumSentencesLabel);
        jLabelTotalNumSentencesValue.setFocusable(false);
        jLabelTotalNumSentencesValue.setMaximumSize(new Dimension(45, 14));
        jLabelTotalNumSentencesValue.setMinimumSize(new Dimension(45, 14));
        jLabelTotalNumSentencesValue.setPreferredSize(new Dimension(45, 14));
        jStatusBar.add(jLabelTotalNumSentencesValue);
        jLabelNumSentencesToExtractLabel.setText("#sentences to extract: ");
        jLabelNumSentencesToExtractLabel.setFocusable(false);
        jStatusBar.add(jLabelNumSentencesToExtractLabel);
        jLabelNumSentencesToExtractValue.setMaximumSize(new Dimension(45, 14));
        jLabelNumSentencesToExtractValue.setMinimumSize(new Dimension(45, 14));
        jLabelNumSentencesToExtractValue.setPreferredSize(new Dimension(45, 14));
        jStatusBar.add(jLabelNumSentencesToExtractValue);
        // jLabelCoverageOfSummaryLabel.setText("coverage of summary: ");
        //jLabelCoverageOfSummaryLabel.setFocusable(false);
        //  jStatusBar.add(jLabelCoverageOfSummaryLabel);
        jLabelCoverageOfSummaryValue.setFocusable(false);
        jLabelCoverageOfSummaryValue.setMaximumSize(new Dimension(70, 14));
        jLabelCoverageOfSummaryValue.setMinimumSize(new Dimension(70, 14));
        jLabelCoverageOfSummaryValue.setPreferredSize(new Dimension(70, 14));
        // jStatusBar.add(jLabelCoverageOfSummaryValue);
        jLabelSeparator13.setText("  ");
        jStatusBar.add(jLabelSeparator13);
        jLabelOrthogonalityOfSummaryLabel.setText("orthogonality of summary: ");
        jLabelOrthogonalityOfSummaryLabel.setFocusable(false);
        //jStatusBar.add(jLabelOrthogonalityOfSummaryLabel);
        jLabelOrthogonalityOfSummaryValue.setFocusable(false);
        jLabelOrthogonalityOfSummaryValue.setMaximumSize(new Dimension(70, 14));
        jLabelOrthogonalityOfSummaryValue.setMinimumSize(new Dimension(70, 14));
        jLabelOrthogonalityOfSummaryValue.setPreferredSize(new Dimension(70, 14));
        // jStatusBar.add(jLabelOrthogonalityOfSummaryValue);
        jSeparator2.setOrientation(1);
        jSeparator2.setMaximumSize(new Dimension(2, 14));
        jSeparator2.setMinimumSize(new Dimension(2, 14));
        jSeparator2.setPreferredSize(new Dimension(2, 14));
        jStatusBar.add(jSeparator2);
        jMenuBar.setBorder(null);
        jMenuFile.setText("File");
        jMenuItemOpen.setIcon(new ImageIcon("openfolderb16.png"));
        jMenuItemOpen.setText("Open");
        jMenuItemOpen.setAccelerator(KeyStroke.getKeyStroke(79, 2));
        jMenuItemOpen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    try {
                        jMenuItemOpenActionPerformed(evt);
                    } catch (SQLException ex) {
                        Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        jMenuFile.add(jMenuItemOpen);
        jMenuItemSave.setIcon(new ImageIcon("src/images//saven16.png"));
        jMenuItemSave.setText("Save");
        jMenuItemSave.setAccelerator(KeyStroke.getKeyStroke(83, 2));
        jMenuItemSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItemSaveActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemmakecluster);
        jMenuItemmakecluster.setIcon(new ImageIcon("null"));
        jMenuItemmakecluster.setText("Make Cluters");
        jMenuItemmakecluster.setAccelerator(KeyStroke.getKeyStroke(89, 2));
        jMenuItemmakecluster.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    jMenuItemMakeClusterActionPerformed(evt);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        jMenuFile.add(jMenuItemViewSummary);
        jMenuItemViewSummary.setIcon(new ImageIcon("null"));
        jMenuItemViewSummary.setText("View Summary");
        jMenuItemViewSummary.setAccelerator(KeyStroke.getKeyStroke(87, 2));
        jMenuItemViewSummary.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItemViewSummaryActionPerformed(evt);

            }
        });




        jMenuFile.add(jMenuItemSave);
        jMenuFile.add(jSeparator1);
        jMenuItemExit.setText("Exit");
        jMenuItemExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItemExitActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemExit);
        jMenuBar.add(jMenuFile);
        jMenuHelp.setText("Help");
        jMenuItemAbout.setText("About...");
        jMenuItemAbout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jMenuItemAboutActionPerformed(evt);
            }
        });
        jMenuHelp.add(jMenuItemAbout);
        jMenuBar.add(jMenuHelp);
        setJMenuBar(jMenuBar);
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jToolBar, -1, 790, 32767).addComponent(jStatusBar, -1, 790, 32767).addComponent(jSplitPaneDocuments, -1, 790, 32767));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jToolBar, -2, 43, -2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jSplitPaneDocuments, -1, 288, 32767).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jStatusBar, -2, 18, -2)));
        pack();
    }

    private void jSliderSummaryLengthStateChanged(ChangeEvent evt) {
        JSlider source = (JSlider) evt.getSource();
        if (!source.getValueIsAdjusting()) {
            int percentage = source.getValue();
            if (jLabelTotalNumSentencesValue.getText() != null && jLabelTotalNumSentencesValue.getText().length() != 0) {
                int tmp_numSentencesInSource = (new Integer(jLabelTotalNumSentencesValue.getText())).intValue();
                float tmp_numSentencesInSummary = ((float) tmp_numSentencesInSource * (float) percentage) / 100F;
                summLevel = Math.round(tmp_numSentencesInSummary);
                jLabelNumSentencesToExtractValue.setText((new Integer(summLevel)).toString());
            }
        }
    }

    private void postProcessSummary(int indices[]) {
        if (indices != null) {
//            jLabelOrthogonalityOfSummaryValue.setText(String.valueOf(help.evaluateJaccardDistance(documentParser.getSparseSentences(), indices)));
//            jLabelCoverageOfSummaryValue.setText(String.valueOf(help.evaluateCoverage(documentParser.getSparseSentences(), indices, documentParser.getNumUniqueWords())));
            highlightDocument(indices, Color.YELLOW);
        } else {
            jLabelOrthogonalityOfSummaryValue.setText("");
            jLabelCoverageOfSummaryValue.setText("");
            jTextAreaSummary.setText("");
        }
    }

    private void jButtonGoActionPerformed(ActionEvent evt) throws URISyntaxException, IOException, InterruptedException, SQLException, ClassNotFoundException {
        long init=System.currentTimeMillis()/1000;
        int n=1;
        String tempfiles=null; 
        System.out.println(" go action performed:");       
        Connection conn = Login.connectDB();
        Statement stmt = conn.createStatement();
        HashMap<String, ArrayList<Integer>> file_summury = new HashMap<String, ArrayList<Integer>>();
        int current_fileid = 0;
        int userid = getuserid();
        System.out.println("userid"+userid);
        System.out.println("Selected f size:"+selectedfiles.size());
       
        for (int i = 0; i < selectedfiles.size(); i++) {
      //      obj.updateBar(n++);
            File file = selectedfiles.get(i);
            String Title_doc = gettitle_db(file.getName());
            System.out.println("File Title:"+Title_doc);
            current_fileid = getfileid(userid, file.getName());          
            tempfiles = Tempfiles.get(i);
            System.out.println("tempfiles are:"+tempfiles);
            File f = new File(tempfiles);
            documentParser = new Parser(f.getAbsolutePath(), current_fileid, freqThreshold, Title_doc);
            ArrayList<Integer> sentenceweight = getsentenceweight(file.getName(), userid, conn, stmt);
            file_summury.put(file.getName(), sentenceweight);
        }
        selectedfiles.removeAll(selectedfiles);
        Tempfiles.removeAll(Tempfiles);
        String[] pass=new String[200];
        for (Map.Entry<String, ArrayList<Integer>> entry : file_summury.entrySet()) {
            String fname = entry.getKey();
            String summ = "";
            String finalsumm = "";
            int fid = getfileid(userid, fname);
            ArrayList<Integer> arrayList = entry.getValue();
            System.out.println("filename:" + fname + "senetences in summury:" + arrayList);
            for (int i = 0; i < arrayList.size(); i++) {
                Integer sid = arrayList.get(i);
                String summarysenetnces = getsummurysenetnces(fid, sid, stmt);
                summ = summ +  summarysenetnces;
                pass[i]=summ;
            
             System.out.println("Summury:" + Summury);
              
            }
           Summury = Summury + "\n" + summ;
           
           
          
        }
        
        String workingDir = System.getProperty("user.dir");
        String[] lines;
        String myfile=workingDir+"\\"+"Summary.txt";
        String op=  write(Summury,myfile);
        
       // lines=TextDivision.divie(op);
       // System.out.println("Lines length:"+lines.length);
        String[] words = Summury.split("\\.");
        for (String str : words) {
      System.out.println("line1--->>>"+str);
    }
        TitleLayout.addcon(words);
       System.out.println("Summury:" + Summury);
       jTextAreaSummary.setText(Summury);
       long end=System.currentTimeMillis()/1000;
       int x= (int) (end-init);
       JOptionPane.showMessageDialog(null,"Time elapsed in Summerization:'"+x+"' seconds");
       
    }
    private void jButton1ActionPerformed(ActionEvent evt) {
        saveFile();
    }

    private void jButtonOpenActionPerformed(ActionEvent evt) throws IOException, SQLException, ClassNotFoundException {
        openFile();
    }

    private void jMenuItemAboutActionPerformed(ActionEvent evt) {
    }

    private void jMenuItemSaveActionPerformed(ActionEvent evt) {
        saveFile();
    }

    private void jMenuItemViewSummaryActionPerformed(ActionEvent evt) {
        //SummaryDetails.summaryTable();

        System.out.println("---------------------------------");
        try {
            int userid = getuserid();
            HashMap<String, String> user_Clusters = getuser_Clusters(userid);

            if (!viewsumselected) {


                if (user_Clusters.size() == 0) {
                    JOptionPane.showMessageDialog(null, "No  clusters found");
                } else {
                    viewsumselected = true;
                    Collection<String> values = user_Clusters.values();
                    for (Iterator<String> it = values.iterator(); it.hasNext();) {
                        String string = it.next();
                        System.out.println("Collection have(Values):"+string);
                    }
                    Object[] toArray = values.toArray();
                    
                    Set<String> keySet = user_Clusters.keySet();
                    Object[] toArray1 = keySet.toArray();
                    String keys_names[] = new String[toArray1.length];


                    System.out.println("-------------------------");
                    for (int i = 0; i < toArray1.length; i++) {
                        Object object = toArray1[i];
                        String abc = (String) object;
                        keys_names[i] = abc;

                    }

                    int select_Clusters = select_Clusters(keys_names);

                    while (select_Clusters < 0) {
                        select_Clusters = select_Clusters(keys_names);
                    }
                    System.out.println("------------");
                    String filesinclusters = (String) toArray[select_Clusters];
                    System.out.println("filesinclusters:" + filesinclusters);
                    String[] filenamesinclusters = filesinclusters.split(",");
                    String path = workingDir + "/USERs/" + Login.username;
                    selectedfiles.removeAll(selectedfiles);
                    System.out.println("path :" + path);
                     System.out.println("File names in clusters:"+filenamesinclusters.length);
                    for (int i = 0; i < filenamesinclusters.length; i++) {
                        String fname = filenamesinclusters[i];
                        System.out.println("Files in Clusters:"+fname);
                        selectedfiles.add(new File(path + "/" + fname));
                    }
                    System.out.println("Selected File size:"+selectedfiles.size());
                    for (int j = 0; j < selectedfiles.size(); j++) {
                        File select_file = selectedfiles.get(j);
                        try {
                            FileWriter myWriter = null;
                            FileOutputStream fop = null;
                            File file = null;
                            sourcefilename = select_file.toString();
                            System.out.println("sourcefilename==" + sourcefilename);
                            if (sourcefilename.endsWith(".doc")) {

                                String filename = "output" + j + ".txt";
                                System.out.println("temfilename:" + filename);
                                readDocfile(select_file, filename);
                                Tempfiles.add(filename);
                            } else if (sourcefilename.endsWith(".docx")) {
                                System.out.println(" select_file.getAbsolutePath()" + select_file.getAbsolutePath());

                                String readdocxfile = readDocxfile(select_file.getAbsolutePath());
                               // System.out.println("alpha=" + readdocxfile);

                                String filename = "output" + j + ".txt";
                                System.out.println("output alpha=" + filename);
                                BufferedWriter outStream = new BufferedWriter(new FileWriter(filename));

                                System.out.println("filename:" + select_file.toString());
                                String st;
                                Tempfiles.add(filename);
                                File fil = new File("D:\\New.txt");
                                Writer output = new BufferedWriter(new FileWriter(filename));    
                                outStream.write(readdocxfile);
                                outStream.flush();
//                                }
                                outStream.close();

                            } else {
                        System.out.println("j:" + j);
                        String filename = "output" + j + ".txt";
                        BufferedWriter outStream=null;
                        outStream = new BufferedWriter(new FileWriter(filename));
                        System.out.println("Text filename:" + select_file.toString());
                        sourcefilename = select_file.toString();
                        String data= read(sourcefilename);
                        String op= write(data, filename);
                        Tempfiles.add(op);
                            }

                        } catch (Exception e) {
                        }
                    }

                    System.out.println("Tempfiles:" + Tempfiles);

                    String Alldata = "";
                    for (int i = 0; i < Tempfiles.size(); i++) {
                        String temfilename = Tempfiles.get(i);
                        File f2 = new File(temfilename);
                        sourceFile = f2;
                        isNewSourceFile = true;
                        String readFromFile = Files.readFromFile(sourceFile);

                        Alldata = Alldata + selectedfiles.get(i).getName() + "\n";

                        Alldata = Alldata + readFromFile + "\n";

                        System.out.println("sourceFile.toURI():" + sourceFile.toURI().toString());
                        if (!jComboBoxURLContents.add(sourceFile.toURI().toString())) {
                            jComboBoxURL.removeItem(sourceFile.toURI());
                        }
                        jComboBoxURL.insertItemAt(sourceFile.toURI(), 0);
                        jComboBoxURL.setSelectedIndex(0);
                        jTextAreaSummary.setText("");

                        jLabelCoverageOfSummaryValue.setText("");
                        jLabelOrthogonalityOfSummaryValue.setText("");

                        SimpleParser sp = new SimpleParser(sourceFile.getAbsolutePath());
                        jLabelNumSentencesToExtractValue.setText((new Integer(summLevel)).toString());

                    }
                    setEditorPanePage(sourceFile, Alldata, 0, Tempfiles.size());


                }
            } else {
                JOptionPane.showMessageDialog(null, "Cluster  is alraedy been selected  for summuray ");
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }

    }

    private void jMenuItemMakeClusterActionPerformed(ActionEvent evt) throws SQLException, ClassNotFoundException, Exception {
        //SummaryDetails.summaryTable();
        String path = getCurrentUserdirectory();
        int userid = getuserid();
        int noOffilesIndirectory = CheckfilesIndirectory(path);
        String usercluster = getusercluster(userid);
        String[] split_val = usercluster.split(",");
        String clusterval = split_val[0];
        String numberoffiles = split_val[1];

//        String numberoffiles = usercluster.substring(lastIndexOf, usercluster.length());
        int nooffiles_db = Integer.parseInt(numberoffiles);
        System.out.println("noOffilesIndirectory:" + noOffilesIndirectory);
        System.out.println("nooffiles_db:" + nooffiles_db);
        if (noOffilesIndirectory <= 1) {
            System.out.println("con1");
            JOptionPane.showMessageDialog(null, "Insufficinet files  in directory for clustering");
        } else if (noOffilesIndirectory > nooffiles_db || nooffiles_db == 0) {
            System.out.println("con2");
            Clustering.makeClusterSet(new File(path));
        } else if (noOffilesIndirectory == nooffiles_db) {
            System.out.println("con3");
            JOptionPane.showMessageDialog(null, " clustering is  already  been performed");
        } else {
            System.out.println("con4 else:");
//            Clustering.makeClusterSet(new File(path));
        }


    }

    private void jMenuItemOpenActionPerformed(ActionEvent evt) throws IOException, SQLException, ClassNotFoundException {
        // readdocfile();
        openFile();
    }

    private void jMenuItemExitActionPerformed(ActionEvent evt) {
        setVisible(false);
        System.exit(1);
    }

    private void jComboBoxAlgorithmsActionPerformed(ActionEvent actionevent) {
    }

    private void jButtonViewActionPerformed(ActionEvent evt) {
        try {
            int userid = getuserid();
            HashMap<String, String> user_Clusters = getuser_Clusters(userid);
            Collection<String> values = user_Clusters.values();
            Iterator<String> it =  values.iterator();
//            for (int i = 0; i < values.size(); i++) {
            while(it.hasNext()){
                System.out.println("Collection values are:"+it.next());
//            }
            }
            Object[] toArray = values.toArray();
            Set<String> keySet = user_Clusters.keySet();
            Iterator<String> it2 =  keySet.iterator();
//            for (int i = 0; i < values.size(); i++) {
            while(it2.hasNext()){
                System.out.println("Collection values are:"+it2.next());
//            }
            }
            Object[] toArray1 = keySet.toArray();
            System.out.println("toArray1 size::"+toArray1.length);
            String keys_names[] = new String[toArray1.length];

            System.out.println("-------------------------");
            for (int i = 0; i < toArray1.length; i++) {
                Object object = toArray1[i];
                String abc = (String) object;
                System.out.println("abc"+i+":"+abc);
                keys_names[i] = abc;
            }

            int select_Clusters = select_Clusters(keys_names);
            while (select_Clusters < 0) {
                select_Clusters = select_Clusters(keys_names);
            }
            System.out.println("------------");
            String filesinclusters = (String) toArray[select_Clusters];
            System.out.println("filesinclusters:" + filesinclusters);
            String[] filenamesinclusters = filesinclusters.split(",");
            System.out.println("filenamesinclusters=="+filenamesinclusters);
            String path = workingDir + "/USERs/" + Login.username;

            System.out.println("path :" + path);
            selectedfiles.removeAll(selectedfiles);
            for (int i = 0; i < filenamesinclusters.length; i++) {
                String fname = filenamesinclusters[i];
                selectedfiles.add(new File(path + "/" + fname));
                System.out.println("cluster name=" + fname);
            }

            for (int j = 0; j < selectedfiles.size(); j++) {

                File select_file = selectedfiles.get(j);
                try {
                    FileWriter myWriter = null;
                    FileOutputStream fop = null;
                    File file = null;
                    sourcefilename = select_file.toString();
                    System.out.println("sourcefilename" + sourcefilename);
                    if (sourcefilename.endsWith(".doc")) {

                        String filename = "output" + j + ".txt";
                        System.out.println("temfilename:" + filename);
                        readDocfile(select_file, filename);
                        Tempfiles.add(filename);
                    } else if (sourcefilename.endsWith(".docx")) {

                               
                                String readdocxfile = readDocxfile(select_file.getAbsolutePath());
                               
                                String filename = "output" + j + ".txt";
                                BufferedWriter outStream = new BufferedWriter(new FileWriter(filename));

                                System.out.println("filename:" + select_file.toString());
                                Tempfiles.add(filename);
//                                File fil = new File("D:\\New.txt");
//                                Writer output = new BufferedWriter(new FileWriter(filename));    
                                outStream.write(readdocxfile);
                                outStream.flush();
                                outStream.close();

                    } else {
                        System.out.println("j:" + j);
                        String filename = "output" + j + ".txt";
                        BufferedWriter outStream=null;
                        outStream = new BufferedWriter(new FileWriter(filename));
                        System.out.println("Text filename:" + select_file.toString());
                        sourcefilename = select_file.toString();
                        String data= read(sourcefilename);
                        String op= write(data, filename);
//                        System.out.println("test==>>"+data);
//                        System.out.println("Source text filename:"+op);
//                        FileReader fr = new FileReader(op);
//                        BufferedReader reader = new BufferedReader(fr);
//                        String st = null;
                         Tempfiles.add(op);
//                        while ((data=reader.readLine()) != null)
//                         {                                                     
//                            data = reader.readLine().replaceAll(" +", " ");
//                             System.out.println("data1:"+data);
//                            outStream.write(data);
//                            outStream.newLine();
//                            outStream.flush();
//                        }
//                         
//                        outStream.close();
                   }
                } catch (Exception e) {
                }
            }

            System.out.println("Tempfiles:" + Tempfiles);
            String Alldata = "";
            for (int i = 0; i < Tempfiles.size(); i++) {
                String temfilename = Tempfiles.get(i);
                File f2 = new File(temfilename);
                sourceFile = f2;
                isNewSourceFile = true;
                String readFromFile = Files.readFromFile(sourceFile);

                Alldata = Alldata + selectedfiles.get(i).getName() + "\n";

                Alldata = Alldata + readFromFile + "\n";

                System.out.println("sourceFile.toURI():" + sourceFile.toURI().toString());
                if (!jComboBoxURLContents.add(sourceFile.toURI().toString())) {
                    jComboBoxURL.removeItem(sourceFile.toURI());
                }
                jComboBoxURL.insertItemAt(sourceFile.toURI(), 0);
                jComboBoxURL.setSelectedIndex(0);
                jTextAreaSummary.setText("");

                jLabelCoverageOfSummaryValue.setText("");
                jLabelOrthogonalityOfSummaryValue.setText("");

                SimpleParser sp = new SimpleParser(sourceFile.getAbsolutePath());
                jLabelNumSentencesToExtractValue.setText((new Integer(summLevel)).toString());

            }
            setEditorPanePage(sourceFile, Alldata, 0, Tempfiles.size());

        } catch (Exception ee) {
            ee.printStackTrace();
        }

    }
     public static String read(String path) throws FileNotFoundException, IOException
     {
         String everything=null;
         BufferedReader br = new BufferedReader(new FileReader(path));
    try {
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();

        while (line != null) {
            sb.append(line);
            sb.append(System.lineSeparator());
            line = br.readLine();
        }
        everything = sb.toString();
        
    } finally {
        br.close();
    }
    return everything;
     }
     public String write(String string,String file)
     {
         try {
                    StringReader stringReader = new StringReader(string);
                    BufferedReader bufferedReader = new BufferedReader(stringReader);
                    FileWriter fileWriter = new FileWriter(file);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                    for(String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                        bufferedWriter.write(line);
                        bufferedWriter.newLine();
                    }
                    bufferedReader.close();
                    bufferedWriter.close();
                } catch (IOException e1) {
                  
                    e1.printStackTrace();
                }
         return file;
     }
    protected void setFrameLocation() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);
    }
    public static String sourcefilename;

    public void readdocfile() {
        try {
            File file = null;
            System.out.println("entry in readdoc");
            JFileChooser chooser = new JFileChooser();
            int returnVal = chooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = chooser.getSelectedFile();
             
                String extension = "";

                int i = file.getName().lastIndexOf('.');
                int p = Math.max(file.getName().lastIndexOf('/'), file.getName().lastIndexOf('\\'));

                if (i > p) {
                    extension = file.getName().substring(i + 1);
                }
                if (extension.equals("doc")) {
                    System.out.println("doc selected...");
                    HWPFDocument document = new HWPFDocument(new FileInputStream(file));
                    WordExtractor extrator = new WordExtractor(document);
                    String[] fileData = extrator.getParagraphText();
                    for (i = 0; i < fileData.length; i++) {
                        if (fileData[i] != null) {
                            System.out.println(fileData[i]);
//                            jTextArea1.append(fileData[i]);
                            System.out.println("reading completed");
                        }
                    }
                } else {
                    System.out.println("error");

                }

            }
        } catch (Exception exep) {
            exep.printStackTrace();
        }

    }

    public static String ReadDocFile_working(String str) throws FileNotFoundException, IOException {
        File file = null;
        String line = "";
        WordExtractor extractor = null;
        try {

            file = new File(str);
            FileInputStream fis = new FileInputStream(file.getAbsolutePath());
            HWPFDocument document = new HWPFDocument(fis);
            extractor = new WordExtractor(document);
            String[] fileData = extractor.getParagraphText();
            for (int i = 0; i < fileData.length; i++) {
                if (fileData[i] != null) {
                    System.out.println("hi:" + fileData[i]);
                    line = line + fileData[i];
                }
            }

        } catch (Exception exep) {
            exep.printStackTrace();
        }
        return line;
    }

    public static String copyfiletodirectory(String username, String filename) throws IOException {
        String workingDir = System.getProperty("user.dir");
        File dir = new File(workingDir + "/USERs/" + username);
        System.out.println(" destination  dir:" + dir);
        File inputfile = new File(filename);
        FileUtils.copyFileToDirectory(inputfile, dir);
        String saveddirectory = dir.toString();
        System.out.println("saveddirectory:" + saveddirectory);
        return saveddirectory;

    }

    public static int insertRecord(String file_title, String filename, String path) throws SQLException, ClassNotFoundException {
        int id = 0;
        Connection conn = Login.connectDB();
        Statement stmt = conn.createStatement();

        String query = "select  max(id) from  users";
        System.out.println("query:" + query);
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            id = rs.getInt(1);
            System.out.println(" maxid:" + id);
        }
        System.out.println("path:" + path);
        path = path.replace("/", "  ");
        String sql = "insert  into  filedetail(uid,filename,file_title,filepath) values('" + id + "','" + filename + "','" + file_title + "','" + path + "')";
        System.out.println("sql:" + sql);
        int executeUpdate = stmt.executeUpdate(sql);
        if (executeUpdate > 0) {
            System.out.println(" values are  inserted  successfully");
        } else {
            System.out.println(" values are   not  inserted");

        }
        String sql_query = "select  max(fileid) from   filedetail";
        ResultSet rs1 = stmt.executeQuery(sql_query);
        while (rs1.next()) {
            id = rs1.getInt(1);
            System.out.println("file id  " + id);
        }
        return id;
    }

    public static boolean get_Title() {
        boolean isname;
        String showInputDialog = JOptionPane.showInputDialog("Please Mention Document  Title");
        System.out.println("showInputDialog" + showInputDialog);

        if (showInputDialog != null && !showInputDialog.isEmpty()) {
            isname = true;
            MainGUI.Title = showInputDialog;
        } else {
            isname = false;
        }

        return isname;
    }

    public static String gettitle_db(String filename) throws SQLException, ClassNotFoundException {
        String title = null;
        Connection conn = Login.connectDB();
        String query = "select  file_title from  filedetail  where filename ='" + filename + "' ";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            title = rs.getString("file_title");
        }
        return title;
    }

    protected void openFile() throws IOException, SQLException, ClassNotFoundException {
        String showInputDialog = "";
        int returnVal = fc.showOpenDialog(this);

        if (returnVal == 0) {
            File selectedFile = fc.getSelectedFile();
            boolean value = false;
            while (!value) {
                value = get_Title();
            }
            System.out.println("title:" + Title);
            Title_document.add(Title);
            System.out.println("Title_document size  :" + Title_document);

            selectedfiles.add(selectedFile);
            System.out.println("selectedfiles:" + selectedfiles.size());


            String message = "Do  You want to select  more Files";
            JFrame frame = new JFrame();
            int answer = JOptionPane.showConfirmDialog(frame, message);
            if (answer == JOptionPane.YES_OPTION) {
                openFile();
          
            } else if (answer == JOptionPane.NO_OPTION) {
                for (int j = 0; j < selectedfiles.size(); j++) {
                    File file = selectedfiles.get(j);
                    String tit = Title_document.get(j);
                    System.out.println("current user :" + Login.username + "selectedfilename:" + file);
                    String filesavedpath = copyfiletodirectory(Login.username, file.toString());
                    String src = file.toString();
                    src = src.substring(src.lastIndexOf("\\") + 1, src.length());
                    System.out.println("src:" + src);
                    int current_fileid = insertRecord(tit, src, filesavedpath);
                    System.out.println("current_fileid:" + current_fileid);
                    System.out.println("--------------------------");
                }

            }

        } else {
            System.out.println("Open command cancelled by user.");
            if (selectedfiles.size() > 1) {
                System.out.println("do processing");
            } else {

                System.out.println("No  files Selected  yet  ..");
            }

        }
    }

    protected void setEditorPanePage(File file, String data, int current_index, int threshold) {
        Highlighter h = jEditorPaneSource.getHighlighter();
//        h.removeAllHighlights();
        Document doc = jEditorPaneSource.getDocument();
        doc.putProperty("stream", null);
        try {
            URL helpURL = file.toURI().toURL();
            if (helpURL != null) {
                try {
//                    data = data + Files.readFromFile(file, "ISO-8859-1");
//                    System.out.println("readFromFile:" + data);
//                    if (current_index == threshold - 1) {
//                    jEditorPaneSource.setText(Files.readFromFile(file, "ISO-8859-1"));
                    jEditorPaneSource.setText(data);
                    jEditorPaneSource.setCaretPosition(0);
             } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println((new StringBuilder()).append("Attempted to read a bad URL: ").append(helpURL).toString());
                }
            } else {
                System.err.println("Couldn't find URL...");
            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
    }

    protected void saveFile() {
        int returnVal = fc.showSaveDialog(this);
        if (returnVal == 0) {
            try {
                File OutFile = fc.getSelectedFile();
                FileWriter myWriter = null;
                BufferedWriter outStream = new BufferedWriter(new FileWriter(OutFile));

                if (OutFile.canWrite() || !OutFile.exists()) {
                    myWriter = new FileWriter(OutFile);
                    outStream.append(this.Summury);
                    outStream.flush();
                    outStream.close();
                } else {
                    //pops up error message
                }
            } catch (Exception e) {
            }
        }
    }

    protected void highlightDocument(int indices[], Color highlightColor) {
        if (jEditorPaneSource.getContentType().equals("text/plain")) {
            highlightPlainDocument(indices, highlightColor);
        }
    }

    private void highlightPlainDocument(int indices[], Color highlightColor) {
        Highlighter h = jEditorPaneSource.getHighlighter();
        h.removeAllHighlights();
        jTextAreaSummary.setText("");
        try {
            int startPosition = -1;
            int endPosition = -1;
            String sourceDocument = jEditorPaneSource.getText();
            if (sourceDocument.indexOf("\r") != -1) {
                sourceDocument = sourceDocument.replaceAll("\r", "");
            }
            for (int i = 0; i < indices.length; i++) {
                String str = (String) documentParser.getSentences().get(indices[i]);
                if (str.indexOf("\r") != -1) {
                    str = str.replaceAll("\r", "");
                }
                jTextAreaSummary.append((new StringBuilder()).append("[").append(indices[i] + 1).append("] ").append(str).append("\n\n").toString());
                startPosition = sourceDocument.indexOf(str);
                if (startPosition != -1) {
                    endPosition = startPosition + str.length() + 1;
                    h.addHighlight(startPosition, endPosition, new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(highlightColor));
                }
            }

        } catch (BadLocationException ble) {
            ble.printStackTrace();
        }
        jTextAreaSummary.setCaretPosition(0);
    }

    protected void highlightDocument(String summaries[]) {
        if (jEditorPaneSource.getContentType().equals("text/plain")) {
            highlightPlainDocument(summaries);
        } else if (jEditorPaneSource.getContentType().equals("text/html")) {
            highlightHtmlDocument(summaries);
        }
    }

    private void highlightPlainDocument(String summaries[]) {
        Highlighter h = jEditorPaneSource.getHighlighter();
        h.removeAllHighlights();
        jTextAreaSummary.setText("");
        try {
            int startPosition = -1;
            int endPosition = -1;
            int counter = 0;
            String sourceDocument = jEditorPaneSource.getText();
            if (sourceDocument.indexOf("\r") != -1) {
                sourceDocument = sourceDocument.replaceAll("\r", "");
            }
            String arr$[] = summaries;
            int len$ = arr$.length;
            for (int i$ = 0; i$ < len$; i$++) {
                String str = arr$[i$];
                counter++;
                jTextAreaSummary.append((new StringBuilder()).append("[").append(counter).append("]  ").append(str).append("\n\n").toString());
                startPosition = sourceDocument.indexOf(str);
                if (startPosition != -1) {
                    endPosition = startPosition + str.length() + 1;
                    h.addHighlight(startPosition, endPosition, new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(Color.LIGHT_GRAY));
                }
            }

        } catch (BadLocationException able) {
            able.printStackTrace();
        }
        jTextAreaSummary.setCaretPosition(0);
    }

    private void highlightHtmlDocument(String summaries[]) {
        Highlighter h = jEditorPaneSource.getHighlighter();
        h.removeAllHighlights();
        jTextAreaSummary.setText("");
        try {
            int startPosition = -1;
            int endPosition = -1;
            int counter = 0;
            String arr$[] = summaries;
            int len$ = arr$.length;
            for (int i$ = 0; i$ < len$; i$++) {
                String str = arr$[i$];
                counter++;
                jTextAreaSummary.append((new StringBuilder()).append("[").append(counter).append("]  ").append(str).append("\n\n").toString());
            }

            HTMLDocument sourceDocument = (HTMLDocument) jEditorPaneSource.getDocument();
            for (javax.swing.text.html.HTMLDocument.Iterator it = sourceDocument.getIterator(javax.swing.text.html.HTML.Tag.CONTENT); it.isValid(); it.next()) {
                String fragment = sourceDocument.getText(it.getStartOffset(), it.getEndOffset() - it.getStartOffset());
                String arr$1[] = summaries;
                int len$1 = arr$1.length;
                for (int i$ = 0; i$ < len$; i$++) {
                    String str = arr$1[i$];
                    startPosition = fragment.indexOf(str);
                    if (startPosition != -1) {
                        endPosition = startPosition + str.length() + 1;
                        h.addHighlight(it.getStartOffset() + startPosition, it.getStartOffset() + endPosition, new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW));
                    }
                    startPosition = str.indexOf(fragment);
                    if (startPosition != -1) {
                        endPosition = fragment.length() + 1;
                        h.addHighlight(it.getStartOffset(), it.getStartOffset() + endPosition, new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW));
                    }

                }
            }

        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
        jTextAreaSummary.setCaretPosition(0);
    }

    protected Object[] readAlgorithmsList() {
        BufferedReader inputStream;
        java.util.List algorithmsList;
        inputStream = null;
        algorithmsList = new ArrayList();
        File file = new File("src\\config\\algorithms.txt");
        try {
            inputStream = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            
            e.printStackTrace();
        }

        do {
            String line = null;
            try {
                if ((line = inputStream.readLine()) == null) {
                    break;
                }
            } catch (IOException e) {
               
                e.printStackTrace();
            }
            if (line.trim().length() != 0) {
                algorithmsList.add(line);
            }
        } while (true);

        if (inputStream != null) {
            try {
                inputStream.close();
            } 
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        Object algorithmsStringList[] = algorithmsList.toArray();
        return algorithmsStringList;

    }

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel("com.jtattoo.plaf.fast.FastLookAndFeel");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                (new Login()).setVisible(true);
            }
        });
    }
    private Parser documentParser;
    // private Utilities help;
    private File sourceFile;
    private int summLevel;
    private double freqThreshold;
    private int algorithm;
    private boolean isNewSourceFile;
    private Set jComboBoxURLContents;
    private JFileChooser fc;
    private JButton jButton1;
    private JButton jButtonGo;
    private JButton jButtonOpen;
    private JComboBox jComboBoxAlgorithms;
    private JComboBox jComboBoxURL;
    private JEditorPane jEditorPaneSource;
    private JLabel jLabel1;
    private JLabel jLabelCoverageOfSummaryLabel;
    private JLabel jLabelCoverageOfSummaryValue;
    private JLabel jLabelNumSentencesToExtractLabel;
    private JLabel jLabelNumSentencesToExtractValue;
    private JLabel jLabelOrthogonalityOfSummaryLabel;
    private JLabel jLabelOrthogonalityOfSummaryValue;
    private JLabel jLabelSeparator1;
    private JLabel jLabelSeparator10;
    private JLabel jLabelSeparator12;
    private JLabel jLabelSeparator13;
    private JLabel jLabelSeparator2;
    private JLabel jLabelSeparator8;
    private JLabel jLabelSeparator9;
    private JLabel jLabelStatus;
    private JLabel jLabelSummaryLength;
    private JLabel jLabelTotalNumSentencesLabel;
    private JLabel jLabelTotalNumSentencesValue;
    private JLabel jLabelURL;
    private JMenuBar jMenuBar;
    private JMenu jMenuFile;
    private JMenu jMenuHelp;
    private JMenuItem jMenuItemAbout;
    private JMenuItem jMenuItemExit;
    private JMenuItem jMenuItemOpen;
    private JMenuItem jMenuItemSave;
    private JScrollPane jScrollPaneSource;
    private JScrollPane jScrollPaneSummary;
    private JSeparator jSeparator1;
    private JSeparator jSeparator2;
    private JSlider jSliderSummaryLength;
    private JSplitPane jSplitPaneDocuments;
    private JToolBar jStatusBar;
    private JTextArea jTextAreaSummary;
    private JToolBar jToolBar;
    private String source;
    private String summary = null;
    static final boolean $assertionsDisabled = true;

    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    class TypeOfFile extends FileFilter {
        //Type of file that should be display in JFileChooser will be set here
        //We choose to display only directory and text file

        public boolean accept(File f) {
            return f.isDirectory() || f.getName().toLowerCase().endsWith(".txt") || f.getName().toLowerCase().endsWith(".rtf") || f.getName().toLowerCase().endsWith(".doc") || f.getName().toLowerCase().endsWith(".docx");
//                    || f.getName().toLowerCase().endsWith(".html") || f.getName().toLowerCase().endsWith(".xml") || f.getName().toLowerCase().endsWith(".php")
//                    || f.getName().toLowerCase().endsWith(".jsp");
        }

        //Set description for the type of file that should be display
        public String getDescription() {
            return ".text & doc files";
        }
    }

    public static String readDocxfile(String fileName) {
        String line = "";
        try {
            File file = new File(fileName);
            FileInputStream fis = new FileInputStream(file.getAbsolutePath());
            XWPFDocument document = new XWPFDocument(fis);
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            System.out.println("" + paragraphs);
            System.out.println("Total no of paragraph " + paragraphs.size());
            for (XWPFParagraph para : paragraphs) {
                System.out.println(para.getText());
                String text = para.getText()+"\n";

                line = line + text;
            }
            System.out.println("line para:" + line);
            fis.close();


        } catch (Exception e) {


            e.printStackTrace();
        }
        return line;
    }

    public static void readDocfile(File files, String filename) throws FileNotFoundException, IOException {


        FileWriter myWriter = null;
        FileOutputStream fop = null;
        File file = null;
        System.out.println("doc selected...");
        HWPFDocument document = new HWPFDocument(new FileInputStream(files));
        WordExtractor extrator = new WordExtractor(document);
        String[] fileData = extrator.getParagraphText();


        for (int i = 0; i < fileData.length; i++) {
            if (fileData[i] != null) {
                System.out.println(fileData[i]);
                fileData[i] = fileData[i].trim().replaceAll("\\s+", " ");
                System.out.println("fileData: " + fileData[i]);

//                            jTextArea1.append(fileData[i]);
                System.out.println(" getParagraphText completed");
            }

        }
        try {

            file = new File(filename);
            System.out.println("writinng file name :" + file);

            fop = new FileOutputStream(file);
            byte[] contentInBytes = null;
            if (!file.exists()) {
                file.createNewFile();
            }
            for (int i = 0; i < fileData.length; i++) {
                contentInBytes = fileData[i].getBytes();
                fop.write(contentInBytes);
                System.out.println("writing file");
                fop.flush();
            }
            fop.close();
            System.out.println("Done");
        } catch (Exception ee) {
            ee.printStackTrace();


        } finally {
            try {
                if (fop != null) {
                    fop.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static int CheckfilesIndirectory(String Path) {
        String fname;
        int count = 0;
        File f = new File(Path);
        File[] listFiles = f.listFiles();
        for (int i = 0; i < listFiles.length; i++) {
            File file = listFiles[i];
            if (!file.isDirectory()) {
                fname = file.getName();
                if (fname.endsWith(".txt") || fname.endsWith(".doc") || fname.endsWith(".docx")) {
                    count++;
                }
            }
        }
//        System.out.println("count:" + count);
        return count;
    }

    public static String getCurrentUserdirectory() {
        String username = Login.username;
        String workingDir = System.getProperty("user.dir");
        String path = workingDir + "/USERs/" + username;
        return path;
    }

    public static String getusercluster(int uid) {
        String filename = null;
        String finalval = null;
        int count = 0;
        try {
            Connection conn = Login.connectDB();
            Statement stmt = conn.createStatement();

            String sql = "select cluster_id,cluster_value from clusterset2 where uid='" + uid + "'";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int cid = rs.getInt("cluster_id");
                String str = rs.getString("cluster_value");
                filename = filename + str + ",";
                count++;
            }
            if (count == 0) {
                String valueOf = String.valueOf(count);
                finalval = count + "," + 0;
                System.out.println("final  val:" + finalval);
            } else {
                int ct = 0;
                String[] split = filename.split(",");
                for (int i = 0; i < split.length; i++) {
                    String string = split[i];
                    ct++;
                    System.out.println("files:" + string);

                }
                System.out.println("total  files:" + split);
                String valueOf = String.valueOf(count);
                finalval = count + "," + ct;
                System.out.println("final  val:" + finalval);
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }
        System.out.println(" user clusters:" + count);
        return finalval;
    }

    public static int getuserid() throws SQLException, ClassNotFoundException {
        String user = Login.username;
        int id = 0;
        Connection con = Login.connectDB();
        Statement stmt = con.createStatement();
        String query = "select  id  from  users where username='" + user + "'";
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            id = rs.getInt("id");
        }
        return id;
    }

    public int select_Clusters(String[] options) {
        System.out.println("jButtonViewActionPerformed");
        int messageType = JOptionPane.QUESTION_MESSAGE;
//        String[] options = {"Java", "C++", "VB", "PHP", "Perl"};
        int code = JOptionPane.showOptionDialog(myFrame,
                "Select Cluster for  processing?",
                "Option Dialog Box", 0, messageType,
                null, options, "PHP");
        System.out.println("Answer: " + code);
        return code;
    }

    public HashMap<String, String> getuser_Clusters(int userid) throws SQLException, ClassNotFoundException {
        HashMap<String, String> clusterdata = new HashMap<String, String>();
        Connection con = Login.connectDB();
        Statement stmt = con.createStatement();
        String query = "select cluster_name,cluster_value from clusterset2 where uid='" + userid + "'";
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            System.out.println("");
            String clus_name = rs.getString("cluster_name");
            String clus_val = rs.getString("cluster_value");
            clusterdata.put(clus_name, clus_val);
        }
        return clusterdata;
    }

    public static int getfileid(int uid, String filename) throws SQLException, SQLException, ClassNotFoundException {
        int fileid = 0;
        Connection conn = Login.connectDB();
        Statement stmt = conn.createStatement();
        String sql = "select fileid from  filedetail where filename='" + filename + "' and uid='" + uid + "' ";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            fileid = rs.getInt("fileid");

        }

        return fileid;
    }

    public static String getsummurysenetnces(int fileid, int sentenceid, Statement stmt) throws SQLException {
        String sum = null;
        String query = "select  sentence from sentence_data where fileid='" + fileid + "'and sentenceid='" + sentenceid + "' ";
//        System.out.println("query:" + query);
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            sum = rs.getString("sentence");
        }
//        System.out.println("sentence:"+sum);
        return sum;

    }

    public ArrayList<Integer> getsentenceweight(String filename, int uid, Connection conn, Statement stmt) {
        TreeMap<Integer, Double> weightdata = new TreeMap<Integer, Double>(Collections.reverseOrder());
        ArrayList<Integer> summury_senid = new ArrayList<Integer>();
        try {
            int fileid = 0;
            int sid = 0;
            double weight = 0;
            double docweight = 0;
            double avg_weight;
            int count = 0;
            String query = "select fileid from filedetail where filename= '" + filename + "'and uid='" + uid + "'  ";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                fileid = rs.getInt("fileid");
            }
            String sql = "select sentenceid,weight from sentence_data where fileid='" + fileid + "' ";
            ResultSet rs1 = stmt.executeQuery(sql);
            while (rs1.next()) {
                sid = rs1.getInt("sentenceid");
                weight = rs1.getDouble("weight");
                weightdata.put(sid, weight);
                count++;
            }

            for (Map.Entry<Integer, Double> entry : weightdata.entrySet()) {
                Double docw = entry.getValue();
                docweight = docweight + docw;
            }
            System.out.println("overall  weight  of document:" + docweight);
            avg_weight = docweight / count;
            for (Map.Entry<Integer, Double> entry : weightdata.entrySet()) {
                Double docw = entry.getValue();
                if (docw > avg_weight) {
                    Integer id = entry.getKey();
                    summury_senid.add(id);
                }
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }
        return summury_senid;
    }
}
