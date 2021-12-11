package org.csc133.a5.gameobjects;

import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;

public abstract class Moveable extends GameObject {

    private int speed;
    private int heading; // Compass angle

    // Current speed and heading
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setHeading(int heading) {
        this.heading = heading;
    }

    public int getHeading() {
        return this.heading;
    }

    public int getSpeed() {
        return this.speed;
    }

    public int getDisplayHeading() {
        return this.heading + 90;
    }

    public abstract void move(int elapsedTimeInMillis);

    @Override
    protected void localDraw(
            Graphics g, Point containerOrigin,
            Point screenOrigin) {
    }
}
