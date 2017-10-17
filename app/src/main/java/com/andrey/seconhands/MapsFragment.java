package com.andrey.seconhands;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sts on 25.09.17.
 */

public class MapsFragment extends Fragment implements OnMapReadyCallback, DBPresenter.View, LocationListener {

    private ClusterManager<ShopMarker> mClusterManager;

    @Override
    public void onLocationChanged(Location location) {

    }

    interface MyListener {
        void onReady();
    }

    View mView;
    GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private MyListener myListener;

    public void setMyListener(MyListener myListener) {
        this.myListener = myListener;
    }

    public MapsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.activity_maps, container, false);

        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("mLog", "OnMapReady");

        mMap = googleMap;
        myListener.onReady();
    }

    @Override
    public void downloadMap(List<Shop> shops) {

        mMap.clear();
        CameraUpdate update = null;

        LatLng ll1= updateInBackGround(shops);

        update = CameraUpdateFactory.newLatLngZoom(ll1, 10);
        mMap.moveCamera(update);
    }

    @Override
    public void downloadMapOneByOne(Shop shop) {

//        mClusterManager = new ClusterManager<ShopMarker>(this.getContext(), mMap);
//
//        // Point the map's listeners at the listeners implemented by the cluster
//        // manager.
//        mMap.setOnCameraIdleListener(mClusterManager);
//        mMap.setOnMarkerClickListener(mClusterManager);
//
//        // Add cluster items (markers) to the cluster manager.
//        mClusterManager.addItem(shop.getShopMarker());

        mMap.addMarker(new MarkerOptions().position(shop.getLL())
                        .title(shop.getName())
                        .snippet(shop.getUpdateDay() + " "
                                + shop.getAddress())
        );
    }

    @Override
    public void clearMap() {
        mMap.clear();
    }

    @Override
    public void updateCamera(LatLng ll) {
        CameraUpdate update = null;
        update = CameraUpdateFactory.newLatLngZoom(ll, 10);
        mMap.moveCamera(update);
    }


    public LatLng updateInBackGround(List<Shop> shops)
    {
        Geocoder coder = new Geocoder(getContext());
        List<Address> address;

        LatLng ll;

        double lat = 0, lng = 0;

        for (int i = 0; i < shops.size(); i++) {
            Log.d("mLog", "marker " + i);
            try {
                coder = new Geocoder(getActivity());
                List<android.location.Address> list = coder.getFromLocationName(shops.get(i).getFullAddress(), 1);

                if (list.size() >= 1) {
                    android.location.Address address1 = list.get(0);
                    lat = address1.getLatitude();
                    lng = address1.getLongitude();
                    // Log.d("mLog", lat + " " + lng);

                    ll = new LatLng(lat, lng);

                    mMap.addMarker(new MarkerOptions().position(ll)
                                    .title(shops.get(i).getName()
//                                            + "  "
//                                    + shops.get(i).getUpdateDay() + "\n"
//                                    + shops.get(i).getAddress())
                                    )
                                    .snippet(shops.get(i).getUpdateDay() + " "
                                            + shops.get(i).getAddress())
                    );

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new LatLng(lat, lng);

    }

}
