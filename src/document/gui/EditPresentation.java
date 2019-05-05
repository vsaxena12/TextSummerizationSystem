package document.gui;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;

public class EditPresentation {
       
      public static void add() throws IOException{
	 String workdir=System.getProperty("user.dir");
      //opening an existing slide show
      File file = new File(workdir+"\\"+"contentlayout.pptx");
      FileInputStream inputstream=new FileInputStream(file);
      XMLSlideShow ppt = new XMLSlideShow(inputstream);
      
      //adding slides to the slodeshow
      XSLFSlide slide1 = ppt.createSlide();
        try {
            FileOutputStream out = new FileOutputStream(file); 
            ppt.write(out);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
            System.out.println("Presentation edited successfully");
        }	
   }

