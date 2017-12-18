package jcubed.wakemewhen;

/**
 * Alarm class
 */

public class Alarm {
    private double id;
    private String name;
    private Double[] location = new Double[2];
    private String address = null;
    private int distance;
    private int time;
    private String media = "someDefaultMediaPath";
    private boolean timeFlag = false;
    private boolean vibrateFlag = true;
    private boolean isActive = false;

    Alarm(String n, Double[] loc, int d, int t, String m, boolean tFlag, boolean vFlag){
        name = n;
        location = loc;
        address = "lat = " + loc[0].toString() + "\nlong = " + loc[1].toString();
        distance = d;
        time = t;
        media = m;
        timeFlag = tFlag;
        vibrateFlag = vFlag;
    }

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double[] getLocation() {
        return location;
    }

    public void setLocation(Double[] location) {
        this.location = location;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public boolean isTimeFlag() {
        return timeFlag;
    }

    public void setTimeFlag(boolean timeFlag) {
        this.timeFlag = timeFlag;
    }

    public boolean isVibrateFlag() {
        return vibrateFlag;
    }

    public void setVibrateFlag(boolean vibrateFlag) {
        this.vibrateFlag = vibrateFlag;
    }
}