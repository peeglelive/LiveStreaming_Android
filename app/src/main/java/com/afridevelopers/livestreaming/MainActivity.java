package com.afridevelopers.livestreaming;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pedro.vlc.VlcListener;
import com.pedro.vlc.VlcVideoLibrary;

import org.videolan.libvlc.MediaPlayer;
import com.afridevelopers.livestreaming.Fragments.CameraView;
import com.afridevelopers.livestreaming.Fragments.StreamDetailFragment;

import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR2;
import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static com.afridevelopers.livestreaming.Fragments.StreamDetailFragment.FRAGMENT_TAG;

public class MainActivity extends AppCompatActivity {
    public static final int CONTENT_VIEW_ID = 10101010;

    public final String[] PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);

        addFragment(new StreamDetailFragment(this,this));
        /* FrameLayout frame = new FrameLayout(this);
        frame.setId(CONTENT_VIEW_ID);
        setContentView(frame, new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        if (savedInstanceState == null) {
            Fragment streamDetailFragment = new StreamDetailFragment(this,this);
            Fragment cameraView = new CameraView();
            //FragmentTransaction ft = getFragmentManager().beginTransaction();
            //int commit = ft.add(CONTENT_VIEW_ID, streamDetailFragment).commit();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(CONTENT_VIEW_ID, streamDetailFragment, FRAGMENT_TAG)
                    .disallowAddToBackStack()
                    .commit();
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } */
        if (!hasPermissions(this, PERMISSIONS)) {
            this.requestPermissions(PERMISSIONS);
        }
    }

    public void requestPermissions(String... PERMISSIONS){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.requestPermissions( PERMISSIONS, 1);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void showPermissionsErrorAndRequest() {
        Toast.makeText(this, "You need permissions before", Toast.LENGTH_SHORT).show();
        ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
    }

    private void showMinSdkError(int minSdk) {
        String named;
        switch (minSdk) {
            case JELLY_BEAN_MR2:
                named = "JELLY_BEAN_MR2";
                break;
            case LOLLIPOP:
                named = "LOLLIPOP";
                break;
            default:
                named = "JELLY_BEAN";
                break;
        }
        Toast.makeText(this, "You need min Android " + named + " (API " + minSdk + " )",
                Toast.LENGTH_SHORT).show();
    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (this.checkSelfPermission(permission)
                        != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }



    public void addFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainCamera, fragment);
        transaction.commit();

    }


}
