/**
 *
 * @author Pelin Kocyigit
 */


package summarization;

import java.math.RoundingMode;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
    This class is to execute processing step ot the text summarization task.
*/

public class Processing {
    
    
    public Connection conn = Preprocessing.conn;
    public Statement statement;   
    public DecimalFormat df;
    public float totalExtFeature;
    public float totalPunctuation = -1, totalLinking = -1, totalBonus = -1, totalCapital = -1, totalNumeric = -1;
    public float totalParagraph = Preprocessing.totalParagraph; // w5
    public float w1=0, w2=0, w3=0, w4=0, w5=0, w6=0, w7=0;
   
    
    /*
        This is to construct the class.
    */
    
    public Processing(){        
        System.out.println("\n--------------------------PROCESSING----------------------------------------\n");
        df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.CEILING);
        calculateWordWeights();
        calculateWeightsForFirstStep();
    }        
        
    
    /*****************************************************************************************************************************************************************/
    /******************************************************** METHODS TO CALCULATE SENTECE WEIGHT-1 *****************************************************************/
    /***************************************************************************************************************************************************************/
     
    /*
        This is to start calculation of both sentence weights 1 and 2 for each sentence.
    */
    
    private void calculateWeightsForFirstStep(){
        String sentence;
        float sentencew1;
        
        calculateExtFeaturesWeights();   
        for(int i=0; i< Preprocessing.sentenceCount; i++){
            sentence = Preprocessing.resultTextMap.get(i);
            sentencew1 = getSentenceWeightForExtFeatures(i); // 
            recordSentenceWeightToDB("sentenceWeight1", i, sentencew1);
            getSentenceWeightForWords(i, sentence); // kendi sonucu direk kaydediyor
        }        
        calculateTotalWeights();
    }
    
    /*
        This is to calculate word frequencies (WF) in processed sentences.
    */       
    
    private void calculateExtFeaturesWeights(){
        totalExtFeature = 0;
        getTotalFeature("punctuation"); // w1
        getTotalFeature("linkingWord"); // w2
        getTotalFeature("bonusWord");  // w3
        getTotalFeature("capitalLetter"); // w4
        getTotalFeature("numericValue"); // w6
        getFeatureWeights();
    }
    
    /* 
        This is to get total count of features of the text from the database.
    */
     
    private void getTotalFeature(String column){
        try {
            statement = conn.createStatement();
            String query = "select COUNT(*) from sentences where "+column+" = 1"; 
            ResultSet rs = statement.executeQuery(query);
            while(rs.next()){
                if("punctuation".equals(column))
                    totalPunctuation = rs.getInt(1);
                
                if("linkingWord".equals(column))
                    totalLinking = rs.getInt(1);
                
                if("bonusWord".equals(column))
                    totalBonus = rs.getInt(1);
                
                if("capitalLetter".equals(column))
                    totalCapital = rs.getInt(1);
                
                if("numericValue".equals(column))
                    totalNumeric = rs.getInt(1);             
            }             
        } catch (SQLException ex) {
            Logger.getLogger(Preprocessing.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    /*
        This is to calculate weight of each feature.
    */
    
    private void getFeatureWeights(){
        // w1+w2+w3+w4+w5+w6
        totalExtFeature = (totalPunctuation + totalLinking + totalBonus + totalCapital + totalParagraph + totalNumeric );
        w1 = (totalPunctuation / totalExtFeature);
        w2 = (totalLinking / totalExtFeature);
        w3 = (totalBonus / totalExtFeature);
        w4 = (totalCapital / totalExtFeature);
        w5 = (totalParagraph / totalExtFeature);
        w6 = (totalNumeric / totalExtFeature);
        
        w1 = Float.parseFloat(df.format(w1));
        w2 = Float.parseFloat(df.format(w2));
        w3 = Float.parseFloat(df.format(w3));
        w4 = Float.parseFloat(df.format(w4));
        w5 = Float.parseFloat(df.format(w5));
        w6 = Float.parseFloat(df.format(w6));
        
    }
    
    /*
        This is to get features of each sentence.
    */
  
    private int wordCountOfSentence; // holds count of all words in the sentence.
    private float getSentenceWeightForExtFeatures(int id){
        float result = 0;
        int punc=0, linking=0, bonus=0, capital=0, paragraph=0, numeric=0;
        
        try {
            statement = conn.createStatement(); 
            String query = "select wordCount, punctuation, linkingWord, bonusWord, capitalLetter, paragraphStart, numericValue from sentences where sentenceId = '"+id+"'";
            ResultSet rs = statement.executeQuery(query);
            if(rs.next()){
                wordCountOfSentence = rs.getInt("wordCount");
                punc = rs.getInt("punctuation");
                linking = rs.getInt("linkingWord");
                bonus = rs.getInt("bonusWord");
                capital = rs.getInt("capitalLetter");
                paragraph = rs.getInt("paragraphStart");
                numeric = rs.getInt("numericValue");
                result = ( (punc*w1) +(linking*w2) + (bonus*w3) + (capital*w4) + (paragraph*w5) + (numeric*w6) ) ;
      
            }             
        } catch (SQLException ex) {
            Logger.getLogger(Preprocessing.class.getName()).log(Level.SEVERE, null, ex);
        } 
        result = Float.parseFloat(df.format(result));
         
        return result;
    }
    
    /*
        This is to record sentence wieghts to the database.
    */
    
    private void recordSentenceWeightToDB(String column, int sentenceId, float SentenceWeight){
        try {
            statement = conn.createStatement(); // bir kez oluşturmak için burdda
            String query = "update sentences set "+column+" = '"+SentenceWeight+"' where sentenceId = '"+sentenceId+"'"; 
            statement.executeUpdate(query);            
        } catch (SQLException ex) {
            Logger.getLogger(Preprocessing.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }

    
    /*****************************************************************************************************************************************************************/
    /******************************************************** METHODS TO CALCULATE SENTECE WEIGHT-2 *****************************************************************/
    /***************************************************************************************************************************************************************/
    
    /*
        This is to calculate words weights for each sentence.
    */
    
    /// değerler:
    //    Preprocessing.sentenceCount = toplam cümle sayısı
    //    Preprocessing.uniqueWordMap.size() = toplam kelime sayısı (veritabanındaki)
    //    Seçilen kelimenin geçtiği cümle sayısı = wordSentenceCount
    //    Seçilen kelimenin toplam metinde kaç kere geçtiği = totalCount
    //    Cümledeki toplam kelime sayısı ( hepsi dahil- orj cümle tüm kelimeler) = wordCountOfSentence
    
    private void calculateWordWeights(){
        try {
            CallableStatement stm = conn.prepareCall("{call getWordWeight()}");
            stm.execute();
            stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(Processing.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    private void getSentenceWeightForWords(int sentenceId, String sentence){
        String word;
        String[] tmp = sentence.split(" ");    
        float WF, SF, sentenceWeight2 = 0f;
        int length = tmp.length;
        SF = 0;       
        
        //  word weights
        int s = 0; // dolu olan kelimeri alacak
        for(int j=0; j<length; j++){
            if (tmp[j].length() > 0){
                word = tmp[j]; 
                
                if( Preprocessing.uniqueWordMap.containsValue(word)){
                    getWordInfoFromDB(word);
                    //WF = ( (float)(Preprocessing.wordCount) / (float)(selectedWordTotalCount) ); // effect of word
                    //WF = (float)( totalCount * (float)(Preprocessing.sentenceCount / wordSentenceCount) );
                    //System.out.println("word:"+word+"--weight:"+WF);
                    //WF = (float)( totalCount * ((float) Preprocessing.sentenceCount / (float) wordSentenceCount) );
                    WF = wordWeight;
                    System.out.println("word:"+word+"--wordWeight:"+wordWeight);
                    SF += WF;  
                    s ++;
                }
            }              
        }
       
        sentenceWeight2 = SF/ (float)s;
        sentenceWeight2 = Float.parseFloat(df.format(sentenceWeight2));
        System.out.println("sentence:"+sentence+"--weight2:"+sentenceWeight2);
        recordSentenceWeightToDB("sentenceWeight2", sentenceId, sentenceWeight2);
    }
    
    /*
        This is to get each word's information which are contained in the selected sentence from the database.
    */
    
    private float wordWeight;
    private void getWordInfoFromDB(String word){
        try {
            statement = conn.createStatement(); // bir kez oluşturmak için burdda
            String query = "select weight  from words where word = '"+word+"'";
            ResultSet rs = statement.executeQuery(query);
            while(rs.next()){
                wordWeight = rs.getFloat(1);
                System.out.println("word:"+word+ "geldim:"+wordWeight);
            }             
        } catch (SQLException ex) {
            Logger.getLogger(Preprocessing.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    /*
        This is to run database procedure which calculates total weights of sentences by using sentence weights 1 and 2.
    */
    
    private void calculateTotalWeights(){
        try {
            CallableStatement stm = conn.prepareCall("{call getTotalSentenceWeights()}");
            stm.execute();
            stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(Processing.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
