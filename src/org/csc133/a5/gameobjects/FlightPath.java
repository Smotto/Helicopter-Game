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

    public FlightPath(Dimension worldSize, River river, Helipad helipad) {
        this.worldSize = worldSize;

        controlPointsList = new ArrayList<>();
        controlPointsList.add(
                new Point2D(helipad.getTranslation().getTranslateX(),
                        helipad.getTranslation().getTranslateY()));
        controlPointsList.add(
                new Point2D(river.getTranslation().getTranslateX(),
                        river.getTranslation().getTranslateY()));
        controlPointsList.add(new Point2D(worldSize.getWidth() / 3.0,
                worldSize.getHeight() / 2.0));

        bezierCurve = new BezierCurve(controlPointsList);
    }

    public void setTail(Point2D lastControlPoint) {
        controlPointsList.set(controlPointsList.size() - 1,
                lastControlPoint);
    }

    Point2D evaluateCurve(double t) {
        return bezierCurve.evaluateCurve(t);
    }

    // * Nested Class * //
    class BezierCurve extends GameObject {
        ArrayList<Point2D> controlPointsList;

        public BezierCurve(ArrayList<Point2D> controlPointsList) {
            this.controlPointsList = controlPointsList;
        }

        private void drawBezierCurve(Graphics g, Point originParent,
                                     ArrayList<Point2D> controlPoints) {
            final double smallFloatIncrement = 0.01;

            g.setColor(ColorUtil.GRAY);
            for (Point2D p : controlPoints) {
                g.fillArc((int) p.getX() - 15 + originParent.getX(),
                        (int) p.getY() - 15 + originParent.getY(), 30,
                        30, 0
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

                g.drawLine((int) currentPoint.getX() + originParent.getX(),
                        (int) currentPoint.getY() + originParent.getY() ,
                        (int) nextPoint.getX() + originParent.getX(),
                        (int) nextPoint.getY() + originParent.getY());

                currentPoint = nextPoint;
                t = t + smallFloatIncrement;
            }
            nextPoint = controlPoints.get(controlPoints.size() - 1);
            g.drawLine((int) currentPoint.getX() + originParent.getX(),
                    (int) currentPoint.getY() + originParent.getY(),
                    (int) nextPoint.getX() + originParent.getX(),
                    (int) nextPoint.getY() + originParent.getY());

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

        Point2D evaluateCurve(double t) {
            Point2D p = new Point2D(0, 0);

            int d = controlPointsList.size() - 1;
            for (int i = 0; i < controlPointsList.size(); i++) {
                // Bernstein polynomial
                p.setX(p.getX() +
                        bernsteinD(d, i, t) * controlPointsList.get(i).getX());
                p.setY(p.getY() +
                        bernsteinD(d, i, t) * controlPointsList.get(i).getY());

            }

            return p;
        }

        @Override
        public void localDraw(Graphics g, Point originParent,
                              Point originScreen) {
            drawBezierCurve(g, originParent, controlPointsList);
        }

    }

    @Override
    protected void localDraw(Graphics g, Point originParent,
                             Point originScreen) {
        // Three Bezier Curves
        // First curve helipad -> river
        // NPH (no player helicopter)
        bezierCurve.localDraw(g, originParent, originScreen);
    }
}
