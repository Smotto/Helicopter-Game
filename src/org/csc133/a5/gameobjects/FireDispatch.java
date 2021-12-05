package org.csc133.a5.gameobjects;

import org.csc133.a5.interfaces.Observer;
import org.csc133.a5.interfaces.Subject;

import java.util.ArrayList;
import java.util.List;

// * This is Fire Publisher * //
public class FireDispatch implements Subject {
    private List<Observer> fireObserverList = new ArrayList<>();

    @Override
    public void attach(Observer o) {
        fireObserverList.add(o);
    }

    @Override
    public void detach(Observer o) {
        fireObserverList.remove(o);
    }

    @Override
    public void notifyUpdate(Fire f) {
        for (Observer o : fireObserverList) {
            o.update(f);
        }
    }

}