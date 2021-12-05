package org.csc133.a5.commands;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import org.csc133.a5.GameWorld;

public class ZoomOutOrIn extends Command {
    private GameWorld gameWorld;

    public ZoomOutOrIn(GameWorld gameWorld) {
        super("Zoom +/-");
        this.gameWorld = gameWorld;
    }

    @Override public void actionPerformed(ActionEvent evt) {
        super.actionPerformed(evt);
        // TODO: Implement Zoom in/out using transforms
    }
}
