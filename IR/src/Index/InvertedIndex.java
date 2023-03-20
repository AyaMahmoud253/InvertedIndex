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
            int docId = Integer.parseInt(filename.substring(0, filename.lastIndexOf(".")));
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] terms = line.split(" ");
                for (String term : terms) {
                    term = term.toLowerCase().replaceAll("[^a-z0-9 ]", "");
                    if (term.length() == 0)
                        continue;
                    if (!index.containsKey(term))
                        index.put(term, new DictEntry());
                    DictEntry entry = index.get(term);
                    entry.term_freq++;
                    Posting curr = entry.pList;
                    Posting prev = null;
                    boolean found = false;
                    while (curr != null) {
                        if (curr.docId == docId) {
                            curr.term_freq++;
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
                        posting.term_freq = 1;
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

    private Posting addPostingToList(Posting head, Posting posting) {// recursive method 
    	/* The method takes two parameters: head, which is the head of the current list, 
    	and posting, which is the new Posting object to be added to the list.*/
        if (head == null)
            return posting;//is head
        if (posting.docId < head.docId) {
        	/*the new Posting object is inserted at the beginning of the list, and its next pointer is set to the old head of the list. 
        	The method then returns the new Posting object as the new head of the list.*/
            posting.next = head;
            return posting;
        }
        /* the method recursively calls itself with the next node in the list (i.e., head.next) and the new Posting object as parameters.
         *  This continues until the new Posting object is inserted at the correct position in the list, and the method returns the original head of the list.*/
        head.next = addPostingToList(head.next, posting);//if >
        return head;
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
    public static void main(String[] args) throws IOException {
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
            System.out.println("Document Frequency: " + result .size());
        }
    }
}