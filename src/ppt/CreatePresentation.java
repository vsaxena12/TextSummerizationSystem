package ppt;
import java.io.File;
import java.io.FileOutputStream;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
public class CreatePresentation {
   public static void main(String args[]){
       try{
           XMLSlideShow ppt = new XMLSlideShow();
           //creating an FileOutputStream object
           File file =new File("D:/example1.ppt");
           FileOutputStream out = new FileOutputStream(file);
           //saving the changes to a file
           ppt.write(out);
           System.out.println("Presentation created successfully");
           out.close();
       } catch (Exception e){
//           System.out.println(""+e.toString());
//           System.out.println(""+
                   e.printStackTrace();//);
       }
   }
}