package com.videoplay.homepage;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.videoplay.R;

/**
 * Created by admin on 2017/10/28.
 */

public class HomePageFragment extends Fragment{

    private  View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {
            Log.e("view","view");
            view=inflater.inflate(R.layout.fragment_home_page,null);
        } else {
            Log.e("view1","view");
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        return view;
    }
}
