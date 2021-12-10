package org.csc133.a5.gameobjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point2D;
import org.csc133.a5.interfaces.HelicopterStrategy;
import org.csc133.a5.interfaces.Steerable;

// !! Compression Context
public class NonPlayerHelicopter extends Helicopter {
    private HelicopterStrategy strategy;
    private FlightPath flightPath;
    private double t = 0;

    public void setCompressionStrategy(HelicopterStrategy strategy) {
        this.strategy = strategy;
    }

    private class FlightStrategy implements HelicopterStrategy {
        Helicopter helicopter;

        FlightStrategy(Helicopter helicopter){
            this.helicopter = helicopter;
        }

        @Override
        public void updateLocation() {
            double angle = (Math.toRadians(getHeading()));
            Point2D c = new Point2D(getTranslation().getTranslateX(),
                    getTranslation().getTranslateY());
            Point2D p = flightPath.evaluateCurve(t);
            translate(getSpeed() * Math.cos(angle),
                    getSpeed() * -Math.sin(angle));
            double tx = p.getX() - c.getX();
            double ty = p.getY() - c.getY();

            // ?? I dont get 360 minus degrees ?? //
            double theta = Math.toDegrees(Math.atan2(ty, tx));

            helicopter.translate(tx, ty);

            if (t <= 1) {
                t = t + getSpeed() * 0.001;
                rotate(getHeading() - theta);
                setHeading((int) theta);
            } else {
                t = 0;
            }

        }
    }

    private class AvoidStrategy implements HelicopterStrategy {
        Helicopter helicopter;

        AvoidStrategy(Helicopter helicopter){
            this.helicopter = helicopter;
        }

        @Override
        public void updateLocation() {

        }

    }

    public NonPlayerHelicopter(Dimension worldSize) {
        super(worldSize, ColorUtil.GREEN);
        this.startOrStopEngine();
        this.setSpeed(5);
        setCompressionStrategy(new FlightStrategy(this));
        translate(worldSize.getWidth() / 2.0,
                getDimension().getHeight() / 12.0f);

    }

    @Override
    public void move(int elapsedTimeInMillis) {
        if (elapsedTimeInMillis > 10) {
            strategy.updateLocation();
        }
    }

    public void setPath(FlightPath flightPath) {
        this.flightPath = flightPath;
    }
}
