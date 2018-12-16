package com.example.nqnguyen.androboum;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

class MyArrayAdapter extends ArrayAdapter<Profil> {

    List<Profil> liste;

    private MyArrayAdapter(Context context, int resource, List<Profil> liste) {
        super(context, resource, liste);
        this.liste = liste;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tv = new TextView(getContext());
        tv.setText(liste.get(position).getEmail());
        return tv;
    }

    @Override
    public int getCount() {
        return liste.size();
    }

}