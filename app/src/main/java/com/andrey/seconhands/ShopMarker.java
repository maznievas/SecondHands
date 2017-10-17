package com.andrey.seconhands;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by sts on 09.10.17.
 */

public class ShopMarker implements ClusterItem{

    final LatLng mPosition;
    final String title;
    final String snippet;

    public ShopMarker(LatLng mPosition, String title, String snippet) {
        this.mPosition = mPosition;
        this.title = title;
        this.snippet = snippet;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return snippet;
    }
}
