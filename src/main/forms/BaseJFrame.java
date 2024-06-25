
package main.forms;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.JFrame;

/**
 *
 */
public class BaseJFrame extends JFrame {
    public static void initLookAndFeel() {
        FlatLaf.registerCustomDefaultsSource("main.theme");
        FlatLightLaf.setup();
    }
}
