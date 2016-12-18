package com.bleadingsmile.utildemo;

/**
 * Created by Larry Hsiao on 2016/12/18.
 */

public abstract class DemoItem {

    @Override
    public String toString() {
        return "Demo item";
    }

    public abstract void triggerDemo();
}
