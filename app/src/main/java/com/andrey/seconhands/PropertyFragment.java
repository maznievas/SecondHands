package com.andrey.seconhands;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sts on 27.09.17.
 */

public class PropertyFragment extends Fragment implements MapsFragment.MyListener, DBPresenter.MainView, View.OnClickListener {

    MapsFragment mapFragment;
    DBPresenter dbPresenter;
    final String TAG = "mLog";
    Spinner spinnerCity, spinnerName, spinnerUpdateDay;
    Button confirmBtn;

    public PropertyFragment(){}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbPresenter = new DBPresenter(this.getContext(), mapFragment, this);
        //dbPresenter.insertData(); //uncomment if you want to update data
        dbPresenter.showData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_properties, container, false);

        spinnerCity = (Spinner) view.findViewById(R.id.spinnerCity);
        dbPresenter.setCitiesSpinner();

        spinnerName = (Spinner) view.findViewById(R.id.spinnerName);
        dbPresenter.setNamesSpinner();

        spinnerUpdateDay = (Spinner) view.findViewById(R.id.spinnerUpdateDay);
        setUpdateDays();

        confirmBtn = (Button) view.findViewById(R.id.buttonConfirm);
        confirmBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onReady() {
        Log.e(TAG, "map has been created");
        //dbPresenter.downloadMap(); //////// uncomment for properly work
        dbPresenter.downloadMapOneByOne(); // comment it
    }

    @Override
    public void setCitiesSpinner(List<String> cities) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, cities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(adapter);
    }

    @Override
    public void setNamesSpinner(List<String> names) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerName.setAdapter(adapter);
    }

    public void setMapFragment(MapsFragment map)
    {
        mapFragment = map;
    }

    public void setUpdateDays()
    {
        List<String> days = new ArrayList<String>();
        days.add("All");
        days.add("Monday");
        days.add("Tuesday");
        days.add("Wednesday");
        days.add("Thursday");
        days.add("Friday");
        days.add("Saturday");
        days.add("Sunday");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, days);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUpdateDay.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.buttonConfirm)
        {
            dbPresenter.confirmProperties(
                    spinnerCity.getSelectedItem().toString(),
                    spinnerName.getSelectedItem().toString(),
                    spinnerUpdateDay.getSelectedItem().toString());
            ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);
            viewPager.setCurrentItem(1);
        }
    }
}
