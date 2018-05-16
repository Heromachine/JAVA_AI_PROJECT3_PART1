package ai_project3_part1;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author Jessie Reyna
 */
public class AI_Project3_Part1 
{
    static int pass = 12;
    public static void main(String[] args) 
    {        

        System.out.println("\nTABLE 1================================================================================");
        FileReadParser FRP = new FileReadParser();        
        ListWords Table1 = new ListWords("Train1", 5, 10, 25, false, false, false, false);
        System.out.println("\t\t 20 PERCENT");
        Table1.TestWordsFind("TestDocs", 20, 0.20f, FRP);
        
        Table1 = new ListWords("Train1", 5, 10, 25, false, false, false, false);
        System.out.println("\t\t 50 PERCENT");
        Table1.TestWordsFind("TestDocs", 20, 0.50f, FRP);        
        System.out.println("\n====================================================================================\n");
        
        
        
        System.out.println("\n\n\nTABLE 2============================================================================");
        ListWords Table2 = new ListWords("Train2", 5, 20, 25, false, false, false, false);
         System.out.println("\t\t 20 PERCENT");
        Table2.TestWordsFind("TestDocs", 20, .2f, FRP);
        
        Table2 = new ListWords("Train2", 5, 20, 25, false, false, false, false);
        System.out.println("\t\t 50 PERCENT");
        Table2.TestWordsFind("TestDocs", 20, .50f, FRP);
        System.out.println("\n====================================================================================\n");
        
        
        
        System.out.println("\n\n\nTABLE 3============================================================================");
        ListWords Table3 = new ListWords("Train3", 5, 10, 50, false, false, false, false);
        System.out.println("\t\t 25 PERCENT");
        Table3.TestWordsFind("TestDocs", 20, .25f, FRP);
        
        Table3 = new ListWords("Train3", 5, 10, 50, false, false, false, false);
        System.out.println("\t\t 10 PERCENT");
        Table3.TestWordsFind("TestDocs", 20, .10f, FRP);
        System.out.println("\n====================================================================================\n");
        
        
        
        
//        MyList.populateStopWords();         
//        MyList.recrusiveFileFind(FRP, "Train1", "Train1", 5, 10);
     
        
       
            
        
       
        
        //MyList.printWordTotalCount();
        
        //MyList.printListVec();
         
         
//        for (String S: MyList.getWords())            
//        {
//             System.out.println("WORD: " + S);
//        }
//        for (String D: MyList.getDocs())
//        {    
//            System.out.println("DOC: " + D);
//        }
//        for (String S: MyList.getWords())            
//        {
//            
//            if (MyList.WORDZ.containsKey(S))
//            {  
//                System.out.println("\n[WORD]=================================================" + S);                
//                
//                for (String D: MyList.getDocs())
//                {                        
//                    if (MyList.WORDZ.get(S).containsKey(D))
//                    {
//                        System.out.println("\t[DOC]: " + D); 
//                        System.out.format("\t\t[FRQUENCY]: "+MyList.WORDZ.get(S).get(D).intValue()+ "\n\n");
//                        
//                    }
//                 
//                }                
//            }
//            System.out.println("=================================================\n");
//        }

    }
    

}
