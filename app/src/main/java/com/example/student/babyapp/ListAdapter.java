package com.example.student.babyapp;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by student on 2016-12-12.
 */

public class ListAdapter extends BaseAdapter {
    String TAG;

    Context context;

    List<Kindergarten> list_kindergarten;

    TextView name_kindergarten;
    TextView location_kindergarten;
    TextView tel_kindergarten;
    TextView num_CCTV;

    LayoutInflater inflator;
    View convertView;

    Fragment kindergartenDetailFragment;
    FragmentTransaction transaction;

    public ListAdapter(Context context, List<Kindergarten> list_test) {
        TAG = this.getClass().getName();

        this.context = context;
        this.list_kindergarten = list_test;

        inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list_kindergarten.size();
    }

    @Override
    public Object getItem(int i) {
        return list_kindergarten.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        Kindergarten kindergarten = list_kindergarten.get(i);
        View view = convertView;
        TextView name_kindergarte;

        if(view == null){
            view = inflator.inflate(R.layout.item_kindergarten, null);
        }
        if(kindergarten != null){
            name_kindergarten = (TextView) view.findViewById(R.id.name_kindergarten);
            location_kindergarten = (TextView) view.findViewById(R.id.location_kindergarten);
            tel_kindergarten = (TextView) view.findViewById(R.id.tel_kindergarten);
            num_CCTV = (TextView) view.findViewById(R.id.num_CCTV);

            name_kindergarten.setText(kindergarten.getName_kindergarten());
            location_kindergarten.setText(kindergarten.getLocation_kindergarten());
            tel_kindergarten.setText(kindergarten.getTel_kindergarten());
            num_CCTV.setText(kindergarten.getNum_CCTV());

            //Toast.makeText(context, "야야"+kindergarten.getName_kindergarten(), Toast.LENGTH_SHORT).show();
        }

        Log.d(TAG, name_kindergarten.getText().toString());
        return view;
    }

}
