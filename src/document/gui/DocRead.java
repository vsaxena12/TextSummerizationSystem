package document.gui;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DocRead {

    public static ArrayList<String> getStopWords() {
        ArrayList<String> al = new ArrayList<String>();
        BufferedReader br = null;
        String workingdir=System.getProperty("user.dir");
        try {
            String sCurrentLine;
            br = new BufferedReader(new FileReader(workingdir+"\\"+"src\\stopword.txt"));
            while ((sCurrentLine = br.readLine()) != null) {
               // System.out.println(sCurrentLine);
                al.add(sCurrentLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return al;
    }
}