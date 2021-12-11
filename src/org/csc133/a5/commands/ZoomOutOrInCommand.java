package org.csc133.a5.commands;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import org.csc133.a5.GameWorld;
import org.csc133.a5.views.MapView;

public class ZoomOutOrInCommand extends Command {
    private MapView mapView;

    public ZoomOutOrInCommand(MapView mapView) {
        super("Zoom +/-");
        this.mapView = mapView;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        super.actionPerformed(evt);
        this.mapView.zoomInOut();
    }
}
