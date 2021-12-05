package org.csc133.a5.gameobjects.parts;

import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import org.csc133.a5.gameobjects.GameObject;

public class Rectangle extends GameObject {

    private Rectangle() {
    }

    public Rectangle(int c, int w, int h, float tx, float ty, float sx,
                     float sy, int degreesRotation) {
        setColorInteger(c);
        setDimension(new Dimension(w, h));
        translate(tx, ty);
        scale(sx, sy);
        rotate(degreesRotation);
    }

    @Override
    public void localDraw(Graphics g, Point parentOrigin,
                          Point screenOrigin) {
        g.setColor(getColorInteger());
        // Shifting the origin
        mapViewContainerTranslate(g, parentOrigin);
        forwardPrimitiveTranslate(g, getDimension());
        // Drawing at origin
        g.fillRect(0, 0,
                getDimension().getWidth(), getDimension().getHeight());
    }
}
