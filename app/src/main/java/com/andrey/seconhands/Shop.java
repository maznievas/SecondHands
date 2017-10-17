package com.andrey.seconhands;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by T530 on 10.07.2017.
 */

public class Shop {

    String name;
    String address;
    String updateDay;
    String city;
    int id;
    ShopMarker shopMarker;
    LatLng ll;

    public Shop()
    {}

    public Shop(String name, String city,  String address, String updateDay)
    {
      //  this.id = id;
        this.name = name;
        this.address = address;
        this.updateDay = updateDay;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getUpdateDay() {
        return updateDay;
    }

    public int getID() {
        return id;
    }

    public void setID(int ID) {
        id = ID;
    }

    public String getCity() { return city; }

    public String getFullAddress()
    {
        return city + "," + address;
    }

    public void setLL(LatLng ll)
    {
        this.ll = ll;
    }

    public LatLng getLL()
    {
        return ll;
    }

    public ShopMarker getShopMarker()
    {
        return new ShopMarker(ll, getName(), getUpdateDay() + " " + getAddress());
    }
}
