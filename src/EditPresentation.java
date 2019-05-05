
import java.io.FileOutputStream;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.usermodel.SlideShow;

public class EditPresentation {

    public static void main(String[] args) {
        try {
            SlideShow slideShow = new SlideShow();
            Slide slide = slideShow.createSlide();
            FileOutputStream out = new FileOutputStream("slideshow.ppt");
            System.out.println("  done :");
            slideShow.write(out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
