/**
 *
 * @author Pelin Kocyigit
 */


package summarization;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
    This class is to execute generation step ot the text summarization task.
*/

public class Generation {
    
    private Connection conn = Preprocessing.conn;
    private Statement statement;
    private int requiredSummaryLength;
    private float sentenceW1, sentenceW2, totalWeight=0;
    private  HashMap<Integer, String> summaryTextMap; // holds sentences of the original text.    
    
    /*
        This is to construct the class.
    */
    
    public Generation(){
        System.out.println("\n--------------------------GENERATION----------------------------------------\n");
        String sentence;
        
        getSummaryLength();
        summaryTextMap = (HashMap) Preprocessing.resultTextMap.clone();  // to display on the interface.     
        System.out.println("required sum length: "+requiredSummaryLength);
        
        for(int i=0; i<summaryTextMap.size(); i++){
            getSentenceInformation(i);
        }
        createSummary();
        showSummary();        
    }
    
    private void getSummaryLength(){
        requiredSummaryLength = Integer.parseInt(Interface.requiredLength.getText());
    }
    
    /*
        This is to get weight information of the selected sentence.
    */
    
    private void getSentenceInformation(int id){
        try {
            statement = conn.createStatement(); 
            String query = "select sentenceWeight1, sentenceWeight2, totalWeight from sentences where sentenceId = '"+id+"'";
            ResultSet rs = statement.executeQuery(query);
            if(rs.next()){
                sentenceW1 = rs.getFloat("sentenceWeight1"); 
                sentenceW2 = rs.getFloat("sentenceWeight2");
                totalWeight = rs.getFloat("totalWeight");
            }   
        } catch (SQLException ex) {
            Logger.getLogger(Preprocessing.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }
    
    /*
        This is to generate summary of the text.
    */
    
    private List<Integer> selectedSentencesId = new ArrayList<>();
    private List<String> selectedSentences = new ArrayList<>();
    private String outputSummary = "";
    private void createSummary(){
        try {
            statement = conn.createStatement();
            String query = "select sentenceId, orjSentence from sentences order by totalWeight desc limit "+requiredSummaryLength+" ";
            ResultSet rs = statement.executeQuery(query);
            while(rs.next()){
               selectedSentencesId.add(rs.getInt(1));
               selectedSentences.add(rs.getString("orjSentence"));
               outputSummary += rs.getString("orjSentence"); // to use in evaluation.
            }   
        } catch (SQLException ex) {
            Logger.getLogger(Preprocessing.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    /*
        This is to display created summary in text field of the interface.
    */
    
    private void showSummary(){
        String summary = "";
        Interface.orjTextBox.setText(Interface.refText);
        for(int i=0; i<selectedSentencesId.size(); i++){
            summary += selectedSentencesId.get(i) + "-) " + selectedSentences.get(i) + "\n";
        }
        Interface.modifiedTextBox.setText(summary);
    }
    
}
