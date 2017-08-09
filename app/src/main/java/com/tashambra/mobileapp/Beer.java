package com.tashambra.mobileapp;

public class Beer extends Drink {

    public Beer(){
        this.name = "Regular Beer";
        this.AlcoholPercent = 5;
    }

    public Beer(String beerName){
        this.name = beerName;
    }

    public Beer(String beerName, double alcoholPercent){
        this.name = beerName;
        this.AlcoholPercent = alcoholPercent;
    }

    public Beer(String beerName, double alcoholPercent, double Volume){
        this.name = beerName;
        this.AlcoholPercent = alcoholPercent;
        this.volume = Volume;
    }
}
