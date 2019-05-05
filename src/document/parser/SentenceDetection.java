package document.parser;

import com.aliasi.sentences.IndoEuropeanSentenceModel;
import com.aliasi.sentences.SentenceModel;
import com.aliasi.tokenizer.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SentenceDetection {

    public SentenceDetection(String document, boolean mode) {
        numSentences = 0;
        sentences = new ArrayList();
        Sentencedetail = new HashMap<Integer, String>();

        detectSentences(document, mode);
    }

    private void detectSentences(String document, boolean mode) {
        System.out.println("detectSentences:");

        TokenizerFactory TOKENIZER_FACTORY = new IndoEuropeanTokenizerFactory();
        SentenceModel SENTENCE_MODEL = new IndoEuropeanSentenceModel();
        ArrayList tokenList = new ArrayList();
        ArrayList whiteList = new ArrayList();
        Tokenizer tokenizer = TOKENIZER_FACTORY.tokenizer(document.toCharArray(), 0, document.length());
        tokenizer.tokenize(tokenList, whiteList); // Tokenization with whitelist
        String tokens[] = new String[tokenList.size()];
        String whites[] = new String[whiteList.size()];
        tokenList.toArray(tokens);
        whiteList.toArray(whites);
        int sentenceBoundaries[] = SENTENCE_MODEL.boundaryIndices(tokens, whites);
        numSentences = sentenceBoundaries.length;

        System.out.println("hi :");
        if (sentenceBoundaries.length < 1) {
            System.err.println("No sentence boundaries found. ");
            sentences = null;
            numSentences = 0;
        } else if (!mode) {
            sentences = null;
        } else if (mode) {
            System.out.println("hi  1 :");
            int sentStartTok = 0;
            int sentEndTok = 0;
            for (int i = 0; i < sentenceBoundaries.length; i++) {
                System.out.println("sentence  id :" + i);
                sentEndTok = sentenceBoundaries[i];
                StringBuffer strbuf = new StringBuffer();
                for (int j = sentStartTok; j <= sentEndTok; j++) {
                    strbuf.append((new StringBuilder()).append(tokens[j]).append(whites[j + 1]).toString());
                }

                sentences.add(strbuf.toString().trim());
//                String trim = strbuf.toString().trim();
                Sentencedetail.put(i + 1, strbuf.toString().trim());
                sentStartTok = sentEndTok + 1;
//                System.out.println("sentences in detectSentences" + sentences);

            }
//            System.out.println("sentences:" + sentences);
        }
    }

    public List getSentences() {
        return sentences;
    }

    public HashMap<Integer, String> getSentencesdetail() {
        return Sentencedetail;
    }

    public int getNumSentences() {
        return numSentences;
    }
    private int numSentences;
    private List sentences;
    private HashMap<Integer, String> Sentencedetail;
}