package com.andrey.seconhands;

import android.content.Context;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by sts on 25.09.17.
 */

public class DBPresenter {

    private final Context context;
    DBHandler db;
    Shop shop;
    //final int VERSION = 1;
    View view;
    final String TAG = "mLog";
    MainView viewMain;

    public DBPresenter(Context applicationContext, View view, MainView viewMain)
    {
        this.context = applicationContext;
        db = new DBHandler(applicationContext);
        shop = new Shop();
        this.view = view;
        this.viewMain = viewMain;
    }

    public void insertData()
    {
        //if(VERSION != 1)
            db.insertData();//delete table in constructor of DatabaseHandler <--- old information

    }

    public void showData()
    {
        List<Shop> shops = db.getAllShops();

        for(Shop shop1 : shops)
        {
            Log.d("mLog", "id = " + shop1.getID() + " name = " + shop1.getName() + " city and address = " + shop1.getCity()
            + " " + shop1.getAddress() + " update day " + shop1.getUpdateDay());
        }
    }

    public void downloadMap()
    {

        view.downloadMap(db.getAllShops());
    }

    public void downloadMapOneByOne()
    {
        view.clearMap();

        Observable.fromIterable(db.getAllShops())
                .map(new Function<Shop, Shop>() {

                    @Override
                    public Shop apply(@NonNull Shop shop) throws Exception {
                        shop.setLL(getLLForMarker(shop));
                        return shop;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Shop>() {
                    @Override
                    public void accept(Shop shop) throws Exception {
                        view.downloadMapOneByOne(shop);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });

        view.updateCamera(getLLForMarker(db.getShop(1)));
    }

    public LatLng getLLForMarker(Shop shop)
    {
        Geocoder coder;
        List<Address> address;

        LatLng ll = null;

        double lat = 0, lng = 0;

        try {
            coder = new Geocoder(context);
            List<android.location.Address> list = coder.getFromLocationName(shop.getFullAddress(), 1);

            if (list.size() >= 1) {
                android.location.Address address1 = list.get(0);
                lat = address1.getLatitude();
                lng = address1.getLongitude();
                // Log.d("mLog", lat + " " + lng);

                ll = new LatLng(lat, lng);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ll;
    }

    public void setCitiesSpinner()
    {
        viewMain.setCitiesSpinner(db.getAllCities());
    }

    public void setNamesSpinner()
    {
        viewMain.setNamesSpinner(db.getAllNames());
    }

    public void confirmProperties(String city, String name, String updateDay)
    {
        view.clearMap();

        Observable.fromIterable(db.selectWithProperties(city, name, updateDay))
                .map(new Function<Shop, Shop>() {

                    @Override
                    public Shop apply(@NonNull Shop shop) throws Exception {
                        shop.setLL(getLLForMarker(shop));
                        return shop;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Shop>() {
                    @Override
                    public void accept(Shop shop) throws Exception {
                        view.downloadMapOneByOne(shop);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });

        view.updateCamera(getLLForMarker(db.getShop(1)));
        //view.downloadMap(db.selectWithProperties(city, name, updateDay));
    }

//    public List<LatLng> getCoords(List<Shop> shops)
//    {    }


    public interface View
    {
        public void downloadMap(List<Shop> shops);
        void downloadMapOneByOne(Shop shop);
        void clearMap();
        void updateCamera(LatLng ll);
    }

    public interface MainView
    {
        void setCitiesSpinner(List<String> cities);
        void setNamesSpinner(List<String> names);
    }
}
