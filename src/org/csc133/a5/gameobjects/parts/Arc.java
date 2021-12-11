package org.csc133.a5.gameobjects.parts;

import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import org.csc133.a5.gameobjects.GameObject;


public class Arc extends GameObject {
    private final int startAngle;
    private final int arcAngle;

    private Arc() {
        this.startAngle = 0;
        this.arcAngle = 0;
    }

    public Arc(int color, int width, int height, int startAngle, int arcAngle) {
        this(color, width, height, 0, 0, 1, 1, 0,
                startAngle, arcAngle);
    }

    // All translate and scale
    public Arc(int color,
               int width, int height,
               float tx, float ty,
               float sx, float sy,
               float degreesRotation,
               int startAngle, int arcAngle) {

        setColorInteger(color);
        setDimension(new Dimension(width, height));
        this.startAngle = startAngle;
        this.arcAngle = arcAngle;

        translate(tx, ty);
        scale(sx, sy);
        rotate(degreesRotation);

    }

    @Override
    public void localDraw(Graphics g,
                          Point parentOrigin,
                          Point screenOrigin) {
        g.setColor(getColorInteger());
        mapViewContainerTranslate(g, parentOrigin);
        forwardPrimitiveTranslate(g, getDimension());
        g.fillArc(0, 0,
                getDimension().getWidth(), getDimension().getHeight(),
                startAngle,
                arcAngle);
    }
}
