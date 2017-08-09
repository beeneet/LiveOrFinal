package com.tashambra.mobileapp;

public class Drink {
    protected double AlcoholPercent;
    protected String name;
    protected double volume;

    public Drink(){
        this.name = "Simple Drink";
        this.AlcoholPercent = 7;
        this.volume = 12;

    }

    public Drink(String name){
        this.name = name;
    }

    public Drink(String name, double AlcoholPercent){
        this.name = name;
        this.AlcoholPercent = AlcoholPercent;
    }

    public Drink(String name, double AlcoholPercent, double Volume){
        this.name = name;
        this.AlcoholPercent = AlcoholPercent;
        this.volume = Volume;
    }



    public double getAlcoholPercent() {
        return AlcoholPercent;
    }

    public void setAlcoholPercent(double alcoholPercent) {
        AlcoholPercent = alcoholPercent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

}
