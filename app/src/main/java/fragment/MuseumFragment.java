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

import BmobUtils.BmobColt;
import MyView.GridItemDecoration;
import adapter.CollectionGridAdapter;
import entity.Collection;
import interfaces.OnBmobReturnWithObj;
import jintong.museum2.R;
import jintong.museum2.TypeColtActivity;

/**
 *
 * 宝库页面
 * Created by wjc on 2017/2/9.
 */
public class MuseumFragment extends Fragment implements View.OnClickListener {
    private View view;
    private RecyclerView recyclerView;
    private List<Collection> collections=new ArrayList<Collection>();
    private CollectionGridAdapter adapter;


    private int theType;

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


        //设置RecycerView的Item间分割线
//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST));


//        recyclerView.addItemDecoration(new GridItemDecoration(10,getActivity()));
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

        if(collections.size()!=0){
            return;
        }
        BmobColt bmobColt=BmobColt.getInstance(getActivity());
        bmobColt.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {
                List<Collection> list= (List<Collection>) Obj;
                collections=list;
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onFail(Object Obj) {

            }
        });
        bmobColt.getHotColt();


//        collections=new ArrayList<Collection>();
//
//            Collection colt = new Collection();
//            colt.setColtLikeNum(100);
//            colt.setColtName("青花粉彩梅瓶");
//            List<String> urls = new ArrayList<>();
//            urls.add("http://bmob-cdn-4183.b0.upaiyun.com/2016/08/03/c4f89b0540d7cf3d80a77d13ca4e04b2.jpg");
//            colt.setColtImageURLs(urls);
//            colt.setColtToMuseumName("A博物馆");
//            collections.add(colt);
//
//        Collection colt2 = new Collection();
//        colt2.setColtLikeNum(100);
//        colt2.setColtName("青花月印梅纹碗");
//        List<String> urls2 = new ArrayList<>();
//        urls2.add("http://bmob-cdn-4183.b0.upaiyun.com/2016/08/03/a220dd564081640d809bf930eef6f732.jpg");
//        colt2.setColtImageURLs(urls2);
//        colt2.setColtToMuseumName("B博物馆");
//        collections.add(colt2);
//
//
//        Collection colt3 = new Collection();
//        colt3.setColtLikeNum(100);
//        colt3.setColtName("粉彩山水纹盘");
//        List<String> urls3 = new ArrayList<>();
//        urls3.add("http://bmob-cdn-4183.b0.upaiyun.com/2016/08/03/cd37af4740e17a4580f1d00cc919a639.jpg");
//        colt3.setColtImageURLs(urls3);
//        colt3.setColtToMuseumName("C博物馆");
//        collections.add(colt3);
//
//        Collection colt4 = new Collection();
//        colt4.setColtLikeNum(100);
//        colt4.setColtName("青花果树纹双管瓶");
//        List<String> urls4 = new ArrayList<>();
//        urls4.add("http://bmob-cdn-4183.b0.upaiyun.com/2016/08/03/50ffdf4140281d96809f8eefdc2a47f6.jpg");
//        colt4.setColtImageURLs(urls4);
//        colt4.setColtToMuseumName("D博物馆");
//        collections.add(colt4);


    }

    private void initViews() {
        recyclerView = (RecyclerView) view.findViewById(R.id.colt_recyclerView);

        typeBronze = (ImageView) view.findViewById(R.id.museum_fragment_bronze);
        typeChina = (ImageView) view.findViewById(R.id.museum_fragment_china);
        typeJade = (ImageView) view.findViewById(R.id.museum_fragment_jade);
        typePaint = (ImageView) view.findViewById(R.id.museum_fragment_paint);
        typeLacquer = (ImageView) view.findViewById(R.id.museum_fragment_lacquer);
        typeOthers = (ImageView) view.findViewById(R.id.museum_fragment_others);



        adapter = new CollectionGridAdapter(getActivity(), collections);
        recyclerView.setAdapter(adapter);
        recyclerView.setFocusable(false);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.museum_fragment_bronze:
                theType=Collection.TYPE_BRONSE;
                break;
            case  R.id.museum_fragment_china:
                theType=Collection.TYPE_CHINA;
                break;
            case R.id.museum_fragment_jade:
                theType=Collection.TYPE_JADE;
                break;
            case R.id.museum_fragment_paint:
                theType=Collection.TYPE_PAINT;
                break;
            case R.id.museum_fragment_lacquer:
                theType=Collection.TYPE_LACQUER;
                break;
            case R.id.museum_fragment_others:
                theType=Collection.TYPE_OTHERS;
                break;
            default:
                break;

        }

        Intent intent=new Intent(getActivity(), TypeColtActivity.class);
        intent.putExtra("type",theType);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.none);

    }
}
