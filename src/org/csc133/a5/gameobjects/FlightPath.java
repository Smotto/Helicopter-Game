package org.csc133.a5.gameobjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;
import org.csc133.a5.interfaces.HelicopterStrategy;

import java.util.ArrayList;

// !! Compression Context
public class FlightPath extends GameObject {
    BezierCurve bezierCurve;
    ArrayList<Point2D> controlPointsList;
    HelicopterStrategy strategy;

    public FlightPath(Dimension worldSize, River river, Helipad helipad) {
        this.worldSize = worldSize;

        controlPointsList = new ArrayList<>();
        controlPointsList.add(
                new Point2D(helipad.getTranslation().getTranslateX(),
                        helipad.getTranslation().getTranslateY()
                                + helipad.getDimension().getHeight()));
        controlPointsList.add(
                new Point2D(river.getTranslation().getTranslateX(),
                        river.getTranslation().getTranslateY() +
                                river.getDimension().getHeight()));

        bezierCurve = new BezierCurve(controlPointsList);
    }

    public void addFireControlPoint(Fire fire) {
        controlPointsList.add(new Point2D(fire.getTranslation().getTranslateX(),
                fire.getTranslation().getTranslateY()));
    }


    // * Nested Class * //
    class BezierCurve extends GameObject {
        ArrayList<Point2D> controlPointsList;

        public BezierCurve(ArrayList<Point2D> controlPointsList) {
            this.controlPointsList = controlPointsList;
        }

        private void drawBezierCurve(Graphics g,
                                     ArrayList<Point2D> controlPoints) {
            final double smallFloatIncrement = 0.01;

            g.setColor(ColorUtil.GRAY);
            for (Point2D p : controlPoints) {
                g.fillArc((int) p.getX() - 15, (int) p.getY() - 15, 30, 30, 0
                        , 360);
            }

            g.setColor(ColorUtil.BLUE);

            Point2D currentPoint = controlPoints.get(0);
            Point2D nextPoint;

            double t = 0;
            while (t < 1) {
                int d = controlPoints.size() - 1;
                nextPoint = new Point2D(0, 0);
                for (int i = 0; i < controlPoints.size(); i++) {
                    // Bernstein polynomial
                    nextPoint.setX(nextPoint.getX() +
                            bernsteinD(d, i, t) * controlPoints.get(i).getX());
                    nextPoint.setY(nextPoint.getY() +
                            bernsteinD(d, i, t) * controlPoints.get(i).getY());

                }

                g.drawLine((int) currentPoint.getX(), (int) currentPoint.getY(),
                        (int) nextPoint.getX(), (int) nextPoint.getY());

                currentPoint = nextPoint;
                t = t + smallFloatIncrement;
            }
            nextPoint = controlPoints.get(controlPoints.size() - 1);
            g.drawLine((int) currentPoint.getX(), (int) currentPoint.getY(),
                    (int) nextPoint.getX(), (int) nextPoint.getY());

        }

        private double bernsteinD(int d, int i, double t) {

            return choose(d, i) * Math.pow(t, i) * Math.pow(1 - t, d - i);
        }

        private double choose(int n, int k) {
            // base case
            //
            if (k <= 0 || k >= n) {
                return 1;
            }
            // recurse using pascal's triangle
            return choose(n - 1, k - 1) + choose(n - 1, k);
        }

        @Override
        public void localDraw(Graphics g, Point originParent,
                              Point originScreen) {
            drawBezierCurve(g, controlPointsList);
        }
    }

    @Override
    protected void localDraw(Graphics g, Point parentOrigin,
                             Point screenOrigin) {
        // Three Bezier Curves
        // First curve helipad -> river
        // NPH (no player helicopter)
        bezierCurve.localDraw(g, parentOrigin, screenOrigin);
    }
}
