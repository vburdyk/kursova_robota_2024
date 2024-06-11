package com.example.test_appliances;

public class ElectroDevice {
    private int id;
    private String name;
    private double power;
    private boolean turnedOn;

    public ElectroDevice(int id, String name, double power, boolean turnedOn) {
        this.id = id;
        this.name = name;
        this.power = power;
    }

    public ElectroDevice(String name, double power) {
        this.name = name;
        this.power = power;
    }

    public ElectroDevice(String name, double power, boolean turnedOn) {
        this.name = name;
        this.power = power;
        this.turnedOn = turnedOn;
    }

    public String getName() {
        return name;
    }


    public double getPower() {
        return power;
    }

    public boolean isTurnedOn() {
        return turnedOn;
    }

    public void setTurnedOn(boolean turnedOn) {
        this.turnedOn = turnedOn;
    }

    @Override
    public String toString() {
        return  name + " " + power + " " + isTurnedOn();
    }

    public void setPower(double power) {
        this.power = power;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }
}
