package org.csc133.a5.commands;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import org.csc133.a5.GameWorld;

public class StartOrStopEngineCommand extends Command {
    private final GameWorld gameWorld;

    public StartOrStopEngineCommand(GameWorld gameWorld) {
        super("Start Engine");
        this.gameWorld = gameWorld;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        super.actionPerformed(evt);
        gameWorld.startOrStopEngine();
    }
}

