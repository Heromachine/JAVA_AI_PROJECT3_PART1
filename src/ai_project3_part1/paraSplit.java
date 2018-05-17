
package ai_project3_part1;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class paraSplit 
{
    public static void main(String[] args) throws FileNotFoundException
    {
    //place the entire story into input.txt

    //Splits story into paragraphs
        String paragraphs[]=parser("tomSawyer.txt");

    //creates ID for each paragraph
        String[] ID=new String[paragraphs.length];
        for(int i=0;i<paragraphs.length;i++)
        {
            ID[i]=""+(i);
            paragraphs[i]=paragraphs[i].trim();
        }

    //read ID from user
        System.out.println("Enter the ID of the paragraph you want <eg. 3> :");
        Scanner scan=new Scanner(System.in);
        String key=scan.next();
            int flag=0;
            int pos=-1;

    //search ID
        for(int i=1;i<ID.length;i++)
        {
            if(ID[i].equalsIgnoreCase(key))
            {
                flag=1;
                pos=i;
                break;
            }
        }

    //if match, display corresponding para
        if(flag==1)
        {
            System.out.println(paragraphs[pos]);
        }

    //else error
        else
        {
            System.out.println("No such ID exists");
        }
}
    
    public static String[] parser(String file) throws FileNotFoundException 
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
        String[] paragraphs = text.split("\n\n");
        return paragraphs;
}
}