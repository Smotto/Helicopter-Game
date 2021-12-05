package org.csc133.a5.gameobjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import org.csc133.a5.interfaces.Observer;

import java.util.Random;

public class Fire extends Fixed implements Observer {
    private final int MAX_SIZE = 500;

    private int counter = 0;
    private FireState fireState;

    public Fire(Dimension worldSize) {
        fireState = new UnStarted();
        this.worldSize = worldSize;
        setColorInteger(ColorUtil.MAGENTA);
        Random r = new Random();
        int randomSize = r.nextInt(15) + 1;
        setDimension(new Dimension(randomSize, randomSize));
    }

    private abstract class FireState {
        protected Fire getFire() {
            return Fire.this;
        }

        public void attach(FireDispatch dispatch) {}
        public void detach(FireDispatch dispatch) {}
        public void select(FireDispatch dispatch) {}
        public void deselect(FireDispatch dispatch) {}
        public void start() {}
        public void stop() {}
        public void grow() {}
        public void shrink(int waterAmount) {}

    }

    // * Three Fire States * //
    private class UnStarted extends FireState {
        @Override
        public void start() {
            getFire().setState(new Burning());
        }
    }

    private class Burning extends FireState {

        @Override
        public void attach(FireDispatch dispatch) {
            super.attach(dispatch);
            dispatch.attach(getFire());
        }

        @Override
        public void select(FireDispatch dispatch) {
            // TODO: Fire is selected.
            System.out.println("Fire " + getFire() + " is selected.");
            dispatch.notifyUpdate(getFire());
        }

        @Override
        public void deselect(FireDispatch dispatch) {
            // TODO: Fire is deselected.
            dispatch.notifyUpdate(getFire());
        }

        @Override
        public void stop() {
            getFire().setState(new Extinguished());
        }

        @Override
        public void grow() {
            int sizeChange = 2;
            if (counter % 12 == 0 &&
                    getDimension().getWidth() <= MAX_SIZE &&
                    getDimension().getWidth() != 0) {
                getDimension().setWidth(
                        getDimension().getWidth() + sizeChange);
                getDimension().setHeight(
                        getDimension().getHeight() + sizeChange);
            }
            counter++;
        }

        @Override
        public void shrink(int waterAmount) {
            waterAmount /= 5;
            if (getDimension().getWidth() > 0) {
                if (waterAmount >= getDimension().getWidth()) {
                    getDimension().setWidth(0);
                    getDimension().setHeight(0);
                } else {
                    getDimension().setWidth(getDimension().getWidth() -
                            waterAmount);
                    getDimension().setHeight(getDimension().getHeight() -
                            waterAmount);
                }
            }
        }
    }

    private class Extinguished extends FireState {
        @Override
        public void detach(FireDispatch dispatch) {
            dispatch.detach(getFire());
        }
    }

    // TODO: Fix this, fire needs to be updated to select?
    @Override
    public void update(Fire f) {
        System.out.println("Updating Fire ... " + f);
    }

    private void setState(FireState fireState) {
        this.fireState = fireState;
    }

    // * Methods Based On State * //
    public void start() {
        fireState.start();
    }

    public void stop() {
        fireState.stop();
    }

    public void grow() {
        fireState.grow();
    }

    public void shrink(int waterAmount) {
        fireState.shrink(waterAmount);
    }

    public void select(FireDispatch fireDispatch) {
        fireState.select(fireDispatch);
    }

    public void deselect(FireDispatch fireDispatch) {
        fireState.deselect(fireDispatch);
    }

    @Override
    protected void localDraw(Graphics g, Point containerOrigin,
                             Point screenOrigin) {
        g.setColor(getColorInteger());

        mapViewContainerTranslate(g, containerOrigin);
        forwardPrimitiveTranslate(g, getDimension());

        if (getDimension().getWidth() != 0 ||
                getDimension().getHeight() != 0) {
            g.fillArc(0, 0,
                    getDimension().getWidth(), getDimension().getHeight(),
                    0,
                    360);
            flipContainerTranslate(g, screenOrigin, getDimension());
            g.drawString(Integer.toString(getDimension().getWidth()),
                    getDimension().getWidth(),
                    getDimension().getHeight() + 250);
        }
    }
}
