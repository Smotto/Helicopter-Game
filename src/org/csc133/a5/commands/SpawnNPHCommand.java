package org.csc133.a5.commands;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import org.csc133.a5.GameWorld;

public class SpawnNPHCommand extends Command {
    private GameWorld gameWorld;

    public SpawnNPHCommand(GameWorld gameWorld) {
        super("Spawn");
        this.gameWorld = gameWorld;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        super.actionPerformed(evt);
        // TODO: Make a helicopter spawn function

    }


}
