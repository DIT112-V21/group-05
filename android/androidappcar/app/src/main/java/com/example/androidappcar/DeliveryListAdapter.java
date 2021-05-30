package com.example.androidappcar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;


public class DeliveryListAdapter extends ArrayAdapter<Delivery> {
    private static final String TAG = "DeliveryListAdapter";
    private Context mContext;
    int mResource;

    public DeliveryListAdapter(Context context, int resource,  ArrayList<Delivery> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){


        //Gets Delivery info from the List
        String patient = getItem(position).getPatient();
        String parcel = getItem(position).getParcel();
        String location = getItem(position).getLocation();
        String date = getItem(position).getDate();
        String time = getItem(position).getTime();

        //Creates A delivery Object with the information
        Delivery delivery = new Delivery(patient, parcel,location, date, time);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvPatient = convertView.findViewById(R.id.textView1);
        TextView tvParcel = convertView.findViewById(R.id.textView2);
        TextView tvLocation = convertView.findViewById(R.id.textView3);
        TextView tvDate = convertView.findViewById(R.id.textView4);
        TextView tvTime = convertView.findViewById(R.id.textView5);

        tvPatient.setText(patient);
        tvParcel.setText(parcel);
        tvLocation.setText(location);
        tvDate.setText(date);
        tvTime.setText(time);

        return convertView;
    }
}
