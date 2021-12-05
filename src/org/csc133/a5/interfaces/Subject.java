package org.csc133.a5.interfaces;

import org.csc133.a5.gameobjects.Fire;

public interface Subject {
    public void attach(Observer o);

    public void detach(Observer o);

    public void notifyUpdate(Fire f);
}
