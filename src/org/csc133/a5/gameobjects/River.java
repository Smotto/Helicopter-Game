package org.csc133.a5.gameobjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;

public class River extends Fixed {
    final int HEIGHT = 150;

    public River(Dimension worldSize) {
        this.worldSize = worldSize;
        setColorInteger(ColorUtil.BLUE);
        setDimension(new Dimension(worldSize.getWidth(), HEIGHT));
        translate(worldSize.getWidth() / 2.0f,
                worldSize.getHeight() - worldSize.getHeight() / 3.0f);
    }

    @Override
    protected void localDraw(Graphics g, Point containerOrigin,
                             Point screenOrigin) {
        g.setColor(getColorInteger());
        mapViewContainerTranslate(g, containerOrigin);
        forwardPrimitiveTranslate(g, getDimension());
        g.drawRect(0, 0,
                getDimension().getWidth(), getDimension().getHeight(),
                2);
    }
}
