package document.gui;

import document.parser.PorterStemmer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Clustering {

    private static ResultSet rs = null;
    private static List al;
    private static ArrayList docAL;
    private static HashMap hashmap;
    private static ArrayList tempAL;
    private static HashMap docHashMap;
    private static ArrayList clusterAL, pendingAL;
    private static HashMap freqHashMap;
    private static ArrayList clusNameAl;
    private static ArrayList proIdAl;
    private static ArrayList finalclusterAL = new ArrayList();
    static String temp = "";
    public static void makeClusterSet(File folder) throws Exception {
        try {

            al = new ArrayList();
            docAL = new ArrayList();
            hashmap = new HashMap();
            tempAL = new ArrayList();
            docHashMap = new HashMap();

            HashMap<String, String> filesHM = listFilesForFolder(folder);


            String refKey = "";
            String refValue = "";

            for (Map.Entry<String, String> entry : filesHM.entrySet()) {
                refKey = entry.getKey();
                refValue = entry.getValue();
                docHashMap.put(refValue, refKey);
                docAL.add(refValue);
                for (String token3 : refValue.split(" ")) {
                    al.add(token3);//stores all attributes elements in arraylist al
                }
            }

            finalclusterAL = assignFreqVal();
            System.out.println("finalclusterAL=>" + finalclusterAL);
            nameCluster(finalclusterAL);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static StringOutput<String, String> listFilesForFolder(final File folder) throws FileNotFoundException, IOException {
        StringOutput<String, String> map = new StringOutput<String, String>();
        for (final File fileEntry : folder.listFiles()) {
            System.out.println("fileEntry:" + fileEntry.toString());
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                if (fileEntry.isFile()) {
                    temp = fileEntry.getName();
                    System.out.println("temp name:" + temp);
                    if ((temp.substring(temp.lastIndexOf('.') + 1, temp.length()).toLowerCase()).equals("txt")) {
                        File file = new File(folder + "//" + temp);
                        String doc = txtToString(file);
                        String newContent = "";
                        for (String token1 : doc.split(" ")) {
                            ArrayList<String> al = DocRead.getStopWords();
                            if (!al.contains(token1)) {
                                String keyWord = new PorterStemmer().stripAffixes(token1);
                                newContent = newContent + keyWord + " ";
                            }
                        }

                        map.put(temp, newContent);
                    } else if ((temp.substring(temp.lastIndexOf('.') + 1, temp.length()).toLowerCase()).equals("docx")) {
                        System.out.println("  id is  docx  file :");
                        File file = new File(folder + "//" + temp);
//                        String doc = txtToString(file);
                        System.out.println("file.getAbsolutePath() hello :" + file.getAbsolutePath());
                        String doc = MainGUI.readDocxfile(file.getAbsolutePath());
                        String newContent = "";
                        for (String token1 : doc.split(" ")) {
                            ArrayList<String> al = DocRead.getStopWords();
                            if (!al.contains(token1)) {
                                String keyWord = new PorterStemmer().stripAffixes(token1);
                                newContent = newContent + keyWord + " ";
                            }
                        }

                        map.put(temp, newContent);

                    } else if ((temp.substring(temp.lastIndexOf('.') + 1, temp.length()).toLowerCase()).equals("doc")) {
                        System.out.println("  id is  docx  file :");
                        File file = new File(folder + "//" + temp);
//                        String doc = txtToString(file);
                        System.out.println("file.getAbsolutePath() hello :" + file.getAbsolutePath());
//                        String doc = MainGUI.readDocxfile(file.getAbsolutePath());
                        String doc = MainGUI.ReadDocFile_working(file.getAbsolutePath());
                        String newContent = "";
                        for (String token1 : doc.split(" ")) {
                            ArrayList<String> al = DocRead.getStopWords();
                            if (!al.contains(token1)) {
                                String keyWord = new PorterStemmer().stripAffixes(token1);
                                newContent = newContent + keyWord + " ";
                            }
                        }

                        map.put(temp, newContent);

                    }
                }
            }
        }
        System.out.println("map size :" + map.size());
        return map;
    }

    public static String txtToString(File file) {
        int ch;
        String strContent = "";
        String strContent1 = "";
//        String filename1 = Clustering.getName();
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(file);
            while ((ch = fin.read()) != -1) //strContent.append((char) ch);
            {
                strContent = strContent + (char) ch;
            }
            strContent1 = strContent.toLowerCase();
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strContent1;

    }

    public static ArrayList assignFreqVal() {
        finalclusterAL = new ArrayList();

        do {
            pendingAL = new ArrayList();
            clusterAL = new ArrayList();
            int size = docAL.size();
            if (debug_level1) {
//                System.out.println("productAL size: " + size);
            }
            String vectorData = (String) docAL.get(0);
            String refDoc = (String) docHashMap.get(vectorData);
            if (debug_level1) {
//                System.out.println("in product arraylist 0th docname=" + refDoc);
            }
            clusterAL.add(vectorData);
            double threshold = 0;
            ArrayList<Double> cosines = new ArrayList<Double>();
            for (int j = 1; j < docAL.size(); j++) {
                String vectorData1 = (String) docAL.get(j);
                String compareDoc = (String) docHashMap.get(vectorData1);
                if (debug_level1) {
//                    System.out.println("ref " + refDoc+" comparedoc " + compareDoc);
                }

                double cosineValue = StringCosineSimilarity.cosineSimilarity(vectorData, vectorData1);
                cosines.add(cosineValue);
                threshold += cosineValue;

                if (debug_level1) {
                    System.out.println("cosineSimilarity: " + cosineValue);
                }
            }
            if (debug_level1) {
                System.out.println("cosines: " + cosines);
            }
            ArrayList<Double> clone = (ArrayList<Double>) cosines.clone();
            Collections.sort(clone);
            double max = clone.get(clone.size() - 1);
            double min = clone.get(0);
            double maxcosineValue = max;
            double mincosineValue = min;
            threshold /= docAL.size() - 1;
            if (debug_level1) {
                System.out.println("thresh " + threshold);
            }

            System.out.println("maxcosineValue" + maxcosineValue + "mincosineValue" + mincosineValue);
            for (int j = 1; j < docAL.size(); j++) {
                String vectorData1 = (String) docAL.get(j);
                String compareDoc = (String) docHashMap.get(vectorData1);
                double cosineValue = (Double) cosines.get(j - 1);

                if (debug_level1) {
                    System.out.println("j " + j + " threshold " + threshold + " cosineValue " + cosineValue);
                }
                if (cosineValue >= threshold || Math.abs(maxcosineValue - mincosineValue) < 5) {
                    if (!clusterAL.contains(vectorData1)) {
                        clusterAL.add(vectorData1);
                    }
                } else {
                    pendingAL.add(vectorData1);
                }
                if (debug_level1) {
//                    System.out.println("cosineSimilarity: " + cosineValue);
                }
            }

//            System.out.println("pendingAL.size(): " + pendingAL.size());
            docAL.clear();
//            System.out.println("productAL.size(): " + docAL.size());
            //productAL.addAll(pendingAL);
            docAL = pendingAL;
            finalclusterAL.add(clusterAL);
//            System.out.println("productAL.size(): " + docAL.size());
        } while (docAL.size() > 0);
        return finalclusterAL;

    }

    public static void nameCluster(ArrayList finalClusterAL) throws SQLException, Exception {
        HashMap clusterName = new HashMap();
        ArrayList tempAL = new ArrayList();
        ArrayList freqAL = new ArrayList();
        freqHashMap = new HashMap();

        String data = null;
        int freq = 0;

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < finalClusterAL.size(); i++) {
            int j = i;
            if (clusterNameDebug) {
                //System.out.println("finalClusterAL.size()" + finalClusterAL.size());
            }
            clusNameAl = new ArrayList();
            proIdAl = new ArrayList();
            ArrayList tempClusterAL = (ArrayList) finalClusterAL.get(i);
            if (clusterNameDebug) {
//                System.out.println("**********tempClusterAL" + tempClusterAL);
            }

            if (clusterNameDebug) {
//                System.out.println("////////// finalClusterAL.get(" + i + ")" + finalClusterAL.get(i));
            }
            for (int a = 0; a < tempClusterAL.size(); a++) {
                data = (String) tempClusterAL.get(a);
                proIdAl.add(data);                   // attributes aaded without removing spaces
                if (clusterNameDebug) {
//                    System.out.println("tempClusterAL.get(" + a + "): " + tempClusterAL.get(a));;
                }
                for (String token3 : data.split(" ")) {

                    if (token3.length() > 3) {
                        if (clusterNameDebug) {
//                            System.out.println("token3" + token3);
                        }
                        tempAL.add(token3);//stores all attributes in arraylist tempAL
                    }
                }
            }
            double avg = 0.0d;
            int size = al.size();
            Set<String> uniqueSet = new HashSet<String>(tempAL);
            for (String temp : uniqueSet) {
                freq = Collections.frequency(tempAL, temp);
                // System.out.println("freq=" + freq);
                clusterName.put(temp, freq); // unique name + frequence added
                freqAL.add(temp);
                if (freq >= 2) {
                    //freqHashMap.put(freq, temp);
                    clusNameAl.add(temp);
                }
                /////       System.out.println("temp: " + temp);
                /////       System.out.println("clusterName.get(temp): " + clusterName.get(temp));
            }

            tempAL.clear();
            insertData(clusNameAl, proIdAl, j); // unide name added  & , full data added 
        }
    }
    static boolean clusterNameDebug = false, debug_level2 = false, debug_level3 = false, debug_level1 = true;

    private static void insertData(ArrayList clusNameAl, ArrayList proIdAl, int j) { // unique name + full values 
        Statement stmt = null;
        j++;
        String cluster_name = "", cluster_values = "";
        int index = 0;
        for (int i = 0; i < clusNameAl.size(); i++) {
            cluster_name += clusNameAl.get(i) + ",";
            if (clusterNameDebug) {
//                System.out.println("+++++++++++cluster_name+++++++++" + cluster_name);
            }
        }
        if (clusterNameDebug) {
//            System.out.println("");
        }
        index = cluster_name.lastIndexOf(',');

        if (clusterNameDebug) {
//            System.out.println("index==" + index);
        }

        if (index != -1) {
            cluster_name = cluster_name.substring(0, index);
            if (clusterNameDebug) {
//                System.out.println("=========cluster_name =========" + cluster_name);
            }
        }
        if (clusterNameDebug) {
//            System.out.println("\ncluster_name: " + cluster_name);
        }

        for (int i = 0; i < proIdAl.size(); i++) {
            cluster_values += docHashMap.get(proIdAl.get(i)) + ",";
        }
        index = cluster_values.lastIndexOf(',');
        if (index != -1) {
            cluster_values = cluster_values.substring(0, index);
        }

//        System.out.println("\ncluster_values: " + cluster_values);
//        Statement stmt = null;
        try {
            int userid = MainGUI.getuserid();
            System.out.println("userid:" + userid);
            String sql1 = "insert into clusterset2 (uid,cluster_name,cluster_value)values ('" + userid + "','cluster" + j + "','" + cluster_values + "')";
            System.out.println("\nsql: " + sql1);
            Connection connectDB = Login.connectDB();
            stmt = connectDB.createStatement();
            stmt.executeUpdate(sql1);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
            } catch (Exception e) {
            }
        }
    }

    public static void copyFile(File source, File dest) throws IOException {

        System.out.println("inside copy");
        try {
            Writer output = null;
            System.out.println("dest:in copy" + dest + "path in copy:" + source);
            output = new BufferedWriter(new FileWriter(dest));

            BufferedReader br = new BufferedReader(new FileReader(source));
            String line;

            while ((line = br.readLine()) != null) {

//                     System.out.println("else line1 ="+line);
                output.write(line);
                output.write("\n");

            }

            br.close();
            output.close();
            System.out.println("File is copied successful!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
