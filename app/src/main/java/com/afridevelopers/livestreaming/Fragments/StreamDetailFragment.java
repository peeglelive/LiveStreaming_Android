package com.afridevelopers.livestreaming.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afridevelopers.livestreaming.BuildConfig;
import com.afridevelopers.livestreaming.MainActivity;
import com.afridevelopers.livestreaming.R;


public class StreamDetailFragment extends Fragment {

    public static final String FRAGMENT_TAG =
            BuildConfig.APPLICATION_ID + ".DEBUG_EXAMPLE_TWO_FRAGMENT_TAG";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context mainActivityContext;
    private MainActivity mainActivity;
    private EditText inputUrl;
    private Button startStreamBtn;

    public StreamDetailFragment(Context context, MainActivity mainActivity) {
        this.mainActivityContext = context;
        this.mainActivity = mainActivity;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_streamdetail, container, false);
        inputUrl = rootView.findViewById(R.id.edittext_inputurl);
        startStreamBtn = rootView.findViewById(R.id.btn_startstream);
        startStreamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mainActivity.hasPermissions(mainActivityContext,mainActivity.PERMISSIONS)){
                    mainActivity.requestPermissions(mainActivity.PERMISSIONS);
                } else{
                    Toast.makeText(mainActivity,"all permissions received",Toast.LENGTH_SHORT).show();
                    Bundle bundle = new Bundle();
                    bundle.putString("url",inputUrl.getText().toString()); // Put anything what you want

                    CameraView camerView = new CameraView();
                    camerView.setArguments(bundle);

                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.mainCamera, camerView)
                            .commit();
                }
            }
        });
        return rootView;
    }
}
