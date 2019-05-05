package document.gui;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.xslf.usermodel.SlideLayout;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.apache.poi.xslf.usermodel.XSLFSlideMaster;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

public class TitleAndBodyLayout {
    
    public static void contentAdd(String data,XMLSlideShow obj) throws FileNotFoundException, IOException
    
    { 
        String workingdir=System.getProperty("user.dir");
        
      String str=null;
       
      XSLFSlideMaster slideMaster = obj.getSlideMasters()[0];
      
      //select a layout from specified list
      XSLFSlideLayout slidelayout = slideMaster.getLayout(SlideLayout.TITLE_AND_CONTENT);      
      
      //creating a slide with title and content layout
      XSLFSlide slide = obj.createSlide(slidelayout);
        XSLFTextShape title = slide.getPlaceholder(0);
        title.setText("Summary");
      
      //selection of body placeholder
      XSLFTextShape body = slide.getPlaceholder(1);
        System.out.println("Current working dir is:"+workingdir);  
      //clear the existing text in the slide
      body.clearText();
      
      //adding new paragraph
      body.addNewTextParagraph().addNewTextRun().setText(data);
      
      //create a file object
      File file=new File(workingdir+"\\"+"contentlayout.pptx");
        try{ 
            FileOutputStream out = new FileOutputStream(file); 
            obj.write(out);
            System.out.println("slide cretated successfully");
        }       
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}