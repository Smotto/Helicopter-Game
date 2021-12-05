package org.csc133.a5.gameobjects;

import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;

public class Fires extends GameObjectCollection<Fire> {

    public Fires() {
        super();
    }

    public void removeFiresWithZeroSize() {
        for (Fire fire : this) {
            if (fire.getDimension().getWidth() == 0 ||
                    fire.getDimension().getHeight() == 0) {
                fire.stop();
                this.remove(fire);
            }
            fire.grow();

        }
    }

    @Override
    protected void localDraw(Graphics g, Point containerOrigin,
                             Point screenOrigin) {
        for (Fire fire : this) {
            fire.draw(g, containerOrigin, screenOrigin);
        }
    }
}
