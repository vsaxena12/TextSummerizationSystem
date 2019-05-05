package document.gui;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

public class test {

    private static String workingDir;

    public static void deleteFiles(String matchname) {

        String workingDir = System.getProperty("user.dir");
        File directory = new File(workingDir);
        File[] files = directory.listFiles();
        for (File f : files) {
            System.out.println("files:" + f.getName());
            if (f.getName().contains(matchname)) {
                f.delete();
                System.out.println("deleted" + f.getName());
            }
        }
    }

    public static void main(String args[]) throws IOException {
        String str = "E:\\DocumentSumerizer\\USERs\\admin\\demo_c.doc";
        String strdata = ReadDocFile_working(str);
        System.out.println("strdata:"+strdata);
    }

    public static String readfile(String fileName) {
        String line = "";
        try {
            File file = new File(fileName);
            FileInputStream fis = new FileInputStream(file.getAbsolutePath());

            XWPFDocument document = new XWPFDocument(fis);
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            System.out.println("" + paragraphs);
            System.out.println("Total no of paragraph " + paragraphs.size());
            for (XWPFParagraph para : paragraphs) {
                System.out.println(para.getText());
                String text = para.getText();
                line = line + text;
            }
            System.out.println("line:" + line);
            fis.close();
        } catch (Exception e) {


            e.printStackTrace();
        }
        return line;
    }

    public static String ReadDocFile_working(String str) throws FileNotFoundException, IOException {
        File file = null;
          String line = "";
        WordExtractor extractor = null;
        try {

            file = new File(str);
            FileInputStream fis = new FileInputStream(file.getAbsolutePath());
            HWPFDocument document = new HWPFDocument(fis);
            extractor = new WordExtractor(document);
            String[] fileData = extractor.getParagraphText();
            for (int i = 0; i < fileData.length; i++) {
                if (fileData[i] != null) {
                    System.out.println("hi:" + fileData[i]);
                    line=line+fileData[i];
                }
            }

        } catch (Exception exep) {
            exep.printStackTrace();
        }
        return line;
    }
}
