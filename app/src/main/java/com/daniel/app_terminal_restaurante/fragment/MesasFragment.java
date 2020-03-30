package com.daniel.app_terminal_restaurante.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.daniel.app_terminal_restaurante.R;


public class MesasFragment extends Fragment implements AdapterView.OnItemClickListener {
    private GridView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmente_mesas, container, false);
        listView = (GridView) view.findViewById(R.id.lvMesas);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
