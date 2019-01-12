package com.github.evan.common_library.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Evan on 2017/12/14.
 */
public class BatteryInfo {
    @SerializedName(value = "isAcPowered", alternate = {"AC powered"})
    private boolean isAcPowered;    //充电器充电
    @SerializedName(value = "isUsbPowered", alternate = {"USB powered"})
    private boolean isUsbPowered;   //usb充电
    @SerializedName(value = "isWirelessPowered", alternate = {"Wireless powered"})
    private boolean isWirelessPowered;  //无线充电
    @SerializedName(value = "isPresent", alternate = {"present"})
    private boolean isPresent;
    private int health;     //寿命
    private int level;      //电量
    private int scale;
    private int status;     //状态
    private String technology;  //厂商
    private int temperature;    //温度
    private int voltage;

    public BatteryInfo() {
    }

    public boolean isAcPowered() {
        return isAcPowered;
    }

    public void setAcPowered(boolean acPowered) {
        isAcPowered = acPowered;
    }

    public boolean isUsbPowered() {
        return isUsbPowered;
    }

    public void setUsbPowered(boolean usbPowered) {
        isUsbPowered = usbPowered;
    }

    public boolean isWirelessPowered() {
        return isWirelessPowered;
    }

    public void setWirelessPowered(boolean wirelessPowered) {
        isWirelessPowered = wirelessPowered;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getVoltage() {
        return voltage;
    }

    public void setVoltage(int voltage) {
        this.voltage = voltage;
    }
}
