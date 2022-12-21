package com.example.binaryclock;

import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class MinuteCircle extends Circle {
    int index;
    int minute;
    boolean isSwitchOn;
    Text text;
    Circle shape;

    public MinuteCircle(int index, boolean isSwitchOn, Text text, Circle shape) {
        this.index = index;
        this.minute = (int) Math.pow(2,index);
        this.isSwitchOn = isSwitchOn;
        this.text = text;
        this.shape = shape;
    }
}
