package com.example.firebase.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.firebase.R;
import com.example.firebase.model.People;

import java.util.ArrayList;

public class PeopleAdapter extends ArrayAdapter<People> {
    public PeopleAdapter(Context context, ArrayList<People> people) {
        super(context, 0, people);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Grab the person to render
        People person = getItem(position);
        // Check if amn existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.people_adapter, parent, false);
        }
        // Get all the fields from the adapter
        TextView firstName = convertView.findViewById(R.id.peopleFirstName);
        TextView lastName = convertView.findViewById(R.id.peopleLastName);
        TextView address = convertView.findViewById(R.id.peopleAddress);
        TextView city = convertView.findViewById(R.id.peopleCity);
        TextView age = convertView.findViewById(R.id.peopleAge);
        TextView height = convertView.findViewById(R.id.peopleHeight);
        TextView weight = convertView.findViewById(R.id.peopleWeight);
        // Format and set the values in the view
        firstName.setText(person.getFirstName());
        lastName.setText(person.getLastName());
        address.setText(person.getCurrentAddress().getAddress());
        city.setText(person.getCurrentAddress().getCity());
        age.setText(String.format("%s yrs.", person.getAge()));
        height.setText(String.format("%S m.", person.getHeight()));
        weight.setText(String.format("%S kgs.", person.getWeight()));

        return convertView;
    }
}
