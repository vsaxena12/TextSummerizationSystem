package document.parser;

import com.aliasi.util.Files;
import document.gui.Login;
import document.gui.MainGUI;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import java.io.*;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    MaxentTagger tagger = new MaxentTagger("tagger/left3words-distsim-wsj-0-18.tagger");
    HashMap<String, Integer> Hm_paramweight = new HashMap<String, Integer>();
    int max_weight_keyword = 0;

    public Parser(String fileName, int id, double freqThreshold, String Title) throws URISyntaxException, IOException, SQLException, ClassNotFoundException {
        float sentence_position_Threshold = 0;
        float numof_sentence = 0;
        final double threshold = 0.1;
        Hm_paramweight.put("tf", 4);
        Hm_paramweight.put("pnf", 3);
        Hm_paramweight.put("ndf", 2);
        Hm_paramweight.put("spf", 1);

        max_weight_keyword = Hm_paramweight.get("tf") * Hm_paramweight.get("pnf") * Hm_paramweight.get("ndf") * Hm_paramweight.get("spf");

        System.out.println("max_weight_keyword:" + max_weight_keyword);

        initComponents(freqThreshold);
        loadFile(fileName);
        detectSentences();
        loadStopwords();
        loadStemmer();
        parseSentence(Title);
        System.out.println("numSentences:" + numSentences);
        sentence_position_Threshold = Math.round(numSentences * threshold);
        if (sentence_position_Threshold < 1) {
            sentence_position_Threshold = 1;
        } else {
            System.out.println(" threshold is  above 0");
        }

        System.out.println("sentence_position_Threshold:" + sentence_position_Threshold);
        ArrayList<Integer> sentence_position = Parser.sentence_position((int) sentence_position_Threshold, numSentences);
        System.out.println("High  priority Sentences:" + sentence_position);

        HashMap<Integer, Integer> sentenceLength = Parser.getSentenceLength();
        HashMap<String, ArrayList<Integer>> keywordDetails = Parser.getKeywordDetails();

        ArrayList<String> propernounkeyword = Parser.getpropernounkeyword();
        ArrayList<String> numericwords = Parser.getnumericwords();
        ArrayList<String> titltkeywords = Parser.getTitltkeywords();

        Parser.insertIntoKeydetails(id, keywordDetails, sentence_position, propernounkeyword, numericwords, titltkeywords);

        ArrayList<String> sentecesfromfile = Parser.getsentecesfromfile();
        HashMap<Integer, Integer> properNoundata = Parser.getproperNoun();


        HashMap<Integer, Integer> titlefeature_sid = Parser.get_titlefeature_sid();
        HashMap<Integer, Integer> numericdata = Parser.getNumericdata();

        Parser.insertIntoSentenceDetails(id, sentenceLength, sentences, titlefeature_sid, numericdata, properNoundata);
        HashMap<String, Integer> allfeaturesweight = Parser.getAllfeaturesweight(id, Hm_paramweight);

        Connection conn = Login.connectDB();
        Statement stmt = conn.createStatement();


        for (int i = 0; i < numSentences; i++) {

            getsentenceweight(stmt, i + 1, id, max_weight_keyword, allfeaturesweight);

        }
        int userid = MainGUI.getuserid();
        ArrayList<Integer> sentenceweight = getsentenceweight(fileName, userid, conn, stmt);

    }

    public  static  ArrayList<Integer> getsentenceweight(String filename, int uid, Connection conn, Statement stmt) {
        TreeMap<Integer, Double> weightdata = new TreeMap<Integer, Double>(Collections.reverseOrder());
        ArrayList<Integer> summury_senid = new ArrayList<Integer>();
        try {
            int fileid = 0;
            int sid = 0;
            double weight = 0;
            double docweight = 0;
            double avg_weight;
            int count = 0;
            String query = "select fileid from filedetail where filename= '" + filename + "'and uid='" + uid + "'  ";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                fileid = rs.getInt("fileid");
            }
            String sql = "select sentenceid,weight from sentence_data where fileid='" + fileid + "' ";
            ResultSet rs1 = stmt.executeQuery(sql);
            while (rs1.next()) {
                sid = rs1.getInt("sentenceid");
                weight = rs1.getDouble("weight");
                weightdata.put(sid, weight);
                count++;
            }

            for (Map.Entry<Integer, Double> entry : weightdata.entrySet()) {
                Double docw = entry.getValue();
                docweight = docweight + docw;
            }
            System.out.println("overall  weight  of document:" + docweight);
            avg_weight = docweight / count;
            for (Map.Entry<Integer, Double> entry : weightdata.entrySet()) {
                Double docw = entry.getValue();
                if (docw > avg_weight) {
                    Integer id = entry.getKey();
                    summury_senid.add(id);
                }
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }
        return summury_senid;
    }

    public static void getsentenceweight(Statement stmt, int sid, int fid, int maxweight, HashMap<String, Integer> allkeyweight) throws SQLException {

        int numofkeyword = 0;
        int weightofsentence = 0;
        double sum = 0;
        String sentence_id = "['" + sid + "']";
        System.out.println("sentence_id:" + sentence_id);

        String query = "select length from  sentence_data where fileid='" + fid + "'and  sentenceid='" + sid + "'";
        ResultSet rs1 = stmt.executeQuery(query);
        while (rs1.next()) {
            String str = rs1.getString("length");
            numofkeyword = Integer.parseInt(str);
        }
        String sql = "select  keywords from keyword_data where fileid='" + fid + "' ";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            String key = rs.getString("keywords");
            Integer weight = allkeyweight.get(key);
            sum = sum + (weight * 1.0 / (maxweight * numofkeyword));
        }

        String inser_weight = "update   sentence_data set weight='" + sum + "' where fileid='" + fid + "' and sentenceid='" + sid + "'   ";
        int exe = stmt.executeUpdate(inser_weight);
        if (exe > 0) {
            System.out.println("sentence weight " + sid + ":" + sum);
        } else {
            System.out.println("sentence weight not  inserted");
        }

    }

    public static ArrayList<Integer> sentence_position(int extraction_threshold, int numsentence) {
        ArrayList<Integer> sentences_high = new ArrayList<Integer>();
        for (int i = 0; i < extraction_threshold; i++) {

            sentences_high.add(i + 1);
            sentences_high.add(numsentence - i);
        }
        return sentences_high;
    }

    public static void insertIntoKeydetails(int fid, HashMap<String, ArrayList<Integer>> keydetail, ArrayList<Integer> sen_pos, ArrayList<String> prnnkey, ArrayList<String> numeric_key, ArrayList<String> Title_key) throws SQLException, ClassNotFoundException {

        Connection conn = Login.connectDB();
        Statement stmt = conn.createStatement();
        int freq = 0;
        boolean Ishighweight_pos = false;
        boolean Isprnnkey = false;
        boolean Isnumerickey = false;
        boolean Istitle = false;
        ArrayList<Integer> sentenceid = null;
//        System.out.println("proper nouns:" + prnnkey);
//        System.out.println("numeric_key:" + prnnkey);
        for (Entry<String, ArrayList<Integer>> entry : keydetail.entrySet()) {
            String keyword = entry.getKey();

//            System.out.println("insert  keyword:" + keyword);
            if (!keyword.isEmpty()) {
                if (keyword.contains("'")) {
                    keyword = keyword.replace("'", "");
                }

                if (prnnkey.contains(keyword)) {
                    Isprnnkey = true;
                } else {
                    Isprnnkey = false;
                }

                if (numeric_key.contains(keyword)) {
                    Isnumerickey = true;
                } else {
                    Isnumerickey = false;
                }
                if (Title_key.contains(keyword)) {
                    Istitle = true;
                } else {
                    Istitle = false;
                }

                sentenceid = entry.getValue();
                for (int i = 0; i < sentenceid.size(); i++) {
                    Integer integer = sentenceid.get(i);

                    if (sen_pos.contains(integer)) {
                        Ishighweight_pos = true;
                    } else {
                        Ishighweight_pos = false;
                    }
                }
                freq = sentenceid.size();
                String sql = "insert  into   keyword_data(fileid,keywords,occoutInSentence,Term_Weight,title_feature,sentence_position,proper_noun,numericalword) values('" + fid + "','" + keyword + "','" + sentenceid + "','" + freq + "','" + Istitle + "','" + Ishighweight_pos + "','" + Isprnnkey + "','" + Isnumerickey + "')";

//                System.out.println("sql:" + sql);
                int executeUpdate = stmt.executeUpdate(sql);
                if (executeUpdate > 0) {
//                    System.out.println(" values are  inserted  successfully");
                } else {
//                    System.out.println(" values are   not  inserted");
                }
            }
        }
    }

    public static HashMap<String, Integer> getAllfeaturesweight(int fid, HashMap<String, Integer> paramweight) throws SQLException, ClassNotFoundException {
        HashMap<String, Integer> keyword_weight = new HashMap<String, Integer>();
        Connection conn = Login.connectDB();
        Statement stmt = conn.createStatement();
        int tf1, spf1, pnf1, ndf1, key_weight = 0;

        int tfval, spfval, pnfval, ndfval;
        String tf, spf = null, pnf = null, ndf = null;

        tfval = paramweight.get("tf");
        spfval = paramweight.get("spf");
        pnfval = paramweight.get("pnf");
        ndfval = paramweight.get("ndf");
        System.out.println(" tf:" + tfval + "spf:" + spf + "pnf:" + pnf + "ndf:" + ndf);


        String sql = " select keywords ,title_feature,sentence_position,proper_noun,numericalword from  keyword_data where fileid='" + fid + "'";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {

            String keyword = rs.getString("keywords");
            tf = rs.getString("title_feature");
            spf = rs.getString("sentence_position");
            pnf = rs.getString("proper_noun");
            ndf = rs.getString("numericalword");

            if (tf.equals("true")) {

                tf1 = tfval * 1;

            } else {
                tf1 = tfval * 0;

            }

            if (spf.equals("true")) {
                spf1 = spfval * 1;
            } else {
                spf1 = spfval * 0;

            }
            if (pnf.equals("true")) {
                pnf1 = pnfval * 1;
            } else {
                pnf1 = pnfval * 0;

            }

            if (ndf.equals("true")) {
                ndf1 = ndfval * 1;
            } else {
                ndf1 = ndfval * 0;

            }

            key_weight = tf1 + spf1 + pnf1 + ndf1;
            System.out.println("keyword:" + keyword + "key_weight:" + key_weight);
            keyword_weight.put(keyword, key_weight);
        }
        return keyword_weight;
    }

    public static void insertIntoSentenceDetails(int id, HashMap<Integer, Integer> sen, List data, HashMap<Integer, Integer> hm_titlekey, HashMap<Integer, Integer> hm_numeric, HashMap<Integer, Integer> hm_propernoun) throws SQLException, ClassNotFoundException {
        Connection conn = Login.connectDB();
        Statement stmt = conn.createStatement();
        Collection<Integer> values = hm_titlekey.values();
        Object[] title_key_freq = values.toArray();
        Collection<Integer> properdata = hm_propernoun.values();

        int count = 0;
        int numericcount = 0;
        int nouncount = 0;
        Collection<Integer> numeric_count = hm_numeric.values();
        Object[] numeric = numeric_count.toArray();
        for (Entry<Integer, Integer> entry : sen.entrySet()) {
            Integer sid = entry.getKey();
            Integer length = entry.getValue();
            Object[] toArray = data.toArray();
            String sentenced = (String) toArray[count];
            numericcount = (Integer) numeric[count];
            Object[] arr_nouns = properdata.toArray();
            nouncount = (Integer) arr_nouns[count];
            Integer val;

            val = (Integer) title_key_freq[count];
            sentenced = sentenced.replaceAll("\\'", "");

            String sql = "insert  into   sentence_data(fileid,sentenceid,sentence,length,Titlekeyoccurence,numeric_count,propernoun) values('" + id + "','" + sid + "','" + sentenced + "','" + length + "','" + val + "','" + numericcount + "','" + nouncount + "')";
//            System.out.println("sql:" + sql);
            int executeUpdate = stmt.executeUpdate(sql);
            if (executeUpdate > 0) {
//                System.out.println(" values are  inserted   into  sentence_data successfully");
            } else {
//                System.out.println(" values are   not  inserted in sentence_data ");
            }
            count++;
        }

    }

    private void initComponents(double freqThreshold) {
        sentences = new ArrayList();
        sparseSentences = new ArrayList();
        stopwords = new HashSet();
        dictionary = new HashMap();
        this.freqThreshold = freqThreshold;
    }

    private void readFile(String fileName) throws IOException {
        BufferedReader inputStream;
        inputStream = null;
        numSentences = 0;
        inputStream = new BufferedReader(new FileReader(fileName));
        do {
            String l;
            if ((l = inputStream.readLine()) == null) {
                break;
            }
            if (l.length() != 0) {
                sentences.add(l.trim());
                numSentences = numSentences + 1;
                System.out.println("+numSentences in parser read file " + numSentences);
            }
        } while (true);
        FileNotFoundException ex;
        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } // Misplaced declaration of an exception variable
        catch (FileNotFoundException ex1) {
            ex1.printStackTrace();
        }


        if (numSentences < 1) {
            System.err.println("Empty file. System Exiting...");
            System.exit(1);
        }
        return;
    }

    private void loadFile(String fileName) {
        try {
            File file = new File(fileName);
            document = Files.readFromFile(file, "ISO-8859-1");
            // System.out.println(document);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void detectSentences() {
        SentenceDetection sd = new SentenceDetection(document, true);
        numSentences = sd.getNumSentences();
        sentences = sd.getSentences();

//        List<String> list = new ArrayList<String>(sentences);
//
//        String[] GPXFILES1 = (String[]) sentences.toArray(new String[sentences.size()]);
//        for (int i = 0; i < list.size(); i++) {
//            String get = list.get(i);
//            sentences_list.add(get);
//        }
//        System.out.println("sentences_list :" + sentences_list.size());

        HashMap<Integer, String> sentencesdetail = sd.getSentencesdetail();
        // System.out.println(sentences);
        System.out.println(" number  of  Sentences" + numSentences);
        System.out.println("--------------------");
    }

    private void loadStopwords() throws URISyntaxException, IOException {

        BufferedReader inputStream;
        inputStream = null;
        File file1 = new File("src\\config\\stopword.list");
        inputStream = new BufferedReader(new FileReader(file1));
        do {
            String line;
            if ((line = inputStream.readLine()) == null) {
                break;
            }
            if (line.length() != 0 && line.trim().length() != 0) {
                stopwords.add(line.trim());
            }
            //  System.out.println("stopwords"+stopwords);
        } while (true);
        FileNotFoundException ex;
        if (inputStream != null) {
            try {
                inputStream.close();
            } // Misplaced declaration of an exception variable
            catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    private void loadStemmer() {

        File file = null;
        try {
            file = new File("src\\config\\stemrules.txt");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        paice_stemmer = new PaiceStemmer(file.getAbsolutePath(), "/p");
        porter_stemmer = new PorterStemmer();
    }

    private void parseSentence(String Title) {
        String Title_words[] = Title.split("\\s+");

        int keywordcount = 0;
        int senetnce_count = 0;

        SentencesLength = new HashMap<Integer, Integer>();
        properNoun_count = new HashMap<Integer, Integer>();

        Numericdatacount = new HashMap<Integer, Integer>();
        title_frequency = new HashMap<Integer, Integer>();
        KeywordDetails = new HashMap<String, ArrayList<Integer>>();
        properNoun_keyword = new ArrayList<String>();
        numeric_keyword = new ArrayList<String>();
        title_keywords = new ArrayList<String>();


        if (sentences == null || sentences.isEmpty()) {
            return;
        }

        String splitRegex = "[\\_\\(\\)\\[\\]\\-\\'\\`\\;\\|\\{\\}\\:\\,\\\"\\s]+";
        Iterator i$;
        for (i$ = sentences.iterator(); i$.hasNext();) {
            int titlecounter = 0;
            senetnce_count++;
            int propernoun = 0;
            int numeric_Count = 0;

            String str = (String) i$.next();

//            numeric_Count = Parser.getNumeric_Count(str);
//            System.out.println("numeric_Count" + numeric_Count);



            str = str.trim();
            if (str.endsWith(".") || str.endsWith("?") || str.endsWith("!")) {
                str = str.substring(0, str.length() - 1);
            }
            if (str.endsWith("...")) {
                str = str.substring(0, str.length() - 3);
            }
            String words[] = str.split("\\s+");

            String arr$[] = words;
            int len$ = arr$.length;
            int i = 0;
            keywordcount = 0;
            while (i < len$) {

                String word = arr$[i];
                word = word.trim().toLowerCase();
//                System.out.println(" before " + word);
                if (!stopwords.contains(word) && word.length() != 0) {
                    ArrayList<Integer> Sid = new ArrayList<Integer>();

                    for (int j = 0; j < sentences.size(); j++) {
                        String wd = (String) sentences.get(j);
                        if (wd.contains(word)) {
                            Sid.add(j + 1);
                        }
                    }

                    if (!Sid.contains(senetnce_count)) {
                        Sid.add(senetnce_count);
                    }

                    boolean n_Count = Isnumeric(word);
                    if (n_Count) {
                        numeric_keyword.add(word);
                        numeric_Count++;
                    }


                    // The tagged string
                    String tagged = tagger.tagString(word);
                    if (tagged.contains("NN") || tagged.contains("NNP") || tagged.contains("NNPS") || tagged.contains("NNS")) {
                        properNoun_keyword.add(word);
                        propernoun++;
                    }
                    numTotalWords++;
                    keywordcount++;
//                    System.out.println("sid:" + senetnce_count + " match word :" + word);
                    for (int j = 0; j < Title_words.length; j++) {
                        String title_str = Title_words[j];

//                        System.out.println("title_str:" + title_str + "word :" + word);
                        title_str = title_str.trim();
                        if (title_str.equals(word)) {
                            System.out.println("word: " + word);

                            title_keywords.add(word);
                            titlecounter++;
                        }
                    }
                    KeywordDetails.put(word, Sid);
                    word = paice_stemmer.stripAffixes(word);
                    Double count = (Double) dictionary.get(word);
                    dictionary.put(word, Double.valueOf(count != null ? count.doubleValue() + 1.0D : 1.0D));
                    // System.out.println(dictionary+"frequency with word:"+word);


                }
                i++;
            }
//            System.out.println("sid:" + senetnce_count + "keywordcount:" + keywordcount);
            System.out.println("sid:" + senetnce_count + "titlecounter:" + titlecounter);

            SentencesLength.put(senetnce_count, keywordcount);
            title_frequency.put(senetnce_count, titlecounter);
            properNoun_count.put(senetnce_count, propernoun);
            Numericdatacount.put(senetnce_count, numeric_Count);
        }
        numSentences = senetnce_count;
        for (Iterator<Entry<String, ArrayList<Integer>>> it = KeywordDetails.entrySet().iterator(); it.hasNext();) {
            Entry<String, ArrayList<Integer>> entry = it.next();
            String integer = entry.getKey();
            ArrayList<Integer> integer1 = entry.getValue();
            System.out.println("word:" + integer + "sid occurence" + integer1);
        }
        System.out.println("---------------------");
        System.out.println("numTotalWords" + numTotalWords);
        System.out.println("---------------------");
        System.out.println("repeated words");
        System.out.println("-------------------------");

        numUniqueWords = dictionary.size();
        // System.out.println("numUniqueWords"+numUniqueWords);
        if (numUniqueWords == 0) {
            System.err.println("None of the words can be used for summarization. System Exit...");
            System.exit(1);
        }
        i$ = sentences.iterator();
        do {
            if (!i$.hasNext()) {
                break;
            }
            String str = (String) i$.next();
            str = str.trim();
            if (str.endsWith(".") || str.endsWith("?") || str.endsWith("!")) {
                str = str.substring(0, str.length() - 1);
            }
            if (str.endsWith("...")) {
                str = str.substring(0, str.length() - 3);
            }
            // System.out.println("1");
            Map hm = new HashMap();
            String words[] = str.split(splitRegex);
            String arr$[] = words;
            int len$ = arr$.length;
            //   System.out.println("for start");
            for (int i1 = 0; i1 < len$; i1++) {
                String word = arr$[i1];
                word = word.trim().toLowerCase();
                if (!stopwords.contains(word) && dictionary.containsKey(paice_stemmer.stripAffixes(word))) //System.out.println("word"+word);
                {
                    hm.put(paice_stemmer.stripAffixes(word), Double.valueOf(1.0D));
                }
                Set mapSet = (Set) hm.entrySet();
                Iterator mapIterator = mapSet.iterator();
                while (mapIterator.hasNext()) {
                    Map.Entry mapEntry = (Map.Entry) mapIterator.next();
                    // getKey Method of HashMap access a key of map
                    String keyValue = (String) mapEntry.getKey();
                    //getValue method returns corresponding key's value
                    Object value = mapEntry.getValue();
                    // System.out.println("Key : " + keyValue + "= Value : " + value);
                }
                //  System.out.println("entry in hm"+hm);
            }
            //System.out.println("for end");
            //   System.out.println("hm size "+hm.size());
            // System.out.println("sparseSentences before");
            sparseSentences.add(hm);
//            for (int i = 0; i < sparseSentences.size(); i++) {
//                System.out.println(" sparseSentences content"+hm.get(i));
//            }

            if (hm.size() == 0) {
                numUselessSentences++;
            }

        } while (true);
        // System.out.println("end of parse sentence.");
    }

    public String getDocument() {
        return document;
    }

    public List getSentences() {
        return sentences;
    }

    public List getSparseSentences() {
        return sparseSentences;
    }

    public Map getDictionary() {
        return dictionary;
    }

    public int getNumSentences() {
        return numSentences;
    }

    public int getNumTotalWords() {
        return numTotalWords;
    }

    public int getNumUniqueWords() {
        return numUniqueWords;
    }

    public int getNumUsefulSentences() {
        return numSentences - numUselessSentences;
    }

    public PaiceStemmer getPaiceStemmer() {
        return paice_stemmer;
    }

    public Set getStopWords() {
        return stopwords;
    }

    public static ArrayList<String> getpropernounkeyword() {
        return properNoun_keyword;
    }

    public static HashMap<Integer, Integer> getSentenceLength() {
        return SentencesLength;
    }

    public static HashMap<Integer, Integer> getNumericdata() {
        return Numericdatacount;
    }

    public static HashMap<Integer, Integer> get_titlefeature_sid() {
        return title_frequency;
    }

    public static HashMap<String, ArrayList<Integer>> getKeywordDetails() {
        return KeywordDetails;
    }

    public static ArrayList<String> getsentecesfromfile() {

        return sentences_list;
    }

    public static HashMap<Integer, Integer> getproperNoun() {
        return properNoun_count;
    }

    public static ArrayList<String> getnumericwords() {
        return numeric_keyword;
    }

    public static ArrayList<String> getTitltkeywords() {

        return title_keywords;
    }

    public static int getNumeric_Count(String sentence) {
        int Numericcount = 0;
        String str = sentence;
        System.out.println("str:" + str);
        Pattern pattern = Pattern.compile("\\w+([0-9]+)\\w+([0-9]+)");
        Matcher matcher = pattern.matcher(str);
        int groupCount = matcher.groupCount();
        System.out.println("groupCount:" + groupCount);
        System.out.println("");
        while (matcher.find()) {
            System.out.println("find:" + matcher.find());
            System.out.println("word:" + matcher.group());
            Numericcount++;
        }

        return Numericcount;
    }

    public static boolean Isnumeric(String str) {

        boolean isnumeric = false;
        int count = 0;
        char c;
        for (int i = 0; i < str.length(); i++) {
            c = str.charAt(i);
            if (Character.isDigit(c)) {
                System.out.println("word has  digit" + str);
                isnumeric = true;
                break;
            } else {
//                System.out.println("no  numeric:");
            }
        }
        return isnumeric;
    }
    private String document;
    private List sentences;
    private int numSentences;
    private List sparseSentences;
    static HashMap<Integer, Integer> SentencesLength;
    static HashMap<Integer, Integer> properNoun_count;
    static ArrayList<String> properNoun_keyword;
    static ArrayList<String> numeric_keyword;
    static ArrayList<String> title_keywords;
    static HashMap<Integer, Integer> Numericdatacount;
    static HashMap<Integer, Integer> title_frequency;
    static HashMap<String, ArrayList<Integer>> KeywordDetails;
    static HashMap<String, ArrayList<String>> sentence_WithKeywords;
    private int numUselessSentences;
    private Set stopwords;
    private int numTotalWords;
    private Map dictionary;
    private int numUniqueWords;
    private PaiceStemmer paice_stemmer;
    private PorterStemmer porter_stemmer;
    private double freqThreshold;
    static public ArrayList<String> sentences_list;
}
