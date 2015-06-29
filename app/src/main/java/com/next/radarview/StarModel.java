package com.next.radarview;

/**
 * Created by NeXT on 15-4-30.
 */
public class StarModel {

    //缩放比例
    public float sizePercent;
    public int xLocation;
    public int yLocation;
    public float alpha;
    public int direction;
    public int speed;
    public float twinkleRate;
    public int currentLocation;

    public StarModel() {
    }

    public StarModel(float sizePercent, int xLocation, int yLocation) {
        this.sizePercent = sizePercent;
        this.xLocation = xLocation;
        this.yLocation = yLocation;
    }

    public float getSizePercent() {
        return sizePercent;
    }

    public void setSizePercent(float sizePercent) {
        this.sizePercent = sizePercent;
    }

    public int getxLocation() {
        return xLocation;
    }

    public void setxLocation(int xLocation) {
        this.xLocation = xLocation;
    }

    public int getyLocation() {
        return yLocation;
    }

    public void setyLocation(int yLocation) {
        this.yLocation = yLocation;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public float getTwinkleRate() {
        return twinkleRate;
    }

    public void setTwinkleRate(float twinkleRate) {
        this.twinkleRate = twinkleRate;
    }

    public int getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(int currentLocation) {
        this.currentLocation = currentLocation;
    }
}
