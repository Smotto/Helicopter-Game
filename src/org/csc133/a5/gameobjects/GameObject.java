package org.csc133.a5.gameobjects;

import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import org.csc133.a5.interfaces.Drawable;

public abstract class GameObject implements Drawable {

    private Transform translation, rotation, scale;
    private Transform gOrigXform;
    private int color;
    private Dimension dimension;
    Dimension worldSize;

    public GameObject() {
        translation = Transform.makeIdentity();
        rotation = Transform.makeIdentity();
        scale = Transform.makeIdentity();
    }

    protected int getColorInteger() {
        return this.color;
    }

    protected void setColorInteger(int color) {
        this.color = color;
    }

    public Dimension getDimension() {
        return this.dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public String toString() {
        return this.getClass().getSimpleName();
    }

    // * Transform Related * //
    protected void translate(double tx, double ty) {
        translation.translate((float) tx, (float) ty);
    }

    protected void rotate(double degrees) {
        rotation.rotate((float) Math.toRadians(degrees), 0, 0);
    }

    protected void scale(double sx, double sy) {
        scale.scale((float) sx, (float) sy);
    }

    public Transform getTranslation() {
        return this.translation;
    }

    protected void flipContainerTranslate(Graphics g,
                                          Point screenOrigin,
                                          Dimension parentDimension) {
        // Obtain the current transform from g
        //
        Transform gXform = Transform.makeIdentity();
        g.getTransform(gXform);

        // move the drawing coordinates back
        //
        gXform.translate(screenOrigin.getX(), screenOrigin.getY());

        // apply display mapping
        //
        gXform.translate(0,
                parentDimension.getHeight()); // move everything back up
        gXform.scale(1, -1); // upper left to lower left

        // move the drawing coordinates as part of
        // the “local origin” transformations
        // absolute screen coordinates
        gXform.translate(-screenOrigin.getX(), -screenOrigin.getY());

        // set the current transform of the graphics context
        //
        g.setTransform(gXform);
    }

    protected void mapViewContainerTranslate(Graphics g, Point parentOrigin) {
        Transform gTransformMatrix = Transform.makeIdentity();
        g.getTransform(gTransformMatrix);
        gTransformMatrix.translate(parentOrigin.getX(), parentOrigin.getY());
        g.setTransform(gTransformMatrix);
    }

    protected void forwardPrimitiveTranslate(Graphics g,
                                             Dimension parentDimension) {
        Transform gTransformMatrix = Transform.makeIdentity();
        g.getTransform(gTransformMatrix);
        gTransformMatrix.translate(-parentDimension.getWidth() / 2f,
                -parentDimension.getHeight() / 2f);
        g.setTransform(gTransformMatrix);
    }

    // * Local Transform * //
    protected Transform preLTTransform(Graphics g, Point originScreen) {
        Transform gXform = Transform.makeIdentity();
        // get the current transform and save it
        g.getTransform(gXform);
        gOrigXform = gXform.copy();

        // move the drawing coordinates back
        gXform.translate(originScreen.getX(), originScreen.getY());

        return gXform;
    }

    protected void localTransforms(Transform gXform) {
        // append Objects's LTs to the graphics object's transform
        gXform.translate(translation.getTranslateX(),
                translation.getTranslateY());
        gXform.concatenate(rotation);
        gXform.scale(scale.getScaleX(), scale.getScaleY());
    }

    protected void postLTTransform(Graphics g, Point originScreen,
                                   Transform gXform) {
        // move the drawing coordinates so that the local origin coincides
        // with the screen origin
        gXform.translate(-originScreen.getX(), -originScreen.getY());
        g.setTransform(gXform);
    }

    protected void restoreOriginalTransforms(Graphics g) {
        // restore the original xform
        g.setTransform(gOrigXform);
    }

    public void updateLocalTransforms() {
    }

    abstract protected void localDraw(Graphics g, Point parentOrigin,
                                      Point screenOrigin);

    public void drawMyString(Graphics g, String str, int x, int y) {
        g.drawString(str, x, y);
    }

    public void draw(Graphics g, Point originParent, Point originScreen) {
        Transform gXform = preLTTransform(g, originScreen);
        localTransforms(gXform);
        postLTTransform(g, originScreen, gXform);

        localDraw(g, originParent, originScreen);

        restoreOriginalTransforms(g);
    }

}
