package org.csc133.a5.views;

import com.codename1.ui.Container;
import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;
import org.csc133.a5.GameWorld;
import org.csc133.a5.gameobjects.GameObject;

public class MapView extends Container {
    GameWorld gw;
    float zoomFloat = 1.0f;

    public MapView(GameWorld gw) {
        this.gw = gw;
    }

    @Override
    public void laidOut() {
        this.gw.setDimension(new Dimension(this.getWidth(), this.getHeight()));
        this.gw.init();
    }

    private Point2D transformPoint2D(Transform t, Point2D p) {
        float[] in = new float[2];
        float[] out = new float[2];
        in[0] = (float) p.getX();
        in[1] = (float) p.getY();
        t.transformPoint(in, out);
        return new Point2D(out[0], out[1]);
    }

    private Transform getInverseVTM() {
        Transform inverseVTM = Transform.makeIdentity();

        try {
            getVTM().getInverse(inverseVTM);
        } catch (Transform.NotInvertibleException e) {
            e.printStackTrace();
        }

        return inverseVTM;
    }

    public void setZoomFloat(float zoomFloat) {
        this.zoomFloat = zoomFloat;
    }

    public void zoomInOut() {
        if (zoomFloat == 1.5f) {
            setZoomFloat(1.0f);
        } else if (zoomFloat == 1.0f) {
            setZoomFloat(1.5f);
        }
    }

    private Transform getVTM() {
        Transform worldToND, ndToDisplay, theVTM;
        float winLeft, winRight, winTop, winBottom;

        winLeft = winBottom = 0;
        winRight = this.getWidth() / zoomFloat;
        winTop = this.getHeight() / zoomFloat;

        // Compensation
        float compensateRight = this.getWidth() - winRight;
        float compensateTop = this.getHeight() - winTop;

        // Adjustment
        winLeft += compensateRight;
        winBottom += compensateTop;

        float winHeight = winTop - winBottom;
        float winWidth = winRight - winLeft;

        // * Start
        worldToND = buildWorldToNDTransform(winWidth, winHeight, winLeft,
                winBottom);
        ndToDisplay = buildNDToDisplayTransform(this.getWidth(),
                this.getHeight());
        theVTM = ndToDisplay.copy();
        theVTM.concatenate(worldToND);
        return theVTM;
    }

    @Override
    public void pointerPressed(int x, int y) {
        x = x - getAbsoluteX();
        y = y - getAbsoluteY();

        Point2D sp = transformPoint2D(getInverseVTM(), new Point2D(x, y));

        this.gw.selectFire(sp);

    }

    // set up the world to ND transform
    private Transform buildWorldToNDTransform(float winWidth, float winHeight,
                                              float winLeft, float winBottom) {
        Transform tmpXform = Transform.makeIdentity();
        tmpXform.scale((1 / winWidth), (1 / winHeight));
        tmpXform.translate(-winLeft, -winBottom);
        return tmpXform;
    }

    // set up the Normalized Device (ND) to Screen transform
    // lower-left, to upper left hand corner
    private Transform buildNDToDisplayTransform(float displayWidth,
                                                float displayHeight) {
        Transform tmpXform = Transform.makeIdentity();
        tmpXform.translate(0, displayHeight);
        // * Flip happens
        tmpXform.scale(displayWidth, -displayHeight);
        return tmpXform;
    }

    private void setupViewingTransformationMatrix(Graphics g) {
        // * VTM ready to use
        Transform gXform = Transform.makeIdentity();
        g.getTransform(gXform);
        gXform.translate(getAbsoluteX(), getAbsoluteY());
        gXform.concatenate(getVTM());
        gXform.translate(-getAbsoluteX(), -getAbsoluteY());

        g.setTransform(gXform);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Step 1: start VTM
        setupViewingTransformationMatrix(g);

        Point originRelativeToParent = new Point(getX(), getY());
        Point originRelativeToScreen = new Point(getAbsoluteX(),
                getAbsoluteY());

        // Step 2: Draw Objects
        for (GameObject go : gw.getGameObjectCollection()) {
            go.draw(g, originRelativeToParent,
                    originRelativeToScreen);
        }

        // Step 3: Reset Affine Transform
        g.resetAffine();
    }

    public void updateLocalTransforms() {
        for (GameObject go : gw.getGameObjectCollection()) {
            go.updateLocalTransforms();
        }
    }
}
