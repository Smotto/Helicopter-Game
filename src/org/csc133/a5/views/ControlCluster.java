package org.csc133.a5.views;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Container;
import com.codename1.ui.layouts.BorderLayout;
import org.csc133.a5.GameWorld;
import org.csc133.a5.commands.*;

import java.util.ArrayList;

public class ControlCluster extends Container {

    GameWorld gw;
    Container leftSide = new Container(new BorderLayout());
    Container middle = new Container(new BorderLayout());
    Container rightSide = new Container(new BorderLayout());

    Button left = new Button("Left");
    Button right = new Button("Right");
    Button fight = new Button("Fight");

    Button startEngine = new Button("Start Engine");
    Button exit = new Button("Exit");

    Button drink = new Button("Drink");
    Button brake = new Button("Brake");
    Button accel = new Button("Accel");
    Button empty = new Button();

    ArrayList<Button> buttonList = new ArrayList<>();

    public ControlCluster(GameWorld gw) {
        this.gw = gw;
        this.setLayout(new BorderLayout());
        this.getAllStyles().setBgTransparency(255);

        ((BorderLayout) getLayout()).setCenterBehavior(
                BorderLayout.CENTER_BEHAVIOR_CENTER);

        buttonList.add(left);
        buttonList.add(right);
        buttonList.add(fight);
        buttonList.add(startEngine);
        buttonList.add(exit);
        buttonList.add(drink);
        buttonList.add(brake);
        buttonList.add(accel);

        for (Button button : buttonList) {
            button.getAllStyles().setBgTransparency(100);
            button.getAllStyles().setBgColor(ColorUtil.GRAY);
            button.getAllStyles().setFgColor(ColorUtil.BLUE);
        }

        left.setCommand(new TurnLeftCommand(gw));
        right.setCommand(new TurnRightCommand(gw));
        fight.setCommand(new DumpWaterCommand(gw));
        exit.setCommand(new QuitGameCommand(gw));

        startEngine.setCommand(new StartOrStopEngineCommand(gw));
        drink.setCommand(new DrinkWaterCommand(gw));

        brake.setCommand(new DecelerateCommand(gw));
        accel.setCommand(new AccelerateCommand(gw));
        empty.setCommand(new Command("     "));

        leftSide.add(BorderLayout.WEST, left);
        leftSide.add(BorderLayout.CENTER, right);
        leftSide.add(BorderLayout.EAST, fight);

        middle.add(BorderLayout.WEST, startEngine);
        middle.add(BorderLayout.CENTER, exit);

        rightSide.add(BorderLayout.WEST, drink);
        rightSide.add(BorderLayout.CENTER, brake);
        rightSide.add(BorderLayout.EAST, accel);

        // Adding all containers to origin container
        this.add(BorderLayout.WEST, leftSide);
        this.add(BorderLayout.CENTER, middle);
        this.add(BorderLayout.EAST, rightSide);
    }

    public void updateEngineText() {
        if (gw.isMoving()) {
            startEngine.getAllStyles().
                    setBgColor(ColorUtil.rgb(105, 105, 105));
            startEngine.getAllStyles().setFgColor(ColorUtil.GRAY);

        } else {
            startEngine.getAllStyles().setBgColor(ColorUtil.GRAY);
            startEngine.getAllStyles().setFgColor(ColorUtil.BLUE);
        }
        if (gw.isEngineOn()) {
            startEngine.setText("Stop Engine");

        } else {
            startEngine.setText("Start Engine");
        }
    }
}
