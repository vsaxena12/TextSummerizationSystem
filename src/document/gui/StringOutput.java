package document.gui;

import document.parser.PorterStemmer;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class StringOutput<K, V> extends LinkedHashMap<K, V> {

    public static ArrayList<Object> alist1 = new ArrayList<Object>();
    public static ArrayList<Object> alist2 = new ArrayList<Object>();
    public static ArrayList<String> alist3 = new ArrayList<String>();
    public static ArrayList<String> alist4 = new ArrayList<String>();
    public static ArrayList<String> alist5 = new ArrayList<String>();
    public static ArrayList<String> alist6 = new ArrayList<String>();
    public static ArrayList<String> alist7 = new ArrayList<String>();
    public static File folder = new File("C://Users//eiosys//Documents//NetBeansProjects//DocumentClustering//document-clustering//web//files");
    static String temp = "";

  /*  public static void main(String[] args) throws IOException {
        StringOutput<String, String> map = listFilesForFolder(folder);
        StringOutput<String, String> map1 = new StringOutput<String, String>();
        HashMap<String, String> clusterHM = new HashMap<String, String>();
        int size = map.size();
        int j = 0;
        String val = "";
        if (j == 0) {
            val = map.getValue(j);
            Object entry = map.getEntry(j);
            alist1.add(entry);
        }
        j++;
        for (int k = 1; k < size; k++) {
            String val2 = map.getValue(k);
            Object entry1 = map.getEntry(k);
            double cosineValue = cosineSimilarity(val, val2);
            if (cosineValue >= 80) {
                alist1.add(entry1);
            } else {
                alist2.add(entry1);
                for (int z = 1; z < alist2.size(); z++) {
                }
            }
        }
        for (int l = 0; l < alist1.size(); l++) {
            Object ans1 = alist1.get(l);
        }
        for (int m = 0; m < alist2.size(); m++) {
            Object ans2 = alist2.get(m);
        }
        HashMap<String, String> clusterHM2 = new HashMap<String, String>();
        HashMap<String, String> pendingHM = new HashMap<String, String>();
    }*/

    public static double cosineSimilarity(String str1, String str2) {
        char[] str1AllTokenSet = makeCharArray(str1);
        char[] str2AllTokenSet = makeCharArray(str2);
        char[] str1UniqueTokenSet = getUniqueTerms(str1AllTokenSet);
        char[] str2UniqueTokenSet = getUniqueTerms(str2AllTokenSet);
        int termsinStr1 = str1UniqueTokenSet.length;
        int termsinStr2 = str2UniqueTokenSet.length;
        char[] allTokenSet = combineTwoArray(str1AllTokenSet, str2AllTokenSet);
        char[] allUniqueTokenSet = getUniqueTerms(allTokenSet);
        int commonTerms = (termsinStr1 + termsinStr2) - allUniqueTokenSet.length;
        double cosineValue = (commonTerms / (Math.sqrt(termsinStr1) * Math.sqrt(termsinStr2)) * 100);
        return cosineValue;
    }

    public static char[] makeCharArray(String str) {
        str = str.toLowerCase();
        str = str.replaceAll("\\s", "");
        char[] charArr = str.toCharArray();
        return charArr;
    }

    public static String[] makeStringArray(String[] tokenSet, String str) {
        if (str.endsWith("")) {
            str = str + " ";
        }
        int i = 0;
        for (String temp : str.split(",|\\s+")) {
            tokenSet[i] = temp;
            i++;
        }
        return tokenSet;
    }

    public static char[] getUniqueTerms(char[] strTokenSet) {
        ArrayList<Character> tempAL = new ArrayList<Character>();
        for (int i = 0; i < strTokenSet.length; i++) {
            boolean isDistinct = false;
            for (int j = 0; j < i; j++) {
                if (strTokenSet[i] == (strTokenSet[j])) {
                    isDistinct = true;
                    break;
                }
            }
            if (!isDistinct) {
                tempAL.add(strTokenSet[i]);
            }
        }
        char[] allchar = new char[tempAL.size()]; 
        // Copying the contents of the 2d array to a new 1d array
        for (int i = 0; i < tempAL.size(); i++) { // nested for loop - typical 2d array format
            allchar[i] = tempAL.get(i); // copying it to the new array

        }
        return allchar;
    }

    public static char[] combineTwoArray(char[] str1AllTokenSet, char[] str2AllTokenSet) {
        char result[] = new char[str1AllTokenSet.length + str2AllTokenSet.length];
        System.arraycopy(str1AllTokenSet, 0, result, 0, str1AllTokenSet.length);
        System.arraycopy(str2AllTokenSet, 0, result, str1AllTokenSet.length, str2AllTokenSet.length);
        return result;
    }

    public static String[] stringToStringArr(String string) {
        String[] strArr = string.split(" ");
        for (int i = 0; i < strArr.length; i++) {
            //System.out.print("strarry="+strArr[i]);
        }

        return strArr;
    }

    public static String txtToString(File file) {
        int ch;
        String strContent = "";
        String strContent1 = "";
//        String filename1 = file.getName();
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

    public static StringOutput<String, String> listFilesForFolder(final File folder) {
        StringOutput<String, String> map = new StringOutput<String, String>();
        for (final File fileEntry : folder.listFiles()) {
            System.out.println("fileEntry:"+fileEntry.toString());
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                if (fileEntry.isFile()) {
                    temp = fileEntry.getName();
                    System.out.println("temp name:"+temp);
                    if ((temp.substring(temp.lastIndexOf('.') + 1, temp.length()).toLowerCase()).equals("txt")||(temp.substring(temp.lastIndexOf('.') + 1, temp.length()).toLowerCase()).equals("docx")||(temp.substring(temp.lastIndexOf('.') + 1, temp.length()).toLowerCase()).equals("doc")) {
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
                    }
                }
            }
        }
        System.out.println("map size :"+map.size());
        return map;
    }

    public V getValue(int i) {
        Map.Entry<K, V> entry = this.getEntry(i);
        if (entry == null) {
            return null;
        }
        return entry.getValue();
    }

    public Map.Entry<K, V> getEntry(int i) {
        // check if negetive index provided
        Set<Map.Entry<K, V>> entries = entrySet();
        int j = 0;

        for (Map.Entry<K, V> entry : entries) {
            //  System.out.println("entry=" + entry);
            if (j++ == i) {
                return entry;
            }
        }

        return null;

    }
}
