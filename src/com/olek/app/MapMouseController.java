package com.olek.app;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class MapMouseController extends MouseAdapter {

    private MapModel model;

    int prevX = 0;
    int prevY = 0;

    public MapMouseController(MapModel model) {
        this.model=model;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        prevX = 0;
        prevY = 0;
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        int currX = mouseEvent.getX();
        int currY = mouseEvent.getY();

        if(prevX != 0 && prevY != 0) {

            model.setOffsetX(model.getOffsetX() + (currX - prevX));
            model.setOffsetY(model.getOffsetY() + (currY - prevY));

            model.update();
            System.out.println(model);
        }

        prevY = currY;
        prevX = currX;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

        int val = model.getChunkSize();

        if(e.getWheelRotation() > 0) {
            val /= 2;
        } else {
            val *= 2;
        }

        if(val > 0) {
            model.setChunkSize(val);
            model.update();
        }
    }
}
