package org.csc133.a5.gameobjects;

public abstract class Fixed extends GameObject {
    public final void translate(double tx, double ty) {
        getTranslation().translate((float) tx, (float) ty);
    }
}
