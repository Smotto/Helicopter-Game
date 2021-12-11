package org.csc133.a5.commands;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import org.csc133.a5.GameWorld;

public class DrinkWaterCommand extends Command {
    private GameWorld gameWorld;

    public DrinkWaterCommand(GameWorld gameWorld) {
        super("Drink");
        this.gameWorld = gameWorld;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        super.actionPerformed(evt);
        gameWorld.drinkWater();
    }


}
