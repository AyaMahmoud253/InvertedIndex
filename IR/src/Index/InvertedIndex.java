package Index;
import java.io.*;
import java.util.*;

public class InvertedIndex {
    HashMap<String, DictEntry> index;

    public InvertedIndex() {
        index = new HashMap<>();
    }
    
    public void buildIndex(String[] filenames) throws IOException {
        for (String filename : filenames) {
        	// Extract the document ID from the filename
            int docId = Integer.parseInt(filename.substring(0, filename.lastIndexOf(".")));//from begin to before .
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line = null;
            while ((line = reader.readLine()) != null) {
            	// Split the line into terms
                String[] terms = line.split(" ");
                for (String term : terms) {
                	  // Normalize the term by converting to lowercase and removing non-alphanumeric characters
                    term = term.toLowerCase().replaceAll("[^a-z0-9 ]", "");
                    if (term.length() == 0)
                        continue;
                    if (!index.containsKey(term))//not already in the index add it
                        index.put(term, new DictEntry());
                    DictEntry entry = index.get(term);
                    entry.term_freq++;
                    Posting curr = entry.pList;
                    Posting prev = null;
                    boolean found = false;
                    while (curr != null) {
                        if (curr.docId == docId) {
                            curr.term_freq++; //found
                            found = true;
                            break;
                        }
                        prev = curr;
                        curr = curr.next;
                    }
                    if (!found) {
                        entry.doc_freq++;
                        Posting posting = new Posting();
                        posting.docId = docId;
                        posting.term_freq = 1;//frist time
                        if (prev == null) {
                            entry.pList = posting;
                        } else {
                            prev.next = posting;
                        }
                    }
                }
            }
            reader.close();
        }
    }
    
    public Map<Integer, Integer> searchWithTermFreq(String word) {
        word = word.toLowerCase().replaceAll("[^a-z0-9 ]", "");
        if (!index.containsKey(word))
            return null;
        Posting pList = index.get(word).pList;
        Map<Integer, Integer> result = new HashMap<>(); // Use a map instead of a list to store document IDs and their corresponding term frequency
        while (pList != null) {
            result.put(pList.docId, pList.term_freq); // Add the document ID and its corresponding term frequency to the map
            pList = pList.next;
        }
        return result;
    }
    public int getDocFreq(String word) {
        word = word.toLowerCase().replaceAll("[^a-z0-9 ]", "");
        if (!index.containsKey(word))
            return 0;
        return index.get(word).doc_freq;
    }
    public static void main(String[] args) throws IOException {
    	BufferedWriter a=new BufferedWriter(new FileWriter("0.txt"));
    	a.write("aya Mahmoud \n");
    	a.write("aya naira hager \n");
    	a.close();
    	BufferedWriter b=new BufferedWriter(new FileWriter("1.txt"));
    	b.write("Is is best \n");
    	b.close();
    	BufferedWriter k=new BufferedWriter(new FileWriter("2.txt"));
    	k.write("Ds is best \n");
    	k.close();
    	BufferedWriter c=new BufferedWriter(new FileWriter("3.txt"));
    	c.write("naira hager aya \n");
    	c.write("koko nono Doaa\n");
    	c.close();
    	BufferedWriter d=new BufferedWriter(new FileWriter("4.txt"));
    	d.write("moura baba \n");
    	d.write("nono soso\n");
    	d.close();
    	BufferedWriter e=new BufferedWriter(new FileWriter("5.txt"));
    	e.write("aya tota \n");
    	e.write(" naira hager \n");
    	e.close();
    	BufferedWriter m=new BufferedWriter(new FileWriter("6.txt"));
    	m.write("yoyo yoyo\n");
    	m.close();
    	BufferedWriter n=new BufferedWriter(new FileWriter("7.txt"));
    	n.write("jojo is the best \n");
    	n.close();
    	BufferedWriter o=new BufferedWriter(new FileWriter("8.txt"));
    	o.write("zozo zozo\n");
    	o.write("zozo \n");
    	o.close();
    	BufferedWriter z=new BufferedWriter(new FileWriter("9.txt"));
    	z.write("toto \n");
    	z.write("nona \n");
    	z.close();
        String[] filenames = {"0.txt","1.txt","2.txt","3.txt","4.txt","5.txt","6.txt","7.txt","8.txt","9.txt"};
        InvertedIndex index = new InvertedIndex();
        index.buildIndex(filenames);
        Scanner sc= new Scanner(System.in);   
        System.out.print("Enter a Word to search: ");  
        String str= sc.nextLine();   
        Map<Integer, Integer>  result = index.searchWithTermFreq(str);
        if (result  == null || result .isEmpty()) {
            System.out.println("No documents found containing the word : " + str);
        } else {
            System.out.println("Documents containing the word : " + str);
            for (Map.Entry<Integer, Integer> entry : result .entrySet()) {
                System.out.println("Document: " + entry.getKey() + ".txt , Term Frequency: " + entry.getValue());
            }
            System.out.println("Document Frequency: " + index.getDocFreq(str));
        }
    }
}