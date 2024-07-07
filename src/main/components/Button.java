package main.components;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.JButton;

/**
 *
 */
public class Button extends JButton {

    private String defaultStyle;

    public Button() {
        defaultStyle = ""
                + "borderWidth:0;"
                + "focusWidth:0;"
                + "arc:10;"
                + "margin:5,11,5,11;";

        putClientProperty(FlatClientProperties.STYLE, defaultStyle);
    }

    public void setColor(String color) {
        putClientProperty(FlatClientProperties.STYLE, defaultStyle +
                "foreground:$Button." + color + ".foreground;" +
                "background:$Button." + color + ".background;");
    }
}
