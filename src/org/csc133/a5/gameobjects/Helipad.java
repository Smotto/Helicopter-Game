package org.csc133.a5.gameobjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;

//`````````````````````````````````````````````````````````````````````````````
public class Helipad extends Fixed {

    public Helipad(Dimension worldSize) {
        this.worldSize = worldSize;
        setColorInteger(ColorUtil.GRAY);
        setDimension(new Dimension(worldSize.getWidth() / 15,
                worldSize.getWidth() / 15));
        translate(worldSize.getWidth() / 2.0,
                getDimension().getHeight());
    }

    @Override
    public void localDraw(Graphics g, Point containerOrigin,
                          Point screenOrigin) {
        g.setColor(getColorInteger());

        mapViewContainerTranslate(g, containerOrigin);
        forwardPrimitiveTranslate(g, getDimension());

        g.drawRect(0,
                0,
                getDimension().getWidth(), getDimension().getHeight(),
                5);
        g.drawArc(15,
                15,
                getDimension().getWidth() - 30,
                getDimension().getHeight() - 30,
                0,
                360);
    }
}
