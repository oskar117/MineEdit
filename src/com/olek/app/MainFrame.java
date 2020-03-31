package com.olek.app;

import javax.swing.*;
import javax.swing.border.Border;
import javax.tools.Tool;
import java.awt.*;

public class MainFrame extends JFrame {

    private Toolbar toolbar;

    public MainFrame() {
        super("Mapper by Olek");
        setLayout(new BorderLayout());
        setSize(1280, 720);

        toolbar = new Toolbar();
        add(toolbar, BorderLayout.NORTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

}
