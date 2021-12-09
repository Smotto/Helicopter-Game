package org.csc133.a5.gameobjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point2D;
import org.csc133.a5.interfaces.HelicopterStrategy;
import org.csc133.a5.interfaces.Steerable;

// TODO: Implement Strategy Pattern
//  two flight strategies flight path towards fire avoid strategy

public class NonPlayerHelicopter extends Helicopter implements HelicopterStrategy {
    private HelicopterStrategy helicopterStrategy;
    private FlightPath flightPath;
    private double t = 0;

    @Override
    public void avoid() {

    }

    public NonPlayerHelicopter(Dimension worldSize) {
        super(worldSize, ColorUtil.GREEN);
        this.startOrStopEngine();
        setSpeed(2);
    }

    private void updateLocation() {
        double angle = (Math.toRadians(getHeading()));
        Point2D c = new Point2D(getTranslation().getTranslateX(),
                getTranslation().getTranslateY());
        Point2D p = flightPath.evaluateCurve(t);
        translate(getSpeed() * Math.cos(angle),
                getSpeed() * -Math.sin(angle));
        double tx = p.getX() - c.getX();
        double ty = p.getY() - c.getY();

        // ?? I dont get 360 minus degrees ?? //
        double theta = 360 - Math.toDegrees(Math.atan2(ty, tx));

        this.translate(tx, ty);

        if (t <= 1) {
            t = t + getSpeed() * 0.001;
            rotate(getHeading() - theta);
            setHeading((int) theta);
        } else
            t = 0;
    }

    @Override
    public void move(int elapsedTimeInMillis) {
        if (elapsedTimeInMillis > 10) {
            updateLocation();
        }
    }

    @Override
    public void steerLeft() {

    }

    public void setPath(FlightPath flightPath) {
        this.flightPath = flightPath;
    }

    @Override
    public void steerRight() {

    }
}
