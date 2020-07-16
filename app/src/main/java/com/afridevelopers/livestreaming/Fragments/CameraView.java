package com.afridevelopers.livestreaming.Fragments;

import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import cn.nodemedia.NodePlayerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afridevelopers.livestreaming.LifecycleEventListener;
import com.afridevelopers.livestreaming.R;
import com.afridevelopers.livestreaming.RCTNodePlayerView;
import com.pedro.encoder.input.video.CameraHelper;
import com.pedro.rtplibrary.rtmp.RtmpCamera1;

import net.ossrs.rtmp.ConnectCheckerRtmp;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class CameraView extends Fragment implements ConnectCheckerRtmp, SurfaceHolder.Callback {

    private final String TAG = "CameraView";
    private RtmpCamera1 rtmpCamera1;
    private RCTNodePlayerView rctNodePlayerView;
    private String currentDateAndTime = "";
    private EditText streamUrl;
    private Button btn_streamPlay;
    private File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/rtmp-rtsp-stream-client-java");

    public CameraView() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_camera_view, container, false);
        SurfaceView surfaceView = rootView.findViewById(R.id.surfaceView);
        NodePlayerView nodePlayerView = rootView.findViewById(R.id.playerView);
        streamUrl = rootView.findViewById(R.id.edittext_streamplay_url);
        btn_streamPlay = rootView.findViewById(R.id.btn_play_stream);
        rtmpCamera1 = new RtmpCamera1(surfaceView, this);
        rtmpCamera1.setReTries(10);


        rctNodePlayerView = new RCTNodePlayerView(getActivity(), new LifecycleEventListener() {
            @Override
            public void codeBack(int code, String msg) {
                Log.d("rctNodePlayerView: ", code + " : " + msg);
            }
        },nodePlayerView);

        btn_streamPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "click on btn", Toast.LENGTH_SHORT).show();
                if(rctNodePlayerView.isPlaying()){
                    rctNodePlayerView.stop();
                    btn_streamPlay.setText("Play Stream");
                } else {
                    rctNodePlayerView.play("http://pullstream.peeglelive.com/live/LIVEROOM414221594800260.flv");
                    btn_streamPlay.setText("Stop Stream");
//                    AudioManager am = (AudioManager) getContext().getSystemService(getContext().AUDIO_SERVICE);
//                    am.requestAudioFocus(null, AudioManager.STREAM_VOICE_CALL, AudioManager.AUDIOFOCUS_GAIN);
//                    am.setMode(AudioManager.MODE_IN_CALL);
                }
            }
        });

        surfaceView.getHolder().addCallback(this);

        return rootView;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG,"surfaceCreated");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        rtmpCamera1.startPreview();
        Bundle bundle = this.getArguments();
        int rotation = CameraHelper.getCameraOrientation(getContext());

        if(bundle != null){
           String url = bundle.getString("url");

           if(url != null){
               if(!rtmpCamera1.isRecording()){
                   Log.d(TAG,"surfaceChanged 1");
                   try {
                       if (!folder.exists()) {
                           folder.mkdir();
                       }
                       SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                       currentDateAndTime = sdf.format(new Date());
                       if (!rtmpCamera1.isStreaming()) {
                           if (rtmpCamera1.prepareAudio() && rtmpCamera1.prepareVideo()) {
                               rtmpCamera1.startRecord(
                                       folder.getAbsolutePath() + "/" + currentDateAndTime + ".mp4");
                               if(rtmpCamera1.isRecording()
                                       || rtmpCamera1.prepareAudio(64 * 240, 32000, true, true, true) && rtmpCamera1.prepareVideo(256,144, 24, 1000*240, true, rotation)){
                                   rtmpCamera1.startStream(url);
                                   Log.d(TAG,"startStream "+url);
                               }
                           } else {
                               Toast.makeText(getActivity(), "Error preparing stream, This device cant do it",
                                       Toast.LENGTH_SHORT).show();
                           }
                       } else {
                           rtmpCamera1.startRecord(
                                   folder.getAbsolutePath() + "/" + currentDateAndTime + ".mp4");
                           Toast.makeText(getActivity(), "Recording... ", Toast.LENGTH_SHORT).show();
                       }
                   } catch (IOException e) {
                       rtmpCamera1.stopRecord();
                       Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                   }
               }
           }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG,"surfaceDestroyed");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 && rtmpCamera1.isRecording()) {
            rtmpCamera1.stopRecord();
        }
        if (rtmpCamera1.isStreaming()) {
            rtmpCamera1.stopStream();
        }
        rtmpCamera1.stopPreview();
    }


    @Override
    public void onConnectionSuccessRtmp() {
        Log.d(TAG,"onConnectionSuccessRtmp");
    }

    @Override
    public void onConnectionFailedRtmp(@NonNull String reason) {
        Log.e(TAG,"onConnectionFailedRtmp  "+ reason);
    }

    @Override
    public void onNewBitrateRtmp(long bitrate) {
        Log.i(TAG,"onNewBitrateRtmp"+bitrate);
    }

    @Override
    public void onDisconnectRtmp() {
        Log.d(TAG,"onDisconnectRtmp");
    }

    @Override
    public void onAuthErrorRtmp() {
        Log.d(TAG,"onAuthErrorRtmp");
    }

    @Override
    public void onAuthSuccessRtmp() {
        Log.d(TAG,"onAuthSuccessRtmp");
    }


}
