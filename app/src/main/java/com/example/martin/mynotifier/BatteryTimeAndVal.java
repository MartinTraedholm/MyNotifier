package com.example.martin.mynotifier;

import java.util.GregorianCalendar;

/**
 * Created by Martin on 24/02/2016.
 */
public class BatteryTimeAndVal
{
    private float batteryLevel;
    private GregorianCalendar timeOfEvent;

    public float getBatteryLevel() {
        return batteryLevel;
    }

    public GregorianCalendar getTimeOfEvent() {
        return timeOfEvent;
    }

    public BatteryTimeAndVal(float batteryLevel, GregorianCalendar timeOfEvent)
    {
        this.batteryLevel = batteryLevel;
        this.timeOfEvent = timeOfEvent;
    }



}
