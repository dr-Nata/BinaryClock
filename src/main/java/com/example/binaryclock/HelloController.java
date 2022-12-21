package com.example.binaryclock;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.time.LocalTime;

public class HelloController {
    public Circle m32;
    public Circle m16;
    public Circle m8;
    public Circle m4;
    public Circle m2;
    public Circle m1;
    public Text tm32;
    public Text tm16;
    public Text tm8;
    public Text tm4;
    public Text tm2;
    public Text tm1;
    public Label fullTimeLabel;
    public Rectangle h8;
    public Rectangle h4;
    public Rectangle h2;
    public Rectangle h1;
    public Text th8;
    public Text th4;
    public Text th2;
    public Text th1;
    public Canvas canvasAmPm;

    MinuteCircle[] minuteCircles;
    HourRect[] hourRects;
    LocalTime localTime;
    Timeline tl = new Timeline(new KeyFrame(Duration.seconds(1),
            e-> showAll()));
    private GraphicsContext gr;

    private void showAll() {
        Watch watch = getTimeNow();
        showAmPm(watch);
        showBinaryTime(watch);
        showDigitalTime(getSystemTime(watch));
    }

    private void showAmPm(Watch watch) {
        Color colorPm = Color.YELLOW;
        Color colorAm = Color.GRAY;
        if (watch.getHour()>=12){
            colorPm = Color.GRAY;
            colorAm = Color.YELLOW;
        }
        gr.setFill(colorAm);
        gr.fillOval(20,30,20,20);
        gr.fillText("AM",65, 40);
        gr.setFill(colorPm);
        gr.fillArc(20,90,20,20,-10, 200, ArcType.CHORD);
        gr.fillText("PM",65, 100);
    }

    private Watch getTimeNow() {
        localTime = LocalTime.now();
        int hour = localTime.getHour();
        int minute = localTime.getMinute();
        return new Watch(hour,minute);
    }

    private void showDigitalTime(String systemTime) {
        fullTimeLabel.setText(systemTime);
    }

    private String getSystemTime(Watch watch) {
        int hour = watch.getHour();
        int minute = watch.getMinute();
        String dDot = ":";
        if (minute < 10) dDot = ":0";
        String dHour = "";
        if (hour < 10) dHour = "0";

        return String.format("%s%d%s%d", dHour, hour, dDot, minute);
    }

    private void showBinaryTime(Watch watch) {
        int hour = watch.getHour();
        int minute = watch.getMinute();
        String binHour = Integer.toBinaryString(hour);
        String binMinute = Integer.toBinaryString(minute);
        onRepaint(binHour,binMinute);
    }

    public void initialize(){
        minuteCircles = new MinuteCircle[6];
        minuteCircles[0] = new MinuteCircle(0,false,tm1,m1);
        minuteCircles[1] = new MinuteCircle(1,false,tm2,m2);
        minuteCircles[2] = new MinuteCircle(2,false,tm4,m4);
        minuteCircles[3] = new MinuteCircle(3,false,tm8,m8);
        minuteCircles[4] = new MinuteCircle(4,false,tm16,m16);
        minuteCircles[5] = new MinuteCircle(5,false,tm32,m32);

        hourRects = new HourRect[4];
        hourRects[0] = new HourRect(0,false,th1,h1);
        hourRects[1] = new HourRect(1,false,th2,h2);
        hourRects[2] = new HourRect(2,false,th4,h4);
        hourRects[3] = new HourRect(3,false,th8,h8);

        gr = canvasAmPm.getGraphicsContext2D();
    }

    public void onCircleClick(MouseEvent mouseEvent) {
        if(!Mode.clockIsOn) {
            Circle source = (Circle) mouseEvent.getSource();
            String id = source.getId();
            int intID = Integer.parseInt(id.substring(1));
            int index = (int) (Math.log(intID) / Math.log(2));
            setMinuteCircles(index, true, Color.YELLOW);

            showTime();
        }
    }

    private void showTime() {
        int minutes = 0;
        for (int i = 0; i < 6; i++) {
            if (minuteCircles[i].isSwitchOn){
                minutes += minuteCircles[i].minute;
            }
        }
        int hours = 0;
        for (int i = 0; i < 4; i++) {
            if (hourRects[i].isSwitchOn){
                hours += hourRects[i].minute;
            }
        }
        String dDot = ":";
        if (minutes < 10) dDot = ":0";
        String dHour = "";
        if (hours < 10) dHour = "0";
        fullTimeLabel.setText(String.format("%s%d%s%d", dHour, hours, dDot, minutes));

    }

    public void onRepaint(String binHour, String binMinute) {
        binHour = toNumber(binHour,4);
        binMinute = toNumber(binMinute,6);

        for (int i = 0; i < 6; i++) {
            char ch = binMinute.charAt(5 - i);
            if (ch == '0')
                setMinuteCircles(i,false,Color.DARKGRAY);
            else
                setMinuteCircles(i,true,Color.YELLOW);

        }
        for (int i = 0; i < 4; i++) {
            char ch = binHour.charAt(3 - i);
            if (ch == '0')
                setHourRects(i, false, Color.DARKGRAY);
            else
                setHourRects(i,true,Color.YELLOW);

        }
        fullTimeLabel.setText("00:00");
    }

    private String toNumber(String binNumber, int lenNumber) {

        while (binNumber.length() < lenNumber){
            binNumber = "0" + binNumber;
        }

        return binNumber;
    }

    private void setHourRects(int i, boolean switchOn, Color color) {
        if(Mode.clockIsOn) {
            hourRects[i].isSwitchOn = switchOn;
        } else {
            hourRects[i].isSwitchOn = !hourRects[i].isSwitchOn;
            if (hourRects[i].isSwitchOn)
                color = Color.YELLOW;
            else
                color = Color.DARKGRAY;
        }
        hourRects[i].shape.setFill(color);
        hourRects[i].text.setFill(color);
    }

    private void setMinuteCircles(int i, boolean switchOn, Color color) {
        if(Mode.clockIsOn) {
            minuteCircles[i].isSwitchOn = switchOn;
        } else {
            minuteCircles[i].isSwitchOn = !minuteCircles[i].isSwitchOn;
            if (minuteCircles[i].isSwitchOn)
                color = Color.YELLOW;
            else
                color = Color.DARKGRAY;
        }
        minuteCircles[i].shape.setFill(color);
        minuteCircles[i].text.setFill(color);
    }

    public void onRectClick(MouseEvent mouseEvent) {
        if(!Mode.clockIsOn) {
            Rectangle source = (Rectangle) mouseEvent.getSource();
            // source.setFill(Color.YELLOW);
            String id = source.getId();
            int intID = Integer.parseInt(id.substring(1));
            int index = (int) (Math.log(intID) / Math.log(2));
            setHourRects(index, true, Color.YELLOW);

            showTime();
        }
    }

    public void onClickTimeMode(ActionEvent actionEvent) {
        RadioButton source = (RadioButton) actionEvent.getSource();
        if(source.getId().equals("clockMode")){
            Mode.clockIsOn = true;
            turnOnClock();

        }else{
            Mode.clockIsOn = false;
            tl.stop();
        }
    }

    private void turnOnClock() {
        onRepaint("0000","000000");
        showSystemTime();
    }

    private void showSystemTime(){
        tl.setCycleCount(Timeline.INDEFINITE);
        tl.play();

        //TODO ShowSystemTime to label

    }

    public void onRestart(ActionEvent actionEvent) {
        Mode.clockIsOn = true;
        onRepaint("0000","000000");
        Mode.clockIsOn = false;
    }
    //TODO refactoring
}

class Watch{
  public int hour;
  public int minute;

    public Watch(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public String getStringHour() {
        if (hour<10) return "0"+hour;
        return "" + hour;
    }
}