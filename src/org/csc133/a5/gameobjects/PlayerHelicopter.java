package org.csc133.a5.gameobjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.geom.Dimension;

public class PlayerHelicopter extends Helicopter {

    public PlayerHelicopter(Dimension worldSize) {
        super(worldSize, ColorUtil.rgb(66, 165, 245));
        translate(worldSize.getWidth() / 2.0,
                getDimension().getHeight() * 6);
    }
}
