package org.csc133.a5;

import com.codename1.components.SpanLabel;
import com.codename1.ui.Button;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BoxLayout;
import org.csc133.a5.gameobjects.*;

import java.util.concurrent.CopyOnWriteArrayList;

import static com.codename1.ui.CN.exitApplication;

public class GameWorld {
    private int FIRE_SIZE = 2;
    private Dimension worldSize;
    private River river;
    private Helipad helipad;
    private Helicopter helicopter;
    private NonPlayerHelicopter nonPlayerHelicopter;
    private Buildings buildings;
    private Fires fires;
    private FireDispatch fireDispatch;
    private CopyOnWriteArrayList<GameObject> gameObjects;
    private FlightPath flightPath;
    private long lastTime = 0;
    private long newTime = 0;
    private int ticks = 0;

    private void createBuildings() {
        Building buildingOne;
        Building buildingTwo;
        Building buildingThree;

        buildingOne = new Building(worldSize, 500);
        buildingOne.setDimension(new Dimension(
                worldSize.getWidth() / 8,
                worldSize.getHeight() / 2 - 100));
        buildingOne.translate(
                worldSize.getWidth() / 20.0
                        + buildingOne.getDimension().getWidth() / 2f,
                worldSize.getHeight() / 2.0
                        - buildingOne.getDimension().getHeight() / 2f);

        buildingTwo = new Building(worldSize, 400);
        buildingTwo.setDimension(new Dimension(
                worldSize.getWidth() / 8,
                worldSize.getHeight() / 2 - 200));
        buildingTwo.translate(
                worldSize.getWidth()
                        - (worldSize.getWidth() / 20.0) -
                        worldSize.getWidth() / 8.0
                        + buildingTwo.getDimension().getWidth() / 2f,
                worldSize.getHeight() / 2.0
                        - buildingTwo.getDimension().getHeight() / 2f);

        buildingThree = new Building(worldSize, 1000);
        buildingThree.setDimension(new Dimension(
                worldSize.getWidth() - 2 * (worldSize.getWidth() / 7),
                worldSize.getHeight() / 11));
        buildingThree.translate(
                worldSize.getWidth() / 7.0
                        + buildingThree.getDimension().getWidth() / 2f,
                worldSize.getHeight() - 50 -
                        buildingThree.getDimension().getHeight() / 2f);

        addBuildings(buildingOne, buildingTwo, buildingThree);
    }

    private void addBuildings(Building buildingOne, Building buildingTwo,
                              Building buildingThree) {
        buildings.add(buildingOne);
        buildings.add(buildingTwo);
        buildings.add(buildingThree);
    }

    private void addFiresToBuildings() {
        for (Building building : buildings) {
            for (int i = 0; i < FIRE_SIZE; i++) {
                Fire fireTemp = new Fire(worldSize);
                building.setFireInBuilding(fireTemp);
                fires.add(fireTemp);
            }
        }
    }

    private void attachFiresToObserver(Fires fires) {
        for (Fire fire : fires) {
            fireDispatch.attach(fire);
        }
    }

    // TODO: Do stuff based on cursor position
    public void selectFire(int x, int y, int parentX, int parentY) {
        System.out.println("Cursor Position x: " + x);
        System.out.println("Cursor Position y: " + y);
        for (Fire fire : fires) {
            fire.select(fireDispatch);
        }
    }

    public GameWorld() {
    }

    public void init() {
        river = new River(worldSize);

        helipad = new Helipad(worldSize);
        helicopter = new Helicopter(worldSize);
        nonPlayerHelicopter = new NonPlayerHelicopter(worldSize);

        buildings = new Buildings();
        fires = new Fires();
        fireDispatch = new FireDispatch();
        flightPath = new FlightPath(worldSize, river, helipad);

        createBuildings();
        addFiresToBuildings();
        // TODO: Fix attachFiresToObserver, put it somewhere else?
        attachFiresToObserver(fires);

        gameObjects = new CopyOnWriteArrayList<>();
        gameObjects.add(river);
        gameObjects.add(buildings);
        gameObjects.add(fires);
        gameObjects.add(helipad);
        gameObjects.add(flightPath);
        gameObjects.add(helicopter);

    }

    // Every 100ms this is being called.
    public void tick() {
        ticks++;
        this.helicopter.updateFuel(this);
        this.buildings.spawnRandomFires(ticks,fireDispatch, gameObjects, fires);
        // Move based on elapsed time between the same calls
        newTime = System.currentTimeMillis();
        this.helicopter.move((int) (newTime - lastTime));
        lastTime = System.currentTimeMillis();

        this.fires.removeFiresWithZeroSize();

        this.buildings.updateBuildings();

        this.buildings.checkOverallDamage(this);
        this.helicopter.checkEndGamePosition(fires, helipad, this);
    }

    // * Pop-up Windows * //
    public void gameOverWin() {
        Dialog d = new Dialog("Game Over");
        Dialog.setDefaultBlurBackgroundRadius(8);
        d.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        d.add(new SpanLabel("You won!", "Play Again?"));
        d.add(new SpanLabel("Score: " +
                String.valueOf(
                        100 - Integer.parseInt(buildings.getTotalDamage()))));
        Button yes = new Button("I'll stay for one more.");
        Button no = new Button("Get me out of here, please...");
        yes.addActionListener((e) -> new Game().show());
        no.addActionListener((e) -> exitApplication());
        d.add(yes);
        d.add(no);
        d.show();
    }

    public void gameOverLoss() {
        Dialog d = new Dialog("Game Over");
        Dialog.setDefaultBlurBackgroundRadius(8);
        d.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        d.add(new SpanLabel("R.I.P California :^(", "Play Again?"));
        Button yes = new Button("I'll stay for one more.");
        Button no = new Button("Get me out of here, please...");
        yes.addActionListener((e) -> new Game().show());
        no.addActionListener((e) -> exitApplication());
        d.add(yes);
        d.add(no);
        d.show();
    }

    public CopyOnWriteArrayList<GameObject> getGameObjectCollection() {
        return gameObjects;
    }

    // * Player Methods * //
    public void dumpWater() {
        this.helicopter.dumpWater(fires);
    }

    public void drinkWater() {
        this.helicopter.drinkWater(river);
    }

    public void playerTurnRight() {
        this.helicopter.steerRight();
    }

    public void startOrStopEngine() {
        this.helicopter.startOrStopEngine();
    }

    public void playerTurnLeft() {
        this.helicopter.steerLeft();
    }

    public void accelerate() {
        this.helicopter.accelerate();
    }

    public void decelerate() {
        this.helicopter.decelerate();
    }

    public boolean isEngineOn() {
        return this.helicopter.isEngineOn();
    }

    public boolean isMoving() {
        return this.helicopter.isMoving();
    }

    public void quit() {
        Display.getInstance().exitApplication();
    }

    public void setDimension(Dimension worldSize) {
        this.worldSize = worldSize;
    }

    // * Glass Cockpit Methods * //
    public String getTotalDamage() {
        return buildings.getTotalDamage();
    }

    public String getHelicopterSpeed() {
        return String.valueOf(helicopter.getSpeed());
    }

    public String getHelicopterFuel() {
        return String.valueOf(helicopter.getFuel());
    }

    public String getFireCount() {
        return String.valueOf(fires.getSize());
    }

    public String getPlayerHeading() {
        return String.valueOf(Math.abs(helicopter.getDisplayHeading() % 360));
    }

    public String getTotalFireSize() {
        double temp = 0;
        for (Fire fire : fires) {
            temp += fire.getDimension().getWidth();
        }
        return String.valueOf((int) temp);
    }

    public String getTotalLoss() {
        double temp = 0;
        for (Building building : buildings) {
            temp += building.getLoss();
        }

        return String.valueOf((int) temp);
    }
}