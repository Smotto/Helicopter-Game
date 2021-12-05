package org.csc133.a5.gameobjects;

import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;
import org.csc133.a5.GameWorld;

import java.util.concurrent.CopyOnWriteArrayList;

public class Buildings extends GameObjectCollection<Building> {

    public Buildings() {
        super();
    }

    public void spawnRandomFires(int ticks,
                                 FireDispatch fireDispatch,
                                 CopyOnWriteArrayList<GameObject> gameObjects
            , Fires fires) {
        for (Building building : this) {
            building.spawnRandomFire(ticks, fireDispatch, gameObjects, fires);
        }
    }

    public void updateBuildings() {
        for (Building building : this) {
            building.updateDamage();
        }
    }

    public void checkOverallDamage(GameWorld gameWorld) {
        if (Integer.parseInt(this.getTotalDamage()) >= 100) {
            gameWorld.gameOverLoss();
        }
    }

    public String getTotalDamage() {
        double damage = 0;
        for (Building building : this) {
            damage += building.getDamage();
        }
        return String.valueOf((int) (damage / this.getSize()));
    }

    @Override
    protected void localDraw(Graphics g, Point containerOrigin,
                             Point screenOrigin) {
        for (Building building : this) {
            // Call draw for each method instead of local draw.
            building.draw(g, containerOrigin, screenOrigin);
        }
    }
}