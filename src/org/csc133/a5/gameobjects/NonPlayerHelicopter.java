package org.csc133.a5.gameobjects;

import com.codename1.ui.geom.Dimension;
import org.csc133.a5.interfaces.HelicopterStrategy;
import org.csc133.a5.interfaces.Steerable;

// TODO: Implement Strategy Pattern
//  two flight strategies flight path towards fire avoid strategy

public class NonPlayerHelicopter implements Steerable {
    private HelicopterStrategy helicopterStrategy;

    public void avoid() {

    }

    public NonPlayerHelicopter(Dimension worldSize) {
    }

    @Override
    public void steerLeft() {

    }

    @Override
    public void steerRight() {

    }
}
