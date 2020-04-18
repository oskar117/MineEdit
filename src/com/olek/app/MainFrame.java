package com.olek.app;

import javax.swing.*;
import javax.swing.border.Border;
import javax.tools.Tool;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;

public class MainFrame extends JFrame {

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;

    private Toolbar toolbar;
    private MapFrame mapFrame;
    private MapModel mapModel = new MapModel();

    public MainFrame() {
        super("Mapper by Olek");
        setLayout(new BorderLayout());
        setSize(WIDTH, HEIGHT);

        toolbar = new Toolbar();
        mapFrame = new MapFrame();

        mapModel.addListener(new MapModelListener() {
            @Override
            public void modelChanged() {
                mapFrame.repaint();
            }
        });

        mapFrame.setModel(mapModel);

        add(toolbar, BorderLayout.WEST);
        add(mapFrame, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

}
