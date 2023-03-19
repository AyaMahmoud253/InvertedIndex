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
                    if (entry.pList == null || entry.pList.docId != docId) {
                        entry.doc_freq++;
                        Posting posting = new Posting();
                        posting.docId = docId;
                        entry.pList = addPostingToList(entry.pList, posting);
                    } else {
                        entry.pList.dtf++;
                    }
                }
            }
            reader.close();
        }
    }

    private Posting addPostingToList(Posting head, Posting posting) {
        if (head == null)
            return posting;
        if (posting.docId < head.docId) {
            posting.next = head;
            return posting;
        }
        head.next = addPostingToList(head.next, posting);
        return head;
    }

    public List<Integer> search(String query) {
        query = query.toLowerCase().replaceAll("[^a-z0-9 ]", "");
        if (!index.containsKey(query))
            return null;
        Posting pList = index.get(query).pList;
        List<Integer> result = new ArrayList<>();
        while (pList != null) {
            result.add(pList.docId);
            pList = pList.next;
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
        String[] filenames = {"1.txt","2.txt"};
        InvertedIndex index = new InvertedIndex();
        index.buildIndex(filenames);
        List<Integer> result = index.search("omar");
        if (result != null) {
            for (int docId : result) {
                System.out.println(docId + ".txt");
            }
        } else {
            System.out.println("Not found.");
        }
    }
}