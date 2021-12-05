package org.csc133.a5.commands;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import org.csc133.a5.GameWorld;

public class QuitGameCommand extends Command {
    private GameWorld gameWorld;

    public QuitGameCommand(GameWorld gameWorld) {
        super("Exit");
        this.gameWorld = gameWorld;
    }

    @Override public void actionPerformed(ActionEvent evt) {
        super.actionPerformed(evt);
        gameWorld.quit();
    }


}
