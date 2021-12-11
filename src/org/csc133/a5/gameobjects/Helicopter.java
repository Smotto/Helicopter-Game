package org.csc133.a5.gameobjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Font;
import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import org.csc133.a5.GameWorld;
import org.csc133.a5.gameobjects.parts.Arc;
import org.csc133.a5.gameobjects.parts.Rectangle;
import org.csc133.a5.interfaces.Steerable;

import java.util.ArrayList;

public class Helicopter extends Moveable implements Steerable {

    // * Constant Variables * //
    private final int SPEED_MULTIPLIER = 2;
    private final int MAX_SPIN_SPEED = 30;
    final static int BUBBLE_RADIUS = 20;
    final static int ENGINE_BLOCK_WIDTH = (int) (BUBBLE_RADIUS * 1.8);
    final static int ENGINE_BLOCK_HEIGHT = ENGINE_BLOCK_WIDTH / 3;
    final static int TAIL_HEIGHT = ENGINE_BLOCK_HEIGHT * 5;
    final static int TAIl_WIDTH = ENGINE_BLOCK_WIDTH / 6;
    final static int BLADE_WIDTH = 5;
    final static int BLADE_LENGTH = BUBBLE_RADIUS * 6;
    final static int LEG_WIDTH = 5;
    final static int LEG_LENGTH = BUBBLE_RADIUS * 3;
    final static int TAIL_SPINNER_HEIGHT = ENGINE_BLOCK_HEIGHT / 5;
    final static int TAIL_SPINNER_WIDTH = ENGINE_BLOCK_WIDTH / 2;

    private int fuel = 25000;
    private int water = 0;
    private int spinSpeed = 0;

    private ArrayList<GameObject> heloParts;
    private HeloBlade heloBlade;
    private HeloTailSpinner heloTailSpinner;

    // * Default Constructor * //
    private Helicopter() {
    }

    public Helicopter(Dimension worldSize, int color) {
        // * Rotation
        setHeading(-90);

        this.worldSize = worldSize;
        setColorInteger(color);
        setDimension(new Dimension(
                worldSize.getWidth() / 80,
                worldSize.getWidth() / 80));

        // * State
        heloState = new Off();

        heloParts = new ArrayList<>();

        heloParts.add(new HeloBubble(getColorInteger()));
        heloParts.add(new HeloEngineBlock());
        heloParts.add(new HeloLeftLeg());
        heloParts.add(new HeloRightLeg());
        heloParts.add(new HeloTail());
        heloTailSpinner = new HeloTailSpinner();
        heloParts.add(heloTailSpinner);
        heloBlade = new HeloBlade();
        heloParts.add(heloBlade);
        heloParts.add(new HeloBladeShaft());
    }

    // * Helicopter Parts * //
    private static class HeloBubble extends Arc {
        public HeloBubble(int color) {
            super(color,
                    2 * Helicopter.BUBBLE_RADIUS,
                    2 * Helicopter.BUBBLE_RADIUS,
                    0, Helicopter.BUBBLE_RADIUS * 0.80f,
                    1, 1,
                    0,
                    0, 360);
            this.setDimension(new Dimension(
                    2 * Helicopter.BUBBLE_RADIUS,
                    2 * Helicopter.BUBBLE_RADIUS));
        }
    }

    private static class HeloEngineBlock extends Rectangle {
        public HeloEngineBlock() {
            super(ColorUtil.rgb(187, 222, 251),
                    Helicopter.ENGINE_BLOCK_WIDTH,
                    Helicopter.ENGINE_BLOCK_HEIGHT,
                    0, -Helicopter.ENGINE_BLOCK_HEIGHT / 2f,
                    1, 1,
                    0
            );
        }

        @Override
        public void localDraw(Graphics g, Point parentOrigin,
                              Point screenOrigin) {
            g.setColor(getColorInteger());
            mapViewContainerTranslate(g, parentOrigin);
            forwardPrimitiveTranslate(g, getDimension());
            g.fillRect(0, 0,
                    getDimension().getWidth(),
                    getDimension().getHeight());
        }
    }

    private static class HeloBlade extends Rectangle {
        public HeloBlade() {
            super(ColorUtil.GRAY,
                    BLADE_LENGTH,
                    BLADE_WIDTH,
                    0, -ENGINE_BLOCK_HEIGHT / 2f,
                    1, 1,
                    0);
        }
    }

    private static class HeloBladeShaft extends Arc {
        public HeloBladeShaft() {
            super(ColorUtil.BLACK,
                    2 * Helicopter.BLADE_WIDTH / 3,
                    2 * Helicopter.BLADE_WIDTH / 3,
                    0, -Helicopter.ENGINE_BLOCK_HEIGHT / 2f,
                    1, 1,
                    0,
                    0, 360);
        }
    }

    private static class HeloLeftLeg extends Rectangle {
        public HeloLeftLeg() {
            super(ColorUtil.rgb(245, 245, 245),
                    LEG_LENGTH,
                    LEG_WIDTH,
                    -ENGINE_BLOCK_WIDTH / 2f - LEG_WIDTH * 2,
                    -BUBBLE_RADIUS / 2f,
                    1, 1,
                    90);
        }
    }

    private static class HeloRightLeg extends Rectangle {
        public HeloRightLeg() {
            super(ColorUtil.rgb(245, 245, 245),
                    LEG_LENGTH,
                    LEG_WIDTH,
                    ENGINE_BLOCK_WIDTH / 2f + LEG_WIDTH * 2,
                    -BUBBLE_RADIUS / 2f,
                    1, 1,
                    90);
        }
    }

    private static class HeloTail extends Rectangle {
        public HeloTail() {
            super(ColorUtil.rgb(0, 188, 212),
                    TAIl_WIDTH, TAIL_HEIGHT,
                    0,
                    -ENGINE_BLOCK_HEIGHT * 5 / 2f - ENGINE_BLOCK_HEIGHT,
                    1, 1, 0);

        }

        @Override
        public void localDraw(Graphics g, Point parentOrigin,
                              Point screenOrigin) {
            g.setColor(getColorInteger());
            // Shifting the origin
            mapViewContainerTranslate(g, parentOrigin);
            forwardPrimitiveTranslate(g, getDimension());
            // Drawing at origin
            g.fillRect(0, 0,
                    getDimension().getWidth(), getDimension().getHeight());

            g.drawLine(0,
                    ENGINE_BLOCK_HEIGHT / 2,
                    -ENGINE_BLOCK_WIDTH / 6, TAIL_HEIGHT);
            g.drawLine(ENGINE_BLOCK_WIDTH / 6,
                    ENGINE_BLOCK_HEIGHT / 2,
                    ENGINE_BLOCK_WIDTH / 3, TAIL_HEIGHT);
        }
    }

    private static class HeloTailSpinner extends Rectangle {
        public HeloTailSpinner() {
            super(ColorUtil.rgb(227, 242, 253),
                    TAIL_SPINNER_WIDTH, TAIL_SPINNER_HEIGHT,
                    0, -TAIL_HEIGHT - TAIL_SPINNER_WIDTH / 2f,
                    1, 1,
                    0);
        }
    }

    // * Helicopter State * //
    private HeloState heloState;

    private abstract class HeloState {
        protected Helicopter getHelo() {
            return Helicopter.this;
        }

        public abstract boolean isEngineOn();

        public abstract boolean isMoving();

        public void startOrStopEngine() {
        }

        public void accelerate() {
        }

        public void decelerate() {
        }

        public void updateLocalTransforms() {
        }

        public void drinkWater(River river) {
        }

        public void dumpWater(Fires fires) {
        }

        public void checkEndGamePosition(Fires fires, Helipad helipad,
                                         GameWorld gameWorld) {

        }

    }

    // * 4 Helicopter States * //
    private class Off extends HeloState {
        @Override
        public boolean isEngineOn() {
            return false;
        }

        @Override
        public boolean isMoving() {
            return false;
        }

        // Engine can start.
        @Override
        public void startOrStopEngine() {
            getHelo().setState(new Starting());
        }

        @Override
        public void checkEndGamePosition(Fires fires, Helipad helipad,
                                         GameWorld gameWorld) {
            if (fires.getSize() == 0) {
                Transform hCopterLocation = getTranslation();
                Transform hPadLocation = helipad.getTranslation();

                double hCopterX = hCopterLocation.getTranslateX();
                double hCopterY = hCopterLocation.getTranslateY();

                double hPadX =
                        hPadLocation.getTranslateX();
                double hPadY =
                        hPadLocation.getTranslateY();

                double x = hCopterX - hPadX;
                double y = hCopterY - hPadY;

                int distance = (int) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));

                if (distance < helipad.getDimension().getWidth() / 2
                        && getSpeed() == 0) {
                    gameWorld.gameOverWin();
                }
            }
        }
    }

    private class Starting extends HeloState {
        // Can transition to stopping at any time

        @Override
        public void updateLocalTransforms() {
            super.updateLocalTransforms();

            // Fuel goes down
            setFuel(Math.subtractExact(fuel,
                    (int) Math.sqrt(getSpeed()) + 5));

            // Blade goes up to speed
            heloBlade.rotate(++spinSpeed);
            heloTailSpinner.rotate(++spinSpeed *
                    SPEED_MULTIPLIER);

            if (spinSpeed == MAX_SPIN_SPEED) {
                getHelo().setState(new Ready());
            }
        }

        @Override
        public boolean isEngineOn() {
            return true;
        }

        @Override
        public boolean isMoving() {
            return false;
        }

        @Override
        public void startOrStopEngine() {
            getHelo().setState(new Stopping());
        }
    }

    private class Stopping extends HeloState {
        @Override
        public void updateLocalTransforms() {
            super.updateLocalTransforms();
            heloBlade.rotate(--spinSpeed);
            heloTailSpinner.rotate(--spinSpeed *
                    SPEED_MULTIPLIER);
            // Blade must stop before entering off state
            if (spinSpeed == 0) {
                getHelo().setState(new Off());
            }
        }

        @Override
        public boolean isEngineOn() {
            return false;
        }

        @Override
        public boolean isMoving() {
            return false;
        }

        // Goes into starting state at anytime
        @Override
        public void startOrStopEngine() {
            getHelo().setState(new Starting());
        }
    }

    private class Ready extends HeloState {
        // Drink
        public void drinkWater(River river) {
            // Step 1: Get Locations
            Transform helicopterTransform = getTranslation();
            Transform riverTransform = river.getTranslation();

            // Step 2: Check boundaries
            if (getSpeed() < 3 &&
                    (helicopterTransform.getTranslateY() -
                            river.getDimension().getHeight() / 2f) <=
                            riverTransform.getTranslateY() &&
                    (helicopterTransform.getTranslateY() -
                            river.getDimension().getHeight() / 2f) >=
                            riverTransform.getTranslateY() -
                                    river.getDimension().getHeight()) {

                // Step 3: Drink
                int water = getWater();
                if (water < 1000) {
                    setWater(water += 100);
                }
            }
        }

        // Fight
        public void dumpWater(Fires fires) {
            for (Fire fire : fires) {
                Transform hCopTransform = getTranslation();

                double xminusx = ((hCopTransform.getTranslateX()) -
                        (fire.getTranslation().getTranslateX()));
                double yminusy =
                        ((hCopTransform.getTranslateY()) -
                                (fire.getTranslation().getTranslateY()));

                // Measure distance from center of fire
                double distance =
                        Math.sqrt(Math.pow(xminusx, 2)
                                + Math.pow(yminusy, 2));

                if (distance <= fire.getDimension().getWidth() / 2.0) {
                    fire.shrink(getWater());
                    break;
                }
            }
            setWater(0);
        }

        @Override
        public void updateLocalTransforms() {
            super.updateLocalTransforms();
            heloBlade.rotate(spinSpeed);
            heloTailSpinner.rotate(spinSpeed *
                    SPEED_MULTIPLIER);

            setFuel(Math.subtractExact(fuel,
                    (int) Math.sqrt(getSpeed()) + 5));
        }

        @Override
        public boolean isEngineOn() {
            return true;
        }

        @Override
        public boolean isMoving() {
            return getSpeed() > 0;
        }

        @Override
        public void startOrStopEngine() {
            // conditions go here to test for whether or not we can stop
            // the engine
            if (getSpeed() == 0) {
                getHelo().setState(new Stopping());
            }
        }

        public void accelerate() {
            increaseSpeed();
        }

        public void decelerate() {
            decreaseSpeed();
        }
    }

    private void setState(HeloState heloState) {
        this.heloState = heloState;
    }

    // * Methods Based on State * //
    public void startOrStopEngine() {
        heloState.startOrStopEngine();
    }

    public boolean isEngineOn() {
        return heloState.isEngineOn();
    }

    public boolean isMoving() {
        return heloState.isMoving();
    }

    public void checkEndGamePosition(Fires fires, Helipad helipad,
                                     GameWorld gameWorld) {
        heloState.checkEndGamePosition(fires, helipad, gameWorld);
    }

    public void accelerate() {
        heloState.accelerate();
    }

    public void decelerate() {
        heloState.decelerate();
    }

    public void updateLocalTransforms() {
        heloState.updateLocalTransforms();
    }

    // * Helicopter Functionality * //
    public void increaseSpeed() {
        int speed = getSpeed();
        if (speed < 10) {
            setSpeed(++speed);
        }
    }

    public void decreaseSpeed() {
        int speed = getSpeed();
        if (speed > 0) {
            setSpeed(--speed);
        }
    }

    public int getFuel() {
        return this.fuel;
    }

    public void setFuel(int fuel) {
        this.fuel = fuel;
    }

    public int getWater() {
        return this.water;
    }

    public void setWater(int water) {
        this.water = water;
    }

    public void dumpWater(Fires fires) {
        heloState.dumpWater(fires);
    }

    public void drinkWater(River river) {
        heloState.drinkWater(river);
    }

    public void updateFuel(GameWorld gameWorld) {
        // End of game
        if (fuel <= 0) {
            gameWorld.gameOverLoss();
        }
        if (gameWorld.isEngineOn()) {
            setFuel(Math.subtractExact(fuel,
                    (int) Math.sqrt(getSpeed()) + 5));
        }

    }

    private void updateLocation() {
        double angle = (Math.toRadians(getHeading()));

        translate(getSpeed() * Math.cos(angle),
                getSpeed() * -Math.sin(angle));
    }

    @Override
    public void move(int elapsedTimeInMillis) {
        if (elapsedTimeInMillis > 10) {
            updateLocation();
        }
    }

    @Override
    public void steerLeft() {
        rotate(15);
        setHeading(getHeading() - 15);
    }

    @Override
    public void steerRight() {
        rotate(-15);
        setHeading(getHeading() + 15);
    }

    @Override
    public void localDraw(Graphics g, Point parentOrigin,
                          Point screenOrigin) {
        for (GameObject go : heloParts) {
            go.draw(g, parentOrigin, screenOrigin);
        }

        // !! This is for Fuel and Water Display !! //
        flipContainerTranslate(g, screenOrigin, getDimension());
        Font mediumBoldSystemFont = Font.createSystemFont(Font.FACE_SYSTEM,
                Font.STYLE_BOLD,
                Font.SIZE_MEDIUM);
        g.setFont(mediumBoldSystemFont);
        g.setColor(ColorUtil.rgb(178, 255, 89));
        g.drawString("F : " + this.fuel, 0,
                220);
        g.drawString("W: " + this.water, 0,
                250);
    }
}
