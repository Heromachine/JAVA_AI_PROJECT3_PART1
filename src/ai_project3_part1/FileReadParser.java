/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ai_project3_part1;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
 import java.nio.file.*;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.File;
/**
 *
 * @author Heromachine
 */
public class FileReadParser {   
    
    FileReadParser()       
    {
    }
    
    FileReadParser(int x)throws FileNotFoundException            
    {
    } 
    
    public Boolean isExisting(Path path) 
    {
        if (path == null || !Files.exists(path))
        {
            return false;
        }
        else 
        {
            return true;
        }
    }
    
        
    public Boolean isExistingDirectory(String path) 
    {
        File file = new File(path);
        if (file.isDirectory())
        {
            return true;
        }
        else 
        {
            return false;
        }
    }
    
    public Boolean isExistingFile(String path) 
    {
        File file = new File(path);
        if (file.isFile())
        {
            return true;
        }
        else 
        {
            return false;
        }
    }
    
    public  String parserDoc02(String file) throws FileNotFoundException 
    {
        String text="";
        Scanner scan=new Scanner(new FileReader(file));
        while(scan.hasNextLine())
        {
            text+="\n"+scan.nextLine();
        }
        return text;
    }
   
    public  String[] parserDoc(String file) throws FileNotFoundException 
    {
        String text="";

        //read the input file
            Scanner scan=new Scanner(new FileReader(file));
                while(scan.hasNextLine())
                {
                    text+="\n"+scan.nextLine();
                }

        //make paragraphs
        //Solution to split paragraphs http://stackoverflow.com/a/9327740/966044
            String[] paragraphs = text.split("\n\n+");
            return paragraphs;
    }
    


}
