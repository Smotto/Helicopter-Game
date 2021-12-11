package org.csc133.a5.views;

import com.codename1.ui.Container;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.GridLayout;
import org.csc133.a5.GameWorld;

public class GlassCockpit extends Container {
    GameWorld gw;

    Label heading;
    Label fuelCount;
    Label speedCount;
    Label fireCount;
    Label fireSize;
    Label damageCount;
    Label lossCount;

    public GlassCockpit(GameWorld gw) {
        this.gw = gw;
        this.setLayout(new GridLayout(2, 7));
        this.getAllStyles().setBgTransparency(255);
        this.add("HEADING");
        this.add("SPEED");
        this.add("FUEL");
        this.add("FIRES");
        this.add("FIRE SIZE");
        this.add("DAMAGE");
        this.add("LOSS");

        heading = new Label("0");
        speedCount = new Label("0");
        fuelCount = new Label("0");
        fireCount = new Label("0");
        fireSize = new Label("0");
        damageCount = new Label("0");
        lossCount = new Label("0");

        this.add(heading);
        this.add(speedCount);
        this.add(fuelCount);
        this.add(fireCount);
        this.add(fireSize);
        this.add(damageCount);
        this.add(lossCount);
    }

    public void update() {
        heading.setText(gw.getPlayerHeading());
        speedCount.setText(gw.getHelicopterSpeed());
        fuelCount.setText(gw.getHelicopterFuel());
        fireCount.setText(gw.getFireCount());
        fireSize.setText(gw.getTotalFireSize());
        damageCount.setText(gw.getTotalDamage());
        lossCount.setText(gw.getTotalLoss());
    }
}
