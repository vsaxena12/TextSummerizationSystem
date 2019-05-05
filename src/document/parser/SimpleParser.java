package document.parser;

import com.aliasi.util.Files;
import java.io.File;
import java.io.IOException;

public class SimpleParser {

    public SimpleParser(String fileName) {
        loadFile(fileName);
        detectSentences();
    }

    private void loadFile(String fileName) {
        try {
            File file = new File(fileName);
            document = Files.readFromFile(file, "ISO-8859-1");
            // System.out.println("document"+document);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void detectSentences() {
        SentenceDetection sd = new SentenceDetection(document, false);
        numSentences = sd.getNumSentences();
        System.out.println("numSentence" + numSentences);
    }

    public int getNumSentences() {
        return numSentences;
    }
    private String document;
    private int numSentences;
}