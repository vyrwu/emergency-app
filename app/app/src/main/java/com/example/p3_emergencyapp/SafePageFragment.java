package com.example.p3_emergencyapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Placeholder for SAFE screen.
 */

public class SafePageFragment extends Fragment {

    public WiFiP2pServiceManager serviceManager = WiFiP2pServiceManager.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_page,
                container, false);
        ViewGroup buttonContainer = (ViewGroup) rootView.findViewById(R.id.buttonsContainer);

        ImageButton button = (ImageButton) getActivity().getLayoutInflater().inflate(R.layout.rectangularbutton_safe_layout, buttonContainer, false);

        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                view.playSoundEffect(SoundEffectConstants.CLICK);
                Toast.makeText(getContext(), R.string.SAFE_MESSAGE, Toast.LENGTH_LONG).show();
                serviceManager.startServiceDiscovery();
                return true;
            }
        });
        buttonContainer.addView(button);
        return rootView;

    }
}
