package org.csc133.a5.gameobjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class Building extends Fixed {

    private final int dollarValue;
    private double damage = 0;
    private Fires buildingFires = new Fires();

    public Building(Dimension worldSize, int dollarValue) {
        this.dollarValue = dollarValue;
        this.worldSize = worldSize;
        setColorInteger(ColorUtil.rgb(255, 0, 0));
    }

    public void spawnRandomFire(int ticks, FireDispatch fireDispatch,
                                CopyOnWriteArrayList<GameObject> gameObjects,
                                Fires fires) {
        // The higher the building damage, the higher probability of fires.
        if (ticks % 100 == 0) {
            int max = 100;
            int min = 1;
            int range = max - min + 1;
            int rand = (int) (Math.random() * range) + min;
            if (this.damage > 40 && rand < this.damage &&
                    buildingFires.getSize() < 3) {
                Fire fire = new Fire(worldSize);
                setFireInBuilding(fire);
                fireDispatch.attach(fire);
                fires.add(fire);
                gameObjects.add(fire);
            }
        }

    }

    // Number of buildings * O((n-1)^2)
    public void updateDamage() {
        // Empty list of damages
        HashSet<Double> intersectionDamages = new HashSet<>();

        // Building Area
        double buildingArea =
                getDimension().getWidth() * getDimension().getHeight();

        double totalAreaDamage = 0;

        for (Fire fire : buildingFires) {
            if (this.damage < 100) {
                totalAreaDamage += Math.PI
                        * Math.pow(fire.getDimension().getWidth() / 2.0, 2);
            }
            if (this.damage > 100) {
                this.damage = 100;
            }
            for (Fire secondFire : buildingFires) {
                if (fire != secondFire) {
                    // Calculate intersection
                    double intersectionArea = area(fire, secondFire);
                    if (intersectionArea > 0.0) {
                        intersectionDamages.add(intersectionArea);
                    }
                }
            }
        }
        Iterator<Double> it = intersectionDamages.iterator();

        // Subtract intersection areas from total fire area
        while (it.hasNext()) {
            totalAreaDamage -= it.next();
        }

        totalAreaDamage = 100 * (totalAreaDamage / buildingArea);

        if (this.damage < totalAreaDamage) {
            this.damage = totalAreaDamage;
        }
    }

    private double area(Fire A, Fire B) {
        double aX = A.getTranslation().getTranslateX();
        double aY = A.getTranslation().getTranslateY();
        double bX = B.getTranslation().getTranslateX();
        double bY = B.getTranslation().getTranslateY();

        double d = Math.hypot(bX - aX, bY - aY);

        double aRadius = A.getDimension().getWidth() / 2f;
        double bRadius = B.getDimension().getWidth() / 2f;

        // Intersection detected
        if (d < aRadius + bRadius) {
            double a = aRadius * aRadius;
            double b = bRadius * bRadius;

            double x = (a - b + d * d) / (2 * d);
            double z = x * x;
            double y = Math.sqrt(a - z);

            // Return intersection area
            return a * Math.asin(y / aRadius) +
                    b * Math.asin(y / bRadius) -
                    y * (x + Math.sqrt(z + b - a));
        }
        return 0;
    }

    public void setFireInBuilding(Fire fire) {
        float x = getTranslation().getTranslateX();
        float y = getTranslation().getTranslateY();

        Random r = new Random();
        double max = x + getDimension().getWidth() / 2f - 15;
        double maxY = y + getDimension().getHeight() / 2f - 15;
        double min = x - getDimension().getWidth() / 2f + 15;
        double minY = y - getDimension().getHeight() / 2f + 15;
        double randomValue =
                r.nextDouble() * (max - min) + min;
        double randomValueY = r.nextDouble() * (maxY - minY) + minY;

        fire.translate(randomValue, randomValueY);
        buildingFires.add(fire);
        fire.start();
    }

    public double getDamage() {
        return this.damage;
    }

    public double getLoss() {
        return (this.damage / 100) * this.dollarValue;
    }

    @Override
    protected void localDraw(
            Graphics g, Point containerOrigin, Point screenOrigin) {
        g.setColor(getColorInteger());

        mapViewContainerTranslate(g, containerOrigin);
        forwardPrimitiveTranslate(g, getDimension());

        g.drawRect(0, 0,
                getDimension().getWidth(), getDimension().getHeight(),
                2);
        flipContainerTranslate(g, screenOrigin, getDimension());
        drawMyString(g, "V: " + Integer.toString(this.dollarValue),
                getDimension().getWidth(), getDimension().getHeight() + 180);
        drawMyString(g, "D: " + Integer.toString(
                        (int) Math.floor(this.damage)) + "%",
                getDimension().getWidth(), getDimension().getHeight() + 210);

    }

}
