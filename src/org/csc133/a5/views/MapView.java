package org.csc133.a5.views;

import com.codename1.ui.Container;
import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import org.csc133.a5.GameWorld;
import org.csc133.a5.gameobjects.GameObject;

public class MapView extends Container {
    GameWorld gw;

    public MapView(GameWorld gw) {
        this.gw = gw;
    }

    @Override
    public void pointerPressed(int x, int y) {
        System.out.println(x);
        System.out.println(y);
        System.out.println(getParent().getAbsoluteX());
        System.out.println(getParent().getAbsoluteY());

        // TODO: use a function (finding distance from fire) to choose the
        //  closest valid distance fire. hint: we already have a function for
        //  that.
        this.gw.selectFire(x, y, getParent().getAbsoluteX(),
                getParent().getAbsoluteY());
        // !! Checks the release of a click
        // !! gives the point location of the cursor
        // !! command triggers a select function
        // !! select function notifies an update to other fires to deselect

        //addPoint(x-getParent().getAbsoluteX(), y-getParent().getAbsoluteY());
    }

    @Override
    public void laidOut() {
        this.gw.setDimension(new Dimension(this.getWidth(), this.getHeight()));
        this.gw.init();
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
        Transform worldToND, ndToDisplay, theVTM;
        float winLeft, winRight, winTop, winBottom;

        winLeft = winBottom = 0;
        winRight = this.getWidth();
        winTop = this.getHeight();

        float winHeight = winTop - winBottom;
        float winWidth = winRight - winLeft;

        // * Start
        worldToND = buildWorldToNDTransform(winWidth, winHeight, winLeft,
                winBottom);
        ndToDisplay =
                buildNDToDisplayTransform(this.getWidth(), this.getHeight());
        theVTM = ndToDisplay.copy();
        theVTM.concatenate(worldToND);

        // * VTM ready to use
        Transform gXform = Transform.makeIdentity();
        g.getTransform(gXform);
        gXform.translate(getAbsoluteX(), getAbsoluteY());
        gXform.concatenate(theVTM);
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
