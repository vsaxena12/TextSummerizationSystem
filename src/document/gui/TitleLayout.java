package document.gui;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.poi.xslf.usermodel.SlideLayout;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.apache.poi.xslf.usermodel.XSLFSlideMaster;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

public class TitleLayout {

   public static void addcon(String[] args) throws IOException{
   
      //creating presentation
      int argslen=args.length;
      int noofslide=argslen/5;
       System.out.println("args len="+argslen);
      XMLSlideShow ppt = new XMLSlideShow();	
   //   ArrayList<String[]> action = new ArrayList<String[]>();
     
            
      //getting the slide master object
      XSLFSlideMaster slideMaster = ppt.getSlideMasters()[0];
      
      //get the desired slide layout 
      XSLFSlideLayout titleLayout = slideMaster.getLayout(SlideLayout.TITLE);
                                                     
      //creating a slide with title layout
      XSLFSlide slide1 = ppt.createSlide(titleLayout);
      
      //selecting the place holder in it 
      XSLFTextShape title1 = slide1.getPlaceholder(0); 
      
//      title1.setText("Presentation on Documents");
//      File file=new File("D://Titlelayout.pptx");
//      FileOutputStream out = new FileOutputStream(file);
      
      for(int i=0;i<args.length;i++)
      {
          TitleAndBodyLayout.contentAdd(args[i], ppt);
          System.out.println("args"+i+":"+args[i]);
          if(i%10==0)
          {
              EditPresentation.add();
          }
      }
      //save the changes in a PPt document
//      ppt.write(out);
//      System.out.println("slide cretated successfully");
//      out.close();  
   }
}