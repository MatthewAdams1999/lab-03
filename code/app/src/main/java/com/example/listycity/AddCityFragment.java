package com.example.listycity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.Serializable;

public class AddCityFragment extends DialogFragment {
    interface AddCityDialogListener {
        void addCity(City city);
        void updateCity(City city, int position);
    }

    public AddCityFragment() {}

    static AddCityFragment newInstance(City city, int position) {
        Bundle args = new Bundle();
        args.putSerializable("city", (Serializable) city);
        args.putInt("position", position);

        AddCityFragment fragment = new AddCityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private AddCityDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddCityDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_city, null);
        EditText editCityName = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);
        City city = getArguments() != null ? (City) getArguments().getSerializable("city") : null;
        int position = getArguments() != null ? getArguments().getInt("position", -1) : -1;

        if (city != null) {
            editCityName.setText(city.getName());
            editProvinceName.setText(city.getProvince());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle(city == null ? "Add City" : "Edit City") // Change the Add/Edit button depending on if a City is selected or not
                .setNegativeButton("Cancel", null)
                .setPositiveButton(city == null ? "Add" : "Save", (dialog, which) -> {
                    String cityName = editCityName.getText().toString();
                    String provinceName = editProvinceName.getText().toString();

                    if (city == null) {
                        listener.addCity(new City(cityName, provinceName));
                    } else {
                        listener.updateCity(new City(cityName, provinceName), position);
                    }
                })
                .create();
    }
}
