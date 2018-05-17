package ai_project3_part1;

import static ai_project3_part1.AI_Project3_Part1.pass;
import java.io.File;
import java.util.*;
import java.util.Map;
import java.io.Reader;
import java.util.Stack;
import java.lang.Object;
import java.util.Vector;
import java.util.HashMap;
import org.apache.lucene.*;
import java.io.IOException;
import java.util.ArrayList;
import java.io.StringReader;
import org.apache.lucene.analysis.*;
import org.apache.lucene.util.Version;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.util.AttributeSource;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;

public class ListWords {

    //CLASS INTERNAL TYPES
    Set<String> stopWords = new HashSet<String>();
    
    Map<String, HashMap<String, Integer>> WORDZ = new HashMap();
    Set<String> docs = new HashSet<String>();

    HashMap<String, Integer> wordsNcount = new HashMap();      
    Vector<String>  words = new Vector<String>();
    Vector<Integer> wordsCount = new Vector<Integer>();
    Vector<String>  docFiles = new Vector<String>();  
    Vector<String> Catagories = new Vector<String>();
    
    Set<String> sTempWords = new HashSet<String>();
    Vector<String> vTempwords = new Vector<String>();
    Vector<Integer> vTempwordsCount = new Vector<Integer>();
    Vector<Integer> vTempwordsSUMS = new Vector<Integer>();
    HashMap<String, Integer> vTempwordsNcount = new HashMap();
    Map<String, HashMap<String, Integer>> CATZ = new HashMap();
    HashMap<String, Integer> tempHM = new  HashMap<String, Integer>();
    HashMap<String, Float> CATZwordcount = new HashMap(); 
    
    
    HashMap<String, Vector<String>> CatNSortVecWORDS = new HashMap(); 
    HashMap<String, Vector<Integer>> CatNSortVecCOUNTS = new HashMap(); 
     
    Vector <Map<String, HashMap<String, Integer>> > ALLCATZ = new Vector();    
    Map<String, HashMap<String, Float>> CATZ_WORD_PERCENTAGE = new HashMap();
    
    HashMap<String, Integer> TestwordsNcount = new HashMap();  
    Vector<String> TestWords = new Vector<String>();
    
    
    int number; 
    int MaxFiles = 0;
    int MaxTestFiles =  0;
    int MaxDirectories = 0; 
    
    boolean showWordsPerCatagory = true;
    boolean showFilesPerCatagory = true;
    boolean showSortedWordsNCountPerCatagory = true;    
    boolean showNaiveBayesStats = true;

    //PUBLIC FUNCTIONS ////////////////////////////////////////////////
    ListWords() {
        populateStopWords();
    }
    ListWords 
    (
        String filename, 
        int catagoriesTotal, 
        int filesTotal, 
        int TopWordsTotal, 
        boolean showWordsPerCatagory, 
        boolean showFilesPerCatagory,
        boolean showSortedWordsNCountPerCatagory, 
        boolean showNaiveBayes
    )
    {
        this.showWordsPerCatagory = showWordsPerCatagory;
        this.showFilesPerCatagory = showFilesPerCatagory;
        this.showSortedWordsNCountPerCatagory = showSortedWordsNCountPerCatagory; 
        this.showNaiveBayesStats = showNaiveBayes;
        init(filename, catagoriesTotal, (filesTotal + 1));        
        CalNaiveBayes(TopWordsTotal);   
        

        
    }
    public void CalNaiveBayes(int top){
        
        Vector<String> TC = new Vector<String> ();
        Vector<Float>TP = new Vector<Float>(); 
        for ( int AC = 0; AC <  ALLCATZ.size(); AC++)
            
        {            
            for (String C : this.Catagories) 
            { 
                float Percentage = 0;
                float allWordsCount = CATZwordcount.get(C);
                HashMap<String, Float> TempHash = new HashMap<String, Float>();  
                //System.out.println("\n====================================================================================\n");
                //System.out.print("CATAGORY: "+C+" [TOTAL WORDS:"+CATZwordcount.get(C)+"]");                
               // System.out.println(ALLCATZ.get(AC).get(C).size());    
               
               //GET TOP N WORDS WHICH ARE THE BOTTOM OF THE VECTOR IN THE HASHTABLE OF EACH CATAGORY
                for (int W = CatNSortVecWORDS.get(C).size()-1;  W > (CatNSortVecWORDS.get(C).size() - top); W--)
                {                      
                        
                    String Word = CatNSortVecWORDS.get(C).get(W);
                                   //System.out.println("=============================\nWORD!!!!: " +Word);
                    int wordcount = CatNSortVecCOUNTS.get(C).get(W);
                    Percentage = wordcount /allWordsCount;
                            
                    Float F = new Float(Percentage);
                    
                    //Is the word in this catagory in 
                    if ( ALLCATZ.get(AC).get(C).containsKey(CatNSortVecWORDS.get(C).get(W)))
                    {                            
                        TempHash.put(Word, F);
                    }
                }
                CATZ_WORD_PERCENTAGE.put(C, TempHash);
            }
        } 
        
        
    }
    public void CalCatagory(String filename, int top, float DocAmount){       
        //CATZ_WORD_PERCENTAGE.get(C).put(Word, Percentage);
        //HashMap<String, Integer> TestwordsNcount = new HashMap(); 
        //Vector<String> TestWords = new Vector<String>();
        Vector<String>Cat = new Vector<String>();
        Vector<Float>Score = new Vector<Float>();
        HashMap<String, Float> CATnSCORE = new HashMap();
        
        
        float TestDocsPercent = DocAmount;
        //System.out.println("\t\t======================DOCT PERCENT: "+DocAmount);
        
        for (String C : this.Catagories) 
        {           
            CATnSCORE.put(C, 1.0f);
            float CurrentCatPercent = CATnSCORE.get(C); 
            if (CATZ_WORD_PERCENTAGE.containsKey(C))                
            {                    
                for (int TW =  0; TW <  TestWords.size()-1;  TW++)
                {                
                    if (CATZ_WORD_PERCENTAGE.get(C).containsKey(TestWords.get(TW)))
                    {               
                        //System.out.println("\n\t=============================\n\tCATAGORY: " +C);
                        
                        float TWordPercent = CATZ_WORD_PERCENTAGE.get(C).get(TestWords.get(TW));
                        int TWCount = TestwordsNcount.get(TestWords.get(TW));
                        
                        
                        
                        Float Answer = new Float(Math.pow(TWordPercent, TWCount) *  TestDocsPercent * CurrentCatPercent);   
                        
                        //System.out.format("\nAnswer: %f | Wp: %f | Wc: %d | Dp: %f | Cp: %f", Answer, TWordPercent, TWCount, TestDocsPercent, CurrentCatPercent);
                        CATnSCORE.replace(C, Answer);
                        Cat.add(C);
                        Score.add(Answer);
                        //System.out.println("\t\tFILE : "+filename);
                    }   
                } 
            }
            Sort2(Cat, Score); 
        }
        
         

        if (Cat.size() > 0)
        {
            System.out.println("\t\t=====================================");
            System.out.println("\n\t\tFILE : "+ filename);
            System.out.println("\t\tSOLUTION CATAGORY: "+Cat.get(Cat.size() -1));
            System.out.println("\t\tSOLUTION SCORE: "+Score.get(Score.size()-1) + "\n");
            System.out.println("\t\t=====================================");
        }      
    }  
    
    public void TestWordsFind(String FolderName, int maxD, float Dpercent, FileReadParser FRP){   
        

        int MinD =  0;        
        File file = new File(FolderName);
        String[] files = file.list();
        
        System.out.println("=====================================");
        System.out.println("FOLDER TESTED: "    + FolderName);
        System.out.println("TOTAL DOCS TESTED: "+ maxD);
        System.out.println("TOTAL FILES: "      + files.length);
        System.out.println("=====================================");
        
        for (String filename : files) 
        {
            
            String Path = FolderName + ("/") +filename;
            if (FRP.isExistingFile(Path)) 
            {           
                
                if (this.MaxTestFiles < maxD)                     
                {                         
                                                                   
                    try {
                        //populateStopWords();
                        String ps = FRP.parserDoc02(Path);
                        parseDocTest(ps, Path);  
                        
                        CalCatagory(Path, maxD, Dpercent);

                    } catch (IOException ex) { }
                    
                    ++this.MaxTestFiles;
                }
            } 
        }
        
        
    }
    public void parseDocTest(String content, String Filename) throws IOException {

        String AppendCont = content + " Appended";
        AppendCont = AppendCont.toLowerCase();
        StringBuilder buildcont = new StringBuilder();
        buildcont.append(AppendCont);

        TokenStream tokenStream = new StandardTokenizer(Version.LUCENE_30, new StringReader(buildcont.toString()));
        tokenStream = new StopFilter(true, tokenStream, stopWords);
        tokenStream = new PorterStemFilter(tokenStream);

        StringBuilder sb = new StringBuilder();
        TermAttribute termAttr = tokenStream.getAttribute(TermAttribute.class);
        try {
            while (tokenStream.incrementToken()) {
                if (sb.length() > 0) {
                    sb.append(" ");
                }
                sb.append(termAttr.term());
            }
        } catch (IOException e) {
            //nothing
        }

        //StringBuilder sb = new StringBuilder();
        //sb.append(buildcont);
        int preIn = 0;
        String tempWord = "";

        //remove puntuation
        for (int i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) == ' ' && sb.charAt(i + 1) == ' ') {
                sb.deleteCharAt(i);
                i--;
            } else if (sb.charAt(i) == '.' || sb.charAt(i) == ',' || sb.charAt(i) == ':'
                    || sb.charAt(i) == ';' || sb.charAt(i) == '?' || sb.charAt(i) == '\''
                    || sb.charAt(i) == '\n' || sb.charAt(i) == '&' || sb.charAt(i) == '!'
                    || sb.charAt(i) == '(' || sb.charAt(i) == ')' || sb.charAt(i) == '\"'
                    || sb.charAt(i) == '_' || sb.charAt(i) == '~' || sb.charAt(i) == '`'
                    || sb.charAt(i) == '<' || sb.charAt(i) == '>' || sb.charAt(i)== '-'
                    || sb.charAt(i) == '@' || sb.charAt(i) == '/' ) {
                sb.deleteCharAt(i);
                sb.insert(i, ' ');
                //i--;
            }
        }

        for (int i = preIn; i < sb.length(); i++) {
            if (sb.charAt(i) != ' ') {
                tempWord += sb.charAt(i);
            } else {
                addWordTest(Filename, tempWord);
                tempWord = "";
                preIn = i + 1;

            }

        }
    }
    public void addWordTest(String filename, String tempWord){
        
        if (!TestwordsNcount.containsKey(tempWord))
        {
            TestWords.add(tempWord);
            TestwordsNcount.put(tempWord, 1);     
        }
        else if (TestwordsNcount.containsKey(tempWord))
        {
            TestwordsNcount.replace(tempWord, TestwordsNcount.get(tempWord).intValue() + 1);     
        }
    }
    public void init(String filename, int catagories, int files){    
        FileReadParser FRP = new FileReadParser();
        populateStopWords();         
        recrusiveFileFind(FRP, filename, filename, catagories, files);
        preparelist();
        Sort(words, wordsCount);    
    }
    public void populateStopWords() {
        stopWords.add("a");
        stopWords.add("about");
        stopWords.add("above");
        stopWords.add("after");
        stopWords.add("again");
        stopWords.add("against");
        stopWords.add("all");
        stopWords.add("am");
        stopWords.add("an");
        stopWords.add("and");
        stopWords.add("any");
        stopWords.add("are");
        stopWords.add("aren't");
        stopWords.add("as");
        stopWords.add("at");
        stopWords.add("be");
        stopWords.add("because");
        stopWords.add("been");
        stopWords.add("before");
        stopWords.add("being");
        stopWords.add("below");
        stopWords.add("between");
        stopWords.add("both");
        stopWords.add("but");
        stopWords.add("by");
        stopWords.add("can't");
        stopWords.add("cannot");
        stopWords.add("could");
        stopWords.add("couldn't");
        stopWords.add("did");
        stopWords.add("didn't");
        stopWords.add("do");
        stopWords.add("does");
        stopWords.add("doesn't");
        stopWords.add("doing");
        stopWords.add("don't");
        stopWords.add("down");
        stopWords.add("during");
        stopWords.add("each");
        stopWords.add("few");
        stopWords.add("for");
        stopWords.add("from");
        stopWords.add("further");
        stopWords.add("had");
        stopWords.add("hadn't");
        stopWords.add("has");
        stopWords.add("hasn't");
        stopWords.add("have");
        stopWords.add("haven't");
        stopWords.add("having");
        stopWords.add("he");
        stopWords.add("he'd");
        stopWords.add("he'll");
        stopWords.add("he's");
        stopWords.add("her");
        stopWords.add("here");
        stopWords.add("here's");
        stopWords.add("her's");
        stopWords.add("herself");
        stopWords.add("him");
        stopWords.add("himself");
        stopWords.add("his");
        stopWords.add("how");
        stopWords.add("how's");
        stopWords.add("i");
        stopWords.add("i'd");
        stopWords.add("i'll");
        stopWords.add("i'm");
        stopWords.add("i've");
        stopWords.add("if");
        stopWords.add("in");
        stopWords.add("into");
        stopWords.add("is");
        stopWords.add("isn't");
        stopWords.add("it");
        stopWords.add("it's");
        stopWords.add("its");
        stopWords.add("itself");
        stopWords.add("let's");
        stopWords.add("me");
        stopWords.add("more");
        stopWords.add("most");
        stopWords.add("mustn't");
        stopWords.add("my");
        stopWords.add("myself");
        stopWords.add("no");
        stopWords.add("nor");
        stopWords.add("not");
        stopWords.add("of");
        stopWords.add("off");
        stopWords.add("on");
        stopWords.add("once");
        stopWords.add("only");
        stopWords.add("or");
        stopWords.add("other");
        stopWords.add("ought");
        stopWords.add("our");
        stopWords.add("ours");
        stopWords.add("ourselves");
        stopWords.add("out");
        stopWords.add("over");
        stopWords.add("own");
        stopWords.add("same");
        stopWords.add("shan't");
        stopWords.add("she");
        stopWords.add("she'd");
        stopWords.add("she'll");
        stopWords.add("she's");
        stopWords.add("should");
        stopWords.add("shouldn't");
        stopWords.add("so");
        stopWords.add("some");
        stopWords.add("such");
        stopWords.add("than");
        stopWords.add("that");
        stopWords.add("that's");
        stopWords.add("the");
        stopWords.add("their");
        stopWords.add("their's");
        stopWords.add("them");
        stopWords.add("themselves");
        stopWords.add("then");
        stopWords.add("there");
        stopWords.add("there's");
        stopWords.add("these");
        stopWords.add("they");
        stopWords.add("they'd");
        stopWords.add("they're");
        stopWords.add("they'll");
        stopWords.add("they've");
        stopWords.add("this");
        stopWords.add("those");
        stopWords.add("though");
        stopWords.add("to");
        stopWords.add("too");
        stopWords.add("under");
        stopWords.add("until");
        stopWords.add("up");
        stopWords.add("very");
        stopWords.add("was");
        stopWords.add("wasn't");
        stopWords.add("we");
        stopWords.add("we'd");
        stopWords.add("we'll");
        stopWords.add("we're");
        stopWords.add("we've");
        stopWords.add("were");
        stopWords.add("weren't");
        stopWords.add("what");
        stopWords.add("what's");
        stopWords.add("when");
        stopWords.add("when's");
        stopWords.add("where");
        stopWords.add("where's");
        stopWords.add("which");
        stopWords.add("while");
        stopWords.add("who");
        stopWords.add("who's");
        stopWords.add("whom");
        stopWords.add("why");
        stopWords.add("why's");
        stopWords.add("with");
        stopWords.add("won't");
        stopWords.add("would");
        stopWords.add("wouldn't");
        stopWords.add("you");
        stopWords.add("you'd");
        stopWords.add("you'll");
        stopWords.add("you're");
        stopWords.add("you've");
        stopWords.add("your");
        stopWords.add("yours");
        stopWords.add("yourself");
        stopWords.add("yourselves");
    }
    public void recrusiveFileFind(FileReadParser FRP, String directory, String parentPath, int maxF, int maxD){  
        if (directory != parentPath)
        {            
            if (FRP.isExistingDirectory(directory)) 
            { 
                ++this.MaxDirectories;
                //System.out.println("\n\n["+this.MaxDirectories+"][CATAGORY]: "+ directory+ "=========================================================================");
                if (this.MaxDirectories < maxD) 
                {      
                    File file = new File(directory);
                    String[] files = file.list();

                    for (String string : files) 
                    {
                        String FuturePath = directory + "/" + string;
                        recrusiveFileFind(FRP, FuturePath, directory, maxF, maxD);
                    }                        
                    if (directory != parentPath)
                    {                    
                        getWordSumForAllDocs(directory);
                    }
                    Catagories.add(directory);
                }
                
                //prepareTempWordsCount();
                preparelist02();
                Sort(vTempwords, vTempwordsCount);                

                if (showSortedWordsNCountPerCatagory){ printTempListVec(false); }
                                
                float toatalWordsCount = 0;
                for (String W: vTempwords){toatalWordsCount += vTempwordsNcount.get(W);}
                String D = new String(directory);
                
                Vector<String> Ws = new Vector<String>(vTempwords);
                Vector<Integer> Is = new Vector<Integer>(vTempwordsCount);
                CatNSortVecWORDS.put(D, Ws);
                CatNSortVecCOUNTS.put(D, Is);
               
                CATZwordcount.put(directory, toatalWordsCount);
                
                vTempwordsNcount.clear();
                vTempwordsCount.clear();
                vTempwords.clear();
                sTempWords.clear();
                this.MaxFiles = 0;

            } else if (FRP.isExistingFile(directory)) 
            {             
                if (this.MaxFiles < maxF)                     
                {                         
                    ++this.MaxFiles;                        
                    if (showFilesPerCatagory)
                    {
                        if (this.MaxFiles%4 == 0)
                        {
                            //System.out.println("\n");                   
                        }
                        //System.out.print("\t["+this.MaxFiles+"][FILE]: "+ directory+"  "); 
                    }                        
                    try {
                        //populateStopWords();
                        String ps = FRP.parserDoc02(directory);
                        parseDoc(ps, directory);

                    } catch (IOException ex) { }
                }
            } 
            else if (!FRP.isExistingFile(directory)&& FRP.isExistingDirectory(directory))
            {
                    System.out.println("DIRECTORY FIALED: "+ directory);
            }
        }
        else 
        {
            if (this.MaxDirectories < maxD) 
            {      
                File file = new File(directory);
                String[] files = file.list();

                for (String string : files) 
                {
                    String FuturePath = directory + "/" + string;
                    recrusiveFileFind(FRP, FuturePath, directory, maxF, maxD);
                }                        
                if (directory != parentPath)
                {                    
                    getWordSumForAllDocs(directory);
                }
            }  
        }      
    }
    public void preparelist(){   for (String W : this.words) 
        { 
            wordsCount.add(wordsNcount.get(W).intValue());           
        }      
    }
    public void preparelist02(){   
        for (String W : this.sTempWords) 
        { 
            vTempwordsCount.add(vTempwordsNcount.get(W).intValue());           
        }      
    }
    public void getWordSumForAllDocs(String Catagory){    
        int sum = 0;
        int x = 0;
        
        for(String W: words)
        {   
            
            if (sTempWords.contains(W))
            {   
                for(String D: docs)
                {            
                    if (WORDZ.get(W).containsKey(D))
                    {  
                        if (WORDZ.get(W).get(D) != null)
                        {   
                            sum +=  WORDZ.get(W).get(D);
                        }
                    }
                }           
                if (showWordsPerCatagory)
                {
                    if (x%5 == 0)
                    System.out.println("\t");
                    System.out.format("[%-30s = %3d] \t",W, sum );
                }  
                
                
                
                tempHM.put(W, sum);
                
                sum = 0;
                ++x;
            }
        }        
        CATZ.put(Catagory, tempHM);
        
         Map<String, HashMap<String, Integer>> TempCATZ = new HashMap();
        
         TempCATZ = CATZ; 
        ALLCATZ.add(TempCATZ);
    }
    public void parseDoc(String content, String DocID) throws IOException {

        String AppendCont = content + " Appended";
        AppendCont = AppendCont.toLowerCase();
        StringBuilder buildcont = new StringBuilder();
        buildcont.append(AppendCont);

        TokenStream tokenStream = new StandardTokenizer(Version.LUCENE_30, new StringReader(buildcont.toString()));
        tokenStream = new StopFilter(true, tokenStream, stopWords);
        tokenStream = new PorterStemFilter(tokenStream);

        StringBuilder sb = new StringBuilder();
        TermAttribute termAttr = tokenStream.getAttribute(TermAttribute.class);
        try {
            while (tokenStream.incrementToken()) {
                if (sb.length() > 0) {
                    sb.append(" ");
                }
                sb.append(termAttr.term());
            }
        } catch (IOException e) {
            //nothing
        }

        //StringBuilder sb = new StringBuilder();
        //sb.append(buildcont);
        int preIn = 0;
        String tempWord = "";

        //remove puntuation
        for (int i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) == ' ' && sb.charAt(i + 1) == ' ') {
                sb.deleteCharAt(i);
                i--;
            } else if (sb.charAt(i) == '.' || sb.charAt(i) == ',' || sb.charAt(i) == ':'
                    || sb.charAt(i) == ';' || sb.charAt(i) == '?' || sb.charAt(i) == '\''
                    || sb.charAt(i) == '\n' || sb.charAt(i) == '&' || sb.charAt(i) == '!'
                    || sb.charAt(i) == '(' || sb.charAt(i) == ')' || sb.charAt(i) == '\"'
                    || sb.charAt(i) == '_' || sb.charAt(i) == '~' || sb.charAt(i) == '`'
                    || sb.charAt(i) == '<' || sb.charAt(i) == '>' || sb.charAt(i)== '-'
                    || sb.charAt(i) == '@' || sb.charAt(i) == '/' ) {
                sb.deleteCharAt(i);
                sb.insert(i, ' ');
                //i--;
            }
        }

        for (int i = preIn; i < sb.length(); i++) {
            if (sb.charAt(i) != ' ') {
                tempWord += sb.charAt(i);
            } else {
                addWord02(DocID, tempWord, preIn);
                tempWord = "";
                preIn = i + 1;

            }
        }
    }
        
    public void prepareTempWordsCount(){
        for (String W : this.words) 
        {    
             vTempwordsCount.add(wordsNcount.get(W).intValue());
        }    
    }
    public void addWord02(String DocID, String tempWord, int preIn){
        if (!sTempWords.contains(tempWord))
        {
            sTempWords.add(tempWord);
            vTempwords.add(tempWord);
            vTempwordsNcount.put(tempWord, 1);    
        }
        else 
        {
            if(!sTempWords.isEmpty())
            {
                vTempwordsNcount.replace(tempWord, vTempwordsNcount.get(tempWord).intValue() + 1);
            }        
        }
        
        if (!WORDZ.containsKey(tempWord)) 
        {       
            docs.add(DocID);            
            HashMap<String, Integer> temp = new HashMap();
            temp.put(DocID, 1);
            WORDZ.put(tempWord, temp);
            
            words.add(tempWord);
            wordsNcount.put(tempWord, 1);      

        } 
        else 
        {            
            wordsNcount.replace(tempWord, wordsNcount.get(tempWord).intValue() + 1);           
            
            if (WORDZ.get(tempWord).containsKey(DocID)) 
            {
                int x = WORDZ.get(tempWord).get(DocID).intValue() + 1;
                ((Map) WORDZ.get(tempWord)).replace(DocID, x);
                
            } else if (!WORDZ.get(tempWord).containsKey(DocID)) 
            {
                HashMap<String, Integer> temp = new HashMap();
                temp.put(DocID, 1);
                ((Map) WORDZ.get(tempWord)).put(DocID, 1);
            }
        }
    }
    public void printListVec(){
        for (String W : this.words) 
        {
            System.out.print("\n[WORD]:" + W +  "[");
            
            System.out.println(wordsNcount.get(W).intValue()+ "]");
        }      
    }
    public void printListVec02(Vector<String> vTempwordsCount, HashMap<String, Integer> vTempwordsNcount, boolean b){
        if (b)
        for (String W : vTempwordsCount) 
        {            
            System.out.format("[WORD]: %-20s = [%10d ]", W, vTempwordsNcount.get(W).intValue() );
        }      
    }
    public void printTempListVec(boolean b){    
        if (b)
        for (int i = 0; i < vTempwords.size(); i++)             
        {
            if (i% 5 == 0){
                System.out.println();           
            }                
            System.out.format("[WORD]: %-20s = [%3d ]", this.vTempwords.get(i), vTempwordsCount.get(i) );
        }        
    }
    public void printWordTotalCount(boolean b) {       
        
        if (b)
        if (!wordsNcount.isEmpty()) {
            for (String W : this.words) {
                System.out.print("\n[WORD]:" + W);
                System.out.println(wordsNcount.get(W).intValue());
            }
        } else {
            System.out.println("NO WORDS");
        }
    }
    public int getMaxFiles() {
        return MaxFiles;
    }
    public void setMaxFiles(int MaxFiles) {
        this.MaxFiles = MaxFiles;
    }
    public boolean isShowWordsPerCatagory() {
        return showWordsPerCatagory;
    }
    public void setShowWordsPerCatagory(boolean showWordsPerCatagory) {
        this.showWordsPerCatagory = showWordsPerCatagory;
    }
    public Set<String> getDocs() {
        return docs;
    }
    public void setDocs(Set<String> docs) {
        this.docs = docs;
    }
    public Vector<String> getWords() {
        return words;
    }
    public void setWords(Vector<String> words) {
        this.words = words;
    }
    
    
    //MODIFIED EXTERNAL CODE
    //CODE FROM: Lars Vogel (c) 2009, 2016 vogella GmbH
    //Version 0.7,04.10.2016
    //http://www.vogella.com/tutorials/JavaAlgorithmsQuicksort/article.html
    public void Sort(Vector<String>  Ws, Vector<Integer>  WNs) {        
        
        //System.out.println("\n[TOTAL WORDS]: " + WNs.size());
        //System.out.println("[TOTAL COUNT]: " + Ws.size());
        if (WNs == null || WNs.size() == 0)
        { return;  }        
        
        QuickSort(0, WNs.size() - 1, Ws, WNs);    
    
    }
    public void QuickSort(int low, int high, Vector<String>  Ws, Vector<Integer>  WNs){        
        int i = low, j = high; 
        int pivot = WNs.get(low + (high - low)/2);
        while (i <= j) 
        {
           while (WNs.get(i) < pivot) 
           {i++;}
           while (WNs.get(j) > pivot) 
           {j--;}
           
            if (i <= j) 
            {
                exchange(j, i, Ws, WNs);
                i++;
                j--;
            }
        }      
        if (low < j)
        {            
            QuickSort(low, j,   Ws,   WNs);
        }
        if (i < high)
        {
            QuickSort(i, high,  Ws,   WNs);
        }
    }
    private void exchange(int i, int j, Vector<String>  Ws, Vector<Integer>  WNs){
       Collections.swap(Ws,i,j);
       Collections.swap(WNs,i,j);   
    }  
    
      public void Sort2(Vector<String>  Ws, Vector<Float>  WNs) {        
        
        //System.out.println("\n[TOTAL WORDS]: " + WNs.size());
        //System.out.println("[TOTAL COUNT]: " + Ws.size());
        if (WNs == null || WNs.size() == 0)
        { return;  }        
        
        QuickSort2(0, WNs.size() - 1, Ws, WNs);    
    
    }
    public void QuickSort2(int low, int high, Vector<String>  Ws, Vector<Float>  WNs){        
        int i = low, j = high; 
        float pivot = WNs.get(low + (high - low)/2);
        while (i <= j) 
        {
           while (WNs.get(i) < pivot) 
           {i++;}
           while (WNs.get(j) > pivot) 
           {j--;}
           
            if (i <= j) 
            {
                exchange2(j, i, Ws, WNs);
                i++;
                j--;
            }
        }      
        if (low < j)
        {            
            QuickSort2(low, j,   Ws,   WNs);
        }
        if (i < high)
        {
            QuickSort2(i, high,  Ws,   WNs);
        }
    }
    private void exchange2(int i, int j, Vector<String>  Ws, Vector<Float>  WNs){
       Collections.swap(Ws,i,j);
       Collections.swap(WNs,i,j);   
    }    
    //END CODE FROM: Lars Vogel////////////////////////////////////////////
}


