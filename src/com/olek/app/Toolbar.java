package com.olek.app;

import javax.swing.*;
import java.awt.*;

public class Toolbar extends JPanel {

    private JButton test1;
    private JButton test2;

    public Toolbar() {
        test1 = new JButton("test1");
        test2 = new JButton("test2");

        setLayout(new FlowLayout(FlowLayout.LEFT));

        add(test1);
        add(test2);
    }
}