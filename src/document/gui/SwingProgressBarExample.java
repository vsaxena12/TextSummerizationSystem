package document.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class SwingProgressBarExample extends JPanel {
  static int a = 1;
  JProgressBar pbar;

  static final int MY_MINIMUM = 0;

  static int MY_MAXIMUM;

  public SwingProgressBarExample() {
    // initialize Progress Bar
    pbar = new JProgressBar();
    pbar.setMinimum(MY_MINIMUM);
    pbar.setMaximum(MY_MAXIMUM);
    // add to JPanel
    add(pbar);
  }

  public void updateBar(int newValue) {
    pbar.setValue(newValue);
  }
public static void pbar(int max)
{    
     final SwingProgressBarExample it = new SwingProgressBarExample();
    
    JFrame frame = new JFrame("Progress Bar Example");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setContentPane(it);
    frame.pack();
    frame.setVisible(true);
   // final int percent = 1;
      
    }
public void raise(final int percent)
{
    final SwingProgressBarExample it = new SwingProgressBarExample();
    try {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
          public void run() {
            it.updateBar(percent);
          }
        });
        java.lang.Thread.sleep(100);
      } catch (InterruptedException e) {
        
      }
}
}