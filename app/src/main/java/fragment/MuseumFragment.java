package fragment;


import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import BmobUtils.BmobColt;
import MyView.GridItemDecoration;
import MyView.PullBaseView;
import MyView.PullRecyclerView;
import adapter.CollectionGridAdapter;
import entity.Collection;
import interfaces.OnBmobReturnWithObj;
import jintong.museum2.CollectionActivity;
import jintong.museum2.R;
import jintong.museum2.TypeColtActivity;
import util.ToastUtils;

import static util.ParameterBase.COLT_ID;
import static util.ParameterBase.COLT_TYPE;

/**
 * 宝库页面
 * Created by wjc on 2017/2/9.
 */
public class MuseumFragment extends Fragment implements View.OnClickListener, adapter.BaseAdapter.OnItemClickListener,
        PullBaseView.OnRefreshListener, adapter.BaseAdapter.OnViewClickListener {
    private View view;
    private PullRecyclerView recyclerView;
    private List<Object> datas = new ArrayList<Object>();
    private CollectionGridAdapter adapter;


    private int theType;

    private int currentPage = 0;
    private ImageView typeBronze; //青铜器
    private ImageView typeChina;//瓷器
    private ImageView typeJade;//玉石器
    private ImageView typePaint;//书画
    private ImageView typeLacquer;//漆器
    private ImageView typeOthers;//其他


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.museum_fragment, container, false);


        initViews();
        initDatas();

        setData();
        setEvents();

        return view;

    }

    private void setEvents() {
        typeBronze.setOnClickListener(this);
        typeChina.setOnClickListener(this);
        typeJade.setOnClickListener(this);
        typePaint.setOnClickListener(this);
        typeLacquer.setOnClickListener(this);
        typeOthers.setOnClickListener(this);


    }

    private void setData() {


    }

    private void initDatas() {

        if (datas.size() != 0) {
            return;
        }

        pullDataFromServer(0);

    }

    private void initViews() {
        recyclerView = (PullRecyclerView) view.findViewById(R.id.colt_recyclerView);
        recyclerView.setFocusable(false);

        typeBronze = (ImageView) view.findViewById(R.id.museum_fragment_bronze);
        typeChina = (ImageView) view.findViewById(R.id.museum_fragment_china);
        typeJade = (ImageView) view.findViewById(R.id.museum_fragment_jade);
        typePaint = (ImageView) view.findViewById(R.id.museum_fragment_paint);
        typeLacquer = (ImageView) view.findViewById(R.id.museum_fragment_lacquer);
        typeOthers = (ImageView) view.findViewById(R.id.museum_fragment_others);


        adapter = new CollectionGridAdapter(getActivity(), datas, this);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        recyclerView.setCanPullDown(false);
        recyclerView.setCanPullUp(false);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.museum_fragment_bronze:
                theType = Collection.TYPE_BRONSE;
                break;
            case R.id.museum_fragment_china:
                theType = Collection.TYPE_CHINA;
                break;
            case R.id.museum_fragment_jade:
                theType = Collection.TYPE_JADE;
                break;
            case R.id.museum_fragment_lacquer:
                theType = Collection.TYPE_LACQUER;
                break;
            case R.id.museum_fragment_paint:
                theType = Collection.TYPE_PAINT;
                break;
            case R.id.museum_fragment_others:
                theType = Collection.TYPE_OTHERS;
                break;
            default:
                break;

        }

        Intent intent = new Intent(getActivity(), TypeColtActivity.class);
        intent.putExtra(COLT_TYPE, theType);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.none);

    }

    public void pullDataFromServer(final int curPage) {
        BmobColt bmobColt = BmobColt.getInstance(getActivity());
        bmobColt.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {
                List<Collection> list = (List<Collection>) Obj;

                if (list == null || list.size() == 0) {
                    ToastUtils.toast(getActivity(), "没有更多数据啦");

                } else {

                    if (curPage == 0) {
                        datas.clear();
                    }

                    for (Collection collection : list) {
                        datas.add(collection);
                    }
                    adapter.notifyDataSetChanged();

                    currentPage++;
                }
                recyclerView.onFooterRefreshComplete();

            }


            @Override
            public void onFail(Object Obj) {

            }
        });
        bmobColt.getHotColt(curPage);

    }

    @Override
    public void onHeaderRefresh(PullBaseView view) {

    }

    @Override
    public void onFooterRefresh(PullBaseView view) {
        pullDataFromServer(currentPage);
    }

    @Override
    public void onItemClick(int position) {


        Collection collection = (Collection) datas.get(position);
        Intent intent = new Intent(getActivity(), CollectionActivity.class);
        intent.putExtra(COLT_ID, collection.getObjectId());
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.none);
    }

    @Override
    public void onViewClick(int position, int viewtype) {

    }
}
