package org.csc133.a5;

import com.codename1.charts.util.ColorUtil;
import com.codename1.components.SpanLabel;
import com.codename1.ui.Button;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point2D;
import com.codename1.ui.layouts.BoxLayout;
import org.csc133.a5.gameobjects.*;

import java.util.concurrent.CopyOnWriteArrayList;

import static com.codename1.ui.CN.exitApplication;

public class GameWorld {
    private int FIRE_SIZE = 2;
    private Dimension worldSize;
    private River river;
    private Helipad helipad;
    private PlayerHelicopter playerHelicopter;
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

    public void selectFire(Point2D sp) {
        for (Fire fire : fires) {
            if (fire.contains(sp)) {
                fire.select(fireDispatch);
                flightPath.setTail(new Point2D(
                        fire.getTranslation().getTranslateX(),
                        fire.getTranslation().getTranslateY()));
            }
        }

    }

    public GameWorld() {
    }

    public void init() {
        river = new River(worldSize);

        helipad = new Helipad(worldSize);
        playerHelicopter = new PlayerHelicopter(worldSize);
        nonPlayerHelicopter = new NonPlayerHelicopter(worldSize);

        buildings = new Buildings();
        fires = new Fires();
        fireDispatch = new FireDispatch();
        flightPath = new FlightPath(worldSize, river, helipad);
        nonPlayerHelicopter.setPath(flightPath);
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
        gameObjects.add(playerHelicopter);
        gameObjects.add(nonPlayerHelicopter);

    }

    // Every 100ms this is being called.
    public void tick() {
        ticks++;
        this.playerHelicopter.updateFuel(this);
        this.buildings.spawnRandomFires(ticks, fireDispatch, gameObjects,
                fires);
        // Move based on elapsed time between the same calls
        newTime = System.currentTimeMillis();
        this.playerHelicopter.move((int) (newTime - lastTime));
        this.nonPlayerHelicopter.move((int) (newTime - lastTime));
        lastTime = System.currentTimeMillis();
        this.fires.removeFiresWithZeroSize();

        this.buildings.updateBuildings();

        this.buildings.checkOverallDamage(this);
        this.playerHelicopter.checkEndGamePosition(fires, helipad, this);
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
        this.playerHelicopter.dumpWater(fires);
    }

    public void drinkWater() {
        this.playerHelicopter.drinkWater(river);
    }

    public void playerTurnRight() {
        this.playerHelicopter.steerRight();
    }

    public void startOrStopEngine() {
        this.playerHelicopter.startOrStopEngine();
    }

    public void playerTurnLeft() {
        this.playerHelicopter.steerLeft();
    }

    public void accelerate() {
        this.playerHelicopter.accelerate();
    }

    public void decelerate() {
        this.playerHelicopter.decelerate();
    }

    public boolean isEngineOn() {
        return this.playerHelicopter.isEngineOn();
    }

    public boolean isMoving() {
        return this.playerHelicopter.isMoving();
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
        return String.valueOf(playerHelicopter.getSpeed());
    }

    public String getHelicopterFuel() {
        return String.valueOf(playerHelicopter.getFuel());
    }

    public String getFireCount() {
        return String.valueOf(fires.getSize());
    }

    public String getPlayerHeading() {
        return String.valueOf(
                Math.abs(playerHelicopter.getDisplayHeading() % 360));
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