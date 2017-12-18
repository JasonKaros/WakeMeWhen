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
    private int vibrateFlag = 1;
    private int isActive = 0;

    Alarm(String name, Double[] loc, int d, int vFlag, String addr){
        this.name = name;
        location = loc;
        distance = d;
        vibrateFlag = vFlag;
        address = addr;
    }

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public int isActive() {
        return isActive;
    }

    public void setActive(int active) {
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

    public int isVibrate() {
        return vibrateFlag;
    }

    public void setVibrate(int vibrateFlag) {
        this.vibrateFlag = vibrateFlag;
    }
}