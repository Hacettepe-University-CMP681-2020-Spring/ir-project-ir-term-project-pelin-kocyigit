/**
 *
 * @author Pelin Kocyigit
 */


package summarization;

import com.mysql.jdbc.Connection;
import java.awt.Color;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;
import org.tartarus.snowball.ext.PorterStemmer;

/*
    This class is to execute preprocessing step of the text summarization task.
*/
 
public class Preprocessing {

    
    public  HashMap<Integer, String> orjTextMap = new HashMap<>(); // holds sentences of the original text.
    public  static HashMap<Integer, String> processedTextMap = new HashMap<>(); // holds sentences of the original text.  
    public  HashMap<Integer, String> orjWordMap = new HashMap<>();
    public static HashMap<Integer, String> uniqueWordMap = new HashMap<>();
    public  List<String> stopWords; // holds stop-words which are needed to remove from the text.
    public  List<String> punctuations; // holds punctuations which may indicate importance to the corresponding sentence.
    public  List<String> linkingWords; // holds defined words which may indicate importance to the corresponding sentence.
    public  List<String> bonusWords; // holds defined words which may indicate importance to the corresponding sentence.
    public static int sentenceCount = 0; // holds sentence count of the text.
    public static int wordCount = 0; // holds word count of the text which are considered in sentence weight, so required and stemmed ones.  
    public  static String text = Interface.orjText;   
    public static Connection conn;
    public static Statement statement;

    /*
        This is to construct the class.
    */
    
    public Preprocessing(){
        System.out.println("\n--------------------------PREPROCESSING----------------------------------------\n");
        Interface.modifiedTextBox.setText("");
             
        createLists();     
        connectToDatabase();        
        splitToSentence();
        getSentenceFeatures();
        cleanSentences(); 
        showDetectedFeatures();
        splitToWords(); 
        
        System.out.println("sentenceCount: "+sentenceCount);
        System.out.println("wordCount: "+wordCount);
        System.out.println("total paragraph:"+ totalParagraph);
        System.out.println("------------------------------------------");
        System.out.println("orjTextMap: "+orjTextMap);
        System.out.println("processedTextMap: "+processedTextMap);
        System.out.println("orjWordMap: "+orjWordMap);
        System.out.println("uniqueWordMap: "+uniqueWordMap);
    }    
    
    /*
        This is to initialize lists requires for processing.
    */
    
    public void createLists(){
        punctuations = Arrays.asList("!", "?", ":", ";", "\"", "\'", "(");
        
        linkingWords = Arrays.asList(" firstly ", " secondly ", " thirdly ", " finally ", " in addition ", " moreover ", " furthermore ", " in conclusion ", " to summarise ", " to sum up ", " so ", 
        " as a result ", " as a consequence ", " therefore ", " thus ", " consequently ", " hence ", " if ", " indeed ", " in fact ", " in addition ", " additionally ", " furthermore ", " because ", " for example ",
        " for instance ", " that is ", " namely ", " however ", " neverthless ", " nonetheless ", " although ", " even though ", " though ", " but ", " whereas ", " on the other hand ", " on the contrary ",
        " similarly ", " likewise ");       
        
        bonusWords = Arrays.asList(" stronger ", " biggest ", " giant ", " more ", " most ", " less ", " least ", " lower ", " higher ", " half ", " quarter ", " similar to ", " same ", " very ", " better ", " best ", " worse ", " worst ", " first ", " second ", " third ",
        " fourth ", " final ", " two ", " three ", " such as ", " including ", " etc ");  
        
        stopWords = Arrays.asList(" a ", " about ", " above ", " after ", " again ", " against ", " aggressively ", " ain't ", " all ", " already ", " am ", " among ", " an " ," and ", " any ", " are ", " aren't ",
        " as ", " at ", " be ", " been ", " before ", " being ", " behind ", " both ", " by ", " can ", " can't ", " cannot ", " could ", " couldn't ", " did ", " didn't ", " do ", " does ", " doesn't ", " doing ", " during ", " especially ", " even ", " every ", " following ",
        " don't ", " each ", " for ", " from ", " general ", " had ", " hadn't ", " has ", " hasn't ", " have ", " haven't ", " having ", " her ", " here ", " here's ", " his ", " if ", " in ", " into ", " is ", " isn't ", " it ", " its ", " itself ", " just ",
        " let's ", " many ", " may ", " much ", " must ", " mustn't ", " my ", " neverthless ", " no ", " non ", " nor ", " not ", " numerous ", " now ", " of ", " off ", " often ", " oh ", " on ", " one ", " or ", " ought ", " out ", " our ", " own ", " other ", " others ", " primarily ", " potentially ", " previously", " shall ", " slightly ", " shan't ", " shouldn't ", " still ", " such ", " than ", " that ",
        " the ", " their ", " these ", " there ", " there's ", " through ", " this ", " toward ", " towards ", " up ", " us ", " to ", " various ", " via ", " yet ", " was ", " wasn't ", " were ", " weren't ", " which ", " when ", " where ", " who ", " whom ", " will ", " with ", " without ", " won't ", " would ", " wouldn't " );        
    }  
    
    /*
        This is for providing database connection.
    */       
    
    public void connectToDatabase(){        
        try {
            String url1 = "jdbc:mysql://localhost:3306/summarization";
            String user = "root";
            String password = "1234";
            Statement stm;
            String query;
 
            conn = (Connection) DriverManager.getConnection(url1, user, password);
            if (conn != null) {
                System.out.println("Connected to the database!");
                query = "truncate table words";
                stm = conn.createStatement();
                stm.executeQuery(query);
                stm.close();                
                
                query = "truncate table sentences";
                stm = conn.createStatement();
                stm.executeQuery(query);
                stm.close();
            }
        } catch (SQLException ex) {
            System.out.println("An error occurred during db connection!");
            ex.printStackTrace();
        }         
    }
        
    
    /********************************************************************************************************************************************************************/
    /**************************************************************** METHODS TO SLIP THE TEXT INTO SENTENCES **********************************************************/
    /******************************************************************************************************************************************************************/
        
    /*
        This is to get sentences of the original text.
    */
    
    public  boolean newSentenceStart = false; // states that start of a new sentence is detected   
    public String tempSentence = "";
    public int textLength = text.length(); // gets character size of the text
    public void splitToSentence(){  
        text = text.replaceAll("\\-", " "); 
        for(int i=0; i<textLength; i++){
            char tempChar = text.charAt(i);
            
            if (i == (textLength-1)){ // textin sonuysa ve herhangi bir işaret konmamadan bitmişse   
                tempSentence += text.charAt(i);
                prepareForNewSentence();
            }
            else{ 
                checkSentenceStart(i, tempChar);
                if(newSentenceStart == true)
                    prepareForNewSentence(); 
                 else
                    tempSentence += tempChar;  
            }            
        }        
        recordOrjSentenceToDB(orjTextMap);
    }
    
    /*
        This is to initialize parameters used to record sentence detection.
    */
    
    public void prepareForNewSentence(){
        String spaceCheck = tempSentence;
        spaceCheck = spaceCheck.replaceAll("\\s+", " ");
        if( spaceCheck.length() > 1){
            orjTextMap.put(sentenceCount, tempSentence); // record new sentence to the sentence map.
            sentenceCount ++;
            tempSentence = "";         
            newSentenceStart = false; 
        }
    }
    
    /*
        This is to check whether a new sentence is starting according to the selected character.
    */    
    
    public void checkSentenceStart(int charPosition, char c){
        if(c == '?' || c == '!'){
            newSentenceStart = true;  
            tempSentence += c;
        }
        if (c == '.'){  
            if (charPosition != 0 && charPosition < (textLength-1)){ 
                checkBeforeCharacter(charPosition);
                checkAfterCharacter(charPosition);                 
            }
            else{ newSentenceStart = true; }                
        }            
    }
    
    /*
        This is to check when the selected character is '.', whether  combination it with prior character states end of sentence.
        For example; "0.4"
    */
    
    public void checkBeforeCharacter(int charPosition){
        if( Character.isDigit(text.charAt(charPosition-1)) == true && Character.isDigit(text.charAt(charPosition+1)) == true ) //checks whether there is a floating number used with dot.                  
            newSentenceStart = false; 
        else{
            newSentenceStart = true;   
            tempSentence += text.charAt(charPosition);
        }
    }
    
    /*
        This is to check when the selected character is '.', whether  combination it with later character states end of sentence.
        For example; "Miss. Kocyigit"
    */
    
    //if it is a ...
    public void checkAfterCharacter(int charPosition){        
        if( text.charAt(charPosition+1) == '.' && text.charAt(charPosition+2) == '.' ){
            newSentenceStart = true;            
        }
    }
    
    /*
        This is to record sentences to the database.
    */

    public void recordOrjSentenceToDB(HashMap map){
        int size = map.size();
        boolean paragraphStart = false;
        findParagraphStarts();
        
        for(int i=0; i<size; i++){
            if(wordsAtParagraphStarts.size() > 0 && i<(size-1)){ 
                paragraphStart = checkParagraphStarts(map.get(i).toString());
                try {
                    if (paragraphStart == true || i == 0){ // ilk cümleyi paragraf başı olarak kaydet
                        PreparedStatement ps = conn.prepareStatement("insert into sentences (sentenceId, orjSentence, paragraphStart) VALUES (?, ?, ?)");
                        ps.setInt(1, i);
                        ps.setString(2, map.get(i).toString());
                        ps.setInt(3, 1);
                        ps.executeUpdate();
                        ps.close();
                    }
                    else{
                        PreparedStatement ps = conn.prepareStatement("insert into sentences (sentenceId, orjSentence, paragraphStart) VALUES (?, ?, ?)");
                        ps.setInt(1, i);
                        ps.setString(2, map.get(i).toString());
                        ps.setInt(3, 0);
                        ps.executeUpdate();
                        ps.close();  
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(Preprocessing.class.getName()).log(Level.SEVERE, null, ex);
                    ex.printStackTrace();
                }
            }
            else{
                try {                    
                    PreparedStatement ps = conn.prepareStatement("insert into sentences (sentenceId, orjSentence, paragraphStart) VALUES (?, ?, ?)");
                    ps.setInt(1, i);
                    ps.setString(2, map.get(i).toString());
                    ps.setInt(3, 0);
                    ps.executeUpdate();
                    ps.close();                   

                } catch (SQLException ex) {
                    Logger.getLogger(Preprocessing.class.getName()).log(Level.SEVERE, null, ex);
                    ex.printStackTrace();
                }                
            }                
        }
    }
    
    /*
        This is to detect starts of paragraphs if the text has any.
    */
    
    public static int totalParagraph;
    public  List<String> wordsAtParagraphStarts = new ArrayList<>();
    public void findParagraphStarts(){
        String ParagraphStart = "";
        totalParagraph = 0;

        for(int i=0; i<text.length(); i++){
            if ( (text.charAt(i) == '\n' ) && i != (text.length() - 1) )
            {
                for(int j=0; j<20; j++)
                    ParagraphStart += text.charAt(i+j);
                    ParagraphStart = ParagraphStart.replaceAll("\\s+", " ");
                    wordsAtParagraphStarts.add(ParagraphStart);     
                    totalParagraph ++;
                    ParagraphStart = "";
                    i = (i + 20);
            }
        }       
    }
    
    /*
        This is to check whether a sentence is a paragraph start.
    */
    
    public boolean checkParagraphStarts(String sentence){
        boolean found = false; 
        int i = 0;
        sentence = sentence.substring(0,20).replaceAll("\\s+", " ");
        
        while ( i<wordsAtParagraphStarts.size() && found == false){
            if(sentence.equals(wordsAtParagraphStarts.get(i)) ){
                wordsAtParagraphStarts.remove(i);  
                found = true;
            }
            i ++;
        }               
        return found;
    }
    
    
    /****************************************************************************************************************************************************************/
    /*********************************************************** METHODS TO EXTRACT FEATURES OF SENTENCES **********************************************************/
    /**************************************************************************************************************************************************************/
      
    public void getSentenceFeatures(){
        for(int i=0; i<sentenceCount; i++)
            processedTextMap.put(i, orjTextMap.get(i)); // copy of the orjinal sentence map to process for further steps.
        
        checkPuntuations();
        checkCapitalLetters();
        checkLinkingWords();
        checkBonusWords();
        checkNumericValues();
                
        countWordsInSentence();
        
        
    }
    
    /*
        This is to check whether sentences contain required punctuations and record this information to the database.
    */    
    
    public void checkPuntuations(){    
        for(int i=0; i<sentenceCount; i++){ 
            for(int j=0; j<punctuations.size();j++){ 
                if (processedTextMap.get(i).contains(punctuations.get(j))){
                    String column = "punctuation";
                    recordSentenceInformationToDB(column, 1, i);
                    j = punctuations.size();                
                }               
            }
            removePunctuations(i);
        }
    }
    
    /*
        This is to remove all punctuations from the selected sentence except quotations ", colons : and parantheses ().
    */
    
    public void removePunctuations(int key){
        String sentence = processedTextMap.get(key);    
        sentence = sentence.replaceAll("\\-", " "); // to slipt words such as "content-based"
        sentence = sentence.replaceAll("/", " "); // to slipt words such as "his/her"
        sentence = sentence.replaceAll("\\p{Punct}", ""); 
        sentence = sentence.replaceAll("£","");
        processedTextMap.put(key, sentence);
    }
    
    /*
        This is to check whether sentences contain any capitale letter except the first character of them.
    */
    
    public void checkCapitalLetters(){
        for(int i=0; i<sentenceCount; i++){ 
           for(int j=1; j<processedTextMap.get(i).length();j++){
               char c = processedTextMap.get(i).charAt(j);
               if ( Character.isUpperCase(c) ){
                   String column = "capitalLetter";
                   recordSentenceInformationToDB(column, 1, i);
                   j = processedTextMap.get(i).length(); 
               }
           }
           changeCapitalLetters(i);
        }
    }
    
    public void changeCapitalLetters(int key){         
        processedTextMap.put(key, processedTextMap.get(key).toLowerCase());
    }
    
    /*
        This is to check whether sentences contain required linking words.
    */
    
    public void checkLinkingWords(){
        for(int i=0; i<sentenceCount; i++){ 
           for(int j=0; j<linkingWords.size();j++){ 
               if (processedTextMap.get(i).toLowerCase().contains(linkingWords.get(j))){
                   String column = "linkingWord";
                   recordSentenceInformationToDB(column, 1, i);
                   j = linkingWords.size(); 
               }
           }
        }
    }
    
    /*
        This is to check whether sentences contain required bonus words.
    */
    
    public void checkBonusWords(){
        for(int i=0; i<sentenceCount; i++){ 
           for(int j=0; j<bonusWords.size();j++){ 
               if (processedTextMap.get(i).toLowerCase().contains(bonusWords.get(j))){
                   String column = "bonusWord";
                   recordSentenceInformationToDB(column, 1, i);
                   j = bonusWords.size(); 
               }
           }
        }
    }
    
    public void checkNumericValues(){ 
        String temp;
        int size;
        for (int i=0; i<processedTextMap.size();i++){
            temp = processedTextMap.get(i);
            size = temp.length();
            for(int j=0; j<size; j++){ 
                if (Character.isDigit(temp.charAt(j)) == true){
                    if(temp.charAt(j-1) == ' '){
                       recordSentenceInformationToDB("numericValue", 1, i); // record to DB 
                       j = size; 
                    }
                }
            }
            removeNumbericValues(i);
        }        
    }
    
    /*
        This is to remove all numeric values from the text.
    */
    
    public void removeNumbericValues(int key){
        String temp;
        temp = processedTextMap.get(key);
        temp = temp.replaceAll("\\d","");
        processedTextMap.put(key, temp);
        
    }
      
    /*
      This is to get count of words for the selected sentence.
    */
    
    public void countWordsInSentence(){
        int wordCount;
        
        for(int i=0; i<orjTextMap.size(); i++){
            String sentence = orjTextMap.get(i);
            sentence = sentence.replace("\n", "").replace("\r", "");
            String[] tmp = sentence.split(" ");
            wordCount = 0;
            for(int j=0; j<tmp.length; j++){
                if(tmp[j].length() > 0){
                    wordCount ++;
                }
            }
            recordSentenceInformationToDB("wordCount", wordCount, i);
        }
    }
    
    /*
        This is to record sentence information (puctuation, linking word etc.)to the database. 
    */
    
    public void recordSentenceInformationToDB(String column, int value, int id){
        try {
            statement = conn.createStatement(); 
            String query = "update sentences set "+column+" = '"+value+"'  where sentenceId = '"+id+"'";               
            statement.executeUpdate(query);            
        } catch (SQLException ex) {
            Logger.getLogger(Preprocessing.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    } 
    
    
    /****************************************************************************************************************************************************************/
    /***************************************** METHODS TO REMOVE IRRELATED CHARACTERS/WORDS FROM SENTENCES *********************************************************/
    /**************************************************************************************************************************************************************/
    
    /*
        This is to eliminate stop-words from sentences.
    */
    
    public void cleanSentences(){
        
        removeMultipleSpaces();  
        stopWordElimination();  
        removeMultipleSpaces();     
    }
    
    /*
        This is to remove stop-words from sentences.
    */
    
    public void stopWordElimination(){
        int size = processedTextMap.size(); 
        String sentence = "", stopWord;
        
        for(int i=0; i<size; i++)
        {        
            for(int j=0; j<stopWords.size(); j++)
            { 
                sentence = (" " + processedTextMap.get(i));
                stopWord = stopWords.get(j);
                if(sentence.contains(stopWord)) 
                    findStopWord(sentence, stopWord, i);
            }                     
        } 
    }
    
    /*
        This is to detect and delete stop-words from sentences.
    */
    
    public void findStopWord(String sentence, String stopWord, int i){
       int wordLength = stopWord.length();  
       int size = sentence.length();
       int k = 0;
       
       while (k<sentence.length() && sentence.contains(stopWord)== true){
           if( ( k + wordLength ) < sentence.length() ){  
                String  wordToCompare = sentence.substring(k, (k+wordLength));
                if(wordToCompare.equals(stopWord)){    
                    sentence = sentence.substring(0, k) + " " + sentence.substring((k + wordLength), sentence.length());
                    processedTextMap.put(i, sentence); // put updated sentence for next stop words for same sentence  
                    size = sentence.length();
               }
           }
           k ++;
       }
    } 
    
    /*
        This is to remove multiple spaces from sentences.
    */
    
    public void removeMultipleSpaces(){
        String temp;
        for (int i=0; i<processedTextMap.size();i++){
            temp = processedTextMap.get(i);
            temp = temp.replaceAll("\\s+", " ");
            processedTextMap.put(i, temp);
        }
    }   
    
    /* 
        This is to show results of preprocessing stepe in the text field of the interface.
    */
    
    public HashMap<Integer, String> wordsToHighlight = new HashMap<>();    
    public  void showDetectedFeatures(){        
        createResultMap();
        recordProcessedSentenceToDB(resultTextMap);         
        Interface.modifiedTextBox.setText(resultTextMap.toString());
    }
    
    /*
        This is to highlight selected characters/words in the text field of the interface.
    */
       
    public void highlightWord(String word, int start, Color color){
        Highlighter highlighter = Interface.modifiedTextBox.getHighlighter();
        HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(color);
        int wordSize = word.length();
         
        try {               
            int end = start + wordSize; 
            highlighter.addHighlight(start, (end), painter);
        } catch (BadLocationException ex) {
            Logger.getLogger(Preprocessing.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }        
  
    /*
        This is to stem words of processes sentences and to record a map.
    */
    
    public static HashMap<Integer, String> resultTextMap = new HashMap<>();
    public void createResultMap(){
        PorterStemmer stemmer = new PorterStemmer();
        String sentence, stemmedSentence = "";
        String[] tmp;
        boolean omittedWord;
        
        for(int i=0; i<processedTextMap.size(); i++){ 
            sentence = processedTextMap.get(i);
            stemmedSentence = "";
            tmp = sentence.split(" ");
            
            for(int j=0; j<tmp.length; j++){   
                stemmer.setCurrent(tmp[j]); //set string you need to stem
                stemmer.stem();  //stem the word
                String stemmed = stemmer.getCurrent();//get the stemmed word
                tmp[j] = stemmed;
                if(tmp[j].length() > 5){
                    tmp[j] = tmp[j].substring(0, 5);
                }
                stemmedSentence += " " + tmp[j];
            }
            for(int k=0; k<tmp.length; k++){
                resultTextMap.put(i, stemmedSentence);
            }
        }
    }
    
    /*
        This is to record processes sentences into the database.
    */
    
    public void recordProcessedSentenceToDB(HashMap map){
        int size = map.size();
        
        for(int i=0; i<size; i++){
            try {                    
                statement = conn.createStatement(); 
                String query = "update sentences set processedSentence = '"+map.get(i)+"' where sentenceId = '"+i+"'";               
                statement.executeUpdate(query);                  

            } catch (SQLException ex) {
                Logger.getLogger(Preprocessing.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }
        }
    }
    
    
    /*****************************************************************************************************************************************************************/
    /********************************************************************* METHODS TO PROCESS WORDS *****************************************************************/
    /***************************************************************************************************************************************************************/
        
    /*
        This is to divide sentences into words .
    */    
    
    public void splitToWords(){
        String sentence;
        for(int i=0; i<resultTextMap.size(); i++)
        {
            sentence = resultTextMap.get(i);
            String[] temp2 = sentence.split(" "); 
            String[] temp = Arrays.stream(temp2).filter(value -> value != null && value.length() > 0).toArray(size -> new String[size]); // to remove spaces at first of the sentence

            createWordMap(temp);
        }
        recordWordsToDB();
    }
    
    /*
        This is to create a map which holds splitted information (words) of the text without appliying any operations.
    */
    
    public List<int[]> wordNumerics;
    public void createWordMap(String[] wordListOfSentence){
        int listSize = wordListOfSentence.length; 
        for(int i=0; i<listSize; i++){
            String word = wordListOfSentence[i];
            word = word.replaceAll("\\s","");
            int id = orjWordMap.size();
            if ( word.length() > 0 ) // to not record space at first position of the new sentence
                orjWordMap.put(id, word); 
        } 
        createUniqueWordMap();
        wordCount = uniqueWordMap.size();
    }
      
    /*
        This is to remove repeated words from list
    */        
    
    public void createUniqueWordMap(){
        int size = orjWordMap.size();
        String word;
        
        for(int i=0; i<size; i++){
            word = orjWordMap.get(i);            
            if (uniqueWordMap.containsValue(word) == false ){ 
                int id = uniqueWordMap.size();
                uniqueWordMap.put(id, word);
            }
        }      
    }    

    /*
        This is to record private information of the words into DB.
    */
  
    public void recordWordsToDB (){
       for(int i=0; i<uniqueWordMap.size(); i++){
           String word = uniqueWordMap.get(i);
           getWordFeatures(i);
           //DB kaydet
            try {
                statement = conn.createStatement(); 
                String query = "insert into words(wordId, word, firstSentenceId, totalCount, sentenceCount) values('"+i+"', '"+word+"', '"+firstSentenceId+"', '"+totalCount+"', '"+wordSentenceCount+"')";
                statement.executeUpdate(query);            
            } catch (SQLException ex) {
                Logger.getLogger(Preprocessing.class.getName()).log(Level.SEVERE, null, ex);
            }           
       }
    }
    
    /*
        This to search words' features.
    */
    
    public int totalCount, firstSentenceId, wordSentenceCount;
    public void getWordFeatures(int key){
        int size = resultTextMap.size();
        String word = uniqueWordMap.get(key);
        String sentence;        
        totalCount = 0; 
        firstSentenceId = -1;
        wordSentenceCount = 0;     
       
        for(int j=0; j<size; j++){
            if(resultTextMap.get(j).contains(word)){
                firstSentenceId = j;
                j = size;
            }
        }        
        // word sentence count için
        int visitedSentence = -1;
        for(int j=0; j<size; j++){ // sentence id
           sentence = resultTextMap.get(j);
           String[] tmp = sentence.split(" ");
           for(int k=0; k<tmp.length; k++){ // sentence words
               if(tmp[k].equals(word)){                   
                   if( j != visitedSentence){ 
                       visitedSentence = j;
                       wordSentenceCount ++;
                   }
                   totalCount ++;
               }
           }
        }
    } 
}
