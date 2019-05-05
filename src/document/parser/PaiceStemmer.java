package document.parser;

import java.io.*;
import java.util.StringTokenizer;
import java.util.Vector;

public class PaiceStemmer {

    public PaiceStemmer(String rules, String pre) {
        ruleTable = new Vector();
        ruleIndex = new int[26];
        preStrip = false;
        filerules = rules;
        if (pre.equals("/p")) {
            preStrip = true;
        }
        ReadRules(filerules);
    }

    private void ReadRules(String stemRules) {
        int ruleCount = 0;
        int j = 0;
        try {
            FileReader fr = new FileReader(stemRules);
            BufferedReader br = new BufferedReader(fr);
            String line = " ";
            try {
                while ((line = br.readLine()) != null) {
                    ruleCount++;
                    j = 0;
                    String rule = new String();
                    rule = "";
                    for (; j < line.length() && line.charAt(j) != ' '; j++) {
                        rule = (new StringBuilder()).append(rule).append(line.charAt(j)).toString();
                    }

                    ruleTable.addElement(rule);

                }
            } catch (Exception e) {
                System.err.println((new StringBuilder()).append("File Error Durring Reading Rules").append(e).toString());
                System.exit(0);
            }
            try {
                fr.close();
            } catch (Exception e) {
                System.err.println("Error Closing File During Reading Rules");
            }
        } catch (Exception e) {
            System.err.println((new StringBuilder()).append("Input File").append(stemRules).append("not found").toString());
            System.exit(1);
        }
        char ch = 'a';
        for (j = 0; j < 25; j++) {
            ruleIndex[j] = 0;
        }

        for (j = 0; j < ruleCount - 1; j++) {
            while (((String) ruleTable.elementAt(j)).charAt(0) != ch) {
                ch++;
                ruleIndex[charCode(ch)] = j;
            }
        }

    }

    private int FirstVowel(String word, int last) {
        int i = 0;
        if (i < last && !vowel(word.charAt(i), 'a')) {
            i++;
        }
        if (i != 0) {
            for (; i < last && !vowel(word.charAt(i), word.charAt(i - 1)); i++);
        }
        if (i < last) {
            return i;
        } else {
            return last;
        }
    }

    private String stripSuffixes(String word) {
        //System.out.println("stripSuffixes");
//        System.out.println("INDIA");
        int ruleok = 0;
        int Continue = 0;
        int pll = 0;
        String rule = "";
        String stem = "";
        boolean intact = true;
        stem = Clean(word.toLowerCase());
        for (pll = 0; pll + 1 < stem.length() && stem.charAt(pll + 1) >= 'a' && stem.charAt(pll + 1) <= 'z'; pll++);
        if (pll < 1) {
            Continue = -1;
        }
        int pfv = FirstVowel(stem, pll);
        int iw = stem.length() - 1;
        while (Continue != -1) {
            Continue = 0;
            char ll = stem.charAt(pll);
            int prt;
            if (ll >= 'a' && ll <= 'z') {
                prt = ruleIndex[charCode(ll)];
            } else {
                prt = -1;
            }
            if (prt == -1) {
                Continue = -1;
            }
            if (Continue == 0) {
                rule = (String) ruleTable.elementAt(prt);
                while (Continue == 0) {
                    ruleok = 0;
                    if (rule.charAt(0) != ll) {
                        Continue = -1;
                        ruleok = -1;
                    }
                    int ir = 1;
                    iw = pll - 1;
                    while (ruleok == 0) {
                        if (rule.charAt(ir) >= '0' && rule.charAt(ir) <= '9') {
                            ruleok = 1;
                        } else if (rule.charAt(ir) == '*') {
                            if (intact) {
                                ir++;
                                ruleok = 1;
                            } else {
                                ruleok = -1;
                            }
                        } else if (rule.charAt(ir) != stem.charAt(iw)) {
                            ruleok = -1;
                        } else if (iw <= pfv) {
                            ruleok = -1;
                        } else {
                            ir++;
                            iw--;
                        }
                    }
                    if (ruleok == 1) {
                        int xl;
                        for (xl = 0; rule.charAt(ir + xl + 1) < '.' || rule.charAt(ir + xl + 1) > '>'; xl++);
                        xl = (pll + xl + 48) - rule.charAt(ir);
                        if (pfv == 0) {
                            if (xl < 1) {
                                ruleok = -1;
                            }
                        } else if ((xl < 2) | (xl < pfv)) {
                            ruleok = -1;
                        }
                    }
                    if (ruleok == 1) {
                        intact = false;
                        pll = (pll + 48) - rule.charAt(ir);
                        ir++;
                        stem = stem.substring(0, pll + 1);
                        while (ir < rule.length() && 'a' <= rule.charAt(ir) && rule.charAt(ir) <= 'z') {
                            stem = (new StringBuilder()).append(stem).append(rule.charAt(ir)).toString();
                            ir++;
                            pll++;
                            //  System.out.println("keyword suffix:"+stem.toString());
                        }
                        if (rule.charAt(ir) == '.') {
                            Continue = -1;
                        } else {
                            Continue = 1;
                        }
                    } else {
                        prt++;
                        rule = (String) ruleTable.elementAt(prt);
                        if (rule.charAt(0) != ll) {
                            Continue = -1;
                        }
                    }
                }
            }
        }
        //   System.out.println("stripSuffixes end");
        return stem;
    }

    private boolean vowel(char ch, char prev) {
        switch (ch) {
            case 97: // 'a'
            case 101: // 'e'
            case 105: // 'i'
            case 111: // 'o'
            case 117: // 'u'
                return true;

            case 121: // 'y'
                switch (prev) {
                    case 97: // 'a'
                    case 101: // 'e'
                    case 105: // 'i'
                    case 111: // 'o'
                    case 117: // 'u'
                        return false;
                }
                return true;
        }
        return false;
    }

    private int charCode(char ch) {
        return ch - 97;
    }

    private String stripPrefixes(String str) {
//        System.out.println("PAK");
        String prefixes[] = {
            "kilo", "micro", "milli", "intra", "ultra", "mega", "nano", "pico", "pseudo"
        };
        int last = prefixes.length;
        for (int i = 0; i < last; i++) {
            if (str.startsWith(prefixes[i]) && str.length() > prefixes[i].length()) {
                str = str.substring(prefixes[i].length());
                //  System.out.println(" keyword:" + str);
                return str;
            }
        }
        // System.out.println("keyword':" + str);
        return str;
    }

    private String Clean(String str) {
        int last = str.length();
        String temp = "";
        for (int i = 0; i < last; i++) {
            if ((str.charAt(i) >= 'a') & (str.charAt(i) <= 'z')) {
                temp = (new StringBuilder()).append(temp).append(str.charAt(i)).toString();
            }
        }
//        System.out.println("temp:" + temp);
        return temp;
    }

    public String stripAffixes(String str) {
        //  System.out.println("stripAffixes:");
        if (str.length() > 3 && preStrip) {
            str = stripPrefixes(str);
        }
        if (str.length() > 3) {
            str = stripSuffixes(str);
        }

        return str;
    }

    private Vector ruleTable;
    private int ruleIndex[];
    private boolean preStrip;
    private String filerules;
}