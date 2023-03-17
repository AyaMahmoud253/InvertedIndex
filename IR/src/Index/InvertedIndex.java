package Index;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class InvertedIndex {
    public static void main(String args[]) throws IOException {
      try{ Index2 index = new Index2();
        String phrase = "";
        index.buildIndex(new String[]{
        		"C:\\Users\\htc\\Desktop\\Files\\100.txt",
                "C:\\Users\\htc\\Desktop\\Files\\101.txt",
                "C:\\\\Users\\\\htc\\\\Desktop\\\\Files\\\\102.txt",
                "C:\\\\Users\\\\htc\\\\Desktop\\\\Files\\\\103.txt",
                "C:\\\\Users\\\\htc\\\\Desktop\\\\Files\\\\104.txt",
                "C:\\\\Users\\\\htc\\\\Desktop\\\\Files\\\\105.txt",
                "C:\\\\Users\\\\htc\\\\Desktop\\\\Files\\\\106.txt",
                "C:\\\\Users\\\\htc\\\\Desktop\\\\Files\\\\107.txt",
                "C:\\\\Users\\\\htc\\\\Desktop\\\\Files\\\\108.txt",
                "C:\\\\Users\\\\htc\\\\Desktop\\\\Files\\\\109.txt"
        });     
        
        System.out.println("Enter Query");
int c=0;
        HashSet<Integer>res=new HashSet<>();
          @SuppressWarnings("resource")
          BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
int m=0;
    phrase = in.readLine();
    phrase=phrase.toLowerCase();
    String[] words = phrase.split("\\W+");
    for(int i=0;i<words.length;i++)
    {
if ((words[i].equals("and"))||(words[i].equals("or")))
{
c++;        
}
    }
    if(c==0)
    res=index.parserone(phrase);
    else if(c==1)
    res=index.parsertwo(phrase);
    else if(c==2)
    res=index.parserthree(phrase);
    System.out.println("---------------------------------------------------------------------------------");
    System.out.println("document:");
for(int i:res)
{
    System.out.println("\t" + index.sources.get(i) + "\n");
m++;
}
if(m==0)
System.out.println("not found");
System.out.println("---------------------------------------------------------------------------------");
    }
    catch (Exception e) {
        System.out.println("Not found");
    }
}
}