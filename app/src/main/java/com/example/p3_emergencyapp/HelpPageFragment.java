package com.example.p3_emergencyapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * Placeholder for HELP screen. - Olek
 */

public class HelpPageFragment extends Fragment {
    public WiFiP2pServiceManager serviceManager = WiFiP2pServiceManager.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_page,
                container, false);
        ViewGroup buttonContainer = (ViewGroup) rootView.findViewById(R.id.buttonsContainer);

        ImageButton button = (ImageButton) getActivity().getLayoutInflater().inflate(R.layout.rectangularbutton_help_layout, buttonContainer, false);

        button.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        view.playSoundEffect(SoundEffectConstants.CLICK);
                        Toast.makeText(getContext(), R.string.HELP_MESSAGE, Toast.LENGTH_LONG).show();

                        FusedLocationProviderClient fusedLocationProviderClient =
                                LocationServices.getFusedLocationProviderClient(getActivity());

                        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    406);
                        }

                        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(
                                new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location == null) {
                                    Log.w("Location", "Location N/A");
                                    serviceManager.broadcastServiceCall(
                                            new ServiceFlyer(
                                                    WiFiP2pServiceManager.getInstance().getThisDeviceId(),
                                                    "_g307p3.call",
                                                    "N/A",
                                                    "N/A",
                                                    WiFiP2pServiceManager.getInstance().getThisDeviceId()));
                                } else {
                                    String longitude = Double.toString(location.getLongitude());
                                    String laptitude = Double.toString(location.getLatitude());
                                    serviceManager.broadcastServiceCall(
                                            new ServiceFlyer(
                                                    WiFiP2pServiceManager.getInstance().getThisDeviceId(),
                                                    "_g307p3.call",
                                                    laptitude,
                                                    longitude,
                                                    WiFiP2pServiceManager.getInstance().getThisDeviceId()));
                                }
                            }
                        });
                        return true;
            }
        });
        buttonContainer.addView(button);
        return rootView;
    }
}
