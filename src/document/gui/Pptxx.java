package document.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.apache.poi.xslf.usermodel.XMLSlideShow;

public class Pptxx {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        try{
            String dir=System.getProperty("user.dir");
           XMLSlideShow ppt = new XMLSlideShow();           
           System.out.println("xml slideshow crtd");
           File file =new File("example1.pptx");
            System.out.println("file obj crtd");
           FileOutputStream out = new FileOutputStream(file);
           ppt.write(out);
           System.out.println("Presentation created successfully");
           out.close();
       } catch (Exception e){
           System.out.println(""+e.toString());
       }
    }
    
    public static void createPPt(){
            try{
           File f= new File("D:/New.txt");
            System.out.println("File crtd");
           FileInputStream fin=new FileInputStream(f);
            System.out.println("fin crtd");
           XMLSlideShow ppt = new XMLSlideShow();
            System.out.println("xml slideshow crtd");
           File file =new File("D:/example1.pptx");
            System.out.println("file obj crtd");
           FileOutputStream out = new FileOutputStream(file);
           ppt.write(out);
           System.out.println("Presentation created successfully");
           out.close();
       } catch (Exception e){
           System.out.println(""+e.toString());
       }
    }
}