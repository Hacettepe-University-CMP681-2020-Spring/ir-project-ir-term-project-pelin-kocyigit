/**
 *
 * @author Pelin Kocyigit
 */


package summarization;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

/*
    This class is to create interface of the project.
*/

public class Interface {
    
    public  JFrame frame1; // main screen
    public  JPanel panel1;// panel for Tab(1)
    JPanel panel2;// panel for Tab(2)
    JPanel panel3;// panel for Tab(3)
    JPanel panel4;// panel for Tab(4)
    JTabbedPane tab;// contains panels
    public  JButton loadTextButton; // loads selected text
    public  JButton loadReferenceButton; // loads selected text
    public  JButton preprocessingButton; // applies preprocessing algortihms
    public  JButton processingButton; // applies preprocessing algortihms
    public  JButton generationButton; // applies preprocessing algortihms
    JButton exitButton;
    public static JTextArea orjTextBox; //contains original text
    public static JTextArea modifiedTextBox; //contains original text 
    public static JTextArea requiredLength;
    public  String textPath ; // path of the loaded text
    public static String orjText; // original text
    public static String refText; // original text
    public Interface(){
        Frame();
        Panel();
        Button();
        TextField();       
    }
    
    /*
        This is to create JFrame components
    */
    
    public void Frame(){
        frame1 = new JFrame("Summarization"); // creates main page of the interface
        frame1.setUndecorated(true); // make unvisible Frame header bar
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.getContentPane().setLayout(null); // to allow changing the dimensions of other components
        frame1.setSize(1250,680);
        frame1.getContentPane().setBackground(Color.red);
        frame1.getContentPane().setVisible(true);
        frame1.setVisible(true);// makes visible main frame
    }
    
    /*
        This is to create JPanel components
    */
    
    public void Panel(){
        panel1 = new JPanel(); // first page of the Tab Control
        panel1.setLayout(null);
       // panel1.setBounds(0, 0, 300, 300);
        panel1.setBackground(Color.white);

        panel2 = new JPanel(); // second page of the Tab Conrtol
        panel2.setLayout(null);
        panel2.setBackground(Color.white);

        panel3 = new JPanel(); // third page of the Tab Control
        panel3.setLayout(null);
        panel3.setBackground(Color.white);

        panel4 = new JPanel(); // fourth page of the Tab Control
        panel4.setLayout(null);
        panel4.setBackground(Color.white);
        
        JPanel borderPanel=new JPanel(); // hides tab border
        borderPanel.setLayout(null);
        borderPanel.setBackground(Color.white);
        borderPanel.setBounds(0,0,1250,25);
        frame1.add(borderPanel);        

        tab = new JTabbedPane(); // Tab Control to provide interaction between pages
        tab.setSize(1250,680);
        tab.addTab("Tab1", panel1);
        tab.addTab("Tab2", panel2);
        tab.addTab("Tab3", panel3);
        tab.addTab("Tab4", panel4);
        tab.setBackground(Color.orange);
        tab.setVisible(true);
               
        frame1.getContentPane().add(tab);
    }
    
    /*
        This is to create JButton components
    */
    
    public void Button(){
        
        //Panel 1 Buttons
        loadTextButton = new JButton(" Load Text "); // selects and loads recognition image
        loadTextButton.addActionListener(new ButtonListener());
        loadTextButton.setBounds(30,600,120,30);
        loadTextButton.setBackground(Color.BLACK);
        loadTextButton.setForeground(Color.white);
        panel1.add(loadTextButton);
        
        loadReferenceButton = new JButton(" Load Reference "); // selects and loads recognition image
        loadReferenceButton.addActionListener(new ButtonListener());
        loadReferenceButton.setBounds(160,600,150,30);
        loadReferenceButton.setBackground(Color.BLACK);
        loadReferenceButton.setForeground(Color.white);
        panel1.add(loadReferenceButton);
        
        preprocessingButton = new JButton(" Preprocessing "); // selects and loads recognition image
        preprocessingButton.addActionListener(new ButtonListener());
        preprocessingButton.setBounds(320,600,150,30);
        preprocessingButton.setBackground(Color.BLACK);
        preprocessingButton.setForeground(Color.white);
        panel1.add(preprocessingButton);
       
        
        processingButton = new JButton(" Processing "); // selects and loads recognition image
        processingButton.addActionListener(new ButtonListener());
        processingButton.setBounds(480,600,150,30);
        processingButton.setBackground(Color.BLACK);
        processingButton.setForeground(Color.white);
        panel1.add(processingButton);
        
        generationButton = new JButton(" Generation "); // selects and loads recognition image
        generationButton.addActionListener(new ButtonListener());
        generationButton.setBounds(640,600,150,30);
        generationButton.setBackground(Color.BLACK);
        generationButton.setForeground(Color.white);
        panel1.add(generationButton);
        
        exitButton = new JButton(" Exit "); // exits from program in Tab -I
        exitButton.addActionListener(new ButtonListener());
        exitButton.setBounds(1050,600,120,30);
        exitButton.setBackground(Color.BLACK);
        exitButton.setForeground(Color.white);
        panel1.add(exitButton);
    }
        
    /*
    * This class is to listen actions of the buttons.
    */    
    
    public class ButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            int choise;

            if (e.getSource() == loadTextButton) { // loads selected image 

                JFileChooser chooser = new JFileChooser(); // creates a chooser dialog for file selection
                chooser.setMultiSelectionEnabled(false);
                chooser.setAcceptAllFileFilterUsed(false);

                FileNameExtensionFilter filter = new FileNameExtensionFilter("Documents","doc", "docx","DOC","DOCX");
                chooser.setFileFilter(filter);
                choise = chooser.showOpenDialog(panel1); // selected file from chooser

                if(choise == JFileChooser.APPROVE_OPTION) {  // file is acceptable
                    textPath = chooser.getSelectedFile().getAbsolutePath(); // holds selected text path
                    XWPFDocument doc;  
                    try  
                    {  
                        doc = new XWPFDocument(new FileInputStream(textPath));  
                        XWPFWordExtractor wordxExtractor = new XWPFWordExtractor(doc);  
                        orjText = wordxExtractor.getText();
                        orjTextBox.setText(orjText);
                    }  
                    catch (IOException ex)  
                    {  
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);  
                    }  
                    }
            }
            
            if (e.getSource() == loadReferenceButton) { // loads reference summary

                JFileChooser chooser = new JFileChooser(); // creates a chooser dialog for file selection
                chooser.setMultiSelectionEnabled(false);
                chooser.setAcceptAllFileFilterUsed(false);

                FileNameExtensionFilter filter = new FileNameExtensionFilter("Documents","doc", "docx","DOC","DOCX");
                chooser.setFileFilter(filter);
                choise = chooser.showOpenDialog(panel1); // selected file from chooser

                if(choise == JFileChooser.APPROVE_OPTION) {  // file is acceptable
                    textPath = chooser.getSelectedFile().getAbsolutePath(); // holds selected text path
                    XWPFDocument doc;  
                    try  
                    {  
                        doc = new XWPFDocument(new FileInputStream(textPath));  
                        XWPFWordExtractor wordxExtractor = new XWPFWordExtractor(doc);  
                        refText = wordxExtractor.getText();
                        modifiedTextBox.setText(refText);
                    }  
                    catch (IOException ex)  
                    {  
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);  
                    }  
                }
                
                
            }

            if (e.getSource() == preprocessingButton ){ // starts preprocessing step
                new Preprocessing();
            }
            
            if (e.getSource() == processingButton ){ // starts preprocessing step
                new Processing(); 
            }
            
            if (e.getSource() == generationButton ){ // starts preprocessing step
                new Generation();
            }

            if (e.getSource() == exitButton ){ // exits from program
                System.exit(0);
                try {
                    Preprocessing.conn.close(); // closes MySql connection
                } catch (SQLException ex) {
                    Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }            
    }
     
    /*
        This is to create JLbale components
    */
    
    public void TextField(){      
        
        orjTextBox = new JTextArea(); // displays original text
        panel1.add(orjTextBox);
        orjTextBox.setBounds(3, 0, 600, 580);
        JScrollPane scrollPane = new JScrollPane(orjTextBox);
        scrollPane.setBounds(3,0, 600,580);
        panel1.add(scrollPane);        
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        orjTextBox.setLineWrap(true);
        scrollPane.validate();
        
        modifiedTextBox = new JTextArea(); // displays original text
        panel1.add(modifiedTextBox);
        modifiedTextBox.setBounds(620, 0, 600, 580);
        JScrollPane scrollPane2 = new JScrollPane(modifiedTextBox);
        scrollPane2.setBounds(620,0, 600,580);
        panel1.add(scrollPane2);        
        scrollPane2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        modifiedTextBox.setLineWrap(true);
        scrollPane2.validate();
        
        requiredLength = new JTextArea(); // displays original text
        panel1.add(requiredLength);
        requiredLength.setBounds(800, 600, 50, 20);
        requiredLength.setBackground(Color.GRAY);
        requiredLength.setLineWrap(true);
    }
    
}
