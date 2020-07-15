package com.afridevelopers.livestreaming;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pedro.vlc.VlcListener;
import com.pedro.vlc.VlcVideoLibrary;

import org.videolan.libvlc.MediaPlayer;

import java.util.Arrays;

import cn.nodemedia.NodePlayerView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class PlayerScreen extends Fragment implements View.OnClickListener {
    Button bStartStop;
    EditText etEndpoint;
    RCTNodePlayerView rctNodePlayerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_player_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NodePlayerView surfaceView = (NodePlayerView) view.findViewById(R.id.surfaceView);


        bStartStop = (Button) view.findViewById(R.id.b_start_stop);
        bStartStop.setOnClickListener(this);
        etEndpoint = (EditText) view.findViewById(R.id.et_endpoint);

        rctNodePlayerView = new RCTNodePlayerView(getActivity(), new LifecycleEventListener() {
            @Override
            public void codeBack(int code, String msg) {
                Log.d("rctNodePlayerView: ", code + " : " + msg);
//                if (code == 1000) {
//                    Toast.makeText(getContext(), "Connecting", Toast.LENGTH_LONG).show();
//                } else if (code == 1000) {
//                    Toast.makeText(getContext(), "Connected", Toast.LENGTH_LONG).show();
//                } else if (code == 1002) {
//                    Toast.makeText(getContext(), "Connection Fail", Toast.LENGTH_LONG).show();
//                } else if (code == 1003) {
//                    Toast.makeText(getContext(), "Connection Reconnect start", Toast.LENGTH_LONG).show();
//                } else if (code == 1004) {
//                    Toast.makeText(getContext(), "Video END", Toast.LENGTH_LONG).show();
//                } else if (code == 1006) {
//                    Toast.makeText(getContext(), "Time out again reconnect", Toast.LENGTH_LONG).show();
//                } else if (code == 1102) {
//                    Toast.makeText(getContext(), "Buffer done", Toast.LENGTH_LONG).show();
//                } else if (code == 1104) {
//                    Toast.makeText(getContext(), "Start Done", Toast.LENGTH_LONG).show();
//                }
            }
        },surfaceView);
        rctNodePlayerView.setBufferTime(200);
        rctNodePlayerView.setMaxBufferTime(400);
        rctNodePlayerView.setScaleMode("ScaleAspectFill");
        rctNodePlayerView.setRenderType("TEXTUREVIEW");
       // surfaceView.addView(rctNodePlayerView);

    }

    public void addLifecycleEventListener() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null && getActivity().getWindow() != null) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity() != null && getActivity().getWindow() != null) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            // Clear the systemUiVisibility flag
            getActivity().getWindow().getDecorView().setSystemUiVisibility(0);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (rctNodePlayerView != null) {
            rctNodePlayerView.onHostDestroy();
        }
    }


    @Nullable
    private ActionBar getSupportActionBar() {
        ActionBar actionBar = null;
        if (getActivity() instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            actionBar = activity.getSupportActionBar();
        }
        return actionBar;
    }


    @Override
    public void onClick(View view) {
        if (!rctNodePlayerView.isPlaying()) {
            rctNodePlayerView.play("http://pullstream.peeglelive.com/live/LIVEROOM414221594801930.flv");
            bStartStop.setText(getString(R.string.stop_player));
        } else {
            rctNodePlayerView.stop();
            bStartStop.setText(getString(R.string.start_player));
        }
    }

}