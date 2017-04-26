package jintong.museum2;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import MyView.TopBar;

/**
 * Created by wjc on 2017/2/8.
 */
public class TabFragment extends Fragment {

    private String mTitle="Default";
    public static final String TITTLE="tittle";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_fragment, container, false);


        return view;

    }
}
