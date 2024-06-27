package main.menu;

import javax.swing.*;

public class MenuItem {
    private final JButton button;
    private final MenuMap map;

    public MenuItem(JButton button, MenuMap map) {
        this.button = button;
        this.map = map;
    }

    public JButton getButton() {
        return button;
    }

    public MenuMap getMap() {
        return map;
    }
}
