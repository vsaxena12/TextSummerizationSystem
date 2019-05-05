package document.gui;

import java.util.ArrayList;

public class StringCosineSimilarity {

    public static String str1, str2;

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

        for (int i = 0; i < tempAL.size(); i++) { 
            allchar[i] = tempAL.get(i); 

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
        }

        return strArr;
    }
}
