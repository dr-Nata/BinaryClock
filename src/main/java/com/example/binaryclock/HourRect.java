package com.example.binaryclock;

import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class HourRect extends Rectangle {
    int index;
    int minute;
    boolean isSwitchOn;
    Text text;
    Rectangle shape;

    public HourRect(int index, boolean isSwitchOn, Text text, Rectangle shape) {
        this.index = index;
        this.minute = (int) Math.pow(2,index);
        this.isSwitchOn = isSwitchOn;
        this.text = text;
        this.shape = shape;
    }
}
