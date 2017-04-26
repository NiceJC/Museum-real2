package fragment;


import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import MyView.GridImageView;
import adapter.ImageGridViewAdapter;
import jintong.museum2.R;
import util.SysUtils;

/**
 * Created by wjc on 2017/2/14.
 */
public class CommunityFragmentF2 extends Fragment {

    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.community_fragment_2,container,false);

        return view;
    }
}
