package org.csc133.a5;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Form;
import com.codename1.ui.Graphics;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.util.UITimer;
import org.csc133.a5.commands.*;
import org.csc133.a5.views.ControlCluster;
import org.csc133.a5.views.GlassCockpit;
import org.csc133.a5.views.MapView;

// Implementing from form enables keyboard listening
// manages user input and display output
public class Game extends Form implements Runnable {

    org.csc133.a5.GameWorld gw;
    MapView worldView;
    GlassCockpit glassCockpit;
    ControlCluster controlCluster;

    public Game() {
        gw = new org.csc133.a5.GameWorld();
        worldView = new MapView(gw);
        glassCockpit = new GlassCockpit(gw);
        controlCluster = new ControlCluster(gw);

        this.getAllStyles().setBgColor(ColorUtil.BLACK);
        this.setLayout(new BorderLayout());
        this.add(BorderLayout.CENTER, worldView);
        this.add(BorderLayout.SOUTH, controlCluster);
        this.add(BorderLayout.NORTH, glassCockpit);

        // * Player - Keyboard Action Keys * //
        addKeyListener('n', new SpawnNPHCommand(gw));
        addKeyListener('Q', new QuitGameCommand(gw)); // Quit game
        addKeyListener('f', new DumpWaterCommand(gw)); // Dump water
        addKeyListener('d', new DrinkWaterCommand(gw)); // Drink water
        addKeyListener('s', new StartOrStopEngineCommand(gw)); // Engine
        addKeyListener(-93, new TurnLeftCommand(gw)); // Left
        addKeyListener(-94, new TurnRightCommand(gw)); // Right
        addKeyListener(-91, new AccelerateCommand(gw));  // Up
        addKeyListener(-92, new DecelerateCommand(gw)); // Down

        UITimer timer = new UITimer(this);
        timer.schedule(100, true, this);
        this.show();
    }

    // Draws a single component - Also Method overloading
    public void paint(Graphics g) {
        // Invoking Form's paint method, takes care of what form needs.
        super.paint(g);
    }

    @Override
    public void run() {
        // Step 1: Update Local Transforms
        // Example, helicopter blade.
        worldView.updateLocalTransforms();

        // Every 100 milliseconds our GameWorld
        // is given the opportunity to update
        gw.tick();
        glassCockpit.update();
        controlCluster.updateEngineText();

        // Redraw the screen.
        repaint();
    }
}
