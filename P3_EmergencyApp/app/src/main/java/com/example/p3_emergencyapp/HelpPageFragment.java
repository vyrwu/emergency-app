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
 * Placeholder for HELP screen. - Olek
 */

public class HelpPageFragment extends Fragment {

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
                        WiFiP2pServiceManager serviceManager = WiFiP2pServiceManager.getInstance();

                        serviceManager.broadcastServiceCall();
                        return true;
            }
        });
        buttonContainer.addView(button);
        return rootView;
    }
}
